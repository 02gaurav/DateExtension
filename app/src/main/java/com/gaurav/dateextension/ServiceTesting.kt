package com.gaurav.dateextension

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.Random
import kotlin.math.absoluteValue


/** Command to the service to display a message.  */
private const val MSG_SAY_HELLO = 1

class ServiceTesting : Service() {

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private lateinit var mMessenger: Messenger

    /**
     * Handler of incoming messages from clients.
     */
    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_SAY_HELLO ->
                    Toast.makeText(applicationContext, "hello! ${msg.data.getString("EXTRA_DATA")}", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    private val TAG = "ServiceTesting"
    // Binder given to clients.
    private val binder = LocalBinder()

    // Random number generator.
    private val mGenerator = Random()

    /** Method for clients.  */
    // client can call this random Number fun to get random number
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): ServiceTesting = this@ServiceTesting
    }
    // Handler that receives messages from the thread
    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder {
       // return binder
        Toast.makeText(applicationContext, "binding", Toast.LENGTH_SHORT).show()
        mMessenger = Messenger(IncomingHandler(this))
        return mMessenger.binder
    }

    override fun onCreate() {
        super.onCreate()
        println("Service Created........")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Service Destroyed.........")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        println("Service Started.........")

        val th = Thread(){
            while (true){
                println("Service Running.......Random number..${randomNumber.absoluteValue}")
                Thread.sleep(1000)
            }
        }
        th.start()
        return Service.START_STICKY
    }
}