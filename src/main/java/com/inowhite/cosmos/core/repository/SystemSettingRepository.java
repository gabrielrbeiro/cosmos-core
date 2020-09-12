package com.inowhite.cosmos.core.repository;

import com.inowhite.cosmos.core.entity.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, String>,
                                                 PagingAndSortingRepository<SystemSetting, String> {
}
