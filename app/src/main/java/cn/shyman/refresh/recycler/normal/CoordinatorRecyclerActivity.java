package cn.shyman.refresh.recycler.normal;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cn.shyman.library.refresh.OnTaskListener;
import cn.shyman.refresh.R;
import cn.shyman.refresh.bean.ColorListBean;
import cn.shyman.refresh.bean.StatusInfo;
import cn.shyman.refresh.databinding.ActivityCoordinatorRecyclerBinding;
import cn.shyman.refresh.databinding.ActivityNormalRecyclerBinding;

public class CoordinatorRecyclerActivity extends AppCompatActivity {
	private ActivityCoordinatorRecyclerBinding dataBinding;
	
	private RecyclerSettingDialog recyclerSettingDialog;
	
	private RecyclerListAdapter recyclerListAdapter;
	
	/** 列表数量 */
	private int listCount;
	/** 页面数量 */
	private int pageCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_coordinator_recycler);
		
		this.listCount = Integer.parseInt(getString(R.string.the_default_number_of_data));
		this.pageCount = Integer.parseInt(getString(R.string.the_default_number_of_page));
		
		observeList();
		observeRefreshReady();
		observeRefreshing();
		observeCompleteSuccess();
		observeCompleteFailure();
		observeCompleteError();
		observeForceRefresh();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_setting, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (this.recyclerSettingDialog == null) {
			this.recyclerSettingDialog = new RecyclerSettingDialog(this,
					new RecyclerSettingDialog.OnSettingListener() {
						@Override
						public void onSetting(int loadMode, int listCount, int pageCount) {
							recyclerListAdapter.setLoadMode(loadMode);
							CoordinatorRecyclerActivity.this.listCount = listCount;
							CoordinatorRecyclerActivity.this.pageCount = pageCount;
						}
					});
		}
		this.recyclerSettingDialog.show();
		return true;
	}
	
	/**
	 * 列表
	 */
	private void observeList() {
		this.dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		this.recyclerListAdapter = new RecyclerListAdapter(this);
		this.recyclerListAdapter.setRefreshLayout(this.dataBinding.refreshLayout);
		this.recyclerListAdapter.setRecyclerView(this.dataBinding.recyclerView);
		this.recyclerListAdapter.setOnTaskListener(new OnTaskListener<Integer>() {
			@Override
			public Integer onTask() {
				return recyclerListAdapter.getCurrentPage();
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
				recyclerListAdapter.swipeRefreshReady();
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
				recyclerListAdapter.swipeRefresh();
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
				ColorListBean colorListBean = ColorListBean.create(listCount, pageCount);
				recyclerListAdapter.swipeResult(colorListBean);
				recyclerListAdapter.swipeStatus(colorListBean.statusInfo);
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
				recyclerListAdapter.swipeStatus(new StatusInfo(StatusInfo.FAILURE));
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
				recyclerListAdapter.swipeStatus(new StatusInfo(StatusInfo.NETWORK_ERROR));
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
				recyclerListAdapter.forceRefresh();
			}
		});
	}
}
