package dev.dazai.wol;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Device.class}, version = 2)
public abstract class DeviceDatabase extends RoomDatabase {
    private static DeviceDatabase database;
    private static String DATABASE_NAME = "device_database";

    public synchronized static DeviceDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    DeviceDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;

    }

    public abstract DeviceDao deviceDao();

}