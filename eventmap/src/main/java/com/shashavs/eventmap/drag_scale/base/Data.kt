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

data class Data (var scale: Float,
                 var isScale: Boolean,
                 var maxScale: Float,
                 var minScale: Float,
                 var width: Float,
                 var height: Float,
                 var pivotX: Float,
                 var pivotY: Float,
                 var touchX: Float,
                 var touchY: Float,
                 var X: Float,
                 var Y: Float,
                 var initX: Float,
                 var initY: Float,
                 var margin: Float) {

    constructor(): this(1f, false,1.2f, 0.8f,0f, 0f,
        0f, 0f,0f, 0f,0f, 0f,0f, 0f,140f)
}

data class TranslateData(var scale: Float, var X: Float?, var Y: Float?) {
    constructor(): this(1f, null, null)
}