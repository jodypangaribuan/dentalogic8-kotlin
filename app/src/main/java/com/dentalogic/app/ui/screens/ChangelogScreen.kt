package com.dentalogic.app.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dentalogic.app.R
import org.json.JSONArray

private const val ChangelogAsset = "changelog.json"

internal data class Release(
    val version: String,
    val description: String,
    val features: List<String> = emptyList(),
    val bugfixes: List<String> = emptyList(),
)

private fun loadReleases(context: Context): List<Release> = runCatching {
    val json = context.assets.open(ChangelogAsset).bufferedReader().use { it.readText() }
    val array = JSONArray(json)
    (0 until array.length()).map { i ->
        val obj = array.getJSONObject(i)
        Release(
            version = obj.getString("version"),
            description = obj.getString("description"),
            features = obj.optJSONArray("features")?.let { arr ->
                (0 until arr.length()).map { arr.getString(it) }
            } ?: emptyList(),
            bugfixes = obj.optJSONArray("bugfixes")?.let { arr ->
                (0 until arr.length()).map { arr.getString(it) }
            } ?: emptyList(),
        )
    }
}.getOrDefault(emptyList())

@Composable
fun ChangelogScreen(contentPadding: PaddingValues) {
    val context = LocalContext.current
    val releases = remember { loadReleases(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.changelog_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(R.string.changelog_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        releases.forEachIndexed { index, release ->
            ReleaseCard(release = release, isCurrent = index == 0)
        }

        Spacer(Modifier.padding(bottom = 16.dp))
    }
}

@Composable
private fun ReleaseCard(release: Release, isCurrent: Boolean) {
    val number = release.version.substringBefore('-')
    val preRelease = release.version.contains('-')

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
                if (preRelease) {
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ) {
                        Text(
                            text = stringResource(R.string.version_beta),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        )
                    }
                }
            }

            Text(
                text = release.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (release.features.isNotEmpty()) {
                BulletSection(
                    icon = Icons.Rounded.AutoAwesome,
                    title = stringResource(R.string.changelog_features),
                    items = release.features,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            if (release.bugfixes.isNotEmpty()) {
                BulletSection(
                    icon = Icons.Rounded.BugReport,
                    title = stringResource(R.string.changelog_fixes),
                    items = release.bugfixes,
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Composable
private fun BulletSection(
    icon: ImageVector,
    title: String,
    items: List<String>,
    tint: Color,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = tint,
            )
        }

        items.forEach { bullet ->
            Row(
                modifier = Modifier.padding(start = 6.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = bullet,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
