package io.matthewnelson.pin_authentication.util

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * @suppress
 * */
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("paGoneUnless")
    fun View.paGoneUnless(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("paInvisibleUnless")
    fun View.paInvisibleUnless(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("paSetBackgroundColor")
    fun View.paSetBackgroundColor(colorHexStringValue: String) =
        this.setBackgroundColor(Color.parseColor(colorHexStringValue))

    @JvmStatic
    @BindingAdapter("paSetBackgroundTint")
    fun View.paSetBackgroundTint(colorHexStringValue: String) =
        this.background.setTint(Color.parseColor(colorHexStringValue))

    @JvmStatic
    @BindingAdapter("paSetTextColor")
    fun Button.paSetTextColor(colorHexStringValue: String) =
        this.setTextColor(Color.parseColor(colorHexStringValue))

    @JvmStatic
    @BindingAdapter("paSetTextColor")
    fun TextView.paSetTextColor(colorHexStringValue: String) =
        this.setTextColor(Color.parseColor(colorHexStringValue))

    @JvmStatic
    @BindingAdapter("paSetTint")
    fun ImageButton.paSetTint(colorHexStringValue: String) =
        this.setColorFilter(Color.parseColor(colorHexStringValue))

    @JvmStatic
    @BindingAdapter("paSetTint")
    fun ImageView.paSetTint(colorHexStringValue: String) =
        this.setColorFilter(Color.parseColor(colorHexStringValue))
}
