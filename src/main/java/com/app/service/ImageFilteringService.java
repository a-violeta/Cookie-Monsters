package com.app.service;

import com.app.util.SimpleMultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageFilteringService {

    private final RestTemplate restTemplate = new RestTemplate();

    // read base URL (without filter name)
    @Value("${app.microservices.image-filtering-url}")
    private String imageFilteringMicroserviceUrl;

    public MultipartFile applyFilter(MultipartFile originalImage, String filterName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", originalImage.getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // build dynamic URL (e.g. http://localhost:5296/filter/sepia)
            String dynamicUrl = imageFilteringMicroserviceUrl + "/" + filterName;

            ResponseEntity<byte[]> response = restTemplate.postForEntity(
                    dynamicUrl,
                    requestEntity,
                    byte[].class
            );

            return new SimpleMultipartFile(
                    response.getBody(),
                    originalImage.getName(),
                    filterName + "_" + originalImage.getOriginalFilename(),
                    "image/jpeg"
            );

        } catch (HttpClientErrorException e) {
            String errorMessageFromCSharp = e.getResponseBodyAsString();
            throw new IllegalArgumentException(errorMessageFromCSharp);

        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with image filtering service", e);
        }
    }
}