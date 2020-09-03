package com.inowhite.cosmos.core.service.impl;

import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;
import com.inowhite.cosmos.core.entity.Action;
import com.inowhite.cosmos.core.repository.ActionRepository;
import com.inowhite.cosmos.core.service.ActionService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultActionService implements ActionService {

  private ActionRepository actionRepository;
  private EntityManager entityManager;

  public DefaultActionService(ActionRepository actionRepository, EntityManager entityManager){
    this.actionRepository = actionRepository;
    this.entityManager = entityManager;
  }

  @Override
  public List<Action> list(int page, int size, String query, List<String> ordering) {
    if (query == null && ordering == null) {
      var pageSpecs = PageRequest.of((page - 1), size);
      return this.actionRepository.findAll(pageSpecs).getContent();
    } else {

      var cb = entityManager.getCriteriaBuilder();
      var cq = cb.createQuery(Action.class);
      var root = cq.from(Action.class);

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
    var root = cq.from(Action.class);
    cq.select(cb.count(root));

    if (query != null && query.trim().length() > 0) {
      appendQuery(query, root, cq);
    }

    return entityManager.createQuery(cq).getSingleResult();
  }

  @Override
  public Optional<Action> findById(String code) {
    return actionRepository.findById(code);
  }

  @Override
  public void createOrUpdate(Action action) {
    actionRepository.save(action);
  }

  @Override
  public void remove(Action action) {
    actionRepository.delete(action);
  }
}
