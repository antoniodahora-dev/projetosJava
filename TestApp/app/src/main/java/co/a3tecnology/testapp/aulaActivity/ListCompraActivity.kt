package co.a3tecnology.testapp.aulaActivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import co.a3tecnology.testapp.R
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class ListCompraActivity : AppCompatActivity() {

    private val tickReceiver by lazy {makeBroadcastReceiver()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_compra)

//        date()
    }

//    private fun date() {
//        val simpleDateFormat =
//            SimpleDateFormat(
//                "dd-MM-yyyy HH:mm",
//                Locale("pt")
//            )
//        val now = Date()
//        val date_time = findViewById<TextView>(R.id.txt_date_time)
//
//        date_time.text = simpleDateFormat.format(now)
//    }

    override fun onResume() {
        super.onResume()
        registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        super.onPause()

        try {
            unregisterReceiver(tickReceiver)
        } catch (e: IllegalArgumentException) {
            println("Time tick Receixer not registered")
        }
    }

    private fun makeBroadcastReceiver() : BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_TIME_TICK) {
                    val simpleDateFormat =
                        SimpleDateFormat("dd-MM-yyyy HH:mm",
                        Locale("pt"))
                    val now = Date()
                    val date_time = findViewById<TextView>(R.id.txt_date_time)

                    date_time.text = simpleDateFormat.format(now)
                }
            }

        }
    }

}