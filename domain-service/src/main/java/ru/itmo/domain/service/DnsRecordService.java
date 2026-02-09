package ru.itmo.domain.service;

import ru.itmo.domain.generated.model.DnsRecord;
import ru.itmo.domain.generated.model.DnsRecordResponse;

public interface DnsRecordService {

    DnsRecordResponse create(String l2Domain, DnsRecord dnsRecord);

    DnsRecordResponse getById(Long id);

    void syncZoneToExdns(String l2Domain);
}
