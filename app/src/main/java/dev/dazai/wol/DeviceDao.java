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

    @Query("SELECT * FROM device WHERE is_reachable == 0")
    List<Device> getNonActive();

    @Query("SELECT * FROM device WHERE is_reachable == 1")
    List<Device> getActive();

    @Query("SELECT * FROM device WHERE deviceId == :id")
    Device getById(int id);

    @Update
    void update(Device device);

    @Insert(onConflict = REPLACE)
    void insert(Device device);

    @Delete
    void delete(Device device);
}
