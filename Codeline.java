/**
 * CodeLine.java
 * -------------
 * A simple model (data class) that holds one line of source code together
 * with its assigned classification type.
 *
 * Used as the element type stored in the ArrayList inside CodeClassifier.
 */
public class CodeLine {

    // ── Classification labels ────────────────────────────────────────────────
    public static final String START    = "START";
    public static final String PROCESS  = "PROCESS";
    public static final String DECISION = "DECISION";
    public static final String LOOP     = "LOOP";
    public static final String END      = "END";

    // ── Fields ───────────────────────────────────────────────────────────────
    private final String text;   // The original (trimmed) line of code
    private final String type;   // One of the constants above

    /**
     * Constructs a CodeLine with the given text and classification type.
     *
     * @param text The trimmed source code line.
     * @param type The classification type (START / PROCESS / DECISION / LOOP / END).
     */
    public CodeLine(String text, String type) {
        this.text = text;
        this.type = type;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getText() { return text; }
    public String getType() { return type; }

    /**
     * Friendly string for debug printing.
     */
    @Override
    public String toString() {
        return String.format("%-10s | %s", type, text);
    }
}