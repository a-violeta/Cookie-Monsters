package com.app.repository;

import com.app.model.Community;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

// it s named Jpa because Jpa from Spring is an abstraction for any SQL database we might need
// so this is good for h2 database, postgres, whatever

//@Primary
// @Primary tells Spring which repo to choose for CommunityService
// because in CommunityService we have an instance of CommunityRepository which can be JpaCommunityRepository or InMemoryCommunityRepository
// without @Primary Spring would not know what bean to choose
public interface JpaCommunityRepository extends JpaRepository<Community, Long>, CommunityRepository {
    boolean existsByCommunityName(String communityName);
}