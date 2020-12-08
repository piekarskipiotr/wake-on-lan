package dev.dazai.wol;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyGroupsViewModel extends AndroidViewModel {
    private MyGroupsRepository repository;
    private LiveData<List<Group>> allGroups;
    private LiveData<List<GroupWithDevices>> allGroupsAndDevices;

    public MyGroupsViewModel(@NonNull Application application) {
        super(application);
        repository = new MyGroupsRepository(application);
        allGroups = repository.getAllGroups();
        allGroupsAndDevices = repository.getAllGroupsAndDevices();
    }

    public LiveData<List<GroupWithDevices>> getAllGroupsAndDevices(){
        return allGroupsAndDevices;
    }

    public LiveData<List<Group>> getAllGroups(){
        return allGroups;
    }

    public LiveData<List<Device>> getDevicesByGroupId(int id){
        return repository.getDevicesByGroupId(id);

    }

    public void insert(Group group){
        repository.insert(group);
    }

    public void update(Group group){
        repository.update(group);
    }

    public void delete(Group group){
        repository.delete(group);
    }


}
