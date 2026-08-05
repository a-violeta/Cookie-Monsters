package com.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

// DTO just for updating community because in CommunityDto displayName, description and iconUrl are not optional

@Data
public class CommunityUpdateRequest {
    @Size(min = 3, message = "Display name must have at least 3 characters")
    @Size(max = 100, message = "Display name is too long")
    private String displayName; // null = unchanged

    private String iconUrl; // null = unchanged

    @Size(max = 500, message = "Description is too long")
    private String description; // null = unchanged
}