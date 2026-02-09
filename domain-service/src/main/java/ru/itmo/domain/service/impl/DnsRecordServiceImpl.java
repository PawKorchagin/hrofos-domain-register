package ru.itmo.domain.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.domain.client.ExdnsClient;
import ru.itmo.domain.entity.DnsRecord;
import ru.itmo.domain.entity.Domain;
import ru.itmo.domain.exception.DnsRecordNameMismatchException;
import ru.itmo.domain.exception.DnsRecordNotFoundException;
import ru.itmo.domain.exception.ForbiddenException;
import ru.itmo.domain.exception.ForbiddenWordException;
import ru.itmo.domain.exception.L2DomainNotFoundException;
import ru.itmo.domain.exception.L3DomainNotFoundException;
import ru.itmo.domain.generated.model.DnsRecordResponse;
import ru.itmo.domain.repository.BadWordRepository;
import ru.itmo.domain.repository.DomainRepository;
import ru.itmo.domain.repository.DnsRecordRepository;
import ru.itmo.domain.service.DnsRecordService;
import ru.itmo.domain.util.SecurityUtil;

import java.util.UUID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DnsRecordServiceImpl implements DnsRecordService {

    private static final Logger log ${DB_USER:***REMOVED***} LoggerFactory.getLogger(DnsRecordServiceImpl.class);

    private static final Map<String, String> RECORD_TYPE_TO_RESPONSE_TYPE ${DB_USER:***REMOVED***} Map.of(
            "DnsRecordA", "DnsRecordResponseA",
            "DnsRecordAAAA", "DnsRecordResponseAAAA",
            "DnsRecordCNAME", "DnsRecordResponseCNAME",
            "DnsRecordMX", "DnsRecordResponseMX",
            "DnsRecordNS", "DnsRecordResponseNS",
            "DnsRecordSOA", "DnsRecordResponseSOA",
            "DnsRecordTXT", "DnsRecordResponseTXT"
    );

    private final DomainRepository domainRepository;
    private final DnsRecordRepository dnsRecordRepository;
    private final BadWordRepository badWordRepository;
    private final ExdnsClient exdnsClient;
    private final ObjectMapper objectMapper;

    public DnsRecordServiceImpl(DomainRepository domainRepository,
                                DnsRecordRepository dnsRecordRepository,
                                BadWordRepository badWordRepository,
                                ExdnsClient exdnsClient,
                                ObjectMapper objectMapper) {
        this.domainRepository ${DB_USER:***REMOVED***} domainRepository;
        this.dnsRecordRepository ${DB_USER:***REMOVED***} dnsRecordRepository;
        this.badWordRepository ${DB_USER:***REMOVED***} badWordRepository;
        this.exdnsClient ${DB_USER:***REMOVED***} exdnsClient;
        this.objectMapper ${DB_USER:***REMOVED***} objectMapper;
    }

    @Override
    @Transactional
    public DnsRecordResponse create(String l2Domain, ru.itmo.domain.generated.model.DnsRecord dnsRecord) {
        String name ${DB_USER:***REMOVED***} l2Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l2Domain.trim();
        JsonNode tree ${DB_USER:***REMOVED***} objectMapper.valueToTree(dnsRecord);
        String bodyName ${DB_USER:***REMOVED***} tree.has("name") && !tree.get("name").isNull() ? tree.get("name").asText().trim() : null;
        String typeStr ${DB_USER:***REMOVED***} tree.has("type") && !tree.get("type").isNull() ? tree.get("type").asText() : null;
        boolean isNs ${DB_USER:***REMOVED***} "DnsRecordNS".equals(typeStr) || "NS".equals(typeStr);

        if (bodyName ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || bodyName.isBlank()) {
            throw new DnsRecordNameMismatchException(name, bodyName);
        }
        boolean nameOk ${DB_USER:***REMOVED***} name.equals(bodyName)
                || (isNs && bodyName.endsWith("." + name) && bodyName.length() > name.length() + 1
                && !bodyName.substring(0, bodyName.length() - name.length() - 1).contains("."));
        if (!nameOk) {
            throw new DnsRecordNameMismatchException(name, bodyName);
        }

        Domain domain ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(name)
                .orElseThrow(() -> new L2DomainNotFoundException(name));

        String recordData ${DB_USER:***REMOVED***} tree.toString();

        DnsRecord entity ${DB_USER:***REMOVED***} new DnsRecord();
        entity.setRecordData(recordData);
        entity.setDomain(domain);
        entity ${DB_USER:***REMOVED***} dnsRecordRepository.save(entity);

        syncZoneToExdns(name);

        if (isNs) {
            log.debug("NS record: processing data field for L3 zone and domain, l2Domain${DB_USER:***REMOVED***}{}", name);
            String dataValue ${DB_USER:***REMOVED***} tree.has("data") && !tree.get("data").isNull() ? tree.get("data").asText().trim() : null;
            log.debug("NS record: data value from body ${DB_USER:***REMOVED***} {}", dataValue);
            if (dataValue !${DB_USER:***REMOVED***} null && !dataValue.isBlank()
                    && dataValue.endsWith("." + name) && dataValue.length() > name.length() + 1
                    && !dataValue.substring(0, dataValue.length() - name.length() - 1).contains(".")) {
                String l3Part ${DB_USER:***REMOVED***} dataValue.substring(0, dataValue.length() - name.length() - 1);
                log.debug("NS record: L3 part extracted ${DB_USER:***REMOVED***} {}, creating or finding domain in table domain", l3Part);
                Domain l3 ${DB_USER:***REMOVED***} domainRepository.findByParentIdAndDomainPart(domain.getId(), l3Part)
                        .orElseGet(() -> {
                            log.debug("NS record: L3 domain not found, creating new Domain parentId${DB_USER:***REMOVED***}{}, domainPart${DB_USER:***REMOVED***}{}", domain.getId(), l3Part);
                            Domain child ${DB_USER:***REMOVED***} new Domain();
                            child.setDomainPart(l3Part);
                            child.setParent(domain);
                            child.setDomainVersion(1L);
                            Domain saved ${DB_USER:***REMOVED***} domainRepository.save(child);
                            log.debug("NS record: L3 domain saved with id${DB_USER:***REMOVED***}{}", saved.getId());
                            return saved;
                        });
                log.debug("NS record: L3 domain id${DB_USER:***REMOVED***}{} for {}", l3.getId(), dataValue);

                log.debug("NS record: building zone body for exdns name${DB_USER:***REMOVED***}{}, version${DB_USER:***REMOVED***}1, records${DB_USER:***REMOVED***}[]", dataValue);
                ObjectNode zoneBody ${DB_USER:***REMOVED***} objectMapper.createObjectNode();
                zoneBody.put("name", dataValue);
                zoneBody.put("version", 1);
                zoneBody.set("records", objectMapper.createArrayNode());
                log.debug("NS record: calling exdns createZone for zone name${DB_USER:***REMOVED***}{}", dataValue);
                exdnsClient.createZone(dataValue, zoneBody);
                log.debug("NS record: exdns createZone completed for {}", dataValue);
            } else {
                log.debug("NS record: data validation failed or skipped (dataValue null/blank or not a single-label subdomain of {}), skipping L3 creation", name);
            }
        }

        return toDnsRecordResponse(recordData, entity.getId());
    }

    @Override
    @Transactional
    public DnsRecordResponse createL3Domain(String l3Domain, ru.itmo.domain.generated.model.DnsRecord dnsRecord) {
        String l3Name ${DB_USER:***REMOVED***} l3Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l3Domain.trim();
        int firstDot ${DB_USER:***REMOVED***} l3Name ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? -1 : l3Name.indexOf('.');
        if (firstDot <${DB_USER:***REMOVED***} 0 || firstDot ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} l3Name.length() - 1) {
            throw new DnsRecordNameMismatchException(l3Name, l3Name);
        }
        String l3Part ${DB_USER:***REMOVED***} l3Name.substring(0, firstDot);
        String l2Name ${DB_USER:***REMOVED***} l3Name.substring(firstDot + 1);

        JsonNode tree ${DB_USER:***REMOVED***} objectMapper.valueToTree(dnsRecord);
        String bodyName ${DB_USER:***REMOVED***} tree.has("name") && !tree.get("name").isNull() ? tree.get("name").asText().trim() : null;
        if (bodyName ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !l3Name.equals(bodyName)) {
            throw new DnsRecordNameMismatchException(l3Name, bodyName);
        }

        Domain l2 ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(l2Name)
                .orElseThrow(() -> new L2DomainNotFoundException(l2Name));

        UUID currentUserId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        Domain l3 ${DB_USER:***REMOVED***} domainRepository.findByParentIdAndDomainPart(l2.getId(), l3Part)
                .orElseGet(() -> {
                    Domain child ${DB_USER:***REMOVED***} new Domain();
                    child.setDomainPart(l3Part);
                    child.setParent(l2);
                    child.setDomainVersion(1L);
                    // Set userId for user-created L3 domains
                    if (currentUserId !${DB_USER:***REMOVED***} null && !SecurityUtil.isAdmin()) {
                        child.setUserId(currentUserId);
                    }
                    return domainRepository.save(child);
                });

        String recordData ${DB_USER:***REMOVED***} tree.toString();
        DnsRecord entity ${DB_USER:***REMOVED***} new DnsRecord();
        entity.setRecordData(recordData);
        entity.setDomain(l3);
        entity ${DB_USER:***REMOVED***} dnsRecordRepository.save(entity);

        syncZoneToExdns(l2Name);
        return toDnsRecordResponse(recordData, entity.getId());
    }

    @Override
    @Transactional
    public DnsRecordResponse createDnsRecordForL3(String l3Domain, ru.itmo.domain.generated.model.DnsRecord dnsRecord) {
        String l3Name ${DB_USER:***REMOVED***} l3Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l3Domain.trim();
        int firstDot ${DB_USER:***REMOVED***} l3Name ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? -1 : l3Name.indexOf('.');
        if (firstDot <${DB_USER:***REMOVED***} 0 || firstDot ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} l3Name.length() - 1) {
            throw new DnsRecordNameMismatchException(l3Name, l3Name);
        }
        String l3Part ${DB_USER:***REMOVED***} l3Name.substring(0, firstDot);
        String l2Name ${DB_USER:***REMOVED***} l3Name.substring(firstDot + 1);

        JsonNode tree ${DB_USER:***REMOVED***} objectMapper.valueToTree(dnsRecord);
        String bodyName ${DB_USER:***REMOVED***} tree.has("name") && !tree.get("name").isNull() ? tree.get("name").asText().trim() : null;
        if (bodyName ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !l3Name.equals(bodyName)) {
            throw new DnsRecordNameMismatchException(l3Name, bodyName);
        }

        Domain l2 ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(l2Name)
                .orElseThrow(() -> new L2DomainNotFoundException(l2Name));
        Domain l3 ${DB_USER:***REMOVED***} domainRepository.findByParentIdAndDomainPart(l2.getId(), l3Part)
                .orElseThrow(() -> new L3DomainNotFoundException(l3Name));

        String recordData ${DB_USER:***REMOVED***} tree.toString();
        DnsRecord entity ${DB_USER:***REMOVED***} new DnsRecord();
        entity.setRecordData(recordData);
        entity.setDomain(l3);
        entity ${DB_USER:***REMOVED***} dnsRecordRepository.save(entity);

        syncL3ZoneToExdns(l3Name);
        return toDnsRecordResponse(recordData, entity.getId());
    }

    @Override
    public List<String> getFreeL3Domains(String name) {
        String l3Part ${DB_USER:***REMOVED***} name ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : name.trim();
        if (l3Part ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || l3Part.isBlank()) {
            return List.of();
        }
        
        // Check if the domain name contains forbidden words
        if (badWordRepository.existsByWordIgnoreCase(l3Part)) {
            throw new ForbiddenWordException(l3Part);
        }
        
        return domainRepository.findAllByParentIsNull().stream()
                .filter(l2 -> !domainRepository.existsByParentIdAndDomainPart(l2.getId(), l3Part))
                .map(l2 -> l3Part + "." + l2.getDomainPart())
                .collect(Collectors.toList());
    }

    @Override
    public List<DnsRecordResponse> getDnsRecords(String l2Domain) {
        String name ${DB_USER:***REMOVED***} l2Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l2Domain.trim();
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(name)
                .orElseThrow(() -> new L2DomainNotFoundException(name));
        return dnsRecordRepository.findByDomainId(domain.getId()).stream()
                .filter(rec -> rec.getRecordData() !${DB_USER:***REMOVED***} null && !rec.getRecordData().isBlank())
                .map(rec -> toDnsRecordResponse(rec.getRecordData(), rec.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DnsRecordResponse> getL3DomainDnsRecords(String l3Domain) {
        String l3Name ${DB_USER:***REMOVED***} l3Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l3Domain.trim();
        int firstDot ${DB_USER:***REMOVED***} l3Name ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? -1 : l3Name.indexOf('.');
        if (firstDot <${DB_USER:***REMOVED***} 0 || firstDot ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} l3Name.length() - 1) {
            throw new L3DomainNotFoundException(l3Name);
        }
        String l3Part ${DB_USER:***REMOVED***} l3Name.substring(0, firstDot);
        String l2Name ${DB_USER:***REMOVED***} l3Name.substring(firstDot + 1);

        Domain l2 ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(l2Name)
                .orElseThrow(() -> new L2DomainNotFoundException(l2Name));
        Domain l3 ${DB_USER:***REMOVED***} domainRepository.findByParentIdAndDomainPart(l2.getId(), l3Part)
                .orElseThrow(() -> new L3DomainNotFoundException(l3Name));

        // Check permissions: user can only view DNS records for their own domains, admin can view all
        UUID currentUserId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (!SecurityUtil.isAdmin()) {
            if (l3.getUserId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !l3.getUserId().equals(currentUserId)) {
                throw new ForbiddenException("You can only view DNS records for your own domains");
            }
        }

        return dnsRecordRepository.findByDomainId(l3.getId()).stream()
                .filter(rec -> rec.getRecordData() !${DB_USER:***REMOVED***} null && !rec.getRecordData().isBlank())
                .map(rec -> toDnsRecordResponse(rec.getRecordData(), rec.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public DnsRecordResponse getById(Long id) {
        DnsRecord entity ${DB_USER:***REMOVED***} dnsRecordRepository.findById(id)
                .orElseThrow(() -> new DnsRecordNotFoundException(id));
        String recordData ${DB_USER:***REMOVED***} entity.getRecordData();
        if (recordData ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || recordData.isBlank()) {
            throw new DnsRecordNotFoundException(id);
        }
        return toDnsRecordResponse(recordData, entity.getId());
    }

    @Override
    @Transactional
    public DnsRecordResponse updateById(Long id, ru.itmo.domain.generated.model.DnsRecord dnsRecord) {
        DnsRecord entity ${DB_USER:***REMOVED***} dnsRecordRepository.findById(id)
                .orElseThrow(() -> new DnsRecordNotFoundException(id));
        Domain domain ${DB_USER:***REMOVED***} entity.getDomain();
        if (domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new DnsRecordNotFoundException(id);
        }
        
        // Check permissions: user can only modify their own domains, admin can modify all
        UUID currentUserId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (!SecurityUtil.isAdmin()) {
            // For L3 domains, check the L3 domain's userId; for L2 domains, check the L2 domain's userId
            Domain domainToCheck ${DB_USER:***REMOVED***} domain.getParent() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? domain : domain;
            if (domainToCheck.getUserId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !domainToCheck.getUserId().equals(currentUserId)) {
                throw new ForbiddenException("You can only modify your own domains");
            }
        }
        
        String expectedName ${DB_USER:***REMOVED***} getFullDomainName(domain);
        JsonNode tree ${DB_USER:***REMOVED***} objectMapper.valueToTree(dnsRecord);
        String bodyName ${DB_USER:***REMOVED***} tree.has("name") && !tree.get("name").isNull() ? tree.get("name").asText().trim() : null;
        if (bodyName ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !expectedName.equals(bodyName)) {
            throw new DnsRecordNameMismatchException(expectedName, bodyName);
        }
        String recordData ${DB_USER:***REMOVED***} tree.toString();
        entity.setRecordData(recordData);
        entity ${DB_USER:***REMOVED***} dnsRecordRepository.save(entity);
        if (domain.getParent() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            syncZoneToExdns(domain.getDomainPart());
        } else {
            syncL3ZoneToExdns(getFullDomainName(domain));
        }
        return toDnsRecordResponse(recordData, entity.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        DnsRecord entity ${DB_USER:***REMOVED***} dnsRecordRepository.findById(id)
                .orElseThrow(() -> new DnsRecordNotFoundException(id));
        Domain domain ${DB_USER:***REMOVED***} entity.getDomain();
        if (domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new DnsRecordNotFoundException(id);
        }
        
        // Check permissions: user can only delete their own domains, admin can delete all
        UUID currentUserId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (!SecurityUtil.isAdmin()) {
            // For L3 domains, check the L3 domain's userId; for L2 domains, check the L2 domain's userId
            Domain domainToCheck ${DB_USER:***REMOVED***} domain.getParent() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? domain : domain;
            if (domainToCheck.getUserId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !domainToCheck.getUserId().equals(currentUserId)) {
                throw new ForbiddenException("You can only delete your own domains");
            }
        }
        
        dnsRecordRepository.delete(entity);
        if (domain.getParent() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            syncZoneToExdns(domain.getDomainPart());
        } else {
            syncL3ZoneToExdns(getFullDomainName(domain));
        }
    }

    @Override
    @Transactional
    public void syncZoneToExdns(String l2Domain) {
        String name ${DB_USER:***REMOVED***} l2Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l2Domain.trim();
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(name)
                .orElseThrow(() -> new L2DomainNotFoundException(name));

        long currentVersion;
        try {
            JsonNode existingZone ${DB_USER:***REMOVED***} exdnsClient.getZoneBody(name);
            currentVersion ${DB_USER:***REMOVED***} existingZone !${DB_USER:***REMOVED***} null && existingZone.has("version")
                    ? existingZone.get("version").asLong()
                    : 1L;
        } catch (ru.itmo.domain.client.ExdnsClientException e) {
            currentVersion ${DB_USER:***REMOVED***} domain.getDomainVersion() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? 1L : domain.getDomainVersion();
        }

        List<DnsRecord> records ${DB_USER:***REMOVED***} dnsRecordRepository.findByDomainId(domain.getId());
        ArrayNode recordsArray ${DB_USER:***REMOVED***} objectMapper.createArrayNode();
        for (DnsRecord rec : records) {
            if (rec.getRecordData() !${DB_USER:***REMOVED***} null && !rec.getRecordData().isBlank()) {
                try {
                    JsonNode node ${DB_USER:***REMOVED***} objectMapper.readTree(rec.getRecordData());
                    recordsArray.add(node);
                } catch (Exception ignored) {
                }
            }
        }

        ObjectNode zoneBody ${DB_USER:***REMOVED***} objectMapper.createObjectNode();
        zoneBody.put("name", domain.getDomainPart());
        zoneBody.put("version", currentVersion);
        zoneBody.set("records", recordsArray);

        exdnsClient.replaceZone(name, zoneBody);

        domain.setDomainVersion(currentVersion + 1);
        domainRepository.save(domain);
    }

    @Transactional
    public void syncL3ZoneToExdns(String l3DomainName) {
        String name ${DB_USER:***REMOVED***} l3DomainName ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l3DomainName.trim();
        int firstDot ${DB_USER:***REMOVED***} name ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? -1 : name.indexOf('.');
        if (firstDot <${DB_USER:***REMOVED***} 0 || firstDot ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} name.length() - 1) {
            throw new L3DomainNotFoundException(name);
        }
        String l3Part ${DB_USER:***REMOVED***} name.substring(0, firstDot);
        String l2Name ${DB_USER:***REMOVED***} name.substring(firstDot + 1);
        Domain l2 ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(l2Name)
                .orElseThrow(() -> new L2DomainNotFoundException(l2Name));
        Domain l3 ${DB_USER:***REMOVED***} domainRepository.findByParentIdAndDomainPart(l2.getId(), l3Part)
                .orElseThrow(() -> new L3DomainNotFoundException(name));

        long currentVersion;
        try {
            JsonNode existingZone ${DB_USER:***REMOVED***} exdnsClient.getZoneBody(name);
            currentVersion ${DB_USER:***REMOVED***} existingZone !${DB_USER:***REMOVED***} null && existingZone.has("version")
                    ? existingZone.get("version").asLong()
                    : 1L;
        } catch (ru.itmo.domain.client.ExdnsClientException e) {
            currentVersion ${DB_USER:***REMOVED***} l3.getDomainVersion() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? 1L : l3.getDomainVersion();
        }

        List<DnsRecord> records ${DB_USER:***REMOVED***} dnsRecordRepository.findByDomainId(l3.getId());
        ArrayNode recordsArray ${DB_USER:***REMOVED***} objectMapper.createArrayNode();
        for (DnsRecord rec : records) {
            if (rec.getRecordData() !${DB_USER:***REMOVED***} null && !rec.getRecordData().isBlank()) {
                try {
                    JsonNode node ${DB_USER:***REMOVED***} objectMapper.readTree(rec.getRecordData());
                    recordsArray.add(node);
                } catch (Exception ignored) {
                }
            }
        }

        ObjectNode zoneBody ${DB_USER:***REMOVED***} objectMapper.createObjectNode();
        zoneBody.put("name", name);
        zoneBody.put("version", currentVersion);
        zoneBody.set("records", recordsArray);

        exdnsClient.replaceZone(name, zoneBody);

        l3.setDomainVersion(currentVersion + 1);
        domainRepository.save(l3);
    }

    private static String getL2DomainName(Domain domain) {
        Domain root ${DB_USER:***REMOVED***} domain;
        while (root.getParent() !${DB_USER:***REMOVED***} null) {
            root ${DB_USER:***REMOVED***} root.getParent();
        }
        return root.getDomainPart();
    }

    /** Full DNS name for the domain: L2 ${DB_USER:***REMOVED***} domainPart, L3 ${DB_USER:***REMOVED***} domainPart + "." + L2 name, etc. */
    private static String getFullDomainName(Domain domain) {
        if (domain.getParent() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return domain.getDomainPart();
        }
        return domain.getDomainPart() + "." + getL2DomainName(domain);
    }

    private DnsRecordResponse toDnsRecordResponse(String recordData, Long id) {
        try {
            JsonNode node ${DB_USER:***REMOVED***} objectMapper.readTree(recordData);
            ObjectNode obj ${DB_USER:***REMOVED***} (ObjectNode) node;
            obj.put("id", id);
            String typeName ${DB_USER:***REMOVED***} node.has("type") ? node.get("type").asText() : null;
            if (typeName !${DB_USER:***REMOVED***} null && RECORD_TYPE_TO_RESPONSE_TYPE.containsKey(typeName)) {
                obj.put("type", RECORD_TYPE_TO_RESPONSE_TYPE.get(typeName));
            }
            return objectMapper.treeToValue(node, DnsRecordResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
