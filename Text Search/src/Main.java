public class Main {
    public static void main(String[] args) {
        try {
            Project.consoleProject();
        } catch (Exception ex){
            Printer.println(ex.getMessage(), ConsoleColors.RED);
        }
    }
}