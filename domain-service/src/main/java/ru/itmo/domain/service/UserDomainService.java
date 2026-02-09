package ru.itmo.domain.service;

import ru.itmo.domain.dto.UserDomainDetailedResponse;
import ru.itmo.domain.generated.model.CreateUserDomainsRequest;
import ru.itmo.domain.generated.model.RenewUserDomainsRequest;

import java.util.List;

public interface UserDomainService {

    List<String> getUserDomains();

    List<UserDomainDetailedResponse> getUserDomainsDetailed();

    List<String> createUserDomains(CreateUserDomainsRequest request);

    List<String> renewUserDomains(RenewUserDomainsRequest request);

    long deleteExpiredDomains();
}
