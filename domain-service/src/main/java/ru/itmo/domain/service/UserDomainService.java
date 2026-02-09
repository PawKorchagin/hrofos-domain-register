package ru.itmo.domain.service;

import java.util.List;

public interface UserDomainService {

    List<String> getUserDomains();

    List<String> createUserDomains(List<String> l3Domains);
}
