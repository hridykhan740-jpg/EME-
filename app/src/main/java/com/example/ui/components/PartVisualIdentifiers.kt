package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.CatalogSystem
import com.example.model.DistanceUnit
import com.example.model.PartVisualKind
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.OemEmerald
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

fun formatDistance(km: Long, unit: DistanceUnit): String {
  return if (unit == DistanceUnit.MI) {
    val miles = (km * 0.621371).roundToInt()
    "%,d mi".format(miles)
  } else {
    "%,d km".format(km)
  }
}

fun systemAccentColor(system: CatalogSystem): Color = when (system) {
  CatalogSystem.ENGINE -> RacingOrange
  CatalogSystem.FUEL_AIR -> Color(0xFFF59E0B)
  CatalogSystem.TRANSMISSION -> CautionAmber
  CatalogSystem.CLUTCH -> Color(0xFFA78BFA)
  CatalogSystem.SUSPENSION -> TelemetryCyan
  CatalogSystem.STEERING -> Color(0xFF38BDF8)
  CatalogSystem.BRAKES -> Color(0xFFFB7185)
  CatalogSystem.COOLING -> Color(0xFF2DD4BF)
  CatalogSystem.AC_HVAC -> Color(0xFF67E8F9)
  CatalogSystem.ELECTRICAL -> Color(0xFF60A5FA)
  CatalogSystem.EXHAUST -> Color(0xFF94A3B8)
  CatalogSystem.BODY_INTERIOR -> OemEmerald
}

fun inferSubPartVisualKind(name: String, fallback: PartVisualKind): PartVisualKind {
  val n = name.lowercase()
  return when {
    n.contains("filter") || n.contains("strainer") || n.contains("cartridge") || n.contains("element") || n.contains("drier") -> PartVisualKind.FILTER_CANISTER
    n.contains("o-ring") || n.contains("seal") || n.contains("gasket") || n.contains("washer") || n.contains("shim") || n.contains("circlip") || n.contains("snap ring") || n.contains("collar") -> PartVisualKind.BRAKE_ROTOR
    n.contains("bolt") || n.contains("nut") || n.contains("stud") || n.contains("pin") || n.contains("plug") || n.contains("injector") || n.contains("coil") || n.contains("nipple") || n.contains("key") -> PartVisualKind.SPARK_IGNITION
    n.contains("spring") || n.contains("strut") || n.contains("shock") || n.contains("damper") -> PartVisualKind.STRUT_COIL
    n.contains("bracket") || n.contains("arm") || n.contains("mount") || n.contains("bushing") || n.contains("bush") || n.contains("fork") || n.contains("ball joint") || n.contains("grommet") -> PartVisualKind.CONTROL_ARM
    n.contains("bearing") || n.contains("gear") || n.contains("sprocket") || n.contains("flywheel") || n.contains("pulley") -> PartVisualKind.TRANSMISSION_GEAR
    n.contains("sensor") || n.contains("switch") || n.contains("relay") || n.contains("solenoid") || n.contains("valve") || n.contains("regulator") || n.contains("servo") -> PartVisualKind.SENSOR_ELECTRONIC
    n.contains("hose") || n.contains("pipe") || n.contains("tube") || n.contains("muffler") || n.contains("resonator") || n.contains("clamp") || n.contains("sleeve") -> PartVisualKind.EXHAUST_PIPE
    n.contains("cable") || n.contains("rack") || n.contains("shaft") || n.contains("rod") -> PartVisualKind.STEERING_RACK
    n.contains("belt") || n.contains("chain") -> PartVisualKind.BELT_TIMING
    n.contains("pad") || n.contains("shoe") || n.contains("plate") -> PartVisualKind.BRAKE_PAD
    n.contains("fan") || n.contains("radiator") || n.contains("condenser") || n.contains("evaporator") || n.contains("blower") -> PartVisualKind.HVAC_BLOWER
    n.contains("fluid") || n.contains("oil") || n.contains("tank") || n.contains("reservoir") -> PartVisualKind.FLUID_BOTTLE
    n.contains("battery") || n.contains("alternator") -> PartVisualKind.BATTERY_MODULE
    else -> fallback
  }
}

@Composable
fun EpcCalloutBadge(
  number: Int,
  accent: Color = RacingOrange,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .size(24.dp)
      .clip(CircleShape)
      .background(Color(0xFF0F172A))
      .border(1.5.dp, accent, CircleShape),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = number.toString(),
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
      color = Color.White,
    )
  }
}

@Composable
fun GooglePartImageButton(
  oemNumber: String,
  partName: String,
  vehicleLabel: String = "",
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val uriHandler = LocalUriHandler.current
  val query = listOf(oemNumber, vehicleLabel, partName)
    .filter { it.isNotBlank() }
    .joinToString(" ")
  val searchUrl = "https://www.google.com/search?tbm=isch&q=${Uri.encode(query)}"

  IconButton(
    onClick = {
      runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl)).apply {
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
      }.onFailure {
        runCatching { uriHandler.openUri(searchUrl) }
      }
    },
    modifier = modifier
      .size(48.dp)
      .semantics { contentDescription = "Search Google Images for OEM part $oemNumber" },
  ) {
    Box(
      modifier = Modifier
        .size(30.dp)
        .clip(CircleShape)
        .background(Color.White)
        .border(1.dp, Color(0xFFCBD5E1), CircleShape),
      contentAlignment = Alignment.Center,
    ) {
      Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = 3.2.dp.toPx()
        val inset = stroke / 2f
        val arcSize = Size(size.width - stroke, size.height - stroke)
        // Red top arc
        drawArc(
          color = Color(0xFFEA4335),
          startAngle = 215f,
          sweepAngle = 105f,
          useCenter = false,
          topLeft = Offset(inset, inset),
          size = arcSize,
          style = Stroke(width = stroke, cap = StrokeCap.Butt),
        )
        // Yellow left arc
        drawArc(
          color = Color(0xFFFBBC05),
          startAngle = 145f,
          sweepAngle = 70f,
          useCenter = false,
          topLeft = Offset(inset, inset),
          size = arcSize,
          style = Stroke(width = stroke, cap = StrokeCap.Butt),
        )
        // Green bottom arc
        drawArc(
          color = Color(0xFF34A853),
          startAngle = 45f,
          sweepAngle = 100f,
          useCenter = false,
          topLeft = Offset(inset, inset),
          size = arcSize,
          style = Stroke(width = stroke, cap = StrokeCap.Butt),
        )
        // Blue right arc + horizontal bar
        drawArc(
          color = Color(0xFF4285F4),
          startAngle = -5f,
          sweepAngle = 55f,
          useCenter = false,
          topLeft = Offset(inset, inset),
          size = arcSize,
          style = Stroke(width = stroke, cap = StrokeCap.Butt),
        )
        drawLine(
          color = Color(0xFF4285F4),
          start = Offset(size.width * 0.50f, size.height * 0.50f),
          end = Offset(size.width * 0.94f, size.height * 0.50f),
          strokeWidth = stroke,
          cap = StrokeCap.Square,
        )
      }
    }
  }
}

@Composable
fun PartVisualIdentifier(
  visualKind: PartVisualKind,
  system: CatalogSystem,
  modifier: Modifier = Modifier,
  size: Dp = 68.dp,
) {
  val accent = systemAccentColor(system)
  val surfaceDark = MaterialTheme.colorScheme.surfaceContainerLowest
  val gridLine = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
  val silver = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f)

  Box(
    modifier = modifier
      .size(size)
      .clip(RoundedCornerShape(14.dp))
      .background(
        Brush.linearGradient(
          listOf(
            surfaceDark,
            accent.copy(alpha = 0.14f),
          )
        )
      )
      .border(1.dp, accent.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
      .padding(6.dp),
    contentAlignment = Alignment.Center,
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = this.size.width
      val h = this.size.height
      val cx = w / 2f
      val cy = h / 2f

      drawLine(gridLine, Offset(cx, 0f), Offset(cx, h), strokeWidth = 1f)
      drawLine(gridLine, Offset(0f, cy), Offset(w, cy), strokeWidth = 1f)

      when (visualKind) {
        PartVisualKind.BRAKE_ROTOR, PartVisualKind.CLUTCH_DISC -> {
          val rOuter = minOf(w, h) * 0.42f
          val rInner = rOuter * 0.45f
          drawCircle(silver, radius = rOuter, center = Offset(cx, cy), style = Stroke(3.2f))
          drawCircle(accent, radius = rInner, center = Offset(cx, cy), style = Stroke(2.5f))
          for (i in 0 until 8) {
            val angle = (i * 45.0) * PI / 180.0
            val hx = cx + (rOuter * 0.72f) * cos(angle).toFloat()
            val hy = cy + (rOuter * 0.72f) * sin(angle).toFloat()
            drawCircle(accent, radius = 2.2f, center = Offset(hx, hy))
          }
        }

        PartVisualKind.BRAKE_PAD -> {
          drawRoundRect(
            color = silver,
            topLeft = Offset(w * 0.15f, h * 0.26f),
            size = Size(w * 0.70f, h * 0.48f),
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(3f),
          )
          drawRoundRect(
            color = accent.copy(alpha = 0.35f),
            topLeft = Offset(w * 0.23f, h * 0.34f),
            size = Size(w * 0.54f, h * 0.32f),
            cornerRadius = CornerRadius(6f, 6f),
          )
          drawLine(surfaceDark, Offset(cx, h * 0.34f), Offset(cx, h * 0.66f), strokeWidth = 2.5f)
        }

        PartVisualKind.FILTER_CANISTER -> {
          drawRoundRect(
            color = silver,
            topLeft = Offset(w * 0.24f, h * 0.16f),
            size = Size(w * 0.52f, h * 0.68f),
            cornerRadius = CornerRadius(10f, 10f),
            style = Stroke(3f),
          )
          drawRect(
            color = accent.copy(alpha = 0.32f),
            topLeft = Offset(w * 0.28f, h * 0.26f),
            size = Size(w * 0.44f, h * 0.46f),
          )
          for (row in 1..4) {
            val y = h * (0.24f + row * 0.10f)
            drawLine(accent, Offset(w * 0.30f, y), Offset(w * 0.70f, y), strokeWidth = 2f)
          }
        }

        PartVisualKind.SPARK_IGNITION -> {
          drawLine(silver, Offset(cx, h * 0.12f), Offset(cx, h * 0.86f), strokeWidth = 4f, cap = StrokeCap.Round)
          drawRoundRect(
            color = accent,
            topLeft = Offset(w * 0.34f, h * 0.36f),
            size = Size(w * 0.32f, h * 0.26f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(2.8f),
          )
          drawLine(accent, Offset(w * 0.38f, h * 0.24f), Offset(w * 0.62f, h * 0.24f), strokeWidth = 2.5f)
          drawLine(accent, Offset(w * 0.38f, h * 0.30f), Offset(w * 0.62f, h * 0.30f), strokeWidth = 2.5f)
          drawLine(silver, Offset(cx, h * 0.86f), Offset(w * 0.62f, h * 0.86f), strokeWidth = 2.8f)
        }

        PartVisualKind.STRUT_COIL -> {
          drawLine(silver, Offset(cx, h * 0.10f), Offset(cx, h * 0.90f), strokeWidth = 3.5f, cap = StrokeCap.Round)
          val path = Path().apply {
            moveTo(w * 0.30f, h * 0.26f)
            lineTo(w * 0.70f, h * 0.34f)
            lineTo(w * 0.30f, h * 0.42f)
            lineTo(w * 0.70f, h * 0.50f)
            lineTo(w * 0.30f, h * 0.58f)
            lineTo(w * 0.70f, h * 0.66f)
          }
          drawPath(path, color = accent, style = Stroke(3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round))
          drawRoundRect(
            color = silver,
            topLeft = Offset(w * 0.36f, h * 0.68f),
            size = Size(w * 0.28f, h * 0.16f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(2.5f),
          )
        }

        PartVisualKind.CONTROL_ARM -> {
          val arm = Path().apply {
            moveTo(w * 0.20f, h * 0.72f)
            lineTo(w * 0.54f, h * 0.26f)
            lineTo(w * 0.80f, h * 0.68f)
            close()
          }
          drawPath(arm, color = silver, style = Stroke(3.2f, join = StrokeJoin.Round))
          drawCircle(accent, radius = 6.5f, center = Offset(w * 0.20f, h * 0.72f))
          drawCircle(accent, radius = 6.5f, center = Offset(w * 0.54f, h * 0.26f))
          drawCircle(accent, radius = 6.5f, center = Offset(w * 0.80f, h * 0.68f))
        }

        PartVisualKind.STEERING_RACK -> {
          drawLine(silver, Offset(w * 0.12f, cy), Offset(w * 0.88f, cy), strokeWidth = 4f, cap = StrokeCap.Round)
          drawRoundRect(
            color = accent,
            topLeft = Offset(w * 0.32f, h * 0.36f),
            size = Size(w * 0.36f, h * 0.28f),
            cornerRadius = CornerRadius(6f, 6f),
            style = Stroke(2.8f),
          )
          drawCircle(accent, radius = 5.5f, center = Offset(w * 0.14f, cy))
          drawCircle(accent, radius = 5.5f, center = Offset(w * 0.86f, cy))
        }

        PartVisualKind.EXHAUST_PIPE -> {
          val pipe = Path().apply {
            moveTo(w * 0.14f, h * 0.66f)
            lineTo(w * 0.40f, h * 0.66f)
            lineTo(w * 0.60f, h * 0.36f)
            lineTo(w * 0.86f, h * 0.36f)
          }
          drawPath(pipe, color = silver, style = Stroke(5f, cap = StrokeCap.Round, join = StrokeJoin.Round))
          drawRoundRect(
            color = accent,
            topLeft = Offset(w * 0.34f, h * 0.38f),
            size = Size(w * 0.32f, h * 0.22f),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(2.8f),
          )
        }

        PartVisualKind.BATTERY_MODULE -> {
          drawRoundRect(
            color = silver,
            topLeft = Offset(w * 0.16f, h * 0.26f),
            size = Size(w * 0.68f, h * 0.52f),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(3f),
          )
          drawRect(accent, topLeft = Offset(w * 0.24f, h * 0.16f), size = Size(w * 0.14f, h * 0.10f))
          drawRect(silver, topLeft = Offset(w * 0.62f, h * 0.16f), size = Size(w * 0.14f, h * 0.10f))
          val bolt = Path().apply {
            moveTo(cx + 4f, h * 0.34f)
            lineTo(cx - 8f, h * 0.52f)
            lineTo(cx + 2f, h * 0.52f)
            lineTo(cx - 4f, h * 0.70f)
          }
          drawPath(bolt, color = accent, style = Stroke(3f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }

        PartVisualKind.SENSOR_ELECTRONIC -> {
          drawRoundRect(
            color = silver,
            topLeft = Offset(w * 0.26f, h * 0.26f),
            size = Size(w * 0.48f, h * 0.48f),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(3f),
          )
          drawCircle(accent, radius = 7f, center = Offset(cx, cy), style = Stroke(2.5f))
          for (i in -1..1) {
            val offset = i * w * 0.13f
            drawLine(accent, Offset(cx + offset, h * 0.12f), Offset(cx + offset, h * 0.24f), strokeWidth = 2.5f)
            drawLine(accent, Offset(cx + offset, h * 0.76f), Offset(cx + offset, h * 0.88f), strokeWidth = 2.5f)
          }
        }

        PartVisualKind.TRANSMISSION_GEAR, PartVisualKind.HVAC_BLOWER -> {
          val r = minOf(w, h) * 0.30f
          drawCircle(silver, radius = r, center = Offset(cx, cy), style = Stroke(3.2f))
          drawCircle(accent, radius = r * 0.45f, center = Offset(cx, cy), style = Stroke(2.5f))
          for (i in 0 until 8) {
            val angle = (i * 45.0) * PI / 180.0
            val sx = cx + r * cos(angle).toFloat()
            val sy = cy + r * sin(angle).toFloat()
            val ex = cx + (r + 8f) * cos(angle).toFloat()
            val ey = cy + (r + 8f) * sin(angle).toFloat()
            drawLine(accent, Offset(sx, sy), Offset(ex, ey), strokeWidth = 4f, cap = StrokeCap.Round)
          }
        }

        PartVisualKind.FLUID_BOTTLE -> {
          val bottle = Path().apply {
            moveTo(w * 0.38f, h * 0.14f)
            lineTo(w * 0.56f, h * 0.14f)
            lineTo(w * 0.56f, h * 0.26f)
            lineTo(w * 0.74f, h * 0.36f)
            lineTo(w * 0.74f, h * 0.84f)
            lineTo(w * 0.26f, h * 0.84f)
            lineTo(w * 0.26f, h * 0.36f)
            lineTo(w * 0.38f, h * 0.26f)
            close()
          }
          drawPath(bottle, color = silver, style = Stroke(3f, join = StrokeJoin.Round))
          drawRect(
            color = accent.copy(alpha = 0.42f),
            topLeft = Offset(w * 0.30f, h * 0.52f),
            size = Size(w * 0.40f, h * 0.28f),
          )
        }

        PartVisualKind.BELT_TIMING -> {
          drawCircle(silver, radius = w * 0.16f, center = Offset(w * 0.32f, h * 0.64f), style = Stroke(3f))
          drawCircle(accent, radius = w * 0.13f, center = Offset(w * 0.68f, h * 0.36f), style = Stroke(3f))
          drawLine(silver, Offset(w * 0.22f, h * 0.52f), Offset(w * 0.58f, h * 0.26f), strokeWidth = 3f)
          drawLine(silver, Offset(w * 0.42f, h * 0.76f), Offset(w * 0.78f, h * 0.46f), strokeWidth = 3f)
        }

        PartVisualKind.BODY_WIPER -> {
          drawArc(
            color = accent,
            startAngle = 200f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(w * 0.12f, h * 0.28f),
            size = Size(w * 0.76f, h * 0.56f),
            style = Stroke(3.5f, cap = StrokeCap.Round),
          )
          drawLine(silver, Offset(cx, h * 0.76f), Offset(w * 0.72f, h * 0.30f), strokeWidth = 3f, cap = StrokeCap.Round)
        }
      }
    }
  }
}

@Composable
fun VehicleHealthGauge(
  score: Int,
  modifier: Modifier = Modifier,
  size: Dp = 84.dp,
) {
  val gaugeColor = when {
    score >= 80 -> OemEmerald
    score >= 55 -> CautionAmber
    else -> Color(0xFFF87171)
  }
  val trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)

  Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
    Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
      val stroke = 8.dp.toPx()
      drawArc(
        color = trackColor,
        startAngle = 135f,
        sweepAngle = 270f,
        useCenter = false,
        style = Stroke(width = stroke, cap = StrokeCap.Round),
      )
      drawArc(
        color = gaugeColor,
        startAngle = 135f,
        sweepAngle = 270f * (score.coerceIn(0, 100) / 100f),
        useCenter = false,
        style = Stroke(width = stroke, cap = StrokeCap.Round),
      )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "$score%",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = gaugeColor,
      )
      Text(
        text = "HEALTH",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
