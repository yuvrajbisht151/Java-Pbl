import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FlowchartGenerator.java
 * -----------------------
 * Responsibilities:
 *   1. Accept the classified List<CodeLine> from CodeClassifier.
 *   2. Print a structured, ASCII-only flowchart to the console.
 *   3. Print a summary statistics table at the end.
 *
 * ASCII-only design — no Unicode box-drawing characters.
 * Uses only: + - | = * v characters that work in every terminal.
 *
 * Node styles:
 *   START    ->  +========+  (double-line border using =)
 *   PROCESS  ->  +--------+  (single-line border using -)
 *   DECISION ->  / ~~~~~~ \  (slanted border to suggest a diamond)
 *   LOOP     ->  * ~~~~~~ *  (star border to suggest a cycle)
 *   END      ->  +========+  (double-line border using =)
 */
public class FlowchartGenerator {

    private static final int MAX_CODE_LEN = 40;

    private final List<CodeLine> codeLines;

    /**
     * @param codeLines The classified list from CodeClassifier.classify()
     */
    public FlowchartGenerator(List<CodeLine> codeLines) {
        this.codeLines = codeLines;
    }

    /**
     * Renders the full flowchart followed by the summary table.
     */
    public void generate() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    EXECUTION FLOWCHART                     ");
        System.out.println("============================================================");
        System.out.println();

        if (codeLines.isEmpty()) {
            System.out.println("  [No code to display]");
            return;
        }

        for (int i = 0; i < codeLines.size(); i++) {
            CodeLine cl = codeLines.get(i);
            printNode(cl);

            // Print connecting arrow between every node except after the last
            if (i < codeLines.size() - 1) {
                printArrow();
            }
        }

        System.out.println();
        printSummaryTable();
    }

    // ------------------------------------------------------------------
    // Node rendering
    // ------------------------------------------------------------------

    /**
     * Dispatches to the correct visual style based on LineType.
     */
    private void printNode(CodeLine cl) {
        switch (cl.getType()) {
            case START:
                printStartNode(cl.getCode());
                break;
            case PROCESS:
                printProcessNode(cl.getCode());
                break;
            case DECISION:
                printDecisionNode(cl.getCode());
                break;
            case LOOP:
                printLoopNode(cl.getCode());
                break;
            case END:
                printEndNode(cl.getCode());
                break;
            default:
                printProcessNode(cl.getCode());
        }
    }

    /**
     * START node — uses === border.
     *
     *   +====================+
     *   |  [START] text      |
     *   +====================+
     */
    private void printStartNode(String code) {
        String content = "  [START]  " + fit(code, MAX_CODE_LEN);
        String border  = repeat("=", content.length() + 2);
        System.out.println("  +" + border + "+");
        System.out.println("  |  " + content + "  |");
        System.out.println("  +" + border + "+");
    }

    /**
     * PROCESS node — uses --- border.
     *
     *   +--------------------+
     *   |  PROCESS: text     |
     *   +--------------------+
     */
    private void printProcessNode(String code) {
        String content = "  PROCESS: " + fit(code, MAX_CODE_LEN);
        String border  = repeat("-", content.length() + 2);
        System.out.println("  +" + border + "+");
        System.out.println("  |  " + content + "  |");
        System.out.println("  +" + border + "+");
    }

    /**
     * DECISION node — slanted lines suggest a diamond shape.
     *
     *   /---------------------\
     *   |  DECISION: text     |
     *   \---------------------/
     *         YES |   NO ---->
     */
    private void printDecisionNode(String code) {
        String content = "  DECISION: " + fit(code, MAX_CODE_LEN);
        String border  = repeat("-", content.length() + 2);
        System.out.println("  /" + border + "\\");
        System.out.println("  |  " + content + "  |");
        System.out.println("  \\" + border + "/");
        System.out.println("        YES |       NO ---->");
    }

    /**
     * LOOP node — star border + back-edge indicator.
     *
     *   *---------------------*
     *   |  LOOP: text         |
     *   *---------------------*
     *     |                   ^
     *     +-- (loop body) ----+
     */
    private void printLoopNode(String code) {
        String content = "  LOOP: " + fit(code, MAX_CODE_LEN);
        String border  = repeat("-", content.length() + 2);
        System.out.println("  *" + border + "*");
        System.out.println("  |  " + content + "  |");
        System.out.println("  *" + border + "*");
        System.out.println("    |  (execute body, then check condition again)");
        System.out.println("    |______________________________________________");
        System.out.println("    ^                                              |");
        System.out.println("    |____________ back to condition _______________/");
    }

    /**
     * END node — uses === border, same style as START.
     *
     *   +====================+
     *   |  [END] text        |
     *   +====================+
     */
    private void printEndNode(String code) {
        // Skip individual closing braces mid-flow (they are noise)
        if (code.equals("}")) return;

        String content = "  [END]  " + fit(code, MAX_CODE_LEN);
        String border  = repeat("=", content.length() + 2);
        System.out.println("  +" + border + "+");
        System.out.println("  |  " + content + "  |");
        System.out.println("  +" + border + "+");
    }

    /**
     * Arrow connecting two nodes.
     *
     *      |
     *      v
     */
    private void printArrow() {
        System.out.println("        |");
        System.out.println("        v");
    }

    // ------------------------------------------------------------------
    // Summary table
    // ------------------------------------------------------------------

    /**
     * Counts node types and prints a statistics table.
     */
    private void printSummaryTable() {
        Map<LineType, Integer> counts = new HashMap<>();
        for (LineType t : LineType.values()) {
            counts.put(t, 0);
        }
        for (CodeLine cl : codeLines) {
            counts.put(cl.getType(), counts.get(cl.getType()) + 1);
        }

        int decisions  = counts.get(LineType.DECISION);
        int loops      = counts.get(LineType.LOOP);
        int complexity = 1 + decisions + loops;
        String level   = complexity <= 3 ? "LOW (simple)"
                       : complexity <= 7 ? "MODERATE"
                       : "HIGH (consider refactoring)";

        System.out.println("============================================================");
        System.out.println("                      ANALYSIS SUMMARY                      ");
        System.out.println("============================================================");
        System.out.printf("  %-30s : %d%n", "Total nodes analyzed", codeLines.size());
        System.out.println("------------------------------------------------------------");
        System.out.printf("  %-30s : %d%n", "START blocks",              counts.get(LineType.START));
        System.out.printf("  %-30s : %d%n", "PROCESS statements",        counts.get(LineType.PROCESS));
        System.out.printf("  %-30s : %d%n", "DECISION branches (if/else)",decisions);
        System.out.printf("  %-30s : %d%n", "LOOP constructs (for/while)",loops);
        System.out.printf("  %-30s : %d%n", "END markers",               counts.get(LineType.END));
        System.out.println("------------------------------------------------------------");
        System.out.printf("  %-30s : %d%n", "Cyclomatic Complexity", complexity);
        System.out.printf("  %-30s : %s%n", "Complexity Level", level);
        System.out.println("============================================================");
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Utility helpers
    // ------------------------------------------------------------------

    /**
     * Truncates text to maxLen characters, pads shorter strings with spaces.
     */
    private String fit(String text, int maxLen) {
        if (text.length() > maxLen) {
            return text.substring(0, maxLen - 2) + "..";
        }
        return text;
    }

    /**
     * Repeats a character string n times (replaces String.repeat for older Java).
     */
    private String repeat(String ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(ch);
        return sb.toString();
    }
}
