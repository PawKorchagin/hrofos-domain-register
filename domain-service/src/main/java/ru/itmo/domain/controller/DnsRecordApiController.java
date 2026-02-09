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
    public ResponseEntity<DnsRecordResponse> getDnsRecordById(Long id) {
        return ResponseEntity.ok(dnsRecordService.getById(id));
    }

    @Override
    public ResponseEntity<DnsRecordResponse> updateDnsRecordById(Long id, DnsRecord dnsRecord) {
        return ResponseEntity.ok(dnsRecordService.updateById(id, dnsRecord));
    }

    @Override
    public ResponseEntity<Void> deleteDnsRecordById(Long id) {
        dnsRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
