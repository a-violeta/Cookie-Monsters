package consoleApp;

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

    void addPost(Post post){
        if(!post.getTitle().isEmpty()
                && !post.getText().isEmpty()
                && post.getCommunityId()==this.getCommunityId()
                && post.getUserId()!=0){
            // validate fields
            communityPosts.add(post);
        }
    }

    void removePost(long postId){
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

    void addUser(User user){
        if(!user.getUsername().isEmpty()
                && !user.getPassword().isEmpty()
                && user.getDescription().isEmpty()){
            // validate fields
            communityUsers.add(user);
        }
    }

    void removeUser(long userId){
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
}
