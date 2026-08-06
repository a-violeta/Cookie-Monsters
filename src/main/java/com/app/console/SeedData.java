package com.app.console;

import com.app.model.*;
import com.app.service.*;

public class SeedData {

    private final UserUseCases userUseCases;
    private final CommunityUseCases communityUseCases;
    private final PostUseCases postUseCases;
    private final CommentUseCases commentUseCases;

    // users
    private User ion, anca, petru, adela, mihai, elena, radu, cristina;

    // communities
    private Community catLovers, ancaCommunity, gamers, bookClub, foodies;

    // posts
    private Post catPost1, catPost2, catPost3, gamePost1, gamePost2, bookPost1, foodPost1, foodPost2;

    public SeedData(UserUseCases userUseCases, CommunityUseCases communityUseCases,
                    PostUseCases postUseCases, CommentUseCases commentUseCases) {
        this.userUseCases = userUseCases;
        this.communityUseCases = communityUseCases;
        this.postUseCases = postUseCases;
        this.commentUseCases = commentUseCases;
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
        userUseCases.login("Ion", "ion123");
        catLovers = communityUseCases.createCommunity("the_cat_lovers", "The cat lovers", "we really love cats", null);
        communityUseCases.joinCommunity(catLovers.getId(), anca.getId());
        communityUseCases.joinCommunity(catLovers.getId(), petru.getId());
        userUseCases.logout();

        userUseCases.login("Anca", "anca123");
        ancaCommunity = communityUseCases.createCommunity("anca_community", "Anca s community", "Anca is here", null);
        userUseCases.logout();

        userUseCases.login("Cristina", "cristina123");
        gamers = communityUseCases.createCommunity("gamers_united", "Gamers United", "for anyone who games, casually or not", null);
        communityUseCases.joinCommunity(gamers.getId(), radu.getId());
        communityUseCases.joinCommunity(gamers.getId(), mihai.getId());
        userUseCases.logout();

        userUseCases.login("Elena", "elena123");
        bookClub = communityUseCases.createCommunity("monthly_book_blub", "Monthly Book Club", "one book a month, no exceptions", null);
        communityUseCases.joinCommunity(bookClub.getId(), adela.getId());
        communityUseCases.joinCommunity(bookClub.getId(), anca.getId());
        userUseCases.logout();

        userUseCases.login("Radu", "radu123");
        foodies = communityUseCases.createCommunity("foodies", "Foodies", "share recipes, rate restaurants, argue about pineapple on pizza", null);
        communityUseCases.joinCommunity(foodies.getId(), petru.getId());
        communityUseCases.joinCommunity(foodies.getId(), mihai.getId());
        communityUseCases.joinCommunity(foodies.getId(), cristina.getId());
        userUseCases.logout();
    }

    private void seedPosts() {
        catPost1 = postUseCases.addPost("First post about cats", "Cats are awesome", "the_cat_lovers", ion.getUsername());

        //attachImage(catPost1, "134110683555465878.jpg");

        catPost2 = postUseCases.addPost("My cat knocked over my plant again", "Third time this week. I've given up on plants.", "the_cat_lovers", anca.getUsername());

        catPost3 = postUseCases.addPost("Cat vs guitar", "She sits on the strings every single time I practice.", "the_cat_lovers", petru.getUsername());

        gamePost1 = postUseCases.addPost("New PB on my speedrun!", "Shaved off 40 seconds, finally under 2 hours.", "gamers_united", cristina.getUsername());

        gamePost2 = postUseCases.addPost("What are you all playing this weekend?", "Looking for co-op recommendations.", "gamers_united", radu.getUsername());

        bookPost1 = postUseCases.addPost("This month's pick: Project Hail Mary", "Starting Monday, discussion thread up next week.", "monthly_book_blub", elena.getUsername());

        foodPost1 = postUseCases.addPost("Made carbonara from scratch", "No cream, I promise. Recipe in comments if anyone wants it.", "foodies", radu.getUsername());

        foodPost2 = postUseCases.addPost("Best coffee spots near the office?", "Need something stronger than what the office machine makes.", "foodies", mihai.getUsername());
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
        commentUseCases.addComment("So true", anca.getId(), catPost1.getId());
        commentUseCases.addComment("Yesss", petru.getId(), catPost1.getId());

        commentUseCases.addComment("Classic cat behavior honestly", petru.getId(), catPost2.getId());
        commentUseCases.addComment("Mine does the same, get a cactus instead", ion.getId(), catPost2.getId());

        commentUseCases.addComment("Lol get a cat-proof stand", anca.getId(), catPost3.getId());

        commentUseCases.addComment("Nice! What route did you change?", radu.getId(), gamePost1.getId());
        commentUseCases.addComment("That's insane, congrats", mihai.getId(), gamePost1.getId());

        commentUseCases.addComment("I'm down, what time?", cristina.getId(), gamePost2.getId());

        commentUseCases.addComment("Loved that one, great pick", adela.getId(), bookPost1.getId());
        commentUseCases.addComment("Ordering it today", anca.getId(), bookPost1.getId());

        commentUseCases.addComment("Yes please, share the recipe", mihai.getId(), foodPost1.getId());
        commentUseCases.addComment("Looks so much better than mine", cristina.getId(), foodPost1.getId());

        commentUseCases.addComment("Try the place two blocks from the station", petru.getId(), foodPost2.getId());
    }
}