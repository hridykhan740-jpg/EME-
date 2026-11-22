package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CatalogPart
import com.example.model.CatalogSystem
import com.example.model.DistanceUnit
import com.example.model.SavedPart
import com.example.model.UserVehicle
import com.example.ui.components.ActiveVehicleBanner
import com.example.ui.components.PartVisualIdentifier
import com.example.ui.components.StockStatusBadge
import com.example.ui.components.systemAccentColor
import com.example.ui.components.systemIcon
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan
import com.example.viewmodel.AiPartVerificationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartsCatalogScreen(
  vehicle: UserVehicle?,
  distanceUnit: DistanceUnit,
  allParts: List<CatalogPart>,
  filteredParts: List<CatalogPart>,
  savedParts: List<SavedPart>,
  selectedSystem: CatalogSystem?,
  searchQuery: String,
  inStockOnly: Boolean,
  highlightedPartId: String?,
  aiPartState: AiPartVerificationState,
  onSelectSystem: (CatalogSystem?) -> Unit,
  onSearchQueryChange: (String) -> Unit,
  onCommitSearchHistory: (String, Int) -> Unit,
  onToggleInStockOnly: () -> Unit,
  onClearHighlight: () -> Unit,
  onSwitchVehicle: () -> Unit,
  onToggleSavePart: (part: CatalogPart, existingSavedDocId: String?) -> Unit,
  onPartInspected: (CatalogPart) -> Unit,
  onVerifyPartWithAi: (CatalogPart) -> Unit,
  onResetAiPartState: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var inspectedPart by remember { mutableStateOf<CatalogPart?>(null) }
  val keyboardController = LocalSoftwareKeyboardController.current

  val openPartDetail: (CatalogPart) -> Unit = { part ->
    onResetAiPartState()
    inspectedPart = part
    onPartInspected(part)
  }

  LaunchedEffect(highlightedPartId, allParts) {
    if (!highlightedPartId.isNullOrBlank()) {
      val match = allParts.firstOrNull { it.id == highlightedPartId }
      if (match != null) {
        openPartDetail(match)
      }
      onClearHighlight()
    }
  }

  val savedByPartId = remember(savedParts) {
    savedParts.associateBy { it.partId }
  }

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    LazyVerticalGrid(
      columns = GridCells.Adaptive(minSize = 310.dp),
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 960.dp),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      // Active Vehicle Fitment Banner
      item(span = { GridItemSpan(maxLineSpan) }) {
        ActiveVehicleBanner(
          vehicle = vehicle,
          distanceUnit = distanceUnit,
          onSwitchVehicle = onSwitchVehicle,
        )
      }

      // Studio Banner
      item(span = { GridItemSpan(maxLineSpan) }) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .clip(MaterialTheme.shapes.large)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.large),
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_vehicle_garage_banner),
            contentDescription = "Verified OEM and aftermarket components",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
          )
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.horizontalGradient(
                  listOf(
                    MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
                    MaterialTheme.colorScheme.background.copy(alpha = 0.45f),
                  )
                )
              )
          )
          Column(
            modifier = Modifier
              .align(Alignment.CenterStart)
              .padding(horizontal = 16.dp),
          ) {
            Text(
              text = "Interactive Parts Catalog",
              style = MaterialTheme.typography.titleLarge,
              color = Color.White,
            )
            Spacer(Modifier.height(4.dp))
            Text(
              text = "${allParts.size} Verified OEM & Aftermarket SKUs across 6 vehicle systems",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFFCBD5E1),
            )
          }
        }
      }

      // Indexed Search Input + Stock Filter
      item(span = { GridItemSpan(maxLineSpan) }) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search part name, OEM #, Denso/Brembo SKU…") },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                  Icon(Icons.Rounded.Clear, contentDescription = "Clear search")
                }
              }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
              onSearch = {
                onCommitSearchHistory(searchQuery, filteredParts.size)
                keyboardController?.hide()
              }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("catalog_search_input"),
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "${filteredParts.size} matching parts",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            FilterChip(
              selected = inStockOnly,
              onClick = onToggleInStockOnly,
              label = { Text("In Stock Only") },
              leadingIcon = if (inStockOnly) {
                { Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp)) }
              } else null,
              modifier = Modifier.testTag("catalog_in_stock_filter"),
            )
          }
        }
      }

      // System-Based Hierarchy Selector
      item(span = { GridItemSpan(maxLineSpan) }) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            SystemHierarchyChip(
              label = "All Systems",
              code = "ALL",
              count = allParts.size,
              accent = RacingOrange,
              selected = selectedSystem == null,
              icon = null,
              tag = "system_chip_all",
              onClick = { onSelectSystem(null) },
            )
          }
          items(CatalogSystem.entries) { system ->
            val count = allParts.count { it.system == system }
            SystemHierarchyChip(
              label = system.displayName,
              code = system.shortCode,
              count = count,
              accent = systemAccentColor(system),
              selected = selectedSystem == system,
              icon = systemIcon(system),
              tag = "system_chip_${system.name.lowercase()}",
              onClick = { onSelectSystem(if (selectedSystem == system) null else system) },
            )
          }
        }
      }

      if (filteredParts.isEmpty()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
          Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(36.dp),
              )
              Text("No parts match your filter", style = MaterialTheme.typography.titleMedium)
              Text(
                "Clear the search query or switch back to All Systems to inspect the full inventory.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }
      } else {
        items(filteredParts, key = { it.id }) { part ->
          val savedRecord = savedByPartId[part.id]
          CatalogPartCard(
            part = part,
            isSaved = savedRecord != null,
            onCardClick = {
              if (searchQuery.isNotBlank()) {
                onCommitSearchHistory(searchQuery, filteredParts.size)
              }
              openPartDetail(part)
            },
            onToggleSave = { onToggleSavePart(part, savedRecord?.id) },
          )
        }
      }
    }
  }

  val activeDetail = inspectedPart
  if (activeDetail != null) {
    BackHandler { inspectedPart = null }
    val savedRecord = savedByPartId[activeDetail.id]
    PartDetailModalSheet(
      part = activeDetail,
      vehicle = vehicle,
      isSaved = savedRecord != null,
      aiPartState = aiPartState,
      onVerifyWithAi = { onVerifyPartWithAi(activeDetail) },
      onToggleSave = { onToggleSavePart(activeDetail, savedRecord?.id) },
      onDismiss = { inspectedPart = null },
    )
  }
}

@Composable
private fun SystemHierarchyChip(
  label: String,
  code: String,
  count: Int,
  accent: Color,
  selected: Boolean,
  icon: androidx.compose.ui.graphics.vector.ImageVector?,
  tag: String,
  onClick: () -> Unit,
) {
  Surface(
    shape = MaterialTheme.shapes.small,
    color = if (selected) accent.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceContainer,
    modifier = Modifier
      .heightIn(min = 48.dp)
      .clip(MaterialTheme.shapes.small)
      .border(
        width = if (selected) 1.5.dp else 1.dp,
        color = if (selected) accent else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        shape = MaterialTheme.shapes.small,
      )
      .clickable(onClick = onClick)
      .testTag(tag),
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      if (icon != null) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = if (selected) accent else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(18.dp),
        )
      }
      Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Text(
        text = "$count",
        style = MaterialTheme.typography.labelSmall,
        color = accent,
      )
    }
  }
}

@Composable
private fun CatalogPartCard(
  part: CatalogPart,
  isSaved: Boolean,
  onCardClick: () -> Unit,
  onToggleSave: () -> Unit,
) {
  val accent = systemAccentColor(part.system)
  Surface(
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainer,
    modifier = Modifier
      .fillMaxWidth()
      .clip(MaterialTheme.shapes.medium)
      .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.medium)
      .clickable(onClick = onCardClick)
      .testTag("part_card_${part.id}"),
  ) {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
      ) {
        PartVisualIdentifier(
          visualKind = part.visualKind,
          system = part.system,
          size = 64.dp,
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "${part.system.shortCode} • ${part.subSystem}".uppercase(),
              style = MaterialTheme.typography.labelSmall,
              color = accent,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f),
            )
            IconButton(
              onClick = onToggleSave,
              modifier = Modifier.size(48.dp).testTag("bookmark_part_${part.id}"),
            ) {
              Icon(
                imageVector = if (isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                contentDescription = if (isSaved) "Remove from Watchlist" else "Save to Watchlist",
                tint = if (isSaved) RacingOrange else MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
          Text(
            text = part.name,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("OEM #", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              text = part.oemNumber,
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = TelemetryCyan,
            )
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(part.aftermarketBrand, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              text = part.aftermarketNumber,
              style = MaterialTheme.typography.labelMedium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        StockStatusBadge(status = part.stockStatus, qty = part.warehouseQty)
        Text(
          text = "$${"%.2f".format(part.priceUsd)}",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PartDetailModalSheet(
  part: CatalogPart,
  vehicle: UserVehicle?,
  isSaved: Boolean,
  aiPartState: AiPartVerificationState,
  onVerifyWithAi: () -> Unit,
  onToggleSave: () -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp)
        .padding(bottom = 28.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        PartVisualIdentifier(
          visualKind = part.visualKind,
          system = part.system,
          size = 76.dp,
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "${part.system.displayName} / ${part.subSystem}",
            style = MaterialTheme.typography.labelMedium,
            color = systemAccentColor(part.system),
          )
          Spacer(Modifier.height(4.dp))
          Text(text = part.name, style = MaterialTheme.typography.titleLarge)
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        StockStatusBadge(status = part.stockStatus, qty = part.warehouseQty)
        Text(
          text = "${part.dispatchSla}  •  $${"%.2f".format(part.priceUsd)}",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

      DetailInfoBlock(label = "OEM Part Number (${part.oemManufacturer})", value = part.oemNumber, accent = TelemetryCyan)
      DetailInfoBlock(label = "Verified Aftermarket (${part.aftermarketBrand})", value = part.aftermarketNumber)
      DetailInfoBlock(label = "Technical Specification", value = part.specSummary)
      DetailInfoBlock(
        label = "Fitment Verification",
        value = part.fitmentNotes,
        accent = OemEmerald,
        leadingIcon = Icons.Rounded.Verified,
      )

      if (vehicle != null) {
        Text(
          text = "Confirmed for ${vehicle.shortTitle()} (${vehicle.marketCode})",
          style = MaterialTheme.typography.bodySmall,
          color = OemEmerald,
        )
      }

      // Firebase AI Logic Official OEM Verification Card
      OutlinedButton(
        onClick = onVerifyWithAi,
        enabled = aiPartState !is AiPartVerificationState.Loading,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(min = 50.dp)
          .testTag("verify_part_ai_button"),
      ) {
        if (aiPartState is AiPartVerificationState.Loading && aiPartState.partId == part.id) {
          CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
          Spacer(Modifier.width(8.dp))
          Text("Verifying OEM Spec with AI…")
        } else {
          Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = TelemetryCyan, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(8.dp))
          Text("Verify Official OEM & Interval w/ AI")
        }
      }

      when (aiPartState) {
        is AiPartVerificationState.Success -> {
          if (aiPartState.partId == part.id) {
            val res = aiPartState.result
            Surface(
              shape = MaterialTheme.shapes.medium,
              color = OemEmerald.copy(alpha = 0.12f),
              modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, OemEmerald.copy(alpha = 0.4f), MaterialTheme.shapes.medium)
                .testTag("ai_part_verification_result"),
            ) {
              Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = "AI-Verified Manufacturer Specification",
                  style = MaterialTheme.typography.titleSmall,
                  color = OemEmerald,
                )
                Text("Official OEM #: ${res.verifiedOemNumber}", style = MaterialTheme.typography.labelLarge, color = TelemetryCyan)
                Text("Aftermarket: ${res.aftermarketEquivalents}", style = MaterialTheme.typography.bodySmall)
                Text(
                  "Recommended Interval: Every ${res.officialIntervalKm} km or ${res.officialIntervalMonths} months",
                  style = MaterialTheme.typography.labelMedium,
                  color = RacingOrange,
                )
                Text(res.fitmentSummary, style = MaterialTheme.typography.bodySmall)
                Text(
                  "Source: ${res.manufacturerSourceNote}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
          }
        }
        is AiPartVerificationState.Error -> {
          if (aiPartState.partId == part.id) {
            Surface(
              shape = MaterialTheme.shapes.medium,
              color = MaterialTheme.colorScheme.errorContainer,
              modifier = Modifier.fillMaxWidth(),
            ) {
              Text(
                text = aiPartState.message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(12.dp),
              )
            }
          }
        }
        else -> Unit
      }

      Button(
        onClick = onToggleSave,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(min = 52.dp)
          .testTag("detail_save_watchlist_button"),
      ) {
        Icon(
          imageVector = if (isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
          contentDescription = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(if (isSaved) "Saved in Parts Watchlist" else "Save Part to Watchlist")
      }
    }
  }
}

@Composable
private fun DetailInfoBlock(
  label: String,
  value: String,
  accent: Color = MaterialTheme.colorScheme.onSurface,
  leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
) {
  Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
    Text(
      text = label.uppercase(),
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (leadingIcon != null) {
        Icon(leadingIcon, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
      }
      Text(
        text = value,
        style = MaterialTheme.typography.bodyLarge,
        color = accent,
      )
    }
  }
}
