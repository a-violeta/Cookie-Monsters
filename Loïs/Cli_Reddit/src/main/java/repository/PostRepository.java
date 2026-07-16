package repository;

import Model.Post;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    // La liste qui stocke tout en mémoire vive
    private final List<Post> posts = new ArrayList<>();

    public void save(Post post) {
        posts.add(post);
    }

    public List<Post> findAll() {
        return posts;
    }

    public Post findById(int id) {
        return posts.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}