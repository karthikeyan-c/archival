package com.dbs.cbt.archival.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class RecordRequest {
    // Field class to represent each field in the "field" array
    @Getter
    @Setter
    @ToString
    public static class Field {
        private String fieldName;
    }

    // FieldListItem class to represent HTD and HTH objects in the "fieldList"
    @Getter
    @Setter
    @ToString
    public static class FieldListItem {
        private List<Field> fieldList;
    }

    // Main class representing the top-level JSON structure
    private String event;
    private String key;
    private String fileName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Map<String, FieldListItem> tableList;
}

