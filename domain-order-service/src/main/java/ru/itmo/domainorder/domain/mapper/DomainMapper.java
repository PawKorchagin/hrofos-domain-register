package ru.itmo.domainorder.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.domainorder.domain.entity.Domain;
import ru.itmo.domainorder.domain.dto.CreateDomainRequest;
import ru.itmo.domainorder.domain.dto.DomainResponse;
import ru.itmo.domainorder.domain.dto.UpdateDomainRequest;
import ru.itmo.domainorder.zone.entity.Zone;

@Mapper(componentModel ${DB_USER:***REMOVED***} "spring")
public interface DomainMapper {
    @Mapping(target ${DB_USER:***REMOVED***} "id", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "zone2Id", source ${DB_USER:***REMOVED***} "request.zoneId")
    @Mapping(target ${DB_USER:***REMOVED***} "activatedAt", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "createdAt", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "updatedAt", ignore ${DB_USER:***REMOVED***} true)
    Domain toEntity(CreateDomainRequest request);

    @Mapping(target ${DB_USER:***REMOVED***} "id", source ${DB_USER:***REMOVED***} "domain.id")
    @Mapping(target ${DB_USER:***REMOVED***} "fqdn", source ${DB_USER:***REMOVED***} "domain.fqdn")
    @Mapping(target ${DB_USER:***REMOVED***} "zoneId", source ${DB_USER:***REMOVED***} "domain.zone2Id")
    @Mapping(target ${DB_USER:***REMOVED***} "zoneName", source ${DB_USER:***REMOVED***} "zone.name")
    @Mapping(target ${DB_USER:***REMOVED***} "activatedAt", source ${DB_USER:***REMOVED***} "domain.activatedAt")
    @Mapping(target ${DB_USER:***REMOVED***} "expiresAt", source ${DB_USER:***REMOVED***} "domain.expiresAt")
    @Mapping(target ${DB_USER:***REMOVED***} "createdAt", source ${DB_USER:***REMOVED***} "domain.createdAt")
    @Mapping(target ${DB_USER:***REMOVED***} "updatedAt", source ${DB_USER:***REMOVED***} "domain.updatedAt")
    DomainResponse toResponse(Domain domain, Zone zone);

    @Mapping(target ${DB_USER:***REMOVED***} "id", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "fqdn", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "zone2Id", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "createdAt", ignore ${DB_USER:***REMOVED***} true)
    @Mapping(target ${DB_USER:***REMOVED***} "updatedAt", ignore ${DB_USER:***REMOVED***} true)
    void updateEntity(@MappingTarget Domain domain, UpdateDomainRequest request);
}
