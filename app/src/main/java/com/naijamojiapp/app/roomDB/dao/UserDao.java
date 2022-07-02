package com.naijamojiapp.app.roomDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.naijamojiapp.app.roomDB.entity.AllStickerList;
import com.naijamojiapp.app.roomDB.entity.CatListTab;
import com.naijamojiapp.app.roomDB.entity.RecentStickerList;
import com.naijamojiapp.app.roomDB.entity.TagList;

import java.util.List;

@Dao
public interface  UserDao {

    //AllEmoji======================================================================================
    @Query("SELECT * FROM EmojiList")
    List<AllStickerList> getAllStickers();

    //listByTag=====================================================================================
    @Query("Select*from EmojiList where category_id = :categoryId")
    List<AllStickerList> getStickersByID(String categoryId);

    @Query("DELETE FROM EmojiList WHERE id = :stickerId")
    void deleteByStickerId(String stickerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(AllStickerList allStickerList);

    @Delete
    void delete(AllStickerList allStickerList);

    //CatListTab====================================================================================
    @Query("SELECT * FROM CatList")
    List<CatListTab> getAllCatList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCatList(CatListTab catListTab);

    @Query("DELETE FROM CatList WHERE id = :catId")
    void deleteByCatId(String catId);


    //TagList=======================================================================================
    @Query("SELECT * FROM TagList")
    List<TagList> getAllTagList();

    @Query("SELECT * FROM TagList WHERE name LIKE :words")
    List<TagList> getTagBySearch(String words);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTagList(TagList tagList);

    @Query("DELETE FROM TagList WHERE id = :tagId")
    void deleteByTagId(String tagId);

    //RecentStickers================================================================================
//    @Query("SELECT * FROM RecentStickersList")
    @Query("SELECT * FROM RecentStickersList ORDER BY uid DESC")
    List<RecentStickerList> getRecentStickersList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecentStickers(RecentStickerList recentStickerList);

    @Query("DELETE FROM RecentStickersList WHERE id = :id")
    void deleteRecentStickers(String id);

    @Query("DELETE FROM RecentStickersList")
    void deleteRecentTable();
}
