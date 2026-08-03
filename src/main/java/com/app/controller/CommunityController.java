package com.app.controller;

import com.app.dto.CommunityDto;
import com.app.dto.PostDto;
import com.app.mapper.CommunityMapper;
import com.app.mapper.PostMapper;
import com.app.model.Community;
import com.app.service.CommunityUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subreddits")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityUseCases communityService;
    private final CommunityMapper communityMapper;
    private final PostMapper postMapper;

    @PostMapping
    public ResponseEntity<CommunityDto> createCommunity(@Valid @RequestBody CommunityDto dto) {
        Community created = communityService.createCommunity(dto.getName(), dto.getDisplayName(), dto.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(communityMapper.toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<CommunityDto>> listCommunities() {
        return ResponseEntity.ok(communityService.listCommunities().stream().map(communityMapper::toDto).toList());
    }

    // it has the same path as find community by name: /subreddits/{parameter}
    // can't distinguish by parameter alone
    /*
    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable long communityId) {
        return ResponseEntity.ok(communityMapper.toDto(communityService.findCommunityById(communityId)));
    }
    */

    @GetMapping("/{name}")
    public ResponseEntity<CommunityDto> getCommunityByName(@PathVariable String name) {
        return ResponseEntity.ok(communityMapper.toDto(communityService.findCommunityByName(name)));
    }

    @PutMapping("/{name}")
    public ResponseEntity<Void> editCommunity(@PathVariable String name, @RequestBody CommunityDto dto) {
        communityService.editCommunity(name, dto.getDisplayName(), dto.getDescription());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable String name) {
        communityService.deleteCommunity(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{communityId}/members/{userId}")
    public ResponseEntity<Void> joinCommunity(@PathVariable Long communityId, @PathVariable Long userId) {
        communityService.joinCommunity(communityId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{communityId}/members/{userId}")
    public ResponseEntity<Void> exitCommunity(@PathVariable Long communityId, @PathVariable Long userId) {
        communityService.exitCommunity(communityId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{name}/posts")
    public ResponseEntity<List<PostDto>> listCommunityPosts(@PathVariable String name){
        return ResponseEntity.ok(communityService.listCommunityPosts(name).stream().map(postMapper::toDto).toList());
    }
}