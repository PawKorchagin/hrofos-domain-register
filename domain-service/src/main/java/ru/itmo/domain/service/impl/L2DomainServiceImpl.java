package ru.itmo.domain.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.domain.entity.Domain;
import ru.itmo.domain.exception.DuplicateL2DomainException;
import ru.itmo.domain.generated.model.L2Domain;
import ru.itmo.domain.repository.DomainRepository;
import ru.itmo.domain.service.L2DomainService;

@Service
public class L2DomainServiceImpl implements L2DomainService {

    private final DomainRepository domainRepository;

    public L2DomainServiceImpl(DomainRepository domainRepository) {
        this.domainRepository ${DB_USER:***REMOVED***} domainRepository;
    }

    @Override
    @Transactional
    public L2Domain create(L2Domain l2Domain) {
        String name ${DB_USER:***REMOVED***} l2Domain.getName() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null ? null : l2Domain.getName().trim();
        if (domainRepository.existsByDomainPartAndParentIsNull(name)) {
            throw new DuplicateL2DomainException(name);
        }
        Domain entity ${DB_USER:***REMOVED***} new Domain();
        entity.setDomainPart(name);
        entity.setParent(null);
        entity ${DB_USER:***REMOVED***} domainRepository.save(entity);
        return new L2Domain().name(entity.getDomainPart());
    }
}
