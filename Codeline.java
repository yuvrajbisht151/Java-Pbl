/**
 * CodeLine.java
 * -------------
 * Model class that holds a single line of source code together
 * with its assigned classification type (from LineType enum).
 *
 * Used as the element stored in the ArrayList inside CodeClassifier.
 */
public class CodeLine {

    private String   code;   // The trimmed source code text
    private LineType type;   // START / PROCESS / DECISION / LOOP / END

    /**
     * @param code The trimmed source code line
     * @param type The classification type from LineType enum
     */
    public CodeLine(String code, LineType type) {
        this.code = code;
        this.type = type;
    }

    public String   getCode() { return code; }
    public LineType getType() { return type; }

    @Override
    public String toString() {
        return String.format("%-10s | %s", type, code);
    }
}
