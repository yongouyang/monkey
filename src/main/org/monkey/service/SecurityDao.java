package org.monkey.service;

import org.monkey.sample.model.Security;

import java.util.List;

public interface SecurityDao {

    void saveOrUpdate(Security security);

    Security findOne(String ricCode);

    Iterable<Security> findAll();
}
