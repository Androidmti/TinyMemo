package com.mti.ad220_project_02_db;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity {
	private static int SPLASH_TIMER = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		// Display splash screen for 3 seconds, after that
		// finish splash screen activity and open main activity
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(mainIntent);
                StartActivity.this.finish();
            }
        }, SPLASH_TIMER);
	}
}
