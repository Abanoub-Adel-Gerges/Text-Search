package helpers;

import java.io.*;
public class FileHelper {
    public static String readFromFile(String path) {
        if (path == null){return "";}
        File file = new File(path);
        if (!file.exists() || !file.canRead()){return "";}
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                content.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
        } catch (IOException e) {
            return "";
        }
        return content.toString();
    }
    public static void writeOnFile(String path, String content, boolean append) {
        if (path == null || content == null){return;}
        File file = new File(path);
        if (!file.exists() || !file.canWrite()){return;}
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))){
            writer.write(content);
        } catch (IOException e){
        }
    }
    public static void clearFile(String path) {
        writeOnFile(path, "", false);
    }
    public static void createFileEnsured(String path){
        if (path == null){return;}
        File file = new File(path);
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
        }
    }
    public static boolean exists(String path){
        File file = new File(path);
        return file.exists();
    }
}
