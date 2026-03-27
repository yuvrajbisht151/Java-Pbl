import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * InputHandler.java
 * -----------------
 * Responsibilities:
 *   1. Accept multi-line source code from the user via the console.
 *   2. Clean the input: trim whitespace, remove blank lines, skip comments.
 *
 * Two modes:
 *   - readInput()          : live console mode (type END_INPUT to finish)
 *   - readFromString(code) : demo/test mode using a pre-built string
 */
public class InputHandler {

    private static final String STOP_SIGNAL = "END_INPUT";

    /**
     * Reads code interactively from the console.
     * The user types END_INPUT on its own line to signal end of input.
     *
     * @return Cleaned list of non-empty, trimmed code lines
     */
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

    /**
     * Reads code from a pre-built string — used for demo / testing.
     *
     * @param code Raw source code as a single multi-line string
     * @return Cleaned list of non-empty, trimmed code lines
     */
    public List<String> readFromString(String code) {
        List<String> rawLines = new ArrayList<>();
        String[] lines = code.split("\n");
        for (String line : lines) {
            rawLines.add(line);
        }
        return cleanInput(rawLines);
    }

    /**
     * Cleans raw lines:
     *   - Trims leading/trailing whitespace
     *   - Removes blank lines
     *   - Removes comment-only lines (starting with //)
     *
     * @param rawLines Unprocessed input lines
     * @return Cleaned list ready for classification
     */
    private List<String> cleanInput(List<String> rawLines) {
        List<String> cleaned = new ArrayList<>();

        for (String line : rawLines) {
            String trimmed = line.trim();

            if (trimmed.isEmpty()) {
                continue;                   // skip blank lines
            }
            if (trimmed.startsWith("//")) {
                continue;                   // skip comment-only lines
            }

            cleaned.add(trimmed);
        }

        return cleaned;
    }
}
