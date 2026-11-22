package com.example.data

import com.example.model.CatalogPart
import com.example.model.CatalogSystem
import com.example.model.MaintenanceLog
import com.example.model.MaintenanceUrgency
import com.example.model.Powertrain
import com.example.model.PredictiveMaintenanceItem
import com.example.model.UserVehicle
import com.example.model.VehicleCategory
import kotlin.math.roundToInt

enum class BaoVehicleClass(
  val titleEn: String,
  val titleBn: String,
  val transmissionKm: Int,
  val transmissionMonths: Int,
  val oilFilterPaperKm: Int,
  val oilFilterPaperMonths: Int,
  val oilFilterFeltReplaceKm: Int,
  val oilFilterFeltReplaceMonths: Int,
  val powerSteeringTopUpLiters: String,
) {
  STAFF_CAR(
    titleEn = "Staff Car / Sedan",
    titleBn = "সকল প্রকার স্টাফকার",
    transmissionKm = 10_000,
    transmissionMonths = 12,
    oilFilterPaperKm = 5_000,
    oilFilterPaperMonths = 3,
    oilFilterFeltReplaceKm = 10_000,
    oilFilterFeltReplaceMonths = 6,
    powerSteeringTopUpLiters = "0.25L",
  ),
  JEEP_QUARTER_TON(
    titleEn = "1/4 Ton Jeep / 4x4 SUV",
    titleBn = "সকল প্রকার ট্রাক ১/৪ টন (জীপ/SUV)",
    transmissionKm = 14_000,
    transmissionMonths = 12,
    oilFilterPaperKm = 5_000,
    oilFilterPaperMonths = 3,
    oilFilterFeltReplaceKm = 10_000,
    oilFilterFeltReplaceMonths = 6,
    powerSteeringTopUpLiters = "0.40L",
  ),
  PICKUP_LIGHT_MPV(
    titleEn = "3/4–1.5 Ton Pickup / MPV",
    titleBn = "ট্রাক ৩/৪ – ১ ১/২ টন / পিকআপ",
    transmissionKm = 14_000,
    transmissionMonths = 12,
    oilFilterPaperKm = 5_000,
    oilFilterPaperMonths = 3,
    oilFilterFeltReplaceKm = 10_000,
    oilFilterFeltReplaceMonths = 6,
    powerSteeringTopUpLiters = "0.45L",
  ),
  MEDIUM_TRUCK(
    titleEn = "2.5–3.5 Ton Truck",
    titleBn = "ট্রাক ২ ১/২ – ৩ ১/২ টন",
    transmissionKm = 29_000,
    transmissionMonths = 12,
    oilFilterPaperKm = 5_000,
    oilFilterPaperMonths = 3,
    oilFilterFeltReplaceKm = 10_000,
    oilFilterFeltReplaceMonths = 6,
    powerSteeringTopUpLiters = "0.60L",
  ),
  HEAVY_TRUCK_BUS(
    titleEn = "5 Ton & Above Truck / Bus",
    titleBn = "ট্রাক ৫ টন এবং উহার উপরে",
    transmissionKm = 38_000,
    transmissionMonths = 18,
    oilFilterPaperKm = 10_000,
    oilFilterPaperMonths = 6,
    oilFilterFeltReplaceKm = 19_000,
    oilFilterFeltReplaceMonths = 12,
    powerSteeringTopUpLiters = "0.60L",
  ),
}

data class BaoChartRow(
  val sl: String,
  val vehicleType: String,
  val engineOil: String,
  val transDiffOil: String,
  val oilFilter: String,
  val fuelAndAirFilter: String,
)

object PredictiveMaintenanceEngine {

  val baoChartRows: List<BaoChartRow> = listOf(
    BaoChartRow("১", "মোটর সাইকেল", "প্রাঃ ৮০০ কি.মি./৩ মাঃ • পরঃ ৫,০০০ কি.মি./৬ মাঃ", "প্রাঃ ৮০০ • পরঃ ৫,০০০ কি.মি./৬ মাঃ", "কাগজ: ৩,২০০/৩ মাঃ • ফেল্ট: ৬,০০০/৬ মাঃ", "টায়ার সামনের ৩২,০০০ / পিছনের ২৪,০০০ কি.মি."),
    BaoChartRow("২", "সকল প্রকার স্টাফকার", "প্রাঃ ৮০০ কি.মি./৩ মাঃ • পরঃ ৫,০০০ কি.মি./৬ মাঃ", "প্রাঃ ৮০০ • পরঃ ১০,০০০ কি.মি./১২ মাঃ", "কাগজ: ৫,০০০/৩ মাঃ • ফেল্ট: ১০,০০০/৬ মাঃ", "ফুয়েল/এয়ার কাগজ: ১৯,০০০/১২ মাঃ • স্পঞ্জ: ২৯,০০০/১৮ মাঃ"),
    BaoChartRow("৩", "সকল প্রকার ট্রাক ১/৪ টন (জীপ/SUV)", "প্রাঃ ৮০০ কি.মি./৩ মাঃ • পরঃ ৫,০০০ কি.মি./৬ মাঃ", "প্রাঃ ৮০০ • পরঃ ১৪,০০০ কি.মি./১২ মাঃ", "কাগজ: ৫,০০০/৩ মাঃ • ফেল্ট: ১০,০০০/৬ মাঃ", "ফুয়েল/এয়ার কাগজ: ১৯,০০০/১২ মাঃ • স্পঞ্জ: ২৯,০০০/১৮ মাঃ"),
    BaoChartRow("৪", "ট্রাক ৩/৪ – ১ ১/২ টন (পিকআপ)", "প্রাঃ ৮০০ কি.মি./৩ মাঃ • পরঃ ৫,০০০ কি.মি./৬ মাঃ", "প্রাঃ ৮০০ • পরঃ ১৪,০০০ কি.মি./১২ মাঃ", "কাগজ: ৫,০০০/৩ মাঃ • ফেল্ট: ১০,০০০/৬ মাঃ", "ফুয়েল/এয়ার কাগজ: ১৯,০০০/১২ মাঃ • স্পঞ্জ: ২৯,০০০/১৮ মাঃ"),
    BaoChartRow("৫", "ট্রাক ২ ১/২ – ৩ ১/২ টন", "প্রাঃ ৮০০ কি.মি./৩ মাঃ • পরঃ ৫,০০০ কি.মি./৬ মাঃ", "প্রাঃ ৮০০ • পরঃ ২৯,০০০ কি.মি./১২ মাঃ", "কাগজ: ৫,০০০/৩ মাঃ • ফেল্ট: ১০,০০০/৬ মাঃ", "ফুয়েল/এয়ার কাগজ: ১৯,০০০/১২ মাঃ • স্পঞ্জ: ২৯,০০০/১৮ মাঃ"),
    BaoChartRow("৬", "ট্রাক ৫ টন ও উহার উপরে", "প্রাঃ ৮০০ কি.মি./৩ মাঃ • পরঃ ৫,০০০ কি.মি./৬ মাঃ", "প্রাঃ ৮০০ • পরঃ ৩৮,০০০ কি.মি./১৮ মাঃ", "কাগজ: ১০,০০০/৬ মাঃ • ফেল্ট: ১৯,০০০/১২ মাঃ", "ফুয়েল/এয়ার কাগজ: ১৯,০০০/১২ মাঃ • স্পঞ্জ: ২৯,০০০/১৮ মাঃ"),
    BaoChartRow("৭", "সকল প্রকার ট্যাংক", "প্রাঃ ০৩ ঘণ্টা • পরঃ ১২০ কি.মি./১৮ মাঃ", "প্রাঃ ৪০০ কি.মি./৩০ মাঃ • পরঃ ৪০০/৩০ মাঃ", "কাগজ/ফেল্ট: ১০০ ঘণ্টা • পরিবর্তন: ২০০ ঘণ্টা", "ফুয়েল ফিল্টার: ১০০ ঘণ্টা"),
  )

  val baoSpecialNotes: List<String> = listOf(
    "এন্টিফ্রিজ (বিএও ২৭৩/০৪): ৩ বছর পর ম্যানুয়াল মোতাবেক পরিবর্তন করতে হবে।",
    "পাওয়ার স্টিয়ারিং অয়েল টপিং আপ (বিব্লিউআই ২৩/০২): স্টাফকার ০.২৫ লিঃ, জীপ ০.৪০ লিঃ, পিকআপ ০.৪৫ লিঃ, ৩–৫ টন ০.৬০ লিঃ।",
    "কভার আউটার (টায়ার): সকল গাড়ী ৪০,০০০ কি.মি. (মোটরসাইকেল সামনের ৩২,০০০ ও পিছনের ২৪,০০০ কি.মি.)।",
    "ফুয়েল ফিল্টার প্রাইমারি (কাগজের কার্তুজ): ৫,০০০ কি.মি. বা ০৬ মাস | সেকেন্ডারি (স্টিল/অন্য): ১০,০০০ কি.মি. বা ১২ মাস।",
    "টাইমিং বেল্ট (সকল জীপ ও পিকআপ): ১,৫০,০০০ কি.মি. অথবা ৯০,০০০ মাইল পর পরিবর্তন করতে হবে।",
  )

  fun classifyVehicle(vehicle: UserVehicle): BaoVehicleClass {
    val combined = "${vehicle.brand} ${vehicle.model} ${vehicle.variant}".lowercase()
    val matchedSpec = VehicleCatalogData.models.firstOrNull {
      it.brand.equals(vehicle.brand, ignoreCase = true) &&
        it.model.equals(vehicle.model, ignoreCase = true)
    }
    return when {
      combined.contains("lpt 1615") || combined.contains("prima") || combined.contains("ak1j") ||
        combined.contains("rm2") || combined.contains("viking") || combined.contains("ecomet") ||
        combined.contains("pro 3015") || combined.contains("skyline") || combined.contains("500 series") ||
        combined.contains("trm 240") || combined.contains("trm 360") ->
        BaoVehicleClass.HEAVY_TRUCK_BUS
      combined.contains("dyna") || combined.contains("canter") || combined.contains("n-series") ||
        combined.contains("pro 2049") || combined.contains("trm 180") ||
        (combined.contains("300") && vehicle.brand.equals("Hino", ignoreCase = true)) ->
        BaoVehicleClass.MEDIUM_TRUCK
      combined.contains("hilux") || combined.contains("l200") || combined.contains("d-max") ||
        combined.contains("bolero") || combined.contains("dost") || combined.contains("ace") ||
        combined.contains("intra") || combined.contains("xenon") || combined.contains("jeeto") ||
        combined.contains("bolyan") || combined.contains("arunima") ||
        matchedSpec?.category == VehicleCategory.MICROBUSES_MPVS_VANS ->
        BaoVehicleClass.PICKUP_LIGHT_MPV
      matchedSpec?.category == VehicleCategory.SUVS_CROSSOVERS_4X4 ||
        combined.contains("prado") || combined.contains("land cruiser") || combined.contains("pajero") ||
        combined.contains("hexa") || combined.contains("harrier") || combined.contains("fortuner") ->
        BaoVehicleClass.JEEP_QUARTER_TON
      matchedSpec?.category == VehicleCategory.COMMERCIAL_TRUCKS_BUSES ->
        BaoVehicleClass.MEDIUM_TRUCK
      else -> BaoVehicleClass.STAFF_CAR
    }
  }

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
      title = "Engine Oil & Paper Cartridge Filter (BAO 35/88)",
      categoryLabel = "BAO 35/88 • Engine Oil",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 5_000,
      intervalMonths = 6,
      linkedPartId = "eng_oil_filter",
      reason = "BAO 35/88 (07/04): Initial 800 km / 3 mo; subsequent Engine Oil 5,000 km / 6 mo & Paper Filter 5,000 km / 3 mo (5T+: 10,000 km / 6 mo).",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "oil_filter_felt_gauze",
      title = "Oil Filter (Felt / Gauze Clean & Replace)",
      categoryLabel = "BAO 35/88 • Felt/Gauze",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 10_000,
      intervalMonths = 6,
      linkedPartId = "eng_oil_filter",
      reason = "BAO 35/88: Clean felt/gauze element every 5,000 km (5T+: 10,000 km); replace at 10,000 km / 6 mo (5T+: 19,000 km / 12 mo).",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "fuel_filter_primary",
      title = "Primary Fuel Filter (Paper Cartridge)",
      categoryLabel = "BAO 35/88 • Fuel Primary",
      system = CatalogSystem.FUEL_AIR,
      baseIntervalKm = 5_000,
      intervalMonths = 6,
      linkedPartId = "ful_air_filter",
      reason = "BAO 35/88 Note: Primary paper-cartridge fuel filters must be replaced every 5,000 km or 6 months.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "fuel_filter_secondary",
      title = "Secondary Fuel Filter (Steel / Auxiliary)",
      categoryLabel = "BAO 35/88 • Fuel Secondary",
      system = CatalogSystem.FUEL_AIR,
      baseIntervalKm = 10_000,
      intervalMonths = 12,
      linkedPartId = "ful_pump_module",
      reason = "BAO 35/88 Note: Secondary steel/cartridge fuel filters replace every 10,000 km or 12 months.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "fuel_filter_service",
      title = "Main Fuel Filter Element Replacement",
      categoryLabel = "BAO 35/88 • Fuel Main",
      system = CatalogSystem.FUEL_AIR,
      baseIntervalKm = 19_000,
      intervalMonths = 12,
      linkedPartId = "ful_fuel_pump_filter",
      reason = "BAO 35/88 Chart Col-5: Complete fuel filter element assembly replacement every 19,000 km / 12 months.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "transmission_fluid",
      title = "Transmission & Differential Oil (BAO 35/88)",
      categoryLabel = "BAO 35/88 • Trans/Diff",
      system = CatalogSystem.TRANSMISSION,
      baseIntervalKm = 10_000,
      intervalMonths = 12,
      linkedPartId = "trn_fluid",
      reason = "BAO 35/88: Initial 800 km / 3 mo; Staff Car 10k, Jeep/1/4T–1.5T Pickup 14k, 2.5–3.5T 29k (12 mo), 5T+ 38k km / 18 mo.",
    ),
    IntervalTemplate(
      taskKey = "engine_air_filter",
      title = "Air Cleaner Element (Paper Cartridge)",
      categoryLabel = "BAO 35/88 • Air Paper",
      system = CatalogSystem.FUEL_AIR,
      baseIntervalKm = 19_000,
      intervalMonths = 12,
      linkedPartId = "ful_air_filter",
      reason = "BAO 35/88 Chart Col-6: Paper-cartridge air cleaner element change every 19,000 km or 12 months.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "air_filter_sponge",
      title = "Air Cleaner Element (Sponge / Heavy Type)",
      categoryLabel = "BAO 35/88 • Air Sponge",
      system = CatalogSystem.FUEL_AIR,
      baseIntervalKm = 29_000,
      intervalMonths = 18,
      linkedPartId = "ful_air_filter",
      reason = "BAO 35/88 Chart Col-6: Sponge and heavy-duty air cleaner elements change every 29,000 km or 18 months.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "power_steering_topup",
      title = "Power Steering Oil Check & Top-Up (BWI 23/02)",
      categoryLabel = "BWI 23/02 • Steering",
      system = CatalogSystem.STEERING,
      baseIntervalKm = 5_000,
      intervalMonths = 6,
      linkedPartId = "str_rack_assembly",
      reason = "BWI 23/02 Spec: Staff Car 0.25L • Jeep 0.40L • Pickup 0.45L • 3–5 Ton Truck 0.60L hydraulic oil top-up.",
    ),
    IntervalTemplate(
      taskKey = "tyre_outer_cover",
      title = "Tyre / Cover Outer Replacement (BAO 35/88)",
      categoryLabel = "BAO 35/88 • Tyres",
      system = CatalogSystem.SUSPENSION,
      baseIntervalKm = 40_000,
      intervalMonths = 36,
      linkedPartId = "sus_wheel_hub",
      reason = "BAO 35/88 Note: All four-wheel vehicles replace outer tyres at 40,000 km (Motorcycle Front 32k / Rear 24k km).",
    ),
    IntervalTemplate(
      taskKey = "coolant_flush",
      title = "Antifreeze / Coolant Replacement (BAO 273/04)",
      categoryLabel = "BAO 273/04 • Cooling",
      system = CatalogSystem.COOLING,
      baseIntervalKm = 45_000,
      intervalMonths = 36,
      linkedPartId = "col_radiator_assembly",
      reason = "BAO 273/04 Note: Replace antifreeze & coolant mixture every 3 years (36 months) per OEM manual.",
    ),
    IntervalTemplate(
      taskKey = "timing_belt",
      title = "Timing Belt Replacement (Jeeps & Pickups)",
      categoryLabel = "BAO 35/88 • Timing",
      system = CatalogSystem.ENGINE,
      baseIntervalKm = 150_000,
      intervalMonths = 60,
      linkedPartId = "eng_timing_chain",
      reason = "BAO 35/88 Note: Replace timing belt at 150,000 km (90,000 miles) on all Jeeps, SUVs & Pickups.",
      excludeEv = true,
    ),
    IntervalTemplate(
      taskKey = "cabin_filter",
      title = "AC Cabin HEPA / PM2.5 Filter",
      categoryLabel = "AC / HVAC",
      system = CatalogSystem.AC_HVAC,
      baseIntervalKm = 12_500,
      intervalMonths = 12,
      linkedPartId = "hvc_cabin_blower",
      reason = "Maintains evaporator efficiency and clean cabin airflow in high-dust Bangladesh traffic.",
    ),
    IntervalTemplate(
      taskKey = "brake_fluid",
      title = "DOT 4 LV Brake Fluid & Caliper Pin Service",
      categoryLabel = "Brakes & Hydraulics",
      system = CatalogSystem.BRAKES,
      baseIntervalKm = 30_000,
      intervalMonths = 24,
      linkedPartId = "brk_front_rotors",
      reason = "Prevents moisture boiling and internal ABS/ESP solenoid corrosion.",
    ),
    IntervalTemplate(
      taskKey = "brake_pads_rotors",
      title = "Brake Pads, Shoes, Shims & Rotor Audit",
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
      reason = "Prevents no-start faults and ECU voltage drops in tropical heat.",
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
      taskKey = "clutch_hydraulics",
      title = "Clutch Disc, Release Bearing & Fluid",
      categoryLabel = "Clutch",
      system = CatalogSystem.CLUTCH,
      baseIntervalKm = 75_000,
      intervalMonths = 60,
      linkedPartId = "clt_complete_kit",
      reason = "Verifies clutch release travel, damper spring wear, and slave seal health.",
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
    val baoClass = classifyVehicle(vehicle)

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
        var intervalMonths = tpl.intervalMonths
        var reason = tpl.reason

        when (tpl.taskKey) {
          "engine_oil_filter" -> {
            intervalKm = 5_000
            intervalMonths = 6
            reason = "Class: ${baoClass.titleEn} • Engine Oil: 5,000 km / 6 mo (Initial 800 km / 3 mo) • Paper Filter: ${baoClass.oilFilterPaperKm} km / ${baoClass.oilFilterPaperMonths} mo."
          }
          "oil_filter_felt_gauze" -> {
            intervalKm = baoClass.oilFilterFeltReplaceKm
            intervalMonths = baoClass.oilFilterFeltReplaceMonths
          }
          "transmission_fluid" -> {
            intervalKm = baoClass.transmissionKm
            intervalMonths = baoClass.transmissionMonths
            reason = "BAO 35/88 (${baoClass.titleEn}): Initial 800 km / 3 mo, then every ${baoClass.transmissionKm} km / ${baoClass.transmissionMonths} months."
          }
          "power_steering_topup" -> {
            reason = "BWI 23/02 (${baoClass.titleEn}): Recommended Power Steering Oil top-up capacity ${baoClass.powerSteeringTopUpLiters}."
          }
          "brake_pads_rotors" -> if (isHybrid || isEv) {
            intervalKm = (intervalKm * 1.35).roundToInt()
          }
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
          intervalMonths = intervalMonths,
          lastServiceKm = lastServiceKm,
          lastServiceEpochMs = lastServiceMs,
          nextDueKm = nextDueKm,
          remainingKm = remainingKm,
          lifeRemainingPercent = lifeRemainingPercent,
          urgency = urgency,
          linkedPartId = resolvedPart?.id ?: "",
          linkedPartOem = resolvedPart?.oemNumber ?: "",
          recommendationReason = reason,
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
