package dev.dazai.wol;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class NetworkScanner extends AsyncTask<Integer, String, List<String>> {
    private static final String TAG = "Network Scanner: ";
    private WeakReference<DashboardFragment> weakReference;
    public NetworkScanner(DashboardFragment fragment) {
        weakReference = new WeakReference<>(fragment);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG,"scanning...");
        weakReference.get().dialogNetworkScanningBinding.progressCircular.setVisibility(View.VISIBLE);
        weakReference.get().dialogNetworkScanningBinding.progressText.setVisibility(View.VISIBLE);
        weakReference.get().dialogNetworkScanningBinding.stopNetworkScanningButton.setVisibility(View.VISIBLE);
        weakReference.get().dialogNetworkScanningBinding.reNetworkScanningButton.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.manualInputNetworkScanningButton.setVisibility(View.GONE);

    }

    @Override
    protected List<String> doInBackground(Integer... integers) {
//        int TIME_OUT = integers[0];
        //used to be reachableDevices list
        List<String> availableDevices = new ArrayList<>();
        InetAddress inetAddress;
        String ipAddress;
        String macAddress;
        String deviceName;

        //quick fix for network, it's for ping every IP in our mask so arp can get it
        WifiManager wifiManager = (WifiManager)
                weakReference.get().requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wifiManager.getDhcpInfo().serverAddress);

        ip = ip.substring(0, ip.lastIndexOf(".")+1);
        for(int i = 1; i < 255; i++){
            try {
                InetAddress.getByName(ip+i).isReachable(1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            Scanner fileReader = new Scanner(new File("/proc/net/arp"));
            fileReader.nextLine();
            while(fileReader.hasNext()){
                String[] line = fileReader.nextLine().split(" +");
                if(line.length >= 4){
                    ipAddress = line[0];
                    macAddress = line[3];
                    if (macAddress.matches("..:..:..:..:..:..") && !macAddress.matches("00:00:00:00:00:00")) {
                        inetAddress = InetAddress.getByName(ipAddress);

                        deviceName = inetAddress.getCanonicalHostName();
                        deviceName = deviceName.substring(0, deviceName.lastIndexOf("."));

//                        boolean reachable = inetAddress.isReachable(TIME_OUT);

//                        if(reachable){
                        availableDevices.add(deviceName);
                        availableDevices.add(macAddress);
                        availableDevices.add(ipAddress);
                        publishProgress(deviceName, macAddress, ipAddress);

//                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return availableDevices;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
//        Log.i(TAG, values[0] + "[" + values[1] + "] (" + values[2] + ") is reachable and has been added to the list!");
        Log.i(TAG, values[0] + "[" + values[1] + "] (" + values[2] + ")");
        weakReference.get().insertDevice(values[0], values[2], values[1]);



    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        weakReference.get().dialogNetworkScanningBinding.progressCircular.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.progressText.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.stopNetworkScanningButton.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.reNetworkScanningButton.setVisibility(View.VISIBLE);
        weakReference.get().dialogNetworkScanningBinding.manualInputNetworkScanningButton.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        weakReference.get().dialogNetworkScanningBinding.progressCircular.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.progressText.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.stopNetworkScanningButton.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.reNetworkScanningButton.setVisibility(View.VISIBLE);
        weakReference.get().dialogNetworkScanningBinding.manualInputNetworkScanningButton.setVisibility(View.VISIBLE);

    }

}
