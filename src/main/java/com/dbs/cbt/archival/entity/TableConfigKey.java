package com.dbs.cbt.archival.entity;

import com.dbs.cbt.archival.data.TableEvent;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Builder
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TableConfigKey {
    @Enumerated(EnumType.STRING)
    private TableEvent tableEvent;
    private String columnName;
}
