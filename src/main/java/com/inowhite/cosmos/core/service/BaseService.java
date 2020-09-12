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
  void createOrUpdate(T item);
  void remove(T item);

}
