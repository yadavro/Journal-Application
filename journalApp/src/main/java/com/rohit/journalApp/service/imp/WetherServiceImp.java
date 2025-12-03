package com.rohit.journalApp.service.imp;

import com.rohit.journalApp.Entity.Wether;
import com.rohit.journalApp.config.AppCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WetherServiceImp {
    @Autowired
    private RedisServiceImp redisServiceImp;

    @Autowired
    private AppCache appCache;

    @Value("${spring.wether.api.key}")
    private String API_KEY;

    private static RestTemplate restTemplate = new RestTemplate();

    public Wether getReport(String city) {
        Wether wether = redisServiceImp.get(city, Wether.class);
        if(wether!=null){
            log.info("wether details fetched from redis: "+ wether);
            return wether;
        }else{
            String BASE_URI = appCache.APP_CACHE.get("weather_api");
            log.info("base_uri: " + BASE_URI);
            String finalUrl = BASE_URI.replace("<api_key>", API_KEY).replace("<city>", city);
            log.info("final url:" + finalUrl);
            ResponseEntity<Wether> exchange = restTemplate.exchange(finalUrl, HttpMethod.GET, null, Wether.class);
            Wether body = exchange.getBody();
            log.info("reponse body" + body);
            redisServiceImp.set(city,body,300);
            return body;
        }
    }
}
