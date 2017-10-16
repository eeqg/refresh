package cn.shyman.refresh.basic;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.shyman.library.refresh.RecyclerAdapter;
import cn.shyman.refresh.R;
import cn.shyman.refresh.bean.StatusInfo;
import cn.shyman.refresh.databinding.ItemBasicLoadingListBinding;
import cn.shyman.refresh.databinding.ItemBasicRefreshListBinding;
import cn.shyman.refresh.databinding.ItemWrapRefreshListBinding;

public abstract class BasicAdapter<AdapterInfo> extends RecyclerAdapter {
	/** 初始页码 */
	private final int initPage = 1;
	/** 当前页码 */
	private int currentPage = initPage;
	
	private AdapterInfo completeAdapterInfo;
	protected AdapterInfo adapterInfo;
	
	/**
	 * 重置数据信息并刷新
	 */
	public final void forceRefresh() {
		resetAdapterInfo(null);
		swipeRefresh();
	}
	
	/**
	 * 获取初始页码
	 *
	 * @return 初始页码
	 */
	public final int getInitPage() {
		return this.initPage;
	}
	
	/**
	 * 获取当前页码
	 *
	 * @return 当前页码
	 */
	public final int getCurrentPage() {
		return this.currentPage;
	}
	
	@Override
	protected void notifyRefreshReady() {
		this.currentPage = this.initPage;
		resetAdapterInfo(null);
		super.notifyRefreshReady();
	}
	
	@Override
	protected void notifyRefresh() {
		this.currentPage = this.initPage;
		super.notifyRefresh();
	}
	
	public void swipeResult(AdapterInfo adapterInfo) {
		this.completeAdapterInfo = adapterInfo;
	}
	
	public void swipeStatus(StatusInfo statusInfo) {
		boolean isRefreshing = this.currentPage == initPage;
		if (statusInfo != null && statusInfo.isSuccessful()) {
			if (isRefreshing) {
				resetAdapterInfo(this.completeAdapterInfo);
			} else if (this.completeAdapterInfo != null) {
				int oldItemCount = getItemCount();
				updateAdapterInfo(this.completeAdapterInfo);
				int newItemCount = getItemCount();
				notifyItemRangeInserted(oldItemCount, newItemCount - oldItemCount);
			}
			super.swipeComplete(statusInfo);
			if (hasMore()) {
				swipeLoadReady();
			}
		} else {
			if (this.currentPage == initPage) {
				resetAdapterInfo(null);
			}
			super.swipeComplete(statusInfo);
		}
		this.completeAdapterInfo = null;
	}
	
	@Override
	protected void notifyRefreshComplete(Object statusInfo) {
		if (statusInfo instanceof StatusInfo) {
			if (((StatusInfo) statusInfo).isSuccessful()) {
				this.currentPage++;
			}
		}
		super.notifyRefreshComplete(statusInfo);
	}
	
	@Override
	protected void notifyLoadComplete(Object statusInfo) {
		if (statusInfo instanceof StatusInfo) {
			if (((StatusInfo) statusInfo).isSuccessful()) {
				this.currentPage++;
			}
		}
		super.notifyLoadComplete(statusInfo);
	}
	
	/**
	 * 重置数据信息
	 *
	 * @param adapterInfo 数据信息
	 */
	protected void resetAdapterInfo(AdapterInfo adapterInfo) {
		this.adapterInfo = adapterInfo;
		notifyDataSetChanged();
	}
	
	/**
	 * 更新数据信息
	 *
	 * @param adapterInfo 数据信息
	 */
	protected abstract void updateAdapterInfo(@NonNull AdapterInfo adapterInfo);
	
	/**
	 * 获取指定位置数据
	 *
	 * @param position 指定位置
	 * @return 数据信息
	 */
	public Object getItem(int position) {
		return null;
	}
	
	/**
	 * 判断是否需加载更多
	 */
	public abstract boolean hasMore();
	
	@SuppressWarnings("unchecked")
	@Override
	public RefreshHolder<StatusInfo> onCreateRefreshHolder() {
		return new RefreshHolder<StatusInfo>() {
			private ItemBasicRefreshListBinding dataBinding;
			
			@Override
			protected View onCreateView(ViewGroup parent) {
				this.dataBinding = DataBindingUtil.inflate(
						LayoutInflater.from(parent.getContext()),
						R.layout.item_basic_refresh_list,
						parent, false
				);
				return this.dataBinding.getRoot();
			}
			
			@Override
			protected void onRefreshReady() {
				this.dataBinding.ivStatus.setVisibility(View.GONE);
				this.dataBinding.tvStatus.setText(R.string.refresh_ready);
			}
			
			@Override
			protected void onRefresh() {
				this.dataBinding.ivStatus.setVisibility(View.VISIBLE);
				this.dataBinding.tvStatus.setText(R.string.refreshing);
			}
			
			@Override
			protected void onRefreshComplete(StatusInfo statusInfo) {
				this.dataBinding.ivStatus.setVisibility(View.GONE);
				if (statusInfo == null) {
					this.dataBinding.tvStatus.setText(R.string.network_error);
				} else if (!statusInfo.isSuccessful()) {
					this.dataBinding.tvStatus.setText(R.string.complete_failure);
				} else {
					this.dataBinding.tvStatus.setText(R.string.data_empty);
				}
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LoadHolder<StatusInfo> onCreateLoadHolder() {
		return new LoadHolder<StatusInfo>() {
			private ItemBasicLoadingListBinding dataBinding;
			
			@Override
			protected View onCreateView(ViewGroup parent) {
				this.dataBinding = DataBindingUtil.inflate(
						LayoutInflater.from(parent.getContext()),
						R.layout.item_basic_loading_list,
						parent, false
				);
				return this.dataBinding.getRoot();
			}
			
			@Override
			protected void onLoadReady() {
				this.dataBinding.tvStatus.setText(R.string.load_ready);
			}
			
			@Override
			protected void onLoading() {
				this.dataBinding.tvStatus.setText(R.string.loading);
			}
			
			@Override
			protected boolean onLoadComplete(StatusInfo statusInfo) {
				if (statusInfo == null) {
					this.dataBinding.tvStatus.setText(R.string.network_error);
					return true;
				} else if (!statusInfo.isSuccessful()) {
					this.dataBinding.tvStatus.setText(R.string.load_failure);
					return true;
				} else {
					this.dataBinding.tvStatus.setText(R.string.load_complete);
					return false;
				}
			}
		};
	}
	
	public static class WrapRefreshHolder extends RefreshHolder<StatusInfo> {
		private ItemWrapRefreshListBinding dataBinding;
		
		@Override
		protected View onCreateView(ViewGroup parent) {
			this.dataBinding = DataBindingUtil.inflate(
					LayoutInflater.from(parent.getContext()),
					R.layout.item_wrap_refresh_list,
					parent, false
			);
			return this.dataBinding.getRoot();
		}
		
		public void notifyRefreshReady() {
			onRefreshReady();
		}
		
		@Override
		protected void onRefreshReady() {
			this.dataBinding.tvStatus.setText(R.string.refresh_ready);
		}
		
		public void notifyRefresh() {
			onRefresh();
		}
		
		@Override
		protected void onRefresh() {
			this.dataBinding.tvStatus.setText(R.string.refreshing);
		}
		
		public void notifyRefreshComplete(StatusInfo statusInfo) {
			onRefreshComplete(statusInfo);
		}
		
		@Override
		protected void onRefreshComplete(StatusInfo statusInfo) {
			if (statusInfo == null) {
				this.dataBinding.tvStatus.setText(R.string.network_error);
			} else if (!statusInfo.isSuccessful()) {
				this.dataBinding.tvStatus.setText(R.string.complete_failure);
			} else {
				this.dataBinding.tvStatus.setText(R.string.data_empty);
			}
		}
	}
}
