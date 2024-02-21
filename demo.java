import java.io.*;
import java.util.*;

class Bus{
    synchronized void buyTicket(int busId){
        try{
            List<String> buses = FileHelper.readFromFile(FileHelper.BUSES_FILE);
            List<String> tickets = FileHelper.readFromFile(FileHelper.TICKETS_FILE);

            int currentCapacity = 0;
            double price = 0.0;

            for(int i=1;i<buses.size();i++){
                String[] data = buses.get(i).split(",");
                if(Integer.parseInt(data[0]) == busId){
                    currentCapacity = Integer.parseInt(data[1]);
                    price = Double.parseDouble(data[5]);
                    break;
                }
            }

            if(currentCapacity > 0){
                for(int i=1;i<buses.size();i++){
                    String[] data = buses.get(i).split(",");
                    if(Integer.parseInt(data[0]) == busId){
                        currentCapacity -= 1;
                        data[1] = String.valueOf(currentCapacity);
                        buses.set(i, String.join("," , data));
                        break;
                    }
                }

                FileHelper.writeToFile(FileHelper.BUSES_FILE, buses);

                int ticketId = tickets.size(); //As the first line should be headers
                String ticketRecord = ticketId + "," + busId + ",booked," + price;
                tickets.add(ticketRecord);

                FileHelper.writeToFile(FileHelper.TICKETS_FILE, tickets);

                System.out.println("The ticket has been booked for Rs " + price);
            }
            else{
                System.out.println("The bus is already full.");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    synchronized void cancelTicket(int ticketId){
        try{
            List<String> tickets = FileHelper.readFromFile(FileHelper.TICKETS_FILE);
            List<String> updatedTickets = new ArrayList<>();

            boolean ticketFound = false;
            int busId = -1;

            for(int i=0;i<tickets.size();i++){
                String[] data = tickets.get(i).split(",");
                if(i == 0){
                    updatedTickets.add(String.join(",", data));
                    continue;
                }
                if(Integer.parseInt(data[0]) == ticketId && data[2].equals("booked")){
                    data[2] = "cancelled";
                    busId = Integer.parseInt(data[1]);
                    ticketFound = true;
                }
                updatedTickets.add(String.join(",", data));
            }

            if(!ticketFound){
                System.out.println("Ticket not found or already cancelled");
                return;
            }

            FileHelper.writeToFile(FileHelper.TICKETS_FILE, updatedTickets);

            //We also have to update the bus capacity

            List<String> buses = FileHelper.readFromFile(FileHelper.BUSES_FILE);
            List<String> updatedBuses = new ArrayList<>();

            for(int i=0;i<buses.size();i++){
                String[] data = buses.get(i).split(",");
                if(i == 0){
                    updatedBuses.add(String.join(",", data));
                    continue;
                }
                if(Integer.parseInt(data[0]) == busId){
                    data[1] = String.valueOf(Integer.parseInt(data[1]) + 1);
                }
                
                updatedBuses.add(String.join(",", data));
            }

            FileHelper.writeToFile(FileHelper.BUSES_FILE, updatedBuses);

            System.out.println("The ticket has been successfully cancelled");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}

class Consumer implements Runnable{
    Scanner inp;

    Consumer(Scanner inp){
        this.inp = inp;
    }

    public void run(){
        try{
            System.out.println("Do you want to book the Ticket or Cancel the Ticket?");
            String reply = inp.nextLine();
            if(reply.equals("Book")){
                System.out.println("Enter bus ID to book the ticket");
                int busId = inp.nextInt();
                inp.nextLine(); // Consume the newline character
                bookTicket(busId);
            }
            else if(reply.equals("Cancel")){
                System.out.println("Enter ticket ID to cancel");
                int ticketId = inp.nextInt();
                inp.nextLine(); // Consume the newline character
                cancelTicket(ticketId);
            }
            else{
                System.out.println("Invalid Command");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        System.out.println("");
    }

    private void bookTicket(int busId) {
        new Bus().buyTicket(busId);
    }

    private void cancelTicket(int ticketId) {
        new Bus().cancelTicket(ticketId);
    }
}

public class demo {
    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);

        System.out.println("These are the buses that we have available");
        displayBusDetails();

        int m;
        System.out.println("How many operations do you want to perform?");
        m = inp.nextInt();
        inp.nextLine(); // Consume the newline character

        Thread[] threads = new Thread[m];

        for(int i=0;i<m;i++){
            Consumer consumer = new Consumer(inp);
            threads[i] = new Thread(consumer);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < m; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Exception has occured " + e);
            }
        }

        displayBusDetails();
    }

    static void displayBusDetails(){
        try{
            List<String> buses = FileHelper.readFromFile(FileHelper.BUSES_FILE);

            for(String bus : buses){
                String data[] = bus.split(",");
                if(data[0].equals("id")){
                    continue;
                }

                System.out.println("Bus ID " + data[0] + "->");
                System.out.println("Capacity: " + data[1]);
                System.out.println("Source: " + data[2]);
                System.out.println("Destination: " + data[3]);
                System.out.println("Timings: " + data[4]);

                System.out.println();
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

}

//https://learn.microsoft.com/en-us/linux/install