package com.example.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.model.CatalogPart
import com.example.model.UserVehicle
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "local_search_history")
data class LocalSearchHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val userId: String,
  val queryText: String,
  val vehicleSummary: String,
  val resultCount: Int,
  val searchedAtEpochMs: Long = System.currentTimeMillis(),
)

@Entity(tableName = "local_viewed_parts")
data class LocalViewedPartEntity(
  @PrimaryKey val partKey: String,
  val userId: String,
  val partId: String,
  val partName: String,
  val oemNumber: String,
  val aftermarketNumber: String,
  val systemName: String,
  val vehicleSummary: String,
  val aiVerificationSummary: String? = null,
  val viewedAtEpochMs: Long = System.currentTimeMillis(),
)

@Entity(tableName = "local_selected_vehicles")
data class LocalSelectedVehicleEntity(
  @PrimaryKey val vehicleKey: String,
  val userId: String,
  val country: String,
  val marketCode: String,
  val brand: String,
  val model: String,
  val year: Long,
  val variant: String,
  val odometerKm: Long,
  val selectedAtEpochMs: Long = System.currentTimeMillis(),
)

@Dao
interface LocalHistoryDao {
  @Query("SELECT * FROM local_search_history WHERE userId = :userId ORDER BY searchedAtEpochMs DESC LIMIT 50")
  fun observeSearchHistory(userId: String): Flow<List<LocalSearchHistoryEntity>>

  @Query("SELECT * FROM local_viewed_parts WHERE userId = :userId ORDER BY viewedAtEpochMs DESC LIMIT 60")
  fun observeViewedParts(userId: String): Flow<List<LocalViewedPartEntity>>

  @Query("SELECT * FROM local_selected_vehicles WHERE userId = :userId ORDER BY selectedAtEpochMs DESC LIMIT 25")
  fun observeSelectedVehicles(userId: String): Flow<List<LocalSelectedVehicleEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSearch(entry: LocalSearchHistoryEntity)

  @Query("DELETE FROM local_search_history WHERE userId = :userId AND queryText = :queryText")
  suspend fun deleteDuplicateSearch(userId: String, queryText: String)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertViewedPart(entry: LocalViewedPartEntity)

  @Query(
    "UPDATE local_viewed_parts SET aiVerificationSummary = :summary, viewedAtEpochMs = :updatedAt WHERE partKey = :partKey AND userId = :userId"
  )
  suspend fun updateViewedPartAiReview(
    userId: String,
    partKey: String,
    summary: String,
    updatedAt: Long,
  )

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertSelectedVehicle(entry: LocalSelectedVehicleEntity)

  @Query("DELETE FROM local_search_history WHERE userId = :userId")
  suspend fun clearSearchHistory(userId: String)

  @Query("DELETE FROM local_viewed_parts WHERE userId = :userId")
  suspend fun clearViewedParts(userId: String)
}

@Database(
  entities = [
    LocalSearchHistoryEntity::class,
    LocalViewedPartEntity::class,
    LocalSelectedVehicleEntity::class,
  ],
  version = 1,
  exportSchema = false,
)
abstract class AppLocalDatabase : RoomDatabase() {
  abstract fun localHistoryDao(): LocalHistoryDao

  companion object {
    @Volatile
    private var INSTANCE: AppLocalDatabase? = null

    fun getInstance(context: Context): AppLocalDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room.databaseBuilder(
          context.applicationContext,
          AppLocalDatabase::class.java,
          "autopulse_local_history.db",
        ).build().also { INSTANCE = it }
      }
    }
  }
}

class LocalHistoryRepository(private val dao: LocalHistoryDao) {

  fun observeSearchHistory(userId: String): Flow<List<LocalSearchHistoryEntity>> =
    dao.observeSearchHistory(userId)

  fun observeViewedParts(userId: String): Flow<List<LocalViewedPartEntity>> =
    dao.observeViewedParts(userId)

  fun observeSelectedVehicles(userId: String): Flow<List<LocalSelectedVehicleEntity>> =
    dao.observeSelectedVehicles(userId)

  suspend fun recordSearch(
    userId: String,
    query: String,
    vehicle: UserVehicle?,
    resultCount: Int,
  ) {
    val clean = query.trim()
    if (clean.length < 2) return
    val vehicleSummary = vehicle?.let { "${it.shortTitle()} (${it.marketCode})" } ?: "Global Catalog"
    dao.deleteDuplicateSearch(userId, clean)
    dao.insertSearch(
      LocalSearchHistoryEntity(
        userId = userId,
        queryText = clean.take(100),
        vehicleSummary = vehicleSummary,
        resultCount = resultCount,
        searchedAtEpochMs = System.currentTimeMillis(),
      )
    )
  }

  suspend fun recordViewedPart(
    userId: String,
    part: CatalogPart,
    vehicle: UserVehicle?,
    aiReviewSummary: String? = null,
  ) {
    val vehicleSummary = vehicle?.let { "${it.shortTitle()} (${it.marketCode})" } ?: "Global Catalog"
    val partKey = "$userId:${part.id}:${vehicle?.id ?: "default"}"
    dao.upsertViewedPart(
      LocalViewedPartEntity(
        partKey = partKey,
        userId = userId,
        partId = part.id,
        partName = part.name,
        oemNumber = part.oemNumber,
        aftermarketNumber = part.aftermarketNumber,
        systemName = part.system.displayName,
        vehicleSummary = vehicleSummary,
        aiVerificationSummary = aiReviewSummary,
        viewedAtEpochMs = System.currentTimeMillis(),
      )
    )
  }

  suspend fun attachAiVerificationReview(
    userId: String,
    part: CatalogPart,
    vehicle: UserVehicle?,
    reviewSummary: String,
  ) {
    val partKey = "$userId:${part.id}:${vehicle?.id ?: "default"}"
    dao.updateViewedPartAiReview(
      userId = userId,
      partKey = partKey,
      summary = reviewSummary,
      updatedAt = System.currentTimeMillis(),
    )
    // Ensure row exists even if update ran before view upsert
    recordViewedPart(userId, part, vehicle, reviewSummary)
  }

  suspend fun recordSelectedVehicle(userId: String, vehicle: UserVehicle) {
    val key = "$userId:${vehicle.marketCode}:${vehicle.brand}:${vehicle.model}:${vehicle.year}:${vehicle.variant}"
    dao.upsertSelectedVehicle(
      LocalSelectedVehicleEntity(
        vehicleKey = key,
        userId = userId,
        country = vehicle.country,
        marketCode = vehicle.marketCode,
        brand = vehicle.brand,
        model = vehicle.model,
        year = vehicle.year,
        variant = vehicle.variant,
        odometerKm = vehicle.odometerKm,
        selectedAtEpochMs = System.currentTimeMillis(),
      )
    )
  }

  suspend fun clearHistory(userId: String) {
    dao.clearSearchHistory(userId)
    dao.clearViewedParts(userId)
  }
}
