package com.example.mylol.service;

import com.example.mylol.riot.dto.ChampionDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StaticDataService {

    private Map<String, ChampionDTO> champions = new LinkedHashMap<>();

    public ChampionDTO getChampionById(Long id) {

        ChampionDTO championDTO = champions.values().stream().filter(champion -> Objects.equals(champion.getKey(), id)).collect(Collectors.toList()).get(0);

        return championDTO;
    }

    public void setChampions(Map<String, ChampionDTO> champions) {
        this.champions = champions;
    }
}
