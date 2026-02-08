package ru.itmo.domain.service;

import ru.itmo.domain.generated.model.L2Domain;

import java.util.List;

public interface L2DomainService {

    List<L2Domain> getL2Domains();

    L2Domain create(L2Domain l2Domain);

    void deleteByName(String l2Domain);
}
