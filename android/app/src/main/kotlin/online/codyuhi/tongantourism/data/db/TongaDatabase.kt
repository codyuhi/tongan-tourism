package online.codyuhi.tongantourism.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ItineraryEntity::class], version = 1, exportSchema = false)
abstract class TongaDatabase : RoomDatabase() {
    abstract fun itineraryDao(): ItineraryDao

    companion object {
        @Volatile
        private var instance: TongaDatabase? = null

        fun getInstance(context: Context): TongaDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TongaDatabase::class.java,
                    "tongan_tourism.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build().also { instance = it }
            }
        }
    }
}
