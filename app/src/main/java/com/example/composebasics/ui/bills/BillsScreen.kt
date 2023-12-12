package com.example.composebasics.ui.bills

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import com.example.composebasics.R
import com.example.composebasics.data.Bill
import com.example.composebasics.data.UserData
import com.example.composebasics.ui.components.BillRow
import com.example.composebasics.ui.components.StatementBody
import com.example.composebasics.ui.theme.RallyTheme


@Composable
fun BillsScreen(
    bills: List<Bill> = remember { UserData.bills}
){
    StatementBody(
        modifier = Modifier.clearAndSetSemantics { contentDescription = "Bills" },
        items = bills, 
        colors = {bill -> bill.color}, 
        amounts = {bill -> bill.amount}, 
        amountsTotal = bills.map {bill -> bill.amount}.sum(), 
        circleLabel = stringResource(R.string.due) ,
        rows = {
            bill -> BillRow(bill = bill)
        }
    ) 
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
fun BillScreenPreview(){
    RallyTheme {
        BillsScreen()
    }
}
