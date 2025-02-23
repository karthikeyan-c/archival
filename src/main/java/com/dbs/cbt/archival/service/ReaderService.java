package com.dbs.cbt.archival.service;

import com.dbs.cbt.archival.data.BusinessEvent;
import com.dbs.cbt.archival.data.RecordRequest;
import com.dbs.cbt.archival.data.TableEvent;
import com.dbs.cbt.archival.entity.MasterConfig;
import com.dbs.cbt.archival.repository.MasterConfigRepository;
import com.dbs.cbt.archival.repository.TableConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReaderService {
    private final TableConfigRepository tableConfigRepository;
    private final MasterConfigRepository masterConfigRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

    @Value("${WORKING_DIR:downloads/}")
    private String workingDir;

    public List<String> readRecord(RecordRequest request) {
        MasterConfig masterConfig = masterConfigRepository.findById(BusinessEvent.TRANSACTION_EVENT)
                .orElseThrow(() -> new RuntimeException("Config missing for Transaction Event"));

        List<String> collect = masterConfig.getTableConfigs().stream()
                .map(tableConfig -> {
                    String yearMonth = request.getFromDate().format(formatter);
                    log.info("yearMonth is {}",yearMonth);

                    var kk = request.getFieldList().entrySet().stream()
                            .map( entry -> {
                                var valueList = tableConfigRepository.fetchColumnDetails(request.getEvent(),entry.getKey());
                    log.info("valueList is {}",valueList);
                    return "kk";
//                    valueList.stream()
//                            .filter( value -> {
//                                return request.getFieldList()value.getTableConfigKey().getColumnName()
//                            })
                            }).toList();
                    log.info("kk is {}",kk);

                    var matchingRecord =  fetchRecord(
                            workingDir + yearMonth + "_" + tableConfig.getTableConfigKey().getTableEvent() + ".csv",
                            request.getKey());
                    log.info("matchingRecord is {}",matchingRecord);
                    return matchingRecord;
                })
                .toList();
        log.info("collect is {}",collect);
        return collect;
    }

    public static String fetchRecord(String filePath, String accountId) {
        try {
            // Build the grep command to search for account_id at the beginning of the line
            String command = String.format("grep \"^%s,\" %s", accountId, filePath);

            // Execute the command using ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process process = processBuilder.start();

            // Get the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            StringBuilder stringBuilder = new StringBuilder();

            // Read and print the output line by line (the matching transaction details)
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                log.info("Found: {} ",line);  // Print the entire line
            }

            // Wait for the command to complete
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.info("Error executing the command");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
