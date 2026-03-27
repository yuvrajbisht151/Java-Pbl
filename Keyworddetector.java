import java.util.HashSet;
import java.util.Set;


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

    
    private void loadKeywords() {

        
        decisionKeywords.add("if");
        decisionKeywords.add("else");
        decisionKeywords.add("else if");
        decisionKeywords.add("switch");
        decisionKeywords.add("case");

       
        loopKeywords.add("for");
        loopKeywords.add("while");
        loopKeywords.add("do");

        endKeywords.add("return");
        endKeywords.add("break");
        endKeywords.add("continue");
        endKeywords.add("}");
    }

   
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

    public boolean isStart(String line) {
        return line.toLowerCase().contains("public static void main");
    }
}
