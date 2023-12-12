package com.example.composebasics.ui.components

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import com.example.composebasics.data.Account
import com.example.composebasics.data.Bill
import com.example.composebasics.ui.theme.RallyTheme
import java.text.DecimalFormat


private val _accountDecimalFormat = DecimalFormat("####")
private val _amountDecimalFormat = DecimalFormat("#,###.##")

fun formatAmount(amount: Float): String{
    return _amountDecimalFormat.format(amount)
}

// Used with Accounts & Bills to create animated circle
fun <E> List<E>.extractProportions(selector: (E) -> Float) : List<Float> {
    val total = this.sumOf { selector(it).toDouble() }
    return this.map { (selector(it)/total).toFloat()}
}

// Vertical colored line that is used in a Base Row to differentiate accounts
@Composable
fun RallyDivider(modifier: Modifier = Modifier){
    Divider(
        color = MaterialTheme.colorScheme.background,
        thickness = 1.dp,
        modifier = Modifier
    )
}

@Composable
private fun AccountIndicator(
    color: Color,
    modifier: Modifier = Modifier
){
    Spacer(
        modifier
            .size(4.dp, 36.dp)
            .background(color = color)
    )
}

@Composable
private fun BaseRow(
    modifier:Modifier = Modifier,
    color: Color,
    title:String,
    subtitle: String,
    amount: Float,
    negative: Boolean
){
    val moneySign = if (negative) "-KSH" else "KSH"
    val formattedAmount = formatAmount(amount)
    Row(
        modifier = modifier
            .height(68.dp)
            .clearAndSetSemantics {
                contentDescription =
                    "$title account ending in ${subtitle.takeLast(4)}, current balance $moneySign$formattedAmount"
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        val typography = MaterialTheme.typography
        AccountIndicator(
            color = color,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(Modifier){
            Text(text = title, style = typography.bodyLarge)
            // Medium Emphasis
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant){
                Text(text = subtitle, style=typography.titleMedium)
            }
        }
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = moneySign,
                style = typography.titleLarge,
//                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = formattedAmount,
                style = typography.titleLarge,
//                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)

            )
        }
        Spacer(Modifier.width(16.dp))
        // Medium Emphasis
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant){
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
            )
        }
    }

    RallyDivider()
}

@Composable
fun AccountRow(
    modifier: Modifier = Modifier,
    account: Account
){
    BaseRow(
        modifier = modifier,
        color = account.color,
        title = account.name,
        subtitle = stringResource(R.string.account_redacted) + _accountDecimalFormat.format(account.number),
        amount = account.balance,
        negative = false
    )
}

@Composable
fun BillRow(bill: Bill){
    BaseRow(
        color = bill.color,
        title = bill.name,
        subtitle = bill.due,
        amount = bill.amount,
        negative = true
    )
}

