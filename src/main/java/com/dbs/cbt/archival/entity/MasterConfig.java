package com.dbs.cbt.archival.entity;

import com.dbs.cbt.archival.data.BusinessEvent;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "master_config",schema = "archival")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
@ToString
public class MasterConfig {
    @Id
    @Enumerated(EnumType.STRING)
    private BusinessEvent businessEvent;
    @Enumerated(EnumType.STRING)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_event")
    private List<TableConfig> tableConfigs;
}
