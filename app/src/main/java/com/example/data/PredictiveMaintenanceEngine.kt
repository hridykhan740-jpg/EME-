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
      title = "Engine Oil & Filter Replacement",
      categoryLabel = "Fluids & Filters",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 10_000,
      intervalMonths = 12,
      linkedPartId = "eng_oil_filter",
      reason = "Prevents thermal shear breakdown and timing-chain varnish.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "cabin_filter",
      title = "Cabin HEPA / Carbon Air Filter",
      categoryLabel = "Filters",
      system = CatalogSystem.BODY,
      baseIntervalKm = 15_000,
      intervalMonths = 12,
      linkedPartId = "bdy_cabin_hepa",
      reason = "Maintains HVAC blower airflow and filters PM2.5 particulates.",
    ),
    IntervalTemplate(
      taskKey = "engine_air_filter",
      title = "Engine Intake Air Filter",
      categoryLabel = "Filters",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 20_000,
      intervalMonths = 24,
      linkedPartId = "eng_air_filter",
      reason = "Protects MAF/MAP accuracy and turbocharger compressor vanes.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "wiper_blades",
      title = "Silicone Windshield Wiper Blades",
      categoryLabel = "Wear Items",
      system = CatalogSystem.BODY,
      baseIntervalKm = 20_000,
      intervalMonths = 12,
      linkedPartId = "bdy_wiper_blades",
      reason = "Ensures streak-free visibility for ADAS forward camera.",
    ),
    IntervalTemplate(
      taskKey = "brake_fluid",
      title = "DOT 4 LV Brake Fluid Exchange",
      categoryLabel = "Fluids",
      system = CatalogSystem.BRAKES,
      baseIntervalKm = 30_000,
      intervalMonths = 24,
      linkedPartId = "brk_dot4_fluid",
      reason = "Hygroscopic moisture lowers boiling point and corrodes ABS valves.",
    ),
    IntervalTemplate(
      taskKey = "brake_pads_rotors",
      title = "Front Brake Pads & Rotor Inspection",
      categoryLabel = "Wear Items",
      system = CatalogSystem.BRAKES,
      baseIntervalKm = 40_000,
      intervalMonths = 36,
      linkedPartId = "brk_front_pads",
      reason = "Verifies friction thickness and rotor runout under heavy braking.",
    ),
    IntervalTemplate(
      taskKey = "transmission_fluid",
      title = "Transmission / e-Axle Fluid Service",
      categoryLabel = "Fluids",
      system = CatalogSystem.TRANSMISSION,
      baseIntervalKm = 60_000,
      intervalMonths = 48,
      linkedPartId = "trn_fluid",
      reason = "Preserves clutch friction modifiers and solenoid hydraulic pressure.",
    ),
    IntervalTemplate(
      taskKey = "battery_12v",
      title = "12V Auxiliary Battery Load Test",
      categoryLabel = "Wear Items",
      system = CatalogSystem.ELECTRICAL,
      baseIntervalKm = 50_000,
      intervalMonths = 36,
      linkedPartId = "elc_battery",
      reason = "Prevents sudden no-start or EV/Hybrid ECU wake-up faults.",
    ),
    IntervalTemplate(
      taskKey = "spark_plugs",
      title = "Iridium Spark / Glow Plug Service",
      categoryLabel = "Wear Items",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 60_000,
      intervalMonths = 48,
      linkedPartId = "eng_spark_or_glow",
      reason = "Maintains clean combustion efficiency and protects ignition coils.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "suspension_inspection",
      title = "Strut, Ball Joint & Bushing Audit",
      categoryLabel = "Wear Items",
      system = CatalogSystem.SUSPENSION,
      baseIntervalKm = 65_000,
      intervalMonths = 48,
      linkedPartId = "sus_front_strut",
      reason = "Detects damper oil weeping and compliance bushing tears early.",
    ),
    IntervalTemplate(
      taskKey = "coolant_flush",
      title = "Engine / HV Inverter Coolant Flush",
      categoryLabel = "Fluids",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 80_000,
      intervalMonths = 60,
      linkedPartId = "eng_ev_drive_coolant",
      reason = "Maintains pH corrosion buffer and dielectric thermal transfer.",
    ),
    IntervalTemplate(
      taskKey = "drive_belt",
      title = "Serpentine Accessory Belt Replacement",
      categoryLabel = "Wear Items",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 80_000,
      intervalMonths = 60,
      linkedPartId = "eng_timing_belt",
      reason = "Prevents rib cracking and alternator/water-pump belt slip.",
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
        // Adjust intervals dynamically by market climate/traffic & powertrain
        var intervalKm = tpl.baseIntervalKm
        if (isSevereAsianMarket && tpl.taskKey in setOf("engine_oil_filter", "cabin_filter", "engine_air_filter")) {
          intervalKm = (intervalKm * 0.85).roundToInt()
        }
        if ((isHybrid || isEv) && tpl.taskKey == "brake_pads_rotors") {
          // Regenerative braking extends friction pad life
          intervalKm = (intervalKm * 1.35).roundToInt()
        }

        val logged = latestLogsByKey[tpl.taskKey]
        // If no log exists yet, estimate prior scheduled cycle below currentOdo or 0L
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
