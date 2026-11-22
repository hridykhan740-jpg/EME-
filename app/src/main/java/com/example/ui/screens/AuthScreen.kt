package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.R
import com.example.auth.GoogleSignInButton
import com.example.auth.attemptAutoSignIn
import com.example.ui.components.DeveloperFooterCard
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan

@Composable
fun AuthScreen(
  onAuthSuccess: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember(context) { CredentialManager.create(context) }
  var authError by remember { mutableStateOf<String?>(null) }

  LaunchedEffect(Unit) {
    attemptAutoSignIn(
      context = context,
      credentialManager = credentialManager,
      onAuthSuccess = onAuthSuccess,
      onUnauthenticated = {},
      scope = scope,
    )
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .safeDrawingPadding(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 560.dp)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Column {
        // Brand Badge Row
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(bottom = 16.dp),
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(RacingOrange.copy(alpha = 0.16f))
              .border(1.dp, RacingOrange.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Rounded.DirectionsCar,
              contentDescription = null,
              tint = RacingOrange,
              modifier = Modifier.size(22.dp),
            )
          }
          Spacer(Modifier.width(12.dp))
          Column {
            Text(
              text = "AUTOPULSE",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
            Text(
              text = "EME AUTOMATIC HELP • VEHICLE INTELLIGENCE",
              style = MaterialTheme.typography.labelSmall,
              color = TelemetryCyan,
            )
          }
        }

        // Hero Studio Artwork
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(MaterialTheme.shapes.large)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), MaterialTheme.shapes.large),
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_onboarding_hero),
            contentDescription = "AutoPulse vehicle cutaway telemetry graphic",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
          )
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  listOf(Color.Transparent, MaterialTheme.colorScheme.background.copy(alpha = 0.88f))
                )
              )
          )
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(16.dp),
          ) {
            Text(
              text = "Zero-Friction OEM Parts & Predictive Care",
              style = MaterialTheme.typography.headlineSmall,
              color = Color.White,
            )
            Spacer(Modifier.height(4.dp))
            Text(
              text = "Global & Asian market specs • Real-time Cloud Garage sync",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFFCBD5E1),
            )
          }
        }

        Spacer(Modifier.height(22.dp))

        // Capability Highlights
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          OnboardingFeatureRow(
            icon = Icons.Rounded.Public,
            accent = TelemetryCyan,
            title = "Complete Bangladesh & Global Fleet",
            subtitle = "Private sedans, SUVs, MPVs & commercial trucks/buses via VIN, JDM chassis, or model.",
          )
          OnboardingFeatureRow(
            icon = Icons.Rounded.Inventory2,
            accent = RacingOrange,
            title = "12-System Micro Breakdown & PDF Export",
            subtitle = "Full EPC assemblies & individual sub-parts (seals, gaskets, bearings) with OEM cross-refs.",
          )
          OnboardingFeatureRow(
            icon = Icons.Rounded.AutoGraph,
            accent = OemEmerald,
            title = "Predictive Maintenance Hub",
            subtitle = "Dynamic mileage & time intervals linked directly to verified replacement parts.",
          )
        }
      }

      Spacer(Modifier.height(24.dp))

      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (authError != null) {
          Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Text(
              text = authError ?: "",
              color = MaterialTheme.colorScheme.onErrorContainer,
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(12.dp),
            )
          }
        }

        GoogleSignInButton(
          onAuthSuccess = {
            authError = null
            onAuthSuccess()
          },
          onAuthError = { msg ->
            authError = msg
          },
        )

        Text(
          text = "Authenticated via Google Identity & Cloud Firestore for multi-device garage sync.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        DeveloperFooterCard()
      }
    }
  }
}

@Composable
private fun OnboardingFeatureRow(
  icon: ImageVector,
  accent: Color,
  title: String,
  subtitle: String,
) {
  Surface(
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    modifier = Modifier.fillMaxWidth(),
  ) {
    Row(
      modifier = Modifier
        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium)
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(accent.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = accent,
          modifier = Modifier.size(20.dp),
        )
      }
      Spacer(Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(2.dp))
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}
