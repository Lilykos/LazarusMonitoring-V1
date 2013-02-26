/* Wireless interface with all the parsing and compare methods */


package mainthread;
import java.util.ArrayList;

public class InterfaceWireless extends Interface {
    
    private String baseStationMAC = "Not Found";
    private String baseStationESSID = "Not Found";
    private String channel = "Not Found";
    private String accessPointSituation = "Not Found";
    private String signalLevel = "Not Found";
    private String linkQuality = "Not Found";
    private String txPower = "Not Found";
    private String noiseLevel = "Not Found";
    private String missedBeacon = "Not Found";
    
    private ParserIwconfig iwconfigParser;
    private ParserIwgetid iwgetidParser;
    private ParserProcNetWireless pnwParser;
    private ParserIwlist iwlistParser;
    private InterfaceWireless interfaceClone;
    
    private volatile boolean checkAP = true;
    private volatile boolean checkSelf = true;
    private ArrayList<AccessPoint> apList;
    
     
    public InterfaceWireless(String name, DataLists lists, DataPrinter printer, int T, int k, int X, int c) {
        super(name, lists, printer, T, k, X, c);
        iwconfigParser = new ParserIwconfig();
        iwgetidParser = new ParserIwgetid();
        pnwParser = new ParserProcNetWireless();
        iwlistParser = new ParserIwlist();
        apList = new ArrayList<>();
    }
    
    
    @Override
    public boolean isWireless() {
        return true;
    }
    
    // Setters - Getters
    public String getBaseStationMAC() {
        return baseStationMAC; }
    
    public String getBaseStationESSID() {
        return baseStationESSID; }
    
    public String getChannel() {
        return channel; }
    
    public String getAccessPointMode() {
        return accessPointSituation; }
    
    public String getSignalLevel() {
        return signalLevel; }
    
    public String getLinkQuality() {
        return linkQuality; }
    
    public String getTxPower() {
        return txPower; }
    
    public String getNoiseLevel() {
        return noiseLevel; }
    
    public String getMissedBeacon() {
        return missedBeacon; }
    
    
    
    public void setBaseStationMAC(String baseStationMAC) {
        this.baseStationMAC = baseStationMAC; }
    
    public void setBaseStationESSID(String baseStationESSID) {
        this.baseStationESSID = baseStationESSID; }
    
    public void setChannel(String channel) {
        this.channel = channel; }
    
    public void setAccessPointMode(String accessPointSituation) {
        this.accessPointSituation = accessPointSituation; }
    
    public void setSignalLevel(String signalLevel) {
        this.signalLevel = signalLevel; }
    
    public void setLinkQuality(String linkQuality) {
        this.linkQuality = linkQuality; }
    
    public void setTxPower(String txPower) {
        this.txPower = txPower; }
    
    public void setNoiseLevel(String noiseLevel) {
        this.noiseLevel = noiseLevel; }
    
    public void setMissedBeacon(String missedBeacon) {
        this.missedBeacon = missedBeacon; }

    
    // We use these flags to check whether THIS wireless interface will check
    // for itself, for access points or for both.
    public void setFlags(boolean checkAp, boolean checkSelf) {
        this.checkSelf = checkSelf;
        this.checkAP = checkAP;
    }
    
    @Override
    public void run() {
        int sleepTime = this.T;
        int localc = 1;
        
        int curtime2 = 0;
        int curtime1 = 0;
        boolean firstCycle = true;
        
        boolean changesFoundAP;
        boolean changesFoundSelf;
        boolean checkSelfPrint = false;
        boolean checkAPPrint = false;
        
        while (this.checkAlive) {
            changesFoundAP = false;
            changesFoundSelf = false;
             
            // Different checks executed depending on the bool attributes
            if (this.checkAP) {
                checkAPPrint = true;
                apList.clear();
                int accessPointSize = lists.getMyAccessPointsSize();
                iwlistParser.getAccessPointProperties(this.name, apList);
                if (apList.size() != accessPointSize)
                    changesFoundAP = true;
                else
                    changesFoundAP = false;
            }
            
            if (this.checkSelf) {
                checkSelfPrint = true;
                interfaceClone = new InterfaceWireless(this.name, null, this.printer, 0, 0, 0, 0);
                interfaceClone.copyConstructor(this);
                
                // Each interface creates and controls it's own parsers
                parser.getIfconfigProperties(this);
                parser.setNetAddr(this);
                pnwParser.setNoiseAndMissedBeacon(this);
                
                //wireless - specific parsers
                routeParser.getRouteProperties(this);
                iwconfigParser.setIwconfigProperties(this);
                iwgetidParser.setChannelProperties(this);
                if (compareTo(this, interfaceClone))
                    changesFoundSelf = true;
                else
                    changesFoundSelf = false;
            }
            
            // Using Markov model for the repeats
            if (changesFoundAP || changesFoundSelf) {
                localc = 1;
                sleepTime = T;
                if (changesFoundAP)
                    printer.printData(this.name, apList, true);
                if (changesFoundSelf)
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
                if (checkSelfPrint)
                    printer.printData(this, false);
                if (checkAPPrint)
                    printer.printData(this.name, apList, false);
            }
            
            // Finding the repeat time...
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
    public void copyConstructor(InterfaceWireless oldInterface) {
        this.setDefaultGateway(oldInterface.getDefaultGateway());
        this.setIP(oldInterface.getIP());
        this.setMAC(oldInterface.getMAC());
        this.setMTU(oldInterface.getMTU());
        this.setMask(oldInterface.getMask());
        this.setNetworkAddress(oldInterface.getNetworkAddress());
        
        this.setBaseStationMAC(oldInterface.getBaseStationMAC());
        this.setBaseStationESSID(oldInterface.getBaseStationESSID());
        this.setChannel(oldInterface.getChannel());
        this.setAccessPointMode(oldInterface.getAccessPointMode());
        this.setLinkQuality(oldInterface.getLinkQuality());
        this.setSignalLevel(oldInterface.getSignalLevel());
        this.setTxPower(oldInterface.getTxPower());
        this.setNoiseLevel(oldInterface.getNoiseLevel());
        this.setMissedBeacon(oldInterface.getMissedBeacon());
    }
    
    
    // Checking some attributes, if differences are found, we restart Markov chain
    private boolean compareTo(InterfaceWireless newInterface, InterfaceWireless oldInterface) {
        if (!oldInterface.getIP().equals(newInterface.getIP()) ||
            !oldInterface.getMAC().equals(newInterface.getMAC()) ||
            !oldInterface.getMask().equals(newInterface.getMask()) ||
            !oldInterface.getDefaultGateway().equals(newInterface.getDefaultGateway()) ||
            !oldInterface.getLinkQuality().equals(newInterface.getLinkQuality()) ||
            !oldInterface.getSignalLevel().equals(newInterface.getSignalLevel()) ||
            !oldInterface.getNoiseLevel().equals(newInterface.getNoiseLevel())) {
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