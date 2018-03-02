package com.cs.kugou.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Scroller
import com.cs.kugou.bean.Lyric

/**
 *
 * author : ChenSen
 * data : 2018/2/28
 * desc:
 */
class LyricView : View {
    companion object {
        const val MSG_HIDE = 0  //隐藏指示器
    }

    private val mBtnColor = Color.parseColor("#EFEFEF")  // 按钮颜色
    private val mHintColor = Color.parseColor("#FFFFFF")  // 提示语颜色
    private val mDefaultColor = Color.parseColor("#FFFFFF")  // 默认字体颜色
    private val mIndicatorColor = Color.parseColor("#EFEFEF")  // 指示器颜色
    private var mHighLightColor = Color.parseColor("#E3F583")  // 当前播放位置的颜色
    private val mCurrentShowColor = Color.parseColor("#AAAAAA")  // 当前拖动位置的颜色

    private lateinit var mTextPaint: Paint
    private lateinit var mTextPaintHL: Paint
    private lateinit var mBtnPaint: Paint
    private lateinit var mIndicatorPaint: Paint

    private var mLineCount = 0 // 行数
    private var mLineHeight = 0f // 行高
    private var mLineSpace = 0f // 行间距（包含在行高中）
    private var mShaderWidth = 0f  // 渐变过渡的距离

    private var mLyricsWordIndex = -1  //当前歌词的第几个字
    private var mLyricsWordHLTime = 0f //当前歌词第几个字 已经播放的时间

    private var mCurrentShowLine = 0       // 当前拖动位置对应的行数
    private var mCurrentPlayLine = 0       // 当前播放位置对应的行数
    private val mMinStartUpSpeed = 1600f    // 最低滑行启动速度
    private var maximumFlingVelocity = 0f  // 最大纵向滑动速度
    private val mMaxDampingDistance = 360  //阻尼效果 最大滑动距离

    private var mVelocityTracker: VelocityTracker? = null //速度检测
    private var mFlingAnimator: ValueAnimator? = null  //滑动动画

    private var isUserTouch = false     //用户是否触摸歌词
    private var isIndicatorShow = false   //是否滑动提示部分内容绘制

    private var mScrollY = 0f  // 纵轴偏移量
    private var mVelocity = 0f  // 纵轴上的滑动速度
    private var mDownX = 0f    // 记录手指按下时的坐标和当前的滑动偏移量
    private var mDownY = 0f
    private var mLastScrollY = 0f

    private var mDefaultHint = "暂无歌词"
    private lateinit var mLyric: Lyric

    private var handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_HIDE -> {
                    isIndicatorShow = false
                    smoothScrollTo(measureCurrentScrollY(mCurrentPlayLine))
                }
            }
        }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        init(context)
    }

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        maximumFlingVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()

        mTextPaint = Paint()
        mTextPaint.isDither = true
        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = getRawSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        mTextPaint.color = mDefaultColor

        mTextPaintHL = Paint()
        mTextPaintHL.isDither = true
        mTextPaintHL.isAntiAlias = true
        mTextPaintHL.textAlign = Paint.Align.CENTER
        mTextPaintHL.textSize = getRawSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        mTextPaintHL.color = mHighLightColor

        mBtnPaint = Paint()
        mIndicatorPaint = Paint()

        measureLineHeight()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mShaderWidth = measuredHeight * 0.3f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mLyric != null && mLyric.lyricLineInfo != null) {
            var lineInfo = mLyric.lyricLineInfo
            for (i in 0 until lineInfo!!.size) {
                var x = measuredWidth * 0.5f
                var y = measuredHeight * 0.5f + (i + 0.5f) * mLineHeight - 6 - mLineSpace * 0.5f - mScrollY

                if (y + mLineHeight * 0.5f < 0) continue  //超出上视图
                if (y - mLineHeight * 0.5f > measuredHeight) break //超出下视图

                //歌词淡入淡出
                when {
                    y < mShaderWidth -> mTextPaint.alpha = 26 + (23000.0f * y / mShaderWidth * 0.01f).toInt()
                    y > measuredHeight - mShaderWidth -> mTextPaint.alpha = 26 + (23000.0f * (measuredHeight - y) / mShaderWidth * 0.01f).toInt()
                    else -> mTextPaint.alpha = 255
                }

                lineInfo[i]?.let {
                    if (i == mCurrentPlayLine - 1) {
                        mTextPaint.color = mHighLightColor
                    } else {
                        mTextPaint.color = mDefaultColor
                    }
                    canvas.drawText(it.lineLyric, x, y, mTextPaint)
                }
            }

        } else {
            canvas.drawText(mDefaultHint, measuredWidth * 0.5f, measuredHeight * 0.5f, mTextPaint)
        }

        if (isIndicatorShow) {
            mTextPaint.alpha = 255
            canvas.drawLine(0f, measuredHeight * 0.5f, measuredWidth.toFloat(), measuredHeight * 0.5f, mTextPaint)
        }
    }

//    //绘制动感歌词
//    private fun drawDGLyric(canvas: Canvas, lyricLine: Lyric.LyricLine, x: Float, y: Float) {
//        var lineLyricsHLWidth = 0f
//        var lineLyric = lyricLine.lineLyric  //整行歌词
//        var curLyricsWidth = mTextPaint.measureText(lineLyric) //整行歌词的宽度
//
//
//
//        if (mLyricsWordIndex == -1) {
//            lineLyricsHLWidth = curLyricsWidth
//        } else {
//
//            var lyricWords = lyricLine.lyricWords      //单个字的数组
//            var wordInterval = lyricLine.wordInterval  //每个字持续时间的数组
//
//            // 当前歌词之前的歌词
//            var lyricBefore = ""
//            for (i in 0 until mLyricsWordIndex) {
//                lyricWords?.let {
//                    lyricBefore += it?.get(i)
//                }
//            }
//            Android.log("之前的歌词  $lyricBefore")
//            // 当前歌词之前的歌词长度
//            val lyricsBeforeWordWidth = mTextPaint.measureText(lyricBefore)
//
//            // 当前歌词
//            val lyricCurWord = lyricWords!![mLyricsWordIndex]!!.trim()
//            // 当前歌词长度
//            val lyricCurWordWidth = mTextPaint.measureText(lyricCurWord)
//
//            val len = lyricCurWordWidth / wordInterval!![mLyricsWordIndex]!! * mLyricsWordHLTime
//            lineLyricsHLWidth = lyricsBeforeWordWidth + len
//        }
//        val curTextX = (width - curLyricsWidth) * 0.5f
//
//        // save和restore是为了剪切操作不影响画布的其它元素
//        canvas.save()
//
//        // 画当前歌词
//        canvas.drawText(lineLyric, curTextX, y, mTextPaint)
//
//        // 设置过渡的颜色和进度
//        canvas.clipRect(curTextX, y - getRealTextHeight(mTextPaint), curTextX + lineLyricsHLWidth,
//                y + getRealTextHeight(mTextPaint))
//
//        // 画当前歌词
//        canvas.drawText(lineLyric, curTextX, y, mTextPaintHL)
//        canvas.restore()
//    }

//    private fun getRealTextHeight(paint: Paint): Int {
//        val fm = paint.fontMetrics
//        return (-fm.leading - fm.ascent + fm.descent).toInt()
//    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain()
        mVelocityTracker?.addMovement(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> actionDown(event)
            MotionEvent.ACTION_MOVE -> actionMove(event)
            MotionEvent.ACTION_UP -> actionUp(event)
            MotionEvent.ACTION_CANCEL -> releaseVelocityTracker()
        }
        invalidateView()
        return true
    }

    private fun actionDown(event: MotionEvent) {
        if (mFlingAnimator != null) {
            mFlingAnimator?.cancel()
            mFlingAnimator = null
        }
        handler.removeMessages(MSG_HIDE)
        mDownX = event.x
        mDownY = event.y
        mLastScrollY = mScrollY
        isUserTouch = true
    }

    private fun actionMove(event: MotionEvent) {
        isIndicatorShow = true
        var curX = event.x
        var curY = event.y
        var deltaX = mDownX - curX
        var deltaY = mDownY - curY

        mVelocityTracker?.computeCurrentVelocity(1000, maximumFlingVelocity)
        var scrollY = mLastScrollY + deltaY         // 102  -2  58  42
        val value01 = scrollY - mLineCount * mLineHeight * 0.5f   // 52  -52  8  -8  总高度的一半
        val value02 = Math.abs(value01) - mLineCount * mLineHeight * 0.5f   // 2  2  -42  -42
        mScrollY = if (value02 > 0) scrollY - measureDampingDistance(value02) * value01 / Math.abs(value01) else scrollY   //   value01 / Math.abs(value01)  控制滑动方向

        mVelocityTracker?.let {
            mVelocity = it.yVelocity
        }
        //    measureCurrentLine()
    }

    private fun actionUp(event: MotionEvent) {
        releaseVelocityTracker()
        isUserTouch = false
        if (isIndicatorShow) {
            handler.sendEmptyMessageDelayed(MSG_HIDE, 2000)
        }

        //超出最上一行
        if (overScrolled() && mScrollY < 0) {
            smoothScrollTo(0f)
            return
        }
        //超出最后一行
        if (overScrolled() && mScrollY > mLineHeight * (mLineCount - 1)) {
            smoothScrollTo(mLineHeight * (mLineCount - 1))
            return
        }

        //滑行
        if (Math.abs(mVelocity) > mMinStartUpSpeed) {
            doFlingAnimator(mVelocity)
            return
        }
    }

    //滑行动画
    private fun doFlingAnimator(velocity: Float) {
        var distance = (velocity / Math.abs(velocity) * Math.min((Math.abs(velocity) * 0.050f), 640f)) // 计算就已当前的滑动速度理论上的滑行距离是多少
        val to = Math.min(Math.max(0f, mScrollY - distance), (mLineCount - 1) * mLineHeight)   // 综合考虑边界问题后得出的实际滑行距离

        mFlingAnimator = ValueAnimator.ofFloat(mScrollY, to)
        mFlingAnimator?.addUpdateListener {
            mScrollY = it.animatedValue as Float
            invalidateView()
        }
        mFlingAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                mVelocity = mMinStartUpSpeed - 1
            }
        })
        mFlingAnimator?.duration = 500
        mFlingAnimator?.interpolator = DecelerateInterpolator()
        mFlingAnimator?.start()
    }

    //判断当前View是否已经滑动到歌词的内容区域之外
    private fun overScrolled(): Boolean {
        return (mScrollY > mLineHeight * (mLineCount - 1) || mScrollY < 0)
    }

    private fun releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker?.clear()
            mVelocityTracker?.recycle()
            mVelocityTracker = null
        }
    }

    private fun measureCurrentLine() {
        val baseScrollY = mScrollY + mLineHeight * 0.5f
        mCurrentShowLine = (baseScrollY / mLineHeight + 1).toInt()
    }

    private fun measureDampingDistance(value02: Float): Float {
        return if (value02 > mMaxDampingDistance) mMaxDampingDistance * 0.6f + (value02 - mMaxDampingDistance) * 0.72f else value02 * 0.6f
    }

    //设置当前播放进度
    fun setCurrentTimeMillis(time: Long) {
        var position = 0
        for (i in 0 until mLineCount) {
            var lineInfo = mLyric.lyricLineInfo?.get(i)
            if (lineInfo != null && lineInfo.startTime > time) {
                position = i
                break
            }
            if (i == mLineCount - 1) {
                position = mLineCount
            }
        }

        if (mCurrentPlayLine != position) {
            mCurrentPlayLine = position
            smoothScrollTo(measureCurrentScrollY(position))
        }
    }

    //从当前位置滑动到指定位置
    private fun smoothScrollTo(toY: Float) {
        val animator = ValueAnimator.ofFloat(mScrollY, toY)
        animator.addUpdateListener {
            if (isUserTouch || isIndicatorShow) {
                animator.cancel()
                return@addUpdateListener
            }
            mScrollY = it.animatedValue as Float
            invalidateView()
        }
        animator.interpolator = OvershootInterpolator(0.5f)
        animator.duration = 400
        animator.start()
    }

    //根据当前行获取scroll Y
    private fun measureCurrentScrollY(line: Int): Float {
        return (line - 1) * mLineHeight
    }

    private fun measureLineHeight() {
        var lineBound = Rect()
        mTextPaint.getTextBounds(mDefaultHint, 0, mDefaultHint.length, lineBound)
        mLineSpace = lineBound.height().toFloat()
        mLineHeight = lineBound.height() + mLineSpace
    }

    fun setLyric(lyric: Lyric) {
        mLyric = lyric
        mLyric.lyricLineInfo?.let {
            mLineCount = it.size
        }
        invalidateView()
    }

    private fun invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //  当前线程是主UI线程，直接刷新。
            invalidate()
        } else {
            //  当前线程是非UI线程，post刷新。
            postInvalidate()
        }
    }

    private fun getRawSize(unit: Int, size: Float): Float {
        return TypedValue.applyDimension(unit, size, resources.displayMetrics)
    }
}