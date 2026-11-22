package com.example

import com.example.auth.AdminAccessControl
import com.example.data.PartSearchIndex
import com.example.data.PartsCatalogEngine
import com.example.data.PredictiveMaintenanceEngine
import com.example.data.VehicleCatalogData
import com.example.model.CatalogSystem
import com.example.model.MaintenanceLog
import com.example.model.MaintenanceUrgency
import com.example.model.Powertrain
import com.example.model.UserVehicle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PartsAndMaintenanceEngineTest {

  private val sampleAxioBd = UserVehicle(
    id = "veh_bd_axio",
    userId = "user_1",
    country = "Bangladesh",
    marketCode = "BD",
    brand = "Toyota",
    model = "Corolla Axio",
    year = 2021,
    variant = "1.5L Hybrid G / EX eCVT (NKE165 1NZ-FXE)",
    powertrain = Powertrain.HYBRID.name,
    odometerKm = 39_500L,
  )

  private val sampleEv = UserVehicle(
    id = "veh_2",
    userId = "user_1",
    country = "ASEAN (TH / ID / MY)",
    marketCode = "ASEAN",
    brand = "BYD",
    model = "Atto 3 / Seal",
    year = 2024,
    variant = "60.48 kWh Blade LFP FWD",
    powertrain = Powertrain.EV.name,
    odometerKm = 18_000L,
  )

  @Test
  fun bangladeshMarketIncludesAllPopularModelsAndOemCodes() {
    assertEquals("BD", VehicleCatalogData.markets.first().marketCode)

    val bdModels = VehicleCatalogData.models
      .filter { it.marketCodes.contains("BD") }
      .map { it.model }

    val expectedModels = listOf(
      "Corolla Axio",
      "Corolla Fielder",
      "Premio",
      "Allion",
      "Noah",
      "HiAce",
      "Land Cruiser Prado",
      "Vezel / HR-V",
      "Grace",
      "Outlander",
      "Tucson",
    )
    expectedModels.forEach { expected ->
      assertTrue("Missing BD model: $expected", bdModels.any { it.contains(expected) })
    }

    val axioParts = PartsCatalogEngine.generateCatalogForVehicle(sampleAxioBd)
    val coveredSystems = axioParts.map { it.system }.toSet()
    assertEquals(CatalogSystem.entries.toSet(), coveredSystems)

    val index = PartSearchIndex(axioParts)
    val axioOilFilterHits = index.search("90915-YZZF2")
    assertTrue(axioOilFilterHits.any { it.id == "eng_oil_filter" })
  }

  @Test
  fun evCatalogExcludesPetrolSparkPlugsAndIncludesDielectricCoolant() {
    val evParts = PartsCatalogEngine.generateCatalogForVehicle(sampleEv)
    assertFalse(evParts.any { it.id == "eng_spark_or_glow" })
    assertTrue(evParts.any { it.id == "eng_ev_drive_coolant" })
  }

  @Test
  fun predictiveMaintenanceRecalculatesAfterServiceLogWithKmAndMonths() {
    val parts = PartsCatalogEngine.generateCatalogForVehicle(sampleAxioBd)
    val initialSchedule = PredictiveMaintenanceEngine.computeSchedule(
      vehicle = sampleAxioBd,
      logs = emptyList(),
      catalogParts = parts,
    )
    assertTrue(initialSchedule.isNotEmpty())
    assertTrue(initialSchedule.all { it.intervalKm > 0 && it.intervalMonths > 0 })

    val oilBefore = initialSchedule.first { it.taskKey == "engine_oil_filter" }
    assertEquals(8500, oilBefore.intervalKm) // BD severe market factor applied
    assertEquals(12, oilBefore.intervalMonths)

    val logs = listOf(
      MaintenanceLog(
        id = "log_1",
        userId = "user_1",
        vehicleId = sampleAxioBd.id,
        taskKey = "engine_oil_filter",
        taskTitle = oilBefore.title,
        serviceKm = 39_500L,
        serviceEpochMs = 1_700_000_000_000L,
      )
    )
    val updatedSchedule = PredictiveMaintenanceEngine.computeSchedule(
      vehicle = sampleAxioBd,
      logs = logs,
      catalogParts = parts,
    )
    val oilAfter = updatedSchedule.first { it.taskKey == "engine_oil_filter" }
    assertEquals(MaintenanceUrgency.HEALTHY, oilAfter.urgency)
    assertEquals(100, oilAfter.lifeRemainingPercent)
    assertEquals(39_500L + oilAfter.intervalKm, oilAfter.nextDueKm)
  }

  @Test
  fun superAdminAccessControlStrictlyAllowsOnlyMhossenali740Gmail() {
    assertTrue(AdminAccessControl.isSuperAdmin("mhossenali740@gmail.com"))
    assertTrue(AdminAccessControl.isSuperAdmin("  MhossenAli740@GMAIL.com "))
    assertFalse(AdminAccessControl.isSuperAdmin("other@gmail.com"))
    assertFalse(AdminAccessControl.isSuperAdmin("mhossenali740@yahoo.com"))
    assertFalse(AdminAccessControl.isSuperAdmin(null))
    assertFalse(AdminAccessControl.isSuperAdmin(""))
  }
}
