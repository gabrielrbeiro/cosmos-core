package com.inowhite.cosmos.core.repository;

import com.inowhite.cosmos.core.entity.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>,
                                               PagingAndSortingRepository<UserAccount, Long> {

  @Query("from UserAccount ua WHERE ua.enabled = true")
  List<UserAccount> findActive(Pageable pageSpec);

  @Query("SELECT count(ua) FROM UserAccount ua WHERE ua.enabled = true")
  Long countActives();

  @Query("from UserAccount ua WHERE ua.enabled = true AND ua.id = ?1")
  Optional<UserAccount> findActive(Long id);
}
