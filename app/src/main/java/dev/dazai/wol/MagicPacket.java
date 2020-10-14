package dev.dazai.wol;

import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MagicPacket {
    private static final String TAG = "MagicPacket: ";

    public void send(String ipAddress, String macAddress, int port){
        try {
            byte[] macBytes = getMacBytes(macAddress);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ipAddress), port);
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
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
