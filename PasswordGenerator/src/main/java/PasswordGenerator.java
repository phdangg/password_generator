import java.util.ArrayList;
import java.util.Scanner;

public class PasswordGenerator {
    private static final ArrayList<Option> options = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("Length: ");
            int length = sc.nextInt();
            System.out.println("Numbers (Y/N): ");
            if (sc.next().equalsIgnoreCase("y")) {
                options.add(Option.Numbers);
            }
            System.out.println("Symbols (Y/N): ");
            if (sc.next().equalsIgnoreCase("y"))
                options.add(Option.Symbols);

            Generator generator = new Generator(length, options);
            System.out.println(generator);

            System.out.print("Another? (Y/N): ");
        } while (sc.next().equalsIgnoreCase("y"));
    }
}
