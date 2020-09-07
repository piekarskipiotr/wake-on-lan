package dev.dazai.wol;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.WIFI_SERVICE;

public class NetworkScanner extends AsyncTask<Integer, String, List<String>> {
    private static final String TAG = "Network Scanner";
    private WeakReference<DashboardFragment> weakReference;


    public NetworkScanner(DashboardFragment fragment) {
        weakReference = new WeakReference<>(fragment);
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

            WifiManager wifiManager = (WifiManager) Objects.requireNonNull(weakReference.get().getActivity()).getApplicationContext().getSystemService(WIFI_SERVICE);
            @SuppressWarnings("deprecation")
            String deviceIP = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            String mask = deviceIP.substring(0, deviceIP.lastIndexOf(".") + 1);

            for (int i = integers[0]; i <= integers[1]; i++) {
                String createdIP = mask + i;
                InetAddress address = InetAddress.getByName(createdIP);
                String hostName = address.getCanonicalHostName();

                if(hostName.matches("") || hostName.contains(mask)){
                    continue;
                }else{
                    hostName = hostName.substring(0, hostName.lastIndexOf("."));
                }
                boolean reachable = address.isReachable(integers[2]);
                Log.i(TAG,hostName + " (" + createdIP  + ")");

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
        Log.i(TAG, values[0] + " (" + values[1] + ") is reachable and has been added to the list!");
        weakReference.get().dialogNetworkScanningBinding.test.append(values[0] + " (" + values[1] + ")\n");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        weakReference.get().dialogNetworkScanningBinding.progressCircular.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.progressText.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.stopNetworkScanningButton.setVisibility(View.GONE);
        weakReference.get().dialogNetworkScanningBinding.reNetworkScanningButton.setVisibility(View.VISIBLE);
    }

}
