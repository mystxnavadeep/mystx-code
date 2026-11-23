package com.mystxcode.app.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mystxcode.app.data.receiver.TermuxOpenReceiver as CoreOpenReceiver

/**
 * Direct com.mystxcode.app.app.TermuxOpenReceiver implementation for shell scripts
 * (e.g., xdg-open, termux-open) targeting explicit component com.mystxcode.app.app.TermuxOpenReceiver.
 */
class TermuxOpenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return
        CoreOpenReceiver.handleOpen(context, intent)
    }
}
