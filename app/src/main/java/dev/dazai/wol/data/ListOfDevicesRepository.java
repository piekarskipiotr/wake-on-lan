package dev.dazai.wol.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ListOfDevicesRepository {
    private DataDao dataDao;
    private LiveData<List<Device>> nonReachableDevices;
    private LiveData<List<Device>> reachableDevices;

    public ListOfDevicesRepository(Application application){
        DeviceDatabase deviceDatabase = DeviceDatabase.getInstance(application);
        dataDao = deviceDatabase.dataDao();
        nonReachableDevices = dataDao.getNonActive();
        reachableDevices = dataDao.getActive();

    }

    public LiveData<List<Device>> getNonActive(){
        return nonReachableDevices;

    }

    public LiveData<List<Device>> getActive(){
        return reachableDevices;

    }
}
