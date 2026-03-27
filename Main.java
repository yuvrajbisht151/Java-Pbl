import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        
        System.out.println();
        System.out.println("==========================================");
        System.out.println("        MINI CODE ANALYZER  v1.0         ");
        System.out.println("  Transforms source code into flowcharts  ");
        System.out.println("==========================================");
        System.out.println();
        System.out.println("  Select run mode:");
        System.out.println("    1  ->  DEMO  (built-in sample code)");
        System.out.println("    2  ->  LIVE  (enter your own code)");
        System.out.println();
        System.out.print("  Your choice: ");

        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine().trim();

      
        InputHandler inputHandler = new InputHandler();
        List<String> lines;

        if (choice.equals("1")) {
            System.out.println();
            System.out.println("  [DEMO MODE - analyzing built-in sample code]");
            lines = inputHandler.readFromString(demoCode());
        } else {
            System.out.println();
            lines = inputHandler.readInput();
        }

        if (lines.isEmpty()) {
            System.out.println();
            System.out.println("  No code was entered. Exiting.");
            return;
        }

       
        KeywordDetector detector = new KeywordDetector();

      
        CodeClassifier classifier = new CodeClassifier(detector);
        List<CodeLine> classified = classifier.classify(lines);

        
        classifier.printClassificationTable(classified);

       
        FlowchartGenerator generator = new FlowchartGenerator(classified);
        generator.generate();
    }

   
    private static String demoCode() {
        return  "int score = 85;\n"
              + "int grade = 0;\n"
              + "if (score >= 90) {\n"
              + "    grade = 1;\n"
              + "} else if (score >= 75) {\n"
              + "    grade = 2;\n"
              + "} else {\n"
              + "    grade = 3;\n"
              + "}\n"
              + "for (int i = 0; i < 3; i++) {\n"
              + "    System.out.println(grade);\n"
              + "}\n"
              + "while (score > 0) {\n"
              + "    score = score - 10;\n"
              + "}\n"
              + "return grade;";
    }
}
