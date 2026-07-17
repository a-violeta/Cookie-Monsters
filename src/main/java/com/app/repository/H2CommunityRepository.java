package com.app.repository;

import com.app.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface H2CommunityRepository extends JpaRepository<Community, Long> {
    boolean existsByCommunityName(String communityName);
}