package com.inowhite.cosmos.core.repository;

import com.inowhite.cosmos.core.entity.AccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AccessGroupRepository extends JpaRepository<AccessGroup, Long>,
                                               PagingAndSortingRepository<AccessGroup, Long> {

  @Query("from AccessGroup a WHERE a.enabled = true AND a.id = ?1")
  Optional<AccessGroup> findActiveById(Long code);

}
