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

import org.sc.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class Activity4_Draw extends Activity {
  private static final int nX = 3, nY = 5;//設定行列數

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(new View(this) {
      private float[] xs, ys;
      private float ref, refW, refH, minRsq, lastX, lastY;
      private int[] last;
      private List<int[]> hist = new ArrayList<>();
      private Path node = new Path(), link = new Path();
      private Paint nodeP, linkP, lastP;

      @Override
      protected void onDraw(Canvas canvas) {
        if (node.isEmpty()) {//init
          ref = Math.min(canvas.getWidth(), canvas.getHeight()) / 100;
          nodeP = ViewUtil.newPaint(Paint.Style.STROKE, ref * 2, Color.BLACK);
          linkP = new Paint(nodeP); linkP.setColor(Color.BLUE);
          lastP = new Paint(nodeP); lastP.setColor(Color.MAGENTA);
          final float r = ref * 4, w = refW = canvas.getWidth() / (nX + 1f), h = refH = canvas.getHeight() / (nY + 1f);
          minRsq = r * r; xs = new float[nX]; ys = new float[nY];
          for (int i = 0; i < nX; ) {
            final float x = xs[i] = w * ++i;
            for (int j = 0; j < nY; ) ys[j] = h * ++j;
            for (final float y : ys) node.addCircle(x, y, r, Path.Direction.CW);
          }
        }
        canvas.drawPath(node, nodeP);
        canvas.drawPath(link, linkP);
        if (null != last) canvas.drawLine(xs[last[0]], ys[last[1]], lastX, lastY, lastP);
        super.onDraw(canvas);
      }

      @Override
      public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            link.reset();
            break;
          case MotionEvent.ACTION_MOVE:
            final float x = lastX = event.getX(), y = lastY = event.getY(), dx, dy;
            final int px = (int) (x / refW + 0.5f), py = (int) (y / refH + 0.5f), id = py * nX - nX + px - 1;
            if (null != (null != last && last[2] == id || px < 1 || px > nX || py < 1 || py > nY ||
              (dx = x - px * refW) * dx + (dy = y - py * refH) * dy > minRsq ? //根據敏感度(minRsq)找最近點
              null : (last = new int[]{px - 1, py - 1, id}))) {
              hist.add(last);
              final float nx = xs[last[0]], ny = ys[last[1]];
              if (link.isEmpty()) link.moveTo(nx, ny);
              else link.lineTo(nx, ny);
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
    });
  }
}
