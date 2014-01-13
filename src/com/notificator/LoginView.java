package com.notificator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginView extends Activity {
	private Button login_button;
	private Button create_button;
	private EditText login;
	private EditText password;
	private InputMethodManager imm;
	private int res;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		login = (EditText) findViewById(R.id.login_login_edit);
		password = (EditText) findViewById(R.id.login_password_edit);
		login_button = (Button) findViewById(R.id.login_login_button);
		create_button = (Button) findViewById(R.id.login_create_button);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
 		login_button.setOnClickListener(new OnClickListener() {
			private ProgressDialog dialog;
			private Handler handler;
			@Override
			public void onClick(View v) {
				imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
				final Thread loginThread = new Thread(new Runnable() {
					@Override
					public void run() {
						res = Utils.login(login.getText().toString(), password.getText().toString());	
					}
				});
				loginThread.start();
				final ProgressDialog login_progress_dialog = new ProgressDialog(LoginView.this);
				login_progress_dialog.setCancelable(false);  
				login_progress_dialog.setMessage(getString(R.string.try_login));
				login_progress_dialog.setIndeterminate(true);
				login_progress_dialog.show(); 
				final Handler handle = new Handler() {
					@Override
					public void handleMessage(Message msg) {
					if (res == 0) {
						login_progress_dialog.setMessage(getString(R.string.loading_orders));
						final Thread load_thread = new Thread(new Runnable() {
							@Override
							public void run() {
								res = Utils.loadOrders();
							}
						});
						load_thread.start();
						handler = new Handler() {
							@Override
							public void handleMessage(Message msg){
								login_progress_dialog.dismiss();
								if (res == 0) {
									if (Utils.orders.size() == 0)
										startActivity(new Intent(LoginView.this, EmptyOrderView.class));
									else 
										startActivity(new Intent(LoginView.this, OrderView.class));
									LoginView.this.finish();
								} else {
									AlertDialog.Builder alert = new AlertDialog.Builder(LoginView.this);
									alert.setMessage("Code: " + Integer.toString(res));
									alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											login_progress_dialog.dismiss();	
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
								while (true) {
									try {
										Thread.sleep(100);
										count++;
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									count++;
									if (!load_thread.isAlive())
										break;
								}
								load_thread.interrupt();
								if (count == 100)
									res = 1;
								handler.sendEmptyMessage(0);
							}
						});
						watcher.start();
					} else {
						login_progress_dialog.dismiss();
						String result = getString(R.string.login_failure);
						AlertDialog.Builder alert = new AlertDialog.Builder(LoginView.this);
						alert.setMessage(result).setCancelable(false).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								password.setText("");
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
					public void run() {
							int count = 0;
							while (count < 100) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								count++;
								if (!loginThread.isAlive()) 
									break;
							}
							loginThread.interrupt();
							if (count == 100) {
								res = 1;
							}
							handle.sendEmptyMessage(0);
						}
				});
				watcher.start();
			}
		});
		create_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginView.this, CreateAccountView.class));
			}
		});
	}
}
