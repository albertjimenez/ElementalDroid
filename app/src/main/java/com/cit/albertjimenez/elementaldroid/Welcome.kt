package com.cit.albertjimenez.elementaldroid

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.dao.RegularUser
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerFB
import com.cit.albertjimenez.elementaldroid.utils.auth
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_welcome.*


class Welcome : AppCompatActivity() {

    private val RC_SIGN_IN = 9001
    private var mAuth: FirebaseAuth? = null
    private val dataManagerFB: DataManagerFB = DataManagerFB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mAuth = FirebaseAuth.getInstance()

        // Lambda for avoid repeating code
        val animation = { AnimationUtils.loadAnimation(applicationContext, R.anim.bounce) }
        logo.startAnimation(animation.invoke())
        logo.setOnClickListener { animation.invoke() }
        val mGoogleApiClient = auth(context = this, fragment = FragmentActivity(),
                token = getString(R.string.token))
        sign_in_button.setOnClickListener { signIn(mGoogleApiClient = mGoogleApiClient) }
    }

    private fun signIn(mGoogleApiClient: GoogleApiClient) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {


        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth?.currentUser
                        Toast.makeText(this, "Welcome " + user?.displayName, Toast.LENGTH_SHORT).show()
                        dataManagerFB.storeNewUser(RegularUser(user!!.displayName!!,
                                user.email!!, ArrayList<Element>()))
                    } else
                        Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()

                }
    }

}
