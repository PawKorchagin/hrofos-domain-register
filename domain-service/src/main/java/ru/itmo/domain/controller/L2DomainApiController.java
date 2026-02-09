package ru.itmo.domain.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.generated.api.L2DomainApi;
import ru.itmo.domain.generated.model.DnsRecord;
import ru.itmo.domain.generated.model.DnsRecordResponse;
import ru.itmo.domain.generated.model.L2Domain;
import ru.itmo.domain.service.DnsRecordService;
import ru.itmo.domain.service.L2DomainService;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.openAPIDNS.base-path:/api/v1}")
public class L2DomainApiController implements L2DomainApi {

    private static final Logger log ${DB_USER:***REMOVED***} LoggerFactory.getLogger(L2DomainApiController.class);

    private final L2DomainService l2DomainService;
    private final DnsRecordService dnsRecordService;

    @Override
    public ResponseEntity<List<L2Domain>> getL2Domains() {
        return ResponseEntity.ok(l2DomainService.getL2Domains());
    }

    @Override
    public ResponseEntity<L2Domain> createL2Domain(L2Domain l2Domain) {
        log.info("createL2Domain called, name${DB_USER:***REMOVED***}{}", l2Domain !${DB_USER:***REMOVED***} null ? l2Domain.getName() : null);
        L2Domain created ${DB_USER:***REMOVED***} l2DomainService.create(l2Domain);
        return ResponseEntity.ok(created);
    }

    @Override
    public ResponseEntity<Void> deleteL2DomainByName(String l2Domain) {
        l2DomainService.deleteByName(l2Domain);
        return ResponseEntity.noContent().build();
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
}
