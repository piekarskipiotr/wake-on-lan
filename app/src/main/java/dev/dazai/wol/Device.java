package dev.dazai.wol;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "device_table", indices = {@Index(value = {"device_name"},
        unique = true), @Index(value = {"group_id"}, unique = true)})
public class Device {

    @PrimaryKey(autoGenerate = true)
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

    @ColumnInfo(name = "device_icon")
    public String deviceIcon;

    @ColumnInfo(name = "group_id")
    public int groupId;

    @ColumnInfo(name = "device_secure_on")
    public String deviceSecureOn;

    @ColumnInfo(name = "is_reachable")
    public Boolean isReachable;

    public Boolean getReachable() {
        return isReachable;
    }

    public void setReachable(Boolean reachable) {
        isReachable = reachable;
    }

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

    public String getDeviceIcon() {
        return deviceIcon;
    }

    public void setDeviceIcon(String deviceIcon) {
        this.deviceIcon = deviceIcon;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getDeviceSecureOn() {
        return deviceSecureOn;
    }

    public void setDeviceSecureOn(String deviceSecureOn) {
        this.deviceSecureOn = deviceSecureOn;
    }
}
