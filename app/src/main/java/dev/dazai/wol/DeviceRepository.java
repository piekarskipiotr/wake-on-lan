package dev.dazai.wol;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

public class DeviceRepository {
    private DeviceDao deviceDao;
    private LiveData<List<Device>> nonReachableDevices;
    private LiveData<List<Device>> reachableDevices;

    public DeviceRepository(Application application){
        DeviceDatabase deviceDatabase = DeviceDatabase.getInstance(application);
        deviceDao = deviceDatabase.deviceDao();
        nonReachableDevices = deviceDao.getNonActive();
        reachableDevices = deviceDao.getActive();

    }

    public LiveData<List<Device>> getNonActive(){
        return nonReachableDevices;

    }

    public LiveData<List<Device>> getActive(){
        return reachableDevices;

    }

    public void insert(Device device){
        new InsertDeviceAsyncTask(deviceDao).execute(device);

    }

    private static class InsertDeviceAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDao;
        private InsertDeviceAsyncTask(DeviceDao deviceDao){
            this.deviceDao = deviceDao;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            deviceDao.insert(devices[0]);
            return null;
        }

    }


}
