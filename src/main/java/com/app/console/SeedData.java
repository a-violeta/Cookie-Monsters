package com.app.console;

import com.app.model.*;
import com.app.service.*;

import java.util.List;

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
        catLovers = communityUseCases.createCommunity("The cat lovers", "we really love cats");
        catLovers.setCommunityUsers(List.of(ion, anca, petru));
        userUseCases.logout();

        userUseCases.login("Anca", "anca123");
        ancaCommunity = communityUseCases.createCommunity("Anca s community", "Anca is here");
        ancaCommunity.setCommunityUsers(List.of(anca));
        userUseCases.logout();

        userUseCases.login("Cristina", "cristina123");
        gamers = communityUseCases.createCommunity("Gamers United", "for anyone who games, casually or not");
        gamers.setCommunityUsers(List.of(cristina, radu, mihai));
        userUseCases.logout();

        userUseCases.login("Elena", "elena123");
        bookClub = communityUseCases.createCommunity("Monthly Book Club", "one book a month, no exceptions");
        bookClub.setCommunityUsers(List.of(elena, adela, anca));
        userUseCases.logout();

        userUseCases.login("Radu", "radu123");
        foodies = communityUseCases.createCommunity("Foodies", "share recipes, rate restaurants, argue about pineapple on pizza");
        foodies.setCommunityUsers(List.of(radu, petru, mihai, cristina));
        userUseCases.logout();
    }

    private void seedPosts() {
        catPost1 = postUseCases.addPost(catLovers.getId(), ion.getId(), "First post about cats", "Cats are awesome");
        //attachImage(catPost1, "134110683555465878.jpg");

        catPost2 = postUseCases.addPost(catLovers.getId(), anca.getId(), "My cat knocked over my plant again", "Third time this week. I've given up on plants.");

        catPost3 = postUseCases.addPost(catLovers.getId(), petru.getId(), "Cat vs guitar", "She sits on the strings every single time I practice.");

        gamePost1 = postUseCases.addPost(gamers.getId(), cristina.getId(), "New PB on my speedrun!", "Shaved off 40 seconds, finally under 2 hours.");

        gamePost2 = postUseCases.addPost(gamers.getId(), radu.getId(), "What are you all playing this weekend?", "Looking for co-op recommendations.");

        bookPost1 = postUseCases.addPost(bookClub.getId(), elena.getId(), "This month's pick: Project Hail Mary", "Starting Monday, discussion thread up next week.");

        foodPost1 = postUseCases.addPost(foodies.getId(), radu.getId(), "Made carbonara from scratch", "No cream, I promise. Recipe in comments if anyone wants it.");

        foodPost2 = postUseCases.addPost(foodies.getId(), mihai.getId(), "Best coffee spots near the office?", "Need something stronger than what the office machine makes.");
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