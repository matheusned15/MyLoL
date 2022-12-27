package com.example.mylol.client;

import com.example.mylol.riot.ChampionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.example.mylol.client.HeaderConstants.*;

@Service
public class StaticDataClient {

    private final String PT_BR = "pt_BR";
    private final String INFO = "info";

    @Value("${mylol.apikey}")
    private String key;
    @Value("${mylol.url.staticdata.champions.byid}")
    private String championsByIdUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ChampionDTO getChampionByChampionId(Long championId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(championsByIdUrl)
                .path(String.valueOf(championId))
                .queryParam(LOCALE, PT_BR)
                .queryParam(TAGS, INFO)
                .queryParam(API_KEY, key);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<ChampionDTO> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                ChampionDTO.class
        );

        return response.getBody();
    }
}