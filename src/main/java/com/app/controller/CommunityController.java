package com.app.controller;

import com.app.dto.CommunityDto;
import com.app.dto.CommunityUpdateRequest;
import com.app.dto.PostDto;
import com.app.mapper.CommunityMapper;
import com.app.mapper.PostMapper;
import com.app.model.Community;
import com.app.response.ApiResponse;
import com.app.service.CommunityUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subreddits")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityUseCases communityService;
    private final CommunityMapper communityMapper;
    private final PostMapper postMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CommunityDto>> createCommunity(
            @Valid @RequestBody CommunityDto dto,
            Authentication authentication) {
        Community created = communityService.createCommunity(
                dto.getName(), dto.getDisplayName(), dto.getDescription(), dto.getIconUrl(),
                authentication.getName()); // the authenticated username from the validated JWT
        return ResponseEntity.ok(ApiResponse.ok(communityMapper.toDto(created)));
    }

    /*
    @GetMapping
    public ResponseEntity<List<CommunityDto>> listCommunities() {
        return ResponseEntity.ok(communityService.listCommunities().stream().map(communityMapper::toDto).toList());
    }
    */

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommunityDto>>> listCommunities() {
        List<CommunityDto> dtos = communityService.listCommunities().stream()
                .map(communityMapper::toDto)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(dtos, dtos.size()));
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
    public ResponseEntity<ApiResponse<CommunityDto>> getCommunityByName(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.ok(communityMapper.toDto(communityService.findCommunityByName(name))));
    }

    @PutMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> editCommunity(
            @PathVariable String name,
            @Valid @RequestBody CommunityUpdateRequest dto,
            Authentication authentication) {
        communityService.editCommunity(name, dto.getDisplayName(), dto.getIconUrl(), dto.getDescription(), authentication.getName());
        return ResponseEntity.ok(ApiResponse.message("Community updated successfully"));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> deleteCommunity(@PathVariable String name, Authentication authentication) {
        communityService.deleteCommunity(name, authentication.getName());
        return ResponseEntity.ok(ApiResponse.message("Community deleted successfully"));
    }

    @DeleteMapping("/{communityId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> exitCommunity(@PathVariable UUID communityId, @PathVariable Long userId) {
        communityService.exitCommunity(communityId, userId);
        return ResponseEntity.ok(ApiResponse.message("Left community successfully"));
    }

    @GetMapping("/{name}/posts")
    public ResponseEntity<ApiResponse<List<PostDto>>> listCommunityPosts(@PathVariable String name){
        List<PostDto> dtos = communityService.listCommunityPosts(name).stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(ApiResponse.ok(dtos, dtos.size()));
    }
}