package com.example.data

import com.example.model.CatalogPart
import com.example.model.CatalogSystem
import com.example.model.PartVisualKind
import com.example.model.Powertrain
import com.example.model.StockStatus
import com.example.model.UserVehicle
import java.util.Locale

class PartSearchIndex(val parts: List<CatalogPart>) {
  private val invertedIndex: Map<String, Set<Int>>
  private val normalizedCorpus: List<String>

  init {
    val map = HashMap<String, MutableSet<Int>>()
    val corpus = ArrayList<String>(parts.size)
    parts.forEachIndexed { idx, part ->
      val text = buildString {
        append(part.name).append(' ')
        append(part.oemNumber).append(' ')
        append(part.oemManufacturer).append(' ')
        append(part.aftermarketNumber).append(' ')
        append(part.aftermarketBrand).append(' ')
        append(part.system.displayName).append(' ')
        append(part.subSystem).append(' ')
        append(part.specSummary).append(' ')
        part.keywords.forEach { append(it).append(' ') }
      }.lowercase(Locale.US)
      corpus.add(text)

      val tokens = text.split(Regex("[^a-z0-9]+")).filter { it.length >= 2 }
      for (token in tokens) {
        map.getOrPut(token) { HashSet() }.add(idx)
        if (token.length >= 3) {
          for (len in 3..minOf(token.length, 6)) {
            map.getOrPut(token.substring(0, len)) { HashSet() }.add(idx)
          }
        }
      }
    }
    invertedIndex = map
    normalizedCorpus = corpus
  }

  fun search(query: String): List<CatalogPart> {
    val trimmed = query.trim().lowercase(Locale.US)
    if (trimmed.isEmpty()) return parts
    val terms = trimmed.split(Regex("\\s+")).filter { it.isNotBlank() }
    if (terms.isEmpty()) return parts

    var candidateIndices: Set<Int>? = null
    for (term in terms) {
      val clean = term.replace(Regex("[^a-z0-9]"), "")
      val tokenHits = if (clean.length >= 2) invertedIndex[clean] else null
      val hits = if (!tokenHits.isNullOrEmpty()) {
        tokenHits
      } else {
        parts.indices.filterTo(HashSet()) { normalizedCorpus[it].contains(term) }
      }
      candidateIndices = if (candidateIndices == null) hits else candidateIndices.intersect(hits)
      if (candidateIndices.isEmpty()) break
    }
    return candidateIndices.orEmpty().sorted().map { parts[it] }
  }
}

object PartsCatalogEngine {

  private data class BrandOemSpec(
    val oemBrand: String,
    val codePrefix: String,
    val oilVisc: String,
    val oilFilterCode: String,
    val airFilterCode: String,
    val cabinFilterCode: String,
    val sparkCode: String,
    val padCode: String,
    val rotorCode: String,
    val batterySpec: String,
  )

  private fun brandSpecFor(brand: String, model: String, powertrain: Powertrain): BrandOemSpec {
    val b = brand.lowercase(Locale.US)
    val m = model.lowercase(Locale.US)
    return when {
      b.contains("toyota") || b.contains("lexus") -> {
        val isAxioFielder = m.contains("axio") || m.contains("fielder")
        val isPremioAllion = m.contains("premio") || m.contains("allion")
        val isNoah = m.contains("noah")
        val isHiAce = m.contains("hiace")
        val isPrado = m.contains("prado")
        BrandOemSpec(
          oemBrand = "Toyota Genuine Parts",
          codePrefix = "TOY",
          oilVisc = when {
            isHiAce && powertrain == Powertrain.DIESEL -> "5W-30 DL-1 / ACEA C2"
            isPrado && powertrain == Powertrain.DIESEL -> "0W-30 / 5W-30 DL-1"
            isAxioFielder && powertrain == Powertrain.HYBRID -> "0W-16 / 0W-20 API SP"
            else -> "0W-20 API SP / ILSAC GF-6A"
          },
          oilFilterCode = when {
            isAxioFielder -> "90915-YZZF2"
            isPremioAllion -> if (m.contains("260") || powertrain == Powertrain.PETROL) "90915-YZZF2" else "04152-YZZA6"
            isNoah -> "04152-YZZA6"
            isHiAce -> if (powertrain == Powertrain.DIESEL) "90915-30002" else "90915-YZZD2"
            isPrado -> if (powertrain == Powertrain.DIESEL) "90915-YZZD4" else "90915-YZZD2"
            else -> "90915-YZZN1"
          },
          airFilterCode = when {
            isAxioFielder -> "17801-21060"
            isPremioAllion -> "17801-21050"
            isNoah -> "17801-37021"
            isHiAce -> "17801-30060"
            isPrado -> "17801-31090"
            else -> "17801-F0020"
          },
          cabinFilterCode = when {
            isAxioFielder || isPremioAllion || isNoah -> "87139-52040"
            isPrado -> "87139-YZZ20"
            else -> "87139-YZZ82"
          },
          sparkCode = when {
            isAxioFielder -> "90919-01275"
            isPremioAllion || isNoah -> "90919-01253"
            else -> "90919-01298"
          },
          padCode = when {
            isAxioFielder -> "04465-52320"
            isPremioAllion -> "04465-21010"
            isNoah -> "04465-28520"
            isHiAce -> "04465-26421"
            isPrado -> "04465-60320"
            else -> "04465-33480"
          },
          rotorCode = when {
            isAxioFielder -> "43512-52130"
            isPremioAllion -> "43512-21020"
            isNoah -> "43512-28190"
            isPrado -> "43512-60190"
            else -> "43512-06150"
          },
          batterySpec = when {
            isAxioFielder && powertrain == Powertrain.HYBRID -> "S34B20R AGM Auxiliary 12V"
            isPrado || isHiAce -> "80D26L / 105D31L 12V Heavy-Duty"
            else -> "46B24R / LN2 12V EFB"
          },
        )
      }
      b.contains("honda") -> {
        val isVezel = m.contains("vezel") || m.contains("hr-v")
        val isGrace = m.contains("grace")
        BrandOemSpec(
          oemBrand = "Honda Genuine",
          codePrefix = "HON",
          oilVisc = "0W-20 Honda Ultra LEO / Type 2.0",
          oilFilterCode = "15400-RTA-003",
          airFilterCode = when {
            isVezel -> "17220-5R0-008"
            isGrace -> "17220-5R0-008"
            else -> "17220-64A-A00"
          },
          cabinFilterCode = when {
            isVezel || isGrace -> "80291-T5A-J01"
            else -> "80292-TBA-A11"
          },
          sparkCode = if (isVezel || isGrace) "12290-5R0-003" else "12290-59B-003",
          padCode = when {
            isVezel -> "45022-T7J-H00"
            isGrace -> "45022-T5A-000"
            else -> "45022-TGG-A01"
          },
          rotorCode = if (isVezel) "45251-T7A-000" else "45251-TVA-A00",
          batterySpec = if (isVezel || isGrace) "44B19L / N-55 EFB 12V" else "55B24L / Group 51R 12V",
        )
      }
      b.contains("mitsubishi") -> BrandOemSpec(
        oemBrand = "Mitsubishi Motors Genuine",
        codePrefix = "MMC",
        oilVisc = if (powertrain == Powertrain.DIESEL) "5W-30 ACEA C3 / DL-1" else "0W-20 / 5W-30 API SP",
        oilFilterCode = if (powertrain == Powertrain.DIESEL) "1230A182" else "MZ690070",
        airFilterCode = if (m.contains("pajero")) "1500A608" else "1500A513",
        cabinFilterCode = "7803A043",
        sparkCode = "1822A086",
        padCode = if (m.contains("pajero")) "4605A879" else "4605B005",
        rotorCode = "4615A178",
        batterySpec = if (m.contains("pajero")) "95D31L 12V Heavy-Duty" else "75D23L / 85D26L 12V",
      )
      b.contains("nissan") -> BrandOemSpec(
        oemBrand = "Nissan Genuine Parts",
        codePrefix = "NIS",
        oilVisc = "0W-20 API SP / Nissan Ester",
        oilFilterCode = "15208-65F0E",
        airFilterCode = "16546-4BA1A",
        cabinFilterCode = "27277-4BA0A",
        sparkCode = "22401-ED815",
        padCode = "D1060-4BA0A",
        rotorCode = "40206-4BA0A",
        batterySpec = "Q-85 EFB / 55B24L 12V",
      )
      b.contains("hyundai") || b.contains("kia") || b.contains("genesis") -> BrandOemSpec(
        oemBrand = "Hyundai Mobis OEM",
        codePrefix = "MOB",
        oilVisc = "0W-20 / 5W-30 ACEA C5",
        oilFilterCode = "26350-2M000",
        airFilterCode = "28113-L1000",
        cabinFilterCode = "97133-N9100",
        sparkCode = "18868-08115",
        padCode = "58101-N9A00",
        rotorCode = "51712-N9000",
        batterySpec = "DIN60L 12V 60Ah AGM",
      )
      b.contains("suzuki") || b.contains("maruti") -> BrandOemSpec(
        oemBrand = "Suzuki Genuine / MGP",
        codePrefix = "SGP",
        oilVisc = "0W-16 / 0W-20 Ecstar",
        oilFilterCode = "16510-M68K00",
        airFilterCode = "13780-M68P00",
        cabinFilterCode = "95860-M74L00",
        sparkCode = "09482-00634",
        padCode = "55810-M74L01",
        rotorCode = "55311-M74L00",
        batterySpec = "38B20L / 44B20L 12V",
      )
      b.contains("bmw") -> BrandOemSpec(
        oemBrand = "BMW Group Original",
        codePrefix = "BMW",
        oilVisc = "0W-20 LL-17 FE+",
        oilFilterCode = "11-42-8-575-211",
        airFilterCode = "13-71-8-577-170",
        cabinFilterCode = "64-11-9-382-886",
        sparkCode = "12-12-0-040-551",
        padCode = "34-11-6-889-570",
        rotorCode = "34-11-6-860-911",
        batterySpec = "H8 12V 90Ah AGM",
      )
      b.contains("volkswagen") || b.contains("audi") -> BrandOemSpec(
        oemBrand = "VW AG Genuine",
        codePrefix = "VAG",
        oilVisc = "0W-20 VW 508.00",
        oilFilterCode = "06L-115-562-B",
        airFilterCode = "5Q0-129-620-B",
        cabinFilterCode = "5Q0-819-653",
        sparkCode = "06K-905-601-M",
        padCode = "5Q0-698-151-AH",
        rotorCode = "5Q0-615-301-F",
        batterySpec = "H6 12V 70Ah EFB+",
      )
      b.contains("tata") || b.contains("mahindra") -> BrandOemSpec(
        oemBrand = "${brand.uppercase(Locale.US)} Genuine",
        codePrefix = "IND",
        oilVisc = "5W-30 ACEA C2/C3",
        oilFilterCode = "2790-1813-0102",
        airFilterCode = "5442-0913-0101",
        cabinFilterCode = "5438-8340-0104",
        sparkCode = "2791-1511-0103",
        padCode = "2702-4210-0118",
        rotorCode = "2702-4210-0104",
        batterySpec = "DIN55 12V 55Ah High-Temp",
      )
      b.contains("byd") -> BrandOemSpec(
        oemBrand = "BYD Auto Genuine",
        codePrefix = "BYD",
        oilVisc = "e-Axle Reduction Gear Oil",
        oilFilterCode = "SC2E-1701310",
        airFilterCode = "SC2E-8121210",
        cabinFilterCode = "HCE-8121230-PM25",
        sparkCode = "EM2E-2104100",
        padCode = "SC2E-3501110",
        rotorCode = "SC2E-3501075",
        batterySpec = "12V LFP Auxiliary 45Ah",
      )
      else -> BrandOemSpec(
        oemBrand = "$brand OEM Genuine",
        codePrefix = brand.take(3).uppercase(Locale.US),
        oilVisc = "0W-20 / 5W-30 Full Synthetic",
        oilFilterCode = "${brand.take(2).uppercase(Locale.US)}-1540-901",
        airFilterCode = "${brand.take(2).uppercase(Locale.US)}-1780-412",
        cabinFilterCode = "${brand.take(2).uppercase(Locale.US)}-8713-220",
        sparkCode = "${brand.take(2).uppercase(Locale.US)}-9091-771",
        padCode = "${brand.take(2).uppercase(Locale.US)}-4502-310",
        rotorCode = "${brand.take(2).uppercase(Locale.US)}-4351-608",
        batterySpec = "Group 48 H6 12V 70Ah AGM",
      )
    }
  }

  fun generateCatalogForVehicle(vehicle: UserVehicle?): List<CatalogPart> {
    val brand = vehicle?.brand ?: "Toyota"
    val model = vehicle?.model ?: "Corolla Axio"
    val year = vehicle?.year ?: 2022L
    val market = vehicle?.marketCode ?: "BD"
    val powertrain = Powertrain.fromRaw(vehicle?.powertrain)
    val spec = brandSpecFor(brand, model, powertrain)
    val fitment = "$year $brand $model (${vehicle?.variant ?: "Standard"}, $market)"
    val isEv = powertrain == Powertrain.EV
    val isDiesel = powertrain == Powertrain.DIESEL
    val isHybrid = powertrain == Powertrain.HYBRID

    val parts = mutableListOf<CatalogPart>()

    // 1. ENGINE (or Electric Drive Unit for EV)
    if (!isEv) {
      parts += CatalogPart(
        id = "eng_oil_filter",
        system = CatalogSystem.ENGINE,
        subSystem = "Lubrication",
        name = "Spin-On / Cartridge Engine Oil Filter",
        oemNumber = spec.oilFilterCode,
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "DENSO 150-2002 / VIC C-110",
        aftermarketBrand = "Denso / VIC Japan",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 48,
        priceUsd = 14.50,
        dispatchSla = "Same-day dispatch",
        specSummary = "20-micron synthetic media • Silicone anti-drainback valve",
        fitmentNotes = "Verified for $fitment. Torque to 18 Nm.",
        visualKind = PartVisualKind.FILTER_CANISTER,
        linkedTaskKey = "engine_oil_filter",
        keywords = listOf("oil", "filter", "lubrication", "service", "cartridge")
      )
      parts += CatalogPart(
        id = "eng_synthetic_oil",
        system = CatalogSystem.ENGINE,
        subSystem = "Lubrication",
        name = "Full Synthetic Motor Oil (${spec.oilVisc}) 5L",
        oemNumber = "${spec.codePrefix}-OIL-${if (isDiesel) "5W30C3" else "0W20SP"}",
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "MOBIL1-ESP-0W20-5L",
        aftermarketBrand = "Mobil 1 / Idemitsu",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 64,
        priceUsd = 46.00,
        dispatchSla = "Same-day dispatch",
        specSummary = if (isDiesel) "ACEA C3 Low-SAPS DPF Safe • 5 Liters" else "API SP / ILSAC GF-6A • 5 Liters",
        fitmentNotes = "Recommended capacity 4.2–5.2L for $fitment.",
        visualKind = PartVisualKind.FLUID_BOTTLE,
        linkedTaskKey = "engine_oil_filter",
        keywords = listOf("motor oil", "0w20", "0w16", "5w30", "synthetic", "fluid", "lubricant")
      )
      parts += CatalogPart(
        id = "eng_air_filter",
        system = CatalogSystem.ENGINE,
        subSystem = "Intake",
        name = "High-Flow Pleated Engine Air Filter",
        oemNumber = spec.airFilterCode,
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "DENSO A-1014 / MANN C 28 043",
        aftermarketBrand = "Denso / Mann-Filter",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 31,
        priceUsd = 24.90,
        dispatchSla = "Ships in 24h",
        specSummary = "Flame-retardant cellulose media • 99.5% dust retention ($market spec)",
        fitmentNotes = "Direct drop-in airbox element for $fitment.",
        visualKind = PartVisualKind.FILTER_CANISTER,
        linkedTaskKey = "engine_air_filter",
        keywords = listOf("air filter", "intake", "dust", "breather")
      )
      parts += CatalogPart(
        id = "eng_spark_or_glow",
        system = CatalogSystem.ENGINE,
        subSystem = if (isDiesel) "Cold-Start" else "Ignition",
        name = if (isDiesel) "Ceramic Quick-Heat Glow Plug Set (4x)" else "Laser Iridium Spark Plug Set (4x)",
        oemNumber = spec.sparkCode,
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = if (isDiesel) "NGK Y1030J" else "NGK DILKAR7G11GS / DENSO IXEH20TT",
        aftermarketBrand = "NGK / Denso",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 19,
        priceUsd = if (isDiesel) 98.00 else 68.00,
        dispatchSla = "Ships in 24h",
        specSummary = if (isDiesel) "11V Ceramic tip • 2.5s pre-heat" else "0.6mm laser-welded iridium center electrode • Pre-gapped 1.1mm",
        fitmentNotes = "Matched heat range for $fitment.",
        visualKind = PartVisualKind.SPARK_IGNITION,
        linkedTaskKey = "spark_plugs",
        keywords = listOf("spark", "plug", "glow", "iridium", "ignition", "misfire")
      )
      parts += CatalogPart(
        id = "eng_timing_belt",
        system = CatalogSystem.ENGINE,
        subSystem = "Accessory & Timing",
        name = "EPDM Multi-Ribbed Serpentine Drive Belt",
        oemNumber = "${spec.codePrefix}-6PK1890",
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "GATES K060744 / BANDO 6PK1890",
        aftermarketBrand = "Gates / Bando Japan",
        stockStatus = StockStatus.LOW_STOCK,
        warehouseQty = 6,
        priceUsd = 34.00,
        dispatchSla = "Ships in 24h",
        specSummary = "Aramid tensile cord • 6-rib 1890mm • Heat resistant to 140°C",
        fitmentNotes = "Inspect idler & tensioner pulleys during replacement.",
        visualKind = PartVisualKind.BELT_TIMING,
        linkedTaskKey = "drive_belt",
        keywords = listOf("belt", "serpentine", "timing", "alternator belt", "pulley")
      )
      parts += CatalogPart(
        id = "eng_water_pump",
        system = CatalogSystem.ENGINE,
        subSystem = "Cooling",
        name = if (isHybrid) "Electric Auxiliary Coolant Water Pump" else "Precision Impeller Coolant Water Pump + Gasket",
        oemNumber = "${spec.codePrefix}-161A0-29015",
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "AISIN WPT-190 / GMB",
        aftermarketBrand = "Aisin / Bosch",
        stockStatus = StockStatus.OEM_ORDER,
        warehouseQty = 4,
        priceUsd = 142.00,
        dispatchSla = "2–3 business days",
        specSummary = "Die-cast aluminum housing • Carbon-ceramic mechanical seal",
        fitmentNotes = "Includes OEM MLS gasket. Flush cooling circuit with silicate-free Super Long Life Coolant.",
        visualKind = PartVisualKind.SENSOR_ELECTRONIC,
        linkedTaskKey = "coolant_flush",
        keywords = listOf("water pump", "coolant", "thermostat", "overheat", "cooling")
      )
    } else {
      parts += CatalogPart(
        id = "eng_ev_drive_coolant",
        system = CatalogSystem.ENGINE,
        subSystem = "Thermal Management",
        name = "Low-Conductivity Battery & Inverter Coolant (4L)",
        oemNumber = "${spec.codePrefix}-BSC-EV01",
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "VALVOLINE EV-COOL-4L",
        aftermarketBrand = "Valvoline / Pentosin",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 27,
        priceUsd = 54.00,
        dispatchSla = "Same-day dispatch",
        specSummary = "Dielectric <5 µS/cm conductivity • Protects HV battery & IGBT inverter",
        fitmentNotes = "Mandatory dielectric specification for $fitment.",
        visualKind = PartVisualKind.FLUID_BOTTLE,
        linkedTaskKey = "coolant_flush",
        keywords = listOf("coolant", "ev", "battery", "inverter", "thermal")
      )
      parts += CatalogPart(
        id = "eng_ev_motor_mount",
        system = CatalogSystem.ENGINE,
        subSystem = "e-Drive Mounting",
        name = "Hydraulic Rear e-Axle Drive Unit Mount",
        oemNumber = "${spec.codePrefix}-21950-EV10",
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "LEMFÖRDER 43219 01",
        aftermarketBrand = "Lemförder",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 11,
        priceUsd = 118.00,
        dispatchSla = "Ships in 24h",
        specSummary = "High-damping elastomer core • Rated for instant torque load",
        fitmentNotes = "Verified fitment for $fitment.",
        visualKind = PartVisualKind.CONTROL_ARM,
        keywords = listOf("mount", "motor", "axle", "bushing", "vibration")
      )
    }

    // 2. TRANSMISSION
    parts += CatalogPart(
      id = "trn_fluid",
      system = CatalogSystem.TRANSMISSION,
      subSystem = "Transmission Fluids",
      name = when {
        isEv -> "Single-Speed e-Axle Reduction Gear Oil (2L)"
        isHybrid -> "Low-Viscosity eCVT / Hybrid Transaxle Fluid (4L)"
        else -> "Synthetic Multi-Vehicle ATF / CVT / DCT Fluid (4L)"
      },
      oemNumber = "${spec.codePrefix}-TF-${if (isEv) "75W" else "WS04"}",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "AISIN ATF-FE4 / MOTUL DCTF",
      aftermarketBrand = "Aisin / Motul",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 39,
      priceUsd = 58.50,
      dispatchSla = "Same-day dispatch",
      specSummary = "Shear-stable viscosity modifiers • High thermal oxidation stability",
      fitmentNotes = "Exact fluid spec for $fitment.",
      visualKind = PartVisualKind.FLUID_BOTTLE,
      linkedTaskKey = "transmission_fluid",
      keywords = listOf("transmission", "atf", "cvt", "dct", "gear oil", "fluid")
    )
    parts += CatalogPart(
      id = "trn_filter_kit",
      system = CatalogSystem.TRANSMISSION,
      subSystem = "Filtration & Pan",
      name = if (isEv) "e-Axle Magnetic Drain Plug & Crush Washer Kit" else "Transmission Strainer Filter & Pan Gasket Kit",
      oemNumber = "${spec.codePrefix}-35330-73010",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "MAHLE HX 184D",
      aftermarketBrand = "Mahle / Beck Arnley",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 16,
      priceUsd = 42.00,
      dispatchSla = "Ships in 24h",
      specSummary = "Fine stainless mesh screen • Molded Viton pan gasket included",
      fitmentNotes = "Recommended every transmission fluid exchange on $fitment.",
      visualKind = PartVisualKind.FILTER_CANISTER,
      linkedTaskKey = "transmission_fluid",
      keywords = listOf("transmission filter", "gasket", "strainer", "pan")
    )
    parts += CatalogPart(
      id = "trn_axle_boot",
      system = CatalogSystem.TRANSMISSION,
      subSystem = "Driveline & CV",
      name = "Front CV Axle Shaft Assembly (LH/RH)",
      oemNumber = "${spec.codePrefix}-43420-06A80",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "GKN LOBRO 306421 / NKN",
      aftermarketBrand = "GKN / NTN / NKN Japan",
      stockStatus = StockStatus.LOW_STOCK,
      warehouseQty = 5,
      priceUsd = 185.00,
      dispatchSla = "Ships in 24h",
      specSummary = "Induction-hardened constant velocity joints • Thermoplastic boots",
      fitmentNotes = "Includes new axle hub locknut.",
      visualKind = PartVisualKind.TRANSMISSION_GEAR,
      keywords = listOf("cv axle", "half shaft", "drivetrain", "boot", "clicking")
    )

    // 3. SUSPENSION
    parts += CatalogPart(
      id = "sus_front_strut",
      system = CatalogSystem.SUSPENSION,
      subSystem = "Damping",
      name = "Gas-Charged Twin-Tube Front Strut Assembly",
      oemNumber = "${spec.codePrefix}-48510-8Z310",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "KYB 339412 / BILSTEIN B4",
      aftermarketBrand = "KYB Japan / Bilstein",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 14,
      priceUsd = 132.00,
      dispatchSla = "Ships in 24h",
      specSummary = "Nitrogen gas pressurized • Multi-stage valving calibrated for $market roads",
      fitmentNotes = "Replace in axle pairs (LH + RH) and perform 4-wheel alignment.",
      visualKind = PartVisualKind.STRUT_COIL,
      linkedTaskKey = "suspension_inspection",
      keywords = listOf("strut", "shock", "absorber", "damper", "coilover", "bounce")
    )
    parts += CatalogPart(
      id = "sus_control_arm",
      system = CatalogSystem.SUSPENSION,
      subSystem = "Linkage",
      name = "Forged Aluminum Lower Control Arm w/ Ball Joint",
      oemNumber = "${spec.codePrefix}-48068-06200",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "SANKEI 555 SA-T292 / CTR",
      aftermarketBrand = "555 Japan / CTR",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 12,
      priceUsd = 114.50,
      dispatchSla = "Ships in 24h",
      specSummary = "Pre-pressed hydraulic compliance bushings • Dust-sealed ball stud",
      fitmentNotes = "Verified geometry for $fitment.",
      visualKind = PartVisualKind.CONTROL_ARM,
      linkedTaskKey = "suspension_inspection",
      keywords = listOf("control arm", "wishbone", "bushing", "ball joint", "clunk")
    )
    parts += CatalogPart(
      id = "sus_sway_link",
      system = CatalogSystem.SUSPENSION,
      subSystem = "Anti-Roll",
      name = "Heavy-Duty Stabilizer Sway Bar End Link Pair",
      oemNumber = "${spec.codePrefix}-48820-33100",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "555 SL-T220 / CTR CL0628",
      aftermarketBrand = "Sankei 555 / CTR",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 42,
      priceUsd = 38.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "Polymer low-friction ball seat • Corrosion-resistant zinc plating",
      fitmentNotes = "Sold as front left + right pair.",
      visualKind = PartVisualKind.CONTROL_ARM,
      keywords = listOf("sway bar", "stabilizer", "link", "rattle", "end link")
    )
    parts += CatalogPart(
      id = "sus_hub_bearing",
      system = CatalogSystem.SUSPENSION,
      subSystem = "Wheel End",
      name = "Gen-3 Sealed Wheel Hub & ABS Bearing Unit",
      oemNumber = "${spec.codePrefix}-43550-06080",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "KOYO 3DACF038D / SKF",
      aftermarketBrand = "Koyo Japan / SKF",
      stockStatus = StockStatus.OEM_ORDER,
      warehouseQty = 7,
      priceUsd = 126.00,
      dispatchSla = "2–3 business days",
      specSummary = "Integrated magnetic ABS encoder ring • Pre-lubed triple-lip seal",
      fitmentNotes = "Fits front axle hub for $fitment.",
      visualKind = PartVisualKind.TRANSMISSION_GEAR,
      keywords = listOf("wheel bearing", "hub", "hum", "abs ring")
    )

    // 4. ELECTRICAL
    parts += CatalogPart(
      id = "elc_battery",
      system = CatalogSystem.ELECTRICAL,
      subSystem = "Energy Storage",
      name = "12V Auxiliary Battery (${spec.batterySpec})",
      oemNumber = "${spec.codePrefix}-28800-31370",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "GS YUASA ECO.R / AMARON PRO",
      aftermarketBrand = "GS Yuasa / Amaron",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 22,
      priceUsd = 169.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "High cold-cranking reserve • Start-Stop & Hybrid/EV wake-up ready",
      fitmentNotes = "Check BMS reset procedure after installation.",
      visualKind = PartVisualKind.BATTERY_MODULE,
      linkedTaskKey = "battery_12v",
      keywords = listOf("battery", "12v", "agm", "efb", "start stop", "dead")
    )
    parts += CatalogPart(
      id = "elc_ignition_coil",
      system = CatalogSystem.ELECTRICAL,
      subSystem = if (isEv) "Thermal / HV Relay" else "Ignition & Charging",
      name = if (isEv) "HV Main Contactor Relay Assembly" else "Direct Ignition Coil-on-Plug Pack",
      oemNumber = "${spec.codePrefix}-90919-02276",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = if (isEv) "PANASONIC EV200" else "DENSO 673-1315",
      aftermarketBrand = if (isEv) "Panasonic" else "Denso / Bosch",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 28,
      priceUsd = if (isEv) 145.00 else 56.00,
      dispatchSla = "Ships in 24h",
      specSummary = if (isEv) "450V DC rated • Hermetic hydrogen arc suppression" else "High-dielectric epoxy insulation • 38kV peak output",
      fitmentNotes = "Matched OEM connector pinout for $fitment.",
      visualKind = PartVisualKind.SPARK_IGNITION,
      keywords = listOf("coil", "ignition", "relay", "voltage", "charging")
    )
    parts += CatalogPart(
      id = "elc_abs_sensor",
      system = CatalogSystem.ELECTRICAL,
      subSystem = "ADAS & Sensors",
      name = "Hall-Effect Wheel Speed / ABS Sensor",
      oemNumber = "${spec.codePrefix}-89542-06081",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "BOSCH 0 265 009 412",
      aftermarketBrand = "Bosch / Ate",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 35,
      priceUsd = 44.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "IP69K waterproof harness • Active direction-sensing Hall IC",
      fitmentNotes = "Front / Rear axle variants verified for $fitment.",
      visualKind = PartVisualKind.SENSOR_ELECTRONIC,
      keywords = listOf("abs", "sensor", "wheel speed", "esp", "traction")
    )
    parts += CatalogPart(
      id = "elc_headlight_led",
      system = CatalogSystem.ELECTRICAL,
      subSystem = "Lighting",
      name = "Bi-LED Headlight Driver Control Ballast Module",
      oemNumber = "${spec.codePrefix}-85967-06040",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "KOITO D4S-LED-CTRL",
      aftermarketBrand = "Koito / Hella",
      stockStatus = StockStatus.BACKORDER,
      warehouseQty = 0,
      priceUsd = 195.00,
      dispatchSla = "Restocking in 5 days",
      specSummary = "CAN-Bus adaptive leveling interface • 12V-24V regulated output",
      fitmentNotes = "Plug-and-play coding for $market lighting regulations.",
      visualKind = PartVisualKind.SENSOR_ELECTRONIC,
      keywords = listOf("headlight", "led", "ballast", "drl", "lamp")
    )

    // 5. BRAKES
    parts += CatalogPart(
      id = "brk_front_pads",
      system = CatalogSystem.BRAKES,
      subSystem = "Friction",
      name = "Low-Dust Ceramic Front Brake Pad Set (4-Pad)",
      oemNumber = spec.padCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "AKEBONO AN-742WK / BREMBO P83",
      aftermarketBrand = "Akebono Japan / Brembo",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 52,
      priceUsd = 64.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "Copper-free ceramic compound • Multi-layer anti-squeal shims included",
      fitmentNotes = "Includes abutment clips & wear indicators for $fitment.",
      visualKind = PartVisualKind.BRAKE_PAD,
      linkedTaskKey = "brake_pads_rotors",
      keywords = listOf("brake pad", "ceramic", "friction", "front", "squeak")
    )
    parts += CatalogPart(
      id = "brk_front_rotors",
      system = CatalogSystem.BRAKES,
      subSystem = "Rotors & Discs",
      name = "UV-Coated Vented Front Brake Rotor Disc (Pair)",
      oemNumber = spec.rotorCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "DIXCEL PD3119167 / BREMBO 09.C891",
      aftermarketBrand = "Dixcel / Brembo",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 20,
      priceUsd = 148.00,
      dispatchSla = "Ships in 24h",
      specSummary = "High-carbon cast iron • Geometric anti-corrosion UV hub coating",
      fitmentNotes = "Minimum discard thickness stamped on hat. Verified for $fitment.",
      visualKind = PartVisualKind.BRAKE_ROTOR,
      linkedTaskKey = "brake_pads_rotors",
      keywords = listOf("rotor", "disc", "brake", "warped", "vented")
    )
    parts += CatalogPart(
      id = "brk_dot4_fluid",
      system = CatalogSystem.BRAKES,
      subSystem = "Hydraulics",
      name = "DOT 4 LV Class-6 Synthetic Brake Fluid (1L)",
      oemNumber = "${spec.codePrefix}-08823-80112",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "SEIKEN BF4 / ATE SL.6 DOT 4",
      aftermarketBrand = "Seiken Japan / ATE",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 75,
      priceUsd = 19.50,
      dispatchSla = "Same-day dispatch",
      specSummary = "Dry boiling point 265°C • Low viscosity for fast ABS/AEB modulation",
      fitmentNotes = "Mandatory Class-6 LV spec for modern ESP/AEB systems.",
      visualKind = PartVisualKind.FLUID_BOTTLE,
      linkedTaskKey = "brake_fluid",
      keywords = listOf("brake fluid", "dot4", "bleed", "hydraulic", "abs")
    )
    parts += CatalogPart(
      id = "brk_caliper_kit",
      system = CatalogSystem.BRAKES,
      subSystem = "Calipers",
      name = "Electronic Parking Brake (EPB) Rear Caliper Actuator",
      oemNumber = "${spec.codePrefix}-46310-33010",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "ADVICS / TRW BHN1194E",
      aftermarketBrand = "Advics / TRW",
      stockStatus = StockStatus.LOW_STOCK,
      warehouseQty = 4,
      priceUsd = 158.00,
      dispatchSla = "Ships in 24h",
      specSummary = "12-spline planetary gear actuator • Weather-sealed EPDM boot",
      fitmentNotes = "Put EPB in service retract mode before installation.",
      visualKind = PartVisualKind.BRAKE_PAD,
      keywords = listOf("caliper", "epb", "parking brake", "actuator", "rear")
    )

    // 6. BODY
    parts += CatalogPart(
      id = "bdy_cabin_hepa",
      system = CatalogSystem.BODY,
      subSystem = "HVAC & Cabin",
      name = "Activated Carbon + HEPA PM2.5 Cabin Air Filter",
      oemNumber = spec.cabinFilterCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "DENSO DCC1014 / MANN FP 2141",
      aftermarketBrand = "Denso / Mann FreciousPlus",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 58,
      priceUsd = 26.50,
      dispatchSla = "Same-day dispatch",
      specSummary = "Captures 99.1% PM2.5 particulates & NOx urban odors ($market grade)",
      fitmentNotes = "2-minute glovebox tool-free install on $fitment.",
      visualKind = PartVisualKind.FILTER_CANISTER,
      linkedTaskKey = "cabin_filter",
      keywords = listOf("cabin filter", "ac filter", "hepa", "hvac", "odor")
    )
    parts += CatalogPart(
      id = "bdy_wiper_blades",
      system = CatalogSystem.BODY,
      subSystem = "Visibility",
      name = "Aero Beam Silicone Front Wiper Blade Set",
      oemNumber = "${spec.codePrefix}-85212-06230",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "NWB AERO / BOSCH AEROTWIN",
      aftermarketBrand = "NWB Japan / Bosch",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 46,
      priceUsd = 32.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "Hydrophobic graphite-coated rubber • Dual-spring tension beam",
      fitmentNotes = "Matched hook/push-button adapter for $market RHD/LHD sweep.",
      visualKind = PartVisualKind.BODY_WIPER,
      linkedTaskKey = "wiper_blades",
      keywords = listOf("wiper", "blade", "windshield", "rain", "visibility")
    )
    parts += CatalogPart(
      id = "bdy_hood_struts",
      system = CatalogSystem.BODY,
      subSystem = "Panels & Hardware",
      name = "Gas-Charged Hood / Tailgate Lift Support Pair",
      oemNumber = "${spec.codePrefix}-53440-06180",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "STABILUS LIFT-O-MAT 4B",
      aftermarketBrand = "Stabilus",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 18,
      priceUsd = 48.00,
      dispatchSla = "Ships in 24h",
      specSummary = "Nitride-coated piston rod • 380N calibrated lift force",
      fitmentNotes = "Direct ball-stud clip replacement.",
      visualKind = PartVisualKind.STRUT_COIL,
      keywords = listOf("hood strut", "tailgate", "lift support", "gas spring")
    )
    parts += CatalogPart(
      id = "bdy_radiator_grille",
      system = CatalogSystem.BODY,
      subSystem = "Front Fascia",
      name = "Active Aero Lower Bumper Grille & Radar Bracket",
      oemNumber = "${spec.codePrefix}-53112-06410",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "TYC 12-5422-00",
      aftermarketBrand = "TYC / Tong Yang",
      stockStatus = StockStatus.OEM_ORDER,
      warehouseQty = 3,
      priceUsd = 165.00,
      dispatchSla = "3–4 business days",
      specSummary = "ADAS millimeter-wave radar transparent ABS polymer",
      fitmentNotes = "Radar calibration check advised after front fascia swap.",
      visualKind = PartVisualKind.BODY_WIPER,
      keywords = listOf("grille", "bumper", "fascia", "radar", "body")
    )

    return parts
  }
}
