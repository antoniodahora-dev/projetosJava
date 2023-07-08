package co.a3tecnology.testapp

import java.text.SimpleDateFormat
import java.util.*

object DateUtis {

    private const val HOUR_FORMAT = "HH:mm"

    private val currentHour: String

    get() {
        val calendar = Calendar.getInstance()
        val sdfHour = SimpleDateFormat(HOUR_FORMAT, Locale("pt", "BR"))
        return sdfHour.format(calendar.time)
    }

  fun isHourInterval(target: String, start: String, end: String) = (target in start..end)
  fun isNowInInterval(start: String, end: String) = isHourInterval(currentHour, start, end)

}