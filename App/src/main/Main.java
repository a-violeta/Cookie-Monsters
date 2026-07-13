package main;

import main.model.Comment;
import main.model.Community;
import main.model.Post;
import main.model.User;
import main.service.CommunityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
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
            System.out.println("1. Create account");
            System.out.println("2. Log into account");
            System.out.println("3. Log out of account");
            System.out.println("4. Post (just text)");
            System.out.println("5. Comment");
            System.out.println("6. Create community");
            System.out.println("7. List users");
            System.out.println("8. List posts");
            System.out.println("9. List comments");
            System.out.println("10. List communities");
            System.out.println("0. Exit\n");

            System.out.print("Choose option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {

                case 1 -> {
                    // read username, password, description
                    // put a user in users list

                    System.out.println("--Creating account--");
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
                            int chosenUserIndex = scanner.nextInt();
                            if (chosenUserIndex < 1 || chosenUserIndex > postsList.size()) {
                                System.out.println("\nInvalid index!");
                                break;
                            }

                            Post post = postsList.get(chosenUserIndex-1);

                            scanner.nextLine(); // consume newline
                            System.out.println("\nType the text: ");
                            String text = scanner.nextLine();

                            if(!text.isEmpty()){
                                Comment comment = new Comment(text, loggedInUser.getUserId(), post.getPostId());
                                post.getCommentList().add(comment);
                                // there is no list of all comments yet
                                System.out.println("\nComment made successfully!");
                            }
                            else{
                                System.out.println("\nText empty!");
                            }
                        }
                    }
                    else{
                        System.out.println("\nThere is no user who can comment!");
                    }
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

                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}