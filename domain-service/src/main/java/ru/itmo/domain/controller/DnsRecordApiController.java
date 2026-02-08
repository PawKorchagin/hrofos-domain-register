package ru.itmo.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.generated.api.DnsRecordApi;
import ru.itmo.domain.generated.model.DnsRecord;
import ru.itmo.domain.generated.model.DnsRecordResponse;
import ru.itmo.domain.service.DnsRecordService;

import java.util.Collections;
import java.util.List;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.openAPIDNS.base-path:/api/v1}")
public class DnsRecordApiController implements DnsRecordApi {

    private final DnsRecordService dnsRecordService;

    public DnsRecordApiController(DnsRecordService dnsRecordService) {
        this.dnsRecordService ${DB_USER:***REMOVED***} dnsRecordService;
    }

    @Override
    public ResponseEntity<DnsRecordResponse> createDnsRecord(String l2Domain, DnsRecord dnsRecord) {
        DnsRecordResponse created ${DB_USER:***REMOVED***} dnsRecordService.create(l2Domain, dnsRecord);
        return ResponseEntity.status(201).body(created);
    }

    @Override
    public ResponseEntity<List<DnsRecordResponse>> getDnsRecords(String l2Domain) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<DnsRecordResponse> getDnsRecordById(String l2Domain, Long id) {
        return ResponseEntity.ok().body(null);
    }

    @Override
    public ResponseEntity<DnsRecordResponse> updateDnsRecordById(String l2Domain, Long id, DnsRecord dnsRecord) {
        return ResponseEntity.ok().body(null);
    }

    @Override
    public ResponseEntity<Void> deleteDnsRecordById(String l2Domain, Long id) {
        return ResponseEntity.noContent().build();
    }
}
