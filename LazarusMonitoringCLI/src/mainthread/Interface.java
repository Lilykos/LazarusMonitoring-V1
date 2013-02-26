/* Wired interface with all the parsing and compare methods */


package mainthread;

public class Interface implements Runnable {
    
    protected String name;
    protected String mac = "Not Found";
    protected String ip = "Not Found";
    protected String mask = "Not Found";
    protected String networkAddress = "Not Found";
    protected String bcastAddr = "Not Found";
    protected String defaultGateway = "Not Found";
    protected String mtu = "Not Found";
    protected String packetErrorRate = "Not Found";
    protected String broadcastRate = "Not Found";
    protected String consumedGauge = "Not Found";
    
    protected ParserIfconfig parser;
    protected ParserRoute routeParser;
    protected DataLists lists;
    protected DataPrinter printer;
    protected volatile boolean checkAlive = true;
    private Interface interfaceClone;
    
    protected final int T;
    protected final int k;
    protected final int X;
    protected final int c;
      
    public Interface(String name, DataLists lists, DataPrinter printer, int T, int k, int X, int c) {
        this.name = name;
        this.lists = lists;
        this.printer = printer;
        this.T = T;
        this.k = k;
        this.X = X;
        this.c = c;
        parser = new ParserIfconfig();
        routeParser = new ParserRoute();
    }
    
    
    public boolean isWireless() {
        return false;
    }
    
    // Setters - Getters
    public String getName() {
        return name; }
    
    public String getMAC() {
        return mac; }
    
    public String getIP() {
        return ip; }
    
    public String getMask() {
        return mask; }
    
    public String getNetworkAddress() {
        return networkAddress; }
    
    public String getBcastAddr() {
        return bcastAddr; }
    
    public String getDefaultGateway() {
        return defaultGateway; }
    
    public String getMTU() {
        return mtu; }
    
    public String getPacketErrorRate() {
        return packetErrorRate; }
    
    public String getBroadcastRate() {
        return broadcastRate; }
    
    public String getConsumedGauge() {
        return consumedGauge;}
    
    
    
    public void setName(String name) {
        this.name = name; }
    
    public void setMAC(String mac) {
        this.mac = mac; }
    
    public void setIP(String ip) {
        this.ip = ip; }
    
    public void setMask(String mask) {
        this.mask = mask; }
    
    public void setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress; }
    
    public void setBcastAddr(String bcastAddr) {
        this.bcastAddr = bcastAddr; }
    
    public void setDefaultGateway(String defaultGateway) {
        this.defaultGateway = defaultGateway; }
    
    public void setMTU(String mtu) {
        this.mtu = mtu; }
    
    public void setPacketErrorRate(String packetsErrorsRate) {
        this.packetErrorRate = packetErrorRate; }
    
    public void setBroadcastRate(String broadcastRate) {
        this.broadcastRate = broadcastRate; }
    
    public void setConsumedGauge(String consumedGauge) {
        this.consumedGauge = consumedGauge; }
    
    public void setAlive(boolean checkAlive) {
        this.checkAlive = checkAlive; }
    

    @Override
    public void run() {
        int sleepTime = this.T;
        int localc = 1;
        int curtime2 = 0;
        int curtime1 = 0;
        boolean firstCycle = true;
        
        
        // Creating a clone using copyConstructor()
        // we assign attributes as to compare the original with the clone
        // to check for differences
        interfaceClone = new Interface(this.name, null, this.printer, 0, 0, 0, 0);
        
        while(this.checkAlive) {
            
            // Each interface creates and controls it's own parsers
            interfaceClone.copyConstructor(this);
            parser.getIfconfigProperties(this);
            parser.setNetAddr(this);
            routeParser.getRouteProperties(this);
            
            // Using Markov model for the repeats
            if (compareTo(this, interfaceClone)) {
                localc = 1;
                sleepTime = T;
                printer.printData(this, true);
            }
            else {  
                if (localc % c != 0)
                    localc ++;
                else {
                    if (sleepTime != k*T) {
                        sleepTime = sleepTime + T;
                        localc = 1;
                }}
                printer.printData(this, false);
            }
            
            
            // Finding the exact time...
            curtime2 = (int) System.currentTimeMillis();
            if (!firstCycle)
                sleepTime = Math.abs(sleepTime - (curtime2 - curtime1));
            firstCycle = false;
            
            try {
                System.out.println("\\*** " + this.getName() + " will sleep for " + sleepTime);
                Thread.sleep(sleepTime); }
            catch (InterruptedException ex) { System.out.println("\\*** " + this.getName() + " is interrupted!\n"); } 
            curtime1 = (int) System.currentTimeMillis();
        }
    }

    
    // Copy constructor for the clone
    private void copyConstructor(Interface oldInterface) {
        this.setDefaultGateway(oldInterface.getDefaultGateway());
        this.setIP(oldInterface.getIP());
        this.setMAC(oldInterface.getMAC());
        this.setMTU(oldInterface.getMTU());
        this.setMask(oldInterface.getMask());
        this.setNetworkAddress(oldInterface.getNetworkAddress());
        this.setNetworkAddress(oldInterface.getConsumedGauge());
        this.setNetworkAddress(oldInterface.getPacketErrorRate());
    }
    
    
    // Checking some attributes, if differences are found, we restart Markov chain
    private boolean compareTo(Interface newInterface, Interface oldInterface) {
        if (!oldInterface.getIP().equals(newInterface.getIP()) ||
            !oldInterface.getMAC().equals(newInterface.getMAC()) ||
            !oldInterface.getMask().equals(newInterface.getMask()) ||
            !oldInterface.getDefaultGateway().equals(newInterface.getDefaultGateway()) ) {
            return true;
        }
        
        if (!oldInterface.getConsumedGauge().equals("Not Found")) {
            double gaugeOld = Double.parseDouble(oldInterface.getConsumedGauge());
            double gaugeNew = Double.parseDouble(newInterface.getConsumedGauge());
            if (((gaugeNew / gaugeOld) * 100) > X)
                return true;
        }
        
        if (!oldInterface.getPacketErrorRate().equals("Not Found") &&
            !oldInterface.getPacketErrorRate().equals("Impossible to calculate.") &&
            !newInterface.getPacketErrorRate().equals("Not Found") &&
            !newInterface.getPacketErrorRate().equals("Impossible to calculate.")) {
            
            double errorsOld = Double.parseDouble(oldInterface.getPacketErrorRate());
            double errorsNew = Double.parseDouble(newInterface.getPacketErrorRate());
            if (errorsOld == 0 && errorsNew == 0)
                return false;
            else if (errorsOld == 0 && errorsNew != 0)
                return true;
            else if (((errorsNew / errorsOld) * 100) > X)
                return true;
        }
        return false;
    }   
}