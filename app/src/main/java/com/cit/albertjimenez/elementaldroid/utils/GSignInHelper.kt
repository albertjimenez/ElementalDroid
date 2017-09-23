package com.cit.albertjimenez.elementaldroid.utils

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient





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

