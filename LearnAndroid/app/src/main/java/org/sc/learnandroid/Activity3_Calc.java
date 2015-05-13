package org.sc.learnandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;

public class Activity3_Calc extends Activity {
  private Editable txt;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity3_calc);
    final EditText txt = (EditText) findViewById(R.id.txt_ans);
    txt.setKeyListener(null);
    this.txt = txt.getText();
    final ViewGroup table = (ViewGroup) findViewById(R.id.lay_main);
    for (final Cmd[] cmds : CMDS) {
      final TableRow row = new TableRow(this);
      for (final Cmd cmd : cmds) row.addView(cmd.newButton());
      table.addView(row);
    }
  }

  abstract class Cmd implements View.OnClickListener {
    private final int color;
    final String text;

    Cmd(String text, int color) {
      this.color = color;
      this.text = text;
    }

    final Button newButton() {
      final Button button = new Button(Activity3_Calc.this);
      button.setText(text);
      button.setTextSize(30);
      button.setOnClickListener(this);
      button.setTextColor(COLOR[color]);
      return button;
    }

    public String toString() {
      return getClass().getSimpleName() + "(" + text + ')';
    }
  }

  abstract class Op extends Cmd {
    final int priority;

    Op(String text, int priority) {
      super(text, 1);
      this.priority = priority;
    }

    public void onClick(View v) {txt.append(text); }

    abstract double eval(double x, double y);
  }

  class Num extends Cmd {
    Num(String text) {super(text, 0); }

    public void onClick(View v) {txt.append(text);}
  }

  private static final int[] COLOR = {0xff333333, 0xff3399AA, 0xff33AA99};
  private final Op SUM = new Op("+", 1) {
    double eval(double x, double y) { return x + y; }
  }, MINUS = new Op("−", 1) {
    double eval(double x, double y) { return x - y; }
  }, MULTIPLY = new Op("×", 2) {
    double eval(double x, double y) { return x * y; }
  }, DIVIDE = new Op("÷", 2) {
    double eval(double x, double y) { return x / y; }
  };
  private final Cmd[][] CMDS = {{new Cmd("C", 1) {
    public void onClick(View v) { txt.clear(); }
  }, DIVIDE, MULTIPLY, new Cmd("◁", 1) {
    public void onClick(View v) {
      final int n = txt.length();
      if (n > 0) txt.delete(n - 1, n);
    }
  }}, {new Num("7"), new Num("8"), new Num("9"), MINUS},
    {new Num("4"), new Num("5"), new Num("6"), SUM},
    {new Num("1"), new Num("2"), new Num("3"), new Cmd("()", 1) {
      public void onClick(View v) {
        int n = txt.length(), all = 0, end = 0, ch = 0;
        for (int i = 0; i < n; i++)
          switch (ch = txt.charAt(i)) {
            case ')': end++;
            case '(': all++;
          }
        if (0 == n || '(' == ch || '+' == ch || '−' == ch || '×' == ch || '÷' == ch)
          txt.append('(');
        else if (all - end > end) txt.append(')');
        else txt.append("×(");
      }
    }}, {new Num("0"), new Num(".") {
    public void onClick(View v) {
      int n = txt.length(), ch;
      while (n-- > 0)
        if ('.' == (ch = txt.charAt(n))) return;//fail
        else if (ch < '0' || ch > '9') break;//pass
      super.onClick(v);
    }
  }, new Cmd("+/-", 1) {
    public void onClick(View v) {
      int n = txt.length(), ch = 0;
      while (n-- > 0) if ((ch = txt.charAt(n)) < '.' || ch > '9') break;
      if ('-' == ch) txt.delete(n - 1, n + 1);
      else txt.insert(n + 1, "(-");
    }
  }, new Cmd("=", 2) {

    public void onClick(View v) {
//      final Deque<Op> ops = new ArrayDeque<>();
//      final Deque<Double> out = new ArrayDeque<>();
//      int p = 0, n = txt.length();
//      for (int i = p; i < n; i++) {
//        System.out.println("out=" + out);
//        System.out.println("ops=" + ops);
//        final Op op;
//        switch (txt.charAt(i)) {
//          case '+': op = SUM; break;
//          case '−': op = MINUS; break;//negative='-' not '−'
//          case '×': op = MULTIPLY; break;
//          case '÷': op = DIVIDE; break;
//          case '(': ops.push(null); break;
//          case ')':
//            for (Op last; null != (last = ops.pollLast()); )
//              out.add(last.eval(out.pollLast(), out.pollLast()));
//            break;
//          default: continue;//number
//        }
//        if (i > p) out.add(Double.parseDouble(txt.subSequence(p, i).toString()));
//        p = i + 1;//next
//        for(Op last;null!=(last=ops.peekLast()) && last.priority>=op.priority;)
//          ops.addLast();
//        out.peekLast()
//      }
//      if (n > p) out.add(Double.parseDouble(txt.subSequence(p, n).toString()));
//      while (!ops.isEmpty()) out.add(ops.pollLast().eval(out.pollLast(), out.pollLast()));
      txt.clear();
      txt.append("Not Supported Yet!");
    }
  }}};

}
