package co.a3tecnology.testapp

import android.app.PendingIntent.CanceledException
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.AsyncTask
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import java.lang.ref.WeakReference
import java.util.*


class NotifierListener : NotificationListenerService() {


    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val notification = sbn?.notification
        val extras = notification?.extras

        println(extras)

        extras?.let {
            this@NotifierListener.cancelNotification(sbn.key)
            ReplyTask(
                WeakReference(applicationContext),
                WeakReference(it),
                WeakReference(sbn)
            ).execute()
        }
    }

    companion object {

        private class ReplyTask(
            private val applicationContext: WeakReference<Context>,
            private val extras: WeakReference<Bundle>,
            private val sbn: WeakReference<StatusBarNotification>
        ) : AsyncTask<Unit, Unit, Unit>() {

            override fun doInBackground(vararg params: Unit?) {
                val extras = extras.get()
                val sbn = sbn.get()
                if (extras != null && sbn != null) {
                    println("start sleep: ${Thread.currentThread().id}")
                    Thread.sleep(15 * 1000) // 15sec

                    println("wake up: ${Thread.currentThread().id}")
                    replyMessage(extras, sbn)
                }
            }

            private fun replyMessage(it: Bundle, sbn: StatusBarNotification) {
                val now = Calendar.getInstance()
                val start = "09:00"
                val end = "18:00"

                var processName =
                    (it["android.rebuild.applicationInfo"] as? ApplicationInfo)?.processName
                if (processName == null)
                    processName = (it["android.appInfo"] as? ApplicationInfo)?.processName

                if (processName == "com.whatsapp"
                    && it.getString("android.bigText") == null
                    && it.getString("android.summaryText") == null
                    && (now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                            || now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                            || !DateUtis.isNowInInterval(start, end))
                ) {
                    val action: Action? =
                        NotificationUtils.getQuickReplyAction(sbn.notification)

                    val sharedPreferences = applicationContext.get()!!.getSharedPreferences(
                        "main",
                        Context.MODE_PRIVATE
                    )
                    val previousTime = sharedPreferences.getLong("current_time", 0L)

                    if (action != null &&
                        (previousTime == 0L) || (System.currentTimeMillis() - previousTime) > 4 * 60 * 1000 // 4min -
                    ) {
                        println("action ok | previousTime: $previousTime, then send: ${Thread.currentThread().id}")

                        val editor = sharedPreferences?.edit()
                        editor?.putLong("current_time", System.currentTimeMillis())
                        editor?.apply()

                        try {
                            action?.sendReply(
                                applicationContext.get()!!,
                                //"Opa! Atendemos em dias úteis das 9-18h. Deixe a sua dúvida e assim que possível, entraremos em contato \uD83D\uDE09"
                                "Oi. Nosso atendimento funciona das 9-18h e em dias úteis. Deixe a sua mensagem e assim que possível, entraremos em contato \uD83D\uDE09"
                            )
                        } catch (e: CanceledException) {
                            e.printStackTrace()
                        }
                    } else {
                        println("not action $action | previousTime: $previousTime")
                    }
                } else {
                    println("Não enviar dados agora")
                }
            }
        }
    }


    override fun onListenerConnected() {
        super.onListenerConnected()

        println("Conected")
    }

}