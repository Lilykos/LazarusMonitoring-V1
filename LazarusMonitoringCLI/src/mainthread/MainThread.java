/* Main class creating threads, comparing info and executing shutdown hook */


package mainthread;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class MainThread implements Runnable {
    
    private ArrayList<Thread> myInterfaceThreads;
    private ArrayList<String> interfaceNewNames;
    private ArrayList<String> interfaceNames;
    private ParserIfconfig parser;
    private ParserIwconfig iwconfigParser;
    private DataLists lists;
    private Random generator;
    private DataPrinter printerRunnable;
    private Properties prop;
    
    
    int T;
    int k;
    int X;
    int c;
    
    
    @Override
    public void run() {
        prop = new Properties();
        printerRunnable = new DataPrinter();
        Thread printer = new Thread(printerRunnable);
        printer.start();
        
        lists = new DataLists();
        myInterfaceThreads = new ArrayList<>();
        interfaceNewNames = new ArrayList<>();
        interfaceNames = new ArrayList<>();
        parser = new ParserIfconfig();
        iwconfigParser = new ParserIwconfig();
        generator = new Random();
        
        int curtime1 = 0;
        int curtime2 = 0;;
        
        boolean changesFound = false;
        boolean firstCycle = true;
        
        int sleepTime = T;
        int localc = 1;
        
        System.out.println("\\****************************************\\\n");
        System.out.println("\\******* LAZARUS: ***********************\\\n");
        System.out.println("\\******* The WiFi Monitoring Software ***\\\n");
        System.out.println("\\****************************************\\\n\n");
        
        try { Thread.sleep(3000); }
        catch (InterruptedException ex) { ex.printStackTrace(); }
        
        // Property file input
        readPropertyFile(T, k, X, c);
        
        // shutdown hook 
        ShutDownClass sdHook = new ShutDownClass(Thread.currentThread(), lists, myInterfaceThreads, printer, printerRunnable);
        Runtime.getRuntime().addShutdownHook(new Thread(sdHook));
   
        try {
            while (!Thread.currentThread().isInterrupted()) {
                changesFound = false;
                
                
            // Parsing only the names, list insertion
            // Checking for different names
            // Create/delete interfaces and threads
                  
                interfaceNewNames = parser.findNames();
                
                // Dhmiourgia newn interfaces/threads
                for (String name: interfaceNewNames) {
                    if (interfaceNames.contains(name))
                        interfaceNames.remove(name);
                    else {
                        if(iwconfigParser.iwConfigCheck(name)) {
                            InterfaceWireless iface = new InterfaceWireless(name, lists, printerRunnable, T, k, X, c);
                            lists.addMyInterfaces(iface);
                            lists.addMyWirelessInterfaces(iface);
                            myInterfaceThreads.add(new Thread(iface));
                            myInterfaceThreads.get(myInterfaceThreads.size() - 1).start();
                            changesFound = true;
                        }
                        else {
                            Interface iface = new Interface(name, lists, printerRunnable, T, k, X, c);
                            lists.addMyInterfaces(iface);
                            myInterfaceThreads.add(new Thread(iface));
                            myInterfaceThreads.get(myInterfaceThreads.size() - 1).start();
                            changesFound = true;
                        }
                    }
                }

                // Delete interfaces/threads with flag kai join()
                for (String name: interfaceNames) {
                    for (int i = 0; i < lists.getMyInterfacesSize(); i++) {
                        if ((lists.getMyInterfaces(i).getName()).equals(name)) {
                            lists.getMyInterfaces(i).setAlive(false);
                            myInterfaceThreads.get(i).join(); 
                            myInterfaceThreads.remove(myInterfaceThreads.get(i));
                            lists.removeMyInterfaces(i);
                            changesFound = true;
                            break;
                        } 
                    }
                    for (int i = 0; i < lists.getMyWirelessInterfacesSize(); i++) {
                        if ((lists.getMyWirelessInterfaces(i).getName()).equals(name)) {
                            lists.removeMyWirelessInterfaces(i);
                            break;
                        }
                    }
                }

                // We assign the new list on the previous variable to check
                // at the next iteration
                interfaceNames = interfaceNewNames;
                
                
                // Random wireless interface to check the access points
                if (lists.getMyWirelessInterfacesSize() > 1) {
                    int index = generator.nextInt(lists.getMyWirelessInterfacesSize());
                    for (int i = 0; i < lists.getMyWirelessInterfacesSize(); i++) {
                        if (lists.getMyWirelessInterfacesSize() == 1) {
                            lists.getMyWirelessInterfaces(i).setFlags(true, true);
                            break;
                        }
                        if (i == index)
                            lists.getMyWirelessInterfaces(i).setFlags(true, false);
                        else 
                            lists.getMyWirelessInterfaces(i).setFlags(false, true);
                    }
                }
                if (lists.getMyWirelessInterfacesSize() == 1) {
                    System.out.println("Only 1 Wireless Interface found. Resuming checks with this interface only.");
                    lists.getMyWirelessInterfaces(0).setFlags(true, true);
                }
                
                System.out.println("Current Thread Number : " + myInterfaceThreads.size());
                System.out.println("Total Interfaces Number : " + lists.getMyInterfacesSize() +
                                   " Wireless Interfaces Number : " + lists.getMyWirelessInterfacesSize()+ "\n");
                
                // Markov chain
                if (changesFound) {
                    localc = 1;
                    sleepTime = T;
                }
                else {  
                    if (localc % c != 0)
                        localc ++;
                    else {
                        if (sleepTime != k*T) {
                            sleepTime = sleepTime + T;
                            localc = 1;
                }}}
                
                curtime2 = (int) System.currentTimeMillis();
                if (!firstCycle)
                    sleepTime = Math.abs(sleepTime - (curtime2 - curtime1));
                firstCycle = false;
                
                System.out.println("\\*** Main thread " + " will sleep for " + sleepTime);
                Thread.sleep(sleepTime);
                curtime1 = (int) System.currentTimeMillis();
            }
        } catch (InterruptedException ex) { System.out.println("\\*** Main Thread Interrupted!\n"); }        
    }
    
    
    // Property file
    public void readPropertyFile(int T, int k, int X, int c) {
        
        // Se periptwsh pou paei kati strava apo to property file
        // dinoume kapoies default times gia na leitourghsei omala to programma.
        String propT = "3000";
        String propk = "5";
        String propX = "10";
        String propC = "1";
        try {
            
            prop.load(getClass().getResourceAsStream("PropertiesFile.properties"));
            propT = prop.getProperty("T");
            propk = prop.getProperty("k");
            propX = prop.getProperty("X");
            propC = prop.getProperty("c");
            
            System.out.println("\t *** Properties File Found! ***");
        } catch (IOException ex) { System.out.println("Property File NOT FOUND: Using default properties."); }
        this.T = Integer.parseInt(propT);
        this.k = Integer.parseInt(propk);
        this.X = Integer.parseInt(propX);
        this.c = Integer.parseInt(propC);
    }

    public static void main(String[] args) {
        Thread mainThread = new Thread(new MainThread());
        mainThread.start();
    } 
}