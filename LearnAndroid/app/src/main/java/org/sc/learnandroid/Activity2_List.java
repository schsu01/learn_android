package org.sc.learnandroid;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Activity2_List extends Activity {
  private final List<Record> data = new ArrayList<>();
  private DatePicker pickDate;
  private TimePicker pickTime;
  private RadioGroup optFlag;
  private TextView btnSave, txtNum;
  private ListView list;

  private static class Record {
    static Resources RES;
    private final int year, month, day, hour, min, flag;
    private final CharSequence value;

    public static Record getNowInstance() {
      final GregorianCalendar now = (GregorianCalendar) GregorianCalendar.getInstance();
      return new Record(now.get(GregorianCalendar.YEAR), now.get(GregorianCalendar.MONTH),
        now.get(GregorianCalendar.DAY_OF_MONTH), now.get(GregorianCalendar.HOUR_OF_DAY),
        now.get(GregorianCalendar.MINUTE), 0, null);
    }

    private Record(int year, int month, int day, int hour, int min, int flag, CharSequence value) {
      this.year = year;
      this.month = month;
      this.day = day;
      this.hour = hour;
      this.min = min;
      this.flag = flag;
      this.value = value;
    }

    public String toString() {
      return String.valueOf(year) + '-' + month + '-' + day + ' ' + hour + ':' + min + ' ' + RES.getString(
        0 == flag ? R.string.opt_before : R.string.opt_after) + " = " + value;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity2_list);
    Record.RES = getResources();
    final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    pickDate = (DatePicker) findViewById(R.id.pick_date);
    applyLayoutParam(NumberPicker.class, pickDate, param);

    (pickTime = (TimePicker) findViewById(R.id.pick_time)).setIs24HourView(true);
    applyLayoutParam(NumberPicker.class, pickTime, param);

    txtNum = (TextView) findViewById(R.id.txt_num);
    optFlag = (RadioGroup) findViewById(R.id.rdoFlag);

    final View.OnClickListener action = new View.OnClickListener() {
      public void onClick(View v) {
        final Integer id = (Integer) v.getTag();
        if (null != id) saveData(id);
        chooseData(-1);
      }
    };

    (btnSave = (TextView) findViewById(R.id.btn_save)).setOnClickListener(action);

    list = (ListView) findViewById(R.id.list);
    list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        chooseData(position);
      }
    });

    chooseData(-1);
  }

  private void saveData(int id) {
    final Record item = new Record(
      pickDate.getYear(), pickDate.getMonth(), pickDate.getDayOfMonth(),
      pickTime.getCurrentHour(), pickTime.getCurrentMinute(),
      optFlag.getCheckedRadioButtonId() == R.id.rdo_before ? 0 : 1, txtNum.getText());
    if (id < 0) data.add(item);
    else data.set(id, item);
    ((ArrayAdapter) list.getAdapter()).notifyDataSetChanged();
  }

  private void chooseData(int id) {
    btnSave.setTag(id);
    final Record item = id < 0 ? Record.getNowInstance() : data.get(id);
    pickDate.updateDate(item.year, item.month, item.day);
    pickTime.setCurrentHour(item.hour);
    pickTime.setCurrentMinute(item.min);
    txtNum.setText(item.value);
    optFlag.check(0 == item.flag ? R.id.rdo_before : R.id.rdo_after);
  }

  private static void applyLayoutParam(Class target, ViewGroup group, ViewGroup.LayoutParams param) {
    for (int i = 0, n = group.getChildCount(); i < n; i++) {
      final View view = group.getChildAt(i);
      if (target.isAssignableFrom(view.getClass())) view.setLayoutParams(param);
      if (view instanceof ViewGroup) applyLayoutParam(target, (ViewGroup) view, param);
    }
  }
}