package ru.itmo.domain.service;

import ru.itmo.domain.generated.model.DnsRecord;
import ru.itmo.domain.generated.model.DnsRecordResponse;

import java.util.List;

public interface DnsRecordService {

    DnsRecordResponse create(String l2Domain, DnsRecord dnsRecord);

    List<DnsRecordResponse> getDnsRecords(String l2Domain);

    DnsRecordResponse getById(Long id);

    DnsRecordResponse updateById(Long id, DnsRecord dnsRecord);

    void deleteById(Long id);

    void syncZoneToExdns(String l2Domain);
}
