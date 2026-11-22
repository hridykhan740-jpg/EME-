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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.model.CatalogSystem
import com.example.model.DistanceUnit
import com.example.model.MaintenanceLog
import com.example.model.SavedPart
import com.example.model.UserVehicle
import com.example.ui.components.DeveloperFooterCard
import com.example.ui.components.OdometerUpdateDialog
import com.example.ui.components.formatDistance
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan
import java.text.DateFormat
import java.util.Date

@Composable
fun GarageScreen(
  displayName: String,
  email: String?,
  vehicles: List<UserVehicle>,
  activeVehicle: UserVehicle?,
  maintenanceLogs: List<MaintenanceLog>,
  savedParts: List<SavedPart>,
  distanceUnit: DistanceUnit,
  onAddVehicle: () -> Unit,
  onSelectActiveVehicle: (String) -> Unit,
  onUpdateOdometer: (vehicleId: String, newKm: Long) -> Unit,
  onDeleteVehicle: (String) -> Unit,
  onOpenSavedPart: (system: CatalogSystem, partId: String) -> Unit,
  onChangeDistanceUnit: (DistanceUnit) -> Unit,
  onSignOut: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var editingOdometerVehicle by remember { mutableStateOf<UserVehicle?>(null) }

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 760.dp),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      // Account & Preferences Card
      item {
        Surface(
          shape = MaterialTheme.shapes.large,
          color = MaterialTheme.colorScheme.surfaceContainer,
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), MaterialTheme.shapes.large),
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .background(TelemetryCyan.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center,
              ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = TelemetryCyan)
              }
              Spacer(Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(text = displayName.ifBlank { "Driver" }, style = MaterialTheme.typography.titleMedium)
                if (!email.isNullOrBlank()) {
                  Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
              }
              OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier.heightIn(min = 48.dp).testTag("sign_out_button"),
              ) {
                Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Sign Out")
              }
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text("Distance Telemetry Unit", style = MaterialTheme.typography.bodyMedium)
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DistanceUnit.entries.forEach { unit ->
                  FilterChip(
                    selected = distanceUnit == unit,
                    onClick = { onChangeDistanceUnit(unit) },
                    label = { Text(unit.name) },
                    modifier = Modifier.testTag("unit_chip_${unit.name.lowercase()}"),
                  )
                }
              }
            }
          }
        }
      }

      // Vehicles Header + Add Vehicle Button
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text("My Garage (${vehicles.size})", style = MaterialTheme.typography.titleLarge)
          Button(
            onClick = onAddVehicle,
            modifier = Modifier.heightIn(min = 48.dp).testTag("garage_add_vehicle_button"),
          ) {
            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Add Vehicle")
          }
        }
      }

      items(vehicles, key = { it.id }) { vehicle ->
        val isActive = vehicle.id == activeVehicle?.id
        Surface(
          shape = MaterialTheme.shapes.medium,
          color = if (isActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceContainer,
          modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .border(
              width = if (isActive) 1.5.dp else 1.dp,
              color = if (isActive) RacingOrange else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
              shape = MaterialTheme.shapes.medium,
            )
            .clickable { onSelectActiveVehicle(vehicle.id) }
            .testTag("garage_vehicle_${vehicle.id}"),
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = if (isActive) Icons.Rounded.CheckCircle else Icons.Rounded.DirectionsCar,
              contentDescription = null,
              tint = if (isActive) RacingOrange else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(26.dp),
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = vehicle.nickname?.takeIf { it.isNotBlank() } ?: vehicle.shortTitle(),
                  style = MaterialTheme.typography.titleSmall,
                )
                Text(
                  text = vehicle.marketCode,
                  style = MaterialTheme.typography.labelSmall,
                  color = TelemetryCyan,
                )
              }
              Text(
                text = "${vehicle.shortTitle()} • ${vehicle.variant}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Text(
                text = "Odometer: ${formatDistance(vehicle.odometerKm, distanceUnit)}",
                style = MaterialTheme.typography.labelMedium,
                color = OemEmerald,
              )
            }
            IconButton(
              onClick = { editingOdometerVehicle = vehicle },
              modifier = Modifier.size(48.dp),
            ) {
              Icon(Icons.Rounded.Edit, contentDescription = "Edit odometer")
            }
            if (vehicles.size > 1) {
              IconButton(
                onClick = { onDeleteVehicle(vehicle.id) },
                modifier = Modifier.size(48.dp),
              ) {
                Icon(
                  Icons.Rounded.DeleteOutline,
                  contentDescription = "Remove vehicle",
                  tint = MaterialTheme.colorScheme.error,
                )
              }
            }
          }
        }
      }

      // Saved Parts Watchlist
      item {
        Text("Saved Parts Watchlist (${savedParts.size})", style = MaterialTheme.typography.titleLarge)
      }
      if (savedParts.isEmpty()) {
        item {
          Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Text(
              text = "Bookmark parts in the Parts Catalog to track OEM numbers and stock here.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(14.dp),
            )
          }
        }
      } else {
        items(savedParts, key = { it.id }) { saved ->
          Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
              .fillMaxWidth()
              .clip(MaterialTheme.shapes.medium)
              .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.medium)
              .clickable {
                onOpenSavedPart(CatalogSystem.fromRaw(saved.systemName), saved.partId)
              },
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(Icons.Rounded.Bookmark, contentDescription = null, tint = RacingOrange)
              Spacer(Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(saved.partName, style = MaterialTheme.typography.titleSmall)
                Text(
                  "${saved.oemNumber} • ${saved.systemName} • ${saved.vehicleSummary}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
          }
        }
      }

      // Service History Logs
      item {
        Text("Recent Service History (${maintenanceLogs.size})", style = MaterialTheme.typography.titleLarge)
      }
      if (maintenanceLogs.isEmpty()) {
        item {
          Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Text(
              text = "No maintenance logs recorded yet. Log completed services from the Maintenance Hub.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(14.dp),
            )
          }
        }
      } else {
        items(maintenanceLogs.take(15), key = { it.id }) { log ->
          val dateStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(log.serviceEpochMs))
          Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(Icons.Rounded.Build, contentDescription = null, tint = OemEmerald, modifier = Modifier.size(20.dp))
              Spacer(Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(log.taskTitle, style = MaterialTheme.typography.titleSmall)
                Text(
                  "${formatDistance(log.serviceKm, distanceUnit)} • $dateStr${if (!log.partOemNumber.isNullOrBlank()) " • OEM ${log.partOemNumber}" else ""}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (!log.notes.isNullOrBlank()) {
                  Text(log.notes, style = MaterialTheme.typography.bodySmall)
                }
              }
            }
          }
        }
      }

      item {
        DeveloperFooterCard()
      }
    }
  }

  val targetEdit = editingOdometerVehicle
  if (targetEdit != null) {
    OdometerUpdateDialog(
      currentKm = targetEdit.odometerKm,
      onDismiss = { editingOdometerVehicle = null },
      onSaveKm = { newKm ->
        editingOdometerVehicle = null
        onUpdateOdometer(targetEdit.id, newKm)
      },
    )
  }
}
