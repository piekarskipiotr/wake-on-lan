package dev.dazai.wol;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

public class MyGroupsRepository {
    private DataDao dataDao;
    private LiveData<List<Group>> allGroups;


    public MyGroupsRepository(Application application){
        DeviceDatabase deviceDatabase = DeviceDatabase.getInstance(application);
        dataDao = deviceDatabase.dataDao();
        allGroups = dataDao.getAllGroups();

    }

    public LiveData<List<Group>> getAllGroups(){
        return allGroups;
    }


    public void insert(Group group){
        new InsertGroupAsyncTask(dataDao).execute(group);

    }

    public void update(Group group){
        new UpdateGroupAsyncTask(dataDao).execute(group);

    }

    public void delete(Group group){
        new DeleteGroupAsyncTask(dataDao).execute(group);

    }

    private static class InsertGroupAsyncTask extends AsyncTask<Group, Void, Void> {
        private DataDao dataDao;
        private InsertGroupAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            dataDao.insert(groups[0]);
            return null;
        }

    }

    private static class UpdateGroupAsyncTask extends AsyncTask<Group, Void, Void> {
        private DataDao dataDao;
        private UpdateGroupAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            dataDao.update(groups[0]);
            return null;
        }

    }

    private static class DeleteGroupAsyncTask extends AsyncTask<Group, Void, Void> {
        private DataDao dataDao;
        private DeleteGroupAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            dataDao.delete(groups[0]);
            return null;
        }
    }
}
