package com.example.data

import com.example.model.CatalogPart
import com.example.model.CatalogSystem
import com.example.model.MaintenanceLog
import com.example.model.MaintenanceUrgency
import com.example.model.Powertrain
import com.example.model.PredictiveMaintenanceItem
import com.example.model.UserVehicle
import kotlin.math.roundToInt

object PredictiveMaintenanceEngine {

  private data class IntervalTemplate(
    val taskKey: String,
    val title: String,
    val categoryLabel: String,
    val system: CatalogSystem,
    val baseIntervalKm: Int,
    val intervalMonths: Int,
    val linkedPartId: String,
    val reason: String,
    val excludeEv: Boolean = false,
  )

  private val templates = listOf(
    IntervalTemplate(
      taskKey = "engine_oil_filter",
      title = "Engine Oil & Filter Assembly Service",
      categoryLabel = "Fluids & Filters",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 10_000,
      intervalMonths = 12,
      linkedPartId = "eng_oil_filter",
      reason = "Replace cartridge, O-ring, and drain crush washer to prevent sludge & chain wear.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "cabin_filter",
      title = "AC Cabin HEPA / PM2.5 Filter",
      categoryLabel = "AC / HVAC",
      system = CatalogSystem.AC_HVAC,
      baseIntervalKm = 15_000,
      intervalMonths = 12,
      linkedPartId = "hvc_cabin_blower",
      reason = "Protects HVAC evaporator and maintains clean cabin airflow in heavy urban dust.",
    ),
    IntervalTemplate(
      taskKey = "engine_air_filter",
      title = "Engine Intake Air Filter",
      categoryLabel = "Fuel & Air",
      system = CatalogSystem.FUEL_AIR,
      baseIntervalKm = 20_000,
      intervalMonths = 24,
      linkedPartId = "ful_air_filter",
      reason = "Protects MAF sensor accuracy and keeps fuel economy optimal.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "wiper_blades",
      title = "Silicone Windshield Wiper Blades",
      categoryLabel = "Body & Visibility",
      system = CatalogSystem.BODY_INTERIOR,
      baseIntervalKm = 20_000,
      intervalMonths = 12,
      linkedPartId = "bdy_wiper_blades",
      reason = "Ensures streak-free monsoon visibility for driver & ADAS camera.",
    ),
    IntervalTemplate(
      taskKey = "brake_fluid",
      title = "DOT 4 LV Brake Fluid Exchange",
      categoryLabel = "Brakes & Hydraulics",
      system = CatalogSystem.BRAKES,
      baseIntervalKm = 30_000,
      intervalMonths = 24,
      linkedPartId = "brk_front_rotors",
      reason = "Prevents moisture boiling and internal ABS/ESP solenoid corrosion.",
    ),
    IntervalTemplate(
      taskKey = "fuel_filter_service",
      title = "Fuel Filter & Water Separator",
      categoryLabel = "Fuel & Air",
      system = CatalogSystem.FUEL_AIR,
      baseIntervalKm = 35_000,
      intervalMonths = 30,
      linkedPartId = "ful_fuel_pump_filter",
      reason = "Protects high-pressure injectors and fuel pump from water and sediment.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "brake_pads_rotors",
      title = "Front Brake Pads, Shims & Rotor Audit",
      categoryLabel = "Brakes & Friction",
      system = CatalogSystem.BRAKES,
      baseIntervalKm = 40_000,
      intervalMonths = 36,
      linkedPartId = "brk_front_pads",
      reason = "Inspects pad lining thickness, slide pins, and disc runout.",
    ),
    IntervalTemplate(
      taskKey = "battery_12v",
      title = "12V Auxiliary Battery & Terminal Test",
      categoryLabel = "Electrical",
      system = CatalogSystem.ELECTRICAL,
      baseIntervalKm = 50_000,
      intervalMonths = 36,
      linkedPartId = "elc_battery",
      reason = "Prevents no-start faults and Hybrid/EV ECU voltage drops in heat.",
    ),
    IntervalTemplate(
      taskKey = "transmission_fluid",
      title = "Transmission / eCVT Fluid & Strainer",
      categoryLabel = "Transmission",
      system = CatalogSystem.TRANSMISSION,
      baseIntervalKm = 60_000,
      intervalMonths = 48,
      linkedPartId = "trn_fluid",
      reason = "Preserves clutch packs, steel belt, and valve-body hydraulic pressure.",
    ),
    IntervalTemplate(
      taskKey = "spark_plugs",
      title = "Iridium Spark / Glow Plug Service",
      categoryLabel = "Engine Ignition",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 60_000,
      intervalMonths = 48,
      linkedPartId = "eng_spark_or_glow",
      reason = "Maintains clean combustion efficiency and protects coil boots.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "suspension_inspection",
      title = "Strut, Ball Joint & Bushing Audit",
      categoryLabel = "Suspension",
      system = CatalogSystem.SUSPENSION,
      baseIntervalKm = 65_000,
      intervalMonths = 48,
      linkedPartId = "sus_front_strut",
      reason = "Detects damper weeping and compliance bushing tears on rough roads.",
    ),
    IntervalTemplate(
      taskKey = "steering_rack_audit",
      title = "Steering Rack, Tie-Rod & Boot Check",
      categoryLabel = "Steering",
      system = CatalogSystem.STEERING,
      baseIntervalKm = 70_000,
      intervalMonths = 48,
      linkedPartId = "str_rack_assembly",
      reason = "Prevents rack play, torn bellows grease loss, and uneven tire scrub.",
    ),
    IntervalTemplate(
      taskKey = "clutch_hydraulics",
      title = "Clutch Disc, Release Bearing & Fluid",
      categoryLabel = "Clutch",
      system = CatalogSystem.CLUTCH,
      baseIntervalKm = 75_000,
      intervalMonths = 60,
      linkedPartId = "clt_complete_kit",
      reason = "Verifies clutch release travel, damper spring wear, and slave seal health.",
    ),
    IntervalTemplate(
      taskKey = "coolant_flush",
      title = "Radiator, Thermostat & Coolant Flush",
      categoryLabel = "Cooling",
      system = CatalogSystem.COOLING,
      baseIntervalKm = 80_000,
      intervalMonths = 60,
      linkedPartId = "col_radiator_assembly",
      reason = "Maintains pH corrosion protection and peak cooling efficiency in traffic.",
    ),
    IntervalTemplate(
      taskKey = "drive_belt",
      title = "Serpentine Belt, Tensioner & Idler",
      categoryLabel = "Engine Drive",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 80_000,
      intervalMonths = 60,
      linkedPartId = "eng_timing_belt",
      reason = "Prevents rib cracking and water-pump/alternator belt slip.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "exhaust_emissions_check",
      title = "Exhaust O2 Sensor, DPF & Flange Check",
      categoryLabel = "Exhaust",
      system = CatalogSystem.EXHAUST,
      baseIntervalKm = 90_000,
      intervalMonths = 60,
      linkedPartId = "exh_cat_o2",
      reason = "Keeps air-fuel trim accurate and checks for donut gasket leaks.",
      excludeEv = true,
    ),
  )

  fun computeSchedule(
    vehicle: UserVehicle?,
    logs: List<MaintenanceLog>,
    catalogParts: List<CatalogPart>,
  ): List<PredictiveMaintenanceItem> {
    if (vehicle == null) return emptyList()
    val powertrain = Powertrain.fromRaw(vehicle.powertrain)
    val isEv = powertrain == Powertrain.EV
    val isHybrid = powertrain == Powertrain.HYBRID
    val isSevereAsianMarket = vehicle.marketCode in setOf("BD", "IND", "ASEAN", "KDM", "JDM")

    val partsById = catalogParts.associateBy { it.id }
    val latestLogsByKey = logs
      .filter { it.vehicleId == vehicle.id }
      .groupBy { it.taskKey }
      .mapValues { (_, list) -> list.maxByOrNull { it.serviceKm }!! }

    val currentOdo = vehicle.odometerKm.coerceAtLeast(0L)

    return templates
      .filterNot { isEv && it.excludeEv }
      .map { tpl ->
        var intervalKm = tpl.baseIntervalKm
        if (isSevereAsianMarket && tpl.taskKey in setOf("engine_oil_filter", "cabin_filter", "engine_air_filter", "fuel_filter_service")) {
          intervalKm = (intervalKm * 0.85).roundToInt()
        }
        if ((isHybrid || isEv) && tpl.taskKey == "brake_pads_rotors") {
          intervalKm = (intervalKm * 1.35).roundToInt()
        }

        val logged = latestLogsByKey[tpl.taskKey]
        val lastServiceKm = logged?.serviceKm ?: run {
          if (currentOdo > intervalKm) {
            ((currentOdo - 1) / intervalKm) * intervalKm
          } else {
            0L
          }
        }
        val lastServiceMs = logged?.serviceEpochMs ?: 0L
        val nextDueKm = lastServiceKm + intervalKm
        val remainingKm = nextDueKm - currentOdo
        val usedFraction = ((currentOdo - lastServiceKm).toDouble() / intervalKm.toDouble()).coerceIn(0.0, 1.4)
        val lifeRemainingPercent = ((1.0 - usedFraction) * 100.0).roundToInt().coerceIn(0, 100)

        val urgency = when {
          remainingKm <= 0L -> MaintenanceUrgency.OVERDUE
          remainingKm <= (intervalKm * 0.22).roundToInt() -> MaintenanceUrgency.DUE_SOON
          else -> MaintenanceUrgency.HEALTHY
        }

        val resolvedPart = partsById[tpl.linkedPartId]
          ?: partsById.values.firstOrNull { it.linkedTaskKey == tpl.taskKey }
          ?: partsById.values.firstOrNull { it.system == tpl.system }

        PredictiveMaintenanceItem(
          taskKey = tpl.taskKey,
          title = tpl.title,
          categoryLabel = tpl.categoryLabel,
          system = tpl.system,
          intervalKm = intervalKm,
          intervalMonths = tpl.intervalMonths,
          lastServiceKm = lastServiceKm,
          lastServiceEpochMs = lastServiceMs,
          nextDueKm = nextDueKm,
          remainingKm = remainingKm,
          lifeRemainingPercent = lifeRemainingPercent,
          urgency = urgency,
          linkedPartId = resolvedPart?.id ?: "",
          linkedPartOem = resolvedPart?.oemNumber ?: "",
          recommendationReason = tpl.reason,
        )
      }
      .sortedWith(
        compareBy<PredictiveMaintenanceItem> {
          when (it.urgency) {
            MaintenanceUrgency.OVERDUE -> 0
            MaintenanceUrgency.DUE_SOON -> 1
            MaintenanceUrgency.HEALTHY -> 2
          }
        }.thenBy { it.remainingKm }
      )
  }

  fun computeHealthScore(items: List<PredictiveMaintenanceItem>): Int {
    if (items.isEmpty()) return 100
    val overdueCount = items.count { it.urgency == MaintenanceUrgency.OVERDUE }
    val dueSoonCount = items.count { it.urgency == MaintenanceUrgency.DUE_SOON }
    val avgLife = items.map { it.lifeRemainingPercent }.average()
    val penalty = overdueCount * 14 + dueSoonCount * 5
    return (avgLife - penalty * 0.4).roundToInt().coerceIn(18, 100)
  }
}
