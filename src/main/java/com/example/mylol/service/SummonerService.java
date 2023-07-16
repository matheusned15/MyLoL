package com.example.mylol.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.mylol.client.ChampionMasteryClient;
import com.example.mylol.client.LeagueClient;
import com.example.mylol.client.SummonerClient;
import com.example.mylol.model.LeaguePosition;
import com.example.mylol.model.Summoner;
import com.example.mylol.riot.dto.ChampionDTO;
import com.example.mylol.riot.dto.ChampionMasteryDTO;
import com.example.mylol.riot.dto.LeaguePositionDTO;
import com.example.mylol.riot.dto.SummonerDTO;
import com.example.mylol.util.LeagueParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.mylol.constants.FileConstants.SUFFIX_JPG;
import static com.example.mylol.constants.FileConstants.SUFFIX_PNG;

@Service
public class SummonerService {

    @Autowired
    private SummonerClient summonerClient;
    @Autowired
    private LeagueClient leagueClient;
    @Autowired
    private ChampionMasteryClient championMasteryClient;
    @Autowired
    private StaticDataService staticDataService;

    @Value("${mylol.url.base}")
    private String urlBase;
    @Value("${mylol.url.static.path.splash}")
    private String staticPathSplash;
    @Value("${mylol.url.static.path.profileicon}")
    private String staticPathProfileIcon;

    public Summoner getSummonerByName(String name) {
        SummonerDTO summonerDTO = summonerClient.getSummonerByName(name);
        Set<LeaguePositionDTO> leaguePositionDTOs = leagueClient.getLeaguePositionBySummonerId(summonerDTO.getId());
        List<ChampionMasteryDTO> championMasteryDTOs = championMasteryClient.getLeaguePositionBySummonerId(summonerDTO.getId());
        ChampionMasteryDTO championMasteryDTO = championMasteryDTOs.get(0);
        ChampionDTO championDTO = staticDataService.getChampionById(championMasteryDTO.getChampionId());

        Summoner summoner = new Summoner();
        parseSummonerDtoToSummoner(summonerDTO, summoner);
        parseLeaguePositionDtoToSummoner(leaguePositionDTOs, summoner);
        parseChampionDtoToSummoner(championDTO, summoner);

        return summoner;
    }

    private void parseSummonerDtoToSummoner(SummonerDTO summonerDTO, Summoner summoner) {
        if (summonerDTO != null) {
            summoner.setId(summonerDTO.getId());
            summoner.setName(summonerDTO.getName());
            summoner.setProfileIconURL(urlBase + staticPathProfileIcon + summonerDTO.getProfileIconId() + SUFFIX_PNG);
            summoner.setSummonerLevel(summonerDTO.getSummonerLevel());
            summoner.setAccountId(summonerDTO.getAccountId());
        }
    }

    private void parseLeaguePositionDtoToSummoner(Set<LeaguePositionDTO> leaguePositionDTOs, Summoner summoner) {
        Set<LeaguePosition> leaguePositions = new HashSet<>();
        if (leaguePositionDTOs != null) {
            leaguePositions = leaguePositionDTOs.stream().map(leaguePositionDTO -> {
                LeaguePosition leaguePosition = new LeaguePosition();

                leaguePosition.setQueueType(LeagueParseUtil.getQueue(leaguePositionDTO.getQueueType()));
                leaguePosition.setWins(leaguePositionDTO.getWins());
                leaguePosition.setLosses(leaguePositionDTO.getLosses());
                leaguePosition.setLeagueName(leaguePositionDTO.getLeagueName());
                leaguePosition.setRank(leaguePositionDTO.getRank());
                leaguePosition.setTier(leaguePositionDTO.getTier());
                leaguePosition.setLeaguePoints(leaguePositionDTO.getLeaguePoints());

                return leaguePosition;
            }).collect(Collectors.toSet());
        }

        summoner.setLeaguePositions(leaguePositions);
    }

    private void parseChampionDtoToSummoner(ChampionDTO championDTO, Summoner summoner) {
        if (championDTO != null) {
            summoner.setSplashURL(urlBase + staticPathSplash + championDTO.getId() + SUFFIX_JPG);
        }
    }
}
