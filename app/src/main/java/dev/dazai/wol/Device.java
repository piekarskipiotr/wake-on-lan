package dev.dazai.wol;

public class Device {
    private String name, ipAddress, macAddress;

    public Device(String deviceName, String deviceIp, String deviceMac){
        name = deviceName;
        ipAddress = deviceIp;
        macAddress = deviceMac;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }


}
