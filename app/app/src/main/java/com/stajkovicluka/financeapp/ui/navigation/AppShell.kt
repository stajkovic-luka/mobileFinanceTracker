package com.stajkovicluka.financeapp.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.ui.theme.ThemeMode
import com.stajkovicluka.financeapp.util.TokenManager
import kotlinx.coroutines.launch

// Zajednicki raspored za glavne ekrane prijavljenog korisnika.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(
    selectedDestination: AppDestination,
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onHomeClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onReportsClick: () -> Unit,
    onCreateGoal: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val tokenManager = remember(context) { TokenManager(context) }
    var plusMenuVisible by remember { mutableStateOf(false) }
    var profileDialogVisible by remember { mutableStateOf(false) }

    val navigationItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onSecondary,
        selectedTextColor = MaterialTheme.colorScheme.onSecondary,
        indicatorColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
        unselectedTextColor = MaterialTheme.colorScheme.onSecondary
    )
    val drawerItemColors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        unselectedContainerColor = Color.Transparent,
        unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
        unselectedTextColor = MaterialTheme.colorScheme.onSecondary
    )

    if (profileDialogVisible) {
        AlertDialog(
            onDismissRequest = { profileDialogVisible = false },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            title = { Text(stringResource(R.string.profile_title)) },
            text = {
                Column {
                    ProfileDataRow(
                        stringResource(R.string.profile_name_label),
                        tokenManager.getUserName()
                    )
                    ProfileDataRow(
                        stringResource(R.string.profile_username_label),
                        tokenManager.getUsername()
                    )
                    ProfileDataRow(
                        stringResource(R.string.profile_email_label),
                        tokenManager.getEmail()
                    )
                    ProfileDataRow(
                        stringResource(R.string.profile_created_at_label),
                        formatProfileDate(tokenManager.getCreatedAt())
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { profileDialogVisible = false }) {
                    Text(stringResource(R.string.close_button))
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                drawerContainerColor = MaterialTheme.colorScheme.secondary,
                drawerContentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Text(
                    text = stringResource(R.string.menu_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.profile_title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    selected = false,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        profileDialogVisible = true
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = drawerItemColors
                )
                Text(
                    text = stringResource(R.string.theme_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 4.dp)
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.theme_system),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    selected = themeMode == ThemeMode.SYSTEM,
                    icon = { Icon(Icons.Default.SettingsBrightness, contentDescription = null) },
                    onClick = { onThemeModeChanged(ThemeMode.SYSTEM) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.theme_light),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    selected = themeMode == ThemeMode.LIGHT,
                    icon = { Icon(Icons.Default.LightMode, contentDescription = null) },
                    onClick = { onThemeModeChanged(ThemeMode.LIGHT) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.theme_dark),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    selected = themeMode == ThemeMode.DARK,
                    icon = { Icon(Icons.Default.DarkMode, contentDescription = null) },
                    onClick = { onThemeModeChanged(ThemeMode.DARK) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.logout_button),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    selected = false,
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                    onClick = onLogout,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = drawerItemColors
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.app_name),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Image(
                                painter = painterResource(R.drawable.acorn),
                                contentDescription = stringResource(R.string.acorn_content_description),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    navigationIcon = {
                        TextButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.menu_content_description)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.secondary) {
                        NavigationBarItem(
                            selected = selectedDestination == AppDestination.HOME,
                            onClick = onHomeClick,
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            label = { Text(stringResource(R.string.home_navigation_label)) },
                            colors = navigationItemColors
                        )
                        NavigationBarItem(
                            selected = selectedDestination == AppDestination.GOALS,
                            onClick = onGoalsClick,
                            icon = { Icon(Icons.Default.Flag, contentDescription = null) },
                            label = { Text(stringResource(R.string.goals_navigation_label)) },
                            colors = navigationItemColors
                        )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { plusMenuVisible = true },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                text = stringResource(R.string.add_navigation_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                        DropdownMenu(
                            expanded = plusMenuVisible,
                            onDismissRequest = { plusMenuVisible = false },
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.create_goal_menu_item),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    plusMenuVisible = false
                                    onCreateGoal()
                                }
                            )
                        }
                    }
                        NavigationBarItem(
                            selected = selectedDestination == AppDestination.REPORTS,
                            onClick = onReportsClick,
                            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                            label = { Text(stringResource(R.string.reports_navigation_label)) },
                            colors = navigationItemColors
                        )
                    }
                }
            },
            content = content
        )
    }
}

// Prikazuje jednu informaciju o prijavljenom korisniku u profilu.
@Composable
private fun ProfileDataRow(label: String, value: String?) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        text = value?.takeIf { it.isNotBlank() } ?: "-",
        style = MaterialTheme.typography.bodyLarge
    )
}

// Pretvara datum iz backend odgovora u evropski format.
private fun formatProfileDate(createdAt: String?): String {
    val parts = createdAt.orEmpty().substringBefore("T").split("-")
    return if (parts.size == 3) "${parts[2]}.${parts[1]}.${parts[0]}" else "-"
}

// Oznacava aktivnu stavku donje navigacije.
enum class AppDestination {
    HOME,
    GOALS,
    REPORTS
}
