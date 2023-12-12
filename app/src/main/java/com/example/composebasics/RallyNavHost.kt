package com.example.composebasics

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composebasics.ui.accounts.AccountsScreen
import com.example.composebasics.ui.bills.BillsScreen
import com.example.composebasics.ui.overview.OverviewScreen

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = OverviewNavInterface.route,
        modifier = modifier
    ){
        composable(route = OverviewNavInterface.route){
            OverviewScreen(
                onClickSeeAllAccounts = {
                    navController.navigateSingleTopTo(AccountsNavInterface.route)
                },
                onClickSeeAllBills = {
                    navController.navigateSingleTopTo(BillsNavInterface.route)
                },
                onAccountClick = {accountType ->
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }

        composable(route = AccountsNavInterface.route){
            AccountsScreen(
                onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }

        composable(route = BillsNavInterface.route){
            BillsScreen()
        }
        composable(
            route = SingleAccountNavInterface.routeWithArgs,
            arguments = SingleAccountNavInterface.arguments,
            deepLinks = SingleAccountNavInterface.deepLinks
        ){ navBackStackEntry ->
            val accountType = navBackStackEntry.arguments?.getString(SingleAccountNavInterface.accountTypeArg)
        }
    }
}

fun NavHostController.navigateSingleTopTo(route:String) = this.navigate(route){
    /**
     * Pop up to the start destination of the graph to avoid
     * building up a large stack of destinations of the back as
     * users select items
     * */
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ){
        saveState = true
    }
    // Avoid multiple copies of the same destination when
    // re-selecting the same item
    launchSingleTop = true
    // Restore state when selecting a previously selected item
    restoreState = true
}

private fun NavHostController.navigateToSingleAccount(accountType: String){
    this.navigateSingleTopTo("${SingleAccountNavInterface.route}/$accountType")
}