package dev.dazai.wol;

import androidx.lifecycle.LiveData;
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
    LiveData<List<Device>> getAll();

    @Query("SELECT * FROM device WHERE is_reachable = 0")
    LiveData<List<Device>>getNonActive();

    @Query("SELECT * FROM device WHERE is_reachable = 1")
    LiveData<List<Device>> getActive();

    @Query("SELECT * FROM device WHERE deviceId LIKE :id")
    LiveData<Device> getById(int id);

    @Query("SELECT * FROM device WHERE device_name LIKE :name")
    Device getByName(String name);

    @Update
    void update(Device device);

    @Insert(onConflict = REPLACE)
    void insert(Device device);

    @Delete
    void delete(Device device);
}
