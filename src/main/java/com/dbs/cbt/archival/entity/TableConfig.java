package com.dbs.cbt.archival.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "table_config",schema = "archival")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
@ToString
public class TableConfig {
    @EmbeddedId
    private TableConfigKey tableConfigKey;
    private int columnPosition;
}
