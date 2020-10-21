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
        deviceDatabase.deviceDao();

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
        new UpdateNoteAsyncTask(deviceDao).equals(device);
    }

    public void insert(Device device){
        new InsertNoteAsyncTask(deviceDao).equals(device);

    }

    public void delete(Device device){
        new DeleteNoteAsyncTask(deviceDao).equals(device);
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDao;
        private UpdateNoteAsyncTask(DeviceDao deviceDao){
            this.deviceDao = deviceDao;
        }


        @Override
        protected Void doInBackground(Device... devices) {
            deviceDao.update(devices[0]);
            return null;
        }
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDao;
        private InsertNoteAsyncTask(DeviceDao deviceDao){
            this.deviceDao = deviceDao;
        }


        @Override
        protected Void doInBackground(Device... devices) {
            deviceDao.insert(devices[0]);
            return null;
        }
    }



    private static class DeleteNoteAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDao;
        private DeleteNoteAsyncTask(DeviceDao deviceDao){
            this.deviceDao = deviceDao;
        }


        @Override
        protected Void doInBackground(Device... devices) {
            deviceDao.delete(devices[0]);
            return null;
        }
    }

}
