package com.example.data

import com.example.model.Powertrain
import com.example.model.VehicleCategory

internal object BdPassengerVehicles {
  val sedansAndHatchbacks: List<VehicleModelSpec> = listOf(
    // TOYOTA SEDANS & HATCHBACKS
    VehicleModelSpec(
      brand = "Toyota",
      model = "Corolla Axio",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2008..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Hybrid G / EX eCVT (NKE165 1NZ-FXE)", Powertrain.HYBRID, "NKE165"),
        VehicleVariantSpec("1.5L X / G Super CVT-i (NZE161 1NZ-FE)", Powertrain.PETROL, "NZE161"),
        VehicleVariantSpec("1.5L Luxel / X (NZE141 1NZ-FE)", Powertrain.PETROL, "NZE141"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Corolla Fielder",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2008..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Hybrid G / WxB eCVT (NKE165G)", Powertrain.HYBRID, "NKE165G"),
        VehicleVariantSpec("1.5L 1NZ-FE / 2NR-FKE Wagon CVT (NZE161G)", Powertrain.PETROL, "NZE161G"),
        VehicleVariantSpec("1.8L 2ZR-FAE Aerotourer CVT (ZRE162G)", Powertrain.PETROL, "ZRE162G"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Allion",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2008..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L A15 G-Plus 1NZ-FE CVT (NZT260)", Powertrain.PETROL, "NZT260"),
        VehicleVariantSpec("1.8L A18 G Package 2ZR-FAE (ZRT260)", Powertrain.PETROL, "ZRT260"),
        VehicleVariantSpec("2.0L A20 S Package 3ZR-FAE (ZRT261)", Powertrain.PETROL, "ZRT261"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Premio",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2008..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L F-EX Package 1NZ-FE CVT (NZT260)", Powertrain.PETROL, "NZT260"),
        VehicleVariantSpec("1.8L X-EX Package 2ZR-FAE Valvematic (ZRT260)", Powertrain.PETROL, "ZRT260"),
        VehicleVariantSpec("2.0L G Superior 3ZR-FAE (ZRT261)", Powertrain.PETROL, "ZRT261"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Corolla Cross",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "ASEAN", "USDM"),
      years = (2020..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L Hybrid Z / S eCVT (ZVG11 / ZVG13)", Powertrain.HYBRID, "ZVG11"),
        VehicleVariantSpec("1.8L 2ZR-FE Petrol CVT (ZSG10)", Powertrain.PETROL, "ZSG10"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Camry",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "EDM", "IND"),
      years = (2015..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.5L Dynamic Force Hybrid eCVT (AXVH70)", Powertrain.HYBRID, "AXVH70"),
        VehicleVariantSpec("2.5L I4 8-Speed Direct-Shift AT (AXVA70)", Powertrain.PETROL, "AXVA70"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Yaris",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "ASEAN", "EDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Hybrid eCVT (MXPH10 / NHP130)", Powertrain.HYBRID, "MXPH10"),
        VehicleVariantSpec("1.5L 2NR-FE Sedan/Hatchback CVT (NSP151)", Powertrain.PETROL, "NSP151"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Vitz",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2010..2021).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.0L 1KR-FE / 1.3L 1NR-FE CVT (KSP130 / NSP130)", Powertrain.PETROL, "KSP130"),
        VehicleVariantSpec("1.5L Hybrid F / Jewela (NHP130)", Powertrain.HYBRID, "NHP130"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Aqua",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 1NZ-FXE THS-II Hybrid (NHP10)", Powertrain.HYBRID, "NHP10"),
        VehicleVariantSpec("1.5L M15A-FXE Dynamic Force Hybrid (MXPK11)", Powertrain.HYBRID, "MXPK11"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Prius",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "USDM", "EDM"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L 2ZR-FXE Hybrid (ZVW30 / ZVW50 / ZVW55)", Powertrain.HYBRID, "ZVW50"),
        VehicleVariantSpec("2.0L Hybrid eCVT (MXWH60)", Powertrain.HYBRID, "MXWH60"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Ist",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2006..2016).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 1NZ-FE 150X / 150G (NCP110 / NCP60)", Powertrain.PETROL, "NCP110"),
        VehicleVariantSpec("1.8L 2ZR-FE 180G (ZSP110)", Powertrain.PETROL, "ZSP110"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Probox",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2008..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 1NZ-FE / 1.3L 1NR-FE DX/GL (NCP160V / NCP50)", Powertrain.PETROL, "NCP160V"),
        VehicleVariantSpec("1.5L Hybrid GL / F (NHP160V)", Powertrain.HYBRID, "NHP160V"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Succeed",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2008..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 1NZ-FE TX / UL-X Wagon (NCP160V / NCP58G)", Powertrain.PETROL, "NCP160V"),
        VehicleVariantSpec("1.5L Hybrid TX (NHP160V)", Powertrain.HYBRID, "NHP160V"),
      ),
    ),

    // HONDA SEDANS & HATCHBACKS
    VehicleModelSpec(
      brand = "Honda",
      model = "Civic",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "EDM"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L VTEC Turbo CVT (FC1 / FE1 / FL1)", Powertrain.PETROL, "FC1/FE1"),
        VehicleVariantSpec("1.8L i-VTEC R18Z1 CVT (FC6 / FB2)", Powertrain.PETROL, "FC6"),
        VehicleVariantSpec("2.0L e:HEV i-MMD Hybrid (FL4)", Powertrain.HYBRID, "FL4"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "Grace",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2014..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Sport Hybrid i-DCD EX / LX 7DCT (GM4)", Powertrain.HYBRID, "GM4"),
        VehicleVariantSpec("1.5L Earth Dreams i-VTEC CVT (GM6)", Powertrain.PETROL, "GM6"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "City",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "IND", "ASEAN", "JDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L i-VTEC DOHC CVT / 6MT (GM2 / GN2)", Powertrain.PETROL, "GN2"),
        VehicleVariantSpec("1.5L e:HEV Strong Hybrid (GN5)", Powertrain.HYBRID, "GN5"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "Fit",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Hybrid i-DCD S / L Package (GP5)", Powertrain.HYBRID, "GP5"),
        VehicleVariantSpec("1.5L e:HEV Home / Crosstar (GR3)", Powertrain.HYBRID, "GR3"),
        VehicleVariantSpec("1.3L i-VTEC L13B CVT (GK3 / GE6)", Powertrain.PETROL, "GK3"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "Insight",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "USDM"),
      years = (2010..2023).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.3L IMA Hybrid (ZE2) / 1.5L e:HEV (ZE4)", Powertrain.HYBRID, "ZE4/ZE2"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "Accord",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L i-MMD / e:HEV Hybrid (CR6 / CV3 / CY2)", Powertrain.HYBRID, "CV3"),
        VehicleVariantSpec("1.5L VTEC Turbo CVT (CV1)", Powertrain.PETROL, "CV1"),
      ),
    ),

    // NISSAN SEDANS & HATCHBACKS
    VehicleModelSpec(
      brand = "Nissan",
      model = "Sunny",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "IND", "ASEAN"),
      years = (2008..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L HR15DE / QG15DE AT/CVT (N17 / B15)", Powertrain.PETROL, "N17/B15"),
      ),
    ),
    VehicleModelSpec(
      brand = "Nissan",
      model = "Bluebird Sylphy",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2008..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L MRA8DE / 2.0L MR20DE Xtronic CVT (TB17 / KG11)", Powertrain.PETROL, "KG11/TB17"),
      ),
    ),
    VehicleModelSpec(
      brand = "Nissan",
      model = "Tiida",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2008..2018).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L HR15DE 15M / Latio (C11 / SC11)", Powertrain.PETROL, "C11"),
      ),
    ),
    VehicleModelSpec(
      brand = "Nissan",
      model = "Note",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2013..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L HR12DE e-POWER Hybrid (HE12 / E13)", Powertrain.HYBRID, "HE12/E13"),
        VehicleVariantSpec("1.2L HR12DE Pure Drive CVT (E12)", Powertrain.PETROL, "E12"),
      ),
    ),

    // MITSUBISHI SEDANS & HATCHBACKS
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Lancer EX",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "ASEAN", "USDM"),
      years = (2008..2019).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.6L 4A92 / 1.8L 4B10 MIVEC INVECS-III CVT (CY1A / CY3A)", Powertrain.PETROL, "CY3A"),
        VehicleVariantSpec("2.0L 4B11 GT / Ralliart (CY4A)", Powertrain.PETROL, "CY4A"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Attrage",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "ASEAN", "JDM"),
      years = (2014..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L 3A92 MIVEC INVECS-III CVT (A13A)", Powertrain.PETROL, "A13A"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Mirage",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2013..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L 3A92 / 1.0L 3A90 Hatchback CVT (A03A / A05A)", Powertrain.PETROL, "A03A"),
      ),
    ),

    // MAZDA SEDANS & HATCHBACKS
    VehicleModelSpec(
      brand = "Mazda",
      model = "Axela",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "EDM"),
      years = (2010..2024).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Skyactiv-G / 2.0L Hybrid-C (BM5FP / BYEFP)", Powertrain.HYBRID, "BM/BY"),
        VehicleVariantSpec("1.5L Skyactiv-D / Petrol 6AT (BP5P)", Powertrain.PETROL, "BP5P"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mazda",
      model = "Demio",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM"),
      years = (2010..2024).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.3L Skyactiv-G 6AT (DJ3FS / DE3FS)", Powertrain.PETROL, "DJ3FS"),
        VehicleVariantSpec("1.5L Skyactiv-D Clean Diesel (DJ5FS)", Powertrain.DIESEL, "DJ5FS"),
      ),
    ),

    // HYUNDAI SEDANS
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Elantra",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "KDM", "USDM", "IND"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.6L Smartstream / Nu 2.0L MPi 6AT (CN7 / AD)", Powertrain.PETROL, "CN7"),
        VehicleVariantSpec("1.6L GDi Hybrid 6DCT (CN7 HEV)", Powertrain.HYBRID, "CN7-HEV"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Sonata",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "KDM", "USDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L Hybrid / 2.5L Smartstream GDi (DN8 / LF)", Powertrain.HYBRID, "DN8"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Accent",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "KDM", "IND", "ASEAN"),
      years = (2012..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.4L / 1.6L Gamma MPi 6AT (HC / RB)", Powertrain.PETROL, "HC"),
      ),
    ),

    // SUZUKI SEDANS & HATCHBACKS
    VehicleModelSpec(
      brand = "Suzuki / Maruti",
      model = "Swift",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "IND", "ASEAN", "EDM"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L K12M / Z12E DualJet 5MT / CVT (ZC83S)", Powertrain.PETROL, "ZC83S"),
        VehicleVariantSpec("1.2L SHVS Mild Hybrid CVT (ZC53S / ZCEDS)", Powertrain.HYBRID, "ZC53S"),
      ),
    ),
    VehicleModelSpec(
      brand = "Suzuki / Maruti",
      model = "Alto",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "IND", "JDM"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("800cc F8D / 1.0L K10C AGS / 5MT (STD / K10)", Powertrain.PETROL, "HA36/K10"),
        VehicleVariantSpec("660cc R06A Ene-Charge CVT (HA36S JDM)", Powertrain.HYBRID, "HA36S"),
      ),
    ),
    VehicleModelSpec(
      brand = "Suzuki / Maruti",
      model = "WagonR",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "JDM", "IND"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("660cc R06A Mild Hybrid Stingray CVT (MH55S / MH35S)", Powertrain.HYBRID, "MH55S"),
        VehicleVariantSpec("1.0L / 1.2L K12N AGS (IND/BD)", Powertrain.PETROL, "MP52"),
      ),
    ),
    VehicleModelSpec(
      brand = "Suzuki / Maruti",
      model = "Dzire",
      category = VehicleCategory.PRIVATE_SEDANS_HATCHBACKS,
      marketCodes = listOf("BD", "IND", "ASEAN"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L K12N / Z12E DualJet AGS / 5MT", Powertrain.PETROL, "SZ-DZIRE"),
      ),
    ),
  )

  val suvsAnd4x4s: List<VehicleModelSpec> = listOf(
    // TOYOTA SUVs & 4x4s
    VehicleModelSpec(
      brand = "Toyota",
      model = "Land Cruiser Prado",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "ASEAN", "EDM"),
      years = (1996..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("J250 2.8L 1GD-FTV / 2.4L Turbo Hybrid 4WD (GDJ250 / TJH250)", Powertrain.DIESEL, "GDJ250"),
        VehicleVariantSpec("J150 2.7L 2TR-FE TX-L Petrol 6AT 4WD (TRJ150W)", Powertrain.PETROL, "TRJ150"),
        VehicleVariantSpec("J150 2.8L 1GD-FTV / 3.0L 1KD-FTV TZ-G Diesel (GDJ150 / KDJ150)", Powertrain.DIESEL, "GDJ150"),
        VehicleVariantSpec("J120 2.7L 2TR-FE / 3.0L 1KD-FTV TX/VX (TRJ120W / KDJ120W)", Powertrain.PETROL, "TRJ120"),
        VehicleVariantSpec("J90 3.0L 1KZ-TE Diesel / 3.4L 5VZ-FE V6 (KZJ95W / VZJ95W)", Powertrain.DIESEL, "KZJ95"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Land Cruiser V8 / 300",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "ASEAN", "USDM"),
      years = (1996..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("LC300 3.3L F33A-FTV Diesel / 3.5L V35A-FTS Twin-Turbo (FJA300 / VJA300)", Powertrain.DIESEL, "J300"),
        VehicleVariantSpec("LC200 4.6L 1UR-FE Petrol / 4.5L 1VD-FTV V8 Diesel (URJ202 / VDJ200)", Powertrain.PETROL, "URJ202"),
        VehicleVariantSpec("LC100 4.7L 2UZ-FE V8 / 4.2L 1HD-FTE Diesel (UZJ100 / HDJ100)", Powertrain.PETROL, "UZJ100"),
        VehicleVariantSpec("LC80 4.5L 1FZ-FE / 4.2L 1HZ 4WD (FZJ80 / HZJ80)", Powertrain.DIESEL, "HZJ80"),
        VehicleVariantSpec("LC70 4.2L 1HZ / 4.5L 1VD-FTV Troop Carrier & Pickup (HZJ79 / VDJ79)", Powertrain.DIESEL, "HZJ79"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Harrier",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L 3ZR-FAE Turbo/Valvematic (ZSU60 / ASU60 / MXUA80)", Powertrain.PETROL, "ZSU60"),
        VehicleVariantSpec("2.5L Hybrid E-Four (AVU65 / AXUH80)", Powertrain.HYBRID, "AXUH80"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "RAV4",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "USDM", "EDM", "ASEAN"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.5L Dynamic Force Hybrid AWD (AXAH54)", Powertrain.HYBRID, "XA50"),
        VehicleVariantSpec("2.0L M20A-FKS Direct Shift CVT (MXAA54)", Powertrain.PETROL, "MXAA54"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Rush",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "ASEAN", "JDM"),
      years = (2012..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 2NR-VE Dual VVT-i 7-Seater RWD 4AT (F800)", Powertrain.PETROL, "F800"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Fortuner",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "ASEAN", "IND"),
      years = (2015..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.8L 1GD-FTV Legender 4x4 6AT (GUN156)", Powertrain.DIESEL, "GUN156"),
        VehicleVariantSpec("2.7L 2TR-FE Petrol 6AT", Powertrain.PETROL, "TGN156"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "C-HR",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "EDM", "USDM"),
      years = (2017..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L 2ZR-FXE Hybrid G / S (ZYX10 / ZYX11)", Powertrain.HYBRID, "ZYX10"),
        VehicleVariantSpec("1.2L 8NR-FTS Turbo AWD (NGX50)", Powertrain.PETROL, "NGX50"),
      ),
    ),

    // HONDA SUVs
    VehicleModelSpec(
      brand = "Honda",
      model = "Vezel",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L i-DCD Hybrid RS / Z Sensing 7DCT (RU3)", Powertrain.HYBRID, "RU3"),
        VehicleVariantSpec("1.5L e:HEV Z / Play Hybrid (RV5)", Powertrain.HYBRID, "RV5"),
        VehicleVariantSpec("1.5L i-VTEC CVT (RU1)", Powertrain.PETROL, "RU1"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "HR-V",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "ASEAN", "USDM", "EDM"),
      years = (2015..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L VTEC Turbo / e:HEV RS (RV3 / RV5)", Powertrain.HYBRID, "RV3"),
        VehicleVariantSpec("1.8L i-VTEC R18ZF CVT (RU5)", Powertrain.PETROL, "RU5"),
      ),
    ),
    VehicleModelSpec(
      brand = "Honda",
      model = "CR-V",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "IND", "EDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L VTEC Turbo 7-Seater AWD CVT (RW2 / RT5)", Powertrain.PETROL, "RW2"),
        VehicleVariantSpec("2.0L e:HEV Hybrid AWD (RS6 / RT5)", Powertrain.HYBRID, "RS6"),
      ),
    ),

    // MITSUBISHI SUVs
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Outlander",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "ASEAN", "USDM", "EDM"),
      years = (2008..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L / 2.4L 4J11/4J12 MIVEC 7-Seater 4WD (GF7W / GF8W)", Powertrain.PETROL, "GF8W"),
        VehicleVariantSpec("2.4L Twin-Motor PHEV 4WD (GG3W / GN0W)", Powertrain.HYBRID, "GG3W"),
        VehicleVariantSpec("2.4L 4B12 / 3.0L V6 6B31 S-AWC (CW5W / CW6W / GN0W)", Powertrain.PETROL, "CW5W"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Pajero",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2006..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("Pajero V93 — 3.0L V6 6G72 MIVEC LWB 4WD (V93W)", Powertrain.PETROL, "V93W"),
        VehicleVariantSpec("Pajero V94 — 2.5L 4D56 / 2.8L 4M40 Intercooler Turbo (V94W)", Powertrain.DIESEL, "V94W"),
        VehicleVariantSpec("Pajero V96 — 3.5L V6 6G74 MIVEC Exceed 4WD (V96W)", Powertrain.PETROL, "V96W"),
        VehicleVariantSpec("Pajero V98 — 3.2L 4M41 DI-D Common-Rail Diesel 4WD (V98W)", Powertrain.DIESEL, "V98W"),
        VehicleVariantSpec("Pajero Sport — 2.4L 4N15 Clean Diesel 8AT 4WD (KS1W)", Powertrain.DIESEL, "KS1W"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Eclipse Cross",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "ASEAN", "EDM"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 4B40 Direct-Injection Turbo S-AWC (GK1W)", Powertrain.PETROL, "GK1W"),
        VehicleVariantSpec("2.4L PHEV Twin-Motor (GL3W)", Powertrain.HYBRID, "GL3W"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "ASX",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "ASEAN", "EDM"),
      years = (2012..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L 4B11 / 1.8L 4J10 MIVEC CVT (GA2W / GA4W)", Powertrain.PETROL, "GA2W"),
      ),
    ),

    // NISSAN SUVs
    VehicleModelSpec(
      brand = "Nissan",
      model = "X-Trail",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "USDM", "ASEAN", "EDM"),
      years = (2014..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L MR20DD 7-Seater 4WD Xtronic (NT32 / T32)", Powertrain.PETROL, "NT32"),
        VehicleVariantSpec("2.0L Hybrid (HNT32) / 1.5L e-POWER e-4ORCE (T33)", Powertrain.HYBRID, "T33"),
      ),
    ),
    VehicleModelSpec(
      brand = "Nissan",
      model = "Juke",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "JDM", "EDM"),
      years = (2011..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L HR15DE / 1.6L DIG-T Turbo (YF15 / NF15)", Powertrain.PETROL, "YF15"),
      ),
    ),
    VehicleModelSpec(
      brand = "Nissan",
      model = "Patrol",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "ASEAN", "JDM"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("5.6L VK56VD V8 7AT 4WD (Y62)", Powertrain.PETROL, "Y62"),
      ),
    ),

    // HYUNDAI / KIA / MG / HAVAL SUVs
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Tucson",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "KDM", "USDM", "EDM", "IND", "ASEAN"),
      years = (2016..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.6L T-GDi HEV Hybrid 6AT AWD (NX4)", Powertrain.HYBRID, "NX4"),
        VehicleVariantSpec("2.0L Nu MPi / Smartstream 6AT (TL / NX4)", Powertrain.PETROL, "NX4"),
        VehicleVariantSpec("2.0L CRDi Diesel 8AT", Powertrain.DIESEL, "NX4"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Santa Fe",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "KDM", "USDM", "EDM"),
      years = (2015..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.2L CRDi Diesel HTRAC / 1.6T Hybrid (TM / MX5)", Powertrain.HYBRID, "TM/MX5"),
        VehicleVariantSpec("2.4L Theta-II / 2.5L GDi 7-Seater", Powertrain.PETROL, "TM"),
      ),
    ),
    VehicleModelSpec(
      brand = "Hyundai",
      model = "Creta",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "IND", "ASEAN", "KDM"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L MPi Smartstream IVT (SU2)", Powertrain.PETROL, "SU2"),
        VehicleVariantSpec("1.5L T-GDi Turbo 7DCT", Powertrain.PETROL, "SU2"),
        VehicleVariantSpec("1.5L U2 CRDi Diesel 6AT", Powertrain.DIESEL, "SU2"),
      ),
    ),
    VehicleModelSpec(
      brand = "Kia",
      model = "Sportage",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "KDM", "USDM", "EDM", "ASEAN"),
      years = (2016..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.0L Nu MPi / 1.6L T-GDi HEV AWD (QL / NQ5)", Powertrain.HYBRID, "NQ5"),
      ),
    ),
    VehicleModelSpec(
      brand = "Kia",
      model = "Sorento",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "KDM", "USDM", "EDM"),
      years = (2015..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.2L Smartstream Diesel 8DCT / 1.6T Hybrid (MQ4)", Powertrain.HYBRID, "MQ4"),
      ),
    ),
    VehicleModelSpec(
      brand = "MG",
      model = "ZS",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "IND", "ASEAN", "EDM"),
      years = (2019..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L NSE VTi-Tech / 1.0L T-GDi 6AT (ZS11)", Powertrain.PETROL, "ZS11"),
        VehicleVariantSpec("51.1 kWh / 72.6 kWh ZS EV", Powertrain.EV, "ZS-EV"),
      ),
    ),
    VehicleModelSpec(
      brand = "MG",
      model = "HS",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "ASEAN", "EDM"),
      years = (2020..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L NetBlue T-GDi 7-Speed TST DCT (AS23)", Powertrain.PETROL, "AS23"),
        VehicleVariantSpec("1.5T PHEV Plug-in Hybrid", Powertrain.HYBRID, "AS23-PHEV"),
      ),
    ),
    VehicleModelSpec(
      brand = "Haval",
      model = "H6",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "ASEAN"),
      years = (2021..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L DHT Strong Hybrid (B01 HEV)", Powertrain.HYBRID, "B01-HEV"),
        VehicleVariantSpec("2.0L GW4N20 Turbo 7DCT AWD", Powertrain.PETROL, "B01"),
      ),
    ),
    VehicleModelSpec(
      brand = "Haval",
      model = "Jolion",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "ASEAN"),
      years = (2021..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L GW4G15K Turbo 7DCT / 1.5L DHT Hybrid", Powertrain.HYBRID, "A01"),
      ),
    ),
    VehicleModelSpec(
      brand = "Tata",
      model = "Hexa",
      category = VehicleCategory.SUVS_CROSSOVERS_4X4,
      marketCodes = listOf("BD", "IND"),
      years = (2017..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.2L Varicor 400 Diesel 6AT / 6MT AWD (XT / XTA / XE)", Powertrain.DIESEL, "HEXA-XTA"),
      ),
    ),
  )

  val microbusesMpvsAndVans: List<VehicleModelSpec> = listOf(
    VehicleModelSpec(
      brand = "Toyota",
      model = "Noah",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L Hybrid Si / WxB eCVT (ZWR80 / ZWR90)", Powertrain.HYBRID, "ZWR80"),
        VehicleVariantSpec("2.0L 3ZR-FAE Valvematic CVT (ZRR80 / ZRR70)", Powertrain.PETROL, "ZRR80"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Voxy",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2010..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L Hybrid ZS Kirameki (ZWR80 / ZWR90)", Powertrain.HYBRID, "ZWR80"),
        VehicleVariantSpec("2.0L 3ZR-FAE ZS CVT (ZRR80)", Powertrain.PETROL, "ZRR80"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Esquire",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM"),
      years = (2014..2022).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.8L Hybrid Gi / Xi eCVT (ZWR80G)", Powertrain.HYBRID, "ZWR80G"),
        VehicleVariantSpec("2.0L 3ZR-FAE Gi CVT (ZRR80G)", Powertrain.PETROL, "ZRR80G"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Alphard",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.5L 2AR-FXE Hybrid Executive Lounge (AYH30 / AAHH40)", Powertrain.HYBRID, "AYH30"),
        VehicleVariantSpec("2.5L 2AR-FE / 3.5L V6 Petrol (AGH30 / GGH30)", Powertrain.PETROL, "AGH30"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Vellfire",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.5L Hybrid Z / Executive Lounge (AYH30 / AAHH40)", Powertrain.HYBRID, "AYH30"),
        VehicleVariantSpec("2.5L 2AR-FE Z-G Edition (AGH30)", Powertrain.PETROL, "AGH30"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "HiAce",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN", "IND"),
      years = (2008..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.7L 2TR-FE Super GL Petrol 6AT (TRH200 / TRH214)", Powertrain.PETROL, "TRH200"),
        VehicleVariantSpec("2.8L 1GD-FTV Clean Diesel DX/GL (GDH200 / GDH300)", Powertrain.DIESEL, "GDH200"),
        VehicleVariantSpec("3.0L 1KD-FTV D-4D Commuter (KDH200)", Powertrain.DIESEL, "KDH200"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "LiteAce",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2008..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 3SZ-VE / 2NR-VE Van & Truck (S402M / S403M)", Powertrain.PETROL, "S402M"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Avanza",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "ASEAN"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 2NR-VE / 1.3L 1NR-VE 7-Seater MPV (F654 / W101)", Powertrain.PETROL, "W101"),
      ),
    ),
    VehicleModelSpec(
      brand = "Toyota",
      model = "Sienta",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2015..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L Hybrid G / X 7-Seater (NHP170G / MXPL10G)", Powertrain.HYBRID, "NHP170G"),
        VehicleVariantSpec("1.5L 2NR-FKE Petrol CVT (NSP170G)", Powertrain.PETROL, "NSP170G"),
      ),
    ),
    VehicleModelSpec(
      brand = "Nissan",
      model = "Serena",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.2L / 1.4L e-POWER Highway Star (HFC27 / C28)", Powertrain.HYBRID, "HFC27"),
        VehicleVariantSpec("2.0L MR20DD S-Hybrid Xtronic (C26 / C27)", Powertrain.HYBRID, "C27"),
      ),
    ),
    VehicleModelSpec(
      brand = "Nissan",
      model = "Caravan",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "ASEAN"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.5L YD25DDTi Diesel / QR25DE Petrol NV350 (E26)", Powertrain.DIESEL, "E26"),
      ),
    ),
    VehicleModelSpec(
      brand = "Nissan",
      model = "NV200",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM", "USDM", "EDM"),
      years = (2010..2025).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.6L HR16DE Vanette / Evalia (VM20 / M20)", Powertrain.PETROL, "M20"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Delica",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "JDM"),
      years = (2012..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("2.2L 4N14 Clean Diesel 4WD / 2.4L D:5 (CV1W / CV5W)", Powertrain.DIESEL, "CV1W"),
      ),
    ),
    VehicleModelSpec(
      brand = "Mitsubishi",
      model = "Xpander",
      category = VehicleCategory.MICROBUSES_MPVS_VANS,
      marketCodes = listOf("BD", "ASEAN"),
      years = (2018..2026).toList().reversed(),
      variants = listOf(
        VehicleVariantSpec("1.5L 4A91 MIVEC 7-Seater MPV Cross (NC1W)", Powertrain.PETROL, "NC1W"),
      ),
    ),
  )
}
