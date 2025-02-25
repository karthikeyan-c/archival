package com.dbs.cbt.archival.config;

import com.dbs.cbt.archival.data.BusinessEvent;
import com.dbs.cbt.archival.data.TableEvent;
import com.dbs.cbt.archival.entity.MasterConfig;
import com.dbs.cbt.archival.entity.TableConfig;
import com.dbs.cbt.archival.entity.TableConfigKey;
import com.dbs.cbt.archival.repository.MasterConfigRepository;
import com.dbs.cbt.archival.repository.TableConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeedConfig {
    private final MasterConfigRepository masterConfigRepository;
    private final TableConfigRepository tableConfigRepository;

    @PostConstruct
    public void seed() {
        var configSaved = masterConfigRepository.save(
                MasterConfig.builder()
                        .businessEvent(BusinessEvent.TRANSACTION_EVENT)
                        .tableConfigs(List.of(
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTD)
                                                .columnName("account_id")
                                                .build())
                                        .columnPosition(1)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTD)
                                                .columnName("tran_id")
                                                .build())
                                        .columnPosition(2)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTD)
                                                .columnName("tran_date")
                                                .build())
                                        .columnPosition(3)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTD)
                                                .columnName("part_tran_srl_num")
                                                .build())
                                        .columnPosition(4)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTD)
                                                .columnName("bank_id")
                                                .build())
                                        .columnPosition(5)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTD)
                                                .columnName("tran_amt")
                                                .build())
                                        .columnPosition(6)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTD)
                                                .columnName("tran_crncy")
                                                .build())
                                        .columnPosition(7)
                                        .build(),
                                // HTH
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTH)
                                                .columnName("account_id")
                                                .build())
                                        .columnPosition(1)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTH)
                                                .columnName("tran_id")
                                                .build())
                                        .columnPosition(2)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTH)
                                                .columnName("tran_date")
                                                .build())
                                        .columnPosition(3)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTH)
                                                .columnName("part_tran_srl_num")
                                                .build())
                                        .columnPosition(4)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTH)
                                                .columnName("bank_id")
                                                .build())
                                        .columnPosition(5)
                                        .build(),
                                TableConfig.builder()
                                        .tableConfigKey(TableConfigKey.builder()
                                                .tableEvent(TableEvent.HTH)
                                                .columnName("channel_id")
                                                .build())
                                        .columnPosition(6)
                                        .build()
                        ))
                        .build()
        );
        log.info("config saved : {}",configSaved);
    }
}
