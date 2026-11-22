package com.example.data

import java.util.Locale

data class VinDecodeMatch(
  val marketCode: String,
  val country: String,
  val modelSpec: VehicleModelSpec,
  val variantSpec: VehicleVariantSpec,
  val inferredYear: Int,
  val matchReason: String,
)

object VinDecoderEngine {

  private data class IndexedVariantEntry(
    val spec: VehicleModelSpec,
    val variant: VehicleVariantSpec,
    val marketCode: String,
    val country: String,
    val latestYear: Int,
    val upperVariantName: String,
    val chassisTokens: List<String>,
    val upperModelHaystack: String,
  )

  private val wmiBrandMap = mapOf(
    "JTD" to "Toyota", "JTE" to "Toyota", "JTM" to "Toyota", "JTN" to "Toyota",
    "MR0" to "Toyota", "MHF" to "Toyota",
    "JHM" to "Honda", "MRH" to "Honda", "SHH" to "Honda", "1HG" to "Honda",
    "JN1" to "Nissan", "JN8" to "Nissan",
    "JA3" to "Mitsubishi", "JA4" to "Mitsubishi", "MMB" to "Mitsubishi",
    "JMZ" to "Mazda", "JM1" to "Mazda",
    "KMH" to "Hyundai", "MAL" to "Hyundai",
    "KNA" to "Kia", "KND" to "Kia",
    "JS2" to "Suzuki / Maruti", "MA3" to "Suzuki / Maruti",
    "MAT" to "Tata", "MA1" to "Mahindra", "MB1" to "Ashok Leyland",
    "MP1" to "Isuzu", "JAA" to "Isuzu", "JHH" to "Hino",
    "LSG" to "MG", "LGW" to "Haval", "WBA" to "BMW", "WVW" to "Volkswagen",
  )

  private val isoYearCodes = mapOf(
    'A' to 2010, 'B' to 2011, 'C' to 2012, 'D' to 2013, 'E' to 2014,
    'F' to 2015, 'G' to 2016, 'H' to 2017, 'J' to 2018, 'K' to 2019,
    'L' to 2020, 'M' to 2021, 'N' to 2022, 'P' to 2023, 'R' to 2024,
    'S' to 2025, 'T' to 2026,
  )

  private val marketCountryByCode: Map<String, String> by lazy {
    VehicleCatalogData.markets.associate { it.marketCode to it.country }
  }

  private val preindexedVariants: List<IndexedVariantEntry> by lazy {
    VehicleCatalogData.models.flatMap { spec ->
      val marketCode = if (spec.marketCodes.contains("BD")) "BD" else spec.marketCodes.first()
      val country = marketCountryByCode[marketCode] ?: "Bangladesh"
      val latestYear = spec.years.firstOrNull() ?: 2022
      val modelHaystack = "${spec.brand} ${spec.model} ${spec.category.displayName}".uppercase(Locale.US)
      spec.variants.map { variant ->
        IndexedVariantEntry(
          spec = spec,
          variant = variant,
          marketCode = marketCode,
          country = country,
          latestYear = latestYear,
          upperVariantName = variant.name.uppercase(Locale.US),
          chassisTokens = variant.chassisCode.uppercase(Locale.US).split("/", " ").filter { it.length >= 2 },
          upperModelHaystack = modelHaystack,
        )
      }
    }
  }

  private val modelsByLowerBrand: Map<String, List<VehicleModelSpec>> by lazy {
    VehicleCatalogData.models.groupBy { it.brand.lowercase(Locale.US) }
  }

  fun searchByVinOrModel(rawInput: String): List<VinDecodeMatch> {
    val clean = rawInput.trim().uppercase(Locale.US)
    if (clean.length < 2) return emptyList()

    val prefixOnly = clean.substringBefore("-").trim()
    val results = ArrayList<VinDecodeMatch>(16)

    // 1. Direct JDM Chassis / Frame Code match (pre-indexed for sub-millisecond lookup)
    for (entry in preindexedVariants) {
      val hit = entry.chassisTokens.any { token ->
        prefixOnly == token ||
          clean.startsWith(token) ||
          (clean.length >= 3 && token.contains(clean)) ||
          entry.upperVariantName.contains(clean)
      }
      if (hit) {
        results += VinDecodeMatch(
          marketCode = entry.marketCode,
          country = entry.country,
          modelSpec = entry.spec,
          variantSpec = entry.variant,
          inferredYear = entry.latestYear,
          matchReason = "Chassis / Frame Match (${entry.variant.chassisCode})",
        )
      }
    }

    // 2. 17-Char ISO VIN WMI Prefix match
    if (clean.length >= 3) {
      val wmi = clean.take(3)
      val brand = wmiBrandMap[wmi]
      if (brand != null) {
        val inferredYr = if (clean.length >= 10) isoYearCodes[clean[9]] else null
        val brandModels = modelsByLowerBrand[brand.lowercase(Locale.US)].orEmpty()
        for (spec in brandModels.take(6)) {
          val variant = spec.variants.first()
          val year = inferredYr?.takeIf { it in spec.years } ?: spec.years.first()
          val marketCode = if (spec.marketCodes.contains("BD")) "BD" else spec.marketCodes.first()
          val country = marketCountryByCode[marketCode] ?: "Bangladesh"
          results += VinDecodeMatch(
            marketCode = marketCode,
            country = country,
            modelSpec = spec,
            variantSpec = variant,
            inferredYear = year,
            matchReason = "VIN WMI Decode ($wmi → $brand)",
          )
        }
      }
    }

    // 3. Model / Brand fuzzy search
    val terms = clean.split(Regex("\\s+")).filter { it.isNotBlank() }
    val seenModels = HashSet<String>()
    for (entry in preindexedVariants) {
      val modelKey = "${entry.spec.brand}|${entry.spec.model}"
      if (!seenModels.add(modelKey)) continue
      if (terms.all { entry.upperModelHaystack.contains(it) }) {
        results += VinDecodeMatch(
          marketCode = entry.marketCode,
          country = entry.country,
          modelSpec = entry.spec,
          variantSpec = entry.variant,
          inferredYear = entry.latestYear,
          matchReason = "Model Lookup (${entry.spec.category.shortLabel})",
        )
      }
    }

    return results
      .distinctBy { "${it.modelSpec.brand}|${it.modelSpec.model}|${it.variantSpec.chassisCode}" }
      .take(12)
  }
}
