package com.app.console;

import com.app.model.*;
import com.app.service.*;

import java.util.List;

public class SeedData {

    private final UserUseCases userUseCases;
    private final CommunityService communityService;
    private final PostService postService;
    private final CommentService commentService;

    // users
    private User ion, anca, petru, adela, mihai, elena, radu, cristina;

    // communities
    private Community catLovers, ancaCommunity, gamers, bookClub, foodies;

    // posts
    private Post catPost1, catPost2, catPost3, gamePost1, gamePost2, bookPost1, foodPost1, foodPost2;

    public SeedData(UserUseCases userUseCases, CommunityService communityService,
                    PostService postService, CommentService commentService) {
        this.userUseCases = userUseCases;
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
        ion = userUseCases.createUser("Ion", "ion@gmail.com", "ion123", "some guy");
        anca = userUseCases.createUser("Anca", "anca@gmail.com", "anca123", "some girl");
        petru = userUseCases.createUser("Petru", "petru@gmail.com", "petru123", "guitarist");
        adela = userUseCases.createUser("Adela", "adela@gmail.com", "adela123", "physicist or smt");
        mihai = userUseCases.createUser("Mihai", "mihai@gmail.com", "mihai123", "backend dev, coffee addict");
        elena = userUseCases.createUser("Elena", "elena@gmail.com", "elena123", "reads too much sci-fi");
        radu = userUseCases.createUser("Radu", "radu@gmail.com", "radu123", "amateur chef");
        cristina = userUseCases.createUser("Cristina", "cristina@gmail.com", "cristina123", "speedrunner");
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
        catPost1 = postService.addPost(catLovers.getId(), ion.getId(), "First post about cats", "Cats are awesome");
        //attachImage(catPost1, "134110683555465878.jpg");

        catPost2 = postService.addPost(catLovers.getId(), anca.getId(), "My cat knocked over my plant again", "Third time this week. I've given up on plants.");

        catPost3 = postService.addPost(catLovers.getId(), petru.getId(), "Cat vs guitar", "She sits on the strings every single time I practice.");

        gamePost1 = postService.addPost(gamers.getId(), cristina.getId(), "New PB on my speedrun!", "Shaved off 40 seconds, finally under 2 hours.");

        gamePost2 = postService.addPost(gamers.getId(), radu.getId(), "What are you all playing this weekend?", "Looking for co-op recommendations.");

        bookPost1 = postService.addPost(bookClub.getId(), elena.getId(), "This month's pick: Project Hail Mary", "Starting Monday, discussion thread up next week.");

        foodPost1 = postService.addPost(foodies.getId(), radu.getId(), "Made carbonara from scratch", "No cream, I promise. Recipe in comments if anyone wants it.");

        foodPost2 = postService.addPost(foodies.getId(), mihai.getId(), "Best coffee spots near the office?", "Need something stronger than what the office machine makes.");
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
        commentService.addComment("So true", anca.getId(), catPost1.getId());
        commentService.addComment("Yesss", petru.getId(), catPost1.getId());

        commentService.addComment("Classic cat behavior honestly", petru.getId(), catPost2.getId());
        commentService.addComment("Mine does the same, get a cactus instead", ion.getId(), catPost2.getId());

        commentService.addComment("Lol get a cat-proof stand", anca.getId(), catPost3.getId());

        commentService.addComment("Nice! What route did you change?", radu.getId(), gamePost1.getId());
        commentService.addComment("That's insane, congrats", mihai.getId(), gamePost1.getId());

        commentService.addComment("I'm down, what time?", cristina.getId(), gamePost2.getId());

        commentService.addComment("Loved that one, great pick", adela.getId(), bookPost1.getId());
        commentService.addComment("Ordering it today", anca.getId(), bookPost1.getId());

        commentService.addComment("Yes please, share the recipe", mihai.getId(), foodPost1.getId());
        commentService.addComment("Looks so much better than mine", cristina.getId(), foodPost1.getId());

        commentService.addComment("Try the place two blocks from the station", petru.getId(), foodPost2.getId());
    }
}