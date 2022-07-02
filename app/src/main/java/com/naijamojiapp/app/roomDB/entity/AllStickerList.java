package com.naijamojiapp.app.roomDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.naijamojiapp.app.roomDB.DateConverter;


@Entity(tableName = "EmojiList", indices = @Index(value = {"id"}, unique = true))
@TypeConverters({DateConverter.class})
public class AllStickerList {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "category_id")
    private String category_id;

    @ColumnInfo(name = "is_publish")
    private String is_publish;

    @ColumnInfo(name = "is_studiomode")
    private String is_studiomode;

    @ColumnInfo(name = "is_gender_available")
    private String is_gender_available;

    @ColumnInfo(name = "text_limit")
    private String text_limit;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "img_f_dark")
    private String img_f_dark;

    @ColumnInfo(name = "img_f_medium")
    private String img_f_medium;

    @ColumnInfo(name = "img_f_light")
    private String img_f_light;

    @ColumnInfo(name = "img_m_dark")
    private String img_m_dark;

    @ColumnInfo(name = "img_m_medium")
    private String img_m_medium;

    @ColumnInfo(name = "img_m_light")
    private String img_m_light;

    @ColumnInfo(name = "position")
    private String position;

    @ColumnInfo(name = "created_at")
    private String created_at;

    @ColumnInfo(name = "updated_at")
    private String updated_at;

    @Ignore
    private String genderSticker;


    public AllStickerList(String id,
                          String title,
                          String category_id,
                          String is_publish,
                          String is_studiomode,
                          String is_gender_available,
                          String text_limit,
                          String image,
                          String img_f_dark,
                          String img_f_medium,
                          String img_f_light,
                          String img_m_dark,
                          String img_m_medium,
                          String img_m_light,
                          String position,
                          String created_at,
                          String updated_at)
    {
        this.id = id;
        this.title = title;
        this.category_id = category_id;
        this.is_publish = is_publish;
        this.is_studiomode = is_studiomode;
        this.is_gender_available = is_gender_available;
        this.text_limit = text_limit;
        this.image = image;
        this.img_f_dark = img_f_dark;
        this.img_f_medium = img_f_medium;
        this.img_f_light = img_f_light;
        this.img_m_dark = img_m_dark;
        this.img_m_medium = img_m_medium;
        this.img_m_light = img_m_light;
        this.position = position;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getGenderSticker() {
        return genderSticker;
    }

    public void setGenderSticker(String genderSticker) {
        this.genderSticker = genderSticker;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }

    public String getIs_studiomode() {
        return is_studiomode;
    }

    public void setIs_studiomode(String is_studiomode) {
        this.is_studiomode = is_studiomode;
    }

    public String getIs_gender_available() {
        return is_gender_available;
    }

    public void setIs_gender_available(String is_gender_available) {
        this.is_gender_available = is_gender_available;
    }

    public String getText_limit() {
        return text_limit;
    }

    public void setText_limit(String text_limit) {
        this.text_limit = text_limit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImg_f_dark() {
        return img_f_dark;
    }

    public void setImg_f_dark(String img_f_dark) {
        this.img_f_dark = img_f_dark;
    }

    public String getImg_f_medium() {
        return img_f_medium;
    }

    public void setImg_f_medium(String img_f_medium) {
        this.img_f_medium = img_f_medium;
    }

    public String getImg_f_light() {
        return img_f_light;
    }

    public void setImg_f_light(String img_f_light) {
        this.img_f_light = img_f_light;
    }

    public String getImg_m_dark() {
        return img_m_dark;
    }

    public void setImg_m_dark(String img_m_dark) {
        this.img_m_dark = img_m_dark;
    }

    public String getImg_m_medium() {
        return img_m_medium;
    }

    public void setImg_m_medium(String img_m_medium) {
        this.img_m_medium = img_m_medium;
    }

    public String getImg_m_light() {
        return img_m_light;
    }

    public void setImg_m_light(String img_m_light) {
        this.img_m_light = img_m_light;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}