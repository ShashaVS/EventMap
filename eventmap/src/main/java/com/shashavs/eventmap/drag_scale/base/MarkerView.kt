/*
 *  Created by Alex Sushchenko on 29.12.18 14:00
 *  Copyright (c) 2018
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.shashavs.eventmap.drag_scale.base

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.widget.TextView

@SuppressLint("ViewConstructor")
class MarkerView private constructor(context: Context, val marker: Marker) : TextView(context) {

    var infoView: InfoView? = null

    fun select(select: Boolean) { isActivated = select }

    class Builder(val context: Context, val marker: Marker) {

        private val markerView: MarkerView

        init {
            markerView = MarkerView(context, marker)
            markerView.tag = Tag.marker
        }

        fun info(infoView: InfoView): Builder {
            markerView.infoView = infoView
            return this
        }

        fun background(resource: Int): Builder {
            markerView.setBackgroundResource(resource)
            return this
        }

        fun text(text: String): Builder {
            markerView.text = text
            return this
        }

        fun textSize(size: Float): Builder {
            markerView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
            return this
        }

        fun textColor(resource: Int): Builder {
            markerView.setTextColor(ContextCompat.getColor(context, resource))
            return this
        }

        fun build() = markerView
    }
}