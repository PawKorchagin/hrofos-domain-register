package ru.itmo.domain.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.common.audit.AuditClient;
import ru.itmo.domain.client.NotificationClient;
import ru.itmo.domain.entity.Domain;
import ru.itmo.domain.exception.ForbiddenException;
import ru.itmo.domain.exception.L2DomainNotFoundException;
import ru.itmo.domain.generated.model.CreateUserDomainsRequest;
import ru.itmo.domain.generated.model.DomainPeriod;
import ru.itmo.domain.generated.model.RenewUserDomainsRequest;
import ru.itmo.domain.repository.DomainRepository;
import ru.itmo.domain.service.UserDomainService;
import ru.itmo.domain.util.SecurityUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserDomainServiceImpl implements UserDomainService {

    private static final DateTimeFormatter DATE_FMT ${DB_USER:***REMOVED***} DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final DomainRepository domainRepository;
    private final AuditClient auditClient;
    private final NotificationClient notificationClient;

    public UserDomainServiceImpl(DomainRepository domainRepository, AuditClient auditClient, NotificationClient notificationClient) {
        this.domainRepository ${DB_USER:***REMOVED***} domainRepository;
        this.auditClient ${DB_USER:***REMOVED***} auditClient;
        this.notificationClient ${DB_USER:***REMOVED***} notificationClient;
    }

    @Override
    public List<String> getUserDomains() {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new IllegalStateException("User ID not found in security context");
        }

        List<Domain> l3Domains ${DB_USER:***REMOVED***} domainRepository.findByUserIdAndParentIsNotNull(userId);
        List<String> result ${DB_USER:***REMOVED***} new ArrayList<>();

        for (Domain l3Domain : l3Domains) {
            Domain l2Domain ${DB_USER:***REMOVED***} l3Domain.getParent();
            if (l2Domain !${DB_USER:***REMOVED***} null) {
                String fullDomainName ${DB_USER:***REMOVED***} l3Domain.getDomainPart() + "." + l2Domain.getDomainPart();
                result.add(fullDomainName);
            }
        }

        return result;
    }

    @Override
    @Transactional
    public List<String> createUserDomains(CreateUserDomainsRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new IllegalStateException("User ID not found in security context");
        }

        DomainPeriod period ${DB_USER:***REMOVED***} request.getPeriod();
        LocalDateTime now ${DB_USER:***REMOVED***} LocalDateTime.now();
        LocalDateTime finishedAt ${DB_USER:***REMOVED***} calculateFinishedAt(now, period);

        List<String> createdDomains ${DB_USER:***REMOVED***} new ArrayList<>();

        for (String l3Domain : request.getL3Domains()) {
            String l3Name ${DB_USER:***REMOVED***} l3Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l3Domain.trim();
            if (l3Name ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || l3Name.isBlank()) {
                continue;
            }

            int firstDot ${DB_USER:***REMOVED***} l3Name.indexOf('.');
            if (firstDot <${DB_USER:***REMOVED***} 0 || firstDot ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} l3Name.length() - 1) {
                continue;
            }

            String l3Part ${DB_USER:***REMOVED***} l3Name.substring(0, firstDot);
            String l2Name ${DB_USER:***REMOVED***} l3Name.substring(firstDot + 1);

            Domain l2 ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(l2Name)
                    .orElseThrow(() -> new L2DomainNotFoundException(l2Name));

            // Create L3 domain if it doesn't exist
            Domain l3 ${DB_USER:***REMOVED***} domainRepository.findByParentIdAndDomainPart(l2.getId(), l3Part)
                    .orElseGet(() -> {
                        Domain child ${DB_USER:***REMOVED***} new Domain();
                        child.setDomainPart(l3Part);
                        child.setParent(l2);
                        child.setDomainVersion(1L);
                        child.setUserId(userId);
                        child.setActivatedAt(now);
                        child.setFinishedAt(finishedAt);
                        return domainRepository.save(child);
                    });

            // If domain already exists but doesn't have userId set, update it
            if (l3.getUserId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
                l3.setUserId(userId);
                l3.setActivatedAt(now);
                l3.setFinishedAt(finishedAt);
                domainRepository.save(l3);
            }

            createdDomains.add(l3Name);
        }

        auditClient.log("Created " + createdDomains.size() + " domains (period${DB_USER:***REMOVED***}" + period + "): " + String.join(", ", createdDomains), userId);

        // Отправляем одно email-уведомление со всеми созданными доменами
        if (!createdDomains.isEmpty()) {
            notificationClient.sendDomainsActivated(userId, createdDomains, finishedAt.format(DATE_FMT));
        }

        return createdDomains;
    }

    @Override
    @Transactional
    public List<String> renewUserDomains(RenewUserDomainsRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new IllegalStateException("User ID not found in security context");
        }

        DomainPeriod period ${DB_USER:***REMOVED***} request.getPeriod();
        List<String> renewedDomains ${DB_USER:***REMOVED***} new ArrayList<>();
        java.util.LinkedHashMap<String, String> domainsWithExpiry ${DB_USER:***REMOVED***} new java.util.LinkedHashMap<>();

        for (String l3Domain : request.getL3Domains()) {
            String l3Name ${DB_USER:***REMOVED***} l3Domain ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l3Domain.trim();
            if (l3Name ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || l3Name.isBlank()) {
                continue;
            }

            int firstDot ${DB_USER:***REMOVED***} l3Name.indexOf('.');
            if (firstDot <${DB_USER:***REMOVED***} 0 || firstDot ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} l3Name.length() - 1) {
                continue;
            }

            String l3Part ${DB_USER:***REMOVED***} l3Name.substring(0, firstDot);
            String l2Name ${DB_USER:***REMOVED***} l3Name.substring(firstDot + 1);

            Domain l2 ${DB_USER:***REMOVED***} domainRepository.findByDomainPartAndParentIsNull(l2Name)
                    .orElseThrow(() -> new L2DomainNotFoundException(l2Name));

            Domain l3 ${DB_USER:***REMOVED***} domainRepository.findByParentIdAndDomainPart(l2.getId(), l3Part)
                    .orElseThrow(() -> new IllegalArgumentException("Domain not found: " + l3Name));

            // Check ownership
            if (!SecurityUtil.isAdmin()) {
                if (l3.getUserId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !l3.getUserId().equals(userId)) {
                    throw new ForbiddenException("You can only renew your own domains");
                }
            }

            // Extend finished_at from current finished_at (or from now if already expired)
            LocalDateTime baseDate ${DB_USER:***REMOVED***} l3.getFinishedAt();
            if (baseDate ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || baseDate.isBefore(LocalDateTime.now())) {
                baseDate ${DB_USER:***REMOVED***} LocalDateTime.now();
            }
            LocalDateTime newFinishedAt ${DB_USER:***REMOVED***} calculateFinishedAt(baseDate, period);
            l3.setFinishedAt(newFinishedAt);
            domainRepository.save(l3);

            renewedDomains.add(l3Name);
            domainsWithExpiry.put(l3Name, newFinishedAt.format(DATE_FMT));
        }

        auditClient.log("Renewed " + renewedDomains.size() + " domains (period${DB_USER:***REMOVED***}" + period + "): " + String.join(", ", renewedDomains), userId);

        if (!domainsWithExpiry.isEmpty()) {
            notificationClient.sendDomainsRenewed(userId, domainsWithExpiry);
        }

        return renewedDomains;
    }

    @Override
    @Transactional
    public long deleteExpiredDomains() {
        List<Domain> expired ${DB_USER:***REMOVED***} domainRepository.findByParentIsNotNullAndFinishedAtBefore(LocalDateTime.now());
        long count ${DB_USER:***REMOVED***} expired.size();
        domainRepository.deleteAll(expired);
        auditClient.log("Deleted " + count + " expired domains");
        return count;
    }

    private LocalDateTime calculateFinishedAt(LocalDateTime from, DomainPeriod period) {
        return switch (period) {
            case MONTH -> from.plusMonths(1);
            case YEAR -> from.plusYears(1);
        };
    }
}
