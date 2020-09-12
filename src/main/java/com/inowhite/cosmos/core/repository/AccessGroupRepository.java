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

package com.inowhite.cosmos.core.repository;

import com.inowhite.cosmos.core.entity.AccessGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.net.ContentHandler;
import java.util.Optional;

public interface AccessGroupRepository extends JpaRepository<AccessGroup, Long>,
                                               PagingAndSortingRepository<AccessGroup, Long> {

  @Query("from AccessGroup a WHERE a.enabled = true AND a.id = ?1")
  Optional<AccessGroup> findActiveById(Long code);

  @Query("from AccessGroup a WHERE a.enabled = true")
  Page<AccessGroup> findAllActive(Pageable page);
}
