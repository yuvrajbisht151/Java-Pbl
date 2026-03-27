import java.util.ArrayList;
import java.util.List;


public class CodeClassifier {

    private final KeywordDetector detector;

    /**
     * @param detector A configured KeywordDetector instance
     */
    public CodeClassifier(KeywordDetector detector) {
        this.detector = detector;
    }

    public List<CodeLine> classify(List<String> lines) {
        List<CodeLine> result = new ArrayList<>();

       
        result.add(new CodeLine("Program Start", LineType.START));

        for (String line : lines) {
            LineType type = determineType(line);
            result.add(new CodeLine(line, type));
        }

        result.add(new CodeLine("Program End", LineType.END));

        return result;
    }

   
    private LineType determineType(String line) {
        if (detector.isStart(line))    return LineType.START;
        if (detector.isDecision(line)) return LineType.DECISION;
        if (detector.isLoop(line))     return LineType.LOOP;
        if (detector.isEnd(line))      return LineType.END;
        return LineType.PROCESS;
    }

    
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
