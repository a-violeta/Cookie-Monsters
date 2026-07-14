package main.model;

import java.util.Iterator;
import java.util.List;

public class Community {

    private static long idIncrementor = 0;
    // for id uniqueness, ids given will be 1, then 2, 3 ...

    private static long incrementId(){
        idIncrementor++;
        return idIncrementor;
    }

    private long communityId;
    private String communityName;
    private String description;
    private List<User> communityUsers;
    private List<Post> communityPosts;

    public Community(){
        this.communityId=incrementId();
        this.communityName="";
        this.description="";
        this.communityUsers=null;
        this.communityPosts=null;
    }

    public Community(String communityName, String description, List<User> communityUsers, List<Post> communityPosts){
        this.communityId=incrementId();
        this.communityName=communityName;
        this.description=description;
        this.communityUsers=communityUsers;
        this.communityPosts=communityPosts;
    }

    public long getCommunityId() {
        return communityId;
    }
    public String getDescription() {
        return description;
    }
    public List<Post> getCommunityPosts() {
        return communityPosts;
    }
    public List<User> getCommunityUsers() {
        return communityUsers;
    }
    public String getCommunityName() {
        return communityName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
    public void setCommunityPosts(List<Post> communityPosts) {
        this.communityPosts = communityPosts;
    }
    public void setCommunityUsers(List<User> communityUsers) {
        this.communityUsers = communityUsers;
    }

    public void addPost(Post post){
        if(!post.getTitle().isEmpty()
                && !post.getText().isEmpty()
                && post.getCommunityId()==this.getCommunityId()
                && post.getUserId()!=0){
            // validate fields
            communityPosts.add(post);
        }
    }

    public void removePost(long postId){
        Iterator<Post> it = communityPosts.iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            Post p = it.next();
            if(p.getPostId() == postId){
                it.remove();
                break;
            }
        }
    }

    public Post findPostById(long postId){

        // if there are any posts at all, we search
        if(this.getCommunityPosts()!=null && !this.getCommunityPosts().isEmpty()) {
            for (Post p : this.getCommunityPosts()) {
                if(p.getPostId()==postId){
                    return p;
                }
            }
        }
        return null;
    }

    public void addUser(User user){
        if(!user.getUsername().isEmpty()
                && !user.getPassword().isEmpty()
                && user.getDescription().isEmpty()){
            // validate fields
            communityUsers.add(user);
        }
    }

    public void removeUser(long userId){
        Iterator<User> it = communityUsers.iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            User u = it.next();
            if(u.getUserId() == userId){
                it.remove();
                break;
            }
        }
    }

    public User findUserById(long userId){

        // if there are any users at all, we search
        if(this.getCommunityUsers()!=null && !this.getCommunityUsers().isEmpty()) {
            for (User u : this.getCommunityUsers()) {
                if(u.getUserId()==userId){
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Community{" +
                "communityId=" + communityId +
                ", communityName='" + communityName + '\'' +
                ", description='" + description + '\'' +
                ", communityUsers=" + communityUsers +
                ", communityPosts=" + communityPosts +
                '}';
    }

    @Override
    public int hashCode() {
        return Long.hashCode(communityId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Community)) {
            return false;
        }

        Community other = (Community) obj;
        return communityId == other.communityId;
    }
}
