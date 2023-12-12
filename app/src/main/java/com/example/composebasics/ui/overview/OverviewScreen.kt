package com.example.composebasics.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import com.example.composebasics.data.UserData
import com.example.composebasics.ui.components.AccountRow
import com.example.composebasics.ui.components.BillRow
import com.example.composebasics.ui.components.RallyAlertDialog
import com.example.composebasics.ui.components.RallyDivider
import com.example.composebasics.ui.components.formatAmount
import com.example.composebasics.ui.theme.RallyTheme
import java.util.Locale


private val _rallyDefaultPadding = 12.dp
private const val SHOWN_ITEMS = 3

@Composable
fun OverviewScreen(
    onClickSeeAllAccounts: () -> Unit = {},
    onClickSeeAllBills: () -> Unit = {},
    onAccountClick: (String) -> Unit = {}
){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    ){
        AlertCard()
        Spacer(modifier = Modifier.height(_rallyDefaultPadding))
        AccountsCard(onClickSeeAll = onClickSeeAllAccounts, onAccountClick = onAccountClick)
        Spacer(modifier = Modifier.height(_rallyDefaultPadding))
        BillsCard(onClickSeeAll=onClickSeeAllBills)
    }
}
@Composable
private fun <T> OverviewScreenCard(
    title:String,
    amount: Float,
    onClickSeeAll: () -> Unit,
    values: (T) -> Float,
    colors: (T) -> Color,
    data: List<T>,
    row: @Composable (T) -> Unit
){
    Card {
        Column{
            Column(Modifier.padding(_rallyDefaultPadding)){
                Text(text = title, style = MaterialTheme.typography.titleSmall)
                val amountText = "KSH " + formatAmount(amount)
                Text(text = amountText, style=MaterialTheme.typography.displayMedium)
            }
            OverviewDivider(data, values, colors)
            Column(Modifier.padding(start = 16.dp, top = 4.dp, end = 8.dp)){
                data.take(SHOWN_ITEMS).forEach { row(it)}
                SeeAllButton(
                    modifier = Modifier.clearAndSetSemantics {
                        contentDescription = "All $title"
                    },
                    onClick = onClickSeeAll
                )
            }
        }
    }
}

@Composable
private fun <T> OverviewDivider(
    data: List<T>,
    values: (T) -> Float,
    colors: (T) -> Color
){
    Row(Modifier.fillMaxWidth()){
        data.forEach{item: T ->
            Spacer(
                modifier = Modifier
                    .weight(values(item))
                    .height(1.dp)
                    .background(colors(item))
            )

        }
    }
}

@Composable
private fun AccountsCard(
    onClickSeeAll: () -> Unit,
    onAccountClick: (String) -> Unit
){
    val amount = UserData.accounts.map { account -> account.balance }.sum()
    OverviewScreenCard(
        title = stringResource(id = R.string.accounts),
        amount = amount,
        onClickSeeAll = onClickSeeAll,
        values = { it.balance },
        colors = { it.color },
        data = UserData.accounts
    ) { account ->
        AccountRow(
            modifier = Modifier.clickable { onAccountClick(account.name) },
            account = account
        )
    }
}

@Composable
private fun BillsCard(
    onClickSeeAll: () -> Unit
){
    val amount = UserData.bills.map { bill -> bill.amount }.sum()
    OverviewScreenCard(
        title = stringResource(id = R.string.bills),
        amount = amount,
        onClickSeeAll = onClickSeeAll,
        values = { it.amount },
        colors = { it.color },
        data = UserData.bills
    ) {bill ->
        BillRow(bill = bill)
    }
}

@Composable
private fun AlertCard(){
    var showDialog by remember { mutableStateOf(false) }
    val alertMessage = "Heads up, you've used up 90% of your Shopping budget for this month"

    if(showDialog){
        RallyAlertDialog(
            onDismiss = { showDialog = false },
            bodyText = alertMessage,
            buttonText = "Dismiss".uppercase(Locale.getDefault())
        )
    }
    Card {
        Column {
            AlertHeader {showDialog = true}
            RallyDivider (
                modifier = Modifier.padding(start= _rallyDefaultPadding, end= _rallyDefaultPadding)
            )
            AlertItem(alertMessage)
        }
    }
}

@Composable
private fun AlertHeader(
    onClickSeeAll: () -> Unit
){
    Row(
        modifier = Modifier
            .padding(_rallyDefaultPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = "Alerts",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        TextButton(
            onClick = onClickSeeAll,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "SEE ALL",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun AlertItem(message: String){

    Row(
        modifier = Modifier
            .padding(_rallyDefaultPadding)
            /**
             * We regard the whole role as one semantic node. This way each row will receive
             * focus as a whole and the focus bound will be around the whole row content.
             * Semantics properties of the descendants will be merged.
             * If we use clearAndSetSemantics instead we'd have to define semantic properties
             * explicitly
             * */
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.Top)
                .clearAndSetSemantics { }
        ) {
            Icon(Icons.Filled.Sort, contentDescription = null)
        }
    }
}

@Composable
private fun SeeAllButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    TextButton(
        onClick = onClick,
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
    ){
        Text(stringResource(R.string.see_all))
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun OverViewScreenPreview(){
    RallyTheme {
        OverviewScreen()
    }
}




