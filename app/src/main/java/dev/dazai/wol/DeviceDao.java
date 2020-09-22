package dev.dazai.wol;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DeviceDao {
    @Query("SELECT * FROM device")
    List<Device> getAll();


    @Update
    void update(Device device);

    @Insert(onConflict = REPLACE)
    void insert(Device devices);

    @Delete
    void delete(Device device);
}
