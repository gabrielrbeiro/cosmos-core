package com.inowhite.cosmos.core.service.impl;

import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;
import com.inowhite.cosmos.core.entity.AccessGroup;
import com.inowhite.cosmos.core.entity.Action;
import com.inowhite.cosmos.core.repository.AccessGroupRepository;
import com.inowhite.cosmos.core.service.AccessGroupService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultAccessGroupService implements AccessGroupService {

  private final AccessGroupRepository accessGroupRepository;
  private final EntityManager entityManager;

  public DefaultAccessGroupService(AccessGroupRepository accessGroupRepository, EntityManager entityManager) {
    this.accessGroupRepository = accessGroupRepository;
    this.entityManager = entityManager;
  }

  @Override
  public List<AccessGroup> list(int page, int size, String query, List<String> ordering) {

    if (query == null && ordering == null) {
      var pageSpec = PageRequest.of(page, size);
      return accessGroupRepository.findAll(pageSpec).getContent();
    } else {

      var cb = entityManager.getCriteriaBuilder();
      var cq = cb.createQuery(AccessGroup.class);
      var root = cq.from(AccessGroup.class);
      cq.select(root);

      if (query != null && query.trim().length() > 0) {
        appendQuery(query, root, cq);
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

  private void appendQuery(String query, Root<?> root, CriteriaQuery<?> cq) {
    var visitor = new JpaPredicateVisitor<Action>().defineRoot(root);
    var rootNode = new RSQLParser().parse(query);
    Predicate predicate = rootNode.accept(visitor, entityManager);
    cq.where(predicate);
  }

  @Override
  public Long count(String query) {
    var cb = entityManager.getCriteriaBuilder();
    var cq = cb.createQuery(Long.class);
    var root = cq.from(AccessGroup.class);
    cq.select(cb.count(root));

    if (query != null) {
      appendQuery(query, root, cq);
    }

    return entityManager.createQuery(cq).getSingleResult();
  }

  @Override
  public Optional<AccessGroup> findById(Long code) {
    return accessGroupRepository.findActiveById(code);
  }

  @Override
  public void createOrUpdate(AccessGroup action) {
    accessGroupRepository.save(action);
  }

  @Override
  public void remove(AccessGroup action) {
    action.setLastUpdate(ZonedDateTime.now());
    action.setEnabled(false);
    accessGroupRepository.save(action);
  }
}
