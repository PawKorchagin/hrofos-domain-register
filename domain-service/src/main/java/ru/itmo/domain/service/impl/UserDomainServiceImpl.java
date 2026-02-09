package ru.itmo.domain.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.domain.entity.Domain;
import ru.itmo.domain.exception.L2DomainNotFoundException;
import ru.itmo.domain.repository.DomainRepository;
import ru.itmo.domain.service.UserDomainService;
import ru.itmo.domain.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserDomainServiceImpl implements UserDomainService {

    private final DomainRepository domainRepository;

    public UserDomainServiceImpl(DomainRepository domainRepository) {
        this.domainRepository ${DB_USER:***REMOVED***} domainRepository;
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
    public List<String> createUserDomains(List<String> l3Domains) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new IllegalStateException("User ID not found in security context");
        }
        
        List<String> createdDomains ${DB_USER:***REMOVED***} new ArrayList<>();
        
        for (String l3Domain : l3Domains) {
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
                        return domainRepository.save(child);
                    });
            
            // If domain already exists but doesn't have userId set, update it
            if (l3.getUserId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
                l3.setUserId(userId);
                domainRepository.save(l3);
            }
            
            createdDomains.add(l3Name);
        }
        
        return createdDomains;
    }
}
