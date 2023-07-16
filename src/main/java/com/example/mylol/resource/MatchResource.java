package com.example.mylol.resource;

import com.example.mylol.model.MatchItem;
import com.example.mylol.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "match")
public class MatchResource {

    @Autowired
    private MatchService matchService;

    @RequestMapping(method = RequestMethod.GET, path = "{accountId}")
    public List<MatchItem> getMatchList(
            @PathVariable("accountId") Long accountId,
            @RequestParam("beginIndex") Integer beginIndex,
            @RequestParam("endIndex") Integer endIndex) {
        return matchService.getMatchList(accountId, beginIndex, endIndex);
    }
}
