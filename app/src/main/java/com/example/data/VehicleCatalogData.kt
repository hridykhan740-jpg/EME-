package com.example.data

import com.example.model.Powertrain

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
  val marketCodes: List<String>,
  val years: List<Int>,
  val variants: List<VehicleVariantSpec>,
)

object VehicleCatalogData {

  val markets: List<MarketSpec> = listOf(
    MarketSpec("Bangladesh", "BD", "South Asia • Priority Market", "Dhaka/BD severe traffic, high-humidity cooling & JDM/CKD specs"),
    MarketSpec("Japan", "JDM", "Asia-Pacific", "Right-hand drive & strict Shaken inspection specs"),
    MarketSpec("India", "IND", "South Asia", "BS6 Phase-2 dust-hardened & high-ambient cooling specs"),
    MarketSpec("ASEAN (TH / ID / MY)", "ASEAN", "Southeast Asia", "Tropical heavy-duty HVAC & diesel/hybrid specs"),
    MarketSpec("South Korea", "KDM", "Asia-Pacific", "Korean domestic emission & Smartstream specs"),
    MarketSpec("United States", "USDM", "North America", "EPA Tier-3 & Federal OBD-II specifications"),
    MarketSpec("Germany / Europe", "EDM", "Europe", "Euro 6d & LongLife extended service specs"),
  )

  val models: List<VehicleModelSpec> = listOf(
    // TOYOTA (Bangladesh & Asia Priority)
    VehicleModelSpec(
      brand = "Toyota",
      model = "Corolla Axio",
      marketCodes = listOf("BD", "JDM"),
      years = (2012..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Hybrid G / EX eCVT (NKE165 1NZ-FXE)", Powertrain.HYBRID, "NKE165"),
        VehicleVariantSpec("1.5L X / G Super CVT-i (NZE161 1NZ-FE)", Powertrain.PETROL, "NZE161"),
        VehicleVariantSpec("1.3L / 1.5L Dual VVT-i (NZE141 / NRE160)", Powertrain.PETROL, "NZE141"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Corolla Fielder",
      marketCodes = listOf("BD", "JDM"),
      years = (2012..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Hybrid G / WxB eCVT (NKE165G)", Powertrain.HYBRID, "NKE165G"),
        VehicleVariantSpec("1.5L 1NZ-FE / 2NR-FKE Wagon CVT (NZE161G)", Powertrain.PETROL, "NZE161G"),
        VehicleVariantSpec("1.8L 2ZR-FAE Aerotourer CVT (ZRE162G)", Powertrain.PETROL, "ZRE162G"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Premio",
      marketCodes = listOf("BD", "JDM"),
      years = (2010..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L F-EX Package 1NZ-FE Super CVT-i (NZT260)", Powertrain.PETROL, "NZT260"),
        VehicleVariantSpec("1.8L X-EX Package 2ZR-FAE Valvematic (ZRT260)", Powertrain.PETROL, "ZRT260"),
        VehicleVariantSpec("2.0L G Superior 3ZR-FAE (ZRT261)", Powertrain.PETROL, "ZRT261"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Allion",
      marketCodes = listOf("BD", "JDM"),
      years = (2010..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L A15 G-Plus 1NZ-FE CVT (NZT260)", Powertrain.PETROL, "NZT260"),
        VehicleVariantSpec("1.8L A18 G Package 2ZR-FAE (ZRT260)", Powertrain.PETROL, "ZRT260"),
        VehicleVariantSpec("2.0L A20 S Package 3ZR-FAE (ZRT261)", Powertrain.PETROL, "ZRT261"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Noah",
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L Hybrid Si / WxB eCVT (ZWR80 / ZWR90)", Powertrain.HYBRID, "ZWR80"),
        VehicleVariantSpec("2.0L 3ZR-FAE Valvematic Petrol CVT (ZRR80)", Powertrain.PETROL, "ZRR80"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "HiAce",
      marketCodes = listOf("BD", "JDM", "ASEAN", "IND"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.7L 2TR-FE Petrol Super GL 6AT (TRH200 / TRH214)", Powertrain.PETROL, "TRH200"),
        VehicleVariantSpec("2.8L 1GD-FTV Turbo Diesel DX/GL 6AT (GDH200)", Powertrain.DIESEL, "GDH200"),
        VehicleVariantSpec("3.0L 1KD-FTV D-4D Commuter (KDH200)", Powertrain.DIESEL, "KDH200"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Land Cruiser Prado",
      marketCodes = listOf("BD", "JDM", "ASEAN", "EDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.7L 2TR-FE TX-L Petrol 6AT 4WD (TRJ150W)", Powertrain.PETROL, "TRJ150"),
        VehicleVariantSpec("2.8L 1GD-FTV TZ-G Clean Diesel 6AT/8AT (GDJ150 / GDJ250)", Powertrain.DIESEL, "GDJ150"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Camry",
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "EDM", "IND"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.5L Dynamic Force Hybrid eCVT (AXVH70)", Powertrain.HYBRID, "XV70"),
        VehicleVariantSpec("2.5L I4 8-Speed Direct-Shift AT (AXVA70)", Powertrain.PETROL, "XV70"),
        VehicleVariantSpec("3.5L V6 8AT XSE (GSV70)", Powertrain.PETROL, "XV70"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Corolla / Corolla Cross",
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "EDM", "IND"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L Hybrid E-CVT (ZWE211 / ZVG11)", Powertrain.HYBRID, "E210"),
        VehicleVariantSpec("2.0L Dynamic Force CVT (MZEA12)", Powertrain.PETROL, "E210"),
        VehicleVariantSpec("1.2L Turbo 6MT / Super CVT-i (NRE210)", Powertrain.PETROL, "E210"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Hilux / Fortuner",
      marketCodes = listOf("BD", "ASEAN", "JDM", "IND", "EDM"),
      years = (2016..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.8L 1GD-FTV Turbo Diesel 4x4 6AT", Powertrain.DIESEL, "AN120"),
        VehicleVariantSpec("2.4L 2GD-FTV Diesel 6MT", Powertrain.DIESEL, "AN120"),
        VehicleVariantSpec("2.7L 2TR-FE VVT-i Petrol 6AT", Powertrain.PETROL, "AN120"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Prius",
      marketCodes = listOf("BD", "JDM", "USDM", "EDM"),
      years = (2016..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L 2ZR-FXE Hybrid E-Four (ZVW50 / ZVW55)", Powertrain.HYBRID, "XW50"),
        VehicleVariantSpec("2.0L Series-Parallel Hybrid eCVT (MXWH60)", Powertrain.HYBRID, "XW60"),
      ),
    ),

    // HONDA (Bangladesh & Asia Priority)
    VehicleModelSpec(
      brand = "Honda",
      model = "Vezel / HR-V",
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L i-DCD Hybrid RS / Z Sensing 7DCT (RU3)", Powertrain.HYBRID, "RU3"),
        VehicleVariantSpec("1.5L e:HEV Z / Play Hybrid (RV5)", Powertrain.HYBRID, "RV5"),
        VehicleVariantSpec("1.5L i-VTEC CVT (RU1)", Powertrain.PETROL, "RU1"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "Grace",
      marketCodes = listOf("BD", "JDM"),
      years = (2014..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Sport Hybrid i-DCD EX / LX 7DCT (GM4)", Powertrain.HYBRID, "GM4"),
        VehicleVariantSpec("1.5L Earth Dreams i-VTEC CVT (GM6)", Powertrain.PETROL, "GM6"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "Civic",
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "EDM"),
      years = (2017..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L VTEC Turbo CVT (FC1 / FE1 / FL1)", Powertrain.PETROL, "FE/FL"),
        VehicleVariantSpec("2.0L e:HEV i-MMD Hybrid (FL4)", Powertrain.HYBRID, "FL4"),
        VehicleVariantSpec("2.0L K20C1 Type R 6MT (FL5)", Powertrain.PETROL, "FL5"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "CR-V",
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "IND", "EDM"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L VTEC Turbo AWD CVT (RW2 / RS4)", Powertrain.PETROL, "RW"),
        VehicleVariantSpec("2.0L e:HEV AWD Hybrid (RS6)", Powertrain.HYBRID, "RS"),
        VehicleVariantSpec("1.6L i-DTEC Diesel 9AT (IND/ASEAN)", Powertrain.DIESEL, "RW"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "City",
      marketCodes = listOf("BD", "IND", "ASEAN", "JDM"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L i-VTEC DOHC CVT / 6MT (GN2)", Powertrain.PETROL, "GN2"),
        VehicleVariantSpec("1.5L e:HEV Strong Hybrid (GN5)", Powertrain.HYBRID, "GN5"),
      ),
    ),

    // MITSUBISHI (Bangladesh & Asia Priority)
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Outlander",
      marketCodes = listOf("BD", "JDM", "ASEAN", "USDM", "EDM"),
      years = (2015..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L / 2.4L MIVEC 7-Seater 4WD CVT (GF7W / GF8W)", Powertrain.PETROL, "GF8W"),
        VehicleVariantSpec("2.4L Twin-Motor PHEV 4WD (GG3W / GN0W)", Powertrain.HYBRID, "GG3W"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Pajero Sport",
      marketCodes = listOf("BD", "ASEAN", "JDM"),
      years = (2016..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.4L 4N15 MIVEC Clean Diesel 8AT Super Select 4WD", Powertrain.DIESEL, "KS1W"),
        VehicleVariantSpec("3.0L 6B31 MIVEC V6 Petrol 8AT", Powertrain.PETROL, "KS5W"),
      ),
    ),

    // NISSAN
    VehicleModelSpec(
      brand = "Nissan",
      model = "X-Trail / Note e-POWER",
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "EDM"),
      years = (2016..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L MR20DD 7-Seater 4WD Xtronic (NT32 / T32)", Powertrain.PETROL, "T32"),
        VehicleVariantSpec("1.5L VC-Turbo e-POWER e-4ORCE (T33)", Powertrain.HYBRID, "T33"),
        VehicleVariantSpec("1.2L HR12DE e-POWER (HE12 / E13 Note)", Powertrain.HYBRID, "E12/E13"),
      ),
    ),

    // HYUNDAI
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Tucson",
      marketCodes = listOf("BD", "KDM", "USDM", "EDM", "IND", "ASEAN"),
      years = (2020..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.6L T-GDi HEV Hybrid 6AT AWD (NX4)", Powertrain.HYBRID, "NX4"),
        VehicleVariantSpec("2.0L Nu MPi / Smartstream 6AT (TL / NX4)", Powertrain.PETROL, "NX4"),
        VehicleVariantSpec("2.0L R-Engine CRDi Diesel 8AT", Powertrain.DIESEL, "NX4"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Creta / Alcazar",
      marketCodes = listOf("BD", "IND", "ASEAN", "KDM"),
      years = (2020..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L MPi Smartstream IVT (SU2)", Powertrain.PETROL, "SU2"),
        VehicleVariantSpec("1.5L T-GDi Turbo 7DCT (SU2)", Powertrain.PETROL, "SU2"),
        VehicleVariantSpec("1.5L U2 CRDi Diesel 6AT / 6MT", Powertrain.DIESEL, "SU2"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Ioniq 5",
      marketCodes = listOf("KDM", "USDM", "EDM", "ASEAN", "IND", "JDM", "BD"),
      years = (2021..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("77.4 kWh Long Range AWD Dual-Motor (NE1)", Powertrain.EV, "E-GMP"),
        VehicleVariantSpec("58 kWh Standard Range RWD (NE1)", Powertrain.EV, "E-GMP"),
      ),
    ),

    // SUZUKI / MARUTI SUZUKI
    VehicleModelSpec(
      brand = "Suzuki / Maruti",
      model = "Swift / Baleno",
      marketCodes = listOf("BD", "JDM", "IND", "ASEAN", "EDM"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L Z12E / K12N DualJet 5MT / AMT / CVT", Powertrain.PETROL, "ZC/WB"),
        VehicleVariantSpec("1.2L Mild Hybrid SHVS CVT (JDM / BD)", Powertrain.HYBRID, "ZCEDS"),
      ),
    ),
    VehicleModelSpec(
      brand = "Suzuki / Maruti",
      model = "Grand Vitara / Jimny",
      marketCodes = listOf("BD", "JDM", "IND", "ASEAN", "EDM"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L K15C Smart Hybrid AllGrip 6AT", Powertrain.HYBRID, "YFG"),
        VehicleVariantSpec("1.5L Intelligent Electric Hybrid eCVT", Powertrain.HYBRID, "YFG"),
        VehicleVariantSpec("1.5L K15B Jimny 4WD 5MT / 4AT (JB74)", Powertrain.PETROL, "JB74"),
      ),
    ),

    // TATA (Bangladesh & India)
    VehicleModelSpec(
      brand = "Tata",
      model = "Nexon / Harrier / Tiago",
      marketCodes = listOf("BD", "IND"),
      years = (2020..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L Revotron Turbo Petrol 7DCA / Tiago AMT", Powertrain.PETROL, "X1/X4"),
        VehicleVariantSpec("1.5L Revotorq / 2.0L Kryotec Diesel 6AT", Powertrain.DIESEL, "O5"),
        VehicleVariantSpec("45 kWh Nexon.ev LR Ziptron", Powertrain.EV, "X1-EV"),
      ),
    ),

    // KIA
    VehicleModelSpec(
      brand = "Kia",
      model = "Seltos / Sportage",
      marketCodes = listOf("BD", "KDM", "IND", "USDM", "ASEAN", "EDM"),
      years = (2020..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Smartstream T-GDi 7DCT (SP2)", Powertrain.PETROL, "SP2/NQ5"),
        VehicleVariantSpec("1.6L T-GDi Hybrid AWD 6AT (NQ5)", Powertrain.HYBRID, "NQ5"),
        VehicleVariantSpec("1.5L CRDi VGT Diesel 6AT", Powertrain.DIESEL, "SP2"),
      ),
    ),
    VehicleModelSpec(
      brand = "Kia",
      model = "EV6",
      marketCodes = listOf("KDM", "USDM", "EDM", "IND", "ASEAN"),
      years = (2022..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("77.4 kWh GT-Line AWD 800V (CV1)", Powertrain.EV, "E-GMP"),
        VehicleVariantSpec("84 kWh Long Range RWD (CV1 Facelift)", Powertrain.EV, "E-GMP"),
      ),
    ),

    // MAZDA & SUBARU & MAHINDRA & BYD
    VehicleModelSpec(
      brand = "Mazda",
      model = "CX-5 / Mazda3",
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "EDM"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.5L Skyactiv-G AWD 6AT (KF)", Powertrain.PETROL, "KF/BP"),
        VehicleVariantSpec("2.2L Skyactiv-D Twin-Turbo Diesel 6AT", Powertrain.DIESEL, "KF"),
      ),
    ),
    VehicleModelSpec(
      brand = "Subaru",
      model = "WRX / Forester",
      marketCodes = listOf("JDM", "USDM", "EDM"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.4L FA24 Boxer Turbo Symmetrical AWD (VB)", Powertrain.PETROL, "VB"),
        VehicleVariantSpec("2.0L e-Boxer Mild Hybrid Lineartronic (SK)", Powertrain.HYBRID, "SK"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mahindra",
      model = "XUV700 / Scorpio-N",
      marketCodes = listOf("IND", "ASEAN", "BD"),
      years = (2021..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.2L mHawk CRDi AWD 6AT", Powertrain.DIESEL, "W601"),
        VehicleVariantSpec("2.0L mStallion TGDi Petrol 6AT", Powertrain.PETROL, "W601"),
      ),
    ),
    VehicleModelSpec(
      brand = "BYD",
      model = "Atto 3 / Seal",
      marketCodes = listOf("BD", "ASEAN", "IND", "JDM", "EDM"),
      years = (2022..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("60.48 kWh Blade LFP FWD (e-Platform 3.0)", Powertrain.EV, "EA1"),
        VehicleVariantSpec("82.5 kWh Seal Performance AWD", Powertrain.EV, "EK"),
      ),
    ),

    // EUROPEAN & USDM
    VehicleModelSpec(
      brand = "BMW",
      model = "3 Series / X3",
      marketCodes = listOf("EDM", "USDM", "JDM", "IND", "KDM", "BD"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L B48 TwinPower Turbo 8AT ZF (G20)", Powertrain.PETROL, "G20"),
        VehicleVariantSpec("2.0L B47 Diesel xDrive 8AT (G01)", Powertrain.DIESEL, "G01"),
        VehicleVariantSpec("3.0L B58 M340i xDrive Mild Hybrid", Powertrain.HYBRID, "G20"),
      ),
    ),
    VehicleModelSpec(
      brand = "Volkswagen",
      model = "Golf / Tiguan",
      marketCodes = listOf("EDM", "USDM", "JDM", "IND"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L EA888 TSI 7-Speed DSG (MK8)", Powertrain.PETROL, "CD1"),
        VehicleVariantSpec("1.5L eTSI Mild Hybrid DSG", Powertrain.HYBRID, "CD1"),
        VehicleVariantSpec("2.0L EA288 TDI Diesel 4Motion DSG", Powertrain.DIESEL, "AD1"),
      ),
    ),
    VehicleModelSpec(
      brand = "Ford",
      model = "Ranger / F-150",
      marketCodes = listOf("USDM", "ASEAN", "EDM"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L Bi-Turbo EcoBlue Diesel 10AT", Powertrain.DIESEL, "P703"),
        VehicleVariantSpec("3.5L EcoBoost PowerBoost Full Hybrid 10AT", Powertrain.HYBRID, "P702"),
        VehicleVariantSpec("2.7L EcoBoost V6 10R80", Powertrain.PETROL, "P702"),
      ),
    ),
  )

  fun brandsForMarket(marketCode: String): List<String> {
    val matched = models
      .filter { it.marketCodes.contains(marketCode) }
      .map { it.brand }
      .distinct()
      .sorted()
    return matched.ifEmpty { models.map { it.brand }.distinct().sorted() }
  }

  fun modelsFor(marketCode: String, brand: String): List<VehicleModelSpec> {
    val marketFiltered = models.filter { it.brand == brand && it.marketCodes.contains(marketCode) }
    return marketFiltered.ifEmpty { models.filter { it.brand == brand } }
  }
}
