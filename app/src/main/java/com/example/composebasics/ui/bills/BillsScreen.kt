package com.example.composebasics.ui.bills

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.composebasics.data.Bill
import com.example.composebasics.data.UserData


@Composable
fun BillsScreen(
    bills: List<Bill> = remember { UserData.bills}
){

}
