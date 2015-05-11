package org.sc.learnandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Activity1_OOXX extends Activity {
  private static final int[][] RULE = {{0, 4, 8}, {2, 4, 6}, {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}};
  private final TextView[] plays = new TextView[9];
  private int step;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity1_ooxx);
    findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        step = 0;
        for (final TextView play : plays) {
          play.setText(null);
          play.setTextColor(0xff000000);
        }
      }
    });
    final ViewGroup table = (ViewGroup) findViewById(R.id.lay_ooxx);
    for (int i = 0, k = 0; i < 3; i++) {
      final TableRow row = new TableRow(this);
      for (int j = 0; j < 3; j++) {
        final int index = k++;
        final TextView button = plays[index] = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            if (step < 0 || button.getText().length() > 0) return;//skip
            final String player = 0 == (step++ & 1) ? "O" : "X";
            button.setText(player);
            check:
            for (final int[] rule : RULE) {
              for (final int id : rule)
                if (!(id == index || player.equals(plays[id].getText()))) continue check;
              for (final int id : rule) plays[id].setTextColor(0xffff0000);
              Toast.makeText(button.getContext(), "Winner is <" + player + "> !", Toast.LENGTH_LONG).show();
              step = -1;
              break;
            }
            if (9 == step) {
              Toast.makeText(button.getContext(), "Draw Game ...", Toast.LENGTH_LONG).show();
              step = -1;
            }
          }

        });
        row.addView(button);
      }
      table.addView(row);
    }
  }
}
