package cn.shyman.refresh.scroll;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.shyman.library.refresh.OnTaskListener;
import cn.shyman.refresh.R;
import cn.shyman.refresh.bean.StatusInfo;
import cn.shyman.refresh.databinding.ActivityScrollBinding;

public class ScrollActivity extends AppCompatActivity {
	private ActivityScrollBinding dataBinding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_scroll);
		
		observeContent();
		observeRefreshReady();
		observeRefreshing();
		observeCompleteSuccess();
		observeCompleteFailure();
		observeCompleteError();
		observeForceRefresh();
	}
	
	/**
	 * 内容
	 */
	private void observeContent() {
		this.dataBinding.refreshLayout.setOnTaskListener(new OnTaskListener<Integer>() {
			@Override
			public Integer onTask() {
				return 0;
			}
			
			@Override
			public void onCancel(Integer integer) {
				
			}
		});
	}
	
	/**
	 * 准备刷新
	 */
	private void observeRefreshReady() {
		this.dataBinding.setRefreshReadyClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dataBinding.refreshLayout.swipeRefreshReady();
			}
		});
	}
	
	/**
	 * 刷新中
	 */
	private void observeRefreshing() {
		this.dataBinding.setRefreshingClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dataBinding.refreshLayout.swipeRefresh();
			}
		});
	}
	
	/**
	 * 请求成功
	 */
	private void observeCompleteSuccess() {
		this.dataBinding.setCompleteSuccessClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dataBinding.refreshLayout.swipeComplete(new StatusInfo());
			}
		});
	}
	
	/**
	 * 请求失败
	 */
	private void observeCompleteFailure() {
		this.dataBinding.setCompleteFailureClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dataBinding.refreshLayout.swipeComplete(new StatusInfo(StatusInfo.FAILURE));
			}
		});
	}
	
	/**
	 * 请求错误
	 */
	private void observeCompleteError() {
		this.dataBinding.setCompleteErrorClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dataBinding.refreshLayout.swipeComplete();
			}
		});
	}
	
	/**
	 * 强制刷新
	 */
	private void observeForceRefresh() {
		this.dataBinding.setForceRefreshClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dataBinding.refreshLayout.forceRefresh();
			}
		});
	}
}
