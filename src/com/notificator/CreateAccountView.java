package com.notificator;
import com.notificator.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateAccountView extends Activity {
	private TextView promt[];
	private ImageView status[];
	private ProgressBar progress;
	private EditText login;
	private EditText password;
	private EditText password_confirm;
	private Spinner game_zone;
	private Button create_button;
	private boolean[] state;
	Handler handler;
	private int res;
	
	private void displayStatus(int what, int stat){
		switch (what) {
			case 0:
				switch (stat) {
					case 0:
						promt[0].setVisibility(View.GONE);
						progress.setVisibility(View.GONE);
						status[0].setVisibility(View.GONE);
						state[0] = false;
						break;
					case 1:
						promt[0].setVisibility(View.VISIBLE);
						promt[0].setText(R.string.incorrect_login);
						status[0].setVisibility(View.VISIBLE);
						status[0].setImageResource(R.drawable.unchecked1);
						progress.setVisibility(View.GONE);
						state[0] = false;
						break;
					case 2:
						progress.setVisibility(View.VISIBLE);
						progress.setIndeterminate(true);
						promt[0].setVisibility(View.GONE);
						status[0].setVisibility(View.GONE);
						state[0] = false;
						handler = new Handler() {
							@Override
							public void handleMessage(Message msg) {
									displayStatus(0, msg.what );
							}
						};
						Thread available = new Thread(new Runnable() {
							@Override
							public void run() {
								handler.sendEmptyMessage(Utils.loginNameAvailable(login.getText().toString()) ? 3 : 4);
							}
						});
						available.start();
						break;
					case 3:
						progress.setVisibility(View.GONE);
						promt[0].setVisibility(View.GONE);
						status[0].setVisibility(View.VISIBLE);
						status[0].setImageResource(R.drawable.checked1);
						state[0] = true;
						break;
					case 4:
						progress.setVisibility(View.GONE);
						promt[0].setVisibility(View.VISIBLE);
						promt[0].setText(R.string.taken_login);
						status[0].setVisibility(View.VISIBLE);
						status[0].setImageResource(R.drawable.unchecked1);
						state[0] = false;
						break;
				}
				break;
			case 1:
				switch (stat) {
					case 0:
						promt[1].setVisibility(View.GONE);
						status[1].setVisibility(View.GONE);
						state[1] = false;
						break;
					case 1:
						promt[1].setVisibility(View.VISIBLE);
						promt[1].setText(R.string.incorrect_password);
						status[1].setVisibility(View.VISIBLE);
						status[1].setImageResource(R.drawable.unchecked1);
						state[1] = false;
						break;
					case 2:
						promt[1].setVisibility(View.GONE);
						status[1].setVisibility(View.VISIBLE);
						status[1].setImageResource(R.drawable.checked1);
						state[1] = true;
						break;
				}
				break;
			case 2:
				switch (stat) {
				case 0:
					promt[2].setVisibility(View.GONE);
					status[2].setVisibility(View.GONE);
					state[2] = false;
					break;
				case 1:
					promt[2].setVisibility(View.VISIBLE);
					promt[2].setText(R.string.password_mismatch);
					status[2].setVisibility(View.VISIBLE);
					status[2].setImageResource(R.drawable.unchecked1);
					state[2] = false;
					break;
				case 2:
					promt[2].setVisibility(View.GONE);
					status[2].setVisibility(View.VISIBLE);
					status[2].setImageResource(R.drawable.checked1);
					state[2] = true;
					break;
				}
				break;
			default:
				break;
		}
		create_button.setEnabled(state[0] && state[1] && state[2]);
	}
	
	private class FocusListener implements OnFocusChangeListener{
		
		@Override
		public void onFocusChange(View view, boolean focus) {
			if (!focus) {
				String s = ((EditText) view).getText().toString();
				int tag = (Integer) view.getTag();
				switch (tag) {
					case 0 :
						if (s.length() == 0)
							displayStatus(0, 0);
						else if (s.length() < 5)
							displayStatus(0, 1);
						else
							displayStatus(0, 2);
						break;
					case 1 : 
						if (s.length() == 0)
							displayStatus(1, 0);
						else if (s.length() < 5)
							displayStatus(1, 1);
						else
							displayStatus(1, 2);
						break;
				}
			}
		}
		
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_account_view);
		login = (EditText) findViewById(R.id.create_account_login_edit);
		password = (EditText) findViewById(R.id.create_account_password_edit);
		password_confirm = (EditText) findViewById(R.id.create_account_password_confirm_edit);
		promt = new TextView[3];
		promt[0] = (TextView) findViewById(R.id.create_account_login_promt);
		promt[1] = (TextView) findViewById(R.id.create_account_password_promt);
		promt[2] = (TextView) findViewById(R.id.create_account_password_confirm_promt);
		promt[0].setVisibility(View.GONE);
		promt[1].setVisibility(View.GONE);
		promt[2].setVisibility(View.GONE);
		status = new ImageView[3];
		status[0] = (ImageView) findViewById(R.id.create_account_login_status);
		status[1] = (ImageView) findViewById(R.id.create_account_password_status);
		status[2] = (ImageView) findViewById(R.id.create_account_password_confirm_status);
		status[0].setVisibility(View.GONE);
		status[1].setVisibility(View.GONE);
		status[2].setVisibility(View.GONE);
		progress = (ProgressBar) findViewById(R.id.create_account_login_progress);
		progress.setVisibility(View.GONE);
		game_zone = (Spinner) findViewById(R.id.create_account_game_zone_spinner);
		create_button = (Button) findViewById(R.id.create_account_create_button);
		create_button.setEnabled(false);
		state = new boolean[3];
		login.setOnFocusChangeListener(new FocusListener());
		login.setTag(0);
		login.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {	
				if (status[0].getVisibility() == View.VISIBLE) {
					String str = login.getText().toString();
					if (str.length() == 0)
						displayStatus(0, 0);
					else if (str.length() < 5)
						displayStatus(0, 1);
					else
						displayStatus(0, 2);
				}
			}
		});
		password.setOnFocusChangeListener(new FocusListener());
		password.setTag(1);
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {	
				if (status[1].getVisibility() == View.VISIBLE) {
					String str = password.getText().toString();
					if (str.length() == 0)
						displayStatus(1, 0);
					else if (str.length() < 5)
						displayStatus(1, 1);
					else
						displayStatus(1, 2);
				}
			}
		});
		password_confirm.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				String str = password_confirm.getText().toString();
				if (str.length() == 0) 
					displayStatus(2, 0);
				else if (!str.equals(password.getText().toString())) 
					displayStatus(2, 1);
				else
					displayStatus(2, 2);
				
			}
		});
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.game_zone, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		game_zone.setAdapter(adapter);
		create_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {						
				final Thread create = new Thread(new Runnable() {
					@Override
					public void run() {
						res = Utils.createAccount(login.getText().toString(), password.getText().toString(), game_zone.getSelectedItemPosition());
					}
				});
				final ProgressDialog create_progress_dialog = new ProgressDialog(CreateAccountView.this);
				create_progress_dialog.setCancelable(false);
				create_progress_dialog.setMessage(getString(R.string.creating_account));
				create_progress_dialog.setIndeterminate(true);
				create_progress_dialog.show();
				create.start();
				final Handler handle = new Handler() {
					public void handleMessage(Message msg) {
						create_progress_dialog.dismiss();
						AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountView.this);
						AlertDialog dialog = builder.setMessage("Code: " + res).setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (res == 0) 
									CreateAccountView.this.finish();
								dialog.dismiss();
							}
						}).create();
						dialog.show();
					};
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
							if (!create.isAlive()) 
								break;
						}
						create.interrupt();
						if (count == 100) {
							res = 1;
						}
						handle.sendEmptyMessage(0);
					}
				});
				watcher.start();
			}
		});
	}
}
