import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class InputHandler {

    private static final String STOP_SIGNAL = "END_INPUT";

   
    public List<String> readInput() {
        Scanner scanner = new Scanner(System.in);
        List<String> rawLines = new ArrayList<>();

        System.out.println();
        System.out.println("==========================================");
        System.out.println("         MINI CODE ANALYZER  v1.0        ");
        System.out.println("==========================================");
        System.out.println();
        System.out.println("  Enter your source code line by line.");
        System.out.println("  Type END_INPUT when you are done.");
        System.out.println("------------------------------------------");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equals(STOP_SIGNAL)) {
                break;
            }
            rawLines.add(line);
        }

        return cleanInput(rawLines);
    }

    
    public List<String> readFromString(String code) {
        List<String> rawLines = new ArrayList<>();
        String[] lines = code.split("\n");
        for (String line : lines) {
            rawLines.add(line);
        }
        return cleanInput(rawLines);
    }

   
    private List<String> cleanInput(List<String> rawLines) {
        List<String> cleaned = new ArrayList<>();

        for (String line : rawLines) {
            String trimmed = line.trim();

            if (trimmed.isEmpty()) {
                continue;                   
            }
            if (trimmed.startsWith("//")) {
                continue;                  
            }

            cleaned.add(trimmed);
        }

        return cleaned;
    }
}
