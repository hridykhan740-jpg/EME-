package com.example.data

import com.example.model.CatalogPart
import com.example.model.CatalogSystem
import com.example.model.PartSubComponent
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
        append(part.epcCatalogRef).append(' ')
        append(part.system.displayName).append(' ')
        append(part.subSystem).append(' ')
        append(part.specSummary).append(' ')
        part.keywords.forEach { append(it).append(' ') }
        part.subComponents.forEach { sub ->
          append(sub.name).append(' ')
          append(sub.oemNumber).append(' ')
          append(sub.aftermarketNumber).append(' ')
          append(sub.specNote).append(' ')
        }
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

  private val catalogCache = java.util.concurrent.ConcurrentHashMap<String, List<CatalogPart>>()

  fun generateCatalogForVehicle(vehicle: UserVehicle?): List<CatalogPart> {
    val brand = vehicle?.brand ?: "Toyota"
    val model = vehicle?.model ?: "Corolla Axio"
    val year = vehicle?.year ?: 2022L
    val market = vehicle?.marketCode ?: "BD"
    val variant = vehicle?.variant ?: "Standard"
    val powertrain = Powertrain.fromRaw(vehicle?.powertrain)
    val cacheKey = "$brand|$model|$year|$market|$variant|${powertrain.name}"
    catalogCache[cacheKey]?.let { return it }

    val spec = BrandOemProfiles.specFor(brand, model, powertrain)
    val fitment = "$year $brand $model ($variant, $market)"
    val isEv = powertrain == Powertrain.EV
    val isDiesel = powertrain == Powertrain.DIESEL
    val isHybrid = powertrain == Powertrain.HYBRID
    val p = spec.codePrefix

    val parts = mutableListOf<CatalogPart>()

    // 1. ENGINE
    if (!isEv) {
      parts += CatalogPart(
        id = "eng_oil_filter",
        system = CatalogSystem.ENGINE,
        subSystem = "Lubrication Assembly",
        name = "Engine Oil Filter Assembly (Complete Kit)",
        oemNumber = spec.oilFilterCode,
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "DENSO 150-2002 / VIC C-110",
        aftermarketBrand = "Denso / VIC Japan",
        epcCatalogRef = "${spec.epcCatalogName} • Fig. 15-02 Lubrication",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 48,
        priceUsd = 14.50,
        dispatchSla = "Same-day dispatch",
        specSummary = "20-micron pleated synthetic media • Complete housing, O-ring & drain gasket kit",
        fitmentNotes = "Verified for $fitment. Torque canister/housing cap to 18–25 Nm.",
        visualKind = PartVisualKind.FILTER_CANISTER,
        linkedTaskKey = "engine_oil_filter",
        keywords = listOf("oil", "filter", "lubrication", "o-ring", "crush washer", "housing cap"),
        subComponents = listOf(
          PartSubComponent(
            id = "sub_oil_element",
            name = "Filter Element / Pleated Cartridge",
            oemNumber = "${spec.oilFilterCode}-ELM",
            aftermarketNumber = "VIC O-119 / DENSO 150-2002",
            quantity = "1 pc",
            specNote = "20-micron synthetic-cellulose high-flow pleats",
            priceUsd = 7.50,
          ),
          PartSubComponent(
            id = "sub_oil_oring",
            name = "Viton Housing Rubber O-Ring / Seal",
            oemNumber = "$p-90301-68005",
            aftermarketNumber = "NOK Viton Seal 68mm",
            quantity = "1 pc",
            specNote = "Pre-lubricate with fresh motor oil before seating",
            priceUsd = 2.20,
          ),
          PartSubComponent(
            id = "sub_drain_washer",
            name = "Oil Pan Drain Plug Crush Washer / Gasket",
            oemNumber = "$p-90430-12031",
            aftermarketNumber = "BLUE-OEM 12mm Fiber/Alu",
            quantity = "1 pc",
            specNote = "Single-use 12mm crush ring • Torque drain plug 37 Nm",
            priceUsd = 1.25,
          ),
          PartSubComponent(
            id = "sub_oil_cap",
            name = "Composite / Alloy Oil Filter Housing Cap",
            oemNumber = "$p-15620-37010",
            aftermarketNumber = "AISIN OFH-001",
            quantity = "1 pc",
            specNote = "14-flute 64mm wrench interface • High-temp PA66-GF30",
            priceUsd = 18.00,
            stockStatus = StockStatus.LOW_STOCK,
          ),
          PartSubComponent(
            id = "sub_oil_valve",
            name = "Center Tube & Bypass Relief Valve Spring",
            oemNumber = "$p-15609-21020",
            aftermarketNumber = "OEM-SPEC 100kPa Valve",
            quantity = "1 pc",
            specNote = "1.0 bar cold-start bypass calibrated valve",
            priceUsd = 4.90,
          ),
        ),
      )

      parts += CatalogPart(
        id = "eng_synthetic_oil",
        system = CatalogSystem.ENGINE,
        subSystem = "Lubrication Fluids",
        name = "Full Synthetic Motor Oil (${spec.oilVisc}) 5L",
        oemNumber = "$p-OIL-${if (isDiesel) "5W30C3" else "0W20SP"}",
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "MOBIL1-ESP-5L / IDEMITSU",
        aftermarketBrand = "Mobil 1 / Idemitsu",
        epcCatalogRef = "${spec.epcCatalogName} • Fluid Spec Sheet",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 64,
        priceUsd = 46.00,
        dispatchSla = "Same-day dispatch",
        specSummary = if (isDiesel) "ACEA C3 / CI-4+ Low-SAPS DPF & EGR Safe" else "API SP / ILSAC GF-6A LSPI protection",
        fitmentNotes = "Recommended sump capacity verified for $fitment.",
        visualKind = PartVisualKind.FLUID_BOTTLE,
        linkedTaskKey = "engine_oil_filter",
        keywords = listOf("motor oil", "0w20", "0w16", "5w30", "15w40", "lubricant"),
        subComponents = listOf(
          PartSubComponent("sub_oil_4l", "4L Primary Jug (${spec.oilVisc})", "$p-OIL-4L", "MOBIL1-4L", "1 jug", "API SP / ACEA C3 base fill", 37.00),
          PartSubComponent("sub_oil_1l", "1L Top-Up Bottle (${spec.oilVisc})", "$p-OIL-1L", "MOBIL1-1L", "1 btl", "For sump capacity >4.0L", 9.80),
          PartSubComponent("sub_drain_bolt", "Magnetic Sump Drain Plug M12x1.25", "$p-90341-12012", "GOLDPLUG MP-01", "1 pc", "Neodymium core captures ferrous wear", 8.50),
          PartSubComponent("sub_filler_cap", "710 / Engine Oil Filler Cap + Seal", "$p-12180-21021", "NOK CAP-SEAL", "1 pc", "Prevents PCV vacuum leaks", 11.00),
        ),
      )

      parts += CatalogPart(
        id = "eng_timing_belt",
        system = CatalogSystem.ENGINE,
        subSystem = "Timing & Accessory Drive",
        name = "Timing Chain / Serpentine Drive Belt Kit",
        oemNumber = "$p-6PK1890",
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "GATES K060744 / BANDO",
        aftermarketBrand = "Gates / Bando Japan",
        epcCatalogRef = "${spec.epcCatalogName} • Fig. 13-02 Valve Train",
        stockStatus = StockStatus.LOW_STOCK,
        warehouseQty = 9,
        priceUsd = 68.00,
        dispatchSla = "Ships in 24h",
        specSummary = "Aramid EPDM 6-rib belt + hydraulic tensioner + idler bearings",
        fitmentNotes = "Replace belt, tensioner, and idler bearings together on $fitment.",
        visualKind = PartVisualKind.BELT_TIMING,
        linkedTaskKey = "drive_belt",
        keywords = listOf("belt", "timing", "serpentine", "tensioner", "idler", "pulley"),
        subComponents = listOf(
          PartSubComponent("sub_belt_rib", "EPDM 6-Rib Multi-V Serpentine Belt", "$p-90916-02664", "BANDO 6PK1890", "1 pc", "Heat-resistant to 140°C", 24.00),
          PartSubComponent("sub_belt_tens", "Automatic Belt Tensioner Assembly", "$p-16620-37011", "INA 534041510", "1 pc", "Spring-damped pivot arm", 42.00),
          PartSubComponent("sub_belt_idler", "Smooth Steel Idler Bearing Pulley", "$p-16603-37010", "NSK 6203DDU", "1 pc", "High-speed Japanese ball bearing", 18.50),
          PartSubComponent("sub_crank_seal", "Front Crankshaft Viton Oil Seal", "$p-90311-38090", "NOK BH5602E", "1 pc", "Triple-lip dust & oil barrier", 7.80),
        ),
      )

      parts += CatalogPart(
        id = "eng_spark_or_glow",
        system = CatalogSystem.ENGINE,
        subSystem = if (isDiesel) "Diesel Cold-Start" else "Ignition",
        name = if (isDiesel) "Ceramic Quick-Heat Glow Plug Kit (4x)" else "Laser Iridium Spark Plug Kit (4x)",
        oemNumber = spec.sparkCode,
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = if (isDiesel) "NGK Y1030J" else "NGK DILKAR7G11GS / DENSO IXEH20TT",
        aftermarketBrand = "NGK / Denso Japan",
        epcCatalogRef = "${spec.epcCatalogName} • Fig. 19-01 Ignition",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 24,
        priceUsd = if (isDiesel) 98.00 else 68.00,
        dispatchSla = "Ships in 24h",
        specSummary = if (isDiesel) "11V dual-coil ceramic glow tip" else "0.6mm laser-iridium tip • Pre-gapped 1.1mm",
        fitmentNotes = "Matched thermal heat range for $fitment.",
        visualKind = PartVisualKind.SPARK_IGNITION,
        linkedTaskKey = "spark_plugs",
        keywords = listOf("spark", "plug", "glow", "iridium", "coil boot", "gasket"),
        subComponents = listOf(
          PartSubComponent("sub_plug_core", if (isDiesel) "Glow Plug Heating Element (x4)" else "Laser Iridium Plug Set (x4)", spec.sparkCode, "NGK / DENSO 4PC", "4 pcs", "Pre-gapped factory calibration", 58.00),
          PartSubComponent("sub_coil_boot", "Silicone Dielectric Ignition Coil Boot Set", "$p-90919-11009", "DENSO BOOT-4", "4 pcs", "Prevents high-voltage carbon tracking", 14.00),
          PartSubComponent("sub_valve_gasket", "Valve Cover & Spark Tube Seal Kit", "$p-11213-21020", "STONE VG-110", "1 set", "Eliminates oil pooling in plug wells", 16.50),
          PartSubComponent("sub_pcv_valve", "Crankcase PCV Breather Valve", "$p-12204-21011", "AISIN PCV-01", "1 pc", "Regulates blow-by crankcase pressure", 9.50),
        ),
      )
    } else {
      parts += CatalogPart(
        id = "eng_ev_motor_mount",
        system = CatalogSystem.ENGINE,
        subSystem = "e-Drive Mounts",
        name = "Hydraulic e-Axle Drive Unit Mount Assembly",
        oemNumber = "$p-21950-EV10",
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "LEMFÖRDER 43219 01",
        aftermarketBrand = "Lemförder",
        epcCatalogRef = "${spec.epcCatalogName} • Fig. 11-04 e-Drive",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 11,
        priceUsd = 118.00,
        dispatchSla = "Ships in 24h",
        specSummary = "High-damping elastomer mount rated for instant electric torque",
        fitmentNotes = "Verified fitment for $fitment.",
        visualKind = PartVisualKind.CONTROL_ARM,
        keywords = listOf("ev", "motor mount", "bushing", "bracket"),
        subComponents = listOf(
          PartSubComponent("sub_ev_core", "Hydro-Elastomer Mount Core", "$p-21950-EV10-A", "LEM 43219-C", "1 pc", "Shore 65A damping compound", 82.00),
          PartSubComponent("sub_ev_bolts", "High-Tensile M12 Flange Bolt Kit (4x)", "$p-90119-12311", "OEM 10.9 GRADE", "4 pcs", "Torque to 88 Nm", 12.00),
        ),
      )
    }

    // 2. FUEL & AIR
    parts += CatalogPart(
      id = "ful_air_filter",
      system = CatalogSystem.FUEL_AIR,
      subSystem = "Air Intake Filtration",
      name = "Engine Air Filter & Intake Box Assembly",
      oemNumber = spec.airFilterCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "DENSO A-1014 / MANN C 28 043",
      aftermarketBrand = "Denso / Mann-Filter",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 17-03 Air Cleaner",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 34,
      priceUsd = 24.90,
      dispatchSla = "Same-day dispatch",
      specSummary = "99.5% dust retention calibrated for dusty urban roads in $market",
      fitmentNotes = "Inspect MAF sensor & intake hose clamps during replacement.",
      visualKind = PartVisualKind.FILTER_CANISTER,
      linkedTaskKey = "engine_air_filter",
      keywords = listOf("air filter", "intake", "maf", "airbox", "seal"),
      subComponents = listOf(
        PartSubComponent("sub_air_elem", "Pleated Cellulose Air Filter Element", spec.airFilterCode, "MANN C 28 043", "1 pc", "Flame-retardant dust media", 19.50),
        PartSubComponent("sub_air_gasket", "Polyurethane Airbox Perimeter Seal", "$p-17816-21010", "OEM FOAM SEAL", "1 pc", "Prevents unfiltered bypass", 4.20),
        PartSubComponent("sub_maf_sensor", "Hot-Wire Mass Air Flow (MAF) Sensor", "$p-22204-22010", "DENSO 197-6030", "1 pc", "Platinum thermistor element", 64.00, StockStatus.LOW_STOCK),
        PartSubComponent("sub_air_clips", "Spring Steel Airbox Latch Clips (Pair)", "$p-17751-21030", "OEM CLIP-2X", "2 pcs", "Stainless tension spring", 5.50),
      ),
    )

    if (!isEv) {
      parts += CatalogPart(
        id = "ful_fuel_pump_filter",
        system = CatalogSystem.FUEL_AIR,
        subSystem = "Fuel Delivery & Filtration",
        name = if (isDiesel) "Diesel Fuel Water-Separator Filter Assembly" else "In-Tank Fuel Pump & Filter Module",
        oemNumber = spec.fuelFilterCode,
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = if (isDiesel) "DENSO 186100-6280 / RACOR" else "DENSO 950-0115",
        aftermarketBrand = "Denso / Bosch / Delphi",
        epcCatalogRef = "${spec.epcCatalogName} • Fig. 22-11 Fuel System",
        stockStatus = StockStatus.IN_STOCK,
        warehouseQty = 18,
        priceUsd = if (isDiesel) 48.00 else 112.00,
        dispatchSla = "Ships in 24h",
        specSummary = "2-micron fine fuel filtration • Ethanol & regional fuel sulfur resistant",
        fitmentNotes = "Critical protection for GDI/CRDi injectors in $fitment.",
        visualKind = PartVisualKind.FILTER_CANISTER,
        linkedTaskKey = "fuel_filter_service",
        keywords = listOf("fuel filter", "fuel pump", "strainer", "regulator", "injector", "diesel separator"),
        subComponents = listOf(
          PartSubComponent("sub_fuel_cart", if (isDiesel) "Diesel 2-Micron Filter Cartridge" else "High-Pressure Fuel Filter Housing", spec.fuelFilterCode, "DENSO 186100", "1 pc", "Water-repelling synthetic layers", 28.00),
          PartSubComponent("sub_fuel_strainer", "In-Tank Fuel Pump Sock / Pre-Strainer", "$p-23217-21020", "AIRTEX FS220", "1 pc", "70-micron nylon sediment sock", 8.40),
          PartSubComponent("sub_fuel_reg", "Fuel Pressure Regulator & Viton O-Rings", "$p-23280-21010", "DENSO FPR-01", "1 kit", "Maintains 3.2–4.0 bar rail pressure", 24.00),
          PartSubComponent("sub_fuel_tank_seal", "Tank Sender Lock-Ring & Rubber Gasket", "$p-77169-52040", "VITON TANK-RING", "1 pc", "Vapor-tight EPDM fuel seal", 9.60),
        ),
      )
    }

    // 3. TRANSMISSION
    parts += CatalogPart(
      id = "trn_fluid",
      system = CatalogSystem.TRANSMISSION,
      subSystem = "Transmission Fluids",
      name = when {
        isEv -> "Single-Speed e-Axle Reduction Gear Oil (2L)"
        isHybrid -> "Low-Viscosity eCVT / Hybrid Transaxle Fluid (4L)"
        else -> "Synthetic Multi-Vehicle ATF / CVT / DCT Fluid (4L)"
      },
      oemNumber = "$p-TF-${if (isEv) "75W" else "WS04"}",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "AISIN ATF-FE4 / MOTUL DCTF",
      aftermarketBrand = "Aisin / Motul",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 35-01 Transaxle",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 39,
      priceUsd = 58.50,
      dispatchSla = "Same-day dispatch",
      specSummary = "Shear-stable viscosity modifiers • Thermal oxidation protection",
      fitmentNotes = "Exact transmission fluid spec for $fitment.",
      visualKind = PartVisualKind.FLUID_BOTTLE,
      linkedTaskKey = "transmission_fluid",
      keywords = listOf("transmission", "atf", "cvt", "dct", "ecvt", "gear oil"),
      subComponents = listOf(
        PartSubComponent("sub_trn_4l", "4L OEM Transmission Fluid Tin", "$p-TF-WS04", "AISIN AFW+ 4L", "1 tin", "Exact OEM friction coefficient", 48.00),
        PartSubComponent("sub_trn_drain", "Transmission Drain & Overflow Plug Washer Set", "$p-90430-18008", "OEM ALU-18MM", "2 pcs", "18mm anodized crush washers", 3.50),
        PartSubComponent("sub_trn_cooler", "Transaxle Oil Cooler Line O-Ring Kit", "$p-90301-11022", "VITON COOL-2X", "2 pcs", "High-temp fluoroelastomer", 5.20),
      ),
    )

    parts += CatalogPart(
      id = "trn_filter_kit",
      system = CatalogSystem.TRANSMISSION,
      subSystem = "Strainer & Pan Assembly",
      name = if (isEv) "e-Axle Magnetic Plug & Seal Kit" else "Transmission Oil Strainer Filter & Pan Kit",
      oemNumber = "$p-35330-73010",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "MAHLE HX 184D / JS ASAKASHI",
      aftermarketBrand = "Mahle / Asakashi Japan",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 35-03 Valve Body",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 16,
      priceUsd = 42.00,
      dispatchSla = "Ships in 24h",
      specSummary = "Fine stainless strainer mesh + molded Viton pan gasket + magnet",
      fitmentNotes = "Recommended every transmission oil change on $fitment.",
      visualKind = PartVisualKind.FILTER_CANISTER,
      linkedTaskKey = "transmission_fluid",
      keywords = listOf("transmission filter", "strainer", "pan gasket", "valve body"),
      subComponents = listOf(
        PartSubComponent("sub_trn_mesh", "Internal Transmission Strainer Screen", "$p-35330-73010", "ASAKASHI JT495", "1 pc", "Stainless steel weave media", 26.00),
        PartSubComponent("sub_trn_pan_gsk", "Molded Viton Oil Pan Gasket", "$p-35168-21011", "CORTECO PAN-GSK", "1 pc", "Zero-leak molded bead", 12.00),
        PartSubComponent("sub_trn_mag", "Pan Ferrous Chip Catcher Magnet (Pair)", "$p-35107-21010", "OEM MAG-2PC", "2 pcs", "Captures clutch & gear fines", 6.00),
        PartSubComponent("sub_trn_axle_seal", "Output Shaft Differential Oil Seal", "$p-90311-34043", "NOK DIFF-SEAL", "1 pc", "Spring-loaded Viton lip", 9.40),
      ),
    )

    // 4. CLUTCH
    parts += CatalogPart(
      id = "clt_complete_kit",
      system = CatalogSystem.CLUTCH,
      subSystem = "Clutch & Flywheel",
      name = if (isHybrid || isEv) "Hybrid Damper / Torque Limiter Clutch Kit" else "3-Piece Clutch Cover, Disc & Release Bearing Kit",
      oemNumber = spec.clutchKitCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "AISIN KT-318 / EXEDY / LUK",
      aftermarketBrand = "Aisin / Exedy Japan / LuK",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 31-01 Clutch Assembly",
      stockStatus = StockStatus.OEM_ORDER,
      warehouseQty = 6,
      priceUsd = 198.00,
      dispatchSla = "1–2 business days",
      specSummary = "High-burst organic friction lining • Diaphragm pressure plate + concentric bearing",
      fitmentNotes = "Inspect flywheel surface & rear main seal during service on $fitment.",
      visualKind = PartVisualKind.CLUTCH_DISC,
      linkedTaskKey = "clutch_hydraulics",
      keywords = listOf("clutch", "pressure plate", "flywheel", "release bearing", "slave cylinder"),
      subComponents = listOf(
        PartSubComponent("sub_clt_disc", "Torsion-Damped Clutch Friction Disc", "${spec.clutchKitCode}-DSC", "EXEDY TYD139U", "1 pc", "Copper-weave organic friction facings", 78.00),
        PartSubComponent("sub_clt_cover", "Diaphragm Spring Clutch Pressure Plate", "${spec.clutchKitCode}-COV", "AISIN CTX-091", "1 pc", "Heat-treated nodular iron cover", 84.00),
        PartSubComponent("sub_clt_rel", "Hydraulic Concentric / Throwout Bearing", "$p-31230-12191", "KOYO RCT336SA", "1 pc", "Precision-sealed Japanese bearing", 34.00),
        PartSubComponent("sub_clt_pilot", "Flywheel Spigot / Pilot Ball Bearing", "$p-90363-12002", "NSK 6201DDU", "1 pc", "Input shaft alignment bearing", 9.50),
        PartSubComponent("sub_clt_slave", "Clutch Slave / Master Cylinder Seal Kit", "$p-31470-12140", "SEIKEN SK4412", "1 kit", "EPDM hydraulic piston cups", 22.00),
      ),
    )

    // 5. SUSPENSION
    parts += CatalogPart(
      id = "sus_front_strut",
      system = CatalogSystem.SUSPENSION,
      subSystem = "MacPherson Struts & Damping",
      name = "Gas-Charged Front Strut & Top Mount Assembly",
      oemNumber = "$p-48510-8Z310",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "KYB 339412 / BILSTEIN B4",
      aftermarketBrand = "KYB Japan / Bilstein",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 48-03 Front Spring & Shock",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 14,
      priceUsd = 132.00,
      dispatchSla = "Ships in 24h",
      specSummary = "Nitrogen pressurized twin-tube • Calibrated for rough urban roads in $market",
      fitmentNotes = "Replace in axle pairs and align toe/camber on $fitment.",
      visualKind = PartVisualKind.STRUT_COIL,
      linkedTaskKey = "suspension_inspection",
      keywords = listOf("strut", "shock", "absorber", "top mount", "coil spring", "bump stop"),
      subComponents = listOf(
        PartSubComponent("sub_str_cart", "Gas-Pressurized Shock Absorber Cartridge", "$p-48510-8Z310", "KYB 339412", "1 pc", "Multi-stage velocity-sensitive valve", 88.00),
        PartSubComponent("sub_str_mount", "Upper Strut Rubber Mount & Bearing", "$p-48609-12570", "KYB SM5740", "1 pc", "Bonded natural rubber isolator", 29.00),
        PartSubComponent("sub_str_boot", "EPDM Dust Boot & Polyurethane Bump Stop", "$p-48331-12260", "SACHS 900326", "1 kit", "Protects chrome piston rod from grit", 14.00),
        PartSubComponent("sub_str_ins", "Lower Coil Spring Rubber Seat Insulator", "$p-48158-12120", "OEM SEAT-PAD", "1 pc", "Eliminates metal-on-metal creak", 7.50),
      ),
    )

    parts += CatalogPart(
      id = "sus_control_arm",
      system = CatalogSystem.SUSPENSION,
      subSystem = "Lower Control Linkage",
      name = "Forged Lower Control Arm & Ball Joint Assembly",
      oemNumber = "$p-48068-06200",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "SANKEI 555 SA-T292 / CTR",
      aftermarketBrand = "555 Japan / CTR",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 48-02 Front Axle Arm",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 12,
      priceUsd = 114.50,
      dispatchSla = "Ships in 24h",
      specSummary = "Pre-pressed compliance bushings + dust-sealed lower ball joint",
      fitmentNotes = "Torque compliance bushing at curb ride height on $fitment.",
      visualKind = PartVisualKind.CONTROL_ARM,
      linkedTaskKey = "suspension_inspection",
      keywords = listOf("control arm", "wishbone", "bushing", "ball joint", "sway link"),
      subComponents = listOf(
        PartSubComponent("sub_arm_body", "Stamped / Forged Lower Arm Frame", "$p-48068-06200", "CTR CQ0265", "1 pc", "Cataphoretic anti-rust E-coat", 76.00),
        PartSubComponent("sub_arm_bj", "Lower Steering Knuckle Ball Joint", "$p-43330-19245", "555 SB-3922", "1 pc", "Induction-hardened ball stud", 24.00),
        PartSubComponent("sub_arm_bush", "Vertical Hydraulic Compliance Bushing", "$p-48655-12170", "FEBEST TAB-447", "1 pc", "High-tear resistance elastomer", 18.00),
        PartSubComponent("sub_sway_link", "Stabilizer Sway Bar End Link", "$p-48820-33100", "555 SL-T220", "1 pc", "Low-friction polymer ball seat", 16.50),
      ),
    )

    // 6. STEERING
    parts += CatalogPart(
      id = "str_rack_assembly",
      system = CatalogSystem.STEERING,
      subSystem = "Rack & Tie-Rod Linkage",
      name = "EPS / Hydraulic Steering Rack & Tie-Rod Assembly",
      oemNumber = spec.steeringRackCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "JTEKT / TRW JRM602",
      aftermarketBrand = "JTEKT Japan / TRW",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 45-05 Steering Gear",
      stockStatus = StockStatus.OEM_ORDER,
      warehouseQty = 5,
      priceUsd = 310.00,
      dispatchSla = "2–3 business days",
      specSummary = "Zero-backlash rack gear + inner rack ends + outer tie-rod ends + bellows",
      fitmentNotes = "Perform steering angle sensor zero-point calibration after fitment on $fitment.",
      visualKind = PartVisualKind.STEERING_RACK,
      linkedTaskKey = "steering_rack_audit",
      keywords = listOf("steering", "rack", "tie rod", "rack end", "boot", "eps"),
      subComponents = listOf(
        PartSubComponent("sub_str_outer", "Outer Tie-Rod End Ball Joint (LH/RH)", "$p-45046-19415", "555 SE-3921", "1 pair", "Pre-greased CR boot + castle nut", 32.00),
        PartSubComponent("sub_str_inner", "Inner Steering Rack End Axial Joint", "$p-45503-19255", "555 SR-3920", "1 pair", "Heavy-duty axial socket joint", 38.00),
        PartSubComponent("sub_str_boot", "Thermoplastic Rack Bellows Boot + Clamps", "$p-45535-12100", "OHNO FB-2142", "1 pair", "Ozone & road-debris resistant", 14.50),
        PartSubComponent("sub_str_mount", "Steering Rack Subframe Mounting Bushing", "$p-45516-12080", "OEM STEER-BUSH", "2 pcs", "Eliminates steering play & knock", 16.00),
      ),
    )

    // 7. BRAKES
    parts += CatalogPart(
      id = "brk_front_pads",
      system = CatalogSystem.BRAKES,
      subSystem = "Front Friction & Hardware",
      name = "Low-Dust Ceramic Front Brake Pad & Clip Kit",
      oemNumber = spec.padCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "AKEBONO AN-742WK / BREMBO P83",
      aftermarketBrand = "Akebono Japan / Brembo",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 47-05 Front Disc Brake",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 52,
      priceUsd = 64.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "Copper-free ceramic friction + stainless abutment clips + anti-squeal shims",
      fitmentNotes = "Clean and lubricate caliper slide pins with silicone grease on $fitment.",
      visualKind = PartVisualKind.BRAKE_PAD,
      linkedTaskKey = "brake_pads_rotors",
      keywords = listOf("brake pad", "ceramic", "shim", "clip", "caliper pin"),
      subComponents = listOf(
        PartSubComponent("sub_pad_4pc", "4-Piece Ceramic Brake Pad Set", spec.padCode, "AKEBONO AN-742WK", "4 pads", "Copper-free low-dust formulation", 48.00),
        PartSubComponent("sub_pad_shims", "Multi-Layer Stainless Anti-Squeal Shim Kit", "$p-04945-52150", "OEM SHIM-KIT", "1 set", "Dampens high-frequency brake squeal", 12.00),
        PartSubComponent("sub_pad_clips", "Stainless Caliper Abutment Hardware Clips", "$p-04947-52010", "ADVICS CLIP-4X", "4 pcs", "Ensures smooth pad retraction", 8.50),
        PartSubComponent("sub_caliper_pin", "Caliper Slide Pin & EPDM Boot Rebuild Kit", "$p-04479-52040", "SEIKEN SP-A412", "1 kit", "Prevents uneven inner/outer pad wear", 15.00),
      ),
    )

    parts += CatalogPart(
      id = "brk_front_rotors",
      system = CatalogSystem.BRAKES,
      subSystem = "Rotors & Hydraulics",
      name = "UV-Coated Vented Brake Rotor & DOT 4 Kit",
      oemNumber = spec.rotorCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "DIXCEL PD3119167 / SEIKEN BF4",
      aftermarketBrand = "Dixcel / Seiken Japan",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 47-06 Brake Disc",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 20,
      priceUsd = 148.00,
      dispatchSla = "Ships in 24h",
      specSummary = "High-carbon vented discs + DOT 4 LV Class-6 fluid + bleeder caps",
      fitmentNotes = "Check hub face runout <0.05mm before rotor mounting on $fitment.",
      visualKind = PartVisualKind.BRAKE_ROTOR,
      linkedTaskKey = "brake_fluid",
      keywords = listOf("rotor", "disc", "brake fluid", "dot4", "hose", "bleeder"),
      subComponents = listOf(
        PartSubComponent("sub_rot_pair", "Vented High-Carbon Rotor Disc Pair", spec.rotorCode, "DIXCEL PD-PAIR", "2 pcs", "Geomet UV anti-rust hub coating", 124.00),
        PartSubComponent("sub_brk_fluid", "DOT 4 LV Class-6 Synthetic Fluid (1L)", "$p-08823-80112", "SEIKEN BF4 1L", "1 btl", "265°C dry boiling point for ABS/VSC", 16.50),
        PartSubComponent("sub_brk_hose", "Braided EPDM Front Brake Flex Hose", "$p-90947-02F21", "MIYACO BH-T512", "1 pc", "Zero volumetric expansion under panic stop", 18.00),
        PartSubComponent("sub_brk_ret", "Rotor Retaining Countersunk Screws (2x)", "$p-90148-80021", "OEM M8-TORX", "2 pcs", "Stainless anti-seize coated", 3.20),
      ),
    )

    // 8. COOLING
    parts += CatalogPart(
      id = "col_radiator_assembly",
      system = CatalogSystem.COOLING,
      subSystem = "Radiator, Thermostat & Pump",
      name = "Brazed Aluminum Radiator & Thermostat Cooling Kit",
      oemNumber = spec.radiatorCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "DENSO 221-3142 / KOYORAD",
      aftermarketBrand = "Denso / Koyorad Japan",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 16-03 Radiator & Water Outlet",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 11,
      priceUsd = 164.00,
      dispatchSla = "Ships in 24h",
      specSummary = "High-density fin radiator + wax thermostat + pressure cap + water pump",
      fitmentNotes = "Use silicate-free Super Long Life Coolant for tropical traffic in $market.",
      visualKind = PartVisualKind.SENSOR_ELECTRONIC,
      linkedTaskKey = "coolant_flush",
      keywords = listOf("radiator", "thermostat", "coolant", "water pump", "cap", "overheat"),
      subComponents = listOf(
        PartSubComponent("sub_col_rad", "Aluminum Core Radiator w/ Trans Cooler", spec.radiatorCode, "KOYORAD PL0124", "1 pc", "22mm high-efficiency louvered fins", 118.00),
        PartSubComponent("sub_col_therm", "OEM Wax-Pellet Thermostat (82°C) + Seal", "$p-90916-03136", "TAMA WV56TB-82", "1 pc", "Exact tropical opening temperature", 19.00),
        PartSubComponent("sub_col_cap", "108 kPa (1.1 bar) Radiator Pressure Cap", "$p-16401-31650", "DENSO 108KPA", "1 pc", "Maintains boiling margin in gridlock", 11.50),
        PartSubComponent("sub_col_pump", if (isHybrid || isEv) "Electric Auxiliary Coolant Pump" else "Precision Coolant Water Pump + Gasket", "$p-161A0-29015", "AISIN WPT-190", "1 pc", "Carbon-ceramic mechanical shaft seal", 88.00, StockStatus.OEM_ORDER),
        PartSubComponent("sub_col_hose", "Upper & Lower EPDM Radiator Hose Set", "$p-16571-21110", "OHNO / GATES", "2 pcs", "Reinforced polyester knit core", 24.00),
      ),
    )

    // 9. AC / HVAC
    parts += CatalogPart(
      id = "hvc_cabin_blower",
      system = CatalogSystem.AC_HVAC,
      subSystem = "Compressor & Cabin Air",
      name = "Tropical AC Compressor, Condenser & PM2.5 HEPA Kit",
      oemNumber = spec.acCompressorCode,
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "DENSO 471-1018 / DCC1014",
      aftermarketBrand = "Denso CoolGear Japan",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 87-14 Heating & AC",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 15,
      priceUsd = 245.00,
      dispatchSla = "Ships in 24h",
      specSummary = "High-ambient AC compressor + desiccant bag + expansion valve + HEPA cabin filter",
      fitmentNotes = "Verify ND-OIL 8/11 compressor oil charge (POE for Hybrid/EV) on $fitment.",
      visualKind = PartVisualKind.HVAC_BLOWER,
      linkedTaskKey = "cabin_filter",
      keywords = listOf("ac", "hvac", "compressor", "cabin filter", "condenser", "blower", "cooling coil"),
      subComponents = listOf(
        PartSubComponent("sub_hvc_cabin", "Activated Carbon + HEPA PM2.5 Cabin Filter", spec.cabinFilterCode, "DENSO DCC1014", "1 pc", "99.1% PM2.5 & Dhaka smog capture", 22.50),
        PartSubComponent("sub_hvc_comp", if (isHybrid || isEv) "High-Voltage Electric Scroll Compressor" else "Swash-Plate AC Compressor Assembly", spec.acCompressorCode, "DENSO 471-1018", "1 pc", "Factory clutch & pulley balanced", 215.00, StockStatus.OEM_ORDER),
        PartSubComponent("sub_hvc_exp", "Block-Type Thermal Expansion Valve (TXV)", "$p-88515-52080", "DENSO 475-2092", "1 pc", "Superheat metered refrigerant flow", 28.00),
        PartSubComponent("sub_hvc_drier", "Receiver Drier Desiccant Filter Bag", "$p-88474-17010", "DENSO 478-2060", "1 pc", "Absorbs R134a / R1234yf moisture", 16.00),
        PartSubComponent("sub_hvc_blower", "Brushless Cabin HVAC Blower Motor", "$p-87103-52141", "DENSO 194000", "1 pc", "Low-noise squirrel-cage turbine", 74.00),
      ),
    )

    // 10. ELECTRICAL
    parts += CatalogPart(
      id = "elc_battery",
      system = CatalogSystem.ELECTRICAL,
      subSystem = "Starting, Charging & Storage",
      name = "12V Auxiliary Battery & Terminal Assembly (${spec.batterySpec})",
      oemNumber = "$p-28800-31370",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "GS YUASA ECO.R / AMARON PRO",
      aftermarketBrand = "GS Yuasa / Amaron / Rahimafrooz",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 82-01 Battery & Cable",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 22,
      priceUsd = 169.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "High cold-cranking reserve + brass clamps + fusible link block",
      fitmentNotes = "Check BMS reset procedure after battery swap on $fitment.",
      visualKind = PartVisualKind.BATTERY_MODULE,
      linkedTaskKey = "battery_12v",
      keywords = listOf("battery", "12v", "agm", "efb", "terminal", "fusible link"),
      subComponents = listOf(
        PartSubComponent("sub_bat_cell", "12V Battery Unit (${spec.batterySpec})", "$p-28800-31370", "GS YUASA / AMARON", "1 unit", "24-month tropical heat warranty", 155.00),
        PartSubComponent("sub_bat_pos", "Positive Battery Terminal & Multi-Fuse Block", "$p-82620-12280", "OEM FUSE-LINK", "1 pc", "Integrated 120A/80A main fusible strip", 24.00),
        PartSubComponent("sub_bat_neg", "Negative Ground Strap w/ Current Sensor", "$p-28850-21040", "DENSO IBS-SNS", "1 pc", "Monitors charging state of health", 38.00),
        PartSubComponent("sub_bat_hold", "Anti-Vibration Battery Hold-Down Clamp", "$p-74404-12310", "OEM TRAY-CLAMP", "1 set", "Powder-coated bracket + J-bolts", 11.00),
      ),
    )

    parts += CatalogPart(
      id = "elc_abs_sensor",
      system = CatalogSystem.ELECTRICAL,
      subSystem = "Sensors, Alternator & Relays",
      name = "Wheel Speed ABS Sensor & Ignition/Relay Kit",
      oemNumber = "$p-89542-06081",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "BOSCH 0 265 009 412 / DENSO",
      aftermarketBrand = "Bosch / Denso",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 84-01 Chassis Sensors",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 35,
      priceUsd = 44.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "IP69K waterproof Hall-effect wheel speed sensor + relay + harness clips",
      fitmentNotes = "Monsoon flood-proof IP69K connector seals for $fitment.",
      visualKind = PartVisualKind.SENSOR_ELECTRONIC,
      keywords = listOf("abs", "sensor", "alternator", "relay", "ignition coil", "fuse"),
      subComponents = listOf(
        PartSubComponent("sub_abs_probe", "Hall-Effect Wheel Speed ABS Sensor", "$p-89542-06081", "BOSCH 0265009", "1 pc", "Active direction-sensing IC", 36.00),
        PartSubComponent("sub_elc_coil", if (isEv) "HV Main Contactor Relay" else "Direct Ignition Coil-on-Plug Pack", "$p-90919-02276", "DENSO 673-1315", "1 pc", "38kV epoxy-potted secondary", 52.00),
        PartSubComponent("sub_elc_relay", "4-Pin 30A Waterproof Fan / Fuel Relay", "$p-90987-02028", "OMRON 12V-30A", "1 pc", "Silver-tin oxide contacts", 8.50),
        PartSubComponent("sub_abs_clips", "Strut Harness Retention Grommets (3x)", "$p-89516-12090", "OEM CLIP-SET", "3 pcs", "Prevents tire rub on sensor lead", 4.50),
      ),
    )

    // 11. EXHAUST
    if (!isEv) {
      parts += CatalogPart(
        id = "exh_cat_o2",
        system = CatalogSystem.EXHAUST,
        subSystem = "Emission & Exhaust Mounting",
        name = if (isDiesel) "Diesel DPF / Catalytic Pipe & Sensor Kit" else "Catalytic Pipe, O2 Sensor & Gasket Assembly",
        oemNumber = spec.exhaustCatCode,
        oemManufacturer = spec.oemBrand,
        aftermarketNumber = "DENSO 234-9090 / BOSAL",
        aftermarketBrand = "Denso / Bosal",
        epcCatalogRef = "${spec.epcCatalogName} • Fig. 17-02 Exhaust Pipe",
        stockStatus = StockStatus.LOW_STOCK,
        warehouseQty = 5,
        priceUsd = 228.00,
        dispatchSla = "2–3 business days",
        specSummary = "304 stainless flex pipe + wide-band A/F O2 sensor + donut flange gasket",
        fitmentNotes = "Always replace crush donut gaskets and spring bolts together on $fitment.",
        visualKind = PartVisualKind.EXHAUST_PIPE,
        linkedTaskKey = "exhaust_emissions_check",
        keywords = listOf("exhaust", "catalytic", "o2 sensor", "dpf", "muffler", "gasket"),
        subComponents = listOf(
          PartSubComponent("sub_exh_o2", "Upstream Wideband Air-Fuel Ratio O2 Sensor", "$p-89467-12100", "DENSO 234-9090", "1 pc", "Fast 10s zirconia planar heater", 94.00),
          PartSubComponent("sub_exh_donut", "Graphite Mesh Exhaust Donut Flange Gasket", "$p-17451-21060", "BOSAL 256-304", "1 pc", "600°C high-compression wire mesh", 9.80),
          PartSubComponent("sub_exh_bolts", "Spring-Loaded Exhaust Flange Bolt Pair", "$p-90901-05026", "OEM M10-SPRING", "2 pcs", "Allows thermal pipe articulation", 11.00),
          PartSubComponent("sub_exh_hanger", "High-Temp EPDM Exhaust Muffler Hanger", "$p-17565-21040", "FEBEST TEXB-001", "2 pcs", "Isolates underbody vibration", 8.00),
        ),
      )
    }

    // 12. BODY & INTERIOR
    parts += CatalogPart(
      id = "bdy_wiper_blades",
      system = CatalogSystem.BODY_INTERIOR,
      subSystem = "Exterior Visibility & Trim",
      name = "Aero Silicone Wiper Blade & Washer Nozzle Kit",
      oemNumber = "$p-85212-06230",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "NWB AERO / BOSCH AEROTWIN",
      aftermarketBrand = "NWB Japan / Bosch",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 85-01 Windshield Wiper",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 46,
      priceUsd = 32.00,
      dispatchSla = "Same-day dispatch",
      specSummary = "Graphite-coated monsoon wiper beam + fan-spray washer nozzles",
      fitmentNotes = "Matched RHD/LHD sweep curvature for $fitment.",
      visualKind = PartVisualKind.BODY_WIPER,
      linkedTaskKey = "wiper_blades",
      keywords = listOf("wiper", "blade", "washer", "nozzle", "windshield"),
      subComponents = listOf(
        PartSubComponent("sub_wip_drv", "Driver-Side Aero Silicone Blade (24\"/26\")", "$p-85222-52150", "NWB NU-024", "1 pc", "Hydrophobic streak-free rubber", 16.00),
        PartSubComponent("sub_wip_pas", "Passenger-Side Aero Silicone Blade (14\"/16\")", "$p-85212-52110", "NWB NU-014", "1 pc", "Dual-spring steel spine", 13.00),
        PartSubComponent("sub_wip_noz", "Mist Fan-Spray Washer Jet Nozzles (Pair)", "$p-85381-12300", "OEM JET-2X", "2 pcs", "Anti-clog brass micro-orifice", 9.50),
        PartSubComponent("sub_wip_arm", "Wiper Arm Pivot Nut & Weather Cap", "$p-85292-13010", "OEM CAP-2X", "2 pcs", "Protects spline shaft from corrosion", 4.00),
      ),
    )

    parts += CatalogPart(
      id = "bdy_hood_grille",
      system = CatalogSystem.BODY_INTERIOR,
      subSystem = "Body Hardware & Fascia",
      name = "Hood Lift Strut, Bumper Retainer & Latch Kit",
      oemNumber = "$p-53440-06180",
      oemManufacturer = spec.oemBrand,
      aftermarketNumber = "STABILUS LIFT-O-MAT / TONG YANG",
      aftermarketBrand = "Stabilus / Tong Yang",
      epcCatalogRef = "${spec.epcCatalogName} • Fig. 53-01 Hood & Front Fender",
      stockStatus = StockStatus.IN_STOCK,
      warehouseQty = 18,
      priceUsd = 48.00,
      dispatchSla = "Ships in 24h",
      specSummary = "Gas-charged hood dampers + bumper push-clips + door check strap",
      fitmentNotes = "Complete body fastener & damper refresh for $fitment.",
      visualKind = PartVisualKind.BODY_WIPER,
      keywords = listOf("hood strut", "bumper", "clip", "fastener", "latch", "body"),
      subComponents = listOf(
        PartSubComponent("sub_bdy_strut", "Gas-Charged Hood / Tailgate Lift Strut Pair", "$p-53440-06180", "STABILUS 380N", "2 pcs", "Nitride piston rod • 380N lift", 36.00),
        PartSubComponent("sub_bdy_clips", "Bumper & Fender Liner Push-Pin Clip Box (20x)", "$p-90467-07201", "POM NYLON-20X", "20 pcs", "7mm / 8mm / 10mm OEM trim retainers", 7.50),
        PartSubComponent("sub_bdy_door", "Front Door Check Strap Stopper", "$p-68610-12230", "OEM CHECK-FR", "1 pc", "Eliminates door hinge pop", 18.00),
      ),
    )

    val result = parts.toList()
    catalogCache[cacheKey] = result
    return result
  }
}
