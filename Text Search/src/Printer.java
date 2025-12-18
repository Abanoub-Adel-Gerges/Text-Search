public class Printer {
    public static String separator = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
    public static void print(Object obj){
        System.out.print(obj);
    }
    public static void println(Object obj){
        System.out.println(obj);
    }
    public static void println(){
        System.out.println();
    }
    public static void print(Object obj, String color){
        System.out.print(color + obj + ConsoleColors.RESET);
    }
    public static void println(Object obj, String color){
        System.out.println(color + obj + ConsoleColors.RESET);
    }
    public static void printSeparator(){
        println(separator, ConsoleColors.BRIGHT_BLUE + ConsoleColors.BOLD);
    }
}