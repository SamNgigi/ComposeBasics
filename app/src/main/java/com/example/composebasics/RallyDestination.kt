package com.example.composebasics

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

// Contract for information needed on every Rally navigation destination
interface RallyDestination{
    val icon: ImageVector
    val route: String
}
// Rally app nav destinations
object OverviewNavInterface: RallyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "overview"
}
object AccountsNavInterface: RallyDestination {
    override val icon = Icons.Filled.AttachMoney
    override val route = "accounts"
}
object SingleAccountNavInterface: RallyDestination {
    override val icon = Icons.Filled.Money
    override val route = "single_account"
    // TODO: Might cause an issue. Review later.
    const val accountTypeArg = "account_type"
    val routeWithArgs = "$route/{$accountTypeArg}"
    val arguments = listOf(
        navArgument(accountTypeArg) {type = NavType.StringType}
    )
    val deepLinks = listOf(
        navDeepLink {uriPattern = "rally://$route/{$accountTypeArg}"}
    )
}
object BillsNavInterface: RallyDestination {
    override val icon = Icons.Filled.MoneyOff
    override val route = "bills"
}

// To be displayed in the top RallyTabRow
val rallyTabRowScreens= listOf(OverviewNavInterface, AccountsNavInterface, BillsNavInterface)


