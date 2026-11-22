package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.OfflinePin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.example.local.LocalSearchHistoryEntity
import com.example.local.LocalSelectedVehicleEntity
import com.example.local.LocalViewedPartEntity
import com.example.model.CatalogSystem
import com.example.model.DistanceUnit
import com.example.ui.components.formatDistance
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan
import java.text.DateFormat
import java.util.Date

private enum class HistoryTab(val label: String) {
  VIEWED_PARTS("Viewed & AI Reviews"),
  SEARCHES("Search History"),
  VEHICLES("Selected Vehicles"),
}

@Composable
fun HistoryReviewsScreen(
  viewedParts: List<LocalViewedPartEntity>,
  searchHistory: List<LocalSearchHistoryEntity>,
  selectedVehicles: List<LocalSelectedVehicleEntity>,
  distanceUnit: DistanceUnit,
  onOpenPartInCatalog: (system: CatalogSystem, partId: String) -> Unit,
  onRunSearchInCatalog: (query: String) -> Unit,
  onClearHistory: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var activeTab by remember { mutableStateOf(HistoryTab.VIEWED_PARTS) }

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 760.dp),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      // Offline Cache Header Card
      item {
        Surface(
          shape = MaterialTheme.shapes.large,
          color = MaterialTheme.colorScheme.surfaceContainer,
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), MaterialTheme.shapes.large),
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(OemEmerald.copy(alpha = 0.16f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(Icons.Rounded.OfflinePin, contentDescription = null, tint = OemEmerald, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("My History & Reviews", style = MaterialTheme.typography.titleLarge)
              Text(
                "Saved locally on your device for instant offline OEM lookup anytime.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
            if (viewedParts.isNotEmpty() || searchHistory.isNotEmpty()) {
              OutlinedButton(
                onClick = onClearHistory,
                modifier = Modifier.heightIn(min = 48.dp).testTag("clear_local_history_button"),
              ) {
                Icon(Icons.Rounded.DeleteSweep, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Clear")
              }
            }
          }
        }
      }

      // Section Selector Chips
      item {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(HistoryTab.entries) { tab ->
            val count = when (tab) {
              HistoryTab.VIEWED_PARTS -> viewedParts.size
              HistoryTab.SEARCHES -> searchHistory.size
              HistoryTab.VEHICLES -> selectedVehicles.size
            }
            FilterChip(
              selected = activeTab == tab,
              onClick = { activeTab = tab },
              label = { Text("${tab.label} ($count)") },
              modifier = Modifier.testTag("history_tab_${tab.name.lowercase()}"),
            )
          }
        }
      }

      when (activeTab) {
        HistoryTab.VIEWED_PARTS -> {
          if (viewedParts.isEmpty()) {
            item {
              EmptyHistoryCard(
                icon = Icons.Rounded.Visibility,
                title = "No Viewed Parts Yet",
                body = "Tap any part in the Parts Catalog or verify its OEM number with AI to save an offline review here.",
              )
            }
          } else {
            items(viewedParts, key = { it.partKey }) { entry ->
              val dateStr = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                .format(Date(entry.viewedAtEpochMs))
              Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(MaterialTheme.shapes.medium)
                  .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.medium)
                  .clickable {
                    onOpenPartInCatalog(CatalogSystem.fromRaw(entry.systemName), entry.partId)
                  }
                  .testTag("history_part_${entry.partId}"),
              ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Text(
                      text = entry.systemName.uppercase(),
                      style = MaterialTheme.typography.labelSmall,
                      color = RacingOrange,
                    )
                    Text(
                      text = dateStr,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                  Text(entry.partName, style = MaterialTheme.typography.titleSmall)
                  Text(
                    text = "OEM: ${entry.oemNumber} • Alt: ${entry.aftermarketNumber}",
                    style = MaterialTheme.typography.labelMedium,
                    color = TelemetryCyan,
                  )
                  Text(
                    text = "Vehicle: ${entry.vehicleSummary}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                  if (!entry.aiVerificationSummary.isNullOrBlank()) {
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(OemEmerald.copy(alpha = 0.14f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = OemEmerald, modifier = Modifier.size(16.dp))
                      Spacer(Modifier.width(6.dp))
                      Text(
                        text = entry.aiVerificationSummary,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = OemEmerald,
                      )
                    }
                  }
                }
              }
            }
          }
        }

        HistoryTab.SEARCHES -> {
          if (searchHistory.isEmpty()) {
            item {
              EmptyHistoryCard(
                icon = Icons.Rounded.Search,
                title = "No Local Searches Yet",
                body = "Use the global or catalog search bar to look up OEM numbers or part names; queries are cached locally.",
              )
            }
          } else {
            items(searchHistory, key = { it.id }) { search ->
              val dateStr = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                .format(Date(search.searchedAtEpochMs))
              Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(MaterialTheme.shapes.medium)
                  .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.medium)
                  .clickable { onRunSearchInCatalog(search.queryText) }
                  .testTag("history_search_${search.id}"),
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Icon(Icons.Rounded.History, contentDescription = null, tint = TelemetryCyan)
                  Spacer(Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text("\"${search.queryText}\"", style = MaterialTheme.typography.titleSmall)
                    Text(
                      "${search.resultCount} matches • ${search.vehicleSummary} • $dateStr",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                }
              }
            }
          }
        }

        HistoryTab.VEHICLES -> {
          if (selectedVehicles.isEmpty()) {
            item {
              EmptyHistoryCard(
                icon = Icons.Rounded.DirectionsCar,
                title = "No Selected Vehicles Cached",
                body = "Vehicles you select are automatically stored offline on this device for quick reference.",
              )
            }
          } else {
            items(selectedVehicles, key = { it.vehicleKey }) { veh ->
              val dateStr = DateFormat.getDateInstance(DateFormat.MEDIUM)
                .format(Date(veh.selectedAtEpochMs))
              Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                  .fillMaxWidth()
                  .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.medium),
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Icon(Icons.Rounded.DirectionsCar, contentDescription = null, tint = RacingOrange)
                  Spacer(Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text("${veh.year} ${veh.brand} ${veh.model}", style = MaterialTheme.typography.titleSmall)
                    Text(
                      "${veh.variant} • ${veh.country} (${veh.marketCode})",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                      "Odometer: ${formatDistance(veh.odometerKm, distanceUnit)} • Cached $dateStr",
                      style = MaterialTheme.typography.labelSmall,
                      color = TelemetryCyan,
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun EmptyHistoryCard(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  body: String,
) {
  Surface(
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(
      modifier = Modifier.padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(34.dp))
      Text(title, style = MaterialTheme.typography.titleMedium)
      Text(body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
