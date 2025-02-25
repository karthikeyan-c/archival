package com.dbs.cbt.archival.controller;

import com.dbs.cbt.archival.data.ConfigCriteria;
import com.dbs.cbt.archival.data.RecordRequest;
import com.dbs.cbt.archival.entity.MasterConfig;
import com.dbs.cbt.archival.service.ConfigService;
import com.dbs.cbt.archival.service.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/archival/v1/")
@RequiredArgsConstructor
@Slf4j
public class ArchivalInquiryController {
    private final ConfigService configService;
    private final ReaderService readerService;
    @Value("${OUTPUT_PATH:output/}")
    private String outputPath;

    @GetMapping("config")
    public ResponseEntity<Optional<MasterConfig>> getConfiguration(@RequestBody ConfigCriteria criteria) {
        log.info("criteria is {}",criteria);
        return ResponseEntity.ok(configService.getConfig(criteria));
    }
    @PostMapping("config")
    public ResponseEntity<Optional<MasterConfig>> postConfiguration(@RequestBody MasterConfig config) {
        log.info("config is {}",config);
        return ResponseEntity.ok(configService.updateConfig(config));
    }
    @GetMapping("record")
    public ResponseEntity<Boolean> getRecords(@RequestBody RecordRequest request, @RequestHeader("correlation-id") UUID requestUUID) throws IOException {
        log.info("request is {}",request);
        return ResponseEntity.ok(readerService.readRecord(request,outputPath + requestUUID.toString() + "_" + request.getFileName()));
    }
}
