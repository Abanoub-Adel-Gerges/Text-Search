import printer.ConsoleColors;
import printer.Printer;

public class Main {
    public static void main(String[] args) {
        try {
            Project project = new Project();
//            project.consoleProject();
            project.GUIProject();
        } catch (Exception ex){
            Printer.println(ex.getMessage(), ConsoleColors.RED);
        }
    }
}