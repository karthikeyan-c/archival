package com.dbs.cbt.archival.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class RecordRequest {
    // Field class to represent each field in the "field" array
    @Getter
    @Setter
    public static class Field {
        private String fieldName;
    }

    // FieldListItem class to represent HTD and HTH objects in the "fieldList"
    @Getter
    @Setter
    public static class FieldListItem {
        private List<Field> field;
    }

    // Main class representing the top-level JSON structure
    private String event;
    private String key;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Map<String, FieldListItem> fieldList;
}

