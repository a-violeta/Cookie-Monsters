package com.app.console.core;

import com.app.model.*;
import com.app.service.*;

public class SeedData {

    private final UserAbstract userAbstract;
    private final CommunityAbstract communityAbstract;
    private final PostAbstract postAbstract;
    private final CommentAbstract commentAbstract;

    // users
    private User ion, anca, petru, adela, mihai, elena, radu, cristina;

    // posts
    private Post catPost1, catPost2, catPost3, gamePost1, gamePost2, bookPost1, foodPost1, foodPost2;

    public SeedData(UserAbstract userAbstract, CommunityAbstract communityAbstract,
                    PostAbstract postAbstract, CommentAbstract commentAbstract) {
        this.userAbstract = userAbstract;
        this.communityAbstract = communityAbstract;
        this.postAbstract = postAbstract;
        this.commentAbstract = commentAbstract;
    }

    public void seed() {
        seedUsers();
        seedCommunities();
        seedPosts();
        seedComments();
    }

    private void seedUsers() {
        ion = userAbstract.createUser("Ion", "ion@gmail.com", "ion123", "some guy");
        anca = userAbstract.createUser("Anca", "anca@gmail.com", "anca123", "some girl");
        petru = userAbstract.createUser("Petru", "petru@gmail.com", "petru123", "guitarist");
        adela = userAbstract.createUser("Adela", "adela@gmail.com", "adela123", "physicist or smt");
        mihai = userAbstract.createUser("Mihai", "mihai@gmail.com", "mihai123", "backend dev, coffee addict");
        elena = userAbstract.createUser("Elena", "elena@gmail.com", "elena123", "reads too much sci-fi");
        radu = userAbstract.createUser("Radu", "radu@gmail.com", "radu123", "amateur chef");
        cristina = userAbstract.createUser("Cristina", "cristina@gmail.com", "cristina123", "speedrunner");
    }

    private void seedCommunities() {
        userAbstract.login("Ion", "ion123");
        // communities
        Community catLovers = communityAbstract.createCommunity("the_cat_lovers", "The cat lovers", "we really love cats", null, ion.getUsername());
        communityAbstract.joinCommunity(catLovers.getId(), anca.getId());
        communityAbstract.joinCommunity(catLovers.getId(), petru.getId());
        userAbstract.logout();

        userAbstract.login("Anca", "anca123");
        Community ancaCommunity = communityAbstract.createCommunity("anca_community", "Anca s community", "Anca is here", null, anca.getUsername());
        userAbstract.logout();

        userAbstract.login("Cristina", "cristina123");
        Community gamers = communityAbstract.createCommunity("gamers_united", "Gamers United", "for anyone who games, casually or not", null, cristina.getUsername());
        communityAbstract.joinCommunity(gamers.getId(), radu.getId());
        communityAbstract.joinCommunity(gamers.getId(), mihai.getId());
        userAbstract.logout();

        userAbstract.login("Elena", "elena123");
        Community bookClub = communityAbstract.createCommunity("monthly_book_blub", "Monthly Book Club", "one book a month, no exceptions", null, elena.getUsername());
        communityAbstract.joinCommunity(bookClub.getId(), adela.getId());
        communityAbstract.joinCommunity(bookClub.getId(), anca.getId());
        userAbstract.logout();

        userAbstract.login("Radu", "radu123");
        Community foodies = communityAbstract.createCommunity("foodies", "Foodies", "share recipes, rate restaurants, argue about pineapple on pizza", null, radu.getUsername());
        communityAbstract.joinCommunity(foodies.getId(), petru.getId());
        communityAbstract.joinCommunity(foodies.getId(), mihai.getId());
        communityAbstract.joinCommunity(foodies.getId(), cristina.getId());
        userAbstract.logout();
    }

    private void seedPosts() {
        catPost1 = postAbstract.addPost("First post about cats", "Cats are awesome", "the_cat_lovers", ion.getUsername(), null, 1);
        catPost2 = postAbstract.addPost("My cat knocked over my plant again", "Third time this week. I've given up on plants.", "the_cat_lovers", anca.getUsername(), null, 1);
        catPost3 = postAbstract.addPost("Cat vs guitar", "She sits on the strings every single time I practice.", "the_cat_lovers", petru.getUsername(), null, 1);
        gamePost1 = postAbstract.addPost("New PB on my speedrun!", "Shaved off 40 seconds, finally under 2 hours.", "gamers_united", cristina.getUsername(), null, 1);
        gamePost2 = postAbstract.addPost("What are you all playing this weekend?", "Looking for co-op recommendations.", "gamers_united", radu.getUsername(), null, 1);
        bookPost1 = postAbstract.addPost("This month's pick: Project Hail Mary", "Starting Monday, discussion thread up next week.", "monthly_book_blub", elena.getUsername(), null, 1);
        foodPost1 = postAbstract.addPost("Made carbonara from scratch", "No cream, I promise. Recipe in comments if anyone wants it.", "foodies", radu.getUsername(), null, 1);
        foodPost2 = postAbstract.addPost("Best coffee spots near the office?", "Need something stronger than what the office machine makes.", "foodies", mihai.getUsername(), null, 1);
    }

    private void seedComments() {
        commentAbstract.addComment("So true", catPost1.getId(), null,anca.getUsername());
        commentAbstract.addComment("Yesss", catPost1.getId(), null, petru.getUsername());
        commentAbstract.addComment("Classic cat behavior honestly", catPost2.getId(), null, petru.getUsername());
        commentAbstract.addComment("Mine does the same, get a cactus instead", catPost2.getId(), null, ion.getUsername());
        commentAbstract.addComment("Lol get a cat-proof stand", catPost3.getId(), null, anca.getUsername());
        commentAbstract.addComment("Nice! What route did you change?", gamePost1.getId(), null, radu.getUsername());
        commentAbstract.addComment("That's insane, congrats", gamePost1.getId(), null, mihai.getUsername());
        commentAbstract.addComment("I'm down, what time?", gamePost2.getId(), null, cristina.getUsername());
        commentAbstract.addComment("Loved that one, great pick", bookPost1.getId(), null, adela.getUsername());
        commentAbstract.addComment("Ordering it today", bookPost1.getId(), null, anca.getUsername());
        commentAbstract.addComment("Yes please, share the recipe", foodPost1.getId(), null, mihai.getUsername());
        commentAbstract.addComment("Looks so much better than mine", foodPost1.getId(), null, cristina.getUsername());
        commentAbstract.addComment("Try the place two blocks from the station", foodPost2.getId(), null, petru.getUsername());
    }
}