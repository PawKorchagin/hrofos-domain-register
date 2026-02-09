package ru.itmo.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.generated.api.L3DomainApi;
import ru.itmo.domain.generated.model.DnsRecord;
import ru.itmo.domain.generated.model.DnsRecordResponse;
import ru.itmo.domain.service.DnsRecordService;

import java.util.List;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.openAPIDNS.base-path:/domains}")
public class L3DomainApiController implements L3DomainApi {

    private final DnsRecordService dnsRecordService;

    public L3DomainApiController(DnsRecordService dnsRecordService) {
        this.dnsRecordService ${DB_USER:***REMOVED***} dnsRecordService;
    }

    @Override
    public ResponseEntity<DnsRecordResponse> createL3Domain(String l3Domain, DnsRecord dnsRecord) {
        return ResponseEntity.status(201).body(dnsRecordService.createL3Domain(l3Domain, dnsRecord));
    }

    @Override
    public ResponseEntity<List<String>> getFreeL3Domains(String name) {
        return ResponseEntity.ok(dnsRecordService.getFreeL3Domains(name));
    }
}
