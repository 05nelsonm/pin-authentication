package io.matthewnelson.pin_authentication_demo

import android.app.Application
import android.app.Service
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.pin_authentication_demo.util.dpToPx
import io.matthewnelson.pin_authentication.service.PinAuthentication

class App: Application() {

    companion object {
        private lateinit var appContext: Context
        private lateinit var previousToast: Toast

        /**
         * To display a Toast w/o an image, send null for:
         *
         * @param [imageBackground]
         * @param [image]
         * */
        fun showToast(message: String,
                      toastLengthLong: Boolean = false,
                      @DrawableRes toastBackground: Int = R.drawable.toast_background,
                      @ColorRes toastBackgroundTint: Int = R.color.primaryColor,
                      @ColorRes toastTextColor: Int = R.color.pa_white,
                      @DrawableRes imageBackground: Int? = R.drawable.ic_launcher_background,
                      @DrawableRes image: Int? = R.drawable.ic_launcher_foreground
        ) {
            if (!::appContext.isInitialized) return

            val inflater = appContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(appContext.resources.getLayout(R.layout.toast), null)

            // Toast background
            val toastLinearLayout = layout.findViewById<LinearLayout>(R.id.layout_linear_toast)
            toastLinearLayout.background = appContext.getDrawable(toastBackground)
            toastLinearLayout.background.setTint(appContext.getColor(toastBackgroundTint))

            // Toast image
            val toastImageView = layout.findViewById<ImageView>(R.id.image_view_toast)
            if (imageBackground != null) {
                toastImageView.background = appContext.getDrawable(imageBackground)
            }
            if (image != null) {
                toastImageView.setImageDrawable(appContext.getDrawable(image))
            }
            if (imageBackground == null && image == null) {
                toastImageView.visibility = View.GONE
            }

            // Toast text
            val toastTextView = layout.findViewById<TextView>(R.id.text_view_toast)
            toastTextView.setTextColor(appContext.getColor(toastTextColor))
            toastTextView.text = message

            // Create the Toast & apply modifications
            val toast = Toast(appContext)
            toast.view = layout
            toast.duration =
                if (toastLengthLong) {
                    Toast.LENGTH_LONG
                } else {
                    Toast.LENGTH_SHORT
                }

            val yOffsetPx =
                if (MainActivity.getBottomNavBarHeightPx() > 0) {
                    MainActivity.getBottomNavBarHeightPx()
                } else {
                    56.dpToPx
                }
            toast.setGravity(Gravity.BOTTOM, 0, yOffsetPx + 8.dpToPx)

            if (::previousToast.isInitialized && previousToast.view.isShown) {
                previousToast.cancel()
            }
            previousToast = toast

            toast.show()
        }
    }

    override fun onCreate() {
        super.onCreate()

        initializePinAuthentication()

        if (PinAuthentication.Controller().hasInitialAppLoginBeenSatisfied().value == true) {

            // Update the theme for MainActivity here before it's instantiated so it doesn't
            // have to go through the recreate() call. Could also load user's preferred theme
            // here, too.
            MainActivity.setCurrentTheme(R.style.PostInitialLoginTheme)
        }

        appContext = this.applicationContext
    }

    private fun initializePinAuthentication() {
        PinAuthentication.Builder()
            .setApplicationAndBuildConfig(this, BuildConfig.DEBUG)
            .applicationHasOnBoardProcess()
            .enableBackgroundLogoutTimer(4)
            .enableHapticFeedbackByDefault()
            .enablePinSecurityByDefault()
            .enableScrambledPinByDefault()
            .enableWrongPinLockout(10, 3)
            .setMinimumPinLength(4)


            /**
             * Set custom colors for [PinAuthentication]'s Activity which will overwrite
             * the default color value for that view.
             *
             * Colors can also be changed after initialization of
             * [PinAuthentication] by utilizing [PinAuthentication.Settings.setCustomColors]
             * method, and also reset back to the colors chosen here by utilizing the
             * [PinAuthentication.Settings.resetColorsToApplicationDefaults] method.
             *
             * @see [PinAuthentication.Builder.OptionsBuilder.setCustomColors]
             * @see [PinAuthentication.Builder.ColorsBuilder]
             * */
            .setCustomColors()
            .set2_ConfirmButtonBackgroundColor(R.color.secondaryLightColor)
            .set4_PinHintContainerColor(R.color.primaryDarkColor)
            .set6_PinPadButtonBackgroundColor(R.color.primaryDarkColor)
            .set8_ScreenBackgroundColor(R.color.primaryColor)

            /**
             * Calling applyColors from within the Builder will **NOT** return null. null is only
             * returned if calling [PinAuthentication.Settings.setCustomColors].
             *
             * @see [PinAuthentication.Builder.ColorsBuilder.applyColors]
             * */
            .applyColors()!!

            .build()
    }

}