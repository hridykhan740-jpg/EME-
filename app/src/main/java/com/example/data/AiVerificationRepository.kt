package com.example.data

import android.util.Log
import com.example.model.AiPartVerification
import com.example.model.AiScheduleAudit
import com.example.model.AiScheduleAuditItem
import com.example.model.CatalogPart
import com.example.model.UserVehicle
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

class AiVerificationRepository {

  private val partVerificationSchema = Schema.obj(
    properties = mapOf(
      "verifiedOemNumber" to Schema.string(description = "Official manufacturer OEM part number for the chassis/model"),
      "aftermarketEquivalents" to Schema.string(description = "Verified Denso, Aisin, Akebono, NGK, Bosch, or Mann equivalents"),
      "officialIntervalKm" to Schema.integer(description = "Recommended replacement interval in kilometers"),
      "officialIntervalMonths" to Schema.integer(description = "Recommended replacement interval in months"),
      "fitmentSummary" to Schema.string(description = "Concise fitment notes for the vehicle market and engine code"),
      "manufacturerSourceNote" to Schema.string(description = "Official manufacturer manual/EPC basis"),
    )
  )

  private val scheduleAuditSchema = Schema.obj(
    properties = mapOf(
      "sourceManualSummary" to Schema.string(description = "Summary of official brand maintenance schedule for this model and market"),
      "severeClimateNote" to Schema.string(description = "Market-specific operating guidance such as Dhaka/BD stop-and-go traffic, humidity, or dust"),
      "items" to Schema.array(
        items = Schema.obj(
          properties = mapOf(
            "taskKey" to Schema.string(description = "Task identifier such as engine_oil_filter, cabin_filter, coolant_flush, brake_pads_rotors, spark_plugs, transmission_fluid"),
            "partOrFluid" to Schema.string(description = "Part or fluid name"),
            "officialIntervalKm" to Schema.integer(description = "Interval in kilometers"),
            "officialIntervalMonths" to Schema.integer(description = "Interval in months"),
            "oemSpec" to Schema.string(description = "Exact viscosity, fluid spec, or OEM part code"),
          )
        ),
        description = "List of official replacement intervals in both km and months",
      ),
    )
  )

  private val partModel by lazy {
    Firebase.ai.generativeModel(
      modelName = MODEL_NAME,
      generationConfig = generationConfig {
        responseMimeType = "application/json"
        responseSchema = partVerificationSchema
      },
    )
  }

  private val scheduleModel by lazy {
    Firebase.ai.generativeModel(
      modelName = MODEL_NAME,
      generationConfig = generationConfig {
        responseMimeType = "application/json"
        responseSchema = scheduleAuditSchema
      },
    )
  }

  suspend fun verifyPartWithAi(
    vehicle: UserVehicle?,
    part: CatalogPart,
  ): Result<AiPartVerification> {
    return try {
      val vehicleContext = if (vehicle != null) {
        "${vehicle.year} ${vehicle.brand} ${vehicle.model}, Variant: ${vehicle.variant}, Market: ${vehicle.country} (${vehicle.marketCode}), Powertrain: ${vehicle.powertrain}"
      } else {
        "Toyota Corolla Axio (NKE165/NZE161), Market: Bangladesh (BD)"
      }
      val prompt = """
        You are an automotive OEM parts engineer verifying official manufacturer part numbers and replacement schedules.
        Vehicle: $vehicleContext
        Catalog Part: ${part.name} (System: ${part.system.displayName} / ${part.subSystem})
        Current Catalog OEM #: ${part.oemNumber} (${part.oemManufacturer})
        Current Aftermarket #: ${part.aftermarketNumber}
        
        Verify the exact official OEM part number, top tier-1 aftermarket equivalents (Denso/Aisin/Akebono/NGK/Mann/KYB), and the official replacement interval in both kilometers and months for this vehicle and market (including severe Dhaka/Bangladesh or tropical traffic conditions if applicable).
      """.trimIndent()

      val response = withTimeout(TIMEOUT_MS) {
        partModel.generateContent(prompt)
      }
      val rawJson = response.text ?: throw IllegalStateException("AI verification returned an empty response.")
      val json = JSONObject(rawJson)
      Result.success(
        AiPartVerification(
          verifiedOemNumber = json.optString("verifiedOemNumber", part.oemNumber),
          aftermarketEquivalents = json.optString("aftermarketEquivalents", part.aftermarketNumber),
          officialIntervalKm = json.optInt("officialIntervalKm", 10_000).coerceAtLeast(1000),
          officialIntervalMonths = json.optInt("officialIntervalMonths", 12).coerceAtLeast(1),
          fitmentSummary = json.optString("fitmentSummary", part.fitmentNotes),
          manufacturerSourceNote = json.optString("manufacturerSourceNote", "Manufacturer OEM EPC Spec"),
        )
      )
    } catch (e: TimeoutCancellationException) {
      Log.e(TAG, "Part AI verification timed out", e)
      Result.failure(IllegalStateException("AI verification timed out after 60 seconds. Please retry."))
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Log.e(TAG, "Part AI verification failed", e)
      Result.failure(e)
    }
  }

  suspend fun auditMaintenanceScheduleWithAi(
    vehicle: UserVehicle,
  ): Result<AiScheduleAudit> {
    return try {
      val prompt = """
        You are an automotive maintenance engineer citing official owner's manual schedules.
        Vehicle: ${vehicle.year} ${vehicle.brand} ${vehicle.model}
        Variant / Chassis: ${vehicle.variant}
        Market: ${vehicle.country} (${vehicle.marketCode})
        Powertrain: ${vehicle.powertrain}
        Current Odometer: ${vehicle.odometerKm} km

        Provide verified official service & replacement intervals (in BOTH kilometers and months) for:
        engine_oil_filter, engine_air_filter, cabin_filter, coolant_flush, brake_pads_rotors, spark_plugs, transmission_fluid, suspension_inspection.
        Include exact OEM fluid viscosity or part code in oemSpec, and severe-driving adjustments for ${vehicle.country} if relevant.
      """.trimIndent()

      val response = withTimeout(TIMEOUT_MS) {
        scheduleModel.generateContent(prompt)
      }
      val rawJson = response.text ?: throw IllegalStateException("AI schedule audit returned an empty response.")
      val json = JSONObject(rawJson)
      val arr = json.optJSONArray("items")
      val items = buildList {
        if (arr != null) {
          for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            add(
              AiScheduleAuditItem(
                taskKey = obj.optString("taskKey", "item_$i"),
                partOrFluid = obj.optString("partOrFluid", "Service Item"),
                officialIntervalKm = obj.optInt("officialIntervalKm", 10_000).coerceAtLeast(1000),
                officialIntervalMonths = obj.optInt("officialIntervalMonths", 12).coerceAtLeast(1),
                oemSpec = obj.optString("oemSpec", "OEM Genuine Spec"),
              )
            )
          }
        }
      }
      Result.success(
        AiScheduleAudit(
          vehicleLabel = "${vehicle.shortTitle()} (${vehicle.marketCode})",
          sourceManualSummary = json.optString("sourceManualSummary", "Official Manufacturer Service Schedule"),
          severeClimateNote = json.optString("severeClimateNote", ""),
          items = items,
        )
      )
    } catch (e: TimeoutCancellationException) {
      Log.e(TAG, "Schedule AI audit timed out", e)
      Result.failure(IllegalStateException("AI schedule verification timed out after 60 seconds."))
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Log.e(TAG, "Schedule AI audit failed", e)
      Result.failure(e)
    }
  }

  private companion object {
    const val TAG = "AiVerificationRepo"
    const val MODEL_NAME = "gemini-3.8-flash"
    const val TIMEOUT_MS = 60_000L
  }
}
