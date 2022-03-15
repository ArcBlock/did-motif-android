package io.arcblock.did_motif.utils

import android.content.res.Resources
import android.util.TypedValue

object Utils {
  fun dp2px(
    value: Float
  ): Int {
    return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().displayMetrics) + 0.5f).toInt()
  }

}