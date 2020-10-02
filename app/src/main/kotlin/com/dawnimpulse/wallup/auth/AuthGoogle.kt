package com.dawnimpulse.wallup.auth

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.network.controller.CtrlBookmark
import com.dawnimpulse.wallup.utils.handlers.HandlerDialog
import com.dawnimpulse.wallup.utils.reusables.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.launch

class AuthGoogle : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth
    private var cancel = false
    private var signIn = false
    private val reqOneTap = 101

    /**
     * on create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        HandlerDialog.loading(this)
        oneTapInit()
        if (intent.extras!!.getBoolean(AUTH, true)) login()
        else signOut()
    }

    /**
     * on destroy
     */
    override fun onDestroy() {
        super.onDestroy()
        HandlerDialog.dismiss()
    }

    /**
     * activity result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            reqOneTap -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            logd("Got ID token.")
                            firebaseAuth(idToken)
                        }
                        else -> {
                            StyleToast.error("Issue while login (${ERROR.AUTH.GOOGLE.USER_2}), please try again", Toast.LENGTH_LONG)
                            loge("No ID token or password!")
                            finish()
                        }
                    }
                } catch (e: ApiException) {
                    if (e.statusCode == CommonStatusCodes.CANCELED) {
                        // if sign in is previously called (& this is signup)
                        // then close the prompt
                        if (signIn) {
                            StyleToast.error("Issue while login (${ERROR.AUTH.GOOGLE.USER}), please try again", Toast.LENGTH_LONG)
                            e.printStackTrace()
                            finish()
                            // else if show signup screen
                        } else {
                            signIn = true
                            signUp()
                        }
                    } else {
                        StyleToast.error("Issue while login (${ERROR.AUTH.GOOGLE.USER}), please try again", Toast.LENGTH_LONG)
                        e.printStackTrace()
                        finish()
                    }
                }
            }
        }
    }

    /**
     * back press
     */
    override fun onBackPressed() {
        if (cancel) finish()
        else
            StyleToast.info("Kindly back press again to cancel")
        cancel = true
    }

    /**
     * init one tap login
     */
    private fun oneTapInit() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.google_web_client_id))
                                .setFilterByAuthorizedAccounts(true)
                                .build()
                )
                .build()
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.google_web_client_id))
                                .setFilterByAuthorizedAccounts(false)
                                .build())
                .build()
    }

    /**
     * one tap login
     */
    private fun login() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                                result.pendingIntent.intentSender, reqOneTap,
                                null, 0, 0, 0, null
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        // error
                        StyleToast.error("Issue while login (${ERROR.AUTH.GOOGLE.LOGIN}), please try again", Toast.LENGTH_LONG)
                        e.printStackTrace()
                        finish()
                    }
                }
                .addOnFailureListener(this) { e ->
                    signUp()
                }
    }

    /**
     * sign up
     */
    private fun signUp() {
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                                result.pendingIntent.intentSender, reqOneTap,
                                null, 0, 0, 0)
                    } catch (e: IntentSender.SendIntentException) {
                        StyleToast.error("Unable to login (${ERROR.AUTH.GOOGLE.SIGNUP}), please try again", Toast.LENGTH_LONG)
                        e.printStackTrace()
                        finish()
                    }
                }
                .addOnFailureListener(this) { e ->
                    StyleToast.error("Issue while login (${ERROR.AUTH.GOOGLE.SIGNUP_2}), please try again", Toast.LENGTH_LONG)
                    e.printStackTrace()
                    finish()
                }
    }

    /**
     * firebase signup
     *
     * @param token
     */
    private fun firebaseAuth(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        logd("signInWithCredential:success")
                        fetchAllBookmarks()
                    } else {
                        // If sign in fails, display a message to the user.
                        StyleToast.error("Issue while login (${ERROR.AUTH.GOOGLE.FIREBASE}), please try again", Toast.LENGTH_LONG)
                        task.exception?.printStackTrace()
                        finish()
                    }
                }
    }

    /**
     * sign out
     */
    private fun signOut() {
        oneTapClient.signOut()
        auth.signOut()
        StyleToast.success("Successfully logged out", Toast.LENGTH_LONG)
        Hawk.deleteAll()
        finish()
    }

    /**
     * fetch all bookmarks
     */
    private fun fetchAllBookmarks() {
        Hawk.deleteAll() // just to make sure internal db is clear
        lifecycleScope.launch {
            try {
                val bookmarks = CtrlBookmark.latest(0, -1)
                if (bookmarks.isNotEmpty())
                    bookmarks.forEach { Hawk.put(it.image.iid, it._id) }
                StyleToast.success("Welcome ${FirebaseAuth.getInstance().currentUser!!.displayName}", Toast.LENGTH_LONG)
                finish()
            } catch (e: Exception) {
                StyleToast.error("Issue while login (${ERROR.AUTH.WALLUP.BOOKMARKS}), please try again", Toast.LENGTH_LONG)
                signOut()
                e.printStackTrace()
                finish()
            }
        }
    }
}