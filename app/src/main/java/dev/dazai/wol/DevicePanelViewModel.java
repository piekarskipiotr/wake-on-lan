package dev.dazai.wol;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DevicePanelViewModel extends AndroidViewModel {
    private DevicePanelRepository repository;

    public DevicePanelViewModel(@NonNull Application application) {
        super(application);
        repository = new DevicePanelRepository(application);

    }

    public LiveData<Device> getDeviceById(int id){
        return repository.getDeviceById(id);

    }

    public void insert(Device device){
        repository.insert(device);
    }
    
    public void update(Device device){
        repository.update(device);

    }

    public void delete(Device device){
        repository.delete(device);

    }




}
