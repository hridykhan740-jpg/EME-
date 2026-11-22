package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.MarketSpec
import com.example.data.VehicleCatalogData
import com.example.data.VehicleModelSpec
import com.example.data.VehicleVariantSpec
import com.example.data.VinDecodeMatch
import com.example.data.VinDecoderEngine
import com.example.model.Powertrain
import com.example.model.VehicleCategory
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan

object VehicleStepRoutes {
  const val COUNTRY = "vehicle_step/country"
  const val BRAND = "vehicle_step/brand"
  const val MODEL = "vehicle_step/model"
  const val YEAR = "vehicle_step/year"
  const val VARIANT = "vehicle_step/variant"
}

private enum class VehicleStep(val route: String, val index: Int, val label: String, val subtitle: String) {
  COUNTRY(VehicleStepRoutes.COUNTRY, 0, "Country", "1. Country & Market Specification"),
  BRAND(VehicleStepRoutes.BRAND, 1, "Brand", "2. Manufacturer & Category"),
  MODEL(VehicleStepRoutes.MODEL, 2, "Model", "3. Select Vehicle Model"),
  YEAR(VehicleStepRoutes.YEAR, 3, "Year", "4. Model Year"),
  VARIANT(VehicleStepRoutes.VARIANT, 4, "Variant", "5. Variant & Odometer");

  companion object {
    fun fromRoute(route: String?): VehicleStep =
      entries.firstOrNull { it.route == route } ?: COUNTRY
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSelectorScreen(
  canDismiss: Boolean,
  isSaving: Boolean,
  onSaveVehicle: (
    country: String,
    marketCode: String,
    brand: String,
    model: String,
    year: Int,
    variant: String,
    powertrain: Powertrain,
    odometerKm: Long,
    nickname: String?,
  ) -> Unit,
  onBackDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val stepNavController = rememberNavController()
  val backStackEntry by stepNavController.currentBackStackEntryAsState()
  val currentStep = VehicleStep.fromRoute(backStackEntry?.destination?.route)

  var selectedMarket by remember { mutableStateOf<MarketSpec?>(null) }
  var selectedCategory by remember { mutableStateOf<VehicleCategory?>(null) }
  var selectedBrand by remember { mutableStateOf<String?>(null) }
  var selectedModelSpec by remember { mutableStateOf<VehicleModelSpec?>(null) }
  var selectedYear by remember { mutableStateOf<Int?>(null) }
  var selectedVariant by remember { mutableStateOf<VehicleVariantSpec?>(null) }
  var odometerText by remember { mutableStateOf("25000") }
  var nicknameText by remember { mutableStateOf("") }
  var vinModelQuery by remember { mutableStateOf("") }

  val vinMatches = remember(vinModelQuery) {
    VinDecoderEngine.searchByVinOrModel(vinModelQuery)
  }

  BackHandler(enabled = currentStep == VehicleStep.COUNTRY && canDismiss) {
    onBackDismiss()
  }

  val navigateBack: () -> Unit = {
    val popped = stepNavController.popBackStack()
    if (!popped && canDismiss) {
      onBackDismiss()
    }
  }

  val applyVinMatch: (VinDecodeMatch) -> Unit = { match ->
    selectedMarket = VehicleCatalogData.markets.firstOrNull { it.marketCode == match.marketCode }
      ?: VehicleCatalogData.markets.first()
    selectedCategory = match.modelSpec.category
    selectedBrand = match.modelSpec.brand
    selectedModelSpec = match.modelSpec
    selectedYear = match.inferredYear
    selectedVariant = match.variantSpec
    vinModelQuery = ""
    stepNavController.navigate(VehicleStepRoutes.VARIANT) {
      launchSingleTop = true
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Vehicle Specification",
              style = MaterialTheme.typography.titleLarge,
            )
            Text(
              text = currentStep.subtitle,
              style = MaterialTheme.typography.bodySmall,
              color = TelemetryCyan,
            )
          }
        },
        navigationIcon = {
          if (currentStep != VehicleStep.COUNTRY || canDismiss) {
            IconButton(
              onClick = navigateBack,
              modifier = Modifier.testTag("selector_back_button"),
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
        ),
      )
    },
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentAlignment = Alignment.TopCenter,
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 680.dp)
          .padding(horizontal = 16.dp),
      ) {
        // Dual Search Bar: VIN / JDM Chassis Frame (e.g. NKE165, NZT260) OR Model Name
        OutlinedTextField(
          value = vinModelQuery,
          onValueChange = { vinModelQuery = it },
          placeholder = { Text("Quick VIN / Frame (e.g. NKE165, JTD) or Model (HiAce, Hino)…") },
          leadingIcon = { Icon(Icons.Rounded.QrCodeScanner, contentDescription = null, tint = TelemetryCyan) },
          trailingIcon = {
            if (vinModelQuery.isNotEmpty()) {
              IconButton(onClick = { vinModelQuery = "" }) {
                Icon(Icons.Rounded.Clear, contentDescription = "Clear VIN or model search")
              }
            }
          },
          singleLine = true,
          shape = MaterialTheme.shapes.medium,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("vin_model_search_input"),
        )

        AnimatedVisibility(visible = vinMatches.isNotEmpty()) {
          Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 4.dp,
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 6.dp)
              .border(1.dp, TelemetryCyan.copy(alpha = 0.4f), MaterialTheme.shapes.medium),
          ) {
            Column {
              vinMatches.take(5).forEachIndexed { idx, match ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { applyVinMatch(match) }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("vin_match_${match.variantSpec.chassisCode}"),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = "${match.inferredYear} ${match.modelSpec.brand} ${match.modelSpec.model}",
                      style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                      text = "${match.matchReason} • ${match.variantSpec.name}",
                      style = MaterialTheme.typography.bodySmall,
                      color = TelemetryCyan,
                    )
                  }
                  Text(
                    text = match.marketCode,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = OemEmerald,
                  )
                }
                if (idx < minOf(4, vinMatches.lastIndex)) {
                  HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
              }
            }
          }
        }

        Spacer(Modifier.height(10.dp))

        LinearProgressIndicator(
          progress = { (currentStep.index + 1) / 5f },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = RacingOrange,
          trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )

        Spacer(Modifier.height(10.dp))

        StepBreadcrumbRow(
          currentStep = currentStep,
          selectedMarket = selectedMarket,
          selectedBrand = selectedBrand,
          selectedModel = selectedModelSpec?.model,
          selectedYear = selectedYear,
          onJumpToStep = { targetStep ->
            if (targetStep.index < currentStep.index) {
              stepNavController.popBackStack(targetStep.route, inclusive = false)
            }
          },
        )

        Spacer(Modifier.height(10.dp))

        NavHost(
          navController = stepNavController,
          startDestination = VehicleStepRoutes.COUNTRY,
          modifier = Modifier.weight(1f),
          enterTransition = {
            slideInHorizontally(animationSpec = tween(240)) { it / 4 } + fadeIn(tween(200))
          },
          exitTransition = {
            slideOutHorizontally(animationSpec = tween(220)) { -it / 6 } + fadeOut(tween(180))
          },
          popEnterTransition = {
            slideInHorizontally(animationSpec = tween(240)) { -it / 4 } + fadeIn(tween(200))
          },
          popExitTransition = {
            slideOutHorizontally(animationSpec = tween(220)) { it / 6 } + fadeOut(tween(180))
          },
        ) {
          composable(VehicleStepRoutes.COUNTRY) {
            CountryStepContent(
              selectedMarket = selectedMarket,
              onSelectMarket = { market ->
                selectedMarket = market
                selectedCategory = null
                selectedBrand = null
                selectedModelSpec = null
                selectedYear = null
                selectedVariant = null
                stepNavController.navigate(VehicleStepRoutes.BRAND) {
                  launchSingleTop = true
                }
              },
            )
          }

          composable(VehicleStepRoutes.BRAND) {
            val marketCode = selectedMarket?.marketCode ?: "BD"
            BrandStepContent(
              marketCode = marketCode,
              selectedCategory = selectedCategory,
              selectedBrand = selectedBrand,
              onSelectCategory = { selectedCategory = it },
              onSelectBrand = { brand ->
                selectedBrand = brand
                selectedModelSpec = null
                selectedYear = null
                selectedVariant = null
                stepNavController.navigate(VehicleStepRoutes.MODEL) {
                  launchSingleTop = true
                }
              },
            )
          }

          composable(VehicleStepRoutes.MODEL) {
            val marketCode = selectedMarket?.marketCode ?: "BD"
            val brand = selectedBrand ?: "Toyota"
            ModelStepContent(
              marketCode = marketCode,
              brand = brand,
              selectedCategory = selectedCategory,
              selectedModelSpec = selectedModelSpec,
              onSelectModel = { modelSpec ->
                selectedModelSpec = modelSpec
                selectedYear = null
                selectedVariant = null
                stepNavController.navigate(VehicleStepRoutes.YEAR) {
                  launchSingleTop = true
                }
              },
            )
          }

          composable(VehicleStepRoutes.YEAR) {
            val years = selectedModelSpec?.years ?: (2018..2026).toList().reversed()
            YearStepContent(
              years = years,
              selectedYear = selectedYear,
              onSelectYear = { yr ->
                selectedYear = yr
                selectedVariant = selectedModelSpec?.variants?.firstOrNull()
                stepNavController.navigate(VehicleStepRoutes.VARIANT) {
                  launchSingleTop = true
                }
              },
            )
          }

          composable(VehicleStepRoutes.VARIANT) {
            VariantStepContent(
              variants = selectedModelSpec?.variants.orEmpty(),
              selectedVariant = selectedVariant,
              odometerText = odometerText,
              nicknameText = nicknameText,
              isSaving = isSaving,
              canConfirmAll = selectedMarket != null &&
                selectedBrand != null &&
                selectedModelSpec != null &&
                selectedYear != null,
              onSelectVariant = { selectedVariant = it },
              onOdometerChange = { raw -> odometerText = raw.filter { it.isDigit() }.take(7) },
              onNicknameChange = { if (it.length <= 50) nicknameText = it },
              onConfirmSave = {
                val market = selectedMarket ?: return@VariantStepContent
                val brand = selectedBrand ?: return@VariantStepContent
                val model = selectedModelSpec ?: return@VariantStepContent
                val year = selectedYear ?: return@VariantStepContent
                val variant = selectedVariant ?: return@VariantStepContent
                val odo = odometerText.toLongOrNull() ?: return@VariantStepContent
                onSaveVehicle(
                  market.country,
                  market.marketCode,
                  brand,
                  model.model,
                  year,
                  variant.name,
                  variant.powertrain,
                  odo,
                  nicknameText.ifBlank { null },
                )
              },
            )
          }
        }
      }
    }
  }
}

@Composable
private fun StepBreadcrumbRow(
  currentStep: VehicleStep,
  selectedMarket: MarketSpec?,
  selectedBrand: String?,
  selectedModel: String?,
  selectedYear: Int?,
  onJumpToStep: (VehicleStep) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    VehicleStep.entries.forEach { step ->
      val valueLabel = when (step) {
        VehicleStep.COUNTRY -> selectedMarket?.marketCode ?: step.label
        VehicleStep.BRAND -> selectedBrand ?: step.label
        VehicleStep.MODEL -> selectedModel ?: step.label
        VehicleStep.YEAR -> selectedYear?.toString() ?: step.label
        VehicleStep.VARIANT -> step.label
      }
      val isCurrent = step == currentStep
      val isCompleted = step.index < currentStep.index
      val bg = when {
        isCurrent -> RacingOrange.copy(alpha = 0.2f)
        isCompleted -> TelemetryCyan.copy(alpha = 0.14f)
        else -> MaterialTheme.colorScheme.surfaceContainerLow
      }
      val fg = when {
        isCurrent -> RacingOrange
        isCompleted -> TelemetryCyan
        else -> MaterialTheme.colorScheme.onSurfaceVariant
      }
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(bg)
          .border(1.dp, fg.copy(alpha = if (isCurrent || isCompleted) 0.5f else 0.25f), RoundedCornerShape(8.dp))
          .clickable(enabled = isCompleted) { onJumpToStep(step) }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("step_crumb_${step.name.lowercase()}"),
      ) {
        Text(
          text = valueLabel,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = fg,
        )
      }
    }
  }
}

@Composable
private fun CountryStepContent(
  selectedMarket: MarketSpec?,
  onSelectMarket: (MarketSpec) -> Unit,
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(10.dp),
    contentPadding = PaddingValues(bottom = 24.dp),
  ) {
    items(VehicleCatalogData.markets, key = { it.marketCode }) { market ->
      val isSelected = selectedMarket?.marketCode == market.marketCode
      SelectorOptionCard(
        title = "${market.country} (${market.marketCode})",
        subtitle = "${market.regionTag} • ${market.notes}",
        badge = market.marketCode,
        isSelected = isSelected,
        tag = "market_${market.marketCode}",
        onClick = { onSelectMarket(market) },
      )
    }
  }
}

@Composable
private fun BrandStepContent(
  marketCode: String,
  selectedCategory: VehicleCategory?,
  selectedBrand: String?,
  onSelectCategory: (VehicleCategory?) -> Unit,
  onSelectBrand: (String) -> Unit,
) {
  val brands = remember(marketCode, selectedCategory) {
    VehicleCatalogData.brandsForMarket(marketCode, selectedCategory)
  }
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      item {
        FilterChip(
          selected = selectedCategory == null,
          onClick = { onSelectCategory(null) },
          label = { Text("All Fleet") },
          modifier = Modifier.testTag("category_chip_all"),
        )
      }
      items(VehicleCategory.entries) { cat ->
        FilterChip(
          selected = selectedCategory == cat,
          onClick = { onSelectCategory(if (selectedCategory == cat) null else cat) },
          label = { Text(cat.shortLabel) },
          modifier = Modifier.testTag("category_chip_${cat.name.lowercase()}"),
        )
      }
    }

    LazyVerticalGrid(
      columns = GridCells.Adaptive(minSize = 150.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 24.dp),
    ) {
      items(brands, key = { it }) { brand ->
        val isSelected = selectedBrand == brand
        Surface(
          shape = MaterialTheme.shapes.medium,
          color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 74.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) RacingOrange else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
              shape = MaterialTheme.shapes.medium,
            )
            .clickable { onSelectBrand(brand) }
            .testTag("brand_$brand"),
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = Icons.Rounded.DirectionsCar,
              contentDescription = null,
              tint = if (isSelected) RacingOrange else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
              text = brand,
              style = MaterialTheme.typography.titleSmall,
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ModelStepContent(
  marketCode: String,
  brand: String,
  selectedCategory: VehicleCategory?,
  selectedModelSpec: VehicleModelSpec?,
  onSelectModel: (VehicleModelSpec) -> Unit,
) {
  var modelFilter by remember { mutableStateOf("") }
  val models = remember(marketCode, brand, selectedCategory, modelFilter) {
    val base = VehicleCatalogData.modelsFor(marketCode, brand, selectedCategory)
    if (modelFilter.isBlank()) base else base.filter {
      it.model.contains(modelFilter.trim(), ignoreCase = true) ||
        it.category.displayName.contains(modelFilter.trim(), ignoreCase = true)
    }
  }
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    OutlinedTextField(
      value = modelFilter,
      onValueChange = { modelFilter = it },
      placeholder = { Text("Filter $brand models…") },
      leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
      singleLine = true,
      shape = MaterialTheme.shapes.medium,
      modifier = Modifier.fillMaxWidth().testTag("model_filter_input"),
    )
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 24.dp),
    ) {
      items(models, key = { "${it.brand}_${it.model}" }) { modelSpec ->
        SelectorOptionCard(
          title = "${modelSpec.brand} ${modelSpec.model}",
          subtitle = "${modelSpec.category.shortLabel} • ${modelSpec.variants.size} trim(s) • ${modelSpec.years.last()}–${modelSpec.years.first()}",
          badge = modelSpec.variants.first().chassisCode,
          isSelected = selectedModelSpec == modelSpec,
          tag = "model_${modelSpec.model}",
          onClick = { onSelectModel(modelSpec) },
        )
      }
    }
  }
}

@Composable
private fun YearStepContent(
  years: List<Int>,
  selectedYear: Int?,
  onSelectYear: (Int) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 104.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    contentPadding = PaddingValues(bottom = 24.dp),
  ) {
    items(years, key = { it }) { yr ->
      val isSelected = selectedYear == yr
      Surface(
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(min = 60.dp)
          .clip(MaterialTheme.shapes.medium)
          .border(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) RacingOrange else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            shape = MaterialTheme.shapes.medium,
          )
          .clickable { onSelectYear(yr) }
          .testTag("year_$yr"),
      ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(12.dp)) {
          Text(
            text = yr.toString(),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          )
        }
      }
    }
  }
}

@Composable
private fun VariantStepContent(
  variants: List<VehicleVariantSpec>,
  selectedVariant: VehicleVariantSpec?,
  odometerText: String,
  nicknameText: String,
  isSaving: Boolean,
  canConfirmAll: Boolean,
  onSelectVariant: (VehicleVariantSpec) -> Unit,
  onOdometerChange: (String) -> Unit,
  onNicknameChange: (String) -> Unit,
  onConfirmSave: () -> Unit,
) {
  val odoParsed = odometerText.toLongOrNull()
  val canSave = canConfirmAll &&
    selectedVariant != null &&
    odoParsed != null &&
    odoParsed in 0L..2_000_000L &&
    !isSaving

  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(bottom = 28.dp),
  ) {
    item {
      Text(
        text = "Select Powertrain & Chassis Variant",
        style = MaterialTheme.typography.titleSmall,
      )
    }
    items(variants, key = { it.name }) { variant ->
      val isSelected = selectedVariant == variant
      SelectorOptionCard(
        title = variant.name,
        subtitle = "${variant.powertrain.label} • Chassis ${variant.chassisCode}",
        badge = variant.powertrain.name,
        isSelected = isSelected,
        tag = "variant_${variant.chassisCode}",
        onClick = { onSelectVariant(variant) },
      )
    }
    item {
      Spacer(Modifier.height(4.dp))
      OutlinedTextField(
        value = odometerText,
        onValueChange = onOdometerChange,
        label = { Text("Current Odometer Reading (km)") },
        leadingIcon = { Icon(Icons.Rounded.Speed, contentDescription = null) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = odoParsed == null,
        modifier = Modifier.fillMaxWidth().testTag("selector_odometer_input"),
      )
    }
    item {
      OutlinedTextField(
        value = nicknameText,
        onValueChange = onNicknameChange,
        label = { Text("Garage Nickname (optional, e.g. Daily Commuter)") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().testTag("selector_nickname_input"),
      )
    }
    item {
      Button(
        onClick = onConfirmSave,
        enabled = canSave,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(min = 54.dp)
          .testTag("selector_save_vehicle_button"),
      ) {
        if (isSaving) {
          CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            strokeWidth = 2.dp,
          )
          Spacer(Modifier.width(10.dp))
          Text("Syncing to Garage…")
        } else {
          Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
          Spacer(Modifier.width(8.dp))
          Text("Confirm & Load Parts + Schedule")
        }
      }
    }
  }
}

@Composable
private fun SelectorOptionCard(
  title: String,
  subtitle: String,
  badge: String,
  isSelected: Boolean,
  tag: String,
  onClick: () -> Unit,
) {
  Surface(
    shape = MaterialTheme.shapes.medium,
    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surfaceContainer,
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = 64.dp)
      .clip(MaterialTheme.shapes.medium)
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) RacingOrange else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        shape = MaterialTheme.shapes.medium,
      )
      .clickable(onClick = onClick)
      .testTag(tag),
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(3.dp))
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Spacer(Modifier.width(10.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(if (isSelected) RacingOrange.copy(alpha = 0.2f) else TelemetryCyan.copy(alpha = 0.14f))
          .padding(horizontal = 8.dp, vertical = 4.dp),
      ) {
        Text(
          text = badge,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = if (isSelected) RacingOrange else TelemetryCyan,
        )
      }
    }
  }
}
