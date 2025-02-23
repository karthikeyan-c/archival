package com.dbs.cbt.archival.repository;

import com.dbs.cbt.archival.data.BusinessEvent;
import com.dbs.cbt.archival.data.TableEvent;
import com.dbs.cbt.archival.entity.TableConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableConfigRepository extends JpaRepository<TableConfig, TableEvent> {
    @Query(value = """
            select *
            from archival.table_config
            where business_event = :businessEvent
            and table_event = :tableEvent
            """, nativeQuery = true)
    List<TableConfig> fetchColumnDetails(@Param("businessEvent") String businessEvent,
                                         @Param("tableEvent") String tableEvent);
}
