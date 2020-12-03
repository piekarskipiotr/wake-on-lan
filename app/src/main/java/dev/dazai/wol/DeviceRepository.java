package dev.dazai.wol;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

public class DeviceRepository {
    private DeviceDao deviceDao;
    private LiveData<List<Device>> allDevices;
    private LiveData<List<Device>> nonReachableDevices;
    private LiveData<List<Device>> reachableDevices;

    public DeviceRepository(Application application){
        DeviceDatabase deviceDatabase = DeviceDatabase.getInstance(application);
        deviceDao = deviceDatabase.deviceDao();
        allDevices = deviceDao.getAll();
        nonReachableDevices = deviceDao.getNonActive();
        reachableDevices = deviceDao.getActive();

    }

    public LiveData<List<Device>> getAllDevices(){
        return allDevices;
    }

    public LiveData<List<Device>> getNonActive(){
        return nonReachableDevices;

    }

    public LiveData<List<Device>> getActive(){
        return reachableDevices;

    }

    public void update(Device device){
        new DeviceRepository.UpdateDeviceAsyncTask(deviceDao).execute(device);

    }

    private static class UpdateDeviceAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDao;
        private UpdateDeviceAsyncTask(DeviceDao deviceDao){
            this.deviceDao = deviceDao;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            deviceDao.update(devices[0]);
            return null;
        }

    }
}
