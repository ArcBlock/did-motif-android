package com.arcblock.wallet.appcommonres.view.didmotif

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Cap
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.graphics.withSave
import com.arcblock.wallet.appcommonres.view.didmotif.DIDMotif.Shape.Circle
import com.arcblock.wallet.appcommonres.view.didmotif.DIDMotif.Shape.Hexagon
import com.arcblock.wallet.appcommonres.view.didmotif.DIDMotif.Shape.Rectangle
import com.arcblock.wallet.appcommonres.view.didmotif.DIDMotif.Shape.Square
import io.arcblock.did_motif.utils.Utils
import io.arcblock.walletkit.did.RoleType
import io.arcblock.walletkit.did.RoleType.ACCOUNT
import io.arcblock.walletkit.did.RoleType.APPLICATION
import io.arcblock.walletkit.did.RoleType.ASSET
import io.arcblock.walletkit.did.RoleType.TOKEN
import io.arcblock.walletkit.utils.address
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Created by Nate on 2/15/22
 */
class DIDMotif(
  context: Context,
  attrs: AttributeSet
) : View(context, attrs) {

  enum class Shape {
    Square,
    Rectangle,
    Circle,
    Hexagon
  }

  private val bgColors = arrayListOf(
    "#E94E4E",
    "#E41115",
    "#E96B4E",
    "#E5502E",
    "#E98F4E",
    "#E57A2E",
    "#E98F4E",
    "#E5A82E",
    "#DACD5D",
    "#DAC825",
    "#BDD13D",
    "#C8E31C",
    "#AEE94E",
    "#7FD558",
    "#52CC19",
    "#4FC469",
    "#59DE9C",
    "#19CC73",
    "#5ED9D1",
    "#19CCC0",
    "#4ED7E9",
    "#19B7CC",
    "#45ACE8",
    "#1C97DE",
    "#4E68E9",
    "#2E4DE5",
    "#7F4EE9",
    "#682EE5",
    "#BE65E7",
    "#AF40E2",
    "#DF58C2",
    "#E94E8F",
  )

  private val DEBUG_MODE = false
  private val totalIndex = arrayListOf<Int>()
  private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var mPaintPoint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val coordinates = arrayListOf<Pair<Int, Int>>()
  private var bgColor = Color.WHITE
  private var radius = 100f
  private var mSize = 0
  private var radio = 1f
    set(value) {
      invalidate()
      field = value
    }
  private lateinit var bounds: RectF
  private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
  private var type: Shape = Square

  init {
    mPaint.style = Paint.Style.FILL
    mPaintPoint.strokeWidth = 8f
    mPaintPoint.style = Paint.Style.FILL
    mPaintPoint.strokeCap = Cap.ROUND
    totalIndex.clear()
    for (i in 0..63) {
      totalIndex.add(i)
    }
  }

  override fun onSizeChanged(
    w: Int,
    h: Int,
    oldw: Int,
    oldh: Int
  ) {
    super.onSizeChanged(w, h, oldw, oldh)
    mSize = measuredWidth
    radius = mSize * 0.25f
    bounds = RectF(0f, 0f, mSize.toFloat(), mSize.toFloat())
  }

  fun startAnimation() {
    val animator: ObjectAnimator = ObjectAnimator.ofFloat(
      this, "radio", 0f, 1f
    )
    animator.duration = 1000
    animator.interpolator = OvershootInterpolator()
    animator.start()
  }

  fun setAddress(
    address: String,
    type: Shape? = null,
    withAnimation: Boolean = true
  ) {
    val info = if (address.address().length < 10) {
      randomMotif()
    } else try {
      DIDMotifUtils.getIndexFromDID(address)
    } catch (e: Exception) {
      // 如果发生异常，随机一个组合
      randomMotif()
    }
    if (type != null) {
      this.type = type
    } else {
      val roleType = info.third
      this.type = when (roleType) {
        ACCOUNT -> Rectangle
        ASSET -> Hexagon
        TOKEN -> Circle
        APPLICATION -> Square
        else -> Square
      }
    }
    bgColor = Color.parseColor(bgColors[info.first])
    coordinates.clear()
    val randoms = info.second.map {
      Pair(it / 8, it % 8)
    }
    coordinates.addAll(randoms)
    if (withAnimation) {
      startAnimation()
    } else {
      invalidate()
    }
  }

  private fun randomMotif(): Triple<Int, List<Int>, RoleType> {
    val colorIndexes = arrayListOf<Int>()
    for (i in 0..31) {
      colorIndexes.add(i)
    }
    return Triple(colorIndexes.random(), totalIndex.shuffled().take(8), APPLICATION)
  }

  private fun calcBorderRadius(): Int {
    return if (mSize > Utils.dp2px(
        80f
      )
    ) Utils.dp2px(10f) else floor(0.1 * mSize + 2).toInt()
  }

  override fun onDraw(canvas: Canvas) {
    val count = canvas.saveLayer(bounds, null)
    mPaint.color = bgColor
    mPaint.alpha = 255
    when (type) {
      Square -> {
        val borderRadius = calcBorderRadius().toFloat()
        canvas.drawRoundRect(
          0f, 0f, mSize.toFloat(), mSize.toFloat(), borderRadius, borderRadius, mPaint
        )
      }
      Rectangle -> {
        val borderRadius = calcBorderRadius().toFloat()
        val height = 0.7 * mSize
        canvas.drawRoundRect(
          0f,
          (mSize / 2f - height / 2f).toFloat(),
          mSize.toFloat(),
          (mSize / 2f + height / 2f).toFloat(),
          borderRadius,
          borderRadius,
          mPaint
        )
      }
      Circle -> canvas.drawCircle(mSize / 2f, mSize / 2f, mSize / 2f, mPaint)
      Hexagon -> canvas.drawPath(getHexagonPath(mSize / 2f), mPaint)
    }
    mPaint.xfermode = XFERMODE
    mPaint.color = Color.WHITE
    mPaint.alpha = 150
    if (DEBUG_MODE) {
      mPaintPoint.color = Color.BLACK
      totalIndex.forEach {
        val x = it / 8
        val y = it % 8
        canvas.drawPoint(mSize * (x + 1) / 9f, mSize * (y + 1) / 9f, mPaintPoint)
      }
    }
    coordinates.forEach { item ->
      drawElements(canvas, item.first, item.second)
    }
    mPaint.xfermode = null
    canvas.restoreToCount(count)
  }

  // x: 0,1,2,3,4,5,6,7
  // y: 0,1,2,3,4,5,6,7
  private fun drawElements(
    canvas: Canvas,
    x: Int,
    y: Int
  ) {
    canvas.withSave {
      canvas.translate(
        (-mSize / 2f + mSize * (x + 1) / 9f) * radio, (-mSize / 2f + mSize * (y + 1) / 9f) * radio
      )
      canvas.drawPath(getHexagonPath(radius), mPaint)
      mPaintPoint.color = Color.RED
    }
    if (DEBUG_MODE) {
      canvas.drawPoint(
        mSize * (x + 1) / 9f, mSize * (y + 1) / 9f, mPaintPoint
      )
    }
  }

  private fun getHexagonPath(hexagonRadius: Float): Path {
    val left = ((mSize - sqrt(3.0) * hexagonRadius) / 2.0)
    val top = ((mSize - 2 * hexagonRadius) / 2.0)
    val localPath = Path()
    localPath.moveTo(((left + sqrt(3.0) * hexagonRadius / 2.0).toFloat()), top.toFloat())
    localPath.lineTo(left.toFloat(), ((top + hexagonRadius / 2f).toFloat()))
    localPath.lineTo(left.toFloat(), ((top + 1.5f * hexagonRadius).toFloat()))
    localPath.lineTo(
      ((left + sqrt(3.0) * hexagonRadius / 2.0f).toFloat()), ((top + 2 * hexagonRadius).toFloat())
    )
    localPath.lineTo(
      ((left + sqrt(3.0) * hexagonRadius).toFloat()), ((top + 1.5f * hexagonRadius).toFloat())
    )
    localPath.lineTo(
      ((left + sqrt(3.0) * hexagonRadius).toFloat()), ((top + hexagonRadius / 2.0f).toFloat())
    )
    localPath.lineTo(((left + sqrt(3.0) * hexagonRadius / 2.0).toFloat()), top.toFloat())
    localPath.close()
    return localPath
  }
}