package cn.shyman.refresh.nested;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.shyman.library.refresh.OnTaskListener;
import cn.shyman.refresh.R;
import cn.shyman.refresh.bean.StatusInfo;
import cn.shyman.refresh.databinding.ActivityCoordinatorNestedScrollBinding;

public class CoordinatorNestedScrollActivity extends AppCompatActivity {
	private ActivityCoordinatorNestedScrollBinding dataBinding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_coordinator_nested_scroll);
		
		observeContent();
	}
	
	/**
	 * 内容
	 */
	private void observeContent() {
		this.dataBinding.refreshLayout.setOnTaskListener(new OnTaskListener<Integer>() {
			@Override
			public Integer onTask() {
				dataBinding.refreshLayout.swipeComplete(new StatusInfo());
				return 0;
			}
			
			@Override
			public void onCancel(Integer integer) {
			
			}
		});
	}
}
