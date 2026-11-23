package com.mystxc.data.agent

import com.mystxc.domain.model.ChatAttachment
import com.mystxc.domain.model.ChatMessage
import com.mystxc.domain.model.Conversation
import com.mystxc.domain.model.ToolCall
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * State of the chat on screen, shared between ChatViewModel and the agent backend that owns the active conversation.
 */
class ChatSessionStore {
    val conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val currentConversation = MutableStateFlow<Conversation?>(null)
    val messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val conversationError = MutableStateFlow<String?>(null)
    val userEmail = MutableStateFlow<String?>(null)
    val attachments = MutableStateFlow<List<ChatAttachment>>(emptyList())
    val terminatedToolDialog = MutableStateFlow<Pair<ToolCall, String>?>(null)
}
