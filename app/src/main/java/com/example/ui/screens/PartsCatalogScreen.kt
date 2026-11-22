package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.local.LocalPdfCartItemEntity
import com.example.model.CatalogPart
import com.example.model.CatalogSystem
import com.example.model.DistanceUnit
import com.example.model.PartSubComponent
import com.example.model.SavedPart
import com.example.model.UserVehicle
import com.example.ui.components.ActiveVehicleBanner
import com.example.ui.components.EpcCalloutBadge
import com.example.ui.components.GooglePartImageButton
import com.example.ui.components.PartVisualIdentifier
import com.example.ui.components.StockStatusBadge
import com.example.ui.components.inferSubPartVisualKind
import com.example.ui.components.systemAccentColor
import com.example.ui.components.systemIcon
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan
import com.example.viewmodel.AiPartVerificationState

@Composable
fun PartsCatalogScreen(
  vehicle: UserVehicle?,
  distanceUnit: DistanceUnit,
  allParts: List<CatalogPart>,
  filteredParts: List<CatalogPart>,
  savedParts: List<SavedPart>,
  pdfCartItems: List<LocalPdfCartItemEntity>,
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
  onAddAssemblyToPdf: (CatalogPart) -> Unit,
  onAddSubPartToPdf: (CatalogPart, PartSubComponent) -> Unit,
  onRemovePdfItem: (String) -> Unit,
  onClearPdfList: () -> Unit,
  onExportPdfShare: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var inspectedPart by remember { mutableStateOf<CatalogPart?>(null) }
  var showPdfSheet by remember { mutableStateOf(false) }
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
  val totalSubParts = remember(allParts) {
    allParts.sumOf { it.subComponents.size }
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
      item(span = { GridItemSpan(maxLineSpan) }) {
        ActiveVehicleBanner(
          vehicle = vehicle,
          distanceUnit = distanceUnit,
          onSwitchVehicle = onSwitchVehicle,
        )
      }

      item(span = { GridItemSpan(maxLineSpan) }) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(114.dp)
            .clip(MaterialTheme.shapes.large)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.large),
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_vehicle_garage_banner),
            contentDescription = "Verified OEM and sub-component assemblies",
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
              text = "EPC Exploded Parts Catalog",
              style = MaterialTheme.typography.titleLarge,
              color = Color.White,
            )
            Spacer(Modifier.height(4.dp))
            Text(
              text = "${allParts.size} Assemblies & $totalSubParts Sub-Parts (${allParts.size + totalSubParts} Total Parts)",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFFCBD5E1),
            )
          }
        }
      }

      // Indexed Search Input + Stock Filter + PDF Export Button
      item(span = { GridItemSpan(maxLineSpan) }) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search assembly, sub-part, OEM # (e.g. O-ring, 90915)…") },
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Text(
              text = "${filteredParts.size} assemblies",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.weight(1f),
            )
            FilterChip(
              selected = inStockOnly,
              onClick = onToggleInStockOnly,
              label = { Text("In Stock") },
              leadingIcon = if (inStockOnly) {
                { Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp)) }
              } else null,
              modifier = Modifier.testTag("catalog_in_stock_filter"),
            )
            Button(
              onClick = { showPdfSheet = true },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (pdfCartItems.isNotEmpty()) RacingOrange else MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = if (pdfCartItems.isNotEmpty()) Color.White else MaterialTheme.colorScheme.onSurface,
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.heightIn(min = 48.dp).testTag("open_pdf_sheet_button"),
            ) {
              Icon(Icons.Rounded.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("PDF (${pdfCartItems.size})")
            }
          }
        }
      }

      // 12-System Hierarchy Selector
      item(span = { GridItemSpan(maxLineSpan) }) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            SystemHierarchyChip(
              label = "All 12 Systems",
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
                "Clear the search query or switch back to All 12 Systems to inspect full assemblies and sub-components.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }
      } else {
        itemsIndexed(filteredParts, key = { _, part -> part.id }) { idx, part ->
          val savedRecord = savedByPartId[part.id]
          CatalogPartCard(
            calloutNumber = idx + 1,
            part = part,
            vehicleLabel = vehicle?.let { "${it.brand} ${it.model}" } ?: "",
            isSaved = savedRecord != null,
            onCardClick = {
              if (searchQuery.isNotBlank()) {
                onCommitSearchHistory(searchQuery, filteredParts.size)
              }
              openPartDetail(part)
            },
            onToggleSave = { onToggleSavePart(part, savedRecord?.id) },
            onAddAssemblyToPdf = { onAddAssemblyToPdf(part) },
            onAddSubPartToPdf = { sub -> onAddSubPartToPdf(part, sub) },
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
      onAddAssemblyToPdf = { onAddAssemblyToPdf(activeDetail) },
      onAddSubPartToPdf = { sub -> onAddSubPartToPdf(activeDetail, sub) },
      onDismiss = { inspectedPart = null },
    )
  }

  if (showPdfSheet) {
    BackHandler { showPdfSheet = false }
    PdfPartsExportSheet(
      vehicle = vehicle,
      items = pdfCartItems,
      onRemoveItem = onRemovePdfItem,
      onClearAll = onClearPdfList,
      onExportSharePdf = onExportPdfShare,
      onDismiss = { showPdfSheet = false },
    )
  }
}

@Composable
private fun SystemHierarchyChip(
  label: String,
  count: Int,
  accent: Color,
  selected: Boolean,
  icon: ImageVector?,
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
  calloutNumber: Int,
  part: CatalogPart,
  vehicleLabel: String,
  isSaved: Boolean,
  onCardClick: () -> Unit,
  onToggleSave: () -> Unit,
  onAddAssemblyToPdf: () -> Unit,
  onAddSubPartToPdf: (PartSubComponent) -> Unit,
) {
  val accent = systemAccentColor(part.system)
  var expandedBreakdown by remember(part.id) { mutableStateOf(false) }

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
        Box {
          PartVisualIdentifier(
            visualKind = part.visualKind,
            system = part.system,
            size = 64.dp,
          )
          EpcCalloutBadge(
            number = calloutNumber,
            accent = accent,
            modifier = Modifier.align(Alignment.TopStart).padding(2.dp),
          )
        }
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
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text("Main OEM #", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = part.oemNumber,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = TelemetryCyan,
              )
              GooglePartImageButton(
                oemNumber = part.oemNumber,
                partName = part.name,
                vehicleLabel = vehicleLabel,
                modifier = Modifier.testTag("google_part_${part.id}"),
              )
            }
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

      // Expandable Micro-Level Sub-Component Drawer + Add to PDF
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        if (part.subComponents.isNotEmpty()) {
          OutlinedButton(
            onClick = { expandedBreakdown = !expandedBreakdown },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1f)
              .heightIn(min = 48.dp)
              .testTag("toggle_subparts_${part.id}"),
          ) {
            Icon(Icons.Rounded.Layers, contentDescription = null, tint = TelemetryCyan, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Sub-Parts (${part.subComponents.size})", maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.width(2.dp))
            Icon(
              imageVector = if (expandedBreakdown) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
          }
        }
        OutlinedButton(
          onClick = onAddAssemblyToPdf,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .heightIn(min = 48.dp)
            .testTag("add_pdf_${part.id}"),
        ) {
          Icon(Icons.Rounded.PlaylistAdd, contentDescription = "Add assembly to PDF", tint = RacingOrange, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(4.dp))
          Text("PDF")
        }
      }

      AnimatedVisibility(visible = expandedBreakdown && part.subComponents.isNotEmpty()) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(1.dp, TelemetryCyan.copy(alpha = 0.28f), RoundedCornerShape(10.dp))
            .padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Text(
            text = "Assembly Breakdown • ${part.epcCatalogRef}",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = TelemetryCyan,
          )
          part.subComponents.forEachIndexed { idx, sub ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              EpcCalloutBadge(number = idx + 1, accent = accent)
              Spacer(Modifier.width(8.dp))
              PartVisualIdentifier(
                visualKind = inferSubPartVisualKind(sub.name, part.visualKind),
                system = part.system,
                size = 44.dp,
              )
              Spacer(Modifier.width(8.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text("${sub.name} (${sub.quantity})", style = MaterialTheme.typography.labelMedium)
                Text(
                  text = "OEM: ${sub.oemNumber} • $${"%.2f".format(sub.priceUsd)}",
                  style = MaterialTheme.typography.labelSmall,
                  color = TelemetryCyan,
                )
              }
              GooglePartImageButton(
                oemNumber = sub.oemNumber,
                partName = sub.name,
                vehicleLabel = vehicleLabel,
                modifier = Modifier.testTag("google_sub_${sub.id}"),
              )
              IconButton(
                onClick = { onAddSubPartToPdf(sub) },
                modifier = Modifier.size(48.dp).testTag("card_add_sub_pdf_${sub.id}"),
              ) {
                Icon(Icons.Rounded.PlaylistAdd, contentDescription = "Add sub-component to PDF", tint = RacingOrange)
              }
            }
            if (idx < part.subComponents.lastIndex) {
              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
          }
        }
      }
    }
  }
}
