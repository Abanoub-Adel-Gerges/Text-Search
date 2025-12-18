import java.util.Scanner;
import java.util.ArrayList;
public class Project {
    public static void consoleProject(){
        Scanner scanner = new Scanner(System.in);
        Printer.print("Enter input file path: ", ConsoleColors.GREEN);
        String inputPath = scanner.nextLine();
        if(!FileHelper.exists(inputPath)){
            Printer.print("File not found!", ConsoleColors.RED);
            return;
        }
        String inputText = FileHelper.readFromFile(inputPath);
        StringSearcher stringSearcher = new StringSearcher(inputText);
        Printer.print("Enter number of queries: ", ConsoleColors.GREEN);
        int numOfQueries = scanner.nextInt();
        for(int queryNum = 1; queryNum <= numOfQueries; queryNum++){
            SearchQuery sq = new SearchQuery();
            sq.readInputFromConsole();
            ArrayList<SearchMatch> matches;
            double time = CodeTimer.measureTimeNano(() -> {stringSearcher.search(sq);}) / 1000000.0;
            matches = stringSearcher.search(sq);

            String result = "Query " + queryNum + " Result\nSearch time: " + time + " ms\nCount: "
                    + matches.size() + "\nMatches: " + matches.toString() + "\n";
            final String[] replace = new String[1];
            if(sq.getReplaceTarget() != null && !sq.getReplaceTarget().isEmpty()){
                double replaceTime = CodeTimer.measureTimeNano(() -> {
                        replace[0] = stringSearcher.getReplacedText(matches, sq.getReplaceTarget());
                }) / 1000000.0;
                result += "Replace time: " + replaceTime + " ms\n\n" + replace[0];
                Printer.println(result, ConsoleColors.PURPLE);
            } else {
                Printer.println(result, ConsoleColors.PURPLE);
                result += stringSearcher.getHighlightedText(matches);
                Printer.println("Highlighted text:\n", ConsoleColors.UNDERLINE);
                stringSearcher.printHighlightedText(matches);
            }
            result += Printer.separator + "\n";
            FileHelper.writeOnFile(sq.getOutputPath(), result, queryNum > 1);
            Printer.printSeparator();
        }
    }
}
