package cn.shyman.refresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.shyman.refresh.recycler.normal.NormalRecyclerActivity;

public class SplashActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		startActivity(new Intent(this, NormalRecyclerActivity.class));
	}
}
