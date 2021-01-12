package dev.dazai.wol.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import dev.dazai.wol.data.DashboardRepository;
import dev.dazai.wol.data.Device;

public class DashboardViewModel extends AndroidViewModel {
    private DashboardRepository repository;
    private LiveData<List<Device>> allDevices;
    private LiveData<List<Device>> savedDevices;
    private LiveData<List<Device>> activeDevices;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repository = new DashboardRepository(application);
        allDevices = repository.getAllDevices();
        savedDevices = repository.getNonActive();
        activeDevices = repository.getActive();

    }

    public LiveData<List<Device>> getAllDevices(){
        return allDevices;

    }

    public LiveData<List<Device>> getSavedDevices(){
        return savedDevices;

    }

    public LiveData<List<Device>> getActiveDevices(){
        return activeDevices;

    }

    public void update(Device device){
        repository.update(device);

    }
}
