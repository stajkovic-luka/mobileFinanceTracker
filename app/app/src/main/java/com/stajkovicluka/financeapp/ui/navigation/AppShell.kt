package com.stajkovicluka.financeapp.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.ui.theme.ThemeMode
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
    var plusMenuVisible by remember { mutableStateOf(false) }
    var profileDialogVisible by remember { mutableStateOf(false) }

    if (profileDialogVisible) {
        AlertDialog(
            onDismissRequest = { profileDialogVisible = false },
            title = { Text(stringResource(R.string.profile_title)) },
            text = { Text(stringResource(R.string.profile_placeholder_message)) },
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
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                Text(
                    text = stringResource(R.string.menu_title),
                    style = MaterialTheme.typography.titleMedium,
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
                    colors = NavigationDrawerItemDefaults.colors()
                )
                Text(
                    text = stringResource(R.string.theme_title),
                    style = MaterialTheme.typography.labelLarge,
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
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
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
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
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
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
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
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
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
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedDestination == AppDestination.HOME,
                        onClick = onHomeClick,
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text(stringResource(R.string.home_navigation_label)) }
                    )
                    NavigationBarItem(
                        selected = selectedDestination == AppDestination.GOALS,
                        onClick = onGoalsClick,
                        icon = { Icon(Icons.Default.Flag, contentDescription = null) },
                        label = { Text(stringResource(R.string.goals_navigation_label)) }
                    )
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .clickable { plusMenuVisible = true }
                                .padding(vertical = 12.dp, horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Text(
                                text = stringResource(R.string.add_navigation_label),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                        DropdownMenu(
                            expanded = plusMenuVisible,
                            onDismissRequest = { plusMenuVisible = false }
                        ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.create_goal_menu_item)) },
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
                        label = { Text(stringResource(R.string.reports_navigation_label)) }
                    )
                }
            },
            content = content
        )
    }
}

// Oznacava aktivnu stavku donje navigacije.
enum class AppDestination {
    HOME,
    GOALS,
    REPORTS
}
