package ru.itmo.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.generated.api.StatsApi;
import ru.itmo.domain.generated.model.DomainStats;
import ru.itmo.domain.service.DomainStatsService;

@RestController
@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RequestMapping("${openapi.openAPIDNS.base-path:/domains}")
public class StatsApiController implements StatsApi {

    private final DomainStatsService domainStatsService;

    @Override
    public ResponseEntity<DomainStats> getDomainStats() {
        return ResponseEntity.ok(domainStatsService.getDomainStats());
    }
}
