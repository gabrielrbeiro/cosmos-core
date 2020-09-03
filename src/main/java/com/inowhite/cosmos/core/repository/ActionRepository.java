package com.inowhite.cosmos.core.repository;

import com.inowhite.cosmos.core.entity.Action;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ActionRepository extends JpaRepository<Action, String>,
                                          PagingAndSortingRepository<Action, String> {
}
