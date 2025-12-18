import java.util.Scanner;
public class SearchQuery {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String defaultOutputPath = "output.txt";
    private char caseSensitive = 's';
    private String keyword;
    private String replaceTarget = null;
    private String matchMode = MatchMode.WHOLE;
    private String outputPath;
    public SearchQuery(char caseSensitive, String keyword, String replaceTarget, String matchMode, String outputPath) {
        if (caseSensitive != 's' && caseSensitive != 'i'){throw new RuntimeException("wrong case sensitive");}
        this.caseSensitive = caseSensitive;
        this.keyword = keyword;
        this.replaceTarget = replaceTarget;
        switch (matchMode.toLowerCase()){
            case "prefix" : this.matchMode = MatchMode.PREFIX; break;
            case "substring" : this.matchMode = MatchMode.SUBSTRING; break;
            default: this.matchMode = MatchMode.WHOLE; break;
        }
        setOutputPath(outputPath);
    }
    public SearchQuery(){}
    public void readInputFromConsole(){
        Printer.println("Enter case sensitive type(i, s): ", ConsoleColors.GREEN);
        caseSensitive = scanner.nextLine().toLowerCase().charAt(0);
        while (caseSensitive != 's' && caseSensitive != 'i'){
            Printer.println("Enter case sensitive type(i, s): ", ConsoleColors.GREEN);
            caseSensitive = scanner.nextLine().toLowerCase().charAt(0);
        }

        Printer.println("Enter keyword: ", ConsoleColors.GREEN);
        keyword = scanner.nextLine();

        Printer.println("Enter replace-target (if there is not just press enter): ", ConsoleColors.GREEN);
        replaceTarget = scanner.nextLine();
        if(replaceTarget.equals("")){replaceTarget = null;}

        Printer.println("Enter match mode (prefix, substring, whole): ", ConsoleColors.GREEN);
        matchMode = scanner.nextLine().toLowerCase();
        switch (matchMode){
            case "prefix" : matchMode = MatchMode.PREFIX; break;
            case "substring" : matchMode = MatchMode.SUBSTRING; break;
            default: matchMode = MatchMode.WHOLE; break;
        }
        Printer.println("Enter output path: ", ConsoleColors.GREEN);
        setOutputPath(scanner.nextLine());
    }
    public void setOutputPath(String outputPath) {
        if (FileHelper.exists(outputPath)){
            this.outputPath = outputPath;
        }
        else{
            Printer.println("File not exist!\noutput path = \"output.txt\"", ConsoleColors.RED);
            this.outputPath = defaultOutputPath;
            FileHelper.createFileEnsured(this.outputPath);
        }
    }
    public boolean isCaseSensitive(){
        return caseSensitive == 's';
    }
    public String getMatchMode() {
        return matchMode;
    }
    public String getKeyword() {
        return keyword;
    }
    public String getOutputPath() {
        return outputPath;
    }

    public String getReplaceTarget() {
        return replaceTarget;
    }
}
