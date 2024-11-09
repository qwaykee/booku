package com.qwaykee.booku.ui.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.transitions.ScreenTransition

// Also see modifiers on ScreenTransition in MainActivity.kt
@OptIn(ExperimentalVoyagerApi::class)
class SwipeFadeTransition : ScreenTransition {
    private val swipeValue = 100
    private val duration = 200

    override fun enter(lastEvent: StackEvent): EnterTransition =
        slideInHorizontally(
            initialOffsetX = { if (lastEvent == StackEvent.Push) swipeValue else -swipeValue },
            animationSpec = tween(duration)
        ) + fadeIn(animationSpec = tween(duration))

    override fun exit(lastEvent: StackEvent): ExitTransition =
        slideOutHorizontally(
            targetOffsetX = { if (lastEvent == StackEvent.Pop) swipeValue else -swipeValue },
            animationSpec = tween(duration)
        ) + fadeOut(animationSpec = tween(duration))
}