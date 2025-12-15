public class Printer {
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
        for(int i = 0; i < 100; i++) {
            print((i % 2 == 0)?"=":"-", ConsoleColors.BRIGHT_BLUE + ConsoleColors.BOLD);
        }
        print("\n");
    }
}