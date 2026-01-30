package com.example.expensetrcaker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    LiveData<List<CategoryEntity>> getAllCategories();

    @Query("SELECT * FROM categories WHERE type = :type")
    LiveData<List<CategoryEntity>> getCategoriesByType(String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(CategoryEntity category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryEntity> categories);

    @Update
    void updateCategory(CategoryEntity category);

    @Delete
    void deleteCategory(CategoryEntity category);
}
