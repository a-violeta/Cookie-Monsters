package com.app.client;

import com.app.dto.CommunityDto;
import com.app.dto.PostDto;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.response.ApiResponse;
import com.app.service.CommunityUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class CommunityHttpClient implements CommunityUseCases {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;

    // validateCommunity is copied here because it s just logic, no DB access
    // round-tripping the network just to do a check would be wasteful
    // and the two copies are kept in sync since neither needs the DB
    // UserService.createUser's validation DOES need the DB to check username uniqueness
    // and so createUser is never duplicated
    @Override
    public void validateCommunity(String communityName, String displayName, String description) {
        // pure validation, no I/O, mirrors CommunityService, safe to duplicate
        if (communityName == null || communityName.isBlank() || displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Community name is required");
        }
        if (!communityName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Community name must contain only letters, numbers, and '_'");
        }
        if (communityName.length() < 3 || displayName.length() < 3) {
            throw new IllegalArgumentException("Community name must have at least 3 characters");
        }
        if (communityName.length() > 50 || displayName.length() > 50) {
            throw new IllegalArgumentException("Community name is too long");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Community description is required");
        }
        if (description.length() > 500) {
            throw new IllegalArgumentException("Description is too long");
        }
    }

    @Override
    public Community createCommunity(String name, String displayName, String description, String iconUrl) {
        validateCommunity(name, displayName, description);
        String url = clientConfig.getBaseUrl() + "/subreddits";

        CommunityDto request = new CommunityDto();
        request.setName(name);
        request.setDisplayName(displayName);
        request.setDescription(description);
        request.setIconUrl(iconUrl);

        try {
            ResponseEntity<ApiResponse<CommunityDto>> response = restTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<CommunityDto>>() {});

            log.info("Community created via HTTP: {}", name);

            return toCommunity(response.getBody().getData());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void deleteCommunity(String name) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + name;
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public List<Community> listCommunities() {
        String url = clientConfig.getBaseUrl() + "/subreddits";
        try {
            ResponseEntity<ApiResponse<List<CommunityDto>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<ApiResponse<List<CommunityDto>>>() {});
            return response.getBody().getData().stream().map(this::toCommunity).toList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public Community findCommunityById(UUID communityId) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + communityId;
        try {
            ResponseEntity<ApiResponse<CommunityDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<ApiResponse<CommunityDto>>() {});
            return toCommunity(response.getBody().getData());
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public Community findCommunityByName(String name) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + name;
        try {
            ResponseEntity<ApiResponse<CommunityDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<ApiResponse<CommunityDto>>() {});
            return toCommunity(response.getBody().getData());
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Community with name " + name + " not found");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void editCommunity(String name, String displayName, String description) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + name;
        CommunityDto request = new CommunityDto();
        request.setDescription(description);
        request.setDisplayName(displayName);
        try {
            restTemplate.put(url, request);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void joinCommunity(UUID communityId, Long userId) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + communityId + "/members/" + userId;
        try {
            restTemplate.postForLocation(url, null);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void exitCommunity(UUID communityId, Long userId) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + communityId + "/members/" + userId;
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public List<Post> listCommunityPosts(String name){
        String url = clientConfig.getBaseUrl() + "/subreddits/" + name + "/posts";
        try {
            ResponseEntity<ApiResponse<List<PostDto>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<ApiResponse<List<PostDto>>>() {});
            return response.getBody().getData().stream().map(this::toPost).toList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    private Post toPost(PostDto dto) {
        if (dto == null) return null;

        Community community = new Community();
        community.setId(dto.getCommunityId());
        community.setName(dto.getCommunityName());

        User user = new User();
        user.setId(dto.getUserId());
        user.setUsername(dto.getUsername());

        Post post = new Post();
        post.setId(dto.getId());
        post.setCommunity(community);
        post.setUser(user);
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setCreatedAt(dto.getCreatedAt());
        return post;
    }

    private Community toCommunity(CommunityDto dto) {
        if (dto == null) return null;
        Community community = new Community();
        community.setId(dto.getId());
        community.setName(dto.getName());
        community.setDisplayName(dto.getDisplayName());
        community.setDescription(dto.getDescription());
        community.setCreatedAt(dto.getCreatedAt());
        community.setIconUrl(dto.getIconUrl());
        // communityUsers/communityPosts intentionally left null, console has no datasource to hydrate them
        return community;
    }

    private String extractMessage(HttpClientErrorException e) {
        return e.getResponseBodyAsString().isBlank()
                ? "Request failed (" + e.getStatusCode() + ")"
                : e.getResponseBodyAsString();
    }

    private String extractMessage(Exception e) {
        return "Request failed: " + e.getMessage();
    }
}