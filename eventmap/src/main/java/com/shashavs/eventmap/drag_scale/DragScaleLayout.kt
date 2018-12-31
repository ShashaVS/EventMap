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

package com.shashavs.eventmap.drag_scale

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.v4.view.MotionEventCompat
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.ImageView
import com.shashavs.eventmap.R
import com.shashavs.eventmap.drag_scale.base.*

class DragScaleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0):
    FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val scaleDetector: ScaleGestureDetector
    private val gestureDetector: GestureDetector
    private val data: Data
    private var markerListener: MarkerListener? = null
    private var selectesMarker: MarkerView? = null
    private val mapImageView: ImageView

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ZoomPinch, defStyleAttr, 0)
        val maxZoom = typedArray.getFloat(R.styleable.ZoomPinch_maxZoom, -1f)
        val mapScr = typedArray.getResourceId(R.styleable.ZoomPinch_mapScr, -1)
        typedArray.recycle()

        data = Data()

        mapImageView = ImageView(context)
        mapImageView.tag = Tag.map
        addView(mapImageView, 0)
        if(mapScr != -1) {
            mapImageView.setImageResource(mapScr)
        }
        mapImageView.waitForLayout { initMap() }

        if(maxZoom != -1f) data.maxScale = maxZoom

        scaleDetector = ScaleGestureDetector(context, object :  ScaleGestureDetector.SimpleOnScaleGestureListener() {

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                data.isScale = true
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
                super.onScaleEnd(detector)
                data.isScale = false
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                if(detector != null) {
                    // Don't let the object get too small or too large.
                    var currentScale = data.scale * detector.scaleFactor
                    currentScale = Math.max(data.minScale, Math.min(currentScale, data.maxScale))
                    if(currentScale == data.scale) return true

                    // todo check offset ratio
                    val ratioX = detector.focusX / width
                    val ratioY = detector.focusY / height

                    data.X -= data.width.times(currentScale - data.scale).times(ratioX)
                    data.Y -= data.height.times(currentScale - data.scale).times(ratioY)
                    data.scale = currentScale

                    onUpdateLayout(data.scale, data.X, data.Y)
                }
                return true
            }

        })

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                // Check margins with minScale
                // calculate markerX
                val toX = when {
                    //inside the screen by width
                    data.width*data.scale < width + 2*data.margin -> {
                        // left corner - in
                        if(data.X < left)
                            data.width*data.scale - right
                        // or right corner - in
                        else if(data.X + data.width*data.scale > right)
                            -left.toFloat()
                        else
                            null
                    }
                    //outside the screen by width
                    else -> {
                        // left corner - out
                        if(data.X > left)
                            -left.toFloat()
                        // or right corner - out
                        else if(data.X + data.width*data.scale < right)
                            data.width*data.scale - right
                        else
                            null
                    }
                }
                // calculate markerY
                val toY = when {
                    //inside the screen by height
                    data.height*data.scale < height + 2*data.margin -> {
                        // top corner - in
                        if(data.Y < top)
                            -top.toFloat()
                        // bottom corner - in
                        else if(data.Y + data.height*data.scale > bottom)
                            data.height*data.scale - bottom
                        else
                            null
                    }
                    //outside the screen by height
                    else -> {
                        // top corner - out
                        if(data.Y < top)
                            data.height*data.scale - bottom
                        // bottom corner - out
                        else if(data.Y + data.height*data.scale > bottom)
                            -top.toFloat()
                        else
                            null
                    }
                }

                if(toX != null || toY != null) {
                    moveTo(data.scale, toX, toY, false)
                }
                return true
            }

            override fun onDoubleTap(event: MotionEvent?): Boolean {
                if(event != null) {
                    if(data.scale == data.minScale) {
                        /**
                         * zoom in
                         * with animation
                         */
                        val toX = data.width * event.x / width
                        val toY = data.height * event.y / height
                        moveTo(1.0f, toX, toY, true)

                        /**
                         * zoom in
                         * without animation
                         */
//                        data.scale = data.maxScale
//                        val offsetX = (data.width * data.scale - width)*(event.x / width)
//                        val offsetY = (data.height * data.scale - height)*(event.y / height)

//                        data.markerX = -offsetX + data.markerX - data.initX
//                        data.markerY = -offsetY + data.markerY - data.initY
//                        onUpdateLayout(data.scale, data.markerX, data.markerY)

                    } else {
                        /**
                         * zoom out
                         * with animation
                         */
                        moveTo(data.minScale, -data.initX, -data.initY, false)

                        /**
                         * zoom out
                         * without animation
                         */
//                        data.scale = data.minScale
//                        data.markerX = data.initX
//                        data.markerY = data.initY
//                        onUpdateLayout(data.scale, data.markerX, data.markerY)
                    }
                }
                return super.onDoubleTap(event)
            }
        })

    }

    private inline fun View.waitForLayout(crossinline func: () -> Unit) = with(viewTreeObserver) {
        addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                    func()
                }
            }
        })
    }

    private fun initMap() {
        data.width = mapImageView.width.toFloat()
        data.height = mapImageView.height.toFloat()

        data.pivotX = 0f
        data.pivotY = 0f

        val scaleX = width / data.width
        val scaleY = height / data.height
        data.scale = Math.min(scaleX, scaleY)

        data.X = (width - data.width*data.scale) / 2
        data.Y = (height - data.height*data.scale) /2

        data.initX = data.X
        data.initY = data.Y
        data.minScale = data.scale

        onUpdateLayout(data.scale, data.X, data.Y)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure ourselves as MATCH_PARENT
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == View.MeasureSpec.UNSPECIFIED || heightMode == View.MeasureSpec.UNSPECIFIED) {
            throw RuntimeException(" must be used with fixed dimensions (e.g. match_parent)")
        }
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)

        // Measure our child as unspecified.
        val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        measureChildren(spec, spec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        scaleDetector.onTouchEvent(event)
        val action = MotionEventCompat.getActionMasked(event)

        /**
         *  MotionEventCompat.getActionMasked(event):
         *  MOVE action: 0 -> 2 -> 1 (ACTION_DOWN -> ACTION_MOVE -> ACTION_UP)
         *  SCALE action: 0 -> 5 -> 2 -> 6 -> 1 (ACTION_DOWN -> ACTION_POINTER_DOWN -> ACTION_MOVE -> ACTION_POINTER_UP -> ACTION_UP)
         *
         *  event.action:
         *  MOVE action: 0 -> 2 -> 1 (ACTION_DOWN -> ACTION_MOVE -> ACTION_UP)
         *  SCALE action: 0 -> 261 -> 2 -> 6 -> 1 (ACTION_DOWN -> ACTION_POINTER_2_DOWN -> ACTION_MOVE -> ACTION_POINTER_UP -> ACTION_UP)
         */

        if(!scaleDetector.isInProgress) {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    data.touchX = event.x
                    data.touchY = event.y
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    data.isScale = true
                }
//                MotionEvent.ACTION_POINTER_2_DOWN -> {
//                    data.isScale = true
//                }
                MotionEvent.ACTION_MOVE -> {
                    if(!data.isScale) {
                        val currentX = data.X + event.x - data.touchX
                        val currentY = data.Y + event.y - data.touchY
                        
//                        DirectionEnum.right

                        /**
                         * right DirectionEnumX < 0
                         * left DirectionEnumX > 0
                         */
                        val DirectionEnumX = when {
                            data.touchX - event.x > 0 -> Direction.left
                            data.touchX - event.x < 0 -> Direction.right
                            else -> Direction.none
                        }
                        /**
                         * top DirectionEnumY > 0
                         * bottom DirectionEnumY < 0
                         */
                        val DirectionEnumY = when {
                            data.touchY - event.y > 0 -> Direction.top
                            data.touchY - event.y < 0 -> Direction.bottom
                            else -> Direction.none
                        }

                        // set horizontal limit area
                        val mapLeft = currentX
                        val mapRight = currentX + data.width*data.scale

                        val updX = when {
                            /**
                             * mapLeft - out
                             * mapRight - in
                             * DirectionEnum only --> right
                             */
                            (mapLeft < left && mapRight < right + data.margin && DirectionEnumX == Direction.right) -> true
                            /**
                             * mapLeft - in
                             * mapRight - out
                             * DirectionEnum only <-- left
                             */
                            (mapLeft > left - data.margin && mapRight > right && DirectionEnumX == Direction.left) -> true
                            /**
                             * mapLeft - out
                             * mapRight - out
                             * (zoom in case)
                             */
                            (mapLeft < left + data.margin && mapRight > right - data.margin) -> true
                            /**
                             * mapLeft - in
                             * mapRight - in
                             */
                            (mapLeft > left - data.margin && mapRight < right + data.margin ) -> true

                            else -> false
                        }

                        // set vertical limit area
                        val mapTop = currentY
                        val mapBottom = currentY + data.height*data.scale

                        val updY = when {
                            /**
                             * mapTop - out
                             * mapBottom - in
                             * DirectionEnum only --> bottom
                             */
                            (mapTop < top && mapBottom < bottom + data.margin && DirectionEnumY == Direction.bottom) -> true
                            /**
                             * mapTop - in
                             * mapBottom - out
                             * DirectionEnum only --> top
                             */
                            (mapTop > top - data.margin && mapBottom > bottom && DirectionEnumY == Direction.top) -> true
                            /**
                             * mapTop - out
                             * mapBottom - out
                             */
                            (mapTop < top + data.margin && mapBottom > bottom - data.margin) -> true
                            /**
                             * mapTop - in
                             * mapBottom - in
                             */
                            (mapTop > top - data.margin && mapBottom < bottom + data.margin) -> true

                            else -> false
                        }

                        if(!updX && !updY) return true

                        if(updX) {
                            data.X = currentX
                            data.touchX = event.x
                        }
                        if(updY) {
                            data.Y = currentY
                            data.touchY = event.y
                        }
                        onUpdateLayout(data.scale, data.X, data.Y)
                    }
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    data.isScale = false
                }
                MotionEvent.ACTION_UP -> {
                    data.touchX = 0f
                    data.touchY = 0f
                }
            }
        }
        return true
    }

    private fun onUpdateLayout(scale: Float, x: Float?, y: Float?) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            when(child.tag) {
                Tag.map -> {
                    /**
                     * by mapMatrix dosent work with screen size more than image
                     */
//                child as ImageView
//                mapMatrix.setScale(scale, scale)
//                mapMatrix.postTranslate(x, y)
//                child.imageMatrix = mapMatrix

                    /**
                     *
                     */
                    child.pivotX = data.pivotX
                    child.pivotY = data.pivotY

                    child.scaleX = scale
                    child.scaleY = scale

                    if(x != null) child.translationX = x
                    if(y != null) child.translationY = y
                }
                Tag.marker -> {
                    child as MarkerView
                    if(x != null) child.translationX = child.marker.X.times(scale) + x - child.width.div(2)
                    if(y != null) child.translationY = child.marker.Y.times(scale) + y - child.height
                }
                Tag.info -> {
                    child as InfoView
                    if(x != null) child.translationX = child.marker.X.times(scale) + x
                    if(y != null) child.translationY = child.marker.Y.times(scale) + y
                }
            }
        }
    }

    private fun moveTo(toScale: Float, toX: Float?, toY: Float?, toCenter: Boolean) {

        val startZoomTranslate = TranslateData().apply {
            scale = data.scale
            X = data.X
            Y = data.Y
        }
        val endZoomTranslate = TranslateData().apply {
            scale = toScale
            if(toX != null) {
                X = if(toCenter) -toX + width/2 else -toX
            }
            if(toY != null) {
                Y = if(toCenter) -toY + height/2 else -toY
            }
        }

        if(data.scale == toScale && data.X == endZoomTranslate.X && data.Y == endZoomTranslate.Y)
            return

        ValueAnimator.ofObject(ZoomTranslateTypeEvaluator(), startZoomTranslate, endZoomTranslate).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener { updatedAnimation ->
                val value = updatedAnimation.animatedValue as TranslateData
                onUpdateLayout(value.scale, value.X, value.Y)

                if(updatedAnimation.animatedFraction == 1f) {
                    data.scale = value.scale
                    if(value.X != null) data.X = value.X!!
                    if(value.Y != null) data.Y = value.Y!!
                }
            }
            start()
        }
    }

    fun addMarker(markerView: MarkerView) {
        val infoView = markerView.infoView
        if(infoView != null) addView(infoView)
        addView(markerView)
        markerView.setOnClickListener(this)
    }

    fun addMarkers(markers: ArrayList<MarkerView>) {
        markers.forEach {
            addView(it)
            it.setOnClickListener(this)
        }
    }

    fun cleanMarkers() {
        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
        selectesMarker = null
    }

    fun getSelectedMarker() = selectesMarker

    override fun onClick(view: View?) {
        if(view is MarkerView) {
            moveTo(1.0f, view.marker.X, view.marker.Y, true)
            selectMarker(view)
            markerListener?.onMarker(view)
        }
    }

    private fun selectMarker(markerView: MarkerView) {
        selectesMarker?.select(false)
        selectesMarker?.infoView?.select(false)

        selectesMarker = markerView
        selectesMarker?.select(true)
        selectesMarker?.infoView?.select(true)
    }

    fun addMarkerListenet(markerListener: MarkerListener) {
        this.markerListener = markerListener
    }

    fun getMapImageView() = mapImageView

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // clean listener
        markerListener = null
    }

}
