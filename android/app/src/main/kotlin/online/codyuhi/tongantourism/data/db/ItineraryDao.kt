package online.codyuhi.tongantourism.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItineraryDao {
    @Query("SELECT * FROM saved_itineraries ORDER BY createdAt DESC")
    fun getAllItineraries(): Flow<List<ItineraryEntity>>

    @Query("SELECT * FROM saved_itineraries WHERE id = :id LIMIT 1")
    suspend fun getItineraryById(id: String): ItineraryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItinerary(itinerary: ItineraryEntity)

    @Query("DELETE FROM saved_itineraries WHERE id = :id")
    suspend fun deleteItineraryById(id: String)

    @Query("SELECT COUNT(*) FROM saved_itineraries")
    fun countItineraries(): Flow<Int>
}
