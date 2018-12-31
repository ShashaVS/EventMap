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

import android.animation.TypeEvaluator

class ZoomTranslateTypeEvaluator : TypeEvaluator<TranslateData> {

    override fun evaluate(fraction: Float, startValue: TranslateData?, endValue: TranslateData?): TranslateData? {
        return TranslateData().apply {
            if(startValue != null && endValue != null) {
                scale =  startValue.scale + (endValue.scale - startValue.scale) * fraction

                if(startValue.X != null && endValue.X != null)
                    X = startValue.X!! + (endValue.X!! - startValue.X!!)* fraction

                if(startValue.Y != null && endValue.Y != null)
                    Y = startValue.Y!! + (endValue.Y!! - startValue.Y!!)* fraction
            }
        }
    }
}