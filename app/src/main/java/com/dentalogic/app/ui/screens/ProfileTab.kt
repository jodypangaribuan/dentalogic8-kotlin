package com.dentalogic.app.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.Button
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.dentalogic.app.R
import com.dentalogic.app.ui.components.ExpressiveSegmentedRow
import com.dentalogic.app.ui.theme.AppTheme

/** The screens reachable from the Profile tab. Hoisted to MainScreen. */
enum class ProfileRoute { List, Changelog }

/**
 * "Profile" destination: app theme choice, version details, repository link, and developer information.
 */
@Composable
fun ProfileTab(
    contentPadding: PaddingValues,
    route: ProfileRoute,
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    onOpenChangelog: () -> Unit,
) {
    AnimatedContent(
        targetState = route,
        transitionSpec = {
            fadeIn(animationSpec = tween(280)) togetherWith fadeOut(animationSpec = tween(200))
        },
        label = "profileRoute",
    ) { current ->
        when (current) {
            ProfileRoute.List -> ProfileList(
                contentPadding = contentPadding,
                currentTheme = currentTheme,
                onThemeChange = onThemeChange,
                onOpenChangelog = onOpenChangelog,
            )
            ProfileRoute.Changelog -> ChangelogScreen(contentPadding)
        }
    }
}

@Composable
private fun ProfileList(
    contentPadding: PaddingValues,
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    onOpenChangelog: () -> Unit,
) {
    val context = LocalContext.current
    val versionName = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: "1.0.0"
    }
    val openUrl = { url: String ->
        context.startActivity(
            Intent(Intent.ACTION_VIEW, url.toUri())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }

    val haptics = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppHeader()

        Spacer(Modifier.height(24.dp))

        ThemeCard(
            selected = currentTheme,
            onSelect = onThemeChange,
        )

        VersionCard(
            versionName = versionName,
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onOpenChangelog()
            },
        )

        val githubProjectUrl = stringResource(R.string.profile_github_project_url)
        val githubProfileUrl = stringResource(R.string.profile_github_url)
        val linkedInUrl = stringResource(R.string.profile_linkedin_url)

        GitHubCard(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                openUrl(githubProjectUrl)
            },
        )

        DevCard(
            onOpenGitHub = { openUrl(githubProfileUrl) },
            onOpenLinkedIn = { openUrl(linkedInUrl) },
        )

        Spacer(Modifier.height(16.dp))
    }
}

/**
 * The app's identity at the top of the tab: the tooth icon, the app name and the tagline.
 */
@Composable
private fun AppHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = stringResource(R.string.app_icon_description),
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape),
            )
        }
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = stringResource(R.string.app_tagline),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

/**
 * The "about" card with developer details and social links.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DevCard(
    onOpenGitHub: () -> Unit,
    onOpenLinkedIn: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DevAvatar()

            Text(
                text = stringResource(R.string.dev_card_author),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 20.dp),
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = onOpenGitHub) {
                    Icon(
                        painter = painterResource(R.drawable.ic_github),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.dev_card_github))
                }
                Button(onClick = onOpenLinkedIn) {
                    Icon(
                        painter = painterResource(R.drawable.ic_linkedin),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.dev_card_linkedin))
                }
            }
        }
    }
}

@Composable
private fun DevAvatar() {
    Image(
        painter = painterResource(R.drawable.dev_avatar),
        contentDescription = stringResource(R.string.dev_card_avatar),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(112.dp)
            .clip(RoundedCornerShape(28.dp)),
    )
}

@Composable
private fun ThemeCard(selected: AppTheme, onSelect: (AppTheme) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.profile_theme),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            ExpressiveSegmentedRow(
                options = AppTheme.entries.map { stringResource(it.labelRes) },
                selectedIndex = selected.ordinal,
                onSelect = { onSelect(AppTheme.entries[it]) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun VersionCard(versionName: String, onClick: () -> Unit) {
    val number = versionName.substringBefore('-')
    val preRelease = versionName.contains('-')

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.profile_version),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = number,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    if (preRelease) {
                        Spacer(Modifier.width(12.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ) {
                            Text(
                                text = stringResource(R.string.version_beta),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(R.string.profile_version_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun GitHubCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_github),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(26.dp),
            )
            Spacer(Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.profile_github_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(R.string.profile_github_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
