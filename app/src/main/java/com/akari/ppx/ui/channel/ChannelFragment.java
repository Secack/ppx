package com.akari.ppx.ui.channel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.akari.ppx.R;
import com.akari.ppx.common.constant.Const;
import com.akari.ppx.common.constant.Prefs;
import com.akari.ppx.common.entity.ChannelEntity;
import com.akari.ppx.common.utils.ChannelUtils;
import com.akari.ppx.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.akari.ppx.common.constant.Const.CATEGORY_LIST_NAME;

public class ChannelFragment extends Fragment {
	private final ChannelActivity context;

	public ChannelFragment(ChannelActivity context) {
		this.context = context;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_channel, container, false);
	}

	@SuppressLint("WorldReadableFiles")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ChannelUtils.initSP(context.getSharedPreferences(Const.XSPreferencesName, 0));
		List<ChannelEntity> items = ChannelUtils.getSPDataList(Prefs.MY_CHANNEL);
		if (items == null) {
			items = new ArrayList<>();
			for (String s : CATEGORY_LIST_NAME) {
				ChannelEntity entity = new ChannelEntity();
				entity.setName(s);
				items.add(entity);
			}
		}
		final List<ChannelEntity> otherItems = ChannelUtils.getSPDataList(Prefs.OTHER_CHANNEL);
		GridLayoutManager manager = new GridLayoutManager(context, 4);
		RecyclerView recyclerview = (requireActivity()).findViewById(R.id.recy);
		recyclerview.setLayoutManager(manager);
		ItemDragHelperCallback callback = new ItemDragHelperCallback();
		final ItemTouchHelper helper = new ItemTouchHelper(callback);
		helper.attachToRecyclerView(recyclerview);

		final ChannelAdapter adapter = new ChannelAdapter(context, helper, items, otherItems != null ? otherItems : new ArrayList<>());
		manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				int viewType = adapter.getItemViewType(position);
				return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;
			}
		});
		recyclerview.setAdapter(adapter);
		List<ChannelEntity> finalItems = items;
		Utils.setPreferenceWorldWritable(context);
		adapter.setOnMyChannelItemClickListener((v, position) -> {
			String channelName = finalItems.get(position).getPureName();
			ChannelUtils.setDefaultChannel(channelName);
			Utils.setPreferenceWorldWritable(context);
			Toast.makeText(context, "当前默认频道：" + channelName, Toast.LENGTH_SHORT).show();
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		Utils.setPreferenceWorldWritable(context);
	}
}
