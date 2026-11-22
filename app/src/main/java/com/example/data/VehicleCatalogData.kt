package com.example.data

import com.example.model.Powertrain
import com.example.model.VehicleCategory

data class MarketSpec(
  val country: String,
  val marketCode: String,
  val regionTag: String,
  val notes: String,
)

data class VehicleVariantSpec(
  val name: String,
  val powertrain: Powertrain,
  val chassisCode: String,
)

data class VehicleModelSpec(
  val brand: String,
  val model: String,
  val category: VehicleCategory,
  val marketCodes: List<String>,
  val years: List<Int>,
  val variants: List<VehicleVariantSpec>,
)

object VehicleCatalogData {

  val markets: List<MarketSpec> = listOf(
    MarketSpec("Bangladesh", "BD", "South Asia • Complete Fleet", "All private cars, SUVs, MPVs & commercial trucks/buses in BD"),
    MarketSpec("Japan", "JDM", "Asia-Pacific", "Right-hand drive & strict Shaken inspection specs"),
    MarketSpec("India", "IND", "South Asia", "BS6 Phase-2 dust-hardened & high-ambient cooling specs"),
    MarketSpec("ASEAN (TH / ID / MY)", "ASEAN", "Southeast Asia", "Tropical heavy-duty HVAC & diesel/hybrid specs"),
    MarketSpec("South Korea", "KDM", "Asia-Pacific", "Korean domestic emission & Smartstream specs"),
    MarketSpec("United States", "USDM", "North America", "EPA Tier-3 & Federal OBD-II specifications"),
    MarketSpec("Germany / Europe", "EDM", "Europe", "Euro 6d & LongLife extended service specs"),
  )

  private val commercialAndGlobalVehicles: List<VehicleModelSpec> = listOf(
    // COMMERCIAL TRUCKS, PICKUPS & BUSES IN BANGLADESH
    VehicleModelSpec(
      brand = "Toyota",
      model = "Hilux",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "ASEAN", "JDM", "EDM"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.8L 1GD-FTV Revo 4x4 6AT/6MT (GUN126)", Powertrain.DIESEL, "GUN126"),
        VehicleVariantSpec("2.4L 2GD-FTV Double Cab 4x4 (GUN125)", Powertrain.DIESEL, "GUN125"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Dyna",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2010..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("4.0L N04C-T / 3.0L 1KD-FTV Light Commercial Truck (XZU / KDY)", Powertrain.DIESEL, "XZU600"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "L200",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "ASEAN", "JDM", "EDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.4L 4N15 / 2.5L 4D56 Triton Pickup 4WD (KL1T / KK1T)", Powertrain.DIESEL, "KL1T"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Fuso Canter",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("3.0L 4P10 / 4.9L 4M50 Cargo Truck (FE70 / FE83 / FEB)", Powertrain.DIESEL, "FE70"),
      ),
    ),
    VehicleModelSpec(
      brand = "Tata",
      model = "Ace",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("702cc / 800cc DICOR Mini Pickup (EX2 / Gold)", Powertrain.DIESEL, "ACE-GOLD"),
      ),
    ),
    VehicleModelSpec(
      brand = "Tata",
      model = "Intra",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.4L / 1.5L DI Compact Truck (V10 / V30 / V50)", Powertrain.DIESEL, "INTRA-V30"),
      ),
    ),
    VehicleModelSpec(
      brand = "Tata",
      model = "Xenon",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND", "ASEAN"),
      years = (2012..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.2L / 3.0L DICOR Double/Single Cab Pickup (Y2)", Powertrain.DIESEL, "XENON-Y2"),
      ),
    ),
    VehicleModelSpec(
      brand = "Tata",
      model = "LPT 1615 / 1109",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("5.9L Cummins 6BT / 3.8L 497 TCIC Heavy Cargo Truck", Powertrain.DIESEL, "LPT1615"),
      ),
    ),
    VehicleModelSpec(
      brand = "Tata",
      model = "Prima",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("6.7L Cummins ISBe Prime Mover / Tipper (4928 / 2528K)", Powertrain.DIESEL, "PRIMA-LX"),
      ),
    ),
    VehicleModelSpec(
      brand = "Ashok Leyland",
      model = "Dost",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2013..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L P15 TDCR LCV Pickup (Dost+ / Bada Dost)", Powertrain.DIESEL, "DOST-LCV"),
      ),
    ),
    VehicleModelSpec(
      brand = "Ashok Leyland",
      model = "Ecomet",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("3.8L H-Series 4CTI Medium Truck (1015 / 1215 / 1615)", Powertrain.DIESEL, "ECOMET-1615"),
      ),
    ),
    VehicleModelSpec(
      brand = "Ashok Leyland",
      model = "Viking",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("5.8L H-Series 6DTI Highway Bus Chassis (222-WB)", Powertrain.DIESEL, "VIKING-BUS"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mahindra",
      model = "Bolero Pickup",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.5L m2DiCR 4-Cyl FB / Maxx Pik-Up", Powertrain.DIESEL, "BOLERO-PU"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mahindra",
      model = "Jeeto",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2015..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("625cc m_Dura Mini Van / Pickup (Plus / Strong)", Powertrain.DIESEL, "JEETO-X7"),
      ),
    ),
    VehicleModelSpec(
      brand = "Isuzu",
      model = "D-Max",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "ASEAN", "JDM", "EDM"),
      years = (2013..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.9L RZ4E / 3.0L 4JJ3-TCX 4x4 Pickup (RG01)", Powertrain.DIESEL, "RG01"),
      ),
    ),
    VehicleModelSpec(
      brand = "Isuzu",
      model = "N-Series",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "JDM", "ASEAN", "USDM"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("3.0L 4JJ1 / 5.2L 4HK1 Elf Cargo Truck (NKR / NPR / NLR)", Powertrain.DIESEL, "NKR85"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hino",
      model = "AK1J",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("7.7L J08C / J08E Front-Engine Intercity Bus Chassis", Powertrain.DIESEL, "AK1JR"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hino",
      model = "RM2",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "ASEAN", "JDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("8.9L A09C / E13C Rear-Engine AC Coach Chassis", Powertrain.DIESEL, "RM2PSS"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hino",
      model = "300 / 500 Series",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "JDM", "ASEAN", "IND"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("4.0L N04C Dutro 300 / 7.7L J08E Ranger 500 (XZU / FC9J)", Powertrain.DIESEL, "FC9J"),
      ),
    ),
    VehicleModelSpec(
      brand = "Eicher",
      model = "Pro 2049 / 3015 / Skyline",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("BD", "IND"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L / 3.8L E474 Volvo-Tech Diesel Truck & Bus", Powertrain.DIESEL, "PRO-3015"),
      ),
    ),

    // ADDITIONAL REGIONAL / GLOBAL EV & EURO VEHICLES
    VehicleModelSpec(
      brand = "Tata",
      model = "Nexon / Harrier / Tiago",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "IND"),
      years = (2020..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L Revotron Turbo Petrol 7DCA", Powertrain.PETROL, "X1"),
        VehicleVariantSpec("1.5L Revotorq / 2.0L Kryotec Diesel 6AT", Powertrain.DIESEL, "O5"),
        VehicleVariantSpec("45 kWh Nexon.ev LR Ziptron", Powertrain.EV, "X1-EV"),
      ),
    ),
    VehicleModelSpec(
      brand = "Suzuki / Maruti",
      model = "Grand Vitara / Jimny",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "IND", "ASEAN", "EDM"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L K15C Smart Hybrid AllGrip 6AT", Powertrain.HYBRID, "YFG"),
        VehicleVariantSpec("1.5L K15B Jimny 4WD 5MT / 4AT (JB74)", Powertrain.PETROL, "JB74"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Ioniq 5",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "KDM", "USDM", "EDM", "ASEAN", "IND", "JDM"),
      years = (2021..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("77.4 kWh Long Range AWD Dual-Motor (NE1)", Powertrain.EV, "E-GMP"),
      ),
    ),
    VehicleModelSpec(
      brand = "Kia",
      model = "EV6",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("KDM", "USDM", "EDM", "IND", "ASEAN"),
      years = (2022..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("77.4 kWh GT-Line AWD 800V (CV1)", Powertrain.EV, "E-GMP"),
      ),
    ),
    VehicleModelSpec(
      brand = "BYD",
      model = "Atto 3 / Seal",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "ASEAN", "IND", "JDM", "EDM"),
      years = (2022..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("60.48 kWh Blade LFP FWD (e-Platform 3.0)", Powertrain.EV, "EA1"),
        VehicleVariantSpec("82.5 kWh Seal Performance AWD", Powertrain.EV, "EK"),
      ),
    ),
    VehicleModelSpec(
      brand = "BMW",
      model = "3 Series / X3",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "EDM", "USDM", "JDM", "IND", "KDM"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L B48 TwinPower Turbo 8AT ZF (G20)", Powertrain.PETROL, "G20"),
        VehicleVariantSpec("2.0L B47 Diesel xDrive 8AT (G01)", Powertrain.DIESEL, "G01"),
      ),
    ),
    VehicleModelSpec(
      brand = "Volkswagen",
      model = "Golf / Tiguan",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("EDM", "USDM", "JDM", "IND"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L EA888 TSI 7-Speed DSG (MK8)", Powertrain.PETROL, "CD1"),
      ),
    ),
    VehicleModelSpec(
      brand = "Ford",
      model = "Ranger / F-150",
      category = VehicleCategory.COMMERCIAL_TRUCKS_BUSES,
      marketCodes = listOf("USDM", "ASEAN", "EDM"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L Bi-Turbo EcoBlue Diesel 10AT", Powertrain.DIESEL, "P703"),
      ),
    ),
  )

  val models: List<VehicleModelSpec> =
    BdPassengerVehicles.sedansAndHatchbacks +
      BdPassengerVehicles.suvsAnd4x4s +
      BdPassengerVehicles.microbusesMpvsAndVans +
      commercialAndGlobalVehicles

  fun brandsForMarket(marketCode: String, category: VehicleCategory? = null): List<String> {
    val matched = models
      .filter { spec ->
        spec.marketCodes.contains(marketCode) && (category == null || spec.category == category)
      }
      .map { it.brand }
      .distinct()
      .sorted()
    return matched.ifEmpty {
      models
        .filter { category == null || it.category == category }
        .map { it.brand }
        .distinct()
        .sorted()
    }
  }

  fun modelsFor(marketCode: String, brand: String, category: VehicleCategory? = null): List<VehicleModelSpec> {
    val marketFiltered = models.filter {
      it.brand == brand &&
        it.marketCodes.contains(marketCode) &&
        (category == null || it.category == category)
    }
    return marketFiltered.ifEmpty {
      models.filter { it.brand == brand && (category == null || it.category == category) }
    }
  }
}
