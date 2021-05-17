package com.akari.ppx.ui.channel;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jetbrains.annotations.NotNull;

public class ItemDragHelperCallback extends ItemTouchHelper.Callback {
	@Override
	public int getMovementFlags(RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
		int dragFlags;
		RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
		if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
			dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
		} else {
			dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
		}
		int swipeFlags = 0;
		return makeMovementFlags(dragFlags, swipeFlags);
	}

	@Override
	public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
		if (viewHolder.getItemViewType() != target.getItemViewType()) return false;
		if (recyclerView.getAdapter() instanceof OnItemMoveListener) {
			OnItemMoveListener listener = ((OnItemMoveListener) recyclerView.getAdapter());
			listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
		}
		return true;
	}

	@Override
	public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
	}

	@Override
	public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
		if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
			if (viewHolder instanceof OnDragVHListener) {
				OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
				itemViewHolder.onItemSelected();
			}
		}
		super.onSelectedChanged(viewHolder, actionState);
	}

	@Override
	public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
		if (viewHolder instanceof OnDragVHListener) {
			OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
			itemViewHolder.onItemFinish();
		}
		super.clearView(recyclerView, viewHolder);
	}

	@Override
	public boolean isLongPressDragEnabled() {
		return false;
	}

	@Override
	public boolean isItemViewSwipeEnabled() {
		return false;
	}
}
