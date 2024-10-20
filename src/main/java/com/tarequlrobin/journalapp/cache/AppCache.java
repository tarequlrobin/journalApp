package com.tarequlrobin.journalapp.cache;

import com.tarequlrobin.journalapp.entity.ConfigJournalApp;
import com.tarequlrobin.journalapp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String, String> APP_CACHE = new HashMap<>();

    @PostConstruct
    public void init() {
        List<ConfigJournalApp> all = configJournalAppRepository.findAll();
        for (ConfigJournalApp app : all) {
            APP_CACHE.put(app.getKey(), app.getValue());
        }
    }
}