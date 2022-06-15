package com.example.finaliot

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class PusherWS(viewModelScope: CoroutineScope, estado: MutableLiveData<String>){

    val TAG = "DEBUG_iot"

    private val PUSHER_APP_KEY = "pusher_server_key"
    private val PUSHER_APP_SECRET = "android_client_secret"
    private val PUSHER_APP_ID = "android_client"
    private val PUSHER_APP_CLUSTER = "mt1"
    private val PUSHER_APP_HOST = "157.90.22.135"
    private val PUSHER_APP_PORT = 6001
    private val PUSHER_AUTH_URL = "http://157.90.22.135/inmoticaserver/public/broadcasting/auth"



    //val modelos:IoTViewModel by ViewModels()


    var privateChannel: PrivateChannel
    var authorizer: HttpAuthorizer
    var options: PusherOptions
    var pusher: Pusher


    init {


        authorizer = HttpAuthorizer(PUSHER_AUTH_URL)


        authorizer.setHeaders(getMapAuthorizationHeaders())

        options = PusherOptions().setCluster(PUSHER_APP_CLUSTER)
            .setHost(PUSHER_APP_HOST)
            .setWsPort(PUSHER_APP_PORT).setEncrypted(false)
            .setAuthorizer(authorizer)

        pusher = Pusher(PUSHER_APP_KEY, options)

        if (pusher == null) {
            Log.d(TAG, "VACIO DE MIERDA")
        } else {
            Log.d(TAG, "LLENO DE MIERDA")
        }



        pusher!!.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.d(
                    TAG,
                    "onConnectionStateChange: " + change.currentState +
                            " from " + change.previousState
                )
            }


            override fun onError(message: String, code: String, e: Exception) {
                Log.d(
                    TAG,
                    "onError: " + e.message + "mensaje: " + message + "codigo:" + code
                )
            }
        }, ConnectionState.ALL)

        privateChannel = pusher!!.subscribePrivate("private-iot-channel")




        privateChannel.bind("client-sendToIoT", object: PrivateChannelEventListener{
            override fun onEvent(event: PusherEvent?) {
                Log.i("Pusher", "Received event with data: " + event.toString())
                var map: HashMap<String, String>? = null
                try {
                    map = jacksonObjectMapper().readValue(
                        event?.getProperty("data").toString(), HashMap::class.java
                    ) as HashMap<String, String>? //casting


                    Log.d(TAG, "ENTERO 2 $map")
                } catch (e: JsonProcessingException) {
                    e.printStackTrace()
                }


                if (map!!["action"] == "ON") {
                    Log.d(
                        TAG,
                        "ESTADO DISPOSITIVO: " + map!!["action"]
                    )
                    viewModelScope.launch {
                        estado.value = "ON"
                    }
                } else {

                    viewModelScope.launch {
                        estado.value = "OFF"
                    }
                }

            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                TODO("Not yet implemented")
            }

            override fun onAuthenticationFailure(message: String?, e: java.lang.Exception?) {
                TODO("Not yet implemented")
            }


        })
    }




    fun cambiarEstado(estado: String) {

        if (privateChannel?.isSubscribed() == true) {
            privateChannel!!.trigger(
                "client-sendToIoT",
                "{\"action\":\"$estado\"}"
            )
        }else{
            Log.d(TAG,"TREMENDO MIERDON LECHERO")
        }

    }


    fun getMapAuthorizationHeaders(): java.util.HashMap<String, String>? {
        return try {
            val authHeader = java.util.HashMap<String, String>()
            authHeader["Authorization"] = "Bearer 4|XIMrvUrpLNW1ogLQCklCaNfgH70AIbYKTIUHtGff"
            authHeader
        } catch (e: java.lang.Exception) {
            null
        }


    }

}


