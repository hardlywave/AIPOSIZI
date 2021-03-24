package com.iit.ad.service;

import com.google.gson.Gson;
import com.iit.ad.respnose.EntityException;
import com.iit.ad.respnose.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
public class Service {
    private final Gson gson;

    public Service(Gson gson) {
        this.gson = gson;
    }


    public ResponseEntity<Request> getMethod(String url) throws EntityException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(url, Request.class);
        }catch (HttpServerErrorException e) {
            String json = e.getMessage().substring(7, e.getMessage().length() - 1);
            throw gson.fromJson(json, EntityException.class);
        }
    }

    public void postMethod(String url, Object date) throws EntityException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(url, date, Request.class);
        } catch (HttpServerErrorException e) {
            String json = e.getMessage().substring(7, e.getMessage().length() - 1);
            throw gson.fromJson(json, EntityException.class);
        }
    }

    public void deleteMethod(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }
}
