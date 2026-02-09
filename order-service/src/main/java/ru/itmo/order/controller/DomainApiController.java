package ru.itmo.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.order.client.DomainClient;
import ru.itmo.order.generated.api.DomainApi;
import ru.itmo.order.generated.model.RenewDomainsRequest;

import java.util.List;
import java.util.UUID;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.orderService.base-path:/orders}")
public class DomainApiController implements DomainApi {

    private final DomainClient domainClient;
    private final HttpServletRequest httpServletRequest;

    public DomainApiController(DomainClient domainClient, HttpServletRequest httpServletRequest) {
        this.domainClient ${DB_USER:***REMOVED***} domainClient;
        this.httpServletRequest ${DB_USER:***REMOVED***} httpServletRequest;
    }

    @Override
    public ResponseEntity<List<String>> renewDomains(RenewDomainsRequest renewDomainsRequest) {
        Authentication auth ${DB_USER:***REMOVED***} org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !(auth.getPrincipal() instanceof UUID)) {
            return ResponseEntity.status(401).build();
        }

        String authHeader ${DB_USER:***REMOVED***} httpServletRequest.getHeader("Authorization");
        String jwtToken ${DB_USER:***REMOVED***} authHeader !${DB_USER:***REMOVED***} null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
        if (jwtToken ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(401).build();
        }

        String period ${DB_USER:***REMOVED***} renewDomainsRequest.getPeriod().getValue();
        List<String> renewedDomains ${DB_USER:***REMOVED***} domainClient.renewUserDomains(
                renewDomainsRequest.getL3Domains(), period, jwtToken);
        return ResponseEntity.ok(renewedDomains);
    }
}
