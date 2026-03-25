import java.util.ArrayList;
import java.util.List;

/**
 * CodeClassifier.java
 * -------------------
 * Responsible for:
 *   1. Iterating over cleaned code lines (List<String>).
 *   2. Using KeywordDetector to identify each line's category.
 *   3. Wrapping each line in a CodeLine object with its type.
 *   4. Returning a List<CodeLine> for the flowchart generator.
 *
 * Data Structure Used: ArrayList<CodeLine>
 *   - Maintains insertion order (line order matters for flowcharts).
 *   - Allows indexed access when building the chart.
 *
 * Classification rules (applied in priority order):
 *   START    → line contains "public static void main" or equals "start"
 *   DECISION → starts with if / else / else if / switch
 *   LOOP     → starts with for / while / do
 *   END      → return / break / continue / closing brace  }
 *   PROCESS  → everything else (assignments, method calls, declarations)
 */
public class CodeClassifier {

    private final KeywordDetector detector;

    /**
     * @param detector A configured KeywordDetector instance.
     */
    public CodeClassifier(KeywordDetector detector) {
        this.detector = detector;
    }

    /**
     * Classifies each line and returns a list of CodeLine objects.
     *
     * @param lines Cleaned source code lines (from InputHandler).
     * @return Ordered list of classified CodeLine objects.
     */
    public List<CodeLine> classify(List<String> lines) {
        List<CodeLine> classified = new ArrayList<>();

        // Always prepend a synthetic START node
        classified.add(new CodeLine("Program Start", CodeLine.START));

        for (String line : lines) {
            String type = determineType(line);
            classified.add(new CodeLine(line, type));
        }

        // Always append a synthetic END node
        classified.add(new CodeLine("Program End", CodeLine.END));

        return classified;
    }

    /**
     * Applies classification rules in priority order.
     *
     * @param line A single trimmed source code line.
     * @return The matching classification constant from CodeLine.
     */
    private String determineType(String line) {

        // Priority 1 — START
        if (detector.isStart(line)) {
            return CodeLine.START;
        }

        // Priority 2 — DECISION (if / else / switch)
        if (detector.isDecision(line)) {
            return CodeLine.DECISION;
        }

        // Priority 3 — LOOP (for / while / do)
        if (detector.isLoop(line)) {
            return CodeLine.LOOP;
        }

        // Priority 4 — END (return / break / } etc.)
        if (detector.isEnd(line)) {
            return CodeLine.END;
        }

        // Priority 5 — PROCESS (default: all other statements)
        return CodeLine.PROCESS;
    }

    /**
     * Prints the raw classification table to the console (optional debug view).
     *
     * @param classified The classified code lines.
     */
    public void printClassificationTable(List<CodeLine> classified) {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────┐");
        System.out.println("│              CLASSIFICATION  TABLE                  │");
        System.out.println("├───────────┬─────────────────────────────────────────┤");
        System.out.printf( "│ %-9s │ %-39s │%n", "TYPE", "SOURCE LINE");
        System.out.println("├───────────┼─────────────────────────────────────────┤");

        for (CodeLine cl : classified) {
            // Truncate long lines for display
            String display = cl.getText().length() > 39
                    ? cl.getText().substring(0, 36) + "..."
                    : cl.getText();
            System.out.printf("│ %-9s │ %-39s │%n", cl.getType(), display);
        }

        System.out.println("└───────────┴─────────────────────────────────────────┘");
    }
}