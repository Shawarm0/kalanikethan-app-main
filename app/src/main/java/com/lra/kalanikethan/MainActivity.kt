package com.lra.kalanikethan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lra.kalanikethan.ui.theme.KalanikethanTheme

/**
 * The main entry point of the app
 *
 * Sets up the navigation graph and handles initial app configurations
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is first created.
     * This is where you should perform initial setup such as inflating layouts,
     * initializing components, and setting up Compose content or navigation.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     * being shut down, this contains the most recent state; otherwise, it is `null`.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalanikethanTheme {

            }
        }
    }

    /**
     * Called when the activity is becoming visible to the user.
     * This is followed by [onResume] if the activity comes to the foreground.
     */
    override fun onStart() {
        super.onStart()
        println("MainActivity: onStart() called")
    }

    /**
     * Called when the activity starts interacting with the user.
     * This is a good place to start animations or resume resources.
     */
    override fun onResume() {
        super.onResume()
        println("MainActivity: onResume() called")
    }

    /**
     * Called when the system is about to put the activity into the background.
     * Use this to pause animations or save UI-related data.
     */
    override fun onPause() {
        super.onPause()
        println("MainActivity: onPause() called")
    }

    /**
     * Called when the activity is no longer visible to the user.
     * Stop any processes that shouldn't run in the background here.
     */
    override fun onStop() {
        super.onStop()
        println("MainActivity: onStop() called")
    }

    /**
     * Called before the activity is destroyed.
     * Use this for final cleanup like releasing resources.
     */
    override fun onDestroy() {
        super.onDestroy()
        println("MainActivity: onDestroy() called")
    }

    /**
     * Called after the activity has been stopped and is restarting again.
     * This is followed by [onStart].
     */
    override fun onRestart() {
        super.onRestart()
        println("MainActivity: onRestart() called")
    }
}
