package org.sc.learnandroid;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Activity4_Draw extends Activity {
  private static final int nX = 3, nY = 5;//設定行列數

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(new View(this) {
      private float[] xs = new float[nX], ys = new float[nY];
      private float ref, refW, refH, minR, maxW, maxH, lastX, lastY;
      private int[] last;
      private List<int[]> hist = new ArrayList<>();
      private Path node = new Path(), link = new Path();
      private Paint nodePaint, linkPaint;

      @Override
      protected void onDraw(Canvas canvas) {
        if (node.isEmpty()) {//init
          ref = Math.min(canvas.getWidth(), canvas.getHeight()) / 100;
          final float r = minR = ref * 3,
            w = refW = canvas.getWidth() / (nX + 1f),
            h = refH = canvas.getHeight() / (nY + 1f);
          maxW = refW - minR;
          maxH = refH - minR;
          nodePaint = new Paint();
          nodePaint.setStyle(Paint.Style.STROKE);
          nodePaint.setStrokeWidth(ref * 2);
          linkPaint = new Paint(nodePaint);
          nodePaint.setColor(Color.BLACK);
          linkPaint.setColor(Color.BLUE);
          for (int i = 0; i < nX; ) {
            final float x = xs[i] = w * ++i;
            for (int j = 0; j < nY; ) ys[j] = h * ++j;
            for (final float y : ys) node.addCircle(x, y, r, Path.Direction.CW);
          }
        }
        canvas.drawPath(node, nodePaint);
        canvas.drawPath(link, linkPaint);
        if (null != last) canvas.drawLine(xs[last[0]], ys[last[1]], lastX, lastY, linkPaint);
        super.onDraw(canvas);
      }

      @Override
      public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            link.reset();
            break;
          case MotionEvent.ACTION_MOVE:
            lastX = event.getX();
            lastY = event.getY();
            final int[] near = getNearPoint();
            if (null != near) {
              final float x = xs[near[0]], y = ys[near[1]];
              if (link.isEmpty()) link.moveTo(x, y);
              else link.lineTo(x, y);
              hist.add(last = near);
            }
            if (null != last) invalidate();
            break;
          case MotionEvent.ACTION_UP:
            if (null != last) {
              final StringBuilder sb = new StringBuilder().append(hist.get(0)[2]);
              for (int i = 1, n = hist.size(); i < n; i++) sb.append("→").append(hist.get(i)[2]);
              Toast.makeText(getContext(), sb, Toast.LENGTH_LONG).show();
              invalidate();
              last = null;
              hist.clear();
            }
          default: return false;
        }
        return true;
      }

      private int[] getNearPoint() {//根據敏感度(minR)找最近點
        final float dx = lastX % refW, dy = lastY % refH;
        if ((dx > minR && dx < maxW) || (dy > minR && dy < maxH)) return null;//outside
        final int px = Math.round(lastX / refW), py = Math.round(lastY / refH), id = py * nX - nX + px - 1;
        return null != last && last[2] == id || px < 1 || px > nX || py < 1 || py > nY ?//invalid
          null : new int[]{px - 1, py - 1, id};
      }
    });
  }
}
