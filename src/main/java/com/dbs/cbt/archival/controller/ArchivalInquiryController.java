package com.dbs.cbt.archival.controller;

import com.dbs.cbt.archival.data.ConfigCriteria;
import com.dbs.cbt.archival.data.RecordRequest;
import com.dbs.cbt.archival.entity.MasterConfig;
import com.dbs.cbt.archival.service.ConfigService;
import com.dbs.cbt.archival.service.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/archival/v1/")
@RequiredArgsConstructor
@Slf4j
public class ArchivalInquiryController {
    private final ConfigService configService;
    private final ReaderService readerService;

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
    public ResponseEntity<List<String>> getRecords(@RequestBody RecordRequest request) {
        log.info("request is {}",request);
        return ResponseEntity.ok(readerService.readRecord(request));
    }
}
