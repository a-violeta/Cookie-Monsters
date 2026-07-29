package com.app.mapper;

import com.app.dto.PostDto;
import com.app.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Post -> PostDto mapping (one direction only, deliberately).
 *
 * WHY THIS MAPPER EXISTS: Post holds real Community/User associations, but
 * PostDto flattens those to communityId/userId (+ display name/username) --
 * see PostDto's own class comment for why. MapStruct's @Mapping with a
 * `source` just walks the already-loaded association
 * (post.getCommunity().getId(), post.getUser().getUsername()) -- no extra
 * database hit, since PostService already loaded those objects before this
 * mapper ever runs.
 *
 * WHY THERE IS NO fromDto(): building a real Post back from a PostDto would
 * require looking up the Community/User by id AND re-running the membership
 * check ("is this user actually part of this community?") that lives in
 * PostService.addPost(). If this mapper did that lookup itself, we'd either
 * duplicate that validation here or silently skip it -- both bad. So the
 * controller never asks this mapper to build a Post; it unpacks the DTO's
 * primitive fields (communityId, userId, title, text) and calls
 * PostService.addPost(...) directly, letting the service own that logic
 * the way it always has.
 */

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "communityId", source = "community.id")
    @Mapping(target = "communityName", source = "community.communityName")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    PostDto toDto(Post post);
}