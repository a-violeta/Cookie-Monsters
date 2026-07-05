package main.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Community {
    private static int nextId = 1;

    private int communityId;
    private String communityName;
    private String description;
    private String creationDate;
    private List<User> members;
    private List<Post> posts;

    public Community(String communityName, String description, User creator) {
        this.communityId = nextId++;

        if (communityName == null || communityName.isBlank()) {
            throw new IllegalArgumentException("Community name is required");
        }

        if (!communityName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Community name must contain only letters, numbers, and '_'");
        }

        if (communityName.length() < 3) {
            throw new IllegalArgumentException("Community name must have at least 3 characters");
        }

        if (communityName.length() > 21) {
            throw new IllegalArgumentException("Community name is too long");
        }

        this.communityName = communityName;

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Community description is required");
        }

        this.description = description;

        this.creationDate = LocalDate.now().toString();

        this.members = new ArrayList<>();
        this.members.add(creator);

        this.posts = new ArrayList<>();
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public List<User> getMembers() {
        return members;
    }

    public User addMember(User newMember) {
        if (members.contains(newMember)) {
            throw new IllegalArgumentException("Member is already part of the community");
        }

        members.add(newMember);
        return newMember;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Post addPost(String title, String text, int userId) {
        Post newPost = new Post(title, text, userId);
        newPost.setCommunityId(communityId);
        posts.add(newPost);

        return newPost;
    }

    public void removePost(int postId) {
        // removeIf returns false if no elements were removed
        if (!posts.removeIf(post -> post.getPostId() == postId)) {
            throw new IllegalArgumentException("Post with id " + postId + " not found");
        }
    }

    @Override
    public String toString() {
        return
                "\nCommunity id: " + communityId + '\n' +
                "Community name: " + communityName + '\n' +
                "Description: " + description + '\n' +
                "Creation date: " + creationDate + '\n' +
                "\nMembers: \n" + members + '\n' +
                "\nPosts: \n" + posts + '\n';
    }
}
