/* Shutdown Hook  (Ctrl-C) */


package mainthread;
import java.util.ArrayList;

public class ShutDownClass implements Runnable {
    Thread main;
    DataLists lists;
    ArrayList<Thread> threads;
    Thread printThread;
    DataPrinter printer;
    
    public ShutDownClass (Thread main, DataLists lists, ArrayList<Thread> threads, Thread printThread, DataPrinter printer) {
        this.main = main;
        this.lists = lists;
        this.threads = threads;
        this.printer = printer;
        this.printThread = printThread;
    }
    
    
    @Override
    public void run() {
        System.out.println("\t *** Executing ShutDown Hook. ***");
        
        main.interrupt();
        try { main.join(); }
        catch (InterruptedException ex) { ex.printStackTrace(); }
        
        
        // Closes all the open threads and removes the information from all the relevant lists
        int size = threads.size();
        for (int i = size - 1; i >= 0; i--) {
            String name = lists.getMyInterfaces(i).getName();
            lists.getMyInterfaces(i).setAlive(false);
            
            try { threads.get(i).join(); }
            catch (InterruptedException ex) { ex.printStackTrace(); }
            threads.remove(threads.get(i));
            
            if (lists.getMyInterfaces(i).isWireless()) {
                for (int j = 0; j < lists.getMyWirelessInterfacesSize(); j++) {
                    if ((lists.getMyWirelessInterfaces(j).getName()).equals(lists.getMyInterfaces(i).getName())) {
                        lists.removeMyWirelessInterfaces(j);
                        break;
            }}} 
            lists.removeMyInterfaces(i);
            System.out.println(name + " is finished!\n");
        }
        
        System.out.println("Current Thread Number : " + threads.size());
        System.out.println("Total Interfaces Number : " + lists.getMyInterfacesSize() +
                           " Wireless Interfaces Number : " + lists.getMyWirelessInterfacesSize()+ "\n");
        
        printer.setAlive(false);
        try { printThread.join(); }
        catch (InterruptedException ex) { ex.printStackTrace(); }
    }
}