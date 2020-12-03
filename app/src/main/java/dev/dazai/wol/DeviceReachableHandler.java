package dev.dazai.wol;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.List;

public class DeviceReachableHandler extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "ReachableHandler: ";
    private WeakReference<DashboardFragment> weakReference;
    public DeviceReachableHandler(DashboardFragment fragment, List<Device> devices) {
        allDevices = devices;
        size = devices.size() - 1;
        weakReference = new WeakReference<>(fragment);
    }
    List<Device> allDevices;
    int size;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG,"checking...");

    }

    @Override
    protected Void doInBackground(Void... voids) {
        Device device;
        boolean preDeviceReachableStatus = false;
        boolean currentDeviceReachableStatus;
            for(int i = 0; i <= size; i++){
                device = allDevices.get(i);

                if(device.getReachable() != null){
                    preDeviceReachableStatus = device.getReachable();
                }else{
                    try {
                        if(InetAddress.getByName(device.getDeviceIpAddress()).isReachable(500))
                            currentDeviceReachableStatus = true;
                        else
                            currentDeviceReachableStatus = false;

                        device.setReachable(currentDeviceReachableStatus);
                        weakReference.get().updateDeviceReachableStatus(device);
                        continue;

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                try {
                    if(InetAddress.getByName(device.getDeviceIpAddress()).isReachable(500))
                        currentDeviceReachableStatus = true;
                    else
                        currentDeviceReachableStatus = false;

                    Log.d("CHUJ", preDeviceReachableStatus + " "  + currentDeviceReachableStatus);
                    if(preDeviceReachableStatus != currentDeviceReachableStatus){
                        device.setReachable(currentDeviceReachableStatus);
                        weakReference.get().updateDeviceReachableStatus(device);

                    }

                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

}
