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

package com.shashavs.eventmap.drag_scale.default_view

import android.annotation.SuppressLint
import android.content.Context
import com.shashavs.eventmap.R
import com.shashavs.eventmap.drag_scale.base.InfoView
import com.shashavs.eventmap.drag_scale.base.Marker
import kotlinx.android.synthetic.main.default_info_layout.view.*

@SuppressLint("ViewConstructor")
class DefaultInfoLayout(context: Context, marker: Marker, val title: String, val description: String) : InfoView(context, marker) {

    init {
        inflate(context, R.layout.default_info_layout, this)
        info_title.text = title
        info_description.text = description
        close_info.setOnClickListener { select(false) }
    }

}