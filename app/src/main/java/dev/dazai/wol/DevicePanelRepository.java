package dev.dazai.wol;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DevicePanelRepository {
    private DeviceDao deviceDao;
    private LiveData<Device> deviceById;


    public DevicePanelRepository(Application application){
        DeviceDatabase deviceDatabase = DeviceDatabase.getInstance(application);
        deviceDao = deviceDatabase.deviceDao();
        
    }

    public LiveData<Device> getDeviceById(int id){
        deviceById = deviceDao.getById(id);
        return deviceById;
    }

    public void insert(Device device){
        new InsertDeviceAsyncTask(deviceDao).execute(device);

    }

    public void update(Device device){
        new UpdateDeviceAsyncTask(deviceDao).execute(device);

    }

    public void delete(Device device){
        new DeleteDeviceAsyncTask(deviceDao).execute(device);

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

    private static class DeleteDeviceAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDao;
        private DeleteDeviceAsyncTask(DeviceDao deviceDao){
            this.deviceDao = deviceDao;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            deviceDao.delete(devices[0]);
            return null;
        }
    }

}
