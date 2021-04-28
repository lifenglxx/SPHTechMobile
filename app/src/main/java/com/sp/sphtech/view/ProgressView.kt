package com.sp.sphtech.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.Nullable
import com.sp.sphtech.utils.dc
import com.sp.sphtech.utils.dm
import com.sph.sphtech.R


/**
 * 条形进度条（可设置 线性渐变-背景色-进度条颜色-进度条高度）
 */
class BarPercentView : View {
    private var rectFBg: RectF = RectF(0f, 0f, 0f, 0f)
    private var rectFProgress: RectF = RectF(0f, 0f, 0f, 0f)
    private var mPaint: Paint = Paint()
    private var mWidth = 0
    private var progressPercent = 0f
    private var bgColor = R.color.ccfcfcf.dc
    private var progressColor = R.color.cff3030.dc
    private var mHeight = R.dimen.margin_10.dm
    private var radius = R.dimen.margin_5.dm
    private var startColor = R.color.cff3030.dc
    private var endColor = R.color.cff3030.dc
    private var gradient: LinearGradient? = null
    private var isGradient = false

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        //获取自定义属性
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BarPercentView)
        bgColor = typedArray.getColor(
            R.styleable.BarPercentView_barBgColor,
            R.color.ccfcfcf.dc
        )
        progressColor = typedArray.getColor(
            R.styleable.BarPercentView_barProgressColor,
            R.color.cff3030.dc
        )
        mHeight = typedArray.getDimensionPixelSize(
            R.styleable.BarPercentView_barHeight,
            R.dimen.margin_10.dm
        )
        isGradient = typedArray.getBoolean(R.styleable.BarPercentView_barIsGradient, false)
        startColor = typedArray.getColor(
            R.styleable.BarPercentView_barStartColor,
            R.color.cff3030.dc
        )
        endColor = typedArray.getColor(
            R.styleable.BarPercentView_barEndColor,
            R.color.cff3030.dc
        )
        radius = typedArray.getInt(R.styleable.BarPercentView_barRadius,
            RADIUS
        )
        init()
    }

    constructor(
        context: Context?,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            widthSpecSize
        } else {
            0
        }
        mHeight =
            if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
                heightSpecSize
            } else {
                context.resources.getDimensionPixelSize(R.dimen.margin_10)
            }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gradient = LinearGradient(
            0f,
            0f,
            width.toFloat(),
            mHeight.toFloat(),
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //1、背景
        mPaint.shader = null
        mPaint.color = bgColor
        rectFBg.right = mWidth.toFloat() //宽度
        rectFBg.bottom = mHeight.toFloat() //高度
        canvas.drawRoundRect(rectFBg, radius.toFloat(), radius.toFloat(), mPaint)
        //2、进度条
        rectFProgress.right = mWidth * progressPercent
        rectFProgress.bottom = mHeight.toFloat()
        //3、是否绘制渐变色
        if (isGradient) {
            mPaint.shader = gradient //设置线性渐变
        } else {
            mPaint.color = progressColor
        }
        if (progressPercent > 0 && rectFProgress.right < radius) //进度值小于半径时，设置大于半径的最小值，防止绘制不出圆弧矩形
            rectFProgress.right = radius + 10.toFloat()
        canvas.drawRoundRect(rectFProgress, radius.toFloat(), radius.toFloat(), mPaint) //进度}
    }

    @Keep
    fun setPercentage(percentage: Float) {
        if (percentage / MAX >= 1) {
            progressPercent = 1f
        } else {
            progressPercent = percentage / MAX
        }
        invalidate()
    }

    private fun init() {
        rectFBg = RectF(0f, 0f, 0f, mHeight.toFloat())
        rectFProgress = RectF(0f, 0f, 0f, mHeight.toFloat())
        mPaint = Paint()
        //设置抗锯齿
        mPaint.isAntiAlias = true
    }

    fun setHeight(mHeight: Int) {
        this.mHeight = mHeight
        invalidate()
    }

    fun setBgColor(bgColor: Int) {
        this.bgColor = bgColor
    }

    fun setProgressColor(progressColor: Int) {
        this.progressColor = progressColor
    }

    fun setStartColor(startColor: Int) {
        this.startColor = startColor
    }

    fun setEndColor(endColor: Int) {
        this.endColor = endColor
    }

    fun setGradient(gradient: Boolean) {
        isGradient = gradient
    }

    companion object {
        const val MAX = 100f
        const val RADIUS = 15
    }
}
