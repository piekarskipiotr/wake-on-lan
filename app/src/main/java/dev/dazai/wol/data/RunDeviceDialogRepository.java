package dev.dazai.wol.data;

import android.app.Application;
import android.os.AsyncTask;

public class RunDeviceDialogRepository {
    private DataDao dataDao;

    public RunDeviceDialogRepository(Application application){
        DeviceDatabase deviceDatabase = DeviceDatabase.getInstance(application);
        dataDao = deviceDatabase.dataDao();

    }


    public void update(Device device){
        new RunDeviceDialogRepository.UpdateDeviceAsyncTask(dataDao).execute(device);

    }

    private static class UpdateDeviceAsyncTask extends AsyncTask<Device, Void, Void> {
        private DataDao dataDao;
        private UpdateDeviceAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            dataDao.update(devices[0]);
            return null;
        }

    }
}
