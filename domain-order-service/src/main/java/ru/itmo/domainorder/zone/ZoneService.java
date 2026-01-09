package ru.itmo.domainorder.zone;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.domainorder.domain.DomainRepository;
import ru.itmo.domainorder.zone.dto.CreateZoneRequest;
import ru.itmo.domainorder.zone.dto.UpdateZoneRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepository zoneRepository;
    private final DomainRepository domainRepository;

    @Transactional(readOnly ${DB_USER:***REMOVED***} true)
    public Page<Zone> getAllZones(Pageable pageable) {
        return zoneRepository.findAll(pageable);
    }

    @Transactional(readOnly ${DB_USER:***REMOVED***} true)
    public Zone getZoneById(UUID id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ZoneNotFoundException("Zone not found with id: " + id));
    }

    @Transactional(readOnly ${DB_USER:***REMOVED***} true)
    public Zone getZoneByName(String name) {
        return zoneRepository.findByName(name)
                .orElseThrow(() -> new ZoneNotFoundException("Zone not found with name: " + name));
    }

    @Transactional
    public Zone createZone(CreateZoneRequest request) {
        if (zoneRepository.existsByName(request.getName())) {
            throw new ZoneAlreadyExistsException("Zone already exists with name: " + request.getName());
        }

        Zone zone ${DB_USER:***REMOVED***} new Zone();
        zone.setName(request.getName());
        zone.setPrice(request.getPrice());
        return zoneRepository.save(zone);
    }

    @Transactional
    public Zone updateZone(UUID id, UpdateZoneRequest request) {
        Zone zone ${DB_USER:***REMOVED***} getZoneById(id);
        zone.setPrice(request.getPrice());
        return zoneRepository.save(zone);
    }

    @Transactional
    public void deleteZone(UUID id) {
        Zone zone ${DB_USER:***REMOVED***} getZoneById(id);
        if (hasDomains(zone.getId())) {
            throw new ZoneHasDomainsException("Cannot delete zone with existing domains");
        }
        zoneRepository.delete(zone);
    }

    private boolean hasDomains(UUID zoneId) {
        return domainRepository.existsByZone2Id(zoneId);
    }
}
