package com.example.composebasics.ui.accounts

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.composebasics.R
import com.example.composebasics.data.UserData
import com.example.composebasics.ui.components.AccountRow
import com.example.composebasics.ui.components.StatementBody
import com.example.composebasics.ui.theme.RallyTheme

@Composable
fun AccountsScreen(
    onAccountClick: (String) -> Unit = {}
){
    val amountsTotal = remember { UserData.accounts.map { account -> account.balance }.sum() }
    StatementBody(
        modifier = Modifier.semantics {contentDescription = "Accounts Screen"},
        items = UserData.accounts,
        colors = {account -> account.color },
        amounts = {account -> account.balance },
        amountsTotal = amountsTotal,
        circleLabel = stringResource(id = R.string.total),
        rows = {account ->
            AccountRow(
                modifier = Modifier.clickable {
                    onAccountClick(account.name)
                },
                account = account
            )
        }
    )
}

@Composable
fun SingleAccountScreen(
    accountType: String? = UserData.accounts.first().name
){
    val account = remember(accountType) {UserData.getAccount(accountType)}
    StatementBody(
        items = listOf(account),
        colors = {account.color},
        amounts = {account.balance},
        amountsTotal = account.balance,
        circleLabel =  account.name
    ) {row ->
        AccountRow(account = row)
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 520)
@Composable
fun AccountScreenPreview(){
    RallyTheme {
        AccountsScreen()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 520)
@Composable
fun SingleAccountScreenPreview(){
    RallyTheme {
        SingleAccountScreen()
    }
}
