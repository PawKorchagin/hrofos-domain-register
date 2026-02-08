package ru.itmo.domain.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.domain.entity.DnsRecord;
import ru.itmo.domain.entity.Domain;
import ru.itmo.domain.exception.L2DomainNotFoundException;
import ru.itmo.domain.generated.model.DnsRecordResponse;
import ru.itmo.domain.repository.DomainRepository;
import ru.itmo.domain.repository.DnsRecordRepository;
import ru.itmo.domain.service.DnsRecordService;

import java.util.Map;

@Service
public class DnsRecordServiceImpl implements DnsRecordService {

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
    private final ObjectMapper objectMapper;

    public DnsRecordServiceImpl(DomainRepository domainRepository,
                                DnsRecordRepository dnsRecordRepository,
                                ObjectMapper objectMapper) {
        this.domainRepository ${DB_USER:***REMOVED***} domainRepository;
        this.dnsRecordRepository ${DB_USER:***REMOVED***} dnsRecordRepository;
        this.objectMapper ${DB_USER:***REMOVED***} objectMapper;
    }

    @Override
    @Transactional
    public DnsRecordResponse create(String l2Domain, ru.itmo.domain.generated.model.DnsRecord dnsRecord) {
        String name ${DB_USER:***REMOVED***} l2Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l2Domain.trim();
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(name)
                .orElseThrow(() -> new L2DomainNotFoundException(name));

        String recordData ${DB_USER:***REMOVED***} objectMapper.valueToTree(dnsRecord).toString();

        DnsRecord entity ${DB_USER:***REMOVED***} new DnsRecord();
        entity.setRecordData(recordData);
        entity.setDomain(domain);
        entity ${DB_USER:***REMOVED***} dnsRecordRepository.save(entity);

        return toDnsRecordResponse(recordData, entity.getId());
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
