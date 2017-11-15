package cn.shyman.refresh;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.shyman.refresh.databinding.ActivitySplashBinding;
import cn.shyman.refresh.nested.NestedScrollActivity;
import cn.shyman.refresh.recycler.normal.NormalRecyclerActivity;
import cn.shyman.refresh.scroll.ScrollActivity;

public class SplashActivity extends AppCompatActivity {
	private ActivitySplashBinding dataBinding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
		
		observeScrollView();
		observeNestedScrollView();
		observeNormalRecycler();
	}
	
	/**
	 * ScrollView
	 */
	private void observeScrollView() {
		this.dataBinding.setScrollClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SplashActivity.this, ScrollActivity.class));
			}
		});
	}
	
	/**
	 * NestedScrollView
	 */
	private void observeNestedScrollView() {
		this.dataBinding.setNestedScrollClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SplashActivity.this, NestedScrollActivity.class));
			}
		});
	}
	
	/**
	 * RecyclerView - Normal
	 */
	private void observeNormalRecycler() {
		this.dataBinding.setNormalRecyclerClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SplashActivity.this, NormalRecyclerActivity.class));
			}
		});
	}
}
