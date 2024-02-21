import java.io.*;
import java.util.*;

public class FileHelper{
    public static String BUSES_FILE = "Buses.txt";
    public static String TICKETS_FILE = "Tickets.txt";
    
    public static List<String> readFromFile(String filepath){
        List<String> records = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;
            while((line = br.readLine()) != null){
                records.add(line);
            }

            br.close();

            return records;
        }
        catch(Exception e){
            System.out.println(e);
        }

        return records;
    }

    public static void writeToFile(String filepath, List<String> records){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
            for (String record: records){
                bw.write(record);
                bw.newLine();
            }

            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}