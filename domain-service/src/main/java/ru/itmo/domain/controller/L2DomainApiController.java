package ru.itmo.domain.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.generated.api.L2DomainApi;
import ru.itmo.domain.generated.model.L2Domain;
import ru.itmo.domain.service.L2DomainService;

import java.util.List;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.openAPIDNS.base-path:/api/v1}")
public class L2DomainApiController implements L2DomainApi {

    private static final Logger log ${DB_USER:***REMOVED***} LoggerFactory.getLogger(L2DomainApiController.class);

    private final L2DomainService l2DomainService;

    public L2DomainApiController(L2DomainService l2DomainService) {
        this.l2DomainService ${DB_USER:***REMOVED***} l2DomainService;
    }

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
}
