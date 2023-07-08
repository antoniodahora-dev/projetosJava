package co.a3tecnology.testapp

import android.app.Notification
import androidx.core.app.NotificationCompat

object NotificationUtils {

    fun getQuickReplyAction(n: Notification) : Action? {
        val action = findQuickReplyAction(n)
        if (action != null)
        return Action(action)
        return null
    }

    private fun findQuickReplyAction(n: Notification) : NotificationCompat.Action? {
        for (i in 0 until NotificationCompat.getActionCount(n)) {
            val action = NotificationCompat.getAction(n, i)

            if (action?.remoteInputs != null) {
                for (remote in action.remoteInputs!! ) {
                    val resultKey = remote.resultKey
                    if (resultKey.isNotEmpty()
                        && resultKey.toLowerCase().contains("reply"))
                    return action
                }
            }
        }

        return null
    }

}