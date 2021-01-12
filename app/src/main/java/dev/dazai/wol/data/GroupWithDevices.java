package dev.dazai.wol.data;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class GroupWithDevices {
    public @Embedded Group group;
    @Relation(
            parentColumn = "group_id",
            entityColumn = "device_group_id"
    )
    public List<Device> devices;
}
