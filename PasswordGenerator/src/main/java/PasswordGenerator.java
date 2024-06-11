import java.util.ArrayList;
import java.util.Scanner;

public class PasswordGenerator {
    private static ArrayList<Option> options = new ArrayList<>();
    private static int length = 0;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Length: ");
        length = sc.nextInt();
        System.out.println("Numbers (Y/N): ");
        if (sc.next().toLowerCase() == "y"){
            options.add(Option.Numbers);
        }
        System.out.println("Symbols (Y/N): ");
        if (sc.next().toLowerCase() == "y")
            options.add(Option.Symbols);

    }
}
