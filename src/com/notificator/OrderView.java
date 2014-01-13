package com.notificator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class OrderView extends ListActivity{
	OrderAdapter adapter;
	Handler handler;
	private ProgressDialog dialog;
	private Handler result_handler;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new OrderAdapter(this);
		setListAdapter(adapter);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg){
				adapter.notifyDataSetChanged();
			}
		};
		Thread watcher = new Thread(new Runnable() {
			@Override
			public void run() {
				int t[] = new int[Utils.orders.size()];
				for (int i = 0; i < t.length; i++)
					t[i] = (int) Utils.orders.get(i).getDuration();
				while (true){
					boolean something_left = false;
					for (int i = 0; i < t.length; i++){
						t[i]--;
						if (t[i] > 0)
							something_left = true;
					}
					handler.sendEmptyMessage(0);
					if (!something_left)
						break;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		watcher.start();
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {
				Utils.c_order = Utils.orders.get(position);
				startActivity(new Intent(OrderView.this, CreateOrderView.class));
				return false;
			}
			
		});
	}	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		dialog = new ProgressDialog(OrderView.this);
		dialog.setCancelable(false);
		dialog.setMessage(getString(R.string.loading_results));
		dialog.setIndeterminate(true);
		dialog.show();
		Utils.loadResult(0);
		result_handler = new Handler() {
			@Override
			public void handleMessage(Message msg){
				dialog.dismiss();
				if (msg.what == 0) {
					if (Utils.result.size() == 0) {
					}
					else 
						startActivity(new Intent(OrderView.this, ResultView.class));
				} else {
					AlertDialog.Builder alert = new AlertDialog.Builder(OrderView.this);
					alert.setMessage(getString(R.string.results_loading_error));
					alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();	
						}
					});
					alert.create();
					alert.show();
				}
			}
		};
		Thread watcher = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				int count = 0;
				while (!Utils.results_loaded && count < 100)
					try {
						Thread.sleep(100);
						count++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				if (Utils.results_loaded) 
					result_handler.sendEmptyMessage(0);
				else
					result_handler.sendEmptyMessage(1);
			}
		});
		watcher.start();
	}
	
}
