import java.io.*;
import java.util.*;

class BusOperations{
    Scanner inp;

    public BusOperations(Scanner inp){
        this.inp = inp;
    }

    public void addNewBus(){
        System.out.println("Enter bus capacity, source, destination, timing (HH:MMAM/PM-HH:MMAM/PM) and price");
        int capacity = inp.nextInt();
        inp.nextLine(); //Consume the newline character
        String src = inp.nextLine();
        String dest = inp.nextLine();
        String timing = inp.nextLine();
        double price = inp.nextDouble();
        inp.nextLine();

        try{
            List<String> buses = FileHelper.readFromFile(FileHelper.BUSES_FILE);
            int id = buses.size(); //As the first line should be headers
            String newBusRecord = id + "," + capacity + "," + src + "," + dest + "," + timing + "," + price;
            buses.add(newBusRecord);
            FileHelper.writeToFile(FileHelper.BUSES_FILE,buses);
            System.out.println("Bus has been successfully added");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void updateBusTimings(){
        System.out.println("Enter the bus Id you want to update");
        int id = inp.nextInt();
        inp.nextLine(); //Consume the newline character

        System.out.println("Enter the new timings");
        String newTiming = inp.nextLine();

        try{
            List<String> buses = FileHelper.readFromFile(FileHelper.BUSES_FILE);
            List<String> updatedBuses = new ArrayList<>();

            for(String bus : buses){
                String[] data = bus.split(",");
                if(data[0].equals(String.valueOf(id))){
                    data[4] = newTiming;
                }

                updatedBuses.add(String.join(",", data));
            }

            FileHelper.writeToFile(FileHelper.BUSES_FILE, updatedBuses);

            System.out.println("Bus timings have been successfully updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void removeBus(){
        System.out.println("Enter the Bus Id to remove");
        int id = inp.nextInt();
        inp.nextLine(); //Consume the newline character

        try{
            List<String> buses = FileHelper.readFromFile(FileHelper.BUSES_FILE);
            List<String> updatedBuses = new ArrayList<>();
            
            for(String bus: buses){
                String[] data = bus.split(",");
                if(!data[0].equals(String.valueOf(id))){
                    updatedBuses.add(bus);
                }
            }

            FileHelper.writeToFile(FileHelper.BUSES_FILE, updatedBuses);
            System.out.println("Bus has been succesfully removed");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}

public class BusServiceManager {
    public static void main(String[] args){
        Scanner inp = new Scanner(System.in);
        BusOperations busOperations = new BusOperations(inp);

        System.out.println("Do you want to Add new bus, Update timings of existing bus or Remove a bus");
        String reply = inp.nextLine();

        switch(reply){
            case "Add":
                busOperations.addNewBus();
                break;
            case "Update":
                busOperations.updateBusTimings();
                break;
            case "Remove":
                busOperations.removeBus();
                break;    
            default:
                System.out.println("Invalid action was selected");        
        }
    }
}