package com.rohit.journalApp.config;

import com.rohit.journalApp.Entity.DBConfig;
import com.rohit.journalApp.repository.DBConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AppCache {
    @Autowired
    DBConfigRepository dbConfigRepository;

    public Map<String, String> APP_CACHE;

    @PostConstruct
    public void init() {
        APP_CACHE = new HashMap<>();
        List<DBConfig> allDBConfig = dbConfigRepository.findAll();
        log.info(String.valueOf("size"+allDBConfig.size()));
        for (DBConfig config : allDBConfig) {
            APP_CACHE.put(config.getKey(), config.getValue());
            log.info("key: "+config.getKey());
            log.info("value: "+config.getValue());
        }
    }
}
