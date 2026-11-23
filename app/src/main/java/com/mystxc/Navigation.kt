package com.mystxc

import androidx.compose.runtime.Composable
import com.mystxc.ui.chat.ChatScreen
import com.mystxc.ui.chat.ChatViewModel

@Composable
fun MainNavigation(viewModel: ChatViewModel? = null) {
    if (viewModel != null) {
        ChatScreen(viewModel = viewModel)
    } else {
        ChatScreen()
    }
}
