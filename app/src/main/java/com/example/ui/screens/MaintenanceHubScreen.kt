package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.PredictiveMaintenanceEngine
import com.example.model.CatalogSystem
import com.example.model.DistanceUnit
import com.example.model.MaintenanceUrgency
import com.example.model.PredictiveMaintenanceItem
import com.example.model.UserVehicle
import com.example.ui.components.ActiveVehicleBanner
import com.example.ui.components.LogServiceDialog
import com.example.ui.components.OdometerUpdateDialog
import com.example.ui.components.UrgencyBadge
import com.example.ui.components.VehicleHealthGauge
import com.example.ui.components.formatDistance
import com.example.ui.components.systemAccentColor
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan
import com.example.viewmodel.AiScheduleAuditState
import java.text.DateFormat
import java.util.Date

@Composable
fun MaintenanceHubScreen(
  vehicle: UserVehicle?,
  schedule: List<PredictiveMaintenanceItem>,
  distanceUnit: DistanceUnit,
  selectedUrgency: MaintenanceUrgency?,
  aiScheduleState: AiScheduleAuditState,
  onSelectUrgency: (MaintenanceUrgency?) -> Unit,
  onSwitchVehicle: () -> Unit,
  onUpdateOdometer: (vehicleId: String, newKm: Long) -> Unit,
  onLogService: (vehicleId: String, item: PredictiveMaintenanceItem, serviceKm: Long, notes: String?) -> Unit,
  onOpenPartInCatalog: (system: CatalogSystem, partId: String) -> Unit,
  onAuditScheduleWithAi: (UserVehicle) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showOdometerDialog by remember { mutableStateOf(false) }
  var logTargetItem by remember { mutableStateOf<PredictiveMaintenanceItem?>(null) }

  val healthScore = remember(schedule) { PredictiveMaintenanceEngine.computeHealthScore(schedule) }
  val overdueCount = remember(schedule) { schedule.count { it.urgency == MaintenanceUrgency.OVERDUE } }
  val dueSoonCount = remember(schedule) { schedule.count { it.urgency == MaintenanceUrgency.DUE_SOON } }
  val healthyCount = remember(schedule) { schedule.count { it.urgency == MaintenanceUrgency.HEALTHY } }

  val filteredItems = remember(schedule, selectedUrgency) {
    if (selectedUrgency == null) schedule else schedule.filter { it.urgency == selectedUrgency }
  }

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
      item {
        ActiveVehicleBanner(
          vehicle = vehicle,
          distanceUnit = distanceUnit,
          onSwitchVehicle = onSwitchVehicle,
          onEditOdometer = if (vehicle != null) ({ showOdometerDialog = true }) else null,
        )
      }

      if (vehicle == null) {
        item {
          Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Icon(
                imageVector = Icons.Rounded.Schedule,
                contentDescription = null,
                tint = RacingOrange,
                modifier = Modifier.size(44.dp),
              )
              Text(
                text = "Configure a Vehicle for Predictive Maintenance",
                style = MaterialTheme.typography.titleLarge,
              )
              Text(
                text = "Select your country market, brand, model, year, and variant to unlock mileage and month-based fluid, filter, and wear intervals.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Button(
                onClick = onSwitchVehicle,
                modifier = Modifier.heightIn(min = 48.dp).testTag("empty_setup_vehicle_button"),
              ) {
                Text("Configure Vehicle Spec")
              }
            }
          }
        }
      } else {
        // Predictive Telemetry Header Card
        item {
          val baoClass = remember(vehicle) { PredictiveMaintenanceEngine.classifyVehicle(vehicle) }
          var showBaoChart by remember { mutableStateOf(false) }

          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(
              shape = MaterialTheme.shapes.large,
              color = MaterialTheme.colorScheme.surfaceContainer,
              modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), MaterialTheme.shapes.large),
            ) {
              Box {
                Image(
                  painter = painterResource(id = R.drawable.img_maintenance_telemetry),
                  contentDescription = "Predictive maintenance telemetry schematic",
                  contentScale = ContentScale.Crop,
                  alpha = 0.28f,
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp),
                )
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
                    .background(
                      Brush.horizontalGradient(
                        listOf(
                          MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.94f),
                          MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.65f),
                        )
                      )
                    )
                )
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  VehicleHealthGauge(score = healthScore, size = 88.dp)
                  Spacer(Modifier.width(16.dp))
                  Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                      text = "Predictive Maintenance Hub",
                      style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                      text = "BAO 35/88 (07/04): ${baoClass.titleBn} • PS Oil ${baoClass.powerSteeringTopUpLiters}",
                      style = MaterialTheme.typography.labelMedium,
                      color = TelemetryCyan,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                      StatMiniPill("$overdueCount Overdue", MaterialTheme.colorScheme.error)
                      StatMiniPill("$dueSoonCount Due Soon", CautionAmber)
                      StatMiniPill("$healthyCount OK", OemEmerald)
                    }
                  }
                }
              }
            }

            // 161 Field Workshop Company BAO 35/88 & Amendment 07/04 Reference Chart Card
            Surface(
              shape = MaterialTheme.shapes.medium,
              color = MaterialTheme.colorScheme.surfaceContainer,
              modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CautionAmber.copy(alpha = 0.45f), MaterialTheme.shapes.medium),
            ) {
              Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.TableChart, contentDescription = null, tint = CautionAmber, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Column {
                      Text(
                        text = "১৬১ ফিল্ড ওয়ার্কশপ কোং • BAO 35/88 (০৭/০৪)",
                        style = MaterialTheme.typography.titleSmall,
                        color = CautionAmber,
                      )
                      Text(
                        text = "তৈল ও ফিল্টার বদলীর চার্ট • টায়ার ৪০,০০০ কি.মি. • কুল্যান্ট ৩ বছর",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                      )
                    }
                  }
                  TextButton(onClick = { showBaoChart = !showBaoChart }) {
                    Text(if (showBaoChart) "Hide" else "Chart")
                    Icon(
                      imageVector = if (showBaoChart) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                      contentDescription = null,
                    )
                  }
                }

                AnimatedVisibility(visible = showBaoChart) {
                  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    PredictiveMaintenanceEngine.baoChartRows.forEach { row ->
                      Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.fillMaxWidth(),
                      ) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                          Text(
                            text = "${row.sl}. ${row.vehicleType}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = TelemetryCyan,
                          )
                          Text("ইঞ্জিন অয়েল: ${row.engineOil}", style = MaterialTheme.typography.bodySmall)
                          Text("ট্রান্স/ডিফারেন্সিয়াল: ${row.transDiffOil}", style = MaterialTheme.typography.bodySmall)
                          Text("তেলের ফিল্টার: ${row.oilFilter}", style = MaterialTheme.typography.bodySmall)
                          Text("ফুয়েল/এয়ার/টায়ার: ${row.fuelAndAirFilter}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                      }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    PredictiveMaintenanceEngine.baoSpecialNotes.forEach { note ->
                      Text("• $note", style = MaterialTheme.typography.bodySmall, color = CautionAmber)
                    }
                  }
                }
              }
            }
          }
        }

        // AI Official Manufacturer Manual Schedule Verification Card
        item {
          Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
              .fillMaxWidth()
              .border(1.dp, TelemetryCyan.copy(alpha = 0.35f), MaterialTheme.shapes.medium),
          ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text("Official OEM Schedule Audit (AI)", style = MaterialTheme.typography.titleSmall)
                  Text(
                    "Cross-check km & month intervals against official ${vehicle.brand} ${vehicle.model} specifications.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                  onClick = { onAuditScheduleWithAi(vehicle) },
                  enabled = aiScheduleState !is AiScheduleAuditState.Loading,
                  modifier = Modifier.heightIn(min = 48.dp).testTag("verify_schedule_ai_button"),
                ) {
                  if (aiScheduleState is AiScheduleAuditState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                  } else {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = TelemetryCyan, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Verify")
                  }
                }
              }

              when (aiScheduleState) {
                is AiScheduleAuditState.Success -> {
                  val audit = aiScheduleState.audit
                  HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                  Text(audit.sourceManualSummary, style = MaterialTheme.typography.bodySmall, color = OemEmerald)
                  if (audit.severeClimateNote.isNotBlank()) {
                    Text(
                      "Market Advisory: ${audit.severeClimateNote}",
                      style = MaterialTheme.typography.labelSmall,
                      color = CautionAmber,
                    )
                  }
                  audit.items.forEach { item ->
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                      Text(
                        text = "${item.partOrFluid} (${item.oemSpec})",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f),
                      )
                      Text(
                        text = "${item.officialIntervalKm} km / ${item.officialIntervalMonths} mo",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TelemetryCyan,
                      )
                    }
                  }
                }
                is AiScheduleAuditState.Error -> {
                  Text(
                    text = aiScheduleState.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                  )
                }
                else -> Unit
              }
            }
          }
        }

        // Urgency Filter Row
        item {
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
              FilterChip(
                selected = selectedUrgency == null,
                onClick = { onSelectUrgency(null) },
                label = { Text("All (${schedule.size})") },
                modifier = Modifier.testTag("filter_urgency_all"),
              )
            }
            items(MaintenanceUrgency.entries) { urgency ->
              val count = schedule.count { it.urgency == urgency }
              FilterChip(
                selected = selectedUrgency == urgency,
                onClick = { onSelectUrgency(if (selectedUrgency == urgency) null else urgency) },
                label = { Text("${urgency.label} ($count)") },
                modifier = Modifier.testTag("filter_urgency_${urgency.name.lowercase()}"),
              )
            }
          }
        }

        // Predictive Maintenance Interval Cards
        items(filteredItems, key = { it.taskKey }) { item ->
          MaintenanceIntervalCard(
            item = item,
            distanceUnit = distanceUnit,
            onLogClick = { logTargetItem = item },
            onViewPartClick = { onOpenPartInCatalog(item.system, item.linkedPartId) },
          )
        }
      }
    }
  }

  if (showOdometerDialog && vehicle != null) {
    OdometerUpdateDialog(
      currentKm = vehicle.odometerKm,
      onDismiss = { showOdometerDialog = false },
      onSaveKm = { newKm ->
        showOdometerDialog = false
        onUpdateOdometer(vehicle.id, newKm)
      },
    )
  }

  val target = logTargetItem
  if (target != null && vehicle != null) {
    LogServiceDialog(
      item = target,
      currentOdometerKm = vehicle.odometerKm,
      onDismiss = { logTargetItem = null },
      onConfirm = { km, notes ->
        logTargetItem = null
        onLogService(vehicle.id, target, km, notes)
      },
    )
  }
}

@Composable
private fun StatMiniPill(text: String, color: Color) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(color.copy(alpha = 0.16f))
      .padding(horizontal = 8.dp, vertical = 4.dp),
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
      color = color,
    )
  }
}

@Composable
private fun MaintenanceIntervalCard(
  item: PredictiveMaintenanceItem,
  distanceUnit: DistanceUnit,
  onLogClick: () -> Unit,
  onViewPartClick: () -> Unit,
) {
  val barColor = when (item.urgency) {
    MaintenanceUrgency.OVERDUE -> MaterialTheme.colorScheme.error
    MaintenanceUrgency.DUE_SOON -> CautionAmber
    MaintenanceUrgency.HEALTHY -> OemEmerald
  }
  val sysColor = systemAccentColor(item.system)

  Surface(
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainer,
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.medium)
      .testTag("maintenance_card_${item.taskKey}"),
  ) {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = item.categoryLabel.uppercase(),
              style = MaterialTheme.typography.labelSmall,
              color = sysColor,
            )
            Text("•", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              text = item.system.displayName,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          Spacer(Modifier.height(3.dp))
          Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
          )
        }
        Spacer(Modifier.width(8.dp))
        UrgencyBadge(urgency = item.urgency)
      }

      Text(
        text = item.recommendationReason,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          val statusText = if (item.remainingKm <= 0L) {
            "Overdue by ${formatDistance(-item.remainingKm, distanceUnit)}"
          } else {
            "${formatDistance(item.remainingKm, distanceUnit)} remaining"
          }
          Text(
            text = statusText,
            style = MaterialTheme.typography.labelMedium,
            color = barColor,
          )
          Text(
            text = "Every ${formatDistance(item.intervalKm.toLong(), distanceUnit)} / ${item.intervalMonths} months",
            style = MaterialTheme.typography.labelMedium,
            color = TelemetryCyan,
          )
        }
        LinearProgressIndicator(
          progress = { (item.lifeRemainingPercent / 100f).coerceIn(0.03f, 1f) },
          modifier = Modifier
            .fillMaxWidth()
            .height(7.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = barColor,
          trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          val lastText = if (item.lastServiceEpochMs > 0L) {
            val formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(item.lastServiceEpochMs))
            "Last logged: ${formatDistance(item.lastServiceKm, distanceUnit)} ($formattedDate)"
          } else {
            "Cycle base: ${formatDistance(item.lastServiceKm, distanceUnit)}"
          }
          Text(
            text = lastText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Text(
            text = "Next: ${formatDistance(item.nextDueKm, distanceUnit)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        OutlinedButton(
          onClick = onLogClick,
          shape = MaterialTheme.shapes.small,
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 48.dp)
            .testTag("log_service_${item.taskKey}"),
        ) {
          Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(6.dp))
          Text("Log Service")
        }
        Button(
          onClick = onViewPartClick,
          shape = MaterialTheme.shapes.small,
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
          ),
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 48.dp)
            .testTag("view_parts_${item.taskKey}"),
        ) {
          Icon(Icons.Rounded.Build, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(6.dp))
          Text("Parts (${item.linkedPartOem.take(10)})", maxLines = 1)
          Spacer(Modifier.width(4.dp))
          Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}
