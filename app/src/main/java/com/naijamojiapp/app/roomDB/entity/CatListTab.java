package com.naijamojiapp.app.roomDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.naijamojiapp.app.roomDB.DateConverter;


@Entity(tableName = "CatList", indices = @Index(value = {"id"}, unique = true))
public class CatListTab {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "updated_at")
    private String updated_at;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "created_at")
    private String created_at;

    @ColumnInfo(name = "position")
    private String position;

    @Ignore
    private Boolean isSelect = false;


    public CatListTab(String id,
                      String image,
                      String updated_at,
                      String name,
                      String created_at)
    {
        this.id = id;
        this.image = image;
        this.updated_at = updated_at;
        this.name = name;
        this.id = id;
        this.created_at = created_at;

    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}