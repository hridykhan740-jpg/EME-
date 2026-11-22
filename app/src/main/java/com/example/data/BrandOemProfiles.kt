package com.example.data

import com.example.model.Powertrain
import java.util.Locale

data class BrandOemSpec(
  val oemBrand: String,
  val epcCatalogName: String,
  val codePrefix: String,
  val oilVisc: String,
  val oilFilterCode: String,
  val airFilterCode: String,
  val fuelFilterCode: String,
  val cabinFilterCode: String,
  val sparkCode: String,
  val clutchKitCode: String,
  val steeringRackCode: String,
  val radiatorCode: String,
  val acCompressorCode: String,
  val exhaustCatCode: String,
  val padCode: String,
  val rotorCode: String,
  val batterySpec: String,
)

object BrandOemProfiles {

  fun specFor(brand: String, model: String, powertrain: Powertrain): BrandOemSpec {
    val b = brand.lowercase(Locale.US)
    val m = model.lowercase(Locale.US)
    return when {
      b.contains("toyota") || b.contains("lexus") -> {
        val isAxioFielder = m.contains("axio") || m.contains("fielder") || m.contains("probox") || m.contains("succeed")
        val isPremioAllion = m.contains("premio") || m.contains("allion")
        val isNoahVoxy = m.contains("noah") || m.contains("voxy") || m.contains("esquire")
        val isHiAce = m.contains("hiace") || m.contains("liteace")
        val isPradoLc = m.contains("prado") || m.contains("land cruiser") || m.contains("hilux") || m.contains("fortuner")
        val isCommercialDyna = m.contains("dyna")
        BrandOemSpec(
          oemBrand = "Toyota Genuine Parts",
          epcCatalogName = "Toyota EPC (Electronic Parts Catalog)",
          codePrefix = "TOY",
          oilVisc = when {
            (isHiAce || isPradoLc || isCommercialDyna) && powertrain == Powertrain.DIESEL -> "5W-30 DL-1 / ACEA C2"
            (isAxioFielder || m.contains("aqua") || m.contains("prius")) && powertrain == Powertrain.HYBRID -> "0W-16 / 0W-20 API SP"
            else -> "0W-20 API SP / ILSAC GF-6A"
          },
          oilFilterCode = when {
            isAxioFielder || m.contains("vitz") || m.contains("yaris") || m.contains("aqua") -> "90915-YZZF2"
            isPremioAllion -> if (powertrain == Powertrain.PETROL) "90915-YZZF2" else "04152-YZZA6"
            isNoahVoxy || m.contains("prius") || m.contains("c-hr") -> "04152-YZZA6"
            isHiAce || isCommercialDyna -> if (powertrain == Powertrain.DIESEL) "90915-30002" else "90915-YZZD2"
            isPradoLc -> if (powertrain == Powertrain.DIESEL) "90915-YZZD4" else "90915-YZZD2"
            else -> "90915-YZZN1"
          },
          airFilterCode = when {
            isAxioFielder -> "17801-21060"
            isPremioAllion -> "17801-21050"
            isNoahVoxy -> "17801-37021"
            isHiAce || isCommercialDyna -> "17801-30060"
            isPradoLc -> "17801-31090"
            else -> "17801-F0020"
          },
          fuelFilterCode = when {
            powertrain == Powertrain.DIESEL -> "23390-0L070"
            isNoahVoxy || isPremioAllion -> "77024-28040"
            else -> "77024-52170"
          },
          cabinFilterCode = when {
            isAxioFielder || isPremioAllion || isNoahVoxy -> "87139-52040"
            isPradoLc -> "87139-YZZ20"
            else -> "87139-YZZ82"
          },
          sparkCode = when {
            isAxioFielder -> "90919-01275"
            isPremioAllion || isNoahVoxy -> "90919-01253"
            else -> "90919-01298"
          },
          clutchKitCode = if (isHiAce || isPradoLc || isCommercialDyna) "31210-35291" else "31210-12281",
          steeringRackCode = if (isPradoLc) "44200-60170" else "45510-12450",
          radiatorCode = if (isPradoLc || isHiAce) "16400-30172" else "16400-21300",
          acCompressorCode = if (powertrain == Powertrain.HYBRID) "88370-47032" else "88310-02710",
          exhaustCatCode = if (powertrain == Powertrain.DIESEL) "18450-11020" else "17410-21B30",
          padCode = when {
            isAxioFielder -> "04465-52320"
            isPremioAllion -> "04465-21010"
            isNoahVoxy -> "04465-28520"
            isHiAce -> "04465-26421"
            isPradoLc -> "04465-60320"
            else -> "04465-33480"
          },
          rotorCode = when {
            isAxioFielder -> "43512-52130"
            isPremioAllion -> "43512-21020"
            isNoahVoxy -> "43512-28190"
            isPradoLc -> "43512-60190"
            else -> "43512-06150"
          },
          batterySpec = when {
            (isAxioFielder || isNoahVoxy || m.contains("aqua")) && powertrain == Powertrain.HYBRID -> "S34B20R AGM Auxiliary 12V"
            isPradoLc || isHiAce || isCommercialDyna -> "80D26L / 105D31L 12V Heavy-Duty"
            else -> "46B24R / LN2 12V EFB"
          },
        )
      }

      b.contains("honda") -> {
        val isVezel = m.contains("vezel") || m.contains("hr-v")
        val isGrace = m.contains("grace") || m.contains("city") || m.contains("fit")
        BrandOemSpec(
          oemBrand = "Honda Genuine",
          epcCatalogName = "Honda EPC (Electronic Parts Catalog)",
          codePrefix = "HON",
          oilVisc = "0W-20 Honda Ultra LEO / Type 2.0",
          oilFilterCode = "15400-RTA-003",
          airFilterCode = if (isVezel || isGrace) "17220-5R0-008" else "17220-64A-A00",
          fuelFilterCode = if (isVezel) "17048-T7A-000" else "17048-T5A-000",
          cabinFilterCode = if (isVezel || isGrace) "80291-T5A-J01" else "80292-TBA-A11",
          sparkCode = if (isVezel || isGrace) "12290-5R0-003" else "12290-59B-003",
          clutchKitCode = "22105-RNA-305",
          steeringRackCode = if (isVezel) "53601-T7J-H01" else "53601-TBA-A04",
          radiatorCode = if (isVezel) "19010-5R0-003" else "19010-5AA-A01",
          acCompressorCode = if (powertrain == Powertrain.HYBRID) "38810-5R8-J01" else "38810-5AA-A01",
          exhaustCatCode = "18160-5R0-000",
          padCode = when {
            isVezel -> "45022-T7J-H00"
            isGrace -> "45022-T5A-000"
            else -> "45022-TGG-A01"
          },
          rotorCode = if (isVezel) "45251-T7A-000" else "45251-TVA-A00",
          batterySpec = if (isVezel || isGrace) "44B19L / N-55 EFB 12V" else "55B24L / Group 51R 12V",
        )
      }

      b.contains("mitsubishi") -> {
        val isCanter = m.contains("canter") || m.contains("l200")
        val isPajero = m.contains("pajero")
        BrandOemSpec(
          oemBrand = "Mitsubishi Motors / Fuso Genuine",
          epcCatalogName = "Mitsubishi ASA / Fuso EPC",
          codePrefix = "MMC",
          oilVisc = if (powertrain == Powertrain.DIESEL) "15W-40 / 5W-30 ACEA C3" else "0W-20 / 5W-30 API SP",
          oilFilterCode = when {
            m.contains("canter") -> "ME013307"
            isPajero && powertrain == Powertrain.DIESEL -> "ME013307 / 1230A182"
            isPajero -> "MD352627"
            powertrain == Powertrain.DIESEL -> "1230A182"
            else -> "MZ690070"
          },
          airFilterCode = when {
            m.contains("canter") -> "ME422778"
            isPajero -> "1500A608 / MR571476"
            else -> "1500A513"
          },
          fuelFilterCode = if (powertrain == Powertrain.DIESEL) "1770A233" else "1770A260",
          cabinFilterCode = if (isPajero) "7803A028" else "7803A043",
          sparkCode = if (powertrain == Powertrain.DIESEL) "ME203539" else "1822A086",
          clutchKitCode = if (isCanter || isPajero) "ME521104" else "2304A075",
          steeringRackCode = if (isCanter) "MK383850" else if (isPajero) "4410A323" else "4410A453",
          radiatorCode = if (isCanter) "ME403638" else if (isPajero) "1350A141" else "1350A297",
          acCompressorCode = if (isPajero) "7813A405" else "7813A618",
          exhaustCatCode = "1570C312",
          padCode = if (isPajero || isCanter) "4605A879" else "4605B005",
          rotorCode = if (isPajero) "4615A035" else "4615A178",
          batterySpec = if (isPajero || isCanter) "95D31L / N100 Heavy-Duty" else "75D23L / 85D26L 12V",
        )
      }

      b.contains("nissan") -> BrandOemSpec(
        oemBrand = "Nissan Genuine Parts",
        epcCatalogName = "Nissan FAST Electronic Catalog",
        codePrefix = "NIS",
        oilVisc = "0W-20 API SP / Nissan Ester",
        oilFilterCode = "15208-65F0E",
        airFilterCode = "16546-4BA1A",
        fuelFilterCode = "16400-4BA0A",
        cabinFilterCode = "27277-4BA0A",
        sparkCode = "22401-ED815",
        clutchKitCode = "30100-ED80A",
        steeringRackCode = "49001-4BA0A",
        radiatorCode = "21410-4BA0A",
        acCompressorCode = "92600-4BA0A",
        exhaustCatCode = "20010-4BA0A",
        padCode = "D1060-4BA0A",
        rotorCode = "40206-4BA0A",
        batterySpec = "Q-85 EFB / 55B24L 12V",
      )

      b.contains("mazda") -> BrandOemSpec(
        oemBrand = "Mazda Genuine Parts",
        epcCatalogName = "Mazda GEPC Electronic Catalog",
        codePrefix = "MAZ",
        oilVisc = "0W-20 Skyactiv Full Synthetic",
        oilFilterCode = "PE01-14-302B",
        airFilterCode = "PE07-13-3A0A",
        fuelFilterCode = "PE01-13-ZE0",
        cabinFilterCode = "KD45-61-J6X",
        sparkCode = "PE5R-18-110",
        clutchKitCode = "PE01-16-460",
        steeringRackCode = "B45A-32-110",
        radiatorCode = "PE01-15-200",
        acCompressorCode = "KD45-61-450",
        exhaustCatCode = "PE01-20-55X",
        padCode = "B4Y0-33-28ZA",
        rotorCode = "B45G-33-251",
        batterySpec = "Q-85 i-Stop EFB 12V",
      )

      b.contains("hyundai") || b.contains("kia") -> BrandOemSpec(
        oemBrand = "Hyundai / Kia Mobis OEM",
        epcCatalogName = "Hyundai / Kia Mobis WPC Catalog",
        codePrefix = "MOB",
        oilVisc = "0W-20 / 5W-30 ACEA C5",
        oilFilterCode = "26350-2M000",
        airFilterCode = "28113-L1000",
        fuelFilterCode = "31112-N9000",
        cabinFilterCode = "97133-N9100",
        sparkCode = "18868-08115",
        clutchKitCode = "41100-26030",
        steeringRackCode = "56500-N9000",
        radiatorCode = "25310-N9000",
        acCompressorCode = "97701-N9000",
        exhaustCatCode = "28510-2M400",
        padCode = "58101-N9A00",
        rotorCode = "51712-N9000",
        batterySpec = "DIN60L 12V 60Ah AGM",
      )

      b.contains("suzuki") || b.contains("maruti") -> BrandOemSpec(
        oemBrand = "Suzuki Genuine / MGP",
        epcCatalogName = "Suzuki EPC / MGP Catalog",
        codePrefix = "SGP",
        oilVisc = "0W-16 / 0W-20 Ecstar",
        oilFilterCode = "16510-M68K00",
        airFilterCode = "13780-M68P00",
        fuelFilterCode = "15410-M68K00",
        cabinFilterCode = "95860-M74L00",
        sparkCode = "09482-00634",
        clutchKitCode = "22100-M68P00",
        steeringRackCode = "48580-M68P00",
        radiatorCode = "17700-M68P00",
        acCompressorCode = "95200-M68P00",
        exhaustCatCode = "14190-M68P00",
        padCode = "55810-M74L01",
        rotorCode = "55311-M74L00",
        batterySpec = "38B20L / 44B20L 12V",
      )

      b.contains("tata") || b.contains("ashok") || b.contains("mahindra") || b.contains("eicher") -> {
        val isHeavy = m.contains("1615") || m.contains("prima") || m.contains("ecomet") || m.contains("viking") || m.contains("3015")
        val prefix = when {
          b.contains("ashok") -> "AL"
          b.contains("mahindra") -> "MAH"
          b.contains("eicher") -> "EIC"
          else -> "TAT"
        }
        BrandOemSpec(
          oemBrand = "${brand.uppercase(Locale.US)} Commercial OEM",
          epcCatalogName = "$brand Commercial E-Catalog",
          codePrefix = prefix,
          oilVisc = if (isHeavy) "15W-40 CI-4+ Heavy-Duty Diesel" else "5W-30 / 15W-40 ACEA C3",
          oilFilterCode = when {
            b.contains("ashok") -> "F8835100"
            b.contains("mahindra") -> "0305AB0291N"
            b.contains("eicher") -> "ID207619"
            isHeavy -> "2525-1813-0102"
            else -> "2790-1813-0102"
          },
          airFilterCode = if (isHeavy) "2786-0913-0101" else "5442-0913-0101",
          fuelFilterCode = if (isHeavy) "2786-0911-0114" else "2790-0911-0108",
          cabinFilterCode = "5438-8340-0104",
          sparkCode = "2791-1511-0103",
          clutchKitCode = if (isHeavy) "2651-2520-0101" else "2702-2520-0104",
          steeringRackCode = if (isHeavy) "2641-4620-0102" else "2702-4620-0101",
          radiatorCode = if (isHeavy) "2525-5010-0105" else "2790-5010-0102",
          acCompressorCode = "5438-8340-0210",
          exhaustCatCode = if (isHeavy) "2525-4920-0108" else "2790-4920-0103",
          padCode = if (isHeavy) "2651-4210-0110" else "2702-4210-0118",
          rotorCode = "2702-4210-0104",
          batterySpec = if (isHeavy) "24V Dual N150 Heavy Commercial" else "DIN55 / N70 High-Temp",
        )
      }

      b.contains("isuzu") || b.contains("hino") -> BrandOemSpec(
        oemBrand = "${brand.uppercase(Locale.US)} Genuine Commercial",
        epcCatalogName = "$brand EPC Commercial Parts Catalog",
        codePrefix = if (b.contains("hino")) "HNO" else "ISZ",
        oilVisc = "15W-40 DH-2 / 10W-40 Heavy Diesel",
        oilFilterCode = if (b.contains("hino")) "15607-2190" else "8-98165071-0",
        airFilterCode = if (b.contains("hino")) "17801-E0010" else "8-98140265-0",
        fuelFilterCode = if (b.contains("hino")) "23401-1730" else "8-98159693-0",
        cabinFilterCode = if (b.contains("hino")) "S8850-E0020" else "8-98139428-0",
        sparkCode = if (b.contains("hino")) "19110-1220" else "8-98018995-0",
        clutchKitCode = if (b.contains("hino")) "31210-E0410" else "8-97351794-0",
        steeringRackCode = if (b.contains("hino")) "44110-E0120" else "8-98061420-0",
        radiatorCode = if (b.contains("hino")) "16090-E0040" else "8-98142510-0",
        acCompressorCode = if (b.contains("hino")) "88310-E0060" else "8-98156220-0",
        exhaustCatCode = if (b.contains("hino")) "17401-E0220" else "8-98174110-0",
        padCode = if (b.contains("hino")) "04465-E0050" else "8-98102251-0",
        rotorCode = if (b.contains("hino")) "43512-E0020" else "8-98051412-0",
        batterySpec = "Dual 12V N120 / N150 Commercial Series",
      )

      b.contains("mg") || b.contains("haval") -> BrandOemSpec(
        oemBrand = "${brand.uppercase(Locale.US)} Genuine OEM",
        epcCatalogName = "$brand SAIC / GWM EPC",
        codePrefix = if (b.contains("mg")) "MGS" else "GWM",
        oilVisc = "0W-20 / 5W-30 SN+ Turbo",
        oilFilterCode = if (b.contains("mg")) "10167486" else "1017100AEG01",
        airFilterCode = if (b.contains("mg")) "10433262" else "1109101XGW01",
        fuelFilterCode = if (b.contains("mg")) "10294410" else "1117100XKY00",
        cabinFilterCode = if (b.contains("mg")) "10367418" else "8104400XKN01A",
        sparkCode = if (b.contains("mg")) "10417745" else "3707100AEG01",
        clutchKitCode = "10291042",
        steeringRackCode = "10489120",
        radiatorCode = "10334890",
        acCompressorCode = "10449100",
        exhaustCatCode = "10491230",
        padCode = if (b.contains("mg")) "10421890" else "3501110XGW01",
        rotorCode = if (b.contains("mg")) "10421895" else "3501075XGW01",
        batterySpec = "DIN60 12V 60Ah EFB",
      )

      b.contains("byd") -> BrandOemSpec(
        oemBrand = "BYD Auto Genuine",
        epcCatalogName = "BYD e-Platform EPC",
        codePrefix = "BYD",
        oilVisc = "e-Axle Reduction Gear Oil",
        oilFilterCode = "SC2E-1701310",
        airFilterCode = "SC2E-8121210",
        fuelFilterCode = "EV-NO-FUEL-SYS",
        cabinFilterCode = "HCE-8121230-PM25",
        sparkCode = "EM2E-2104100",
        clutchKitCode = "SC2E-1701100",
        steeringRackCode = "SC2E-3401010",
        radiatorCode = "SC2E-1301010",
        acCompressorCode = "SC2E-8103010-HV",
        exhaustCatCode = "EV-ZERO-EXHAUST",
        padCode = "SC2E-3501110",
        rotorCode = "SC2E-3501075",
        batterySpec = "12V LFP Auxiliary 45Ah",
      )

      b.contains("bmw") -> BrandOemSpec(
        oemBrand = "BMW Group Original",
        epcCatalogName = "BMW ETK Electronic Parts Catalog",
        codePrefix = "BMW",
        oilVisc = "0W-20 LL-17 FE+",
        oilFilterCode = "11-42-8-575-211",
        airFilterCode = "13-71-8-577-170",
        fuelFilterCode = "16-12-7-451-424",
        cabinFilterCode = "64-11-9-382-886",
        sparkCode = "12-12-0-040-551",
        clutchKitCode = "21-20-8-631-806",
        steeringRackCode = "32-10-6-892-975",
        radiatorCode = "17-11-8-672-103",
        acCompressorCode = "64-52-9-364-869",
        exhaustCatCode = "18-30-8-672-214",
        padCode = "34-11-6-889-570",
        rotorCode = "34-11-6-860-911",
        batterySpec = "H8 12V 90Ah AGM",
      )

      else -> BrandOemSpec(
        oemBrand = "$brand OEM Genuine",
        epcCatalogName = "$brand Official EPC",
        codePrefix = brand.take(3).uppercase(Locale.US),
        oilVisc = "0W-20 / 5W-30 Full Synthetic",
        oilFilterCode = "${brand.take(2).uppercase(Locale.US)}-1540-901",
        airFilterCode = "${brand.take(2).uppercase(Locale.US)}-1780-412",
        fuelFilterCode = "${brand.take(2).uppercase(Locale.US)}-2330-510",
        cabinFilterCode = "${brand.take(2).uppercase(Locale.US)}-8713-220",
        sparkCode = "${brand.take(2).uppercase(Locale.US)}-9091-771",
        clutchKitCode = "${brand.take(2).uppercase(Locale.US)}-3121-204",
        steeringRackCode = "${brand.take(2).uppercase(Locale.US)}-4551-308",
        radiatorCode = "${brand.take(2).uppercase(Locale.US)}-1640-611",
        acCompressorCode = "${brand.take(2).uppercase(Locale.US)}-8831-409",
        exhaustCatCode = "${brand.take(2).uppercase(Locale.US)}-1741-522",
        padCode = "${brand.take(2).uppercase(Locale.US)}-4502-310",
        rotorCode = "${brand.take(2).uppercase(Locale.US)}-4351-608",
        batterySpec = "Group 48 H6 12V 70Ah AGM",
      )
    }
  }
}
