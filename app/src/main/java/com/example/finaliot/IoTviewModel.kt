package com.example.finaliot

import android.app.Application
import androidx.lifecycle.*

class IoTViewModel (application: Application) : AndroidViewModel(application){

    val TAG = "DEBUG_iot"

    var estado = MutableLiveData<String>("ON")

    var pusherIoT = PusherWS(this.viewModelScope,estado)


    fun nuevoEstado(nuevoEstado: String){


        if(nuevoEstado=="ON"){
            estado.value = "OFF"
            pusherIoT.cambiarEstado("OFF")

        }else{
            estado.value = "ON"
            pusherIoT.cambiarEstado("ON")

        }
    }



}