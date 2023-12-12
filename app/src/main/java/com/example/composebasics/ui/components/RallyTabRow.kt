package com.example.composebasics.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composebasics.RallyDestination
import com.example.composebasics.ui.theme.RallyTheme
import java.util.Locale

private val _tabHeight = 56.dp
private const val INACTIVE_TAB_OPACITY = 0.60f

private const val TAB_FADE_IN_ANIMATION_DURATION = 150
private const val TAB_FADE_IN_ANIMATION_DELAY = 100
private const val TAB_FADE_OUT_ANIMATION_DURATION = 100

@Composable
fun RallyTabRow(
    allScreens: List<RallyDestination>,
    onTabSelected: (RallyDestination) -> Unit,
    currentScreen: RallyDestination
){
    Surface(
        Modifier
            .height(_tabHeight)
            .fillMaxWidth()
    ){
        Row(Modifier.selectableGroup()){
            allScreens.forEach{ screen ->
                RallyTab(
                    text = screen.route,
                    icon = screen.icon,
                    onSelected = {onTabSelected(screen)},
                    selected = currentScreen == screen
                )
            }
        }
    }
}

@Composable
private fun RallyTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean
){
    val color = MaterialTheme.colorScheme.onSurface
    val durationMillis = if(selected) TAB_FADE_IN_ANIMATION_DURATION else TAB_FADE_OUT_ANIMATION_DURATION
    val animationSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis =  TAB_FADE_IN_ANIMATION_DELAY
        )
    }

    val tabIntColor by animateColorAsState(
        targetValue = if(selected) color else color.copy(alpha = INACTIVE_TAB_OPACITY),
        animationSpec = animationSpec, label = ""
    )
    Row(
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize()
            .height(_tabHeight)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
            .clearAndSetSemantics { contentDescription = text }
    ){
        Icon(imageVector = icon, contentDescription = text, tint = tabIntColor)
        if(selected){
            Spacer(Modifier.width(12.dp))
            Text(text.uppercase(Locale.getDefault()), color = tabIntColor)
        }
    }
}


