package com.dbs.cbt.archival.data;

import com.dbs.cbt.archival.entity.MasterConfig;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ConfigResponse {
    private ConfigCriteria criteria;
    private MasterConfig details;
}
