package dev.dazai.wol.data;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

public class DashboardRepository {
    private DataDao dataDao;
    private LiveData<List<Device>> allDevices;
    private LiveData<List<Device>> nonReachableDevices;
    private LiveData<List<Device>> reachableDevices;

    public DashboardRepository(Application application){
        DeviceDatabase deviceDatabase = DeviceDatabase.getInstance(application);
        dataDao = deviceDatabase.dataDao();
        allDevices = dataDao.getAll();
        nonReachableDevices = dataDao.getNonActive();
        reachableDevices = dataDao.getActive();

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
        new DashboardRepository.UpdateDeviceAsyncTask(dataDao).execute(device);

    }

    private static class UpdateDeviceAsyncTask extends AsyncTask<Device, Void, Void> {
        private DataDao dataDao;
        private UpdateDeviceAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            dataDao.update(devices[0]);
            return null;
        }

    }
}
