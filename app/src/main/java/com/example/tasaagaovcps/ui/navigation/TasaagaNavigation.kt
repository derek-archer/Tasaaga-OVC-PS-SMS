package com.example.tasaagaovcps.ui.navigation

import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Handshake
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface TasaagaRoute : NavKey {
    val icon: ImageVector
    val label: String
}

@Serializable
data object HomeRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Home
    override val label = "Home"
}

@Serializable
data object LoginRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Lock
    override val label = "Portal Login"
}

@Serializable
data object AboutRoute : TasaagaRoute {
    override val icon = Icons.AutoMirrored.Rounded.MenuBook
    override val label = "About Us"
}

@Serializable
data object VisionRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Visibility
    override val label = "Vision"
}

@Serializable
data object PartnershipsRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Handshake
    override val label = "Partnerships"
}

@Serializable
data object ContactRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Call
    override val label = "Contact Us"
}

@Serializable
data object MissionRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Info
    override val label = "Programs"
}

@Serializable
data object SupportRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Favorite
    override val label = "Donate"
}

@Serializable
data object VolunteerRoute : TasaagaRoute {
    override val icon = Icons.Rounded.VolunteerActivism
    override val label = "Volunteer"
}

@Serializable
data object CommunityRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Public
    override val label = "Community"
}

@Serializable
data object MoreRoute : TasaagaRoute {
    override val icon = Icons.Rounded.Menu
    override val label = "More"
}

val TOP_LEVEL_ROUTES = listOf(
    HomeRoute,
    MissionRoute,
    SupportRoute,
    VolunteerRoute,
    MoreRoute
)

