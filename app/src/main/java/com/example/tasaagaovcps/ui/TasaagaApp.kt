package com.example.tasaagaovcps.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import com.example.tasaagaovcps.ui.components.TasaagaLogo
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.tasaagaovcps.ui.navigation.AboutRoute
import com.example.tasaagaovcps.ui.navigation.ContactRoute
import com.example.tasaagaovcps.ui.navigation.HomeRoute
import com.example.tasaagaovcps.ui.navigation.CommunityRoute
import com.example.tasaagaovcps.ui.navigation.MissionRoute
import com.example.tasaagaovcps.ui.navigation.MoreRoute
import com.example.tasaagaovcps.ui.navigation.SupportRoute
import com.example.tasaagaovcps.ui.navigation.TOP_LEVEL_ROUTES
import com.example.tasaagaovcps.ui.navigation.TasaagaRoute
import com.example.tasaagaovcps.ui.navigation.VolunteerRoute
import com.example.tasaagaovcps.ui.navigation.VisionRoute
import com.example.tasaagaovcps.ui.navigation.PartnershipsRoute
import com.example.tasaagaovcps.ui.navigation.LoginRoute
import com.example.tasaagaovcps.ui.screens.AboutScreen
import com.example.tasaagaovcps.ui.screens.CommunityScreen
import com.example.tasaagaovcps.ui.screens.ContactScreen
import com.example.tasaagaovcps.ui.screens.HomeScreen
import com.example.tasaagaovcps.ui.screens.LoginScreen
import com.example.tasaagaovcps.ui.screens.MissionScreen
import com.example.tasaagaovcps.ui.screens.PartnershipsScreen
import com.example.tasaagaovcps.ui.screens.SupportScreen
import com.example.tasaagaovcps.ui.screens.VolunteerScreen
import com.example.tasaagaovcps.ui.screens.VisionScreen
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.AppViewModelProvider
import com.example.tasaagaovcps.ui.viewmodel.NewsViewModel
import com.example.tasaagaovcps.ui.viewmodel.SchoolViewModel
import com.example.tasaagaovcps.ui.viewmodel.SupportViewModel
import com.example.tasaagaovcps.ui.viewmodel.VolunteerViewModel

@Composable
fun TasaagaApp() {
    val topLevelBackStack = remember { TopLevelBackStack<TasaagaRoute>(HomeRoute) }

    NavigationSuiteScaffold(
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = MaterialTheme.colorScheme.primary,
            navigationBarContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationRailContainerColor = MaterialTheme.colorScheme.primary,
            navigationRailContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationDrawerContainerColor = MaterialTheme.colorScheme.primary,
            navigationDrawerContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationSuiteItems = {
            TOP_LEVEL_ROUTES.forEach { route ->
                item(
                    selected = topLevelBackStack.topLevelKey == route,
                    onClick = { topLevelBackStack.addTopLevel(route) },
                    icon = { Icon(route.icon, contentDescription = route.label) },
                    label = { Text(route.label) }
                )
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TasaagaLogo()
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                NavDisplay(
                    backStack = topLevelBackStack.backStack,
                    onBack = { topLevelBackStack.removeLast() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    entryProvider = entryProvider {
                        entry<HomeRoute> {
                            HomeScreen()
                        }
                        entry<AboutRoute> {
                            AboutScreen()
                        }
                        entry<VisionRoute> {
                            VisionScreen()
                        }
                        entry<PartnershipsRoute> {
                            PartnershipsScreen()
                        }
                        entry<ContactRoute> {
                            ContactScreen()
                        }
                        entry<LoginRoute> {
                            LoginScreen(
                                onLoginSuccess = { role ->
                                    // Implementation for routing after login can go here
                                    topLevelBackStack.removeLast()
                                }
                            )
                        }
                        entry<MissionRoute> {
                            val viewModel: SchoolViewModel = viewModel(factory = AppViewModelProvider.Factory)
                            MissionScreen(viewModel = viewModel)
                        }
                        entry<SupportRoute> {
                            val viewModel: SupportViewModel = viewModel(factory = AppViewModelProvider.Factory)
                            SupportScreen(viewModel = viewModel)
                        }
                        entry<VolunteerRoute> {
                            val viewModel: VolunteerViewModel = viewModel(factory = AppViewModelProvider.Factory)
                            VolunteerScreen(viewModel = viewModel)
                        }
                        entry<CommunityRoute> {
                            val viewModel: NewsViewModel = viewModel(factory = AppViewModelProvider.Factory)
                            CommunityScreen(viewModel = viewModel)
                        }
                        entry<MoreRoute> {
                            MoreScreen(
                                onNavigate = { subRoute ->
                                    topLevelBackStack.navigateToSubRoute(subRoute)
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MoreScreen(onNavigate: (TasaagaRoute) -> Unit) {
    val items = listOf(
        LoginRoute,
        AboutRoute,
        VisionRoute,
        PartnershipsRoute,
        ContactRoute,
        CommunityRoute
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Discover More",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
            )
        }
        items(items) { route ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(route) }
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = route.label,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = route.icon,
                            contentDescription = route.label,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "Navigate to ${route.label}",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    }
}

class TopLevelBackStack<T : Any>(startKey: T) {
    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
        startKey to mutableStateListOf(startKey)
    )

    var topLevelKey by mutableStateOf(startKey)
        private set

    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() {
        backStack.clear()
        backStack.addAll(topLevelStacks.flatMap { it.value })
    }

    fun addTopLevel(key: T) {
        if (topLevelStacks[key] == null) {
            topLevelStacks[key] = mutableStateListOf(key)
        } else {
            topLevelStacks.apply {
                remove(key)?.let {
                    put(key, it)
                }
            }
        }
        topLevelKey = key
        updateBackStack()
    }

    fun navigateToSubRoute(route: T) {
        topLevelStacks[topLevelKey]?.add(route)
        updateBackStack()
    }

    fun removeLast() {
        if ((topLevelStacks[topLevelKey]?.size ?: 0) > 1) {
            topLevelStacks[topLevelKey]?.removeLastOrNull()
        } else if (topLevelStacks.size > 1) {
            topLevelStacks.remove(topLevelKey)
            topLevelKey = topLevelStacks.keys.last()
        }
        updateBackStack()
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun TasaagaAppPreview() {
    TasaagaOVCPSTheme {
        TasaagaApp()
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun TasaagaAppTabletPreview() {
    TasaagaOVCPSTheme {
        TasaagaApp()
    }
}
