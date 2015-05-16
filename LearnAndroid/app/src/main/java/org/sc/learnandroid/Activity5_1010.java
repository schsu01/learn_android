package org.sc.learnandroid;

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import org.sc.util.ViewUtil;

import java.util.*;

public class Activity5_1010 extends Activity {
  private static final Random rnd = new Random();

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(new View(this) {
      private final Paint paintBMP = new Paint(Paint.DITHER_FLAG);
      private final Paint paintGray = ViewUtil.newPaint(Paint.Style.FILL_AND_STROKE, 20, 0xcccccccc);
      private final Paint paintGrid = ViewUtil.newPaint(Paint.Style.STROKE, 5, 0xaaffffff);
      private final Bitmap[] moveBMP = new Bitmap[4];
      private final Bitmap backBMP = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_4444);
      private final Path grid = new Path();
      private final Rect textRect = new Rect();
      private final RectF[] moveRect = new RectF[3], oriRect = new RectF[3];////10+2=12, 5*3+4=19
      private RectF backRect, moveingRect;
      private float round, offsetX, offsetY, textX, textY;
      private int moving = -1, score = 0;

      @Override
      protected void onDraw(Canvas canvas) {
        if (grid.isEmpty()) {//init
          final int w = canvas.getWidth();
          final float b0 = w / 12f, bs = b0 * 10, bt = b0 * 4, br = b0 + bs, bb = bt + bs, bc = b0 / 4;
          final float t0 = round = w / 19f, ts = t0 * 5, tt = bb + b0 + t0, tb = tt + ts, b5 = b0 * 5;
          final float[] ta = {t0, t0 * 7, t0 * 13};
          for (int i = 1; i < 10; i++) {
            final float d = i * b0, x = b0 + d, y = bt + d;
            grid.moveTo(x, bt);
            grid.lineTo(x, bb);
            grid.moveTo(b0, y);
            grid.lineTo(br, y);
          }
          for (int i = 0; i < 3; i++) {
            grid.addRoundRect(oriRect[i] = new RectF(ta[i], tt, ta[i] + ts, tb), bc, bc, Path.Direction.CW);
            moveBMP[i] = Bitmap.createBitmap(5, 5, Bitmap.Config.ARGB_4444);
          }
          genPattern();
          grid.addRoundRect(backRect = new RectF(b0, bt, br, bb), bc, bc, Path.Direction.CW);
          moveingRect = new RectF(0, 0, b5, b5);
          offsetX = b0 * -2.5f;
          offsetY = b0 * -7;
          textX = (b0 + w) / 2;
          textY = b0 + bt / 2;
          paintBMP.setColor(0xff0099cc);
          paintBMP.setTextAlign(Paint.Align.CENTER);
          paintBMP.setTextSize(b0);
          paintBMP.setTypeface(Typeface.SANS_SERIF);
        }
        canvas.drawRoundRect(backRect, round, round, paintGray);
        canvas.drawBitmap(backBMP, null, backRect, paintBMP);
        canvas.drawPath(grid, paintGrid);
        for (int i = 0; i < 3; i++)
          if (null != moveRect[i]) canvas.drawBitmap(moveBMP[i], null, moveRect[i], paintBMP);
        final String text = String.valueOf(score);
        paintBMP.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, textX - textRect.exactCenterX(), textY - textRect.exactCenterY(), paintBMP);
      }

      private void genPattern() {
        for (final RectF rect : moveRect) if (null != rect) return;
        for (int i = 0; i < 3; i++) {
          moveRect[i] = oriRect[i];
          Pattern.randomPattern(moveBMP[i]);
        }
      }

      private RectF checkMove() {
        final float s = backRect.left;
        final int dx = Math.round((moveingRect.left - s) / s), dy = Math.round((moveingRect.top - backRect.top) / s);
        final Bitmap move = moveBMP[moving];
        final Collection<Integer> changeX = new TreeSet<>(), changeY = new TreeSet<>();
        final List<int[]> copy = new ArrayList<>();
        for (int x = 0, bx, by, color; x < 5; x++)
          for (int y = 0; y < 5; y++)
            if (0 != (color = move.getPixel(x, y)) && (bx = x + dx) >= 0 && bx <= 9 && (by = y + dy) >= 0 && by <= 9)
              if (0 != backBMP.getPixel(bx, by)) return oriRect[moving];//not placeable
              else {
                changeX.add(bx);
                changeY.add(by);
                copy.add(new int[]{bx, by, color});
              }
        for (final int[] xyc : copy) backBMP.setPixel(xyc[0], xyc[1], xyc[2]);
checkX:
        for (final int x : changeX.toArray(new Integer[changeX.size()]))
          for (int y = 0; y < 10; y++)
            if (0 == backBMP.getPixel(x, y)) {
              changeX.remove(x);
              continue checkX;
            }
checkY:
        for (final int y : changeY.toArray(new Integer[changeY.size()]))
          for (int x = 0; x < 10; x++)
            if (0 == backBMP.getPixel(x, y)) {
              changeY.remove(y);
              continue checkY;
            }
        for (final int x : changeX) for (int y = 0; y < 10; y++) backBMP.setPixel(x, y, 0);
        for (final int y : changeY) for (int x = 0; x < 10; x++) backBMP.setPixel(x, y, 0);
        score += (changeX.size() + changeY.size()) * 10 + copy.size();
        return null;
      }

      @Override
      public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX(), y = event.getY();
        moveingRect.offsetTo(x + offsetX, y + offsetY);
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            for (int i = 0; i < 3; i++)
              if (null != moveRect[i] && oriRect[i].contains(x, y)) {
                moveRect[moving = i] = moveingRect;
                break;
              }
            break;
          case MotionEvent.ACTION_MOVE:
            if (moving >= 0) invalidate();
            break;
          case MotionEvent.ACTION_UP:
            if (moving >= 0) {
              moveRect[moving] = checkMove();
              moving = -1;
              genPattern();
              invalidate();
            }
          default:
            return false;
        }
        return true;
      }
    });
  }

  static abstract class Pattern {
    private static final Pattern POS = new Pattern() {
      int get(int value) { return value; }
    }, NEG = new Pattern() {
      int get(int value) { return 4 - value; }
    };
    private static final int[] COLOR = {0xffef9a9a, 0xfff48fb1, 0xffce93d8, 0xffb39ddb, 0xff9fa8da, 0xff90caf9,
        0xff81d4fa, 0xff80deea, 0xff80cbc4, 0xffa5d6a7, 0xffc5e1a5, 0xffe6ee9c, 0xfffff59d, 0xffffe082,
        0xffffcc80, 0xffffab91, 0xffbcaaa4, 0xffb0bec5};
    private static final List<int[][]> XY = freq(new int[][][]{{/*1*/{2}, {2}}, {/*2*/{2, 2}, {1, 2}}, {
    /*3*/{2, 2, 2}, {1, 2, 3}}, {/*4*/{2, 2, 2, 2}, {0, 1, 2, 3}}, {/*5*/{2, 2, 2, 2, 2}, {0, 1, 2, 3, 4}}, {
    /*sL*/{1, 2, 2}, {2, 2, 3}}, {/*bL*/{1, 2, 3, 3, 3}, {1, 1, 1, 2, 3}}, {
    /*sB*/{1, 1, 2, 2}, {1, 2, 1, 2}}, {/*bB*/{1, 1, 1, 2, 2, 2, 3, 3, 3}, {1, 2, 3, 1, 2, 3, 1, 2, 3}}},
        2, 4, 5, 6, 7, 7, 5, 5, 1);

    private static <T> List<T> freq(T[] item, int... freq) {
      final List<T> result = new ArrayList<>();
      for (int i = 0, n = item.length; i < n; i++)
        result.addAll(Collections.nCopies(freq[i], item[i]));
      return result;
    }

    public static void randomPattern(Bitmap bmp) {
      final int[][] pattern = XY.get(rnd.nextInt(XY.size()));
      final boolean swap = rnd.nextBoolean();//swap x & y
      final int[] x = swap ? pattern[1] : pattern[0], y = swap ? pattern[0] : pattern[1];
      final int color = COLOR[rnd.nextInt(COLOR.length)], n = Math.min(x.length, y.length);
      final Pattern px = rnd.nextBoolean() ? NEG : POS, py = rnd.nextBoolean() ? NEG : POS;
      bmp.eraseColor(0);
      for (int i = 0; i < n; i++) bmp.setPixel(px.get(x[i]), py.get(y[i]), color);
    }

    abstract int get(int value);
  }
}