package com.dbs.cbt.archival.repository;

import com.dbs.cbt.archival.data.BusinessEvent;
import com.dbs.cbt.archival.entity.MasterConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterConfigRepository extends JpaRepository<MasterConfig, BusinessEvent> {
}
