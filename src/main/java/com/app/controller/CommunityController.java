package com.app.controller;

import com.app.dto.CommunityDto;
import com.app.mapper.CommunityMapper;
import com.app.model.Community;
import com.app.service.CommunityUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityUseCases communityService;
    private final CommunityMapper communityMapper;

    @PostMapping
    public ResponseEntity<CommunityDto> createCommunity(@Valid @RequestBody CommunityDto dto) {
        Community created = communityService.createCommunity(dto.getCommunityName(), dto.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(communityMapper.toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<CommunityDto>> listCommunities() {
        return ResponseEntity.ok(communityService.listCommunities().stream().map(communityMapper::toDto).toList());
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable long communityId) {
        return ResponseEntity.ok(communityMapper.toDto(communityService.findCommunityById(communityId)));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CommunityDto> getCommunityByName(@PathVariable String name) {
        return ResponseEntity.ok(communityMapper.toDto(communityService.findCommunityByName(name)));
    }

    @PutMapping("/{communityId}")
    public ResponseEntity<Void> editCommunity(@PathVariable long communityId, @RequestBody CommunityDto dto) {
        communityService.editCommunity(communityId, dto.getDescription());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable long communityId) {
        communityService.deleteCommunity(communityId);
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
}