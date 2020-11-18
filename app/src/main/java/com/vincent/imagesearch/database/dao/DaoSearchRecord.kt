package com.vincent.imagesearch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vincent.imagesearch.model.DBParams
import com.vincent.imagesearch.model.ItemSearchEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Vincent on 2020/11/18.
 */
@Dao
interface DaoSearchRecord {

    @Query("SELECT COUNT(*) FROM SearchRecord")
    fun getCount(): Flowable<Int>

    @Query("DELETE FROM SearchRecord WHERE Id = (SELECT Id FROM SearchRecord ORDER BY Id ASC LIMIT 1)")
    fun deleteLastRecord(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecord(item: ItemSearchEntity): Completable

    @Query("SELECT * FROM SearchRecord ORDER BY Id DESC")
    fun getRecordList(): Flowable<List<ItemSearchEntity>>

    @Query("DELETE FROM SearchRecord WHERE ${DBParams.COLUMN_KEY_WORDS} = :keyWord")
    fun deleteDuplicatedKeyWordRecord(keyWord: String): Single<Int>
}