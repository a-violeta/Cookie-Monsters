package com.app.console;

import com.app.model.*;
import com.app.service.CommentService;
import com.app.service.CommunityService;
import com.app.service.PostService;
//import com.app.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

public class SeedData {

    //private final UserService userService;
    private final CommunityService communityService;
    private final PostService postService;
    private final CommentService commentService;

    // users
    private User ion, anca, petru, adela, mihai, elena, radu, cristina;

    // communities
    private Community catLovers, ancaCommunity, gamers, bookClub, foodies;

    // posts
    private Post catPost1, catPost2, catPost3, gamePost1, gamePost2, bookPost1, foodPost1, foodPost2;

    public SeedData(/*UserService userService, */CommunityService communityService,
                    PostService postService, CommentService commentService) {
        //this.userService = userService;
        this.communityService = communityService;
        this.postService = postService;
        this.commentService = commentService;
    }

    public void seed() {
        seedUsers();
        seedCommunities();
        seedPosts();
        seedComments();
    }

    private void seedUsers() {
        ion = new User("Ion", "ion123", "some guy");
        anca = new User("Anca", "anca123", "some girl");
        petru = new User("Petru", "petru123", "guitarist");
        adela = new User("Adela", "adela123", "physicist or smt");
        mihai = new User("Mihai", "mihai123", "backend dev, coffee addict");
        elena = new User("Elena", "elena123", "reads too much sci-fi");
        radu = new User("Radu", "radu123", "amateur chef");
        cristina = new User("Cristina", "cristina123", "speedrunner");
    }

    private void seedCommunities() {
        catLovers = communityService.addCommunity(
                new Community("The cat lovers", "we really love cats", List.of(ion, anca, petru), null));

        ancaCommunity = communityService.addCommunity(
                new Community("Anca s community", "Anca is here", List.of(anca), null));

        gamers = communityService.addCommunity(
                new Community("Gamers United", "for anyone who games, casually or not", List.of(cristina, radu, mihai), null));

        bookClub = communityService.addCommunity(
                new Community("Monthly Book Club", "one book a month, no exceptions", List.of(elena, adela, anca), null));

        foodies = communityService.addCommunity(
                new Community("Foodies", "share recipes, rate restaurants, argue about pineapple on pizza", List.of(radu, petru, mihai, cristina), null));
    }

    private void seedPosts() {
        catPost1 = postService.addPost(catLovers.getId(),ion.getUserId(), "First post about cats", "Cats are awesome");
        //attachImage(catPost1, "134110683555465878.jpg");

        catPost2 = postService.addPost(catLovers.getId(), anca.getUserId(), "My cat knocked over my plant again", "Third time this week. I've given up on plants.");

        catPost3 = postService.addPost(catLovers.getId(), petru.getUserId(), "Cat vs guitar", "She sits on the strings every single time I practice.");

        gamePost1 = postService.addPost(gamers.getId(), cristina.getUserId(), "New PB on my speedrun!", "Shaved off 40 seconds, finally under 2 hours.");

        gamePost2 = postService.addPost(gamers.getId(), radu.getUserId(), "What are you all playing this weekend?", "Looking for co-op recommendations.");

        bookPost1 = postService.addPost(bookClub.getId(), elena.getUserId(), "This month's pick: Project Hail Mary", "Starting Monday, discussion thread up next week.");

        foodPost1 = postService.addPost(foodies.getId(), radu.getUserId(), "Made carbonara from scratch", "No cream, I promise. Recipe in comments if anyone wants it.");

        foodPost2 = postService.addPost(foodies.getId(), mihai.getUserId(), "Best coffee spots near the office?", "Need something stronger than what the office machine makes.");
    }

    /*private void attachImage(Post post, String fileName) {
        Media media = new Media(
                "C:\\Users\\iulia\\OneDrive\\Imagini\\" + fileName,
                fileName,
                LocalDateTime.now(),
                MediaType.IMAGE);
        post.setMedia(media);
        //postService.updatePost(post); // no update yet
    }*/

    private void seedComments() {
        commentService.addComment("So true", anca.getUserId(), catPost1.getId());
        commentService.addComment("Yesss", petru.getUserId(), catPost1.getId());

        commentService.addComment("Classic cat behavior honestly", petru.getUserId(), catPost2.getId());
        commentService.addComment("Mine does the same, get a cactus instead", ion.getUserId(), catPost2.getId());

        commentService.addComment("Lol get a cat-proof stand", anca.getUserId(), catPost3.getId());

        commentService.addComment("Nice! What route did you change?", radu.getUserId(), gamePost1.getId());
        commentService.addComment("That's insane, congrats", mihai.getUserId(), gamePost1.getId());

        commentService.addComment("I'm down, what time?", cristina.getUserId(), gamePost2.getId());

        commentService.addComment("Loved that one, great pick", adela.getUserId(), bookPost1.getId());
        commentService.addComment("Ordering it today", anca.getUserId(), bookPost1.getId());

        commentService.addComment("Yes please, share the recipe", mihai.getUserId(), foodPost1.getId());
        commentService.addComment("Looks so much better than mine", cristina.getUserId(), foodPost1.getId());

        commentService.addComment("Try the place two blocks from the station", petru.getUserId(), foodPost2.getId());
    }
}