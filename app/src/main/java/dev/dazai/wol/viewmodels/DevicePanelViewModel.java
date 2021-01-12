package dev.dazai.wol.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.dazai.wol.data.Device;
import dev.dazai.wol.data.DevicePanelRepository;
import dev.dazai.wol.data.Group;

public class DevicePanelViewModel extends AndroidViewModel {
    private DevicePanelRepository repository;
    LiveData<List<Group>> allGroups;

    public DevicePanelViewModel(@NonNull Application application) {
        super(application);
        repository = new DevicePanelRepository(application);
        allGroups = repository.getAllGroups();

    }

    public LiveData<Group> getGroupById(int id){
        return repository.getGroupById(id);
    }

    public LiveData<List<Group>> getAllGroups(){
        return allGroups;
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
