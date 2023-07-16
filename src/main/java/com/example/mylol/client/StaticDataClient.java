package com.example.mylol.client;

import com.example.mylol.riot.dto.ChampionDTO;
import com.example.mylol.riot.dto.ChampionListDTO;
import com.example.mylol.riot.dto.JsonChampionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.example.mylol.constants.HeaderConstants.*;

@Service
public class StaticDataClient {

    private final String PT_BR = "pt_BR";
    private final String INFO = "info";

    @Value("${mylol.apikey}")
    private String key;
    @Value("${mylol.url.staticdata.champions}")
    private String champions;

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable("champions")
    public ChampionDTO getChampionByChampionId(Long championId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(champions)
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

    public ChampionListDTO getChampions() throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(champions)
                .queryParam(API_KEY, key);

        HttpEntity<?> entity = new HttpEntity<>(headers);


        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        String json = response.getBody();

        ObjectMapper mapper = new ObjectMapper();

        JsonChampionDTO readValue = mapper.readValue(json, JsonChampionDTO.class);
        ChampionListDTO championListDTO = new ChampionListDTO();
        championListDTO.setData(readValue.getData());

        return championListDTO;
    }
}