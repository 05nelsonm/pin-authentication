/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
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
internal object BindingAdapters {

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
