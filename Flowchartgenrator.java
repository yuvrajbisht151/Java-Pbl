import java.util.List;

/**
 * FlowchartGenerator.java
 * -----------------------
 * Responsible for:
 *   1. Taking the classified List<CodeLine> from CodeClassifier.
 *   2. Rendering a structured, readable flowchart in the console.
 *   3. Using different visual symbols per line type:
 *
 *       START    →  ╔══ START ══╗
 *       PROCESS  →  [ statement ]
 *       DECISION →  ◇ if / else ◇
 *       LOOP     →  ↻ [ for / while ]
 *       END      →  ╚══ END ══╝
 *
 *   4. Printing a summary statistics table at the end.
 */
public class FlowchartGenerator {

    // ── Visual symbol constants ───────────────────────────────────────────────
    private static final String ARROW        = "  ↓";
    private static final String INDENT       = "  ";
    private static final int    MAX_LINE_LEN = 45;

    /**
     * Renders the full flowchart to stdout.
     *
     * @param codeLines Classified list produced by CodeClassifier.
     */
    public void generate(List<CodeLine> codeLines) {
        System.out.println();
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║           EXECUTION  FLOWCHART              ║");
        System.out.println("╚═════════════════════════════════════════════╝");
        System.out.println();

        for (int i = 0; i < codeLines.size(); i++) {
            CodeLine cl = codeLines.get(i);

            // Print the flowchart node for this line
            printNode(cl);

            // Print the connecting arrow between nodes (not after the last one)
            if (i < codeLines.size() - 1) {
                System.out.println(ARROW);
            }
        }

        System.out.println();

        // Print summary statistics
        printSummary(codeLines);
    }

    // ── Node rendering ────────────────────────────────────────────────────────

    /**
     * Dispatches to the correct visual renderer based on the node's type.
     *
     * @param cl A single classified code line.
     */
    private void printNode(CodeLine cl) {
        switch (cl.getType()) {
            case CodeLine.START:
                printStartNode(cl.getText());
                break;
            case CodeLine.PROCESS:
                printProcessNode(cl.getText());
                break;
            case CodeLine.DECISION:
                printDecisionNode(cl.getText());
                break;
            case CodeLine.LOOP:
                printLoopNode(cl.getText());
                break;
            case CodeLine.END:
                printEndNode(cl.getText());
                break;
            default:
                printProcessNode(cl.getText());
        }
    }

    /**
     * START node — rounded terminal box.
     *
     *   ╔══════════════════╗
     *   ║  START           ║
     *   ╚══════════════════╝
     */
    private void printStartNode(String text) {
        String content = "  ★  " + truncate(text);
        String border  = "═".repeat(content.length() + 2);
        System.out.println(INDENT + "╔" + border + "╗");
        System.out.println(INDENT + "║ " + content + " ║");
        System.out.println(INDENT + "╚" + border + "╝");
    }

    /**
     * PROCESS node — rectangle box.
     *
     *   ┌──────────────────┐
     *   │  int a = 10;     │
     *   └──────────────────┘
     */
    private void printProcessNode(String text) {
        String content = "  " + truncate(text);
        String border  = "─".repeat(content.length() + 2);
        System.out.println(INDENT + "┌" + border + "┐");
        System.out.println(INDENT + "│ " + content + " │");
        System.out.println(INDENT + "└" + border + "┘");
    }

    /**
     * DECISION node — diamond shape (approximated with ◇ markers).
     *
     *   ◇ ─────────────────── ◇
     *     DECISION: if (a > 5)
     *   ◇ ─────────────────── ◇
     */
    private void printDecisionNode(String text) {
        String label   = "DECISION: " + truncate(text);
        String dashes  = "─".repeat(label.length() + 2);
        System.out.println(INDENT + "◇" + dashes + "◇");
        System.out.println(INDENT + "  " + label);
        System.out.println(INDENT + "◇" + dashes + "◇");
        System.out.println(INDENT + "         YES ↓          NO →");
    }

    /**
     * LOOP node — cycle arrow with bracket.
     *
     *   ↻ ═══════════════════════ ↻
     *     LOOP: for (int i = 0 ...)
     *   ↻ ═══════════════════════ ↻
     */
    private void printLoopNode(String text) {
        String label  = "LOOP: " + truncate(text);
        String dashes = "═".repeat(label.length() + 2);
        System.out.println(INDENT + "↻ " + dashes + " ↻");
        System.out.println(INDENT + "  " + label);
        System.out.println(INDENT + "↻ " + dashes + " ↻");
        System.out.println(INDENT + "    [loop body executes, then ↑ back to condition]");
    }

    /**
     * END node — rounded terminal box (closing style).
     *
     *   ╔══════════════════╗
     *   ║  ■  Program End  ║
     *   ╚══════════════════╝
     */
    private void printEndNode(String text) {
        // Suppress individual END nodes for closing braces in the middle of code
        if (text.equals("}")) return;

        String content = "  ■  " + truncate(text);
        String border  = "═".repeat(content.length() + 2);
        System.out.println(INDENT + "╔" + border + "╗");
        System.out.println(INDENT + "║ " + content + " ║");
        System.out.println(INDENT + "╚" + border + "╝");
    }

    // ── Summary table ─────────────────────────────────────────────────────────

    /**
     * Counts and displays classification statistics.
     *
     * @param codeLines The full classified list.
     */
    private void printSummary(List<CodeLine> codeLines) {
        int starts    = 0, processes = 0, decisions = 0, loops = 0, ends = 0;

        for (CodeLine cl : codeLines) {
            switch (cl.getType()) {
                case CodeLine.START    -> starts++;
                case CodeLine.PROCESS  -> processes++;
                case CodeLine.DECISION -> decisions++;
                case CodeLine.LOOP     -> loops++;
                case CodeLine.END      -> ends++;
            }
        }

        int total = codeLines.size();
        // Cyclomatic complexity: decisions + loops + 1
        int complexity = decisions + loops + 1;

        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│              ANALYSIS  SUMMARY               │");
        System.out.println("├──────────────────────────────┬──────────────┤");
        System.out.printf( "│  %-28s  │  %-10d  │%n", "Total Nodes Analyzed",  total);
        System.out.println("├──────────────────────────────┼──────────────┤");
        System.out.printf( "│  %-28s  │  %-10d  │%n", "START blocks",          starts);
        System.out.printf( "│  %-28s  │  %-10d  │%n", "PROCESS statements",    processes);
        System.out.printf( "│  %-28s  │  %-10d  │%n", "DECISION branches",     decisions);
        System.out.printf( "│  %-28s  │  %-10d  │%n", "LOOP constructs",       loops);
        System.out.printf( "│  %-28s  │  %-10d  │%n", "END markers",           ends);
        System.out.println("├──────────────────────────────┼──────────────┤");
        System.out.printf( "│  %-28s  │  %-10d  │%n", "Cyclomatic Complexity", complexity);
        System.out.println("└──────────────────────────────┴──────────────┘");
        System.out.println();
        System.out.println("  Cyclomatic Complexity = decisions + loops + 1");
        if (complexity <= 4) {
            System.out.println("  → Code complexity: LOW (simple, easy to test)");
        } else if (complexity <= 7) {
            System.out.println("  → Code complexity: MODERATE (manageable)");
        } else {
            System.out.println("  → Code complexity: HIGH (consider refactoring)");
        }
        System.out.println();
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /**
     * Truncates a line to MAX_LINE_LEN characters for clean display.
     */
    private String truncate(String text) {
        if (text.length() <= MAX_LINE_LEN) return text;
        return text.substring(0, MAX_LINE_LEN - 3) + "...";
    }
}