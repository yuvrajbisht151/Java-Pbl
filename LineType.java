/**
 * LineType.java
 * -------------
 * Enum defining the 5 possible classifications for any line of source code.
 * Used by CodeLine, CodeClassifier, and FlowchartGenerator.
 */
public enum LineType {
    START,
    PROCESS,
    DECISION,
    LOOP,
    END
}
