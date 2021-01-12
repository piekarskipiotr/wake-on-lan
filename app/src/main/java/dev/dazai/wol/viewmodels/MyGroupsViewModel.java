package dev.dazai.wol.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.dazai.wol.data.Device;
import dev.dazai.wol.data.Group;
import dev.dazai.wol.data.GroupWithDevices;
import dev.dazai.wol.data.MyGroupsRepository;

public class MyGroupsViewModel extends AndroidViewModel {
    private MyGroupsRepository repository;
    private LiveData<List<GroupWithDevices>> allGroupsAndDevices;

    public MyGroupsViewModel(@NonNull Application application) {
        super(application);
        repository = new MyGroupsRepository(application);
        allGroupsAndDevices = repository.getAllGroupsAndDevices();
    }

    public LiveData<List<GroupWithDevices>> getAllGroupsAndDevices(){
        return allGroupsAndDevices;
    }

    public void insert(Group group){
        repository.insert(group);
    }

    public void update(Group group){
        repository.update(group);
    }

    public void update(Device device){
        repository.update(device);
    }

    public void delete(Group group){
        repository.delete(group);
    }


}
