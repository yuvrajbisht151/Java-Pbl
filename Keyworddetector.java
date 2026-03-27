import java.util.HashSet;
import java.util.Set;

/**
 * KeywordDetector.java
 * --------------------
 * Responsibilities:
 *   1. Store control-structure keywords in HashSets for O(1) lookup.
 *   2. Expose methods to test which category a line belongs to.
 *
 * Data Structure: HashSet<String>
 *   - No duplicates
 *   - Fast constant-time contains() check
 *
 * Categories supported:
 *   DECISION  -> if, else, else if, switch, case
 *   LOOP      -> for, while, do
 *   END       -> return, break, continue, }
 *   START     -> public static void main
 */
public class KeywordDetector {

    private final Set<String> decisionKeywords;
    private final Set<String> loopKeywords;
    private final Set<String> endKeywords;

    public KeywordDetector() {
        decisionKeywords = new HashSet<>();
        loopKeywords     = new HashSet<>();
        endKeywords      = new HashSet<>();
        loadKeywords();
    }

    /**
     * Populates all three HashSets with their keywords.
     */
    private void loadKeywords() {

        // DECISION keywords
        decisionKeywords.add("if");
        decisionKeywords.add("else");
        decisionKeywords.add("else if");
        decisionKeywords.add("switch");
        decisionKeywords.add("case");

        // LOOP keywords
        loopKeywords.add("for");
        loopKeywords.add("while");
        loopKeywords.add("do");

        // END keywords
        endKeywords.add("return");
        endKeywords.add("break");
        endKeywords.add("continue");
        endKeywords.add("}");
    }

    // -----------------------------------------------------------------
    // Detection methods — each checks if the line starts with a keyword
    // -----------------------------------------------------------------

    /**
     * @param line Trimmed source line
     * @return true if line is a DECISION statement (if/else/switch)
     */
    public boolean isDecision(String line) {
        String lower = line.toLowerCase();
        if (lower.startsWith("else if")) return true;
        if (lower.startsWith("} else")) return true;
        for (String kw : decisionKeywords) {
            if (lower.equals(kw)
                    || lower.startsWith(kw + " ")
                    || lower.startsWith(kw + "(")) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param line Trimmed source line
     * @return true if line is a LOOP statement (for/while/do)
     */
    public boolean isLoop(String line) {
        String lower = line.toLowerCase();
        for (String kw : loopKeywords) {
            if (lower.equals(kw)
                    || lower.equals(kw + " {")
                    || lower.startsWith(kw + " ")
                    || lower.startsWith(kw + "(")) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param line Trimmed source line
     * @return true if line is an END marker (return/break/}/etc.)
     */
    public boolean isEnd(String line) {
        String lower = line.toLowerCase().trim();
        for (String kw : endKeywords) {
            if (lower.equals(kw)
                    || lower.startsWith(kw + " ")
                    || lower.startsWith(kw + ";")) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param line Trimmed source line
     * @return true if line contains the main method signature
     */
    public boolean isStart(String line) {
        return line.toLowerCase().contains("public static void main");
    }
}
