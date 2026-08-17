package com.app.service;

import com.app.model.*;
import com.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "false", matchIfMissing = true)
public class PostService implements PostAbstract {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final PostVoteRepository postVoteRepository;
    private final ImageStorageService imageStorageService;
    private final ImageFilteringService imageFilteringService;

    public void validatePostImage(MultipartFile image) {
        // size validation (max 5 MB)
        long maxSizeBytes = 5 * 1024 * 1024;
        if (image.getSize() > maxSizeBytes) {
            throw new IllegalArgumentException("Image size must be less than 5 MB");
        }

        // format validation (only JPG and PNG)
        String contentType = image.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Only JPG and PNG formats are allowed");
        }
    }

    @Transactional
    public Post addPost(String title, String content, String subredditName, String requesterUsername,
                        MultipartFile image, Integer filter) {
        Community subreddit = communityRepository.findByName(subredditName)
                .orElseThrow(() -> new IllegalArgumentException("Subreddit " + subredditName + " not found"));

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        /*
        allow posting if you are not a member of the community
        because there is no option to join communities yet

        if (subreddit.findUserById(requester.getId()) == null) {
            throw new IllegalArgumentException("You are not a member of this community");
        }
        */

        Post post = new Post();

        post.setSubreddit(subreddit);
        post.setAuthor(requester);
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());
        post.setCommentList(new ArrayList<>());

        post.setUpvotes(1);
        post.setScore(1);

        post.setCommentCount(post.getCommentList().size());

        post.setUserVote("up");

        if (image != null && !image.isEmpty()) {
            validatePostImage(image);

            if (filter != null && filter == 1) {
                image = imageFilteringService.applyGrayscale(image);
            }

            String originalFileName = image.getOriginalFilename();
            String imageUrl = imageStorageService.saveImage(image);

            Media media = new Media(imageUrl, originalFileName, MediaType.IMAGE, filter);
            post.setMedia(media);
        }

        postRepository.save(post);

        PostVote postVote = new PostVote();
        postVote.setAuthor(requester);
        postVote.setPost(post);
        postVote.setVoteType(VoteType.UP);
        postVoteRepository.save(postVote);

        return post;
    }

    private void populateUserVoteStatus(Post post, User currentUser) {
        if (currentUser == null) {
            return;
        }

        postVoteRepository.findByPostAndAuthor(post, currentUser).ifPresent(vote -> {
            if (vote.getVoteType() != null) {
                post.setUserVote(vote.getVoteType().toString().toLowerCase());
            }
        });
    }

    @Transactional(readOnly = true)
    public Post findPostById(UUID postId, String requesterUsername) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Post with id " + postId + " not found"
                        ));

        User requester = null;

        if (requesterUsername != null) {
            requester = userRepository.findByUsername(requesterUsername).orElse(null);
        }

        final User finalRequester = requester;

        populateUserVoteStatus(post, finalRequester);
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> listPosts(UUID communityId) {
        Community subreddit = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community with id " + communityId + " not found"));

        List<Post> posts = new ArrayList<>();
        for (Post post : postRepository.findAll()) {
            if (Objects.equals(post.getSubreddit(), subreddit)) {
                posts.add(post);
            }
        }
        return posts;
    }

    @Transactional(readOnly = true)
    public List<Post> listPosts(String requesterUsername) {
        List<Post> posts = postRepository.findAll();

        User requester = null;

        if (requesterUsername != null) {
            requester = userRepository.findByUsername(requesterUsername).orElse(null);
        }

        final User finalRequester = requester;
        posts.forEach(post -> populateUserVoteStatus(post, finalRequester));
        return posts;
    }

    @Transactional
    public Post editPost(UUID postId, String newTitle, String newContent, String requesterUsername) {
        if (newTitle == null && newContent == null) {
            throw new IllegalArgumentException("At least one field must be provided to update the post");
        }

        Post post = findPostById(postId, requesterUsername);

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        if (!Objects.equals(post.getAuthor(), requester)) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        if (newTitle != null) {
            post.setTitle(newTitle);
        }

        if (newContent != null) {
            post.setContent(newContent);
        }

        post.setUpdatedAt(Instant.now());
        postRepository.save(post);
        return post;
    }

    @Transactional
    public void deletePost(UUID postId, String requesterUsername) {
        Post post = findPostById(postId, requesterUsername);

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        if (!Objects.equals(post.getAuthor(), requester)) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        post.setDeleted(true);
        postRepository.save(post);
    }

    @Transactional
    public Post votePost(UUID postId, String voteType, String requesterUsername) {
        Post post = findPostById(postId, requesterUsername);

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        PostVote postVote = postVoteRepository.findByPostAndAuthor(post, requester).orElse(null);

        if (postVote == null) {
            postVote = new PostVote();
            postVote.setPost(post);
            postVote.setAuthor(requester);
        }

        VoteType currentVote = postVote.getVoteType();

        // toggle logic: if voteType in request is the same as current vote, user intention is to cancel the vote
        if (("up".equals(voteType) && currentVote == VoteType.UP) ||
                ("down".equals(voteType) && currentVote == VoteType.DOWN)) {
            voteType = "none";
        }

        if (currentVote == VoteType.UP) {
            post.setUpvotes(post.getUpvotes() - 1);
        } else if (currentVote == VoteType.DOWN) {
            post.setDownvotes(post.getDownvotes() - 1);
        }

        switch (voteType) {
            case "up" -> {
                post.setUpvotes(post.getUpvotes() + 1);
                postVote.setVoteType(VoteType.UP);
            }
            case "down" -> {
                post.setDownvotes(post.getDownvotes() + 1);
                postVote.setVoteType(VoteType.DOWN);
            }
            case "none" -> postVote.setVoteType(null);
            case null, default -> throw new IllegalArgumentException("Invalid vote.");
        }

        post.setScore(post.getUpvotes() - post.getDownvotes());

        if (postVote.getVoteType() != null) {
            post.setUserVote(postVote.getVoteType().toString().toLowerCase());
        } else {
            post.setUserVote(null);
        }

        postVoteRepository.save(postVote);
        postRepository.save(post);
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> listPostsBySubreddit(String subredditName, String requesterUsername) {
        List<Post> posts = postRepository.findBySubredditName(subredditName);

        User requester = null;

        if (requesterUsername != null) {
            requester = userRepository.findByUsername(requesterUsername).orElse(null);
        }

        final User finalRequester = requester;

        posts.forEach(post -> populateUserVoteStatus(post, finalRequester));

        return posts;
    }
}