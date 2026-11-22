package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.auth.AdminAccessControl
import com.example.ui.theme.RacingOrange
import com.example.ui.theme.TelemetryCyan

private const val DEVELOPER_FACEBOOK_URL = "https://www.facebook.com/"

@Composable
fun DeveloperFooterCard(
  modifier: Modifier = Modifier,
) {
  val uriHandler = LocalUriHandler.current

  Surface(
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium)
      .testTag("developer_footer_card"),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        painter = painterResource(id = R.drawable.developer_avatar),
        contentDescription = "Developer Àbdüllāh Aĺ Hỗŝŝâîň portrait",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .border(1.5.dp, TelemetryCyan, CircleShape)
          .testTag("developer_avatar_image"),
      )

      Spacer(Modifier.width(12.dp))

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp),
      ) {
        Text(
          text = "EME AUTOMATIC HELP • AutoPulse",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = RacingOrange,
        )
        Text(
          text = "Developed by Àbdüllāh Aĺ Hỗŝŝâîň",
          style = MaterialTheme.typography.bodySmall,
          color = TelemetryCyan,
        )
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        IconButton(
          onClick = { runCatching { uriHandler.openUri(DEVELOPER_FACEBOOK_URL) } },
          colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
          ),
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("developer_facebook_button"),
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_facebook),
            contentDescription = "Facebook",
            modifier = Modifier.size(24.dp),
          )
        }

        IconButton(
          onClick = {
            runCatching { uriHandler.openUri("mailto:${AdminAccessControl.SUPER_ADMIN_EMAIL}") }
          },
          colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
          ),
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("developer_gmail_button"),
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_gmail),
            contentDescription = "Gmail",
            modifier = Modifier.size(24.dp),
          )
        }
      }
    }
  }
}
