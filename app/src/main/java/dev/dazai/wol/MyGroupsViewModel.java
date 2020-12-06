package dev.dazai.wol;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyGroupsViewModel extends AndroidViewModel {
    private MyGroupsRepository repository;
    private LiveData<List<Group>> allGroups;

    public MyGroupsViewModel(@NonNull Application application) {
        super(application);
        repository = new MyGroupsRepository(application);
        allGroups = repository.getAllGroups();
    }

    public LiveData<List<Group>> getAllGroups(){
        return allGroups;
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
