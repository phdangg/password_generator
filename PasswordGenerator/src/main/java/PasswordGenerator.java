import java.util.ArrayList;
import java.util.Scanner;

public class PasswordGenerator {

    public static void main(String[] args) {
        CharacterGroup[] groups = {
                CharacterGroup.UPPER,
                CharacterGroup.LOWER,
                CharacterGroup.SYMBOL,
                CharacterGroup.NUMBER
        };

        String[] prompts = {
                "Uppercase (y/n): ",
                "Lowercase (y/n): ",
                "Symbol (y/n): ",
                "Number (y/n): "
        };
        Scanner sc = new Scanner(System.in);
        try {
            do {
                System.out.println("Email:");
                String email = sc.next();
                System.out.println("Master Password:");
                String masterPassword = sc.next();
                ArrayList<CharacterGroup> options = new ArrayList<>();
                for (int i = 0; i < groups.length; i++) {
                    System.out.println(prompts[i]);
                    if (sc.next().equalsIgnoreCase("y")) {
                        options.add(groups[i]);
                    }
                }
                System.out.println("Length:");
                int length = sc.nextInt();
                System.out.println("Your password:");
                System.out.println(new Generator(masterPassword,length,options,email));
                System.out.print("Another? (Y/N): ");
            } while (sc.next().equalsIgnoreCase("y"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
