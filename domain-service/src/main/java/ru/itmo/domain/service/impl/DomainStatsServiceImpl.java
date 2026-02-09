package ru.itmo.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.domain.generated.model.DomainStats;
import ru.itmo.domain.repository.DomainRepository;
import ru.itmo.domain.service.DomainStatsService;

@Service
@RequiredArgsConstructor
public class DomainStatsServiceImpl implements DomainStatsService {

    private final DomainRepository domainRepository;

    @Override
    public DomainStats getDomainStats() {
        long activeUsers ${DB_USER:***REMOVED***} domainRepository.countDistinctUserIds();
        long registeredDomains ${DB_USER:***REMOVED***} domainRepository.countRegisteredDomains();

        DomainStats stats ${DB_USER:***REMOVED***} new DomainStats();
        stats.setActiveUsersCount(activeUsers);
        stats.setRegisteredDomainsCount(registeredDomains);
        return stats;
    }
}
