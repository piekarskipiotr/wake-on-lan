package dev.dazai.wol.network;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class MagicPacket {
    private static final String TAG = "MagicPacket: ";

    public void send(String macAddress, int port, String secureOn, Context context){
        try {
            byte[] macBytes = getMacBytes(macAddress);
            byte[] secureOnBytes = getMacBytes(secureOn);

            byte[] bytes = new byte[6 + 16 * macBytes.length + 6];
            for (int i = 0; i < 6; i++)
                bytes[i] = (byte) 0xff;

            for (int i = 6; i < bytes.length; i += macBytes.length)
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);

            //102 is the position of last mac address bytes and on the end of the magic packet we heave to put secureOn
            //here is link to info-graphics: https://www.codeproject.com/KB/IP/WOL/WOL_MagicP_Big.GIF
            System.arraycopy(secureOnBytes, 0, bytes, 102, secureOnBytes.length);

            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int broadcastInteger = (wifiManager.getDhcpInfo().ipAddress & wifiManager.getDhcpInfo().netmask) | ~wifiManager.getDhcpInfo().netmask;
            String broadcast = Formatter.formatIpAddress(broadcastInteger);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(broadcast), port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

            Log.i(TAG, "Magic packet has been sent.");
        }
        catch (Exception e) {
            Log.e(TAG,"Failed to send magic packet: " + e);

        }
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        try {
            for (int i = 0; i < 6; i++)
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);


        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
