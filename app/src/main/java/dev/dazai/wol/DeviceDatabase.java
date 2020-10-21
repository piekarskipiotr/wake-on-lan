package dev.dazai.wol;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Device.class}, version = 1)
public abstract class DeviceDatabase extends RoomDatabase {
    private static DeviceDatabase instance;
    private static String DATABASE_NAME = "device_database";
    public abstract DeviceDao deviceDao();

    public synchronized static DeviceDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DeviceDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;

    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DeviceDao deviceDao;

        private PopulateDbAsyncTask(DeviceDatabase deviceDatabase){
            deviceDao = deviceDatabase.deviceDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }



}