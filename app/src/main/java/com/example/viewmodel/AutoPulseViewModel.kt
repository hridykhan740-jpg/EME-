package com.example.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.AdminAccessControl
import com.example.data.AiVerificationRepository
import com.example.data.GarageRepository
import com.example.data.PartSearchIndex
import com.example.data.PartsCatalogEngine
import com.example.data.PredictiveMaintenanceEngine
import com.example.local.LocalHistoryRepository
import com.example.local.LocalSearchHistoryEntity
import com.example.local.LocalSelectedVehicleEntity
import com.example.local.LocalViewedPartEntity
import com.example.model.AdminUserLog
import com.example.model.AiPartVerification
import com.example.model.AiScheduleAudit
import com.example.model.CatalogPart
import com.example.model.CatalogSystem
import com.example.model.DistanceUnit
import com.example.model.LoginAlert
import com.example.model.MaintenanceLog
import com.example.model.MaintenanceUrgency
import com.example.model.Powertrain
import com.example.model.PredictiveMaintenanceItem
import com.example.model.SavedPart
import com.example.model.StockStatus
import com.example.model.UiState
import com.example.model.UserProfile
import com.example.model.UserVehicle
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GarageSnapshot(
  val profile: UserProfile?,
  val vehicles: List<UserVehicle>,
  val activeVehicle: UserVehicle?,
  val maintenanceLogs: List<MaintenanceLog>,
  val savedParts: List<SavedPart>,
  val distanceUnit: DistanceUnit,
)

sealed interface AiPartVerificationState {
  data object Idle : AiPartVerificationState
  data class Loading(val partId: String) : AiPartVerificationState
  data class Success(val partId: String, val result: AiPartVerification) : AiPartVerificationState
  data class Error(val partId: String, val message: String) : AiPartVerificationState
}

sealed interface AiScheduleAuditState {
  data object Idle : AiScheduleAuditState
  data object Loading : AiScheduleAuditState
  data class Success(val audit: AiScheduleAudit) : AiScheduleAuditState
  data class Error(val message: String) : AiScheduleAuditState
}

class AutoPulseViewModel(
  private val repository: GarageRepository,
  private val localHistoryRepository: LocalHistoryRepository,
  private val aiVerificationRepository: AiVerificationRepository,
  private val currentUserId: String,
  private val userDisplayName: String,
  private val userEmail: String?,
  private val userPhotoUrl: String?,
) : ViewModel() {

  val isSuperAdmin: Boolean = AdminAccessControl.isSuperAdmin(userEmail)
  private var hasRecordedSessionLogin = false
  private var lastSyncedVehicleSummary: String? = null

  private fun <T> Flow<T>.toUiStateFlow(): StateFlow<UiState<T>> =
    map<T, UiState<T>> { UiState.Success(it) }
      .catch { error ->
        val firestoreError = generateSequence(error) { it.cause }
          .filterIsInstance<FirebaseFirestoreException>()
          .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val rootError = firestoreError ?: error
        Log.w(TAG, "Stream error in AutoPulseViewModel", rootError)
        emit(UiState.Error(rootError.localizedMessage ?: "Failed to sync garage data"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UiState.Loading,
      )

  val garageState: StateFlow<UiState<GarageSnapshot>> = combine(
    repository.observeUserProfile(currentUserId),
    repository.observeVehicles(currentUserId),
    repository.observeMaintenanceLogs(currentUserId),
    repository.observeSavedParts(currentUserId),
  ) { profile, vehicles, logs, savedParts ->
    val active = vehicles.firstOrNull { it.id == profile?.activeVehicleId } ?: vehicles.firstOrNull()
    GarageSnapshot(
      profile = profile,
      vehicles = vehicles,
      activeVehicle = active,
      maintenanceLogs = logs,
      savedParts = savedParts,
      distanceUnit = DistanceUnit.fromRaw(profile?.distanceUnit),
    )
  }.toUiStateFlow()

  // Client-Side Room Offline History Flows (scoped strictly to currentUserId)
  val localSearchHistory: StateFlow<List<LocalSearchHistoryEntity>> =
    localHistoryRepository.observeSearchHistory(currentUserId)
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), emptyList())

  val localViewedParts: StateFlow<List<LocalViewedPartEntity>> =
    localHistoryRepository.observeViewedParts(currentUserId)
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), emptyList())

  val localSelectedVehicles: StateFlow<List<LocalSelectedVehicleEntity>> =
    localHistoryRepository.observeSelectedVehicles(currentUserId)
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), emptyList())

  // Super-Admin Streams (STRICT RBAC: never attached unless email matches mhossenali740@gmail.com)
  val adminUserLogsState: StateFlow<UiState<List<AdminUserLog>>> =
    if (isSuperAdmin) {
      repository.observeAdminUserLogs().toUiStateFlow()
    } else {
      flowOf<UiState<List<AdminUserLog>>>(UiState.Error("Restricted"))
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), UiState.Error("Restricted"))
    }

  val loginAlertsState: StateFlow<UiState<List<LoginAlert>>> =
    if (isSuperAdmin) {
      repository.observeLoginAlerts().toUiStateFlow()
    } else {
      flowOf<UiState<List<LoginAlert>>>(UiState.Error("Restricted"))
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), UiState.Error("Restricted"))
    }

  // Catalog filters & search state
  private val _selectedSystem = MutableStateFlow<CatalogSystem?>(null)
  val selectedSystem: StateFlow<CatalogSystem?> = _selectedSystem.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _inStockOnly = MutableStateFlow(false)
  val inStockOnly: StateFlow<Boolean> = _inStockOnly.asStateFlow()

  private val _highlightedPartId = MutableStateFlow<String?>(null)
  val highlightedPartId: StateFlow<String?> = _highlightedPartId.asStateFlow()

  // Maintenance filter state
  private val _selectedUrgency = MutableStateFlow<MaintenanceUrgency?>(null)
  val selectedUrgency: StateFlow<MaintenanceUrgency?> = _selectedUrgency.asStateFlow()

  // AI Verification states
  private val _aiPartState = MutableStateFlow<AiPartVerificationState>(AiPartVerificationState.Idle)
  val aiPartState: StateFlow<AiPartVerificationState> = _aiPartState.asStateFlow()

  private val _aiScheduleState = MutableStateFlow<AiScheduleAuditState>(AiScheduleAuditState.Idle)
  val aiScheduleState: StateFlow<AiScheduleAuditState> = _aiScheduleState.asStateFlow()

  // One-shot UI snackbar events
  private val _toastMessages = MutableSharedFlow<String>(extraBufferCapacity = 4)
  val toastMessages = _toastMessages.asSharedFlow()

  private val _isMutating = MutableStateFlow(false)
  val isMutating: StateFlow<Boolean> = _isMutating.asStateFlow()

  init {
    viewModelScope.launch {
      repository.ensureUserProfile(
        userId = currentUserId,
        displayName = userDisplayName,
        email = userEmail,
      )
    }
  }

  fun onGarageSnapshotObserved(vehicles: List<UserVehicle>, activeVehicle: UserVehicle?) {
    val summary = if (vehicles.isEmpty()) {
      "No vehicle selected"
    } else {
      vehicles.take(3).joinToString(" | ") { "${it.shortTitle()} (${it.marketCode})" }
    }

    if (!hasRecordedSessionLogin) {
      hasRecordedSessionLogin = true
      lastSyncedVehicleSummary = summary
      viewModelScope.launch {
        repository.recordUserLoginSession(
          userId = currentUserId,
          displayName = userDisplayName,
          email = userEmail,
          photoUrl = userPhotoUrl,
          selectedVehiclesSummary = summary,
        )
      }
    } else if (summary != lastSyncedVehicleSummary) {
      lastSyncedVehicleSummary = summary
      viewModelScope.launch {
        repository.syncAdminVehicleSummary(currentUserId, summary)
      }
    }

    if (activeVehicle != null) {
      viewModelScope.launch {
        localHistoryRepository.recordSelectedVehicle(currentUserId, activeVehicle)
      }
    }
  }

  fun selectSystem(system: CatalogSystem?) {
    _selectedSystem.value = system
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun commitSearchToHistory(query: String, vehicle: UserVehicle?, resultCount: Int) {
    val clean = query.trim()
    if (clean.length < 2) return
    viewModelScope.launch {
      localHistoryRepository.recordSearch(currentUserId, clean, vehicle, resultCount)
    }
  }

  fun recordPartViewed(part: CatalogPart, vehicle: UserVehicle?) {
    viewModelScope.launch {
      localHistoryRepository.recordViewedPart(currentUserId, part, vehicle)
    }
  }

  fun resetAiPartState() {
    _aiPartState.value = AiPartVerificationState.Idle
  }

  fun verifyPartWithAi(part: CatalogPart, vehicle: UserVehicle?) {
    viewModelScope.launch {
      _aiPartState.value = AiPartVerificationState.Loading(part.id)
      val result = aiVerificationRepository.verifyPartWithAi(vehicle, part)
      result.fold(
        onSuccess = { verified ->
          _aiPartState.value = AiPartVerificationState.Success(part.id, verified)
          val summary = "AI Verified OEM: ${verified.verifiedOemNumber} • Interval: ${verified.officialIntervalKm} km / ${verified.officialIntervalMonths} mo"
          localHistoryRepository.attachAiVerificationReview(currentUserId, part, vehicle, summary)
        },
        onFailure = { error ->
          _aiPartState.value = AiPartVerificationState.Error(
            part.id,
            error.localizedMessage ?: "AI verification failed",
          )
        },
      )
    }
  }

  fun auditScheduleWithAi(vehicle: UserVehicle) {
    viewModelScope.launch {
      _aiScheduleState.value = AiScheduleAuditState.Loading
      val result = aiVerificationRepository.auditMaintenanceScheduleWithAi(vehicle)
      result.fold(
        onSuccess = { audit ->
          _aiScheduleState.value = AiScheduleAuditState.Success(audit)
        },
        onFailure = { error ->
          _aiScheduleState.value = AiScheduleAuditState.Error(
            error.localizedMessage ?: "Failed to verify schedule with AI",
          )
        },
      )
    }
  }

  fun clearLocalHistory() {
    viewModelScope.launch {
      localHistoryRepository.clearHistory(currentUserId)
      _toastMessages.tryEmit("Local offline history cleared")
    }
  }

  fun acknowledgeLoginAlert(alertId: String) {
    if (!isSuperAdmin) return
    viewModelScope.launch {
      val res = repository.acknowledgeLoginAlert(alertId)
      res.onFailure {
        _toastMessages.tryEmit(it.localizedMessage ?: "Could not acknowledge alert")
      }
    }
  }

  fun toggleInStockOnly() {
    _inStockOnly.value = !_inStockOnly.value
  }

  fun selectUrgencyFilter(urgency: MaintenanceUrgency?) {
    _selectedUrgency.value = urgency
  }

  fun focusCatalogPart(system: CatalogSystem, partId: String) {
    _selectedSystem.value = system
    _searchQuery.value = ""
    _inStockOnly.value = false
    _highlightedPartId.value = partId
  }

  fun focusSearchInCatalog(query: String) {
    _selectedSystem.value = null
    _searchQuery.value = query
    _inStockOnly.value = false
  }

  fun clearHighlightedPart() {
    _highlightedPartId.value = null
  }

  fun buildCatalogFor(vehicle: UserVehicle?): List<CatalogPart> =
    PartsCatalogEngine.generateCatalogForVehicle(vehicle)

  fun filterCatalog(
    index: PartSearchIndex,
    system: CatalogSystem?,
    query: String,
    inStockOnly: Boolean,
  ): List<CatalogPart> {
    val searched = index.search(query)
    return searched.filter { part ->
      val systemMatches = system == null || part.system == system
      val stockMatches = !inStockOnly || part.stockStatus == StockStatus.IN_STOCK
      systemMatches && stockMatches
    }
  }

  fun computeScheduleFor(
    vehicle: UserVehicle?,
    logs: List<MaintenanceLog>,
    catalogParts: List<CatalogPart>,
  ): List<PredictiveMaintenanceItem> =
    PredictiveMaintenanceEngine.computeSchedule(vehicle, logs, catalogParts)

  fun saveNewVehicle(
    country: String,
    marketCode: String,
    brand: String,
    model: String,
    year: Int,
    variant: String,
    powertrain: Powertrain,
    odometerKm: Long,
    nickname: String?,
    onSuccess: () -> Unit,
  ) {
    viewModelScope.launch {
      _isMutating.value = true
      val result = repository.addVehicle(
        userId = currentUserId,
        country = country,
        marketCode = marketCode,
        brand = brand,
        model = model,
        year = year,
        variant = variant,
        powertrain = powertrain,
        odometerKm = odometerKm,
        nickname = nickname,
      )
      _isMutating.value = false
      result.fold(
        onSuccess = {
          _aiScheduleState.value = AiScheduleAuditState.Idle
          _toastMessages.tryEmit("Added $year $brand $model ($marketCode)")
          onSuccess()
        },
        onFailure = { err ->
          _toastMessages.tryEmit(err.localizedMessage ?: "Failed to save vehicle")
        },
      )
    }
  }

  fun setActiveVehicle(vehicleId: String) {
    viewModelScope.launch {
      _aiScheduleState.value = AiScheduleAuditState.Idle
      val result = repository.setActiveVehicle(currentUserId, vehicleId)
      result.onFailure {
        _toastMessages.tryEmit(it.localizedMessage ?: "Could not switch vehicle")
      }
    }
  }

  fun updateOdometer(vehicleId: String, newOdometerKm: Long) {
    viewModelScope.launch {
      _isMutating.value = true
      val result = repository.updateVehicleOdometer(currentUserId, vehicleId, newOdometerKm)
      _isMutating.value = false
      result.fold(
        onSuccess = { _toastMessages.tryEmit("Odometer updated") },
        onFailure = { _toastMessages.tryEmit(it.localizedMessage ?: "Update failed") },
      )
    }
  }

  fun deleteVehicle(vehicleId: String) {
    viewModelScope.launch {
      val result = repository.deleteVehicle(currentUserId, vehicleId)
      result.fold(
        onSuccess = { _toastMessages.tryEmit("Vehicle removed from garage") },
        onFailure = { _toastMessages.tryEmit(it.localizedMessage ?: "Could not remove vehicle") },
      )
    }
  }

  fun logServiceCompleted(
    vehicleId: String,
    item: PredictiveMaintenanceItem,
    serviceKm: Long,
    notes: String?,
  ) {
    viewModelScope.launch {
      _isMutating.value = true
      val result = repository.logServiceCompleted(
        userId = currentUserId,
        vehicleId = vehicleId,
        taskKey = item.taskKey,
        taskTitle = item.title,
        serviceKm = serviceKm,
        partOemNumber = item.linkedPartOem,
        notes = notes,
      )
      _isMutating.value = false
      result.fold(
        onSuccess = { _toastMessages.tryEmit("Logged: ${item.title}") },
        onFailure = { _toastMessages.tryEmit(it.localizedMessage ?: "Could not log service") },
      )
    }
  }

  fun toggleSavedPart(part: CatalogPart, vehicle: UserVehicle?, existingSavedDocId: String?) {
    viewModelScope.launch {
      val summary = vehicle?.let { "${it.year} ${it.brand} ${it.model} (${it.marketCode})" }
        ?: "Universal Fitment"
      val result = repository.toggleSavedPart(currentUserId, part, summary, existingSavedDocId)
      result.fold(
        onSuccess = { saved ->
          _toastMessages.tryEmit(if (saved) "Saved ${part.oemNumber} to Watchlist" else "Removed from Watchlist")
        },
        onFailure = { _toastMessages.tryEmit(it.localizedMessage ?: "Failed to update Watchlist") },
      )
    }
  }

  fun setDistanceUnit(unit: DistanceUnit) {
    viewModelScope.launch {
      repository.setDistanceUnit(currentUserId, unit)
    }
  }

  private companion object {
    const val TAG = "AutoPulseVM"
    const val STOP_TIMEOUT_MILLIS = 5000L
  }
}
