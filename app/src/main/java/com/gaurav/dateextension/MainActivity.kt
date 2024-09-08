package com.gaurav.dateextension

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gaurav.date_extensions.changeToDateFormat
import com.gaurav.date_extensions.getDayWithSuffix
import com.gaurav.date_extensions.getHourMinFormatString
import com.gaurav.date_extensions.getTime
import com.gaurav.date_extensions.getTimeInString
import com.gaurav.date_extensions.isDateLastMinuteOrBefore
import com.gaurav.date_extensions.isDateThisWeek
import com.gaurav.date_extensions.isDateToday
import com.gaurav.date_extensions.isDateYesterday
import com.gaurav.date_extensions.isDateYesterdayOrBefore
import com.gaurav.date_extensions.isMoreThanSevenDays
import com.gaurav.date_extensions.parseRemainingTimeDifference
import com.gaurav.date_extensions.parseTimeDifference
import com.gaurav.date_extensions.parseTimeTo_dd_MMM
import com.gaurav.date_extensions.timeDayOfWeek
import com.gaurav.dateextension.ui.theme.DateExtensionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DateExtensionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val currentTime = System.currentTimeMillis() - 1000*60*24
    val currentTime1 = System.currentTimeMillis()
    val currentTime2 = System.currentTimeMillis()- 1000*60*24*12
    val currentTime3 = System.currentTimeMillis()- 1000*60*60*24*8
    LazyColumn {
        item {
            Text(
                text = "Date format ${
                    (1694157381L).changeToDateFormat(
                        true,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                }",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "Time diff ${(currentTime1-currentTime).parseTimeDifference()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "Rem time diff ${(currentTime1-currentTime2).parseRemainingTimeDifference()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "Is today ${currentTime1.isDateToday()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "Is Yesterday ${currentTime2.isDateYesterday()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "Get Time ${currentTime1.getTime()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "Yesterday or before ${currentTime1.isDateYesterdayOrBefore()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "Last min or before ${currentTime1.isDateLastMinuteOrBefore()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "parseTimeTo_dd_MMM = ${(1694157381L).parseTimeTo_dd_MMM(true)}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "timeDayOfWeek${currentTime1.timeDayOfWeek()}",
                modifier = modifier
            )
        }
        item {

            Text(
                text = "isMoreThanSevenDays ${currentTime3.isMoreThanSevenDays()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "isDateThisWeek ${currentTime2.isDateThisWeek()}",
                modifier = modifier
            )
        }
        item {
            Text(
                text = "getTimeInString ${(127L).getTimeInString()}",
                modifier = modifier
            )
        }
        item {

            Text(
                text = "getHourMinFormatString ${currentTime1.getHourMinFormatString()}",
                modifier = modifier
            )
        }
        item {

            Text(
                text = "getDayWithSuffix ${currentTime3.getDayWithSuffix()}",
                modifier = modifier
            )
        }

        //parseTimeTo_dd_MMM
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DateExtensionTheme {
        Greeting("Android")
    }
}