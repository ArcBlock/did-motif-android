package io.arcblock.did_motif.blockies;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import io.arcblock.did.did_motif.R;

/**
 * Custom view that is used to display a
 */

public class BlockiesIdenticon extends View {

  public static final int SHAPE_SQUARE = 0;
  public static final int SHAPE_CIRCLE = 1;

  private BlockiesData mData;
  private Paint mPaint;
  private RectF mBlock = new RectF();
  private int type = SHAPE_SQUARE; // 0 - square 1 - circle
  private float cornerSize = 0f;
  private PorterDuffXfermode XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

  public BlockiesIdenticon(Context context) {
    super(context);
    mData = new BlockiesData("", BlockiesData.DEFAULT_SIZE);
    init();
  }

  public BlockiesIdenticon(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray a =
        context.getTheme().obtainStyledAttributes(attrs, R.styleable.BlockiesIdenticon, 0, 0);
    String address = "";
    try {
      address = a.getString(R.styleable.BlockiesIdenticon_address);
    } finally {
      a.recycle();
    }
    mData = new BlockiesData(address, BlockiesData.DEFAULT_SIZE);
    init();
  }

  private void init() {
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL);
  }

  @Override
  protected void onSizeChanged(int width, int height, int oldw, int oldh) {
    float right = width;
    float bottom = height;
    mBlock.set(0, 0, right, bottom);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int w = this.getWidth();
    int[] colors = mData.getColors();
    int count = canvas.saveLayer(mBlock, null);
    mPaint.setColor(colors[0]);
    if (type == SHAPE_SQUARE) {
      canvas.drawRoundRect(0f, 0f, w, w, cornerSize, cornerSize, mPaint);
    } else {
      canvas.drawCircle(w / 2f, w / 2f, w / 2f, mPaint);
    }
    mPaint.setXfermode(XFERMODE);
    int[] data = mData.getImageData();
    double blockSize = w / Math.sqrt(data.length);
    for (int i = 0; i < data.length; i++) {
      double x = (blockSize * i) % w;
      double y = Math.floor((blockSize * i) / w) * blockSize;
      RectF rect =
          new RectF((float) x, (float) y, (float) (x + blockSize), (float) (y + blockSize));
      if (data[i] == 2) {
        mPaint.setColor(colors[2]);
        canvas.drawRect(rect, mPaint);
      } else if (data[i] == 1) {
        mPaint.setColor(colors[1]);
        canvas.drawRect(rect, mPaint);
      }
    }

    mPaint.setXfermode(null);
    canvas.restoreToCount(count);
  }

  public void setAddress(String address, int type) {
    this.setAddress(address, type, BlockiesData.DEFAULT_SIZE, 0);
  }

  public void setAddressWithCorner(String address, int type, float cornerSize) {
    this.setAddress(address, type, BlockiesData.DEFAULT_SIZE, cornerSize);
  }

  public void setAddress(String address, int type, int size, float cornerSize) {
    mData = new BlockiesData(address, size);
    this.cornerSize = cornerSize;
    this.type = type;
    invalidate();
  }
}
