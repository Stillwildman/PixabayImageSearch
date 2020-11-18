package com.vincent.imagesearch.database

import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vincent.imagesearch.AppController
import com.vincent.imagesearch.database.dao.DaoSearchRecord
import com.vincent.imagesearch.model.DBParams
import com.vincent.imagesearch.model.ItemSearchEntity
import java.lang.ref.SoftReference

/**
 * Created by Vincent on 2020/11/18.
 */
@Database(entities = [(ItemSearchEntity::class)], version = DBParams.DB_VERSION)
abstract class ImageSearchDatabase : RoomDatabase() {

    companion object {
        private object SingleDB {
            var INSTANCE: SoftReference<ImageSearchDatabase>? = null
        }

        fun getInstance(): ImageSearchDatabase {
            if (SingleDB.INSTANCE == null || SingleDB.INSTANCE?.get() == null) {
                SingleDB.INSTANCE = SoftReference(getDatabase())
            }
            return SingleDB.INSTANCE?.get()!!
        }

        private fun getDatabase() : ImageSearchDatabase {
            return Room.databaseBuilder(AppController.instance.applicationContext, ImageSearchDatabase::class.java, DBParams.DB_IMAGE_SEARCH)
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.i("PoiDatabase", "onMigrating!!! Current version: " + database.version)
                // TODO Migration if needed.
                Log.i("PoiDatabase", "Migration DONE!!!")
            }
        }
    }

    abstract fun getSearchRecordDao(): DaoSearchRecord
}