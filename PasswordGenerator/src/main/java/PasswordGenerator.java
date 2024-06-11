import java.util.ArrayList;
import java.util.Scanner;

public class PasswordGenerator {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            do {
                System.out.println("Email:");
                String email = sc.next();
                System.out.println("Master Password:");
                String masterPassword = sc.next();
                System.out.println("Character Set (default: d):");
                String characterSet = sc.next();
                System.out.println("Length:");
                int length = sc.nextInt();
                System.out.println("Your password:");
                System.out.println(Generator.getPassword(email, masterPassword, length,characterSet));

                System.out.print("Another? (Y/N): ");
            } while (sc.next().equalsIgnoreCase("y"));
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
