package com.dbs.cbt.archival.service;

import com.dbs.cbt.archival.data.ConfigCriteria;
import com.dbs.cbt.archival.entity.MasterConfig;
import com.dbs.cbt.archival.repository.MasterConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigService {
    private final MasterConfigRepository masterConfigRepository;

    public Optional<MasterConfig> getConfig(ConfigCriteria criteria) {
        return Optional.ofNullable(masterConfigRepository.findById(criteria.event())
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public Optional<MasterConfig> updateConfig(MasterConfig masterConfig) {
        var config = masterConfigRepository.findById(masterConfig.getBusinessEvent())
                .orElseThrow(() -> new RuntimeException("Config not found"));
        copyNewToOld(masterConfig, config);
        return Optional.of(masterConfigRepository.save(config));
    }

    private static void copyNewToOld(MasterConfig masterConfig, MasterConfig config) {
        for (Field field : MasterConfig.class.getDeclaredFields()) {
            try {
                field.setAccessible(true); // Make private fields accessible
                Object updatedValue = field.get(masterConfig); // Get the value from updatedUser

                if (updatedValue != null) {
                    field.set(config, updatedValue); // Set the value in the original user
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
