package com.gaurav.dateextension

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
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
import com.gaurav.dateextension.circularCarousel.CircularCarousel
import com.gaurav.dateextension.circularCarousel.MovieData
import com.gaurav.dateextension.ui.theme.DateExtensionTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

class MainActivity : ComponentActivity() {
    /** Messenger for communicating with the service.  */
    private var mService: Messenger? = null

    /** Flag indicating whether we have called bind on the service.  */
    private var mBound: Boolean = false
    /** Defines callbacks for service binding, passed to bindService().  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            mService = Messenger(service)
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mService = null
            mBound = false
        }
    }

    val data:ArrayList<MovieData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repeat(10) {it->
            data.add(MovieData(it, "$it"))
        }
       // enableEdgeToEdge()
        setContent {
            DateExtensionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    var item:MutableState<Int> = rememberSaveable {    mutableStateOf(1)}
//                    val state = SnackbarHostState()
//                   // NamePicker(state, Modifier.padding(innerPadding))
//                   // CompositionLocalExample(Modifier.padding(innerPadding))
//                    ContactRow(Contact("Gaurav","8546082536"),
//                        modifier = Modifier.padding(innerPadding)
//                    ) { onButtonClick() }
                    CircularCarousel(data, Modifier.padding(innerPadding))

                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        mBound = false
    }
    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute).  */
    fun onButtonClick() {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call is something that might hang, then put this request
            // in a separate thread to avoid slowing down the activity performance.
            // Create and send a message to the service, using a supported 'what' value.
            val msg: Message = Message.obtain(null, 1, 0, 0).apply {
                data.putString("EXTRA_DATA" ,"This is Gaurav")
            }
            try {

                mService?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        // Bind to LocalService.
        Intent(this, ServiceTesting::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DateExtensionTheme {
        Greeting("Android")
    }
}


/**
 * Display a list of names the user can click with a header
 */
@Composable
fun NamePicker(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier
) {
    // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
    val scope = rememberCoroutineScope()

    println("Recomposing NamePicker .....")
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            Button(
                onClick = {
                    // Create a new coroutine in the event handler to show a snackbar
                    scope.launch {
                        snackbarHostState.showSnackbar("Something happened!")
                    }
                }
            ) {
                Text("Press me")
            }
            repeat(5){
                NamePickerItem("Android $it",{})
            }
            //Testing()

        }
    }
}

/**
 * Display a single name the user can click.
 */
@Composable
private fun NamePickerItem(name: String,
                           onClicked: (String) -> Unit) {
    val item :MutableState<Int> = remember { mutableStateOf(1) }
    println("Recomposing NamePickerItem .....")
    Text("$name ${item.value}", Modifier.clickable(onClick = {
        onClicked(name)
        item.value =item.value+1
        println("====${item.value}")
    }))

    DisposableEffect(item) {
        println("====DisposableEffect started ")
        onDispose {
            println("====onDispose called ")
        }
    }
    LaunchedEffect(item.value) {
        repeat(5) {
            delay(1000)
            println("====LaunchedEffect called $it")
        }
    }

    SideEffect {

        println("====SideEffect called ")

    }

    if(TestReturnState().value != 0) {
        Text("${TestReturnState().value}")
    }
}
@Composable
fun Testing():Composable {
     Text("hello")
    return Composable().apply {
        Text("hello 1")
    }
}
@Composable
fun TestReturnState():State<Int> {
    return produceState(1) {
        println("Produced state called ===== ")
        delay(3000)
        value = 100
    }
}

@Composable
fun CompositionLocalExample(padding: Modifier) {
    MaterialTheme {
        // Surface provides contentColorFor(MaterialTheme.colorScheme.surface) by default
        // This is to automatically make text and other content contrast to the background
        // correctly.
        Surface {
            Column {
                Text("Uses Surface's provided content color")
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                    Text("Primary color provided by LocalContentColor")
                    Text("This Text also uses primary as textColor")
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.error) {
                        DescendantExample()
                    }
                }
            }
        }
    }
}

@Composable
fun DescendantExample() {
    // CompositionLocalProviders also work across composable functions
    Text("This Text uses the error color now")
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

@Composable
fun ContactRow(contact: Contact, modifier: Modifier = Modifier, onButtonClick: ()->Unit) {
    println("Recomposing ContactRow .....")
    var selected by remember { mutableStateOf(false) }

    Row(modifier) {
        ContactDetails(contact)
        Switch(selected, onCheckedChange = {
            selected = !selected
            onButtonClick.invoke()
        })
    }
    var pulseRateMs by remember { mutableStateOf(3000L) }
    val alpha = remember { Animatable(1f) }
    LaunchedEffect(pulseRateMs) {
        while (isActive) {
            println("Launched effect called .....")
            delay(pulseRateMs) // Pulse the alpha every pulseRateMs to alert the user
            alpha.animateTo(0f)
            alpha.animateTo(1f)
        }

    }
    SideEffect {
        println("Side effect called .....")
    }
    DisposableEffect(Unit) {

        println("Dispose effect called .....")
        onDispose {
            println("Dispose effect ondispose called .....")
        }
    }
}

@Composable
fun ContactDetails(contact: Contact) {
    println("Recomposing ContactDetails .....")
    val context  = LocalContext.current
    val intent = Intent(context, ServiceTesting::class.java)
    Column {
        Text(contact.name, modifier = Modifier
            .clickable {
                context.startService(intent)
            })
        Text(contact.number)
    }
}


data class Contact(val name: String, val number: String, val list:MutableState<String> = mutableStateOf("") )