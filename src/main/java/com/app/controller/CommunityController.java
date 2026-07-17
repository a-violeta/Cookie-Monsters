package com.app.controller;

import com.app.dto.CommunityDto;
import com.app.mapper.CommunityMapper;
import com.app.model.Community;
import com.app.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor

public class CommunityController {

    private final CommunityService communityService;
    private final CommunityMapper communityMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Community addCommunity(@RequestBody CommunityDto communityDto ) {
        var community = communityMapper.fromDto(communityDto);
        return communityService.addCommunity(community);
    }

}
