package ru.itmo.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.generated.api.L3DomainApi;
import ru.itmo.domain.generated.model.DnsRecord;
import ru.itmo.domain.generated.model.DnsRecordResponse;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.openAPIDNS.base-path:/api/v1}")
public class L3DomainApiController implements L3DomainApi {

    @Override
    public ResponseEntity<DnsRecordResponse> createL3Domain(String l2Domain, DnsRecord dnsRecord) {
        return ResponseEntity.status(201).body(null);
    }

    @Override
    public ResponseEntity<DnsRecordResponse> getFreeL3Domains(String name) {
        return ResponseEntity.ok().body(null);
    }
}
