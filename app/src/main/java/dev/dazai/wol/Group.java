package dev.dazai.wol;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "group_table", indices = {@Index(value = {"group_name"},
        unique = true)})

public class Group {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    public int groupId;

    @NonNull
    @ColumnInfo(name = "group_name")
    public String groupName;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}
