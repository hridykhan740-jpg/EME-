package com.example.data

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.example.R
import com.example.auth.AdminAccessControl
import com.example.model.AdminUserLog
import com.example.model.CatalogPart
import com.example.model.DistanceUnit
import com.example.model.LoginAlert
import com.example.model.MaintenanceLog
import com.example.model.Powertrain
import com.example.model.SavedPart
import com.example.model.UserProfile
import com.example.model.UserVehicle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GarageRepository(
  private val db: FirebaseFirestore,
  private val auth: FirebaseAuth = Firebase.auth,
) {
  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  private val httpClient by lazy {
    OkHttpClient.Builder()
      .connectTimeout(10, TimeUnit.SECONDS)
      .readTimeout(10, TimeUnit.SECONDS)
      .build()
  }

  private fun requireUserId(expectedUserId: String): String {
    val uid = auth.currentUser?.uid
      ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    check(uid == expectedUserId) { "Signed-in user changed; refusing cross-user Firestore operation." }
    return uid
  }

  private fun requireSuperAdmin(): String {
    val user = auth.currentUser
      ?: throw IllegalStateException("Authentication required.")
    check(AdminAccessControl.isSuperAdmin(user.email)) {
      "Restricted: Super-Admin privileges required."
    }
    return user.uid
  }

  fun observeUserProfile(userId: String): Flow<UserProfile?> = flow {
    val uid = requireUserId(userId)
    val path = "users/$uid"
    emitAll(
      db.collection("users").document(uid)
        .snapshots()
        .map { snapshot ->
          if (snapshot.exists()) {
            snapshot.toObject(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
          } else {
            null
          }
        }
        .catch { error ->
          logAndRethrowStreamError(error, OperationType.GET, path, uid)
        }
    )
  }

  fun observeVehicles(userId: String): Flow<List<UserVehicle>> = flow {
    val uid = requireUserId(userId)
    val path = "users/$uid/vehicles"
    emitAll(
      db.collection("users").document(uid).collection("vehicles")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(UserVehicle::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            .sortedByDescending { it.updatedAt?.seconds ?: it.createdAt?.seconds ?: 0L }
        }
        .catch { error ->
          logAndRethrowStreamError(error, OperationType.LIST, path, uid)
        }
    )
  }

  fun observeMaintenanceLogs(userId: String): Flow<List<MaintenanceLog>> = flow {
    val uid = requireUserId(userId)
    val path = "users/$uid/maintenanceLogs"
    emitAll(
      db.collection("users").document(uid).collection("maintenanceLogs")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(MaintenanceLog::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            .sortedByDescending { it.serviceEpochMs }
        }
        .catch { error ->
          logAndRethrowStreamError(error, OperationType.LIST, path, uid)
        }
    )
  }

  fun observeSavedParts(userId: String): Flow<List<SavedPart>> = flow {
    val uid = requireUserId(userId)
    val path = "users/$uid/savedParts"
    emitAll(
      db.collection("users").document(uid).collection("savedParts")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(SavedPart::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            .sortedByDescending { it.createdAt?.seconds ?: 0L }
        }
        .catch { error ->
          logAndRethrowStreamError(error, OperationType.LIST, path, uid)
        }
    )
  }

  // Strictly gated to mhossenali740@gmail.com; never emits or listens for normal users
  fun observeAdminUserLogs(): Flow<List<AdminUserLog>> = flow {
    val adminUid = requireSuperAdmin()
    val path = "adminUserLogs"
    emitAll(
      db.collection("adminUserLogs")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(AdminUserLog::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            .sortedByDescending { it.lastLoginEpochMs }
        }
        .catch { error ->
          logAndRethrowStreamError(error, OperationType.LIST, path, adminUid)
        }
    )
  }

  fun observeLoginAlerts(): Flow<List<LoginAlert>> = flow {
    val adminUid = requireSuperAdmin()
    val path = "loginAlerts"
    emitAll(
      db.collection("loginAlerts")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(LoginAlert::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            .sortedByDescending { it.loginEpochMs }
        }
        .catch { error ->
          logAndRethrowStreamError(error, OperationType.LIST, path, adminUid)
        }
    )
  }

  private fun logAndRethrowStreamError(
    error: Throwable,
    operationType: OperationType,
    path: String,
    expectedUid: String,
  ): Nothing {
    val firestoreError = generateSequence(error) { it.cause }
      .filterIsInstance<FirebaseFirestoreException>()
      .firstOrNull()
    if (firestoreError == null && error is CancellationException) throw error
    val exceptionToLog = firestoreError ?: (error as? Exception)
    if (exceptionToLog != null && auth.currentUser?.uid == expectedUid) {
      handleFirestoreError(exceptionToLog, operationType, path)
    }
    throw error
  }

  suspend fun ensureUserProfile(
    userId: String,
    displayName: String,
    email: String?,
  ): Result<Unit> {
    val path = "users/$userId"
    return try {
      val uid = requireUserId(userId)
      val docRef = db.collection("users").document(uid)
      val existing = docRef.get().await()
      val safeName = displayName.trim().ifBlank { "Driver" }.take(80)
      val safeEmail = email?.trim()?.takeIf { it.isNotEmpty() }?.take(120)

      if (!existing.exists()) {
        val payload = mapOf(
          "userId" to uid,
          "displayName" to safeName,
          "email" to safeEmail,
          "distanceUnit" to DistanceUnit.KM.name,
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp(),
        ).filterValues { it != null }
        docRef.set(payload).await()
      } else {
        val updates = mutableMapOf<String, Any>(
          "displayName" to safeName,
          "updatedAt" to FieldValue.serverTimestamp(),
        )
        if (safeEmail != null) {
          updates["email"] = safeEmail
        }
        docRef.update(updates).await()
      }
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, path)
      Result.failure(e)
    }
  }

  suspend fun recordUserLoginSession(
    userId: String,
    displayName: String,
    email: String?,
    photoUrl: String?,
    selectedVehiclesSummary: String,
  ): Result<Unit> {
    val path = "adminUserLogs/$userId"
    return try {
      val uid = requireUserId(userId)
      val cleanName = displayName.trim().ifBlank { "Driver" }.take(80)
      val cleanEmail = email?.trim()?.takeIf { it.isNotEmpty() }?.take(120) ?: return Result.success(Unit)
      val cleanPhoto = photoUrl?.trim()?.takeIf { it.isNotEmpty() }?.take(500)
      val cleanSummary = selectedVehiclesSummary.trim().ifBlank { "No vehicle selected" }.take(300)
      val nowMs = System.currentTimeMillis()

      val logRef = db.collection("adminUserLogs").document(uid)
      val existing = logRef.get().await()
      val isFirstTimeUser = !existing.exists()

      if (isFirstTimeUser) {
        val payload = mapOf(
          "userId" to uid,
          "displayName" to cleanName,
          "email" to cleanEmail,
          "photoUrl" to cleanPhoto,
          "lastLoginEpochMs" to nowMs,
          "loginCount" to 1L,
          "selectedVehiclesSummary" to cleanSummary,
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp(),
        ).filterValues { it != null }
        logRef.set(payload).await()
      } else {
        val prevCount = (existing.get("loginCount") as? Number)?.toLong() ?: 0L
        val nextCount = (prevCount + 1L).coerceIn(1L, 1_000_000L)
        val updates = mutableMapOf<String, Any>(
          "displayName" to cleanName,
          "email" to cleanEmail,
          "lastLoginEpochMs" to nowMs,
          "loginCount" to nextCount,
          "selectedVehiclesSummary" to cleanSummary,
          "updatedAt" to FieldValue.serverTimestamp(),
        )
        if (cleanPhoto != null) {
          updates["photoUrl"] = cleanPhoto
        }
        logRef.update(updates).await()
      }

      // Trigger automated alert for Super-Admin (mhossenali740@gmail.com)
      val web3Key = BuildConfig.WEB3FORMS_ACCESS_KEY.trim()
        .takeIf { it.isNotEmpty() && !it.equals("UNCONFIGURED", ignoreCase = true) }
        .orEmpty()
      val webhookDelivered = if (web3Key.isNotEmpty()) {
        dispatchWeb3FormsAlert(
          accessKey = web3Key,
          userName = cleanName,
          userEmail = cleanEmail,
          vehiclesSummary = cleanSummary,
          loginEpochMs = nowMs,
        )
      } else {
        false
      }

      val channel = if (webhookDelivered) "WEB3FORMS_EMAIL_AND_FIRESTORE" else "FIRESTORE_ALERT_QUEUE"
      val alertRef = db.collection("loginAlerts").document()
      val alertPayload = mapOf(
        "userId" to uid,
        "displayName" to cleanName,
        "email" to cleanEmail,
        "targetAdminEmail" to AdminAccessControl.SUPER_ADMIN_EMAIL,
        "deliveryChannel" to channel,
        "loginEpochMs" to nowMs,
        "acknowledged" to false,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp(),
      )
      alertRef.set(alertPayload).await()

      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, path)
      Result.failure(e)
    }
  }

  suspend fun syncAdminVehicleSummary(
    userId: String,
    selectedVehiclesSummary: String,
  ): Result<Unit> {
    val path = "adminUserLogs/$userId"
    return try {
      val uid = requireUserId(userId)
      val logRef = db.collection("adminUserLogs").document(uid)
      val snap = logRef.get().await()
      if (snap.exists()) {
        val cleanSummary = selectedVehiclesSummary.trim().ifBlank { "No vehicle selected" }.take(300)
        logRef.update(
          mapOf(
            "selectedVehiclesSummary" to cleanSummary,
            "updatedAt" to FieldValue.serverTimestamp(),
          )
        ).await()
      }
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, path)
      Result.failure(e)
    }
  }

  suspend fun acknowledgeLoginAlert(alertId: String): Result<Unit> {
    val path = "loginAlerts/$alertId"
    return try {
      requireSuperAdmin()
      require(alertId.isNotBlank()) { "Alert ID required" }
      db.collection("loginAlerts").document(alertId).update(
        mapOf(
          "acknowledged" to true,
          "updatedAt" to FieldValue.serverTimestamp(),
        )
      ).await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, path)
      Result.failure(e)
    }
  }

  private suspend fun dispatchWeb3FormsAlert(
    accessKey: String,
    userName: String,
    userEmail: String,
    vehiclesSummary: String,
    loginEpochMs: Long,
  ): Boolean = withContext(Dispatchers.IO) {
    try {
      val bodyJson = JSONObject().apply {
        put("access_key", accessKey)
        put("subject", "AutoPulse User Sign-In Alert: $userName ($userEmail)")
        put("from_name", "AutoPulse Security Monitor")
        put("email", AdminAccessControl.SUPER_ADMIN_EMAIL)
        put(
          "message",
          "User Signed In:\nName: $userName\nGmail: $userEmail\nSelected Vehicles: $vehiclesSummary\nEpochMs: $loginEpochMs"
        )
      }
      val request = Request.Builder()
        .url("https://api.web3forms.com/submit")
        .post(bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
        .build()
      httpClient.newCall(request).execute().use { response ->
        response.isSuccessful
      }
    } catch (e: Exception) {
      Log.w(TAG, "Web3Forms alert webhook skipped/failed (Firestore alert still recorded)", e)
      false
    }
  }

  suspend fun setActiveVehicle(userId: String, vehicleId: String): Result<Unit> {
    val path = "users/$userId"
    return try {
      val uid = requireUserId(userId)
      require(vehicleId.isNotBlank() && vehicleId.length <= 128) { "Invalid vehicleId" }
      db.collection("users").document(uid).update(
        mapOf(
          "activeVehicleId" to vehicleId,
          "updatedAt" to FieldValue.serverTimestamp(),
        )
      ).await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, path)
      Result.failure(e)
    }
  }

  suspend fun setDistanceUnit(userId: String, unit: DistanceUnit): Result<Unit> {
    val path = "users/$userId"
    return try {
      val uid = requireUserId(userId)
      db.collection("users").document(uid).update(
        mapOf(
          "distanceUnit" to unit.name,
          "updatedAt" to FieldValue.serverTimestamp(),
        )
      ).await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, path)
      Result.failure(e)
    }
  }

  suspend fun addVehicle(
    userId: String,
    country: String,
    marketCode: String,
    brand: String,
    model: String,
    year: Int,
    variant: String,
    powertrain: Powertrain,
    odometerKm: Long,
    nickname: String?,
  ): Result<String> {
    var path = "users/$userId/vehicles"
    return try {
      val uid = requireUserId(userId)
      val cleanCountry = country.trim().take(60)
      val cleanMarket = marketCode.trim().take(20)
      val cleanBrand = brand.trim().take(60)
      val cleanModel = model.trim().take(80)
      val cleanVariant = variant.trim().take(120)
      val cleanOdo = odometerKm.coerceIn(0L, 2_000_000L)
      val cleanYear = year.coerceIn(1980, 2030)
      val cleanNick = nickname?.trim()?.takeIf { it.isNotEmpty() }?.take(60)

      require(cleanCountry.isNotEmpty() && cleanMarket.isNotEmpty()) { "Market required" }
      require(cleanBrand.isNotEmpty() && cleanModel.isNotEmpty() && cleanVariant.isNotEmpty()) {
        "Brand, model, and variant are required"
      }

      val vehiclesCol = db.collection("users").document(uid).collection("vehicles")
      val docRef = vehiclesCol.document()
      path = docRef.path

      val payload = mapOf(
        "userId" to uid,
        "country" to cleanCountry,
        "marketCode" to cleanMarket,
        "brand" to cleanBrand,
        "model" to cleanModel,
        "year" to cleanYear,
        "variant" to cleanVariant,
        "powertrain" to powertrain.name,
        "odometerKm" to cleanOdo,
        "nickname" to cleanNick,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp(),
      ).filterValues { it != null }

      docRef.set(payload).await()

      db.collection("users").document(uid).update(
        mapOf(
          "activeVehicleId" to docRef.id,
          "updatedAt" to FieldValue.serverTimestamp(),
        )
      ).await()

      Result.success(docRef.id)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, path)
      Result.failure(e)
    }
  }

  suspend fun updateVehicleOdometer(
    userId: String,
    vehicleId: String,
    odometerKm: Long,
  ): Result<Unit> {
    val path = "users/$userId/vehicles/$vehicleId"
    return try {
      val uid = requireUserId(userId)
      require(vehicleId.isNotBlank()) { "Vehicle ID required" }
      val cleanOdo = odometerKm.coerceIn(0L, 2_000_000L)
      db.collection("users").document(uid).collection("vehicles").document(vehicleId)
        .update(
          mapOf(
            "odometerKm" to cleanOdo,
            "updatedAt" to FieldValue.serverTimestamp(),
          )
        ).await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, path)
      Result.failure(e)
    }
  }

  suspend fun deleteVehicle(userId: String, vehicleId: String): Result<Unit> {
    val path = "users/$userId/vehicles/$vehicleId"
    return try {
      val uid = requireUserId(userId)
      require(vehicleId.isNotBlank()) { "Vehicle ID required" }
      db.collection("users").document(uid).collection("vehicles").document(vehicleId)
        .delete()
        .await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, path)
      Result.failure(e)
    }
  }

  suspend fun logServiceCompleted(
    userId: String,
    vehicleId: String,
    taskKey: String,
    taskTitle: String,
    serviceKm: Long,
    partOemNumber: String?,
    notes: String?,
  ): Result<Unit> {
    var path = "users/$userId/maintenanceLogs"
    return try {
      val uid = requireUserId(userId)
      require(vehicleId.isNotBlank()) { "Vehicle ID required" }
      val cleanKey = taskKey.trim().take(80)
      val cleanTitle = taskTitle.trim().take(120)
      val cleanKm = serviceKm.coerceIn(0L, 2_000_000L)
      val cleanOem = partOemNumber?.trim()?.takeIf { it.isNotEmpty() }?.take(60)
      val cleanNotes = notes?.trim()?.takeIf { it.isNotEmpty() }?.take(300)

      val docRef = db.collection("users").document(uid).collection("maintenanceLogs").document()
      path = docRef.path

      val payload = mapOf(
        "userId" to uid,
        "vehicleId" to vehicleId,
        "taskKey" to cleanKey,
        "taskTitle" to cleanTitle,
        "serviceKm" to cleanKm,
        "serviceEpochMs" to System.currentTimeMillis(),
        "partOemNumber" to cleanOem,
        "notes" to cleanNotes,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp(),
      ).filterValues { it != null }

      docRef.set(payload).await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, path)
      Result.failure(e)
    }
  }

  suspend fun toggleSavedPart(
    userId: String,
    part: CatalogPart,
    vehicleSummary: String,
    currentlySavedDocId: String?,
  ): Result<Boolean> {
    val sanitizedPartId = part.id.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(128)
    val docId = currentlySavedDocId ?: sanitizedPartId
    val path = "users/$userId/savedParts/$docId"
    return try {
      val uid = requireUserId(userId)
      val docRef = db.collection("users").document(uid).collection("savedParts").document(docId)
      if (currentlySavedDocId != null) {
        docRef.delete().await()
        Result.success(false)
      } else {
        val payload = mapOf(
          "userId" to uid,
          "partId" to sanitizedPartId,
          "oemNumber" to part.oemNumber.take(60),
          "partName" to part.name.take(120),
          "systemName" to part.system.displayName.take(40),
          "vehicleSummary" to vehicleSummary.ifBlank { "Global Fitment" }.take(140),
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp(),
        )
        docRef.set(payload).await()
        Result.success(true)
      }
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, path)
      Result.failure(e)
    }
  }

  private companion object {
    const val TAG = "GarageRepository"
  }
}
