/* Access Points Class with Getters - Setters*/


package mainthread;
class AccessPoint {
    
    private String mac = "Not Found";
    private String essid = "Not Found";
    private String mode = "Not Found";
    private String signalLevel = "Not Found";
    private String channel = "Not Found";
    
    
    public String getMAC() {
        return mac; }
    
    public String getESSID() {
        return essid; }
    
    public String getChannel() {
        return channel; }
    
    public String getMode() {
        return mode; }
    
    public String getSignalLevel() {
        return signalLevel; }
    
    
    public void setMAC(String mac) {
        this.mac = mac; }
    
    public void setESSID(String essid) {
        this.essid = essid; }
    
    public void setChannel(String channel) {
        this.channel = channel; }
    
    public void setMode(String mode) {
        this.mode = mode; }
    
    public void setSignalLevel(String signalLevel) {
        this.signalLevel = signalLevel; } 
}