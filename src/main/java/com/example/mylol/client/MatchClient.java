package com.example.mylol.client;

import com.example.mylol.riot.dto.MatchlistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import static com.example.mylol.constants.HeaderConstants.ACCEPT;
import static com.example.mylol.constants.HeaderConstants.API_KEY;

@Service
public class MatchClient {

    @Value("${mylol.apikey}")
    private String key;
    @Value("${mylol.url.match.byaccountid}")
    private String matchByAccountId;

    @Autowired
    private RestTemplate restTemplate;

    public MatchlistDTO getMatchList(Long accountId, Integer beginIndex, Integer endIndex) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(matchByAccountId)
                .path(String.valueOf(accountId))
                .queryParam("beginIndex", beginIndex)
                .queryParam("endIndex", endIndex)
                .queryParam(API_KEY, key);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<MatchlistDTO> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                MatchlistDTO.class
        );

        return response.getBody();
    }
}
