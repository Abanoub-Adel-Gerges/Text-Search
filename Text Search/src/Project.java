import helpers.*;
import printer.*;
import searcher.*;

import javax.swing.*;
import java.util.Scanner;
import datastructures.ArrayList;
public class Project {
    public static final Scanner scanner = new Scanner(System.in);
    private StringSearcher stringSearcher;
    public Project(){
        FileHelper.createFileEnsured("input.txt");
        FileHelper.createFileEnsured("output.txt");
    }
    public void consoleProject(){
        Printer.print("Enter input file path: ", ConsoleColors.GREEN);
        String inputPath = scanner.nextLine();
        if(!FileHelper.exists(inputPath)){
            Printer.print("File not found!", ConsoleColors.RED);
            return;
        }
        String inputText = FileHelper.readFromFile(inputPath);
        stringSearcher = new StringSearcher(inputText);
        Printer.print("Enter number of queries: ", ConsoleColors.GREEN);
        int numOfQueries = scanner.nextInt();
        for(int queryNum = 1; queryNum <= numOfQueries; queryNum++){
            SearchQuery sq = new SearchQuery();
            sq.readInputFromConsole();
            ArrayList<SearchMatch> matches;
            double time = CodeTimer.measureTimeNano(() -> stringSearcher.search(sq)) / 1000000.0;
            matches = stringSearcher.search(sq);

            StringBuilder result = new StringBuilder();
            result.append("Query ").append(queryNum).append(" Result\n").append("Query: ").append(sq);
            result.append("\nSearch time: ").append(time).append(" ms\nCount: ");
            result.append(matches.size()).append("\nMatches: ").append(matches.toString()).append("\n");
            final String[] replace = new String[1];
            if(sq.getReplaceTarget() != null && !sq.getReplaceTarget().isEmpty()){
                double replaceTime = CodeTimer.measureTimeNano(() -> {
                        replace[0] = stringSearcher.getReplacedText(matches, sq.getReplaceTarget());
                }) / 1000000.0;
                result.append("Replace time: ").append(replaceTime).append(" ms\n\n").append(replace[0]);
                Printer.println(result.toString(), ConsoleColors.PURPLE);
            } else {
                Printer.println(result.toString(), ConsoleColors.PURPLE);
                result.append(stringSearcher.getHighlightedText(matches));
                Printer.println("Highlighted text:\n", ConsoleColors.UNDERLINE);
                stringSearcher.printHighlightedText(matches);
            }
            result.append(Printer.separator).append("\n");
            FileHelper.writeOnFile(sq.getOutputPath(), result.toString(), queryNum > 1);
            Printer.printSeparator();
        }
    }
    public void GUIProject(){
        SwingUtilities.invokeLater(SearcherGUI::new);
    }
}
