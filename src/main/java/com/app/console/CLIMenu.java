package com.app.console;


import com.app.model.Comment;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.service.CommunityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

@Component
public class CLIMenu implements CommandLineRunner {
    private final CommunityService communityService;

    public CLIMenu(CommunityService communityService){
        this.communityService = communityService;
    }

    @Override
    public void run(String... args){
        Scanner scanner = new Scanner(System.in);

        List<User> usersList = new ArrayList<>();
        List<Post> postsList = new ArrayList<>();

        User loggedInUser = null;
        // temporarily, Main class will keep track of the user who is currently logged in

        CommunityService communityService = CommunityService.getInstance();

        // seed data for testing:

        User user1 = new User("Ion", "ion123", "some guy");
        User user2 = new User("Anca", "anca123", "some girl");
        User user3 = new User("Petru", "petru123", "guitarist");
        User user4 = new User("Adela", "adela123", "physicist or smt");

        usersList.add(user1);
        usersList.add(user2);
        usersList.add(user3);
        usersList.add(user4);

        List<User> communityUsers1 = new ArrayList<>();
        communityUsers1.add(user1);
        communityUsers1.add(user2);
        communityUsers1.add(user3);
        Community community1 = new Community("The cat lovers", "we really love cats", communityUsers1, null);
        communityService.getApplicationCommunities().add(community1);

        List<User> communityUsers2 = new ArrayList<>();
        communityUsers2.add(user2);
        Community community2 = new Community("Anca s community", "Anca is here", communityUsers2, null);
        communityService.getApplicationCommunities().add(community2);

        Post post1 = new Post(1, 1, "First post about cats", "Cats are awesome", null);
        Post post2 = new Post(1, 2, "Second post abouts cats", "Cats are still awesome", null);
        postsList.add(post1);
        postsList.add(post2);
        List<Post> communityPosts1 = new ArrayList<>();
        communityPosts1.add(post1);
        communityPosts1.add(post2);
        community1.setCommunityPosts(communityPosts1);

        Comment comment1 = new Comment("So true", 2, 1);
        Comment comment2 = new Comment("Yesss", 3, 1);
        List<Comment> commentList1 = new ArrayList<>();
        commentList1.add(comment1);
        commentList1.add(comment2);
        post1.setCommentList(commentList1);

        while(true){
            System.out.println("1. Create user");
            System.out.println("2. Log into account");
            System.out.println("3. Log out of account");
            System.out.println("4. Post (just text)");
            System.out.println("5. Comment");
            System.out.println("6. Create community");
            System.out.println("7. List users");
            System.out.println("8. List posts");
            System.out.println("9. List comments");
            System.out.println("10. List communities");
            System.out.println("11. Delete user");
            System.out.println("12. Delete post");
            System.out.println("13. Delete comment");
            System.out.println("14. Delete community");
            System.out.println("0. Exit\n");

            System.out.print("Choose option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {

                case 1 -> {
                    // read username, password, description
                    // put a user in users list

                    System.out.println("--Create user--");
                    System.out.println("\nType a username: ");
                    String username = scanner.nextLine();
                    System.out.println("\nType a password: ");
                    String password = scanner.nextLine();
                    System.out.println("\nType a description: ");
                    String description = scanner.nextLine();

                    User user = new User(username, password, description);
                    usersList.add(user);

                    System.out.println();
                }

                case 2 -> {
                    System.out.println("--Logging into account--");
                    System.out.println();

                    // check that there is no user already logged-in
                    if(loggedInUser==null){

                        System.out.println("Type the username: ");
                        String username = scanner.nextLine();
                        System.out.println("\nType the password: ");
                        String password = scanner.nextLine();

                        // minimal log in:
                        boolean findUserAndCheckPassword = false;
                        for(User u: usersList){
                            if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                                loggedInUser = u;
                                findUserAndCheckPassword = true;
                                System.out.println("\nWelcome " + loggedInUser.getUsername() + "!");
                            }
                        }
                        if(!findUserAndCheckPassword){
                            System.out.println("Incorrect username or password!");
                        }
                    }
                    else{
                        System.out.println("User already logged-in!");
                    }

                    System.out.println();
                }

                case 3 -> {
                    System.out.println("--Logging out of account--");
                    System.out.println();

                    // minimal log out:
                    if(loggedInUser!=null){
                        loggedInUser = null;
                    }
                    else{
                        System.out.println("No logged-in user!");
                    }

                    System.out.println();
                }

                case 4 -> {
                    System.out.println("--Post--");
                    // community and type text, title

                    // check if there is a logged-in user
                    if(loggedInUser!=null){

                        // we should list all communities the user is part of (for him to choose from)
                        // for now, we read a community id and check if the user is part of that community

                        System.out.println("Type the community id: ");
                        long communityId = scanner.nextLong();

                        scanner.nextLine(); // consume newline

                        boolean findUserInCommunityUsers = false;
                        for(Community c: communityService.getApplicationCommunities()){
                            if(c.getCommunityId()==communityId){
                                for(User u: c.getCommunityUsers()){
                                    if(u.getUserId()==loggedInUser.getUserId()){
                                        findUserInCommunityUsers = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if(findUserInCommunityUsers){
                            System.out.println("Type post title: ");
                            String title = scanner.nextLine();

                            //scanner.nextLine(); // consume newline

                            System.out.println("Type post text: ");
                            String text = scanner.nextLine();

                            //scanner.nextLine(); // consume newline

                            if(!title.isEmpty() && !text.isEmpty()){
                                Post post = new Post(communityId, loggedInUser.getUserId(), title, text, null);
                                postsList.add(post);
                                System.out.println("\nPost made successfully!");
                            }
                            else{
                                System.out.println("Title and text cannot be empty!");
                            }
                        }
                        else{
                            System.out.println("The user is not part of that community!");
                        }
                    }
                    else{
                        System.out.println("There is no logged-in user!");
                    }
                }

                case 5 -> {
                    System.out.println("--Comment--");

                    // check text, userId and postId

                    // check if there is a logged-in user
                    if(loggedInUser!=null){

                        //check of there are any posts to comment on
                        if(postsList.isEmpty()){
                            System.out.println("There is no post to comment on!");
                        }
                        else{
                            // choose post to comment on

                            for (int i = 0; i < postsList.size(); i++) {
                                System.out.println(i+1 + ": " + postsList.get(i));
                            }

                            // check the number chosen for validity
                            System.out.println("Type a post id: ");
                            int chosenUserIndex = scanner.nextInt();
                            if (chosenUserIndex < 1 || chosenUserIndex > postsList.size()) {
                                System.out.println("\nInvalid index!");
                                break;
                            }

                            // check the current user is part of the community with that post
                            Post post = postsList.get(chosenUserIndex-1);

                            // search for the community of said post
                            long communityIdOfPost = post.getCommunityId();
                            Community communityOfPost = null;

                            for(Community c: communityService.getApplicationCommunities()){
                                if(c.getCommunityId()==communityIdOfPost){
                                    communityOfPost = c;
                                    break;
                                }
                            }

                            // now check if current user is part of that community
                            boolean userIsInCommunity = false;
                            for(User u: communityOfPost.getCommunityUsers()){
                                if(u.getUserId()==loggedInUser.getUserId()){
                                    userIsInCommunity = true;
                                    break;
                                }
                            }

                            if(userIsInCommunity){

                                scanner.nextLine(); // consume newline
                                System.out.println("\nType the text: ");
                                String text = scanner.nextLine();

                                if(!text.isEmpty()){
                                    Comment comment = new Comment(text, loggedInUser.getUserId(), post.getPostId());

                                    // check if the post has any comments yet
                                    // this is business logic, not for the menu
                                    // but it has to exist somewhere for this app to work in its current stage
                                    if(post.getCommentList()==null) {
                                        List<Comment> newCommentList = new ArrayList<>();
                                        newCommentList.add(comment);
                                        post.setCommentList(newCommentList);
                                    }
                                    else {
                                        post.getCommentList().add(comment);
                                    }

                                    System.out.println("\nComment made successfully!");
                                }
                                else{
                                    System.out.println("\nText empty!");
                                }
                            }
                            else{
                                System.out.println("The user is not part of the community where they want to comment!");
                            }
                        }
                    }
                    else{
                        System.out.println("\nThere is no user who can comment!");
                    }

                    System.out.println();
                }

                case 6 -> {
                    System.out.println("--Create community--");

                    // create description and members list

                    // check if there is a logged-in user
                    if(loggedInUser!=null){

                        // make the community members list
                        // a list of just 1 user, the creator
                        List<User> membersList = new ArrayList<>();
                        membersList.add(loggedInUser);

                        System.out.println("\nType community name: ");
                        String communityName = scanner.nextLine();

                        System.out.println("\nType community description: ");
                        String description = scanner.nextLine();

                        // make the community and add it to the list
                        Community newCommunity = new Community(communityName, description, membersList, null);
                        communityService.addCommunity(newCommunity);
                        System.out.println("\nCommunity successfully created!");
                    }
                    else{
                        System.out.println("There is no logged-in user!");
                    }

                    System.out.println();
                }

                case 7 -> {
                    System.out.println("--List users--\n");
                    if(usersList.isEmpty()){
                        System.out.println("There are no users yet!");
                        break;
                    }

                    for(User u: usersList){
                        System.out.println(u);
                    }

                    System.out.println();
                }

                case 8 -> {
                    System.out.println("--List posts--\n");
                    if(postsList.isEmpty()){
                        System.out.println("There are no posts to show yet!");
                        break;
                    }

                    for(Post p: postsList){
                        System.out.println(p);
                    }

                    System.out.println();
                }

                case 9 -> {
                    System.out.println("--List comments--\n");

                    // there is no list of all comments yet
                    // so we will go through all posts and print post + the comments

                    for(Post p: postsList){
                        System.out.println(p);
                        System.out.println("---------------------------------");
                        if(p.getCommentList()!=null && !p.getCommentList().isEmpty()) {
                            for (Comment c : p.getCommentList()) {
                                System.out.println("    " + c);
                            }
                            System.out.println();
                        }
                        else{
                            System.out.println("    No comments for this post");
                            System.out.println();
                        }
                    }
                }

                case 10 -> {
                    System.out.println("--List communities--");
                    System.out.println();

                    communityService.listCommunities();
                    System.out.println();
                }

                case 11 -> {
                    System.out.println("--Delete user--");
                    System.out.println();

                    // action for admin, but admin doesn't exist yet
                    // so there will need to be an additional check that loggedInUser is admin

                    // there needs to be someone logged in to do this action at least
                    if(loggedInUser!=null){

                        System.out.println("Type the username: ");
                        String username = scanner.nextLine().trim();

                        // search the user and delete from memory
                        // memory in this case is usersList

                        // user should also be deleted from all communities they are part of
                        // and if any one of those communities is now empty => delete it too
                        // right now it doesn't do that

                        // we can keep their posts and comments
                        // either we let them be and whenever we can't fetch the user (coz he doesn't exist) we display 'unknown user'
                        // or we make the userId from posts and comments be 0 or -1 or something distinct
                        // so we don't assume that not finding a user automatically means he s been deleted

                        boolean userFound = false;
                        Iterator<User> it = usersList.iterator();
                        // removing from list by using iterator
                        while(it.hasNext()){
                            User u = it.next();
                            if(u.getUsername().equals(username)){
                                it.remove();
                                userFound = true;
                                System.out.println("User found and deleted successfully!");
                                break;
                            }
                        }

                        if(!userFound){
                            System.out.println("User not found!");
                        }
                    }
                    else{
                        System.out.println("No logged-in user!");
                    }

                    System.out.println();
                }

                case 12 -> {
                    System.out.println("--Delete post--");
                    System.out.println();

                    // action for admin or community moderator or person who wrote the post
                    // but admin and moderator don't exist yet
                    // so there will need to be an additional check that loggedInUser is in role

                    // there needs to be someone logged in to do this action at least
                    if(loggedInUser!=null){

                        // check if there are any posts to delete
                        if(postsList.isEmpty()){
                            System.out.println("There is no post to delete!");
                        }
                        else{
                            // choose post to delete

                            for (int i = 0; i < postsList.size(); i++) {
                                System.out.println(i+1 + ": " + postsList.get(i));
                            }

                            // check the number chosen for validity
                            System.out.println("Type post id: ");
                            int chosenUserIndex = scanner.nextInt();
                            if (chosenUserIndex < 1 || chosenUserIndex > postsList.size()) {
                                System.out.println("\nInvalid index!");
                                break;
                            }

                            // check the current user is the one who wrote it
                            // or that current user has a role (admin or moderator within that community)
                            // but for now this is the only check

                            Post post = postsList.get(chosenUserIndex-1);

                            // check that current user wrote the post
                            if(post.getUserId()==loggedInUser.getUserId()){

                                scanner.nextLine(); // consume newline

                                // search for the post and delete from memory
                                // memory in this case is postsList
                                // and delete the post from its community

                                boolean foundPostInPostsList = false;
                                Iterator<Post> it = postsList.iterator();
                                // removing from list by using iterator
                                while(it.hasNext()){
                                    Post p = it.next();
                                    if(p.equals(post)){
                                        it.remove();
                                        foundPostInPostsList = true;
                                        break;
                                    }
                                }

                                // search for that community and delete the post from there too
                                boolean foundPostInCommunity = false;
                                for(Community c: communityService.getApplicationCommunities()){
                                    if(c.getCommunityId()==post.getCommunityId()){
                                        // found the community with that post

                                        Iterator<Post> it2 = c.getCommunityPosts().iterator();
                                        // removing from list by using iterator
                                        while(it2.hasNext()){
                                            Post p = it2.next();
                                            if(p.equals(post)){
                                                it2.remove();
                                                foundPostInCommunity = true;
                                                break;
                                            }
                                        }

                                    }
                                }

                                if(foundPostInCommunity && foundPostInPostsList){
                                    System.out.println("Post found and deleted successfully!");
                                }
                                else{
                                    System.out.println("Post missing from either the community or the postsList!");
                                }

                            }
                            else{
                                System.out.println("The user is not the one who wrote the post!");
                            }
                        }
                    }
                    else{
                        System.out.println("\nThere is no logged-in user!");
                    }

                    System.out.println();
                }

                case 13 -> {
                    System.out.println("--Delete comment--");
                    System.out.println();

                    // action for admin or community moderator or person who wrote the comment
                    // but admin and moderator don't exist yet
                    // so there will need to be an additional check that loggedInUser is in role

                    // there needs to be someone logged in to do this action at least
                    if(loggedInUser!=null){

                        // check if there are any posts (no post means no comment)
                        if(postsList.isEmpty()){
                            System.out.println("There is no comment to delete!");
                        }
                        else{

                            // print all comments and let the person type the corresponding commentId

                            for(Post p: postsList){
                                // for each post get the comments list and print
                                if(p.getCommentList()!=null && !p.getCommentList().isEmpty()) {
                                    for (Comment c : p.getCommentList()) {
                                        System.out.println(c);
                                    }
                                }
                            }

                            System.out.println("Type the comment id: ");
                            long deletingCommentId = scanner.nextLong();

                            // find that comment
                            Comment deletingComment = null;
                            Post postWithDeletingComment = null;

                            boolean foundComment = false;
                            for(Post p: postsList){
                                // for each post get the comments list and search for the comment
                                if(p.getCommentList()!=null && !p.getCommentList().isEmpty()) {
                                    for (Comment c : p.getCommentList()) {
                                        if(c.getCommentId()==deletingCommentId){
                                            foundComment = true;
                                            deletingComment = c;
                                            postWithDeletingComment = p;
                                        }
                                    }
                                }
                            }

                            if(foundComment){

                                // check the current user is the one who wrote it
                                // or that current user has a role (admin or moderator within that community)
                                // but for now this is the only check

                                // check that current user wrote the post
                                if(deletingComment.getUserId()==loggedInUser.getUserId()) {

                                    scanner.nextLine(); // consume newline

                                    // search for the comment and delete from memory
                                    // memory in this case is the list of comments from the post

                                    Iterator<Comment> it = postWithDeletingComment.getCommentList().iterator();
                                    // removing from list by using iterator
                                    while(it.hasNext()){
                                        Comment c = it.next();
                                        if(c.equals(deletingComment)){
                                            it.remove();
                                            break;
                                        }
                                    }
                                }
                                else{
                                    System.out.println("The current user is no");
                                }
                            }
                            else{
                                System.out.println("Comment not found!");
                            }
                        }
                    }
                    else{
                        System.out.println("\nThere is no logged-in user!");
                    }

                    System.out.println();
                }

                case 14 -> {
                    System.out.println("--Delete community--");
                    System.out.println();

                    // action for admin or community moderator or person who created the community
                    // but admin and moderator don't exist yet, and we don't know who made the community
                    // so there will need to be an additional check that loggedInUser is in role

                    // there needs to be someone logged in to do this action at least
                    if(loggedInUser!=null){

                        // print all communities and let person type the corresponding id
                        communityService.listCommunities();

                        System.out.println("Type the community id: ");
                        long deletingCommunityId = scanner.nextLong();

                        // find community by id
                        Community deletingCommunity = communityService.findCommunity(deletingCommunityId);

                        // check that the community is found
                        if(deletingCommunity!=null){

                            // check if current user is part of the community
                            boolean userIsInCommunity = false;
                            for(User u: deletingCommunity.getCommunityUsers()){
                                if(u.getUserId()==loggedInUser.getUserId()){
                                    userIsInCommunity = true;
                                    break;
                                }
                            }

                            if(userIsInCommunity){

                                // delete from communitiesList using communityId
                                communityService.removeCommunity(deletingCommunityId);

                                System.out.println("Community deleted successfully!");

                            }else{
                                System.out.println("User cannot delete a community they are not a part of!");
                            }
                        }
                        else{
                            System.out.println("Wrong community id!");
                        }
                    }
                    else{
                        System.out.println("\nThere is no logged-in user!");
                    }

                    System.out.println();
                }

                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}