package com.dbs.cbt.archival.service;

import com.dbs.cbt.archival.data.RecordRequest;
import com.dbs.cbt.archival.entity.TableConfig;
import com.dbs.cbt.archival.repository.TableConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReaderService {
    private final TableConfigRepository tableConfigRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

    @Value("${WORKING_DIR:downloads/}")
    private String workingDir;

    public boolean readRecord(RecordRequest request,String fileName) throws IOException {
        String yearMonth = request.getFromDate().format(formatter);
        log.info("Processing records for yearMonth: {}", yearMonth);

        request.getTableList().entrySet()
                .forEach(requiredFields -> {
                    log.info("Processing table: {}", requiredFields.getKey());

                    List<String> inputFieldNameList = getFieldNames(requiredFields);
                    List<List<String>> excelData = fetchRecordAndCreateExcel(
                            workingDir + yearMonth + "_" + requiredFields.getKey() + ".csv",
                            request.getKey(), request.getEvent(), requiredFields.getKey(), inputFieldNameList
                    );

                    log.info("Creating Excel file for table: {}", requiredFields.getKey());
                    try {
                        File file = new File(fileName);
                        Workbook workbook = file.exists() ? new XSSFWorkbook(new FileInputStream(file)) : new XSSFWorkbook();
                        createExcelFile(excelData, inputFieldNameList, requiredFields.getKey(), fileName, workbook);
                    } catch (IOException e) {
                        log.error("Error creating Excel file for table {}: {}", requiredFields.getKey(), e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
        return true;
    }

    private List<String> getFieldNames(Map.Entry<String, RecordRequest.FieldListItem> requiredFields) {
        return requiredFields.getValue().getFieldList().stream()
                .map(RecordRequest.Field::getFieldName)
                .toList();
    }

    public List<List<String>> fetchRecordAndCreateExcel(String filePath, String accountId, String businessEvent, String tableName, List<String> inputFieldNameList) {
        List<List<String>> excelData = new ArrayList<>();
        Map<String, Integer> columnPositionMap = getColumnPositionMap(businessEvent, tableName);

        try {
            Process process = new ProcessBuilder("bash", "-c", buildGrepCommand(accountId, filePath)).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log.info("Found: {}", line);
                List<String> row = processLine(line, columnPositionMap, inputFieldNameList);
                excelData.add(row);
            }

            if (process.waitFor() != 0) {
                log.error("Error executing the command.");
            }

        } catch (Exception e) {
            log.error("Error processing file {}: {}", filePath, e.getMessage());
        }
        log.info("excelData is {}",excelData);
        return excelData;
    }

    // todo : move to cachable.
    private Map<String, Integer> getColumnPositionMap(String businessEvent, String tableName) {
        List<TableConfig> tableConfigList = tableConfigRepository.fetchColumnDetails(businessEvent, tableName);
        return tableConfigList.stream()
                .collect(Collectors.toMap(
                        columnDetails -> columnDetails.getTableConfigKey().getColumnName(),
                        TableConfig::getColumnPosition
                ));
    }

    private String buildGrepCommand(String accountId, String filePath) {
        return String.format("grep \"^%s,\" %s", accountId, filePath);
    }

    private List<String> processLine(String line, Map<String, Integer> columnPositionMap, List<String> inputFieldNameList) {
        String[] fields = line.split(",");
        return inputFieldNameList.stream()
                .map(inputFieldName -> {
                    log.info("columnPositionMap is {}",columnPositionMap);
                    Integer columnPosition = columnPositionMap.get(inputFieldName);
                    log.info("columnPosition is {}",columnPosition);
                    log.info("fields is {}",columnPosition);
                    return columnPosition != null ? fields[columnPosition - 1] : "";
                })
                .collect(Collectors.toList());
    }

    public Workbook createExcelFile(List<List<String>> data, List<String> inputFieldNameList, String sheetName, String fileName, Workbook workbook) throws IOException {
        log.info("data is {}",data);
        Sheet sheet = workbook.createSheet(sheetName);
        createHeaderRow(sheet, inputFieldNameList);
        populateSheetData(sheet, data);

        try (workbook; FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        }

        log.info("Excel file {} created/updated successfully!", fileName);
        return workbook;
    }

    private void createHeaderRow(Sheet sheet, List<String> inputFieldNameList) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < inputFieldNameList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(inputFieldNameList.get(i));
        }
    }

    private void populateSheetData(Sheet sheet, List<List<String>> data) {
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1); // Skip the header row
            List<String> rowData = data.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData.get(j));
            }
        }
    }
}
