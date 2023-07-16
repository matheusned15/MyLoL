package com.example.mylol.resource;

import com.example.mylol.model.Summoner;
import com.example.mylol.service.SummonerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "summoner")
public class SummonerResource {

    @Autowired
    private SummonerService summonerService;

    @RequestMapping(method = RequestMethod.GET, path = "{name}")
    public Summoner getSummonerByName(@PathVariable(name = "name") String name){
        return summonerService.getSummonerByName(name);
    }
}
