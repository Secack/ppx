package com.akari.ppx.ui.channel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.akari.ppx.R;
import com.akari.ppx.common.entity.ChannelEntity;
import com.akari.ppx.common.utils.ChannelUtils;
import com.akari.ppx.common.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static com.akari.ppx.common.constant.Prefs.MY_CHANNEL;
import static com.akari.ppx.common.constant.Prefs.OTHER_CHANNEL;

public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {
	public static final int TYPE_MY_CHANNEL_HEADER = 0;
	public static final int TYPE_MY = 1;
	public static final int TYPE_OTHER_CHANNEL_HEADER = 2;
	public static final int TYPE_OTHER = 3;

	private static final int COUNT_PRE_MY_HEADER = 1;
	private static final int COUNT_PRE_OTHER_HEADER = COUNT_PRE_MY_HEADER + 1;
	private static final long ANIM_TIME = 360L;
	private static final long SPACE_TIME = 100;
	private final LayoutInflater inflater;
	private final ItemTouchHelper itemTouchHelper;
	private final List<ChannelEntity> myChannelItems;
	private final List<ChannelEntity> otherChannelItems;
	private final Handler delayHandler = new Handler();
	private long startTime;
	private boolean isEditMode;
	private OnMyChannelItemClickListener mChannelItemClickListener;

	public ChannelAdapter(Context context, ItemTouchHelper helper, List<ChannelEntity> myChannelItems, List<ChannelEntity> otherChannelItems) {
		inflater = LayoutInflater.from(context);
		itemTouchHelper = helper;
		this.myChannelItems = myChannelItems;
		this.otherChannelItems = otherChannelItems;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return TYPE_MY_CHANNEL_HEADER;
		else if (position == myChannelItems.size() + 1)
			return TYPE_OTHER_CHANNEL_HEADER;
		else if (position > 0 && position < myChannelItems.size() + 1)
			return TYPE_MY;
		else return TYPE_OTHER;
	}

	@Override
	public int getItemCount() {
		return myChannelItems.size() + otherChannelItems.size() + COUNT_PRE_OTHER_HEADER;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NotNull final ViewGroup parent, int viewType) {
		final View view;
		switch (viewType) {
			case TYPE_MY_CHANNEL_HEADER:
				view = inflater.inflate(R.layout.item_my_channel_header, parent, false);
				final MyChannelHeaderViewHolder holder = new MyChannelHeaderViewHolder(view);
				holder.tvBtnEdit.setOnClickListener(v -> {
					if (!isEditMode) {
						startEditMode((RecyclerView) parent);
						holder.tvBtnEdit.setText(R.string.finish);
					} else {
						cancelEditMode((RecyclerView) parent);
						holder.tvBtnEdit.setText(R.string.edit);
					}
				});
				return holder;
			case TYPE_MY:
				view = inflater.inflate(R.layout.item_my, parent, false);
				final MyViewHolder myHolder = new MyViewHolder(view);
				myHolder.textView.setOnClickListener(v -> {
					int position = myHolder.getAdapterPosition();
					if (isEditMode) {
						RecyclerView recyclerView = ((RecyclerView) parent);
						View targetView = Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(myChannelItems.size() + COUNT_PRE_OTHER_HEADER);
						View currentView = recyclerView.getLayoutManager().findViewByPosition(position);
						if (recyclerView.indexOfChild(targetView) >= 0) {
							int targetX, targetY;
							RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
							int spanCount = ((GridLayoutManager) manager).getSpanCount();
							if ((myChannelItems.size() - COUNT_PRE_MY_HEADER) % spanCount == 0) {
								View preTargetView = recyclerView.getLayoutManager().findViewByPosition(myChannelItems.size() + COUNT_PRE_OTHER_HEADER - 1);
								targetX = Objects.requireNonNull(preTargetView).getLeft();
								targetY = preTargetView.getTop();
							} else {
								targetX = Objects.requireNonNull(targetView).getLeft();
								targetY = targetView.getTop();
							}
							moveMyToOther(myHolder);
							startAnimation(recyclerView, currentView, targetX, targetY);

						} else {
							moveMyToOther(myHolder);
						}
					} else {
						mChannelItemClickListener.onItemClick(v, position - COUNT_PRE_MY_HEADER);
					}
				});
				myHolder.textView.setOnLongClickListener(v -> {
					if (!isEditMode) {
						RecyclerView recyclerView = ((RecyclerView) parent);
						startEditMode(recyclerView);
						View view1 = recyclerView.getChildAt(0);
						if (view1 == Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(0)) {
							TextView tvBtnEdit = view1.findViewById(R.id.tv_btn_edit);
							tvBtnEdit.setText(R.string.finish);
						}
					}
					itemTouchHelper.startDrag(myHolder);
					return true;
				});
				myHolder.textView.setOnTouchListener((v, event) -> {
					if (isEditMode) {
						switch (MotionEventCompat.getActionMasked(event)) {
							case MotionEvent.ACTION_DOWN:
								startTime = System.currentTimeMillis();
								break;
							case MotionEvent.ACTION_MOVE:
								if (System.currentTimeMillis() - startTime > SPACE_TIME) {
									itemTouchHelper.startDrag(myHolder);
								}
								break;
							case MotionEvent.ACTION_CANCEL:
							case MotionEvent.ACTION_UP:
								startTime = 0;
								break;
						}
					}
					return false;
				});
				return myHolder;
			case TYPE_OTHER_CHANNEL_HEADER:
				view = inflater.inflate(R.layout.item_other_channel_header, parent, false);
				return new RecyclerView.ViewHolder(view) {
				};
			case TYPE_OTHER:
				view = inflater.inflate(R.layout.item_other, parent, false);
				final OtherViewHolder otherHolder = new OtherViewHolder(view);
				otherHolder.textView.setOnClickListener(v -> {
					if (!isEditMode) {
						RecyclerView recyclerView = ((RecyclerView) parent);
						startEditMode(recyclerView);
						View view1 = recyclerView.getChildAt(0);
						if (view1 == Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(0)) {
							TextView tvBtnEdit = view1.findViewById(R.id.tv_btn_edit);
							tvBtnEdit.setText(R.string.finish);
						}
					}
					RecyclerView recyclerView = ((RecyclerView) parent);
					RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
					int currentPosition = otherHolder.getAdapterPosition();
					View currentView = Objects.requireNonNull(manager).findViewByPosition(currentPosition);
					View preTargetView = manager.findViewByPosition(myChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);
					if (recyclerView.indexOfChild(preTargetView) >= 0) {
						int targetX = Objects.requireNonNull(preTargetView).getLeft();
						int targetY = preTargetView.getTop();
						int targetPosition = myChannelItems.size() - 1 + COUNT_PRE_OTHER_HEADER;
						GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
						int spanCount = gridLayoutManager.getSpanCount();
						if ((targetPosition - COUNT_PRE_MY_HEADER) % spanCount == 0) {
							View targetView = manager.findViewByPosition(targetPosition);
							targetX = Objects.requireNonNull(targetView).getLeft();
							targetY = targetView.getTop();
						} else {
							targetX += preTargetView.getWidth();
							if (gridLayoutManager.findLastVisibleItemPosition() == getItemCount() - 1) {
								if ((getItemCount() - 1 - myChannelItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount == 0) {
									int firstVisiblePostion = gridLayoutManager.findFirstVisibleItemPosition();
									if (firstVisiblePostion == 0) {
										if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
											int offset = (-recyclerView.getChildAt(0).getTop()) - recyclerView.getPaddingTop();
											targetY += offset;
										}
									} else
										targetY += preTargetView.getHeight();
								}
							}
						}
						if (currentPosition == gridLayoutManager.findLastVisibleItemPosition()
								&& (currentPosition - myChannelItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount != 0
								&& (targetPosition - COUNT_PRE_MY_HEADER) % spanCount != 0) {
							moveOtherToMyWithDelay(otherHolder);
						} else {
							moveOtherToMy(otherHolder);
						}
						startAnimation(recyclerView, currentView, targetX, targetY);
					} else {
						moveOtherToMy(otherHolder);
					}
				});
				return otherHolder;
		}
		return null;
	}

	@Override
	public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof MyViewHolder) {
			MyViewHolder myHolder = (MyViewHolder) holder;
			myHolder.textView.setText(myChannelItems.get(position - COUNT_PRE_MY_HEADER).getPureName());
			myHolder.imgEdit.setVisibility(isEditMode ? View.VISIBLE : View.INVISIBLE);
		} else if (holder instanceof OtherViewHolder) {
			((OtherViewHolder) holder).textView.setText(otherChannelItems.get(position - myChannelItems.size() - COUNT_PRE_OTHER_HEADER).getPureName());
		} else if (holder instanceof MyChannelHeaderViewHolder) {
			MyChannelHeaderViewHolder headerHolder = (MyChannelHeaderViewHolder) holder;
			headerHolder.tvBtnEdit.setText(isEditMode ? R.string.finish : R.string.edit);
		}
	}

	private void startAnimation(RecyclerView recyclerView, final View currentView, float targetX, float targetY) {
		final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
		final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);
		Animation animation = getTranslateAnimator(targetX - currentView.getLeft(), targetY - currentView.getTop());
		currentView.setVisibility(View.INVISIBLE);
		mirrorView.startAnimation(animation);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				viewGroup.removeView(mirrorView);
				if (currentView.getVisibility() == View.INVISIBLE)
					currentView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
	}

	private void moveMyToOther(MyViewHolder myHolder) {
		int position = myHolder.getAdapterPosition();
		int startPosition = position - COUNT_PRE_MY_HEADER;
		if (startPosition > myChannelItems.size() - 1) return;
		ChannelEntity item = myChannelItems.get(startPosition);
		myChannelItems.remove(startPosition);
		otherChannelItems.add(0, item);
		notifyItemMoved(position, myChannelItems.size() + COUNT_PRE_OTHER_HEADER);
	}

	private void moveOtherToMy(OtherViewHolder otherHolder) {
		int position = processItemRemoveAdd(otherHolder);
		if (position == -1) return;
		notifyItemMoved(position, myChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);
	}

	private void moveOtherToMyWithDelay(OtherViewHolder otherHolder) {
		final int position = processItemRemoveAdd(otherHolder);
		if (position == -1) return;
		delayHandler.postDelayed(() -> notifyItemMoved(position, myChannelItems.size() - 1 + COUNT_PRE_MY_HEADER), ANIM_TIME);
	}

	private int processItemRemoveAdd(OtherViewHolder otherHolder) {
		int position = otherHolder.getAdapterPosition();
		int startPosition = position - myChannelItems.size() - COUNT_PRE_OTHER_HEADER;
		if (startPosition > otherChannelItems.size() - 1) return -1;
		ChannelEntity item = otherChannelItems.get(startPosition);
		otherChannelItems.remove(startPosition);
		myChannelItems.add(item);
		return position;
	}

	private ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		final ImageView mirrorView = new ImageView(recyclerView.getContext());
		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
		mirrorView.setImageBitmap(bitmap);
		view.setDrawingCacheEnabled(false);
		int[] locations = new int[2];
		view.getLocationOnScreen(locations);
		int[] parenLocations = new int[2];
		recyclerView.getLocationOnScreen(parenLocations);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
		params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
		parent.addView(mirrorView, params);
		return mirrorView;
	}

	@Override
	public void onItemMove(int fromPosition, int toPosition) {
		ChannelEntity item = myChannelItems.get(fromPosition - COUNT_PRE_MY_HEADER);
		myChannelItems.remove(fromPosition - COUNT_PRE_MY_HEADER);
		myChannelItems.add(toPosition - COUNT_PRE_MY_HEADER, item);
		notifyItemMoved(fromPosition, toPosition);
	}

	private void startEditMode(RecyclerView parent) {
		isEditMode = true;
		int visibleChildCount = parent.getChildCount();
		for (int i = 0; i < visibleChildCount; i++) {
			View view = parent.getChildAt(i);
			ImageView imgEdit = view.findViewById(R.id.img_edit);
			if (imgEdit != null)
				imgEdit.setVisibility(View.VISIBLE);
		}
	}

	private void cancelEditMode(RecyclerView parent) {
		isEditMode = false;
		int visibleChildCount = parent.getChildCount();
		for (int i = 0; i < visibleChildCount; i++) {
			View view = parent.getChildAt(i);
			ImageView imgEdit = view.findViewById(R.id.img_edit);
			if (imgEdit != null)
				imgEdit.setVisibility(View.INVISIBLE);
		}
		Context context = parent.getContext();
		ChannelUtils.setSPDataList(MY_CHANNEL, myChannelItems);
		ChannelUtils.setSPDataList(OTHER_CHANNEL, otherChannelItems);
		Utils.setPreferenceWorldWritable(context);
		Toast.makeText(context, "配置已保存", Toast.LENGTH_SHORT).show();
	}

	private TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.ABSOLUTE, targetX,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.ABSOLUTE, targetY);
		translateAnimation.setDuration(ANIM_TIME);
		translateAnimation.setFillAfter(true);
		return translateAnimation;
	}

	public void setOnMyChannelItemClickListener(OnMyChannelItemClickListener listener) {
		mChannelItemClickListener = listener;
	}

	interface OnMyChannelItemClickListener {
		void onItemClick(View v, int position);
	}

	static class MyViewHolder extends RecyclerView.ViewHolder implements OnDragVHListener {
		private final TextView textView;
		private final ImageView imgEdit;

		public MyViewHolder(View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.tv);
			imgEdit = itemView.findViewById(R.id.img_edit);
		}

		@Override
		public void onItemSelected() {
			textView.setBackgroundResource(R.drawable.bg_channel_p);
		}

		@Override
		public void onItemFinish() {
			textView.setBackgroundResource(R.drawable.bg_channel);
		}
	}

	static class OtherViewHolder extends RecyclerView.ViewHolder {
		private final TextView textView;

		public OtherViewHolder(View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.tv);
		}
	}

	static class MyChannelHeaderViewHolder extends RecyclerView.ViewHolder {
		private final TextView tvBtnEdit;

		public MyChannelHeaderViewHolder(View itemView) {
			super(itemView);
			tvBtnEdit = itemView.findViewById(R.id.tv_btn_edit);
		}
	}
}
