package com.example.temperaturetracking.util

import android.util.Log
import com.example.temperaturetracking.data.entity.Member
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FunctionalHelper {
    companion object {
        fun formatTimeFromMillis(timeStamp: String): String {
            if(timeStamp.isNotEmpty()) {
                val date = Date(timeStamp.toLong())
                val language = "en"
                val formattedDateAsShortMonth = SimpleDateFormat("dd/MM/yyyy", Locale(language))
                return formattedDateAsShortMonth.format(date)
            }
            return ""
        }

        fun getMilliSecondFromDate(dateInString: String): Long? {
            if(dateInString.isNotEmpty()) {
                val language = "en"
                val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale(language))
                val date = simpleDateFormat.parse(dateInString)
                return date.time
            }
            return null
        }

        fun getHourFromMillis(timeStamp: String): String {
            val date = Date(timeStamp.toFloat().toLong())
            val cal = Calendar.getInstance()
            cal.time = date
            val hour = cal[Calendar.HOUR_OF_DAY]
            val min = cal[Calendar.MINUTE]
            var time = "$hour"
            if(hour < 10) {
                time = "0$hour"
            }
            if(min < 10) {
                return "$time:0$min"
            }
            return "$time:$min"
        }

        fun getMemberInfo(): MutableList<Member> {
            return mutableListOf(
                Member("Minh Chó", "https://scontent-sin6-2.xx.fbcdn.net/v/t1.0-9/106350725_1775530575920767_7803104528657020398_o.jpg?_nc_cat=102&ccb=2&_nc_sid=09cbfe&_nc_ohc=06qjpEfBA44AX8WUTw1&_nc_ht=scontent-sin6-2.xx&oh=741dac5caf5c0db3dc3cca5f46a1c405&oe=5FDFF06A"),
                Member("Phạm Hồng Phúc", "https://scontent-sin6-2.xx.fbcdn.net/v/t1.0-9/121162938_2610316552613834_1897311959270879413_o.jpg?_nc_cat=103&ccb=2&_nc_sid=09cbfe&_nc_ohc=r7zWmhm4Hu4AX9-ZAH7&_nc_ht=scontent-sin6-2.xx&oh=030cc2825df3322330cf1b17d61bca9d&oe=5FDE4ADF"),
                Member("Võ Thị Ngọc Phương", "https://scontent-sin6-2.xx.fbcdn.net/v/t1.0-9/73001134_2400661200234161_7222675439211478194_o.jpg?_nc_cat=102&ccb=2&_nc_sid=174925&_nc_ohc=Sq99i-sfQn8AX8bSARz&_nc_ht=scontent-sin6-2.xx&oh=b250e10a453c39ffc63c5425344fb24d&oe=5FDEEFBD"),
                Member("Thái Quốc Toàn", "https://scontent-sin6-2.xx.fbcdn.net/v/t1.0-9/123414979_1447604422100573_4276986606296824582_o.jpg?_nc_cat=109&ccb=2&_nc_sid=09cbfe&_nc_ohc=6_YS5cJdnX8AX94XCeA&_nc_ht=scontent-sin6-2.xx&oh=9a7a1f7b6d37c87deeba270a97f325c8&oe=5FE0AC23")
            )
        }
    }
}