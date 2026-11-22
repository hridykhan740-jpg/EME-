package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.local.LocalPdfCartItemEntity
import com.example.model.CatalogPart
import com.example.model.PartSubComponent
import com.example.model.UserVehicle
import com.example.ui.components.EpcCalloutBadge
import com.example.ui.components.GooglePartImageButton
import com.example.ui.components.PartVisualIdentifier
import com.example.ui.components.StockStatusBadge
import com.example.ui.components.inferSubPartVisualKind
import com.example.ui.components.systemAccentColor
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan
import com.example.viewmodel.AiPartVerificationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartDetailModalSheet(
  part: CatalogPart,
  vehicle: UserVehicle?,
  isSaved: Boolean,
  aiPartState: AiPartVerificationState,
  onVerifyWithAi: () -> Unit,
  onToggleSave: () -> Unit,
  onAddAssemblyToPdf: () -> Unit,
  onAddSubPartToPdf: (PartSubComponent) -> Unit,
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

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(modifier = Modifier.weight(1f)) {
          DetailInfoBlock(label = "OEM Part Number (${part.oemManufacturer})", value = part.oemNumber, accent = TelemetryCyan)
        }
        GooglePartImageButton(
          oemNumber = part.oemNumber,
          partName = part.name,
          vehicleLabel = vehicle?.let { "${it.brand} ${it.model}" } ?: "",
          modifier = Modifier.testTag("detail_google_main_oem"),
        )
      }
      DetailInfoBlock(label = "Official EPC Catalog Reference", value = part.epcCatalogRef, accent = RacingOrange, leadingIcon = Icons.Rounded.Description)
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

      // Micro-Level Assembly Breakdown
      if (part.subComponents.isNotEmpty()) {
        Surface(
          shape = MaterialTheme.shapes.medium,
          color = MaterialTheme.colorScheme.surfaceContainerLow,
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TelemetryCyan.copy(alpha = 0.35f), MaterialTheme.shapes.medium)
            .testTag("detail_subcomponents_section"),
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Rounded.Layers, contentDescription = null, tint = TelemetryCyan, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(8.dp))
              Text(
                text = "Micro-Level Assembly Breakdown (${part.subComponents.size} Sub-Parts)",
                style = MaterialTheme.typography.titleSmall,
                color = TelemetryCyan,
              )
            }
            part.subComponents.forEachIndexed { idx, sub ->
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth(),
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  EpcCalloutBadge(number = idx + 1, accent = systemAccentColor(part.system))
                  Spacer(Modifier.width(8.dp))
                  PartVisualIdentifier(
                    visualKind = inferSubPartVisualKind(sub.name, part.visualKind),
                    system = part.system,
                    size = 46.dp,
                  )
                  Spacer(Modifier.width(10.dp))
                  Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(sub.name, style = MaterialTheme.typography.labelLarge)
                    Text(
                      text = "OEM: ${sub.oemNumber} • ${sub.quantity} • $${"%.2f".format(sub.priceUsd)}",
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                      color = TelemetryCyan,
                    )
                    Text(
                      text = "Alt: ${sub.aftermarketNumber} • ${sub.specNote}",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                  Spacer(Modifier.width(4.dp))
                  GooglePartImageButton(
                    oemNumber = sub.oemNumber,
                    partName = sub.name,
                    vehicleLabel = vehicle?.let { "${it.brand} ${it.model}" } ?: "",
                    modifier = Modifier.testTag("detail_google_sub_${sub.id}"),
                  )
                  OutlinedButton(
                    onClick = { onAddSubPartToPdf(sub) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.heightIn(min = 48.dp).testTag("add_sub_pdf_${sub.id}"),
                  ) {
                    Icon(Icons.Rounded.PlaylistAdd, contentDescription = "Add sub-part to PDF", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("PDF")
                  }
                }
              }
            }
          }
        }
      }

      // Firebase AI Logic Official EPC Verification Card
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
          Text("Checking Official EPC w/ AI…")
        } else {
          Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = TelemetryCyan, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(8.dp))
          Text("Verify Official EPC, Sub-Parts & Interval w/ AI")
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
                  text = "AI-Verified Official EPC Specification",
                  style = MaterialTheme.typography.titleSmall,
                  color = OemEmerald,
                )
                Text("Official OEM #: ${res.verifiedOemNumber}", style = MaterialTheme.typography.labelLarge, color = TelemetryCyan)
                Text("EPC Source: ${res.epcCatalogSource}", style = MaterialTheme.typography.labelMedium, color = RacingOrange)
                Text("Aftermarket: ${res.aftermarketEquivalents}", style = MaterialTheme.typography.bodySmall)
                Text(
                  "Service Interval: Every ${res.officialIntervalKm} km or ${res.officialIntervalMonths} months",
                  style = MaterialTheme.typography.labelMedium,
                  color = RacingOrange,
                )
                if (res.subComponentsBreakdown.isNotEmpty()) {
                  Spacer(Modifier.height(2.dp))
                  Text("AI Exploded Sub-Components:", style = MaterialTheme.typography.labelMedium)
                  res.subComponentsBreakdown.forEach { line ->
                    Text("• $line", style = MaterialTheme.typography.bodySmall)
                  }
                }
                Text(res.fitmentSummary, style = MaterialTheme.typography.bodySmall)
                Text(
                  "Basis: ${res.manufacturerSourceNote}",
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

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        OutlinedButton(
          onClick = onAddAssemblyToPdf,
          shape = MaterialTheme.shapes.medium,
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 52.dp)
            .testTag("detail_add_assembly_pdf_button"),
        ) {
          Icon(Icons.Rounded.PictureAsPdf, contentDescription = null, tint = RacingOrange, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(6.dp))
          Text("Add to PDF")
        }

        Button(
          onClick = onToggleSave,
          shape = MaterialTheme.shapes.medium,
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 52.dp)
            .testTag("detail_save_watchlist_button"),
        ) {
          Icon(
            imageVector = if (isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
            contentDescription = null,
          )
          Spacer(Modifier.width(6.dp))
          Text(if (isSaved) "Watchlisted" else "Watchlist")
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfPartsExportSheet(
  vehicle: UserVehicle?,
  items: List<LocalPdfCartItemEntity>,
  onRemoveItem: (String) -> Unit,
  onClearAll: () -> Unit,
  onExportSharePdf: () -> Unit,
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
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Rounded.PictureAsPdf, contentDescription = null, tint = RacingOrange, modifier = Modifier.size(26.dp))
          Spacer(Modifier.width(10.dp))
          Column {
            Text("OEM Parts PDF Export", style = MaterialTheme.typography.titleLarge)
            Text(
              text = vehicle?.let { "${it.shortTitle()} (${it.marketCode})" } ?: "EME AUTOMATIC HELP",
              style = MaterialTheme.typography.bodySmall,
              color = TelemetryCyan,
            )
          }
        }
        if (items.isNotEmpty()) {
          TextButton(onClick = onClearAll, modifier = Modifier.testTag("pdf_clear_all_button")) {
            Icon(Icons.Rounded.DeleteSweep, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(4.dp))
            Text("Clear")
          }
        }
      }

      if (items.isEmpty()) {
        Surface(
          shape = MaterialTheme.shapes.medium,
          color = MaterialTheme.colorScheme.surfaceContainerLow,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text = "Your parts export list is empty. Tap \"+ PDF\" on any main assembly or micro sub-part to include it in a printable OEM PDF sheet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp),
          )
        }
      } else {
        val total = items.sumOf { it.priceUsd * it.quantity }
        items.forEach { item ->
          Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
              .fillMaxWidth()
              .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium),
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(4.dp))
                      .background(if (item.isSubComponent) TelemetryCyan.copy(alpha = 0.16f) else RacingOrange.copy(alpha = 0.16f))
                      .padding(horizontal = 6.dp, vertical = 2.dp),
                  ) {
                    Text(
                      text = if (item.isSubComponent) "SUB-PART" else "ASSEMBLY",
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                      color = if (item.isSubComponent) TelemetryCyan else RacingOrange,
                    )
                  }
                  Text(item.systemName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(item.partName, style = MaterialTheme.typography.titleSmall)
                Text(
                  text = "OEM: ${item.oemNumber} • $${"%.2f".format(item.priceUsd)}",
                  style = MaterialTheme.typography.labelMedium,
                  color = TelemetryCyan,
                )
              }
              IconButton(
                onClick = { onRemoveItem(item.itemKey) },
                modifier = Modifier.size(48.dp),
              ) {
                Icon(Icons.Rounded.DeleteOutline, contentDescription = "Remove item from PDF list", tint = MaterialTheme.colorScheme.error)
              }
            }
          }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Text("${items.size} items selected", style = MaterialTheme.typography.titleSmall)
          Text(
            text = "Est. Total: $${"%.2f".format(total)}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = OemEmerald,
          )
        }

        Button(
          onClick = onExportSharePdf,
          shape = MaterialTheme.shapes.medium,
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp)
            .testTag("export_pdf_share_button"),
        ) {
          Icon(Icons.Rounded.Share, contentDescription = null, modifier = Modifier.size(20.dp))
          Spacer(Modifier.width(8.dp))
          Text("Generate & Share PDF Parts List")
        }
      }
    }
  }
}

@Composable
private fun DetailInfoBlock(
  label: String,
  value: String,
  accent: Color = MaterialTheme.colorScheme.onSurface,
  leadingIcon: ImageVector? = null,
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
