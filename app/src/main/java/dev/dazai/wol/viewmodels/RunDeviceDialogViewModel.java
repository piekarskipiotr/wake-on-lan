package dev.dazai.wol.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import dev.dazai.wol.data.Device;
import dev.dazai.wol.data.RunDeviceDialogRepository;

public class RunDeviceDialogViewModel extends AndroidViewModel {
    private RunDeviceDialogRepository repository;
    public RunDeviceDialogViewModel(@NonNull Application application) {
        super(application);
        repository = new RunDeviceDialogRepository(application);
    }

    public void update(Device device){
        repository.update(device);

    }
}
