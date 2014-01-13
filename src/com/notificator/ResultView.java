package com.notificator;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultView extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ResultAdapter(this));
	}
	
	private class ResultAdapter extends BaseAdapter{
		private Activity context;
		
		public ResultAdapter(Activity context) {
			this.context = context;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return false;
		}
		
		@Override
		public int getCount() {
			return Utils.result.size();
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
		public View getView(int position, View view, ViewGroup viewgroup) {
			LayoutInflater inflater = context.getLayoutInflater();
			View root = inflater.inflate(R.layout.result, null, true);
			TextView name = (TextView) root.findViewById(R.id.result_name);
			Result result = Utils.result.get(position);
			name.setText(result.getName());
			TextView main_attr = (TextView) root.findViewById(R.id.result_main_attr);
			main_attr.setText(Integer.toString(result.getMain_attr()));
			TextView property[] = new TextView[7];
			property[0] = (TextView) root.findViewById(R.id.result_property0);
			property[1] = (TextView) root.findViewById(R.id.result_property1);
			property[2] = (TextView) root.findViewById(R.id.result_property2);
			property[3] = (TextView) root.findViewById(R.id.result_property3);
			property[4] = (TextView) root.findViewById(R.id.result_property4);
			property[5] = (TextView) root.findViewById(R.id.result_property5);
			property[6] = (TextView) root.findViewById(R.id.result_property6);
			String property_string[] = Utils.getPropertyStringArray(result.getArray(), result.getProperty_number());
			for (int i = 0; i < result.getProperty_number(); i++)
				property[i].setText(property_string[i]);
			for (int i = result.getProperty_number(); i < property.length; i++)
				property[i].setVisibility(View.GONE);
			TextView socket[] = new TextView[3];
			socket[0] = (TextView) root.findViewById(R.id.result_socket0);
			socket[1] = (TextView) root.findViewById(R.id.result_socket1);
			socket[2] = (TextView) root.findViewById(R.id.result_socket2);
			for (int i = 0; i < result.getSocket_number(); i++)
				socket[i].setText(getString(R.string.socket));
			for (int i = result.getSocket_number(); i < socket.length; i++)
				socket[i].setVisibility(View.GONE);
			TextView buyout = (TextView) root.findViewById(R.id.result_buyout);
			buyout.setText(getString(R.string.buyout) + ": " + (result.getBuyout() != -1 ? result.getBuyout() : getString(R.string.NA)));
			TextView bid = (TextView) root.findViewById(R.id.result_bid);
			bid.setText(getString(R.string.bid) + ": " + result.getBid());
			return root;
		}
		
	}
}
