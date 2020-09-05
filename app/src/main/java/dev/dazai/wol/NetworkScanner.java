package dev.dazai.wol;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class NetworkScanner extends AsyncTask<Integer, String, List<String>> {
    private static final String TAG = "Network Scanner";
    private WeakReference<Context> contextRef;
    public NetworkScannerResults delegate = null;

    public NetworkScanner(Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG,"scanning...");
    }

    @Override
    protected List<String> doInBackground(Integer... integers) {
        List<String> networkList = new ArrayList<>();
        try {
            WifiManager wifiManager = (WifiManager) contextRef.get().getApplicationContext().getSystemService(WIFI_SERVICE);
            @SuppressWarnings("deprecation")
            String deviceIP = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            String mask = deviceIP.substring(0, deviceIP.lastIndexOf(".") + 1);

            for (int i = integers[0]; i <= integers[1]; i++) {
                String createdIP = mask + i;
                InetAddress address = InetAddress.getByName(createdIP);
                boolean reachable = address.isReachable(integers[2]);
                String hostName = address.getCanonicalHostName();
                if(hostName.matches("") || hostName.contains(mask)){
                    hostName = "";
                }else{
                    hostName = hostName.substring(0, hostName.lastIndexOf("."));
                }
                Log.i(TAG,"IP: " + createdIP + "(" + hostName  + ")");

                if(reachable){
                    networkList.add(hostName);
                    networkList.add(createdIP);
                    publishProgress(hostName, createdIP);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return networkList;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.i(TAG, values[0] + " ("+ values[1] + ") is reachable and has been added to the list!");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        delegate.processFinish(strings);


    }


}
