package com.example

import com.example.auth.AdminAccessControl
import com.example.data.PartSearchIndex
import com.example.data.PartsCatalogEngine
import com.example.data.PredictiveMaintenanceEngine
import com.example.data.VehicleCatalogData
import com.example.data.VinDecoderEngine
import com.example.model.CatalogSystem
import com.example.model.MaintenanceLog
import com.example.model.MaintenanceUrgency
import com.example.model.Powertrain
import com.example.model.UserVehicle
import com.example.model.VehicleCategory
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
  fun bangladeshMarketCoversAllFourVehicleCategoriesAndRequestedModels() {
    assertEquals("BD", VehicleCatalogData.markets.first().marketCode)

    val bdModels = VehicleCatalogData.models.filter { it.marketCodes.contains("BD") }
    val byCat = bdModels.groupBy { it.category }

    assertTrue(byCat[VehicleCategory.PRIVATE_SEDANS_HATCHBACKS].orEmpty().isNotEmpty())
    assertTrue(byCat[VehicleCategory.SUVS_CROSSOVERS_4X4].orEmpty().isNotEmpty())
    assertTrue(byCat[VehicleCategory.MICROBUSES_MPVS_VANS].orEmpty().isNotEmpty())
    assertTrue(byCat[VehicleCategory.COMMERCIAL_TRUCKS_BUSES].orEmpty().isNotEmpty())

    val expectedBdModels = listOf(
      "Corolla Axio", "Corolla Fielder", "Allion", "Premio", "Probox", "Aqua", "Prius",
      "Civic", "Grace", "Fit", "Sunny", "Note", "Lancer EX", "Axela", "Demio", "Swift",
      "Land Cruiser Prado", "Land Cruiser V8 / 300", "Harrier", "Vezel", "HR-V",
      "Outlander", "Pajero", "X-Trail", "Tucson", "Sportage", "ZS", "H6", "Hexa",
      "Noah", "Voxy", "Alphard", "HiAce", "Serena", "Delica",
      "Bolyan", "TRM 180", "TRM 240", "TRM 360",
      "Hilux", "Dyna", "L200", "Fuso Canter", "Ace", "Intra", "Xenon", "LPT 1615",
      "Prima", "Dost", "Ecomet", "Viking", "Bolero Pickup", "Jeeto", "D-Max", "N-Series",
      "AK1J", "RM2", "300 / 500 Series", "Pro 2049",
    )
    val modelNames = bdModels.map { "${it.brand} ${it.model}" }
    expectedBdModels.forEach { expected ->
      assertTrue(
        "Missing Bangladesh vehicle: $expected",
        modelNames.any { it.contains(expected, ignoreCase = true) },
      )
    }

    val pajero = bdModels.first { it.brand == "Mitsubishi" && it.model.contains("Pajero", ignoreCase = true) }
    val pajeroVariants = pajero.variants.joinToString(" ")
    listOf("V93", "V94", "V96", "V98").forEach { code ->
      assertTrue("Missing Pajero chassis $code", pajeroVariants.contains(code, ignoreCase = true))
    }
  }

  @Test
  fun twelveSystemsAndMicroSubComponentsAreGeneratedAndSearchIndexed() {
    val axioParts = PartsCatalogEngine.generateCatalogForVehicle(sampleAxioBd)
    val coveredSystems = axioParts.map { it.system }.toSet()
    assertEquals(CatalogSystem.entries.toSet(), coveredSystems)
    val totalPartsCount = axioParts.size + axioParts.sumOf { it.subComponents.size }
    assertTrue("Expected 1800+ parts per vehicle, got $totalPartsCount", totalPartsCount >= 1800)

    listOf(
      "ful_fuel_filter_micro",
      "elc_alternator",
      "elc_starter_motor",
      "elc_ignition_coils",
      "brk_caliper_kit",
      "clt_flywheel",
      "hvc_ac_pipes",
    ).forEach { id ->
      assertTrue("Missing micro breakdown assembly: $id", axioParts.any { it.id == id && it.subComponents.size == 24 })
    }

    val oilFilterAssembly = axioParts.first { it.id == "eng_oil_filter" }
    assertTrue(oilFilterAssembly.epcCatalogRef.contains("Toyota EPC"))
    assertTrue(oilFilterAssembly.subComponents.size >= 5)

    val subNames = oilFilterAssembly.subComponents.joinToString(" | ") { it.name.lowercase() }
    assertTrue(subNames.contains("element") || subNames.contains("cartridge"))
    assertTrue(subNames.contains("o-ring"))
    assertTrue(subNames.contains("crush washer"))
    assertTrue(subNames.contains("housing cap"))
    assertTrue(subNames.contains("bypass"))

    // Every assembly has micro-level sub-components
    assertTrue(axioParts.all { it.subComponents.isNotEmpty() && it.epcCatalogRef.isNotBlank() })

    val index = PartSearchIndex(axioParts)
    assertTrue(index.search("90915-YZZF2").any { it.id == "eng_oil_filter" })
    // Searching by sub-part OEM or keyword resolves the parent assembly
    assertTrue(index.search("90430-12031").any { it.id == "eng_oil_filter" })
    assertTrue(index.search("crush washer").any { it.id == "eng_oil_filter" })
  }

  @Test
  fun vinDecoderResolvesJdmChassisIsoVinAndCommercialModels() {
    val jdmAxio = VinDecoderEngine.searchByVinOrModel("NKE165-7045123")
    assertTrue(jdmAxio.any { it.modelSpec.model == "Corolla Axio" && it.marketCode == "BD" })

    val isoVin = VinDecoderEngine.searchByVinOrModel("JTDKN3DU0N0123456")
    assertTrue(isoVin.any { it.modelSpec.brand == "Toyota" && it.inferredYear == 2022 })

    val hinoMatch = VinDecoderEngine.searchByVinOrModel("Hino AK1J")
    assertTrue(hinoMatch.any { it.modelSpec.brand == "Hino" && it.modelSpec.model == "AK1J" })
  }

  @Test
  fun evCatalogExcludesPetrolSparkPlugsAndExhaust() {
    val evParts = PartsCatalogEngine.generateCatalogForVehicle(sampleEv)
    assertFalse(evParts.any { it.id == "eng_spark_or_glow" })
    assertFalse(evParts.any { it.system == CatalogSystem.EXHAUST })
    assertTrue(evParts.any { it.system == CatalogSystem.COOLING })
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
    assertEquals(5000, oilBefore.intervalKm)
    assertEquals(6, oilBefore.intervalMonths)

    val trnStaffCar = initialSchedule.first { it.taskKey == "transmission_fluid" }
    assertEquals(10_000, trnStaffCar.intervalKm)
    assertEquals(12, trnStaffCar.intervalMonths)

    val pajeroSuv = sampleAxioBd.copy(
      id = "veh_bd_pajero",
      brand = "Mitsubishi",
      model = "Pajero",
    )
    val suvSchedule = PredictiveMaintenanceEngine.computeSchedule(
      vehicle = pajeroSuv,
      logs = emptyList(),
      catalogParts = parts,
    )
    val trnSuv = suvSchedule.first { it.taskKey == "transmission_fluid" }
    assertEquals(14_000, trnSuv.intervalKm)

    val bolyan = sampleAxioBd.copy(
      id = "veh_bd_bolyan",
      brand = "Arunima",
      model = "Bolyan (অরোনিমা বলিয়ান)",
      powertrain = Powertrain.DIESEL.name,
    )
    val bolyanSchedule = PredictiveMaintenanceEngine.computeSchedule(
      vehicle = bolyan,
      logs = emptyList(),
      catalogParts = parts,
    )
    assertEquals(14_000, bolyanSchedule.first { it.taskKey == "transmission_fluid" }.intervalKm)

    val trm180 = sampleAxioBd.copy(
      id = "veh_bd_trm180",
      brand = "Renault / TRM",
      model = "TRM 180 (টি আর এম ১৮০)",
      powertrain = Powertrain.DIESEL.name,
    )
    val trm180Schedule = PredictiveMaintenanceEngine.computeSchedule(
      vehicle = trm180,
      logs = emptyList(),
      catalogParts = parts,
    )
    assertEquals(29_000, trm180Schedule.first { it.taskKey == "transmission_fluid" }.intervalKm)

    val trm360 = sampleAxioBd.copy(
      id = "veh_bd_trm360",
      brand = "Renault / TRM",
      model = "TRM 360 (টি আর এম ৩৬০)",
      powertrain = Powertrain.DIESEL.name,
    )
    val trm360Schedule = PredictiveMaintenanceEngine.computeSchedule(
      vehicle = trm360,
      logs = emptyList(),
      catalogParts = parts,
    )
    assertEquals(38_000, trm360Schedule.first { it.taskKey == "transmission_fluid" }.intervalKm)

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
