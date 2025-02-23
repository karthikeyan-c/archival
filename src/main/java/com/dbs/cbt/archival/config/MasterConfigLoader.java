//package com.dbs.cbt.archival.config;
//
//import com.dbs.cbt.archival.data.BusinessEvent;
//import com.dbs.cbt.archival.entity.MasterConfig;
//import com.dbs.cbt.archival.repository.MasterConfigRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class MasterConfigLoader {
//    private Map<String,String> masterConfigMap;
//    private final MasterConfigRepository masterConfigRepository;
//
//    @PostConstruct
//    public void buildConfig() {
//        List<MasterConfig> allConfig = masterConfigRepository.findAll();
//        log.info("allConfig is {}",allConfig);
//        allConfig.forEach( masterConfig -> {
//            masterConfigMap.put(masterConfig.getBusinessEvent(),
//                    )
//        });
//
//    }
//}
