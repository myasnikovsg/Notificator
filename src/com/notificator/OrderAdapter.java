package com.notificator;

import model.Order;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter{
	private Activity context;
	
	public OrderAdapter(Activity context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return Utils.orders.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup view_group) {
		LayoutInflater inflater = context.getLayoutInflater();
		View root = inflater.inflate(R.layout.order, null, true);
		Order order;
		synchronized (Utils.orders) {
			order = Utils.orders.get(position);
		}
		TextView name = (TextView) root.findViewById(R.id.order_name); 
		name.setText(order.getName());
		TextView found = (TextView) root.findViewById(R.id.order_found_number);
		found.setText(Integer.toString(10));
		TextView time_left = (TextView) root.findViewById(R.id.order_time_left);
		time_left.setText(Integer.toString(163));
		RelativeLayout layout = (RelativeLayout) root.findViewById(R.id.order_relative_layout);
		layout.setBackgroundDrawable(Utils.getBack( 1d * 160 / order.getDuration()));
		return root;
	}
	
}
