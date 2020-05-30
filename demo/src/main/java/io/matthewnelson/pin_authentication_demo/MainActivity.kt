package io.matthewnelson.pin_authentication_demo

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.Px
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.matthewnelson.pin_authentication.service.PinAuthentication
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    companion object {
        @StyleRes private var currentTheme = R.style.OnApplicationStartTheme
        fun setCurrentTheme(@StyleRes theme: Int) {
            currentTheme = theme
        }

        @Px private var bottomNavBarHeightPx = 0
        fun getBottomNavBarHeightPx(): Int =
            bottomNavBarHeightPx

        private fun setBottomNavBarHeightPx(navView: BottomNavigationView) {
            if (navView.height > 0 && navView.height != bottomNavBarHeightPx) {
                bottomNavBarHeightPx = navView.height
            }
        }
    }

    private val paController by lazy { PinAuthentication.Controller() }
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)
        navView = findViewById(R.id.nav_view)
        setBottomNavBarHeightPx(navView)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_controller,
                R.id.navigation_settings
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        observeInitialAppLogin()

    }

    private fun observeInitialAppLogin() {
        paController.hasInitialAppLoginBeenSatisfied().observe(this,  Observer<Boolean> {
            if (it) {

                // Update our currentTheme and recreate
                if (currentTheme == R.style.OnApplicationStartTheme) {

                    // Could also load user selected themes from SharedPrefs here

                    currentTheme = R.style.PostInitialLoginTheme
                    recreate()
                } else {

                    executePostLoginProcesses()

                }
            } else { // User has yet to either authenticate, or complete the on board process

                doOnBoard()

                // Hide the support action bar and bottom nav bar so we get a clean looking
                // transition on app startup.
                supportActionBar?.hide()
                navView.visibility = View.GONE
            }
        })
    }

    private fun executePostLoginProcesses() {
        if (!paController.hasPostLoginProcessBeenStarted()) {
            paController.postLoginProcessStarted()

            // if the on-board process was started, clear the backstack
            if (navController.currentDestination?.id != R.id.navigation_controller) {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.mobile_navigation, true)
                    .build()
                navController.navigate(R.id.navigation_controller, Bundle(), navOptions)
            }

            // When initial login is satisfied make the support action bar
            // and bottom nav bar visible again.
            supportActionBar?.show()
            navView.visibility = View.VISIBLE
        }
    }

    private fun doOnBoard() {
        if (!paController.hasOnBoardProcessBeenSatisfied()) {

            if (navController.currentDestination?.id != R.id.navigation_on_board) {
                navController.navigate(R.id.navigation_on_board)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear_data_restart -> {
                // Build notification to restart the Application
                val intent = Intent(this, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

                val channelId = "${this.packageName}.restart_application"
                val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val name = "PinAuthentication Notification Channel"
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val description = "Channel to restart the application after clearing data."

                    val channel = NotificationChannel(channelId, name, importance)
                    channel.description = description

                    notificationManager.createNotificationChannel(channel)
                }

                val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Restart PinAuthenticationDemo")
                    .setContentText("Click to launch")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                // Clear shared prefs and fire notification
                paController.clearPinAuthenticationData()
                notificationManager.notify(9999, notificationBuilder.build())

                exitProcess(0)
            }
            R.id.menu_see_website -> {
                val gitHubPagesURL = "https://05nelsonm.github.io/pin-authentication/"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(gitHubPagesURL)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.navigation_on_board) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(currentTheme, true)
        return theme
    }
}
