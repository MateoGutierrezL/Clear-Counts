package com.example.clearcounts.ui.screens.PreguntasComentarios

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.clearcounts.ui.screens.InicioSesion.LoginError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.security.auth.Subject

@HiltViewModel
class ViewModelPreguntasComentarios @Inject constructor(

    @ApplicationContext private val context: Context
): ViewModel(){

    fun SendEmail(context: Context, recipient: String, subject: String, body: String){

        val uriString = "mailto:$recipient" + "?subject=${Uri.encode(subject)}" + "&body=${Uri.encode(body)}"

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(uriString)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        try {
            context.startActivity(intent)
        } catch(e: ActivityNotFoundException){
            Toast.makeText(context, "No tienes una aplicación de correo instalada", Toast.LENGTH_SHORT).show()
        }catch(e: Exception){
            Log.e("email", "Error al abrir gmail")
        }
    }
}