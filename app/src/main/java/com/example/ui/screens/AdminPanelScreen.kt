package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.MarkEmailRead
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.auth.AdminAccessControl
import com.example.model.AdminUserLog
import com.example.model.LoginAlert
import com.example.model.UiState
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan
import java.text.DateFormat
import java.util.Date

@Composable
fun AdminPanelScreen(
  currentUserEmail: String?,
  userLogsState: UiState<List<AdminUserLog>>,
  loginAlertsState: UiState<List<LoginAlert>>,
  onAcknowledgeAlert: (String) -> Unit,
  onUnauthorizedRedirect: () -> Unit,
  modifier: Modifier = Modifier,
) {
  // Defense-in-depth RBAC route guard: redirect immediately if email is not Super-Admin
  val isAuthorized = AdminAccessControl.isSuperAdmin(currentUserEmail)
  LaunchedEffect(isAuthorized) {
    if (!isAuthorized) {
      onUnauthorizedRedirect()
    }
  }
  if (!isAuthorized) return

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 820.dp),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      // Super-Admin Header Banner
      item {
        Surface(
          shape = MaterialTheme.shapes.large,
          color = MaterialTheme.colorScheme.surfaceContainer,
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, RacingOrange.copy(alpha = 0.55f), MaterialTheme.shapes.large)
            .testTag("admin_panel_header"),
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(RacingOrange.copy(alpha = 0.16f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                Icons.Rounded.AdminPanelSettings,
                contentDescription = "Super Admin Panel",
                tint = RacingOrange,
                modifier = Modifier.size(28.dp),
              )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Super-Admin User Logs", style = MaterialTheme.typography.titleLarge)
              Text(
                "Authorized exclusively for ${AdminAccessControl.SUPER_ADMIN_EMAIL}",
                style = MaterialTheme.typography.bodySmall,
                color = TelemetryCyan,
              )
            }
          }
        }
      }

      // Automated Gmail Alerts Feed
      item {
        Text("Automated Login Alerts → ${AdminAccessControl.SUPER_ADMIN_EMAIL}", style = MaterialTheme.typography.titleMedium)
      }

      when (loginAlertsState) {
        is UiState.Loading -> {
          item {
            Row(
              modifier = Modifier.fillMaxWidth().padding(12.dp),
              horizontalArrangement = Arrangement.Center,
            ) {
              CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
          }
        }
        is UiState.Error -> {
          item {
            Text(
              text = loginAlertsState.message,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.error,
            )
          }
        }
        is UiState.Success -> {
          val alerts = loginAlertsState.data
          if (alerts.isEmpty()) {
            item {
              Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth(),
              ) {
                Text(
                  text = "No login alerts triggered yet.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(14.dp),
                )
              }
            }
          } else {
            items(alerts.take(20), key = { it.id }) { alert ->
              val timeStr = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                .format(Date(alert.loginEpochMs))
              Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                  .fillMaxWidth()
                  .border(
                    1.dp,
                    if (alert.acknowledged) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) else CautionAmber.copy(alpha = 0.6f),
                    MaterialTheme.shapes.medium,
                  )
                  .testTag("login_alert_${alert.id}"),
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Icon(
                    imageVector = if (alert.acknowledged) Icons.Rounded.MarkEmailRead else Icons.Rounded.NotificationsActive,
                    contentDescription = null,
                    tint = if (alert.acknowledged) OemEmerald else CautionAmber,
                  )
                  Spacer(Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text("${alert.displayName} (${alert.email})", style = MaterialTheme.typography.titleSmall)
                    Text(
                      "$timeStr • Channel: ${alert.deliveryChannel}",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                  if (!alert.acknowledged) {
                    Spacer(Modifier.width(8.dp))
                    Button(
                      onClick = { onAcknowledgeAlert(alert.id) },
                      colors = ButtonDefaults.buttonColors(containerColor = RacingOrange),
                      modifier = Modifier.heightIn(min = 48.dp).testTag("ack_alert_${alert.id}"),
                    ) {
                      Text("Ack")
                    }
                  } else {
                    Icon(
                      Icons.Rounded.CheckCircle,
                      contentDescription = "Acknowledged",
                      tint = OemEmerald,
                      modifier = Modifier.size(20.dp),
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Registered Users Table / Log Cards
      item {
        Spacer(Modifier.height(4.dp))
        Text("Registered & Signed-In Users", style = MaterialTheme.typography.titleMedium)
      }

      when (userLogsState) {
        is UiState.Loading -> {
          item {
            Row(
              modifier = Modifier.fillMaxWidth().padding(16.dp),
              horizontalArrangement = Arrangement.Center,
            ) {
              CircularProgressIndicator()
            }
          }
        }
        is UiState.Error -> {
          item {
            Text(
              text = userLogsState.message,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.error,
            )
          }
        }
        is UiState.Success -> {
          val logs = userLogsState.data
          if (logs.isEmpty()) {
            item {
              Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth(),
              ) {
                Text(
                  text = "No user sign-in logs recorded yet.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(14.dp),
                )
              }
            }
          } else {
            items(logs, key = { it.userId }) { userLog ->
              val loginTimeStr = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                .format(Date(userLog.lastLoginEpochMs))
              Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                  .fillMaxWidth()
                  .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), MaterialTheme.shapes.medium)
                  .testTag("admin_user_row_${userLog.userId}"),
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  if (!userLog.photoUrl.isNullOrBlank()) {
                    AsyncImage(
                      model = userLog.photoUrl,
                      contentDescription = "${userLog.displayName} profile photo",
                      contentScale = ContentScale.Crop,
                      modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    )
                  } else {
                    Box(
                      modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(TelemetryCyan.copy(alpha = 0.16f)),
                      contentAlignment = Alignment.Center,
                    ) {
                      Icon(Icons.Rounded.Person, contentDescription = null, tint = TelemetryCyan)
                    }
                  }
                  Spacer(Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      Text(
                        text = userLog.displayName,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                      )
                      Text(
                        text = "${userLog.loginCount}x logins",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = OemEmerald,
                      )
                    }
                    Text(
                      text = userLog.email,
                      style = MaterialTheme.typography.bodySmall,
                      color = TelemetryCyan,
                    )
                    Text(
                      text = "Last login: $loginTimeStr",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(
                        Icons.Rounded.DirectionsCar,
                        contentDescription = null,
                        tint = RacingOrange,
                        modifier = Modifier.size(15.dp),
                      )
                      Spacer(Modifier.width(5.dp))
                      Text(
                        text = userLog.selectedVehiclesSummary,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
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
}
