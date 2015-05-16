package org.sc.learnandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
  private static final Class[] APP_KLASS = {Activity1_OOXX.class, Activity2_List.class,
      Activity3_Calc.class, Activity4_Draw.class, Activity5_1010.class};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final ListView list = (ListView) findViewById(R.id.list);
    list.setOnItemClickListener(this);
    list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getTextArray(R.array.app_list)));
  }

  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    final Intent intent = new Intent();
    intent.setClass(this, APP_KLASS[position]);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
  }

}
