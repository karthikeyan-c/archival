package com.dbs.cbt.archival.controller;

import com.dbs.cbt.archival.data.RecordRequest;
import com.dbs.cbt.archival.service.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/archival/v1/")
@Slf4j
public class ArchivalDownloadController {
    private final ReaderService readerService;
    @Value("${OUTPUT_PATH:output/}")
    private String outputPath;

    // Date formatter to convert from string to LocalDate
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("downloadExcel")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody RecordRequest request,
                                                @RequestHeader("correlation-id") UUID requestUUID) throws IOException {
        // Process the request and get the Excel data
        log.info("request is {}",request);
        var fileWithPath = outputPath + requestUUID.toString() + "_" + request.getFileName();
        var returnValue = readerService.readRecord(request,fileWithPath);
        log.info("returnValue is {}",returnValue);

        File file = new File(fileWithPath);
        if (file.exists()) {
            var workbook = new XSSFWorkbook(new FileInputStream(file));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            byte[] excelBytes = byteArrayOutputStream.toByteArray();

            // Set response headers to download the file
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=downloaded_file.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } else {
            throw new RuntimeException("File not generated");
        }
    }
}
