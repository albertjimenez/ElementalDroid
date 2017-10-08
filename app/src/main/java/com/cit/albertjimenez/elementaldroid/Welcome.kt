package com.cit.albertjimenez.elementaldroid

import android.content.Intent
import android.net.http.HttpResponseCache
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import com.cit.albertjimenez.elementaldroid.dao.RegularUser
import com.cit.albertjimenez.elementaldroid.dao.TeacherUser
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerJ
import com.cit.albertjimenez.elementaldroid.utils.auth
import com.cit.albertjimenez.elementaldroid.utils.random
import com.cit.albertjimenez.elementaldroid.utils.toastMakeIt
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*


class Welcome : AppCompatActivity() {

    private val RC_SIGN_IN = 9001
    private var mAuth: FirebaseAuth? = null
    private val dataManagerFB = DataManagerJ.getInstance()

    companion object {
        lateinit var mGoogleApiClient: GoogleApiClient
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        dataManagerFB.initFB()
        mAuth = FirebaseAuth.getInstance()
        HttpResponseCache.install(cacheDir, 100000L)

        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.move))

        // Lambda for avoid repeating code
        logo.setOnClickListener { logoInteraction() }
        mGoogleApiClient = auth(context = this, fragment = FragmentActivity(),
                token = getString(R.string.token))
        sign_in_button.setOnClickListener { signIn(mGoogleApiClient = mGoogleApiClient) }
        progressBar.visibility = View.GONE


    }

    private fun logoInteraction() {
        val animationList: ArrayList<Int> = arrayListOf(R.anim.blink, R.anim.bounce,
                R.anim.fade_in, R.anim.fade_out, R.anim.move, R.anim.rotate, R.anim.slide_down,
                R.anim.slide_up, R.anim.zoom_in, R.anim.zoom_out)
        logo.startAnimation(AnimationUtils.loadAnimation(this, animationList[(0..animationList.size).random()]))

    }

    //One line function for generate random number from a Range (Extension)


    private fun signIn(mGoogleApiClient: GoogleApiClient) {
        progressBar.visibility = View.VISIBLE
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
        } else progressBar.visibility = View.GONE
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        progressBar.visibility = View.GONE
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth?.currentUser
                        val regularUser = RegularUser(user!!.displayName!!, user.email!!)
                        dataManagerFB.storeNewUser(regularUser)
                        val myIntent = with(Intent(this, ListElements::class.java)) {
                            putExtra("PROFILEPHOTO", user.photoUrl?.toString())
                            putExtra("PROFILEUSERNAME", user.displayName)
                            putExtra("PROFILEEMAIL", user.email)
                        }
                        if (dataManagerFB.isTeacher(TeacherUser(user.displayName!!, user.email!!)))
                            startActivity(Intent(this, TeacherActivity::class.java))
                        else
                            startActivity(Intent(myIntent))
                    } else
                        toastMakeIt(applicationContext, "Authentication failed.")

                }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

}