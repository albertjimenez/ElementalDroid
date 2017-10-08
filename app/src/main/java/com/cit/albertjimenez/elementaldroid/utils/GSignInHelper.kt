package com.cit.albertjimenez.elementaldroid.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.cit.albertjimenez.elementaldroid.Welcome
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth


/**
 * Created by Beruto on 23/9/17.
 */
fun auth(context: Context, fragment: FragmentActivity, token: String): GoogleApiClient {
    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
            requestIdToken(token).requestEmail().build()
    var mGoogleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage( fragment/* FragmentActivity */,
                    {  } /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    return mGoogleApiClient

}

fun logOutGoogle(context: Context, classDestination: Class<*>) {
    Welcome.mGoogleApiClient.connect()
    Welcome.mGoogleApiClient.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
        override fun onConnected(p0: Bundle?) {
            FirebaseAuth.getInstance().signOut()
            Auth.GoogleSignInApi.signOut(Welcome.mGoogleApiClient).
                    setResultCallback {
                        context.startActivity(Intent(context, classDestination))
                    }
        }

        override fun onConnectionSuspended(p0: Int) {
            Log.d("GAuth", "Suspended log")
        }
    })
}

