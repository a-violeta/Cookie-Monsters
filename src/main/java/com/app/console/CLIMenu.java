package com.app.console;


import com.app.model.*;
import com.app.service.CommentService;
import com.app.service.CommunityService;
import com.app.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("cli")
public class CLIMenu implements CommandLineRunner {
    private final CommunityService communityService;
    private final CommentService commentService;
    private final PostService postService;
    //private final UserService userService;

    public CLIMenu(CommunityService communityService, CommentService commentService, PostService postService/*, UserService userService*/){
        this.communityService = communityService;
        this.commentService=commentService;
        this.postService=postService;
        //this.userService=userService;
    }

    @Override
    public void run(String... args){

        List<User> usersList = new ArrayList<>();
        List<Post> postsList = new ArrayList<>();

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
        communityService.addCommunity(community1);

        List<User> communityUsers2 = new ArrayList<>();
        communityUsers2.add(user2);
        Community community2 = new Community("Anca s community", "Anca is here", communityUsers2, null);
        communityService.addCommunity(community2);

        Post post1 = new Post(1, 1, "First post about cats", "Cats are awesome", null);
        Post post2 = new Post(1, 2, "Second post abouts cats", "Cats are still awesome", null);
        Media image1 = new Media("C:\\Users\\iulia\\OneDrive\\Imagini\\134110683555465878.jpg", "134110683555465878.jpg", LocalDateTime.now(), MediaType.IMAGE);
        post1.setMedia(image1);
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

        ConsoleReader consoleReader = new ConsoleReader();
        ConsolePrinter consolePrinter = new ConsolePrinter();

        consolePrinter.printBanner();

        InputParser inputParser = new InputParser(consoleReader, consolePrinter, communityService, commentService, postService/*, userService*/);

        inputParser.startListening();

    }
}