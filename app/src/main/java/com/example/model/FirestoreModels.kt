package com.example.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class DistanceUnit(val label: String) {
  KM("km"),
  MI("mi");

  companion object {
    fun fromRaw(raw: String?): DistanceUnit =
      entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: KM
  }
}

enum class Powertrain(val label: String) {
  PETROL("Petrol ICE"),
  DIESEL("Turbo Diesel"),
  HYBRID("Hybrid HEV"),
  EV("Battery EV");

  companion object {
    fun fromRaw(raw: String?): Powertrain =
      entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: PETROL
  }
}

enum class CatalogSystem(val displayName: String, val shortCode: String) {
  ENGINE("Engine", "ENG"),
  TRANSMISSION("Transmission", "TRN"),
  SUSPENSION("Suspension", "SUS"),
  ELECTRICAL("Electrical", "ELC"),
  BRAKES("Brakes", "BRK"),
  BODY("Body", "BDY");

  companion object {
    fun fromRaw(raw: String?): CatalogSystem =
      entries.firstOrNull {
        it.name.equals(raw, ignoreCase = true) || it.displayName.equals(raw, ignoreCase = true)
      } ?: ENGINE
  }
}

enum class StockStatus(val label: String) {
  IN_STOCK("In Stock"),
  LOW_STOCK("Low Stock"),
  OEM_ORDER("OEM Dispatch"),
  BACKORDER("Backorder")
}

enum class PartVisualKind {
  FILTER_CANISTER,
  BRAKE_ROTOR,
  BRAKE_PAD,
  SPARK_IGNITION,
  STRUT_COIL,
  CONTROL_ARM,
  BATTERY_MODULE,
  SENSOR_ELECTRONIC,
  TRANSMISSION_GEAR,
  FLUID_BOTTLE,
  BELT_TIMING,
  BODY_WIPER
}

enum class MaintenanceUrgency(val label: String) {
  OVERDUE("Overdue"),
  DUE_SOON("Due Soon"),
  HEALTHY("Optimal")
}

@Keep
@IgnoreExtraProperties
data class UserProfile(
  @DocumentId val id: String = "",
  val userId: String = "",
  val displayName: String = "",
  val email: String? = null,
  val activeVehicleId: String? = null,
  val distanceUnit: String = DistanceUnit.KM.name,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@Keep
@IgnoreExtraProperties
data class UserVehicle(
  @DocumentId val id: String = "",
  val userId: String = "",
  val country: String = "",
  val marketCode: String = "",
  val brand: String = "",
  val model: String = "",
  val year: Long = 2024L,
  val variant: String = "",
  val powertrain: String = Powertrain.PETROL.name,
  val odometerKm: Long = 0L,
  val nickname: String? = null,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
) {
  fun shortTitle(): String = "$year $brand $model"
  fun fullSubtitle(): String = "$variant • $marketCode"
}

@Keep
@IgnoreExtraProperties
data class MaintenanceLog(
  @DocumentId val id: String = "",
  val userId: String = "",
  val vehicleId: String = "",
  val taskKey: String = "",
  val taskTitle: String = "",
  val serviceKm: Long = 0L,
  val serviceEpochMs: Long = 0L,
  val partOemNumber: String? = null,
  val notes: String? = null,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@Keep
@IgnoreExtraProperties
data class SavedPart(
  @DocumentId val id: String = "",
  val userId: String = "",
  val partId: String = "",
  val oemNumber: String = "",
  val partName: String = "",
  val systemName: String = "",
  val vehicleSummary: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@Keep
@IgnoreExtraProperties
data class AdminUserLog(
  @DocumentId val id: String = "",
  val userId: String = "",
  val displayName: String = "",
  val email: String = "",
  val photoUrl: String? = null,
  val lastLoginEpochMs: Long = 0L,
  val loginCount: Long = 1L,
  val selectedVehiclesSummary: String = "No vehicle selected",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@Keep
@IgnoreExtraProperties
data class LoginAlert(
  @DocumentId val id: String = "",
  val userId: String = "",
  val displayName: String = "",
  val email: String = "",
  val targetAdminEmail: String = "mhossenali740@gmail.com",
  val deliveryChannel: String = "FIRESTORE_ALERT_QUEUE",
  val loginEpochMs: Long = 0L,
  val acknowledged: Boolean = false,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

data class CatalogPart(
  val id: String,
  val system: CatalogSystem,
  val subSystem: String,
  val name: String,
  val oemNumber: String,
  val oemManufacturer: String,
  val aftermarketNumber: String,
  val aftermarketBrand: String,
  val stockStatus: StockStatus,
  val warehouseQty: Int,
  val priceUsd: Double,
  val dispatchSla: String,
  val specSummary: String,
  val fitmentNotes: String,
  val visualKind: PartVisualKind,
  val linkedTaskKey: String? = null,
  val keywords: List<String> = emptyList(),
)

data class PredictiveMaintenanceItem(
  val taskKey: String,
  val title: String,
  val categoryLabel: String,
  val system: CatalogSystem,
  val intervalKm: Int,
  val intervalMonths: Int,
  val lastServiceKm: Long,
  val lastServiceEpochMs: Long,
  val nextDueKm: Long,
  val remainingKm: Long,
  val lifeRemainingPercent: Int,
  val urgency: MaintenanceUrgency,
  val linkedPartId: String,
  val linkedPartOem: String,
  val recommendationReason: String,
)

data class AiPartVerification(
  val verifiedOemNumber: String,
  val aftermarketEquivalents: String,
  val officialIntervalKm: Int,
  val officialIntervalMonths: Int,
  val fitmentSummary: String,
  val manufacturerSourceNote: String,
  val verifiedAtEpochMs: Long = System.currentTimeMillis(),
)

data class AiScheduleAuditItem(
  val taskKey: String,
  val partOrFluid: String,
  val officialIntervalKm: Int,
  val officialIntervalMonths: Int,
  val oemSpec: String,
)

data class AiScheduleAudit(
  val vehicleLabel: String,
  val sourceManualSummary: String,
  val severeClimateNote: String,
  val items: List<AiScheduleAuditItem>,
  val verifiedAtEpochMs: Long = System.currentTimeMillis(),
)

sealed interface UiState<out T> {
  data object Loading : UiState<Nothing>
  data class Success<T>(val data: T) : UiState<T>
  data class Error(val message: String) : UiState<Nothing>
}
