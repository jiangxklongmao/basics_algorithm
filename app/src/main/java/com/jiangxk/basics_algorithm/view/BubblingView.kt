package com.jiangxk.basics_algorithm.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.jiangxk.basics_algorithm.R
import com.jiangxk.basics_algorithm.bean.SortBean

/**
 * @desc
 * @auth jiangxk
 * @time 2018/5/21  21:46
 */
class BubblingView : SurfaceView, SurfaceHolder.Callback, Runnable {

    companion object {
        const val TIME_IN_FRAME = 10
    }

    private lateinit var paint: Paint
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var canvas: Canvas
    private var isDrawing: Boolean = false

    /**
     * 左右边距
     */
    private var margin = 20f
    private var space = 10f
    val valueList = arrayListOf<SortBean>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        init()
    }

    private fun init() {
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        keepScreenOn = true

        paint = Paint()
        paint.isAntiAlias = true
        paint.strokeWidth = 1f
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK

//        path = Path()
//        path.moveTo(sinX, sinY)
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        isDrawing = true
        Thread(this).start()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        isDrawing = false
    }

    override fun run() {
        while (isDrawing) {
            //取得更新之前的时间
            val startTime = System.currentTimeMillis()

            //同步锁
            synchronized(surfaceHolder) {
                //                //锁定画布
//                canvas = surfaceHolder.lockCanvas()
                draw()
//                //解锁画布并显示
//                surfaceHolder.unlockCanvasAndPost(canvas)
            }
            //结束时间
            val endTime = System.currentTimeMillis()
            var diffTime = endTime - startTime

            /**确保每次更新时间为30帧**/
            while (diffTime <= TIME_IN_FRAME) {
                diffTime = System.currentTimeMillis() - startTime
                /**线程等待**/
                Thread.yield()
            }
        }
    }

    private fun draw() {
        try {
            canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(context.resources.getColor(R.color.colorWhite))

            if (valueList.isEmpty()) {
                return
            }
            val valueWidth = (width - margin * 2 - space * (valueList.size - 1)).div(valueList.size)

            for (index in 0 until valueList.size) {
                when {
                    valueList[index].isCompleted -> paint.color = context.resources.getColor(R.color.colorPrimary)
                    valueList[index].isComparing -> paint.color = context.resources.getColor(R.color.colorAccent)
                    else -> paint.color = Color.BLACK
                }

                val left = margin + valueWidth * index + space * index
                val top = height.div(2).minus(valueList[index].value)
                val right = margin + valueWidth * (index + 1) + space * index
                val bottom = height.div(2).toFloat()
                val rectF = RectF(left, top, right, bottom)
                canvas.drawRect(rectF, paint)
            }
        } catch (e: Exception) {

        } finally {
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

}