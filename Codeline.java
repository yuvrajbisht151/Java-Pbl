
public class CodeLine {

    private String   code;   
    private LineType type;  

    
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
