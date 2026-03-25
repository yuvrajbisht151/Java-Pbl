import java.util.List;
import java.util.Scanner;

/**
 * Main.java
 * ---------
 * Entry point for the Mini Code Analyzer.
 *
 * Wires all modules together in this pipeline:
 *
 *   InputHandler  →  KeywordDetector  →  CodeClassifier  →  FlowchartGenerator
 *
 * Run modes:
 *   1. DEMO mode  — runs a built-in sample so you can see output immediately.
 *   2. LIVE mode  — you type your own code into the console.
 */
public class Main {

    public static void main(String[] args) {

        // ── Step 0: Choose run mode ───────────────────────────────────────────
        System.out.println();
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║       MINI CODE ANALYZER  v1.0              ║");
        System.out.println("║  Visualize your code as a flowchart         ║");
        System.out.println("╚═════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Select mode:");
        System.out.println("    1  →  Run DEMO (built-in sample code)");
        System.out.println("    2  →  Enter YOUR OWN code");
        System.out.print(  "  Your choice: ");

        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine().trim();

        // ── Step 1: Collect input ─────────────────────────────────────────────
        InputHandler inputHandler = new InputHandler();
        List<String> lines;

        if (choice.equals("1")) {
            lines = inputHandler.readFromString(buildDemoCode());
            System.out.println();
            System.out.println("  [DEMO MODE — analyzing built-in sample code]");
        } else {
            System.out.println();
            lines = inputHandler.readInput();
        }

        if (lines.isEmpty()) {
            System.out.println();
            System.out.println("  ⚠  No code was entered. Please try again.");
            return;
        }

        // ── Step 2: Detect keywords (HashSet lookup) ──────────────────────────
        KeywordDetector detector = new KeywordDetector();

        // ── Step 3: Classify each line ────────────────────────────────────────
        CodeClassifier classifier = new CodeClassifier(detector);
        List<CodeLine> classified = classifier.classify(lines);

        // ── Step 4: Show raw classification table ─────────────────────────────
        classifier.printClassificationTable(classified);

        // ── Step 5: Generate and print the flowchart ──────────────────────────
        FlowchartGenerator generator = new FlowchartGenerator();
        generator.generate(classified);
    }

    // ── Demo code ─────────────────────────────────────────────────────────────

    /**
     * A small but representative Java snippet used in DEMO mode.
     * It exercises all four classification types.
     */
    private static String buildDemoCode() {
        return String.join("\n",
                "int score = 85;",
                "int grade = 0;",
                "if (score >= 90) {",
                "    grade = 1;",
                "} else if (score >= 75) {",
                "    grade = 2;",
                "} else {",
                "    grade = 3;",
                "}",
                "for (int i = 0; i < 3; i++) {",
                "    System.out.println(grade);",
                "}",
                "while (score > 0) {",
                "    score = score - 10;",
                "}",
                "return grade;"
        );
    }
}