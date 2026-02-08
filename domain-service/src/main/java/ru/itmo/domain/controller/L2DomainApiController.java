package ru.itmo.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.generated.api.L2DomainApi;
import ru.itmo.domain.generated.model.L2Domain;

import java.util.Collections;
import java.util.List;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.openAPIDNS.base-path:/api/v1}")
public class L2DomainApiController implements L2DomainApi {

    @Override
    public ResponseEntity<List<L2Domain>> getL2Domains() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<L2Domain> createL2Domain(L2Domain l2Domain) {
        return ResponseEntity.ok(l2Domain);
    }

    @Override
    public ResponseEntity<Void> deleteL2DomainByName(String l2Domain) {
        return ResponseEntity.noContent().build();
    }
}
