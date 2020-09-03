package com.inowhite.cosmos.core.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, I> {

  default List<T> list(int page, int size) {
    return list(page, size, null, null);
  }

  default List<T> list(int page, int size, String query) {
    return list(page, size, query, null);
  }

  List<T> list(int page, int size, String query, List<String> ordering);
  Long count(String query);
  Optional<T> findById(I code);
  void createOrUpdate(T action);
  void remove(T action);

}
