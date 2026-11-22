package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
  primary = RacingOrange,
  onPrimary = Color.White,
  primaryContainer = RacingOrangeDeep,
  onPrimaryContainer = RacingOrangeSoft,
  secondary = TelemetryCyan,
  onSecondary = Color(0xFF062538),
  secondaryContainer = TelemetryCyanDeep,
  onSecondaryContainer = TelemetryCyanSoft,
  tertiary = OemEmerald,
  onTertiary = Color(0xFF042B1C),
  tertiaryContainer = OemEmeraldDeep,
  onTertiaryContainer = OemEmeraldSoft,
  background = CarbonAsphalt,
  onBackground = CrispFrost,
  surface = MachinedGraphite,
  onSurface = CrispFrost,
  surfaceVariant = BrushedAlloy,
  onSurfaceVariant = SlateSilver,
  surfaceContainerLowest = CarbonAsphalt,
  surfaceContainerLow = SteelPanelLow,
  surfaceContainer = SteelPanel,
  surfaceContainerHigh = SteelPanelHigh,
  surfaceContainerHighest = BrushedAlloy,
  outline = SlateBorder,
  outlineVariant = SlateDivider,
  error = AlertCoral,
  onError = Color(0xFF3E0A0A),
  errorContainer = AlertCoralDeep,
  onErrorContainer = Color(0xFFFECACA),
)

private val LightColorScheme = lightColorScheme(
  primary = RacingOrangeLight,
  onPrimary = Color.White,
  primaryContainer = RacingOrangeContainerLight,
  onPrimaryContainer = RacingOrangeOnContainerLight,
  secondary = TelemetryBlueLight,
  onSecondary = Color.White,
  secondaryContainer = TelemetryBlueContainerLight,
  onSecondaryContainer = TelemetryBlueOnContainerLight,
  tertiary = OemEmeraldLight,
  onTertiary = Color.White,
  tertiaryContainer = OemEmeraldContainerLight,
  onTertiaryContainer = OemEmeraldOnContainerLight,
  background = StudioPaper,
  onBackground = InkNavy,
  surface = StudioWhite,
  onSurface = InkNavy,
  surfaceVariant = StudioAlloy,
  onSurfaceVariant = SlateText,
  surfaceContainerLowest = StudioWhite,
  surfaceContainerLow = StudioPaper,
  surfaceContainer = StudioAlloy,
  surfaceContainerHigh = StudioPanelHigh,
  outline = BlueprintBorder,
  outlineVariant = Color(0xFFE2E8F0),
  error = Color(0xFFDC2626),
  onError = Color.White,
  errorContainer = Color(0xFFFEE2E2),
  onErrorContainer = Color(0xFF7F1D1D),
)

val AutoPulseShapes = Shapes(
  small = RoundedCornerShape(10.dp),
  medium = RoundedCornerShape(16.dp),
  large = RoundedCornerShape(22.dp),
  extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun AutoPulseTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AutoPulseTypography,
    shapes = AutoPulseShapes,
    content = content,
  )
}
