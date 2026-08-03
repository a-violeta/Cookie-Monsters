package com.app.client;

import com.app.dto.CommunityDto;
import com.app.model.Community;
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
import java.util.stream.Collectors;

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
    public void validateCommunity(String communityName, String description) {
        // pure validation, no I/O, mirrors CommunityService, safe to duplicate
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
    public Community createCommunity(String communityName, String description) {
        validateCommunity(communityName, description);
        String url = clientConfig.getBaseUrl() + "/api/communities";

        CommunityDto request = new CommunityDto();
        request.setCommunityName(communityName);
        request.setDescription(description);

        try {
            CommunityDto response = restTemplate.postForObject(url, request, CommunityDto.class);
            log.info("Community created via HTTP: {}", communityName);
            return toCommunity(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void deleteCommunity(long communityId) {
        String url = clientConfig.getBaseUrl() + "/api/communities/" + communityId;
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public List<Community> listCommunities() {
        String url = clientConfig.getBaseUrl() + "/api/communities";
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
    public Community findCommunityById(long communityId) {
        String url = clientConfig.getBaseUrl() + "/api/communities/" + communityId;
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
        String url = clientConfig.getBaseUrl() + "/api/communities/name/" + name;
        try {
            return toCommunity(restTemplate.getForObject(url, CommunityDto.class));
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Community with name " + name + " not found");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void editCommunity(long communityId, String description) {
        String url = clientConfig.getBaseUrl() + "/api/communities/" + communityId;
        CommunityDto request = new CommunityDto();
        request.setDescription(description);
        try {
            restTemplate.put(url, request);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void joinCommunity(Long communityId, Long userId) {
        String url = clientConfig.getBaseUrl() + "/api/communities/" + communityId + "/members/" + userId;
        try {
            restTemplate.postForLocation(url, null);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void exitCommunity(Long communityId, Long userId) {
        String url = clientConfig.getBaseUrl() + "/api/communities/" + communityId + "/members/" + userId;
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

    private Community toCommunity(CommunityDto dto) {
        if (dto == null) return null;
        Community community = new Community();
        community.setId(dto.getId());
        community.setCommunityName(dto.getCommunityName());
        community.setDescription(dto.getDescription());
        community.setCreatedAt(dto.getCreatedAt());
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