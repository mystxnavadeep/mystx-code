package com.mystxcode.app

import androidx.compose.runtime.Composable
import com.mystxcode.app.ui.chat.ChatScreen
import com.mystxcode.app.ui.chat.ChatViewModel

@Composable
fun MainNavigation(viewModel: ChatViewModel? = null) {
    if (viewModel != null) {
        ChatScreen(viewModel = viewModel)
    } else {
        ChatScreen()
    }
}
