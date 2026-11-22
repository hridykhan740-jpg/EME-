package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.DiscFull
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.CatalogSystem
import com.example.model.DistanceUnit
import com.example.model.MaintenanceUrgency
import com.example.model.PredictiveMaintenanceItem
import com.example.model.StockStatus
import com.example.model.UserVehicle
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan

fun systemIcon(system: CatalogSystem): ImageVector = when (system) {
  CatalogSystem.ENGINE -> Icons.Rounded.Engineering
  CatalogSystem.TRANSMISSION -> Icons.Rounded.Settings
  CatalogSystem.SUSPENSION -> Icons.Rounded.Tune
  CatalogSystem.ELECTRICAL -> Icons.Rounded.ElectricBolt
  CatalogSystem.BRAKES -> Icons.Rounded.DiscFull
  CatalogSystem.BODY -> Icons.Rounded.DirectionsCar
}

@Composable
fun StockStatusBadge(status: StockStatus, qty: Int, modifier: Modifier = Modifier) {
  val (bg, fg) = when (status) {
    StockStatus.IN_STOCK -> OemEmerald.copy(alpha = 0.16f) to OemEmerald
    StockStatus.LOW_STOCK -> CautionAmber.copy(alpha = 0.18f) to CautionAmber
    StockStatus.OEM_ORDER -> TelemetryCyan.copy(alpha = 0.16f) to TelemetryCyan
    StockStatus.BACKORDER -> MaterialTheme.colorScheme.error.copy(alpha = 0.16f) to MaterialTheme.colorScheme.error
  }
  val text = when (status) {
    StockStatus.IN_STOCK -> "${status.label} ($qty)"
    StockStatus.LOW_STOCK -> "Low ($qty left)"
    StockStatus.OEM_ORDER -> status.label
    StockStatus.BACKORDER -> status.label
  }
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bg)
      .border(1.dp, fg.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
      .padding(horizontal = 8.dp, vertical = 4.dp),
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
      color = fg,
    )
  }
}

@Composable
fun UrgencyBadge(urgency: MaintenanceUrgency, modifier: Modifier = Modifier) {
  val (bg, fg) = when (urgency) {
    MaintenanceUrgency.OVERDUE -> MaterialTheme.colorScheme.error.copy(alpha = 0.18f) to MaterialTheme.colorScheme.error
    MaintenanceUrgency.DUE_SOON -> CautionAmber.copy(alpha = 0.18f) to CautionAmber
    MaintenanceUrgency.HEALTHY -> OemEmerald.copy(alpha = 0.16f) to OemEmerald
  }
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bg)
      .border(1.dp, fg.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
      .padding(horizontal = 8.dp, vertical = 4.dp),
  ) {
    Text(
      text = urgency.label.uppercase(),
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
      color = fg,
    )
  }
}

@Composable
fun ActiveVehicleBanner(
  vehicle: UserVehicle?,
  distanceUnit: DistanceUnit,
  onSwitchVehicle: () -> Unit,
  onEditOdometer: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainer,
    tonalElevation = 2.dp,
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), MaterialTheme.shapes.medium)
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(RacingOrange.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Rounded.DirectionsCar,
          contentDescription = "Active vehicle",
          tint = RacingOrange,
          modifier = Modifier.size(22.dp),
        )
      }
      Spacer(Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        if (vehicle != null) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = vehicle.shortTitle(),
              style = MaterialTheme.typography.titleSmall,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f, fill = false),
            )
            Spacer(Modifier.width(6.dp))
            Text(
              text = vehicle.marketCode,
              style = MaterialTheme.typography.labelSmall,
              color = TelemetryCyan,
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(TelemetryCyan.copy(alpha = 0.14f))
                .padding(horizontal = 6.dp, vertical = 2.dp),
            )
          }
          Spacer(Modifier.height(2.dp))
          Text(
            text = "${vehicle.variant} • ${formatDistance(vehicle.odometerKm, distanceUnit)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        } else {
          Text(
            text = "No vehicle selected",
            style = MaterialTheme.typography.titleSmall,
          )
          Text(
            text = "Select market, brand, model & variant",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
      if (onEditOdometer != null && vehicle != null) {
        Surface(
          modifier = Modifier
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onEditOdometer)
            .testTag("edit_odometer_button"),
          color = Color.Transparent,
        ) {
          Box(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Rounded.Edit,
              contentDescription = "Update odometer",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp),
            )
          }
        }
      }
      Surface(
        modifier = Modifier
          .heightIn(min = 48.dp)
          .clip(RoundedCornerShape(10.dp))
          .clickable(onClick = onSwitchVehicle)
          .testTag("switch_vehicle_button"),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            imageVector = Icons.Rounded.SwapHoriz,
            contentDescription = "Change or add vehicle",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(18.dp),
          )
          Spacer(Modifier.width(4.dp))
          Text(
            text = if (vehicle == null) "Setup" else "Spec",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
          )
        }
      }
    }
  }
}

@Composable
fun OdometerUpdateDialog(
  currentKm: Long,
  onDismiss: () -> Unit,
  onSaveKm: (Long) -> Unit,
) {
  var input by remember(currentKm) { mutableStateOf(currentKm.toString()) }
  val parsed = input.toLongOrNull()
  val isValid = parsed != null && parsed in 0L..2_000_000L

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Update Vehicle Odometer", style = MaterialTheme.typography.titleLarge) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          "Predictive maintenance intervals recalculate automatically against your latest mileage.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
          value = input,
          onValueChange = { raw -> input = raw.filter { it.isDigit() }.take(7) },
          label = { Text("Current Odometer (km)") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          isError = !isValid,
          modifier = Modifier.fillMaxWidth().testTag("odometer_dialog_input"),
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { if (parsed != null) onSaveKm(parsed) },
        enabled = isValid,
        modifier = Modifier.testTag("odometer_dialog_save"),
      ) {
        Text("Save Reading")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    },
  )
}

@Composable
fun LogServiceDialog(
  item: PredictiveMaintenanceItem,
  currentOdometerKm: Long,
  onDismiss: () -> Unit,
  onConfirm: (serviceKm: Long, notes: String?) -> Unit,
) {
  var serviceKmText by remember(currentOdometerKm) { mutableStateOf(currentOdometerKm.toString()) }
  var notesText by remember { mutableStateOf("") }
  val parsedKm = serviceKmText.toLongOrNull()
  val isValid = parsedKm != null && parsedKm in 0L..2_000_000L

  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        imageVector = Icons.Rounded.Build,
        contentDescription = null,
        tint = RacingOrange,
      )
    },
    title = { Text("Log Service Completed", style = MaterialTheme.typography.titleLarge) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          item.title,
          style = MaterialTheme.typography.titleSmall,
        )
        if (item.linkedPartOem.isNotBlank()) {
          Text(
            "OEM Reference: ${item.linkedPartOem}",
            style = MaterialTheme.typography.labelMedium,
            color = TelemetryCyan,
          )
        }
        OutlinedTextField(
          value = serviceKmText,
          onValueChange = { raw -> serviceKmText = raw.filter { it.isDigit() }.take(7) },
          label = { Text("Odometer at Service (km)") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          isError = !isValid,
          modifier = Modifier.fillMaxWidth().testTag("log_service_km_input"),
        )
        OutlinedTextField(
          value = notesText,
          onValueChange = { if (it.length <= 280) notesText = it },
          label = { Text("Workshop / Part Notes (optional)") },
          maxLines = 3,
          modifier = Modifier.fillMaxWidth().testTag("log_service_notes_input"),
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { if (parsedKm != null) onConfirm(parsedKm, notesText.ifBlank { null }) },
        enabled = isValid,
        modifier = Modifier.testTag("log_service_confirm_button"),
      ) {
        Text("Record Service")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    },
  )
}
