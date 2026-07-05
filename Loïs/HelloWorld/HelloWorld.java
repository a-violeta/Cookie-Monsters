import java.util.Scanner; 

public class HelloWorld {
    public static void main(String[] args){
        
        // Objet Scanner
        Scanner clavier = new Scanner(System.in);
        
        // Invocation de la méthode print sur l'objet System.out
        System.out.print("Quel est ton nom ? ");
        
        // Déclaration d'une variable de type String et affectation via la méthode nextLine
        String nom = clavier.nextLine();
        
        // Concaténation de chaînes et affichage
        System.out.println("Hello World " + nom + " !!");
        
        // Libération des ressources
        clavier.close();
    }
}