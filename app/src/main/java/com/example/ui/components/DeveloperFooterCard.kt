package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
private const val PREFS_COMMUNITY_STATS = "eme_community_stats"
private const val KEY_VISITS = "site_visits"
private const val KEY_FOLLOWERS = "site_followers"
private const val KEY_LIKES = "site_likes"
private const val KEY_FOLLOWED = "site_followed"
private const val KEY_LIKED = "site_liked"
private const val KEY_COMMENTS = "site_comments_v1"

private data class FooterComment(val author: String, val text: String)

private val defaultFooterComments = listOf(
  FooterComment("মোঃ রাশেদুল ইসলাম", "বাংলাদেশ সেনাবাহিনীর সকল যানবাহনের BAO 35/88 মেইনটেনেন্স শিডিউল ও পার্টস লিস্ট খুবই কার্যকর।"),
  FooterComment("Tariqul Hasan", "Fast OEM search with Google Image support for Bolyan, TRM 180/240/360 & Pajero."),
  FooterComment("ইঞ্জিনিয়ার সাকিব", "ফুয়েল ফিল্টার, অল্টারনেটর, স্টার্টার ও ব্রেকের ক্ষুদ্র যন্ত্রাংশগুলো ওয়ার্কশপের জন্য দারুণ সহায়ক।"),
)

private object FooterVisitTracker {
  var recordedInProcess: Boolean = false
}

private fun loadStoredComments(raw: String?): List<FooterComment> {
  if (raw.isNullOrBlank()) return defaultFooterComments
  val parsed = raw.split("\n").mapNotNull { line ->
    val idx = line.indexOf('|')
    if (idx <= 0) null else FooterComment(line.substring(0, idx), line.substring(idx + 1))
  }
  return parsed.ifEmpty { defaultFooterComments }
}

private fun serializeComments(list: List<FooterComment>): String =
  list.joinToString("\n") { "${it.author.replace("|", " ").replace("\n", " ")}|${it.text.replace("\n", " ")}" }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeveloperFooterCard(
  modifier: Modifier = Modifier,
) {
  val uriHandler = LocalUriHandler.current
  val context = LocalContext.current
  val prefs = remember(context) {
    context.getSharedPreferences(PREFS_COMMUNITY_STATS, Context.MODE_PRIVATE)
  }

  var visits by remember { mutableIntStateOf(prefs.getInt(KEY_VISITS, 0)) }
  var followers by remember { mutableIntStateOf(prefs.getInt(KEY_FOLLOWERS, 0)) }
  var likes by remember { mutableIntStateOf(prefs.getInt(KEY_LIKES, 0)) }
  var isFollowed by remember { mutableStateOf(prefs.getBoolean(KEY_FOLLOWED, false)) }
  var isLiked by remember { mutableStateOf(prefs.getBoolean(KEY_LIKED, false)) }
  var comments by remember { mutableStateOf(loadStoredComments(prefs.getString(KEY_COMMENTS, null))) }
  var draftComment by remember { mutableStateOf("") }

  LaunchedEffect(Unit) {
    if (!FooterVisitTracker.recordedInProcess) {
      FooterVisitTracker.recordedInProcess = true
      val updatedVisits = prefs.getInt(KEY_VISITS, 0) + 1
      prefs.edit().putInt(KEY_VISITS, updatedVisits).apply()
      visits = updatedVisits
    }
  }

  Surface(
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium)
      .testTag("developer_footer_card"),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
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

      Spacer(Modifier.height(10.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
      Spacer(Modifier.height(8.dp))

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("footer_community_stats_row"),
      ) {
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surfaceContainerHigh,
          modifier = Modifier.testTag("footer_visits_chip"),
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
          ) {
            Icon(
              imageVector = Icons.Rounded.Visibility,
              contentDescription = "Visits",
              tint = TelemetryCyan,
              modifier = Modifier.size(16.dp),
            )
            Text(
              text = "ভিজিট: $visits",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurface,
            )
          }
        }

        FilterChip(
          selected = isFollowed,
          onClick = {
            val next = !isFollowed
            val nextCount = (followers + if (next) 1 else -1).coerceAtLeast(0)
            isFollowed = next
            followers = nextCount
            prefs.edit()
              .putBoolean(KEY_FOLLOWED, next)
              .putInt(KEY_FOLLOWERS, nextCount)
              .apply()
          },
          leadingIcon = {
            Icon(
              imageVector = if (isFollowed) Icons.Rounded.Check else Icons.Rounded.PersonAdd,
              contentDescription = "Followers",
              modifier = Modifier.size(16.dp),
            )
          },
          label = {
            Text(
              text = if (isFollowed) "ফলোয়িং: $followers" else "ফলো: $followers",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = TelemetryCyan.copy(alpha = 0.18f),
            selectedLabelColor = TelemetryCyan,
            selectedLeadingIconColor = TelemetryCyan,
          ),
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("footer_follow_button"),
        )

        FilterChip(
          selected = isLiked,
          onClick = {
            val next = !isLiked
            val nextCount = (likes + if (next) 1 else -1).coerceAtLeast(0)
            isLiked = next
            likes = nextCount
            prefs.edit()
              .putBoolean(KEY_LIKED, next)
              .putInt(KEY_LIKES, nextCount)
              .apply()
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Rounded.ThumbUp,
              contentDescription = "Likes",
              modifier = Modifier.size(16.dp),
            )
          },
          label = {
            Text(
              text = "লাইক: $likes",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = RacingOrange.copy(alpha = 0.18f),
            selectedLabelColor = RacingOrange,
            selectedLeadingIconColor = RacingOrange,
          ),
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("footer_like_button"),
        )
      }

      Spacer(Modifier.height(10.dp))
      Text(
        text = "মতামত ও কমেন্ট / Comments (${comments.size})",
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = TelemetryCyan,
      )
      Spacer(Modifier.height(6.dp))
      Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("footer_comments_list"),
      ) {
        comments.take(4).forEach { item ->
          Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)) {
              Text(
                text = item.author,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TelemetryCyan,
              )
              Text(
                text = item.text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
              )
            }
          }
        }
      }

      Spacer(Modifier.height(8.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
      ) {
        OutlinedTextField(
          value = draftComment,
          onValueChange = { draftComment = it },
          singleLine = true,
          placeholder = { Text("বাংলা বা ইংরেজিতে কমেন্ট লিখুন…") },
          modifier = Modifier
            .weight(1f)
            .testTag("footer_comment_input"),
        )
        FilledTonalIconButton(
          onClick = {
            val clean = draftComment.trim()
            if (clean.isNotEmpty()) {
              val updated = listOf(FooterComment("User", clean)) + comments
              comments = updated.take(25)
              draftComment = ""
              prefs.edit().putString(KEY_COMMENTS, serializeComments(comments)).apply()
            }
          },
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("footer_comment_submit_button"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Rounded.Send,
            contentDescription = "Post comment",
          )
        }
      }
    }
  }
}
