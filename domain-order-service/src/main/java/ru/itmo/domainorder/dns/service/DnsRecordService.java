package ru.itmo.domainorder.dns.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.domainorder.dns.client.ExdnsClient;
import ru.itmo.domainorder.dns.dto.*;
import ru.itmo.domainorder.dns.entity.DnsRecord;
import ru.itmo.domainorder.dns.enumeration.DomainRecordType;
import ru.itmo.domainorder.dns.exception.DnsRecordNotFoundException;
import ru.itmo.domainorder.dns.exception.DomainAccessDeniedException;
import ru.itmo.domainorder.dns.repository.DnsRecordRepository;
import ru.itmo.domainorder.domain.entity.Domain;
import ru.itmo.domainorder.domain.repository.DomainMemberRepository;
import ru.itmo.domainorder.domain.repository.DomainRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DnsRecordService {

    private final DnsRecordRepository dnsRecordRepository;
    private final DomainRepository domainRepository;
    private final DomainMemberRepository domainMemberRepository;
    private final ExdnsClient exdnsClient;

    @Transactional(readOnly ${DB_USER:***REMOVED***} true)
    public List<DnsRecordResponse> getRecordsByDomain(UUID domainId, UUID userId) {
        checkDomainAccess(domainId, userId);

        List<DnsRecord> records ${DB_USER:***REMOVED***} dnsRecordRepository.findByDomainId(domainId);
        return records.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly ${DB_USER:***REMOVED***} true)
    public DnsRecordResponse getRecordById(UUID recordId, UUID userId) {
        DnsRecord record ${DB_USER:***REMOVED***} dnsRecordRepository.findById(recordId)
                .orElseThrow(() -> new DnsRecordNotFoundException("DNS record not found"));

        checkDomainAccess(record.getDomainId(), userId);

        return toResponse(record);
    }

    @Transactional
    public DnsRecordResponse createARecord(CreateARecordRequest request, UUID userId) {
        checkDomainAccess(request.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        DnsRecord record ${DB_USER:***REMOVED***} new DnsRecord();
        record.setDomainId(request.getDomainId());
        record.setType(DomainRecordType.A);
        record.setName(request.getName());
        record.setValue(request.getIpv4Address());
        record.setTtl(request.getTtl());

        record ${DB_USER:***REMOVED***} dnsRecordRepository.save(record);
        syncZoneToExdns(domain.getFqdn());

        return toResponse(record);
    }

    @Transactional
    public DnsRecordResponse createAaaaRecord(CreateAaaaRecordRequest request, UUID userId) {
        checkDomainAccess(request.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        DnsRecord record ${DB_USER:***REMOVED***} new DnsRecord();
        record.setDomainId(request.getDomainId());
        record.setType(DomainRecordType.AAAA);
        record.setName(request.getName());
        record.setValue(request.getIpv6Address());
        record.setTtl(request.getTtl());

        record ${DB_USER:***REMOVED***} dnsRecordRepository.save(record);
        syncZoneToExdns(domain.getFqdn());

        return toResponse(record);
    }

    @Transactional
    public DnsRecordResponse createCnameRecord(CreateCnameRecordRequest request, UUID userId) {
        checkDomainAccess(request.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        DnsRecord record ${DB_USER:***REMOVED***} new DnsRecord();
        record.setDomainId(request.getDomainId());
        record.setType(DomainRecordType.CNAME);
        record.setName(request.getName());
        record.setValue(request.getCanonicalName());
        record.setTtl(request.getTtl());

        record ${DB_USER:***REMOVED***} dnsRecordRepository.save(record);
        syncZoneToExdns(domain.getFqdn());

        return toResponse(record);
    }

    @Transactional
    public DnsRecordResponse createTxtRecord(CreateTxtRecordRequest request, UUID userId) {
        checkDomainAccess(request.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        DnsRecord record ${DB_USER:***REMOVED***} new DnsRecord();
        record.setDomainId(request.getDomainId());
        record.setType(DomainRecordType.TXT);
        record.setName(request.getName());
        record.setValue(request.getTextValue());
        record.setTtl(request.getTtl());

        record ${DB_USER:***REMOVED***} dnsRecordRepository.save(record);
        syncZoneToExdns(domain.getFqdn());

        return toResponse(record);
    }

    @Transactional
    public DnsRecordResponse createMxRecord(CreateMxRecordRequest request, UUID userId) {
        checkDomainAccess(request.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        DnsRecord record ${DB_USER:***REMOVED***} new DnsRecord();
        record.setDomainId(request.getDomainId());
        record.setType(DomainRecordType.MX);
        record.setName(request.getName());
        record.setValue(request.getMailExchange());
        record.setTtl(request.getTtl());

        record ${DB_USER:***REMOVED***} dnsRecordRepository.save(record);
        syncZoneToExdns(domain.getFqdn());

        DnsRecordResponse response ${DB_USER:***REMOVED***} toResponse(record);
        response.setPriority(request.getPriority());
        return response;
    }

    @Transactional
    public DnsRecordResponse createSrvRecord(CreateSrvRecordRequest request, UUID userId) {
        checkDomainAccess(request.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        String value ${DB_USER:***REMOVED***} String.format("%d %d %d %s", 
                request.getPriority(), request.getWeight(), request.getPort(), request.getTarget());

        DnsRecord record ${DB_USER:***REMOVED***} new DnsRecord();
        record.setDomainId(request.getDomainId());
        record.setType(DomainRecordType.SRV);
        record.setName(request.getServiceName());
        record.setValue(value);
        record.setTtl(request.getTtl());

        record ${DB_USER:***REMOVED***} dnsRecordRepository.save(record);
        syncZoneToExdns(domain.getFqdn());

        DnsRecordResponse response ${DB_USER:***REMOVED***} toResponse(record);
        response.setPriority(request.getPriority());
        response.setWeight(request.getWeight());
        response.setPort(request.getPort());
        return response;
    }

    @Transactional
    public DnsRecordResponse createCaaRecord(CreateCaaRecordRequest request, UUID userId) {
        checkDomainAccess(request.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        String value ${DB_USER:***REMOVED***} String.format("%s %s %s", 
                request.getFlags(), request.getTag(), request.getValue());

        DnsRecord record ${DB_USER:***REMOVED***} new DnsRecord();
        record.setDomainId(request.getDomainId());
        record.setType(DomainRecordType.CAA);
        record.setName(request.getName());
        record.setValue(value);
        record.setTtl(request.getTtl());

        record ${DB_USER:***REMOVED***} dnsRecordRepository.save(record);
        syncZoneToExdns(domain.getFqdn());

        DnsRecordResponse response ${DB_USER:***REMOVED***} toResponse(record);
        response.setFlags(request.getFlags());
        response.setTag(request.getTag());
        return response;
    }

    @Transactional
    public DnsRecordResponse updateRecord(UUID recordId, UpdateDnsRecordRequest request, UUID userId) {
        DnsRecord record ${DB_USER:***REMOVED***} dnsRecordRepository.findById(recordId)
                .orElseThrow(() -> new DnsRecordNotFoundException("DNS record not found"));

        checkDomainAccess(record.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(record.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        record.setValue(request.getValue());
        record.setTtl(request.getTtl());

        if (request.getPriority() !${DB_USER:***REMOVED***} null) {
            if (record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.MX || record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.SRV) {
                String currentValue ${DB_USER:***REMOVED***} record.getValue();
                if (record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.MX) {
                    String[] parts ${DB_USER:***REMOVED***} currentValue.split(" ", 2);
                    if (parts.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 2) {
                        record.setValue(request.getPriority() + " " + parts[1]);
                    }
                } else if (record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.SRV) {
                    String[] parts ${DB_USER:***REMOVED***} currentValue.split(" ", 4);
                    if (parts.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 4) {
                        record.setValue(String.format("%d %s %s %s", 
                                request.getPriority(), parts[1], parts[2], parts[3]));
                    }
                }
            }
        }

        record ${DB_USER:***REMOVED***} dnsRecordRepository.save(record);
        syncZoneToExdns(domain.getFqdn());

        DnsRecordResponse response ${DB_USER:***REMOVED***} toResponse(record);
        if (request.getPriority() !${DB_USER:***REMOVED***} null) {
            response.setPriority(request.getPriority());
        }
        if (request.getWeight() !${DB_USER:***REMOVED***} null && record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.SRV) {
            response.setWeight(request.getWeight());
        }
        if (request.getPort() !${DB_USER:***REMOVED***} null && record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.SRV) {
            response.setPort(request.getPort());
        }
        if (request.getFlags() !${DB_USER:***REMOVED***} null && record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.CAA) {
            response.setFlags(request.getFlags());
        }
        if (request.getTag() !${DB_USER:***REMOVED***} null && record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.CAA) {
            response.setTag(request.getTag());
        }
        return response;
    }

    @Transactional
    public void deleteRecord(UUID recordId, UUID userId) {
        DnsRecord record ${DB_USER:***REMOVED***} dnsRecordRepository.findById(recordId)
                .orElseThrow(() -> new DnsRecordNotFoundException("DNS record not found"));

        checkDomainAccess(record.getDomainId(), userId);
        Domain domain ${DB_USER:***REMOVED***} domainRepository.findById(record.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        dnsRecordRepository.delete(record);
        syncZoneToExdns(domain.getFqdn());
    }

    private void checkDomainAccess(UUID domainId, UUID userId) {
        if (!domainMemberRepository.existsByDomainIdAndUserId(domainId, userId)) {
            throw new DomainAccessDeniedException("Access denied to domain");
        }
    }

    private void syncZoneToExdns(String fqdn) {
        try {
            Domain domain ${DB_USER:***REMOVED***} domainRepository.findByFqdn(fqdn)
                    .orElseThrow(() -> new RuntimeException("Domain not found"));

            List<DnsRecord> records ${DB_USER:***REMOVED***} dnsRecordRepository.findByDomainId(domain.getId());

            ExdnsClient.ExdnsZone zone ${DB_USER:***REMOVED***} new ExdnsClient.ExdnsZone();
            zone.setName(fqdn);
            zone.setVersion(1);

            ExdnsClient.ExdnsZone existingZone ${DB_USER:***REMOVED***} exdnsClient.getZone(fqdn);
            if (existingZone !${DB_USER:***REMOVED***} null && existingZone.getVersion() !${DB_USER:***REMOVED***} null) {
                zone.setVersion(existingZone.getVersion() + 1);
            }

            List<ExdnsClient.ExdnsRecord> exdnsRecords ${DB_USER:***REMOVED***} new ArrayList<>();
            for (DnsRecord record : records) {
                ExdnsClient.ExdnsRecord exdnsRecord ${DB_USER:***REMOVED***} new ExdnsClient.ExdnsRecord();
                exdnsRecord.setName(record.getName());
                exdnsRecord.setType(record.getType().name());
                exdnsRecord.setTtl(record.getTtl());

                if (record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.MX) {
                    String[] parts ${DB_USER:***REMOVED***} record.getValue().split(" ", 2);
                    if (parts.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 2) {
                        try {
                            Map<String, Object> mxData ${DB_USER:***REMOVED***} new java.util.HashMap<>();
                            mxData.put("preference", Integer.parseInt(parts[0]));
                            mxData.put("exchange", parts[1]);
                            exdnsRecord.setData(mxData);
                        } catch (NumberFormatException e) {
                            exdnsRecord.setData(record.getValue());
                        }
                    } else {
                        exdnsRecord.setData(record.getValue());
                    }
                } else {
                    exdnsRecord.setData(record.getValue());
                }

                exdnsRecords.add(exdnsRecord);
            }

            zone.setRecords(exdnsRecords);
            exdnsClient.createOrUpdateZone(fqdn, zone);
        } catch (Exception e) {
            log.error("Failed to sync zone {} to exdns: {}", fqdn, e.getMessage(), e);
        }
    }

    private DnsRecordResponse toResponse(DnsRecord record) {
        DnsRecordResponse response ${DB_USER:***REMOVED***} DnsRecordResponse.builder()
                .id(record.getId())
                .domainId(record.getDomainId())
                .recordType(record.getType().name())
                .name(record.getName())
                .value(record.getValue())
                .ttl(record.getTtl())
                .build();

        if (record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.MX) {
            String[] parts ${DB_USER:***REMOVED***} record.getValue().split(" ", 2);
            if (parts.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 2) {
                try {
                    response.setPriority(Integer.parseInt(parts[0]));
                    response.setValue(parts[1]);
                } catch (NumberFormatException e) {
                }
            }
        } else if (record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.SRV) {
            String[] parts ${DB_USER:***REMOVED***} record.getValue().split(" ", 4);
            if (parts.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 4) {
                try {
                    response.setPriority(Integer.parseInt(parts[0]));
                    response.setWeight(Integer.parseInt(parts[1]));
                    response.setPort(Integer.parseInt(parts[2]));
                    response.setValue(parts[3]);
                } catch (NumberFormatException e) {
                }
            }
        } else if (record.getType() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} DomainRecordType.CAA) {
            String[] parts ${DB_USER:***REMOVED***} record.getValue().split(" ", 3);
            if (parts.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 3) {
                response.setFlags(parts[0]);
                response.setTag(parts[1]);
                response.setValue(parts[2]);
            }
        }

        return response;
    }
}
