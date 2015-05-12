package org.sc.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ViewUtil {

  public static void applyOptions(RadioGroup radio, String... options) {
    final Context context = radio.getContext();
    RadioButton btn;
    for (int i = 0, n = options.length; i < n; i++) {
      radio.addView(btn = new RadioButton(context));
      btn.setId(i);
      btn.setText(options[i]);
    }
  }

  public static void applyLayout(ViewGroup group, ViewGroup.LayoutParams param, Class targetToApply) {
    for (int i = 0, n = group.getChildCount(); i < n; i++) {
      final View view = group.getChildAt(i);
      if (targetToApply.isAssignableFrom(view.getClass())) view.setLayoutParams(param);
      if (view instanceof ViewGroup) applyLayout((ViewGroup) view, param, targetToApply);
    }
  }

}
