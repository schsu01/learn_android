package org.sc.learnandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setRedirectTo(R.id.app1_ooxx, Activity1_OOXX.class);
    setRedirectTo(R.id.app2_list, Activity2_List.class);
    setRedirectTo(R.id.app3_calc, Activity3_Calc.class);
  }

  private void setRedirectTo(int id, Class klass) {
    final View view = findViewById(id);
    view.setTag(klass);
    view.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    final Intent intent = new Intent();
    intent.setClass(this, (Class) v.getTag());
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
