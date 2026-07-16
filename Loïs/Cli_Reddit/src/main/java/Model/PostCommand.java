package Model;

import View.ConsolePrinter;
import repository.PostRepository;

public class PostCommand implements Command {
    private final PostRepository repository;
    private final ConsolePrinter printer;

    // Injection de dépendances : on fournit la DB et la Vue à la création
    public PostCommand(PostRepository repository, ConsolePrinter printer) {
        this.repository = repository;
        this.printer = printer;
    }

    @Override
    public void execute(String[] args) {
        // 1. Validation : On vérifie qu'on a bien les 3 arguments attendus
        if (args.length < 3) {
            printer.printError("Format invalide. Utilisation : post \"Titre\" \"Contenu\" \"Communaute\"");
            return;
        }

        // 2. Extraction des données
        String title = args[0];
        String body = args[1];
        String community = args[2];

        // 3. Création et sauvegarde
        Post newPost = new Post(title, body, community);
        repository.save(newPost);

        // 4. Affichage
        printer.printSuccess("Votre post a été publié avec succès !");
        printer.displayPost(newPost);
    }
}
