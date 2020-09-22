package dev.dazai.wol;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Device {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int deviceId;

    @NonNull
    @ColumnInfo(name = "device_name")
    public String deviceName;

    @NonNull
    @ColumnInfo(name = "device_ip_address")
    public String deviceIpAddress;

    @NonNull
    @ColumnInfo(name = "device_mac_address")
    public String deviceMacAddress;

    @NonNull
    @ColumnInfo(name = "device_lan_port")
    public String deviceLanPort;

    @ColumnInfo(name = "device_wan_port")
    public String deviceWanPort;

    @ColumnInfo(name = "device_icon")
    public String deviceIcon;

    @ColumnInfo(name = "device_group")
    public String deviceGroup;

    @ColumnInfo(name = "device_router_ip")
    public String deviceRouterIp;

    @ColumnInfo(name = "device_secure_on")
    public String deviceSecureOn;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @NonNull
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(@NonNull String deviceName) {
        this.deviceName = deviceName;
    }

    @NonNull
    public String getDeviceIpAddress() {
        return deviceIpAddress;
    }

    public void setDeviceIpAddress(@NonNull String deviceIpAddress) {
        this.deviceIpAddress = deviceIpAddress;
    }

    @NonNull
    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(@NonNull String deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    @NonNull
    public String getDeviceLanPort() {
        return deviceLanPort;
    }

    public void setDeviceLanPort(@NonNull String deviceLanPort) {
        this.deviceLanPort = deviceLanPort;
    }

    public String getDeviceWanPort() {
        return deviceWanPort;
    }

    public void setDeviceWanPort(String deviceWanPort) {
        this.deviceWanPort = deviceWanPort;
    }

    public String getDeviceIcon() {
        return deviceIcon;
    }

    public void setDeviceIcon(String deviceIcon) {
        this.deviceIcon = deviceIcon;
    }

    public String getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(String deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public String getDeviceRouterIp() {
        return deviceRouterIp;
    }

    public void setDeviceRouterIp(String deviceRouterIp) {
        this.deviceRouterIp = deviceRouterIp;
    }

    public String getDeviceSecureOn() {
        return deviceSecureOn;
    }

    public void setDeviceSecureOn(String deviceSecureOn) {
        this.deviceSecureOn = deviceSecureOn;
    }
}
