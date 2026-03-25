import java.util.HashSet;
import java.util.Set;

/**
 * KeywordDetector.java
 * --------------------
 * Responsible for:
 *   1. Storing control-structure keywords in a HashSet for O(1) lookup.
 *   2. Detecting which category a line belongs to based on its first keyword.
 *
 * Data Structure Used: HashSet<String>
 *   - No duplicate keywords.
 *   - Fast constant-time contains() check.
 */
public class KeywordDetector {

    // HashSet stores DECISION keywords (if / else conditions)
    private final Set<String> decisionKeywords;

    // HashSet stores LOOP keywords (for / while repetition)
    private final Set<String> loopKeywords;

    // HashSet stores END keywords (return / break / closing brace)
    private final Set<String> endKeywords;

    // HashSet stores START markers
    private final Set<String> startKeywords;

    /**
     * Constructor — populates all keyword sets.
     */
    public KeywordDetector() {
        decisionKeywords = new HashSet<>();
        loopKeywords     = new HashSet<>();
        endKeywords      = new HashSet<>();
        startKeywords    = new HashSet<>();

        initializeKeywords();
    }

    /**
     * Fills each HashSet with its respective control-structure keywords.
     */
    private void initializeKeywords() {

        // ── DECISION keywords ────────────────────────────────────────────────
        decisionKeywords.add("if");
        decisionKeywords.add("else");
        decisionKeywords.add("else if");
        decisionKeywords.add("switch");
        decisionKeywords.add("case");

        // ── LOOP keywords ────────────────────────────────────────────────────
        loopKeywords.add("for");
        loopKeywords.add("while");
        loopKeywords.add("do");

        // ── END keywords ─────────────────────────────────────────────────────
        endKeywords.add("return");
        endKeywords.add("break");
        endKeywords.add("continue");
        endKeywords.add("}");

        // ── START keywords ───────────────────────────────────────────────────
        startKeywords.add("start");
        startKeywords.add("begin");
        startKeywords.add("main");
    }

    // ── Public detection methods ──────────────────────────────────────────────

    /**
     * Checks if a line starts with a DECISION keyword.
     *
     * @param line Trimmed source line.
     * @return true if it is a decision/condition line.
     */
    public boolean isDecision(String line) {
        String lower = line.toLowerCase();
        // "else if" must be checked before "else" alone
        if (lower.startsWith("else if")) return true;
        if (lower.startsWith("} else if")) return true;
        if (lower.startsWith("} else"))   return true;

        for (String kw : decisionKeywords) {
            if (lower.startsWith(kw + " ") || lower.startsWith(kw + "(")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a line starts with a LOOP keyword.
     *
     * @param line Trimmed source line.
     * @return true if it is a loop line.
     */
    public boolean isLoop(String line) {
        String lower = line.toLowerCase();
        for (String kw : loopKeywords) {
            if (lower.startsWith(kw + " ") || lower.startsWith(kw + "(")) {
                return true;
            }
        }
        // "do {" alone
        if (lower.equals("do") || lower.equals("do {")) return true;
        return false;
    }

    /**
     * Checks if a line is an END marker (return, break, }, etc.).
     *
     * @param line Trimmed source line.
     * @return true if it signals the end of a block or program.
     */
    public boolean isEnd(String line) {
        String lower = line.toLowerCase().trim();
        for (String kw : endKeywords) {
            if (lower.equals(kw) || lower.startsWith(kw + " ") || lower.startsWith(kw + ";")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a line is a START marker.
     *
     * @param line Trimmed source line.
     * @return true if it signals program start.
     */
    public boolean isStart(String line) {
        String lower = line.toLowerCase().trim();
        for (String kw : startKeywords) {
            if (lower.equals(kw) || lower.contains("public static void main")) {
                return true;
            }
        }
        return false;
    }

    // ── Keyword set accessors (for display/debug) ─────────────────────────────

    public Set<String> getDecisionKeywords() { return decisionKeywords; }
    public Set<String> getLoopKeywords()     { return loopKeywords; }
    public Set<String> getEndKeywords()      { return endKeywords; }
}