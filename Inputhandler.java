import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * InputHandler.java
 * -----------------
 * Responsible for:
 *   1. Accepting multi-line source code from the user via console.
 *   2. Cleaning the input: trimming whitespace, removing blank lines.
 *
 * The user types code line by line and signals "done" by typing END_INPUT.
 */
public class InputHandler {

    private static final String STOP_SIGNAL = "END_INPUT";

    /**
     * Reads code lines from the console interactively.
     * Stops when the user types END_INPUT.
     *
     * @return A cleaned list of non-empty, trimmed code lines.
     */
    public List<String> readInput() {
        Scanner scanner = new Scanner(System.in);
        List<String> rawLines = new ArrayList<>();

        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│        MINI CODE ANALYZER  v1.0         │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.println();
        System.out.println("  Enter your source code line by line.");
        System.out.println("  Type  END_INPUT  when you are done.");
        System.out.println("─────────────────────────────────────────────");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equals(STOP_SIGNAL)) {
                break;
            }
            rawLines.add(line);
        }

        return cleanInput(rawLines);
    }

    /**
     * Reads code from a pre-built string — used for demo / testing.
     *
     * @param code Raw source code as a single multi-line string.
     * @return A cleaned list of non-empty, trimmed code lines.
     */
    public List<String> readFromString(String code) {
        List<String> rawLines = new ArrayList<>();
        for (String line : code.split("\n")) {
            rawLines.add(line);
        }
        return cleanInput(rawLines);
    }

    /**
     * Cleans the raw input:
     *   - Trims leading/trailing whitespace from each line.
     *   - Removes completely blank lines.
     *   - Removes single-line comment-only lines (// ...).
     *
     * @param rawLines The unprocessed list of lines.
     * @return Cleaned list ready for classification.
     */
    private List<String> cleanInput(List<String> rawLines) {
        List<String> cleaned = new ArrayList<>();

        for (String line : rawLines) {
            String trimmed = line.trim();

            // Skip blank lines
            if (trimmed.isEmpty()) {
                continue;
            }

            // Skip comment-only lines
            if (trimmed.startsWith("//")) {
                continue;
            }

            cleaned.add(trimmed);
        }

        return cleaned;
    }
}