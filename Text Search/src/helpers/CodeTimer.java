package helpers;

import printer.Printer;

public class CodeTimer {
    public static long measureTimeNano(Runnable code){
        long before = System.nanoTime();
        code.run();
        long after = System.nanoTime();
        return after - before;
    }
    public static void measureTime(Runnable code){
        Printer.println("Time = " + (measureTimeNano(code) / 1000000.0) + " ms");
        Printer.printSeparator();
    }
}
