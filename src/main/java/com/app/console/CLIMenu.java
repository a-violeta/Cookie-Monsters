package com.app.console;


import com.app.model.*;
import com.app.service.CommentService;
import com.app.service.CommunityService;
import com.app.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


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

        // seed data for testing:

        new SeedData(/*userService, */communityService, postService, commentService).seed();

        ConsoleReader consoleReader = new ConsoleReader();

        InputParser inputParser = new InputParser(consoleReader, communityService, commentService, postService/*, userService*/);

        inputParser.startListening();

    }
}