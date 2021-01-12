package dev.dazai.wol.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.dazai.wol.data.Device;
import dev.dazai.wol.data.ListOfDevicesRepository;

public class ListOfDevicesViewModel extends AndroidViewModel {
    private ListOfDevicesRepository repository;
    private LiveData<List<Device>> savedDevices;
    private LiveData<List<Device>> activeDevices;

    public ListOfDevicesViewModel(@NonNull Application application) {
        super(application);
        repository = new ListOfDevicesRepository(application);
        savedDevices = repository.getNonActive();
        activeDevices = repository.getActive();
    }

    public LiveData<List<Device>> getSavedDevices(){
        return savedDevices;

    }

    public LiveData<List<Device>> getActiveDevices(){
        return activeDevices;

    }


}
