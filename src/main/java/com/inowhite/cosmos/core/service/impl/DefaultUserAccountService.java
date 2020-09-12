/*
 * Cosmos - IT Management and Service Desk System
 * Copyright (C) 2020  Gabriel Ribeiro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inowhite.cosmos.core.service.impl;

import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;
import com.inowhite.cosmos.core.entity.Action;
import com.inowhite.cosmos.core.entity.UserAccount;
import com.inowhite.cosmos.core.repository.UserAccountRepository;
import com.inowhite.cosmos.core.service.UserAccountService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultUserAccountService implements UserAccountService {

  private final UserAccountRepository userAccountRepository;
  private EntityManager entityManager;

  public DefaultUserAccountService(UserAccountRepository userAccountRepository, EntityManager entityManager) {
    this.userAccountRepository = userAccountRepository;
    this.entityManager = entityManager;
  }

  @Override
  public List<UserAccount> list(int page, int size, String query, List<String> ordering) {

    if ((query == null || query.isBlank()) && (ordering == null || ordering.isEmpty())) {
      var pageRequest = PageRequest.of((page - 1), size);
      return userAccountRepository.findActive(pageRequest);
    } else {

      var cb = entityManager.getCriteriaBuilder();
      var cq = cb.createQuery(UserAccount.class);
      var root = cq.from(UserAccount.class);
      cq.select(root);

      if (query != null && !query.isBlank()) {
        var predicate = preparePredicate(query, root);
        cq.where(
          cb.and(
            predicate,
            cb.equal(root.get("enabled"), true)
          )
        );
      }

      if (ordering != null && !ordering.isEmpty()) {
        var specs = ordering.stream().map(order -> {
          if (order.startsWith("+")) {
            return cb.asc(root.get(order.substring(1).trim()));
          } else if (order.startsWith("-")) {
            return cb.desc(root.get(order.substring(1).trim()));
          } else {
            throw new IllegalArgumentException("Order fields must start with '-' or '+' prefix.");
          }
        }).collect(Collectors.toList());
        cq.orderBy(specs);
      }

      return entityManager.createQuery(cq)
        .setFirstResult((page - 1) * size)
        .setMaxResults(size)
        .getResultList();
    }
  }

  @Override
  public Long count(String query) {
    if (query == null || query.isBlank()) {
      return userAccountRepository.countActives();
    } else {

      var cb = entityManager.getCriteriaBuilder();
      var cq = cb.createQuery(Long.class);
      var root = cq.from(UserAccount.class);
      cq.select(cb.count(root));

      var predicates = preparePredicate(query, root);
      cq.where(
        cb.and(
          predicates,
          cb.equal(root.get("enabled"), true)
        )
      );

      return entityManager.createQuery(cq).getSingleResult();

    }
  }

  private Predicate preparePredicate(String query, Root<?> root) {
    var visitor = new JpaPredicateVisitor<Action>().defineRoot(root);
    var rootNode = new RSQLParser().parse(query);
    return rootNode.accept(visitor, entityManager);
  }

  @Override
  public Optional<UserAccount> findById(Long id) {
    return userAccountRepository.findActive(id);
  }

  @Override
  public void createOrUpdate(UserAccount action) {
    userAccountRepository.save(action);
  }

  @Override
  public void remove(UserAccount action) {
    action.setLastUpdate(ZonedDateTime.now());
    action.setEnabled(false);
    userAccountRepository.save(action);
  }
}
