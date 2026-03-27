import java.util.ArrayList;
import java.util.List;

/**
 * CodeClassifier.java
 * -------------------
 * Responsibilities:
 *   1. Iterate over cleaned code lines (List<String>).
 *   2. Use KeywordDetector to determine each line's LineType.
 *   3. Wrap each line in a CodeLine object.
 *   4. Return a List<CodeLine> for the flowchart generator.
 *
 * Data Structure: ArrayList<CodeLine>
 *   - Preserves insertion order (line order matters for flowcharts)
 *   - Allows indexed access
 *
 * Classification priority order:
 *   1. START    -> contains "public static void main"
 *   2. DECISION -> starts with if / else / else if / switch
 *   3. LOOP     -> starts with for / while / do
 *   4. END      -> return / break / continue / }
 *   5. PROCESS  -> everything else (assignments, declarations, calls)
 */
public class CodeClassifier {

    private final KeywordDetector detector;

    /**
     * @param detector A configured KeywordDetector instance
     */
    public CodeClassifier(KeywordDetector detector) {
        this.detector = detector;
    }

    /**
     * Classifies each line and returns an ordered list of CodeLine objects.
     * A synthetic START and END node are automatically added at the boundaries.
     *
     * @param lines Cleaned source code lines from InputHandler
     * @return Ordered list of classified CodeLine objects
     */
    public List<CodeLine> classify(List<String> lines) {
        List<CodeLine> result = new ArrayList<>();

        // Synthetic START node at the top
        result.add(new CodeLine("Program Start", LineType.START));

        for (String line : lines) {
            LineType type = determineType(line);
            result.add(new CodeLine(line, type));
        }

        // Synthetic END node at the bottom
        result.add(new CodeLine("Program End", LineType.END));

        return result;
    }

    /**
     * Applies classification rules in priority order.
     *
     * @param line A single trimmed source code line
     * @return The matching LineType constant
     */
    private LineType determineType(String line) {
        if (detector.isStart(line))    return LineType.START;
        if (detector.isDecision(line)) return LineType.DECISION;
        if (detector.isLoop(line))     return LineType.LOOP;
        if (detector.isEnd(line))      return LineType.END;
        return LineType.PROCESS;
    }

    /**
     * Prints the raw classification table to the console.
     * Call this before generateFlowchart() to see the classified breakdown.
     *
     * @param classified The classified code lines from classify()
     */
    public void printClassificationTable(List<CodeLine> classified) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                  CLASSIFICATION TABLE                      ");
        System.out.println("============================================================");
        System.out.printf("  %-12s | %s%n", "TYPE", "SOURCE LINE");
        System.out.println("------------------------------------------------------------");

        for (CodeLine cl : classified) {
            String display = cl.getCode();
            if (display.length() > 45) {
                display = display.substring(0, 42) + "...";
            }
            System.out.printf("  %-12s | %s%n", cl.getType(), display);
        }

        System.out.println("============================================================");
    }
}
