package com.app.client;

import com.app.dto.CommunityDto;
import com.app.dto.PostDto;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.service.CommunityUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class CommunityHttpClient implements CommunityUseCases {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;

    @Override
    public void validateCommunity(String communityName, String description) {
        if (communityName == null || communityName.isBlank()) {
            throw new IllegalArgumentException("Community name is required");
        }
        if (!communityName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Community name must contain only letters, numbers, and '_'");
        }
        if (communityName.length() < 3) {
            throw new IllegalArgumentException("Community name must have at least 3 characters");
        }
        if (communityName.length() > 50) {
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
        validateCommunity(name, description);
        String url = clientConfig.getBaseUrl() + "/subreddits";

        CommunityDto request = new CommunityDto();
        request.setName(name);
        request.setDisplayName(displayName);
        request.setDescription(description);
        request.setIconUrl(iconUrl);

        try {
            CommunityDto response = restTemplate.postForObject(url, request, CommunityDto.class);
            log.info("Community created via HTTP: {}", name);
            return toCommunity(response);
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
            org.springframework.http.ResponseEntity<List<CommunityDto>> response = restTemplate.exchange(
                    url, org.springframework.http.HttpMethod.GET, null,
                    new org.springframework.core.ParameterizedTypeReference<List<CommunityDto>>() {});
            return response.getBody().stream().map(this::toCommunity).toList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public Community findCommunityById(UUID communityId) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + communityId;
        try {
            return toCommunity(restTemplate.getForObject(url, CommunityDto.class));
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
            return toCommunity(restTemplate.getForObject(url, CommunityDto.class));
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
        request.setDisplayName(displayName);
        request.setDescription(description);
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
    public List<Community> listCommunitiesByUserId(Long userId) {
        CommunityDto[] dtos = restTemplate.getForObject(
                clientConfig.getBaseUrl() + "/api/users/" + userId + "/communities",
                CommunityDto[].class
        );
        return Arrays.stream(dtos)
                .map(this::toCommunity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> listCommunityPosts(String name) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + name + "/posts";
        try {
            org.springframework.http.ResponseEntity<List<PostDto>> response = restTemplate.exchange(
                    url, org.springframework.http.HttpMethod.GET, null,
                    new org.springframework.core.ParameterizedTypeReference<List<PostDto>>() {});

            return response.getBody().stream().map(dto -> {
                Post post = new Post();
                post.setId(dto.getId());
                post.setTitle(dto.getTitle());
                post.setContent(dto.getContent());

                Community c = new Community();
                c.setId(dto.getCommunityId());
                c.setName(dto.getSubreddit());
                post.setSubreddit(c);

                User u = new User();
                u.setId(dto.getUserId());
                u.setUsername(dto.getAuthor());
                post.setAuthor(u);

                return post;
            }).collect(Collectors.toList());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    private Community toCommunity(CommunityDto dto) {
        if (dto == null) return null;
        Community community = new Community();
        community.setId(dto.getId());
        community.setName(dto.getName());
        community.setDisplayName(dto.getDisplayName());
        community.setDescription(dto.getDescription());
        community.setIconUrl(dto.getIconUrl());
        community.setCreatedAt(dto.getCreatedAt());
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