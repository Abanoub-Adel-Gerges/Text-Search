import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Printer.print("Enter input file path: ", ConsoleColors.GREEN);
        String inputPath = scanner.next();
        if(!FileHelper.exists(inputPath)){
            Printer.print("File not found!", ConsoleColors.RED);
            return;
        }
        String inputText = FileHelper.readFromFile(inputPath);
        StringSearcher stringSearcher = new StringSearcher(inputText);
        Printer.print("Enter number of queries: ", ConsoleColors.GREEN);
        int numOfQueries = scanner.nextInt();
        while (numOfQueries-- > 0){
            SearchQuery sq = new SearchQuery();
            sq.readInputFromConsole();
            String result = stringSearcher.search(sq).toString();
            Printer.println(result);
            FileHelper.writeOnFile(sq.getOutputPath(), result, false);
        }
//        printAllCode();
    }
    public static void printAllCode(){
        String srcPath = "A:\\Faculty\\3rd year\\1st term\\1 - Algorithms and Data structures\\Project\\Code\\Text Search\\src\\";
        File directory = new File(srcPath);
        if (!(directory.exists() && directory.isDirectory())){return;}
        File[] javaFiles = directory.listFiles();
        for (File file : javaFiles) {
            Printer.println(file.getPath());
            Printer.println(FileHelper.readFromFile(file.getPath()));
        }
    }
}