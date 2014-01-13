package com.notificator;

import model.Order;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CreateOrderView extends Activity{
	ListView list;
	Order order;
	String content[];
	boolean checked[][];
	boolean current_checked[];
	CheckAdapter adapter;
	private View root;
	private AlertDialog.Builder builder;
	private TextView dialog_title;
	private ListView dialog_list;
	private Button dialog_ok;
	private Button dialog_cancel;
	CompoundButton compound[];
	AlertDialog dialog;
	private RadioDialogAdapter dialog_adapter;
	private LayoutInflater inflater;
	private int main_attr;
	private int current_main_attr; 
	private int property_type[];
	private int property_value[];
	private int cur_property_type;
	private int cur_property_value;
	private int buyout;
	private int cur_buyout;
	private String name;
	private String cur_name;
	private int res;
	
	private class onOkClick implements OnClickListener{
		private int index_in_checked;
		public onOkClick(int index_in_checked, int amount){
			this.index_in_checked = index_in_checked;
		}
		@Override
		public void onClick(View v) {
			checked[index_in_checked] = current_checked;
			if (index_in_checked == 3) {
				for (int i = 0; i < checked[4].length; i++)
					checked[4][i] = false;
				checked[4][0] = true;
			}
			dialog.dismiss();
			Utils.fixProperties(checked[1], getPrimary(), checked[4], property_type, property_value);
			adapter.notifyDataSetChanged();
		}
		
	}
	
	private class onEditOkClick implements OnClickListener{
		private int what;
		
		public onEditOkClick(int what){
			this.what = what;
		}
		
		@Override
		public void onClick(View v) {
			if (what < 6) {
				property_value[what] = cur_property_value;
				Utils.forbidProperty(property_type[what]);
			}
			if (what == 6) 
				main_attr = current_main_attr;
			if (what == 7)
				buyout = cur_buyout;
			if (what == 8)
				name = cur_name;
			dialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	}
	
	private Order getOrder(){
		Order order = new Order();
		int len = 0;
		for (int i = 0; i < 5; i++)
			if (checked[1][i])
				len++;
		byte temp[] = new byte[len];
		len = 0;
		for (int i = 0; i < 5; i++)
			if (checked[1][i])
				temp[len++] = (byte)i;
		order.setClasses(temp);
		order.setDuration(1000);
		order.setHardcore(checked[0][0]);
		for (int i = 0; i < 3; i++)
			if (checked[2][i]) {
				order.setLvl_group((byte)i);
				break;
			}
		order.setMaxBuyout(buyout);
		order.setName(name);
		order.setProperty_type(property_type);
		order.setProperty_value(property_value);
		len = 0;
		for (int i = 0; i < Utils.item_amount[getPrimary()]; i++)
			if (checked[4][i])
				len++;
		temp = new byte[len];
		len = 0;
		for (int i = 0; i < Utils.item_amount[getPrimary()]; i++)
			if (checked[4][i])
				temp[len++] = (byte) Utils.getOffsettedIndex(getPrimary(), i);
		order.setType_2(temp);
		order.setMain_attr(main_attr);
		return order;
	}
	
	private class onAlertOkClick implements OnClickListener{
		private int what;
		public onAlertOkClick(int what){
			this.what = what;
		}
		@Override
		public void onClick(View v) {
			if (what == 6) {			
				dialog.dismiss();
				final Thread send_thread = new Thread(new Runnable() {
					@Override
					public void run() {
						res = Utils.sendOrder(getOrder());
					}
				});
				final ProgressDialog send_progress_dialog = new ProgressDialog(CreateOrderView.this);
				send_progress_dialog.setCancelable(false);
				send_progress_dialog.setMessage(getString(R.string.creating_account));
				send_progress_dialog.setIndeterminate(true);
				send_progress_dialog.show();
				send_thread.start();
				final Handler handle = new Handler() {
					public void handleMessage(Message msg) {
						send_progress_dialog.dismiss();
						AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrderView.this);
							AlertDialog dialog = builder.setMessage("Code: " + res).setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (res == 0) 
										CreateOrderView.this.finish();
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
								if (!send_thread.isAlive()) 
									break;
							}
							send_thread.interrupt();
							if (count == 100) {
								res = 1;
							}
							handle.sendEmptyMessage(0);
						}
					});
					watcher.start();
			} else {
				Utils.releaseProperty(property_type[what]);
				for (int i = what; i < property_type.length - 1; i++) {
					property_type[i] = property_type[i + 1];
					property_value[i] = property_value[i + 1];
				}
				dialog.dismiss();
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private class onPropertyOkClick implements OnClickListener{
		private int property_number;
		public onPropertyOkClick(int property_number){
			this.property_number = property_number;
		}
		@Override
		public void onClick(View v) {
			property_type[property_number] = cur_property_type;
			dialog.dismiss();
			inflater = CreateOrderView.this.getLayoutInflater();
			root = inflater.inflate(R.layout.radio_dialog, null, true);
			root.setMinimumWidth(Utils.DIALOG_WIDTH);
			dialog_list = (ListView) root.findViewById(R.id.radio_dialog_list);
			dialog_title = (TextView) root.findViewById(R.id.radio_dialog_title);
			dialog_title.setText(Utils.property_name[cur_property_type]);
			cur_property_value = Math.max(property_value[property_number], Utils.property_min_val[property_type[property_number]]);
			dialog_adapter = new RadioDialogAdapter(CreateOrderView.this, new String[] {"$"}, DIALOG_TYPE.EDIT, property_number);
			dialog_list.setAdapter(dialog_adapter);
			dialog_ok = (Button) root.findViewById(R.id.radio_dialog_ok_button);
			dialog_cancel = (Button) root.findViewById(R.id.radio_dialog_cancel_button);
			dialog_cancel.setVisibility(View.GONE);
			dialog_ok.setOnClickListener(new onEditOkClick(property_number));
			builder.setView(root);
			dialog = builder.create();
			dialog.setOnShowListener(new OnShowListener() {
				@Override
				public void onShow(DialogInterface dial) {
					dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				}
			});
			dialog.show();
		}
	}
	
	private class onCancelClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
		
	}
	
	private class onItemClick implements OnItemClickListener{
		private boolean isRadio;
		public onItemClick(boolean isRadio) {
			this.isRadio = isRadio;
		}
		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
			if (isRadio) {
				for (int i = 0; i < current_checked.length; i++)
					current_checked[i] = false;
					current_checked[position] = true;
			} else {
				current_checked[position] = !current_checked[position];
				boolean flag = false;
				for (int i = 0; i < current_checked.length; i++)
					flag |= current_checked[i];
				dialog_ok.setEnabled(flag);
			}
			dialog_adapter.notifyDataSetChanged();
		}
	}
	
	private class onPropertyItemClick implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
			cur_property_type = Utils.getPropertyId(position);
			dialog_ok.setEnabled(true);
			dialog_adapter.notifyDataSetChanged();
		}
	}
	
	private class onTextChange implements TextWatcher{
		private int what;
		
		public onTextChange(int what){
			this.what = what;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (what < 6) {
				try {
					int d = Integer.parseInt(s.toString());
					dialog_ok.setEnabled(d > Utils.property_min_val[property_type[what]]);
					cur_property_value = d;
				} catch (Exception e) {
					dialog_ok.setEnabled(false);
				}
			}
			if (what == 6)
				try {
					int d = Integer.parseInt(s.toString());
					if (d < Utils.getMainAttr(getPrimary(), checked[4]))
						dialog_ok.setEnabled(false);
					else
						dialog_ok.setEnabled(true);
					current_main_attr = d;
				} catch (Exception e) {
					dialog_ok.setEnabled(false);
				}
			if (what == 7) 
				try {
					int d = Integer.parseInt(s.toString());
					if (d > 1000) {
						cur_buyout = d;
						dialog_ok.setEnabled(true);
					} else
						dialog_ok.setEnabled(false);
				} catch (Exception e) {
					dialog_ok.setEnabled(false);
				}
			if (what == 8) {
				if (s.length() > 0) {
					cur_name = s.toString();
					dialog_ok.setEnabled(true);
				} else
					dialog_ok.setEnabled(false);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
	}
	
	public int getPrimary() {
		int primary = 0;
		for (int i = 0; i < checked[3].length; i++)
			if (checked[3][i]) {
				primary = i;
				break;
			}
		return primary;
	}
	
	public int isDPS(){ // > 0 - dps, < 0 - armor, 0 - neither of it
		int primary = getPrimary();
		if (primary < 2) // weapon always have dps 
			return 1;
		if (primary == 2) {
			if (checked[4][0] || checked[4][1]) // mojo and source also have dps
				return 1;
			else 
				if (checked[4][2]) // quiver can only have IAS
					return 0;
				else
					return -1; // shields have armor only
		}
		if (checked[4][0] || checked[4][10]) // ring and amulet can have both dps and armor, but not always
			return 0;
		else
			return -1;
	}
	
	public int getPropNumber(){
		for (int i = 0; i < property_type.length; i++)
			if (property_type[i] == -10) {
				return i;
			}
		return property_type.length;
	}
	
	public String[] getSecondaryArray(int primary) {
		String[] choice = null;
		switch (primary) {
			case 0 :
				choice = getResources().getStringArray(R.array.onehand_items);
				break;
			case 1 : 
				choice = getResources().getStringArray(R.array.twohand_items);
				break;
			case 2 :
				choice = getResources().getStringArray(R.array.offhand_items);
				break;
			case 3 :
				choice = getResources().getStringArray(R.array.armor_items);
				break;
			case 4 :
				choice = getResources().getStringArray(R.array.follower_items);
				break;
		}
		return Utils.getPossibleItems(checked[1], primary, choice);
	}
	
	public boolean isPropertyPosition(int position) {
		return (position > 12 && position < 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 1);
	}
	
	public boolean isNewPropertyPosition(int position) {
		if (isPropertyPosition(position)){
			if (getPropNumber() != property_type.length)
				return position == 12 + getPropNumber() + 1;
		}
		return false;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_order);
		content = new String[5];
		checked = new boolean[5][15];
		property_type = new int[6];
		property_value = new int[property_type.length];
		Order c_order = Utils.c_order;
		if (c_order == null) {
			buyout = 10000;
			name = "Foo";
			for (int i = 0; i < 5; i++)
				checked[i][0] = true;
			for (int i = 0; i < property_type.length; i++) {
				property_type[i] = -10;
				property_value[i] = -1;
			}
		} else {
			buyout = c_order.getMaxBuyout();
			name = c_order.getName();
			if (c_order.isHardcore())
				checked[0][0] = true;
			else
				checked[0][1] = true;
			byte temp[] = c_order.getClasses();
			for (int i = 0; i < temp.length; i++)
				checked[1][temp[i]] = true;
			checked[2][c_order.getLvl_group()] = true;
			checked[3][Utils.getType_1(c_order.getType_2()[0])] = true;
			temp = c_order.getType_2();
			for (int i = 0; i < temp.length; i++)
				checked[4][Utils.getType_2(temp[i])] = true;
			property_type = c_order.getProperty_type();
			property_value = c_order.getProperty_value();
			main_attr = c_order.getMain_attr();
		}
		compound = new CompoundButton[15];
		list = (ListView) findViewById(R.id.create_order_list);
		adapter = new CheckAdapter(this);
		list.setAdapter(adapter);
		builder = new AlertDialog.Builder(this);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DIALOG_TYPE type = DIALOG_TYPE.RADIO;
				int index = 0;
				String array[] = null;
				int what = 0;
				switch (position) {
					case 1 : 
						index = 0;
						type = DIALOG_TYPE.RADIO;
						what = 0;
						array = getResources().getStringArray(R.array.game_mode);
				 		break;
					case 3 :
						index = 1;
						type = DIALOG_TYPE.CHECK;
						what = 0;
						array = getResources().getStringArray(R.array.classes);
				 		break;
					case 5 :
						index = 2;
						type = DIALOG_TYPE.RADIO;
						what = 0;
						array = getResources().getStringArray(R.array.lvl_group);
						break;
					case 7 : 
						index = 3;
						type = DIALOG_TYPE.RADIO;
						what = 0;
						array = getResources().getStringArray(R.array.primary_type);
						break;
					case 9 :
						index = 4;
						type = DIALOG_TYPE.CHECK;
						what = 0;
						int primary = getPrimary();
						if (primary > 1) 
							type = DIALOG_TYPE.RADIO;
						array = getSecondaryArray(primary);
						break;
					case 11 : 
						index = (isDPS() > 0) ? 5 : 6; 
						type = DIALOG_TYPE.EDIT;
						array = new String[] {"$"};
						what = 6;
						break;
				}
				if (isPropertyPosition(position)) {
					index = 7;
					type = DIALOG_TYPE.PROPERTY;
					cur_property_type = (isNewPropertyPosition(position)) ? -1 : property_type[position - 12 - 1];
					if (!isNewPropertyPosition(position))
						Utils.releaseProperty(cur_property_type);
					array = new String[] {"$"};
				}
				if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 2) {
					index = 8;
					type = DIALOG_TYPE.EDIT;
					array = new String[] {"$"};
					what = 7;
				}
				if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 4) {
					index = 9;
					type = DIALOG_TYPE.EDIT;
					array = new String[] {"$"};
					what = 8;
				}
				if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 5) {
					index = 10;
					type = DIALOG_TYPE.ALERT;
					array = new String[] {"$"};
					what = 6;
				}
				inflater = CreateOrderView.this.getLayoutInflater();
				root = inflater.inflate(R.layout.radio_dialog, null, true);
				root.setMinimumWidth(Utils.DIALOG_WIDTH);
				dialog_list = (ListView) root.findViewById(R.id.radio_dialog_list);
				dialog_title = (TextView) root.findViewById(R.id.radio_dialog_title);
				dialog_title.setText(getResources().getStringArray(R.array.dialog_title)[index]);
				dialog_adapter = new RadioDialogAdapter(CreateOrderView.this, array, type, what);
				dialog_list.setAdapter(dialog_adapter);
				dialog_ok = (Button) root.findViewById(R.id.radio_dialog_ok_button);
				switch (type) {
					case RADIO :
						current_checked = checked[index];
						dialog_list.setOnItemClickListener(new onItemClick(type == DIALOG_TYPE.RADIO));
						dialog_ok.setOnClickListener(new onOkClick(index, array.length));
						break;
					case CHECK :
						current_checked = checked[index];
						dialog_list.setOnItemClickListener(new onItemClick(type == DIALOG_TYPE.RADIO));
						dialog_ok.setOnClickListener(new onOkClick(index, array.length));
						break;
					case EDIT : 
						dialog_ok.setOnClickListener(new onEditOkClick(what));
						break;
					case PROPERTY : 
						dialog_list.setOnItemClickListener(new onPropertyItemClick());
						dialog_ok.setOnClickListener(new onPropertyOkClick(position - 12 - 1));
						if (isNewPropertyPosition(position))
							dialog_ok.setEnabled(false);
						break;
					case ALERT :
						dialog_ok.setOnClickListener(new onAlertOkClick(what));
						break;
				}
				dialog_cancel = (Button) root.findViewById(R.id.radio_dialog_cancel_button);
				dialog_cancel.setOnClickListener(new onCancelClick());
				builder.setView(root);
				dialog = builder.create();
				dialog.setOnShowListener(new OnShowListener() {
					@Override
					public void onShow(DialogInterface dial) {
						dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
					}
				});
				dialog.show();
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {
				if (isPropertyPosition(position) && !isNewPropertyPosition(position)){
					inflater = CreateOrderView.this.getLayoutInflater();
					root = inflater.inflate(R.layout.radio_dialog, null, true);
					dialog_list = (ListView) root.findViewById(R.id.radio_dialog_list);
					dialog_title = (TextView) root.findViewById(R.id.radio_dialog_title);
					dialog_title.setText(getString(R.string.delete_poroperty));
					dialog_adapter = new RadioDialogAdapter(CreateOrderView.this, new String[] {"$"}, DIALOG_TYPE.ALERT, position - 12 - 1);
					dialog_list.setAdapter(dialog_adapter);
					dialog_ok = (Button) root.findViewById(R.id.radio_dialog_ok_button);
					dialog_cancel = (Button) root.findViewById(R.id.radio_dialog_cancel_button);
					dialog_cancel.setOnClickListener(new onCancelClick());
					dialog_ok.setOnClickListener(new onAlertOkClick(position - 12 - 1));
					builder.setView(root);
					dialog = builder.create();
					dialog.setOnShowListener(new OnShowListener() {
						@Override
						public void onShow(DialogInterface dial) {
							dialog.getWindow().setLayout(Utils.DIALOG_WIDTH, LayoutParams.WRAP_CONTENT);
						}
					});
					dialog.show();
				}
				return false;
			}
		});
	}
	
	private class RadioDialogAdapter extends BaseAdapter {
		private Activity context;
		private String choice[];
		private DIALOG_TYPE type;
		private int what; // 0 - 5 =  property, 6 = main_attr, 7 = buyout
		
		public RadioDialogAdapter(Activity context, String choice[], DIALOG_TYPE type, int what) {
			this.context = context;
			this.choice = choice;
			this.type = type;
			this.what = what;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return (type == DIALOG_TYPE.CHECK || type == DIALOG_TYPE.RADIO);
		}
		
		@Override
		public boolean isEnabled(int position) {
			if (type == DIALOG_TYPE.RADIO || type == DIALOG_TYPE.CHECK)
				return true;
			if (type == DIALOG_TYPE.EDIT || type == DIALOG_TYPE.ALERT)
				return false;
			return !Utils.isDelimiter(position);
		}
		
		@Override
		public int getCount() {
			return (type == DIALOG_TYPE.PROPERTY) ? Utils.cur_property.length : choice.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View root = null;
			TextView text;
			switch (type){
				case RADIO :
					root = inflater.inflate(R.layout.radio_string, null, true);
					text = (TextView) root.findViewById(R.id.radio_string_text);
					text.setText(choice[position]);
					compound[position] = (CompoundButton) root.findViewById(R.id.radio_string_box);
					compound[position].setText("");
					compound[position].setChecked(current_checked[position]);
					break;
				case CHECK :
					root = inflater.inflate(R.layout.check_string, null, true);
					text = (TextView) root.findViewById(R.id.check_string_text);
					text.setText(choice[position]);
					compound[position] = (CompoundButton) root.findViewById(R.id.check_string_box);
					compound[position].setText("");
					compound[position].setChecked(current_checked[position]);
					break;
				case EDIT :
					root = inflater.inflate(R.layout.edit_string, null, true);
					EditText edit = (EditText) root.findViewById(R.id.edit_string_text);
					if (what < 6) { // property value
						edit.setText(Integer.toString(cur_property_value));
						edit.addTextChangedListener(new onTextChange(what));
					}
					if (what == 6){ // main_attr
						edit.setText(Integer.toString(main_attr));
						edit.addTextChangedListener(new onTextChange(what));
					}
					if (what == 7) { // buyout
						edit.setText(Integer.toString(buyout));
						edit.addTextChangedListener(new onTextChange(what));
					}
					if (what == 8) { // name
						edit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
						edit.setText(name);
						edit.addTextChangedListener(new onTextChange(what));
					}
					break;
				case PROPERTY :
					if (Utils.isDelimiter(position)){
						root = inflater.inflate(R.layout.delimiter, null, true);
						text = (TextView) root.findViewById(R.id.delimiter_text);
						text.setText(Utils.getString(position));
					} else {
						root = inflater.inflate(R.layout.radio_string, null, true);
						text = (TextView) root.findViewById(R.id.radio_string_text);
						text.setText(Utils.getString(position));
						CompoundButton button = (CompoundButton) root.findViewById(R.id.radio_string_box);
						button.setText("");
						button.setChecked(cur_property_type != -1 && Utils.getPropertyId(position) == cur_property_type);
					}
					break;
				case ALERT :
					root = inflater.inflate(R.layout.check_string, null, true);
					text = (TextView) root.findViewById(R.id.check_string_text);
					if (what != 6)
						text.setText(Utils.getPropertyString(property_type[what], property_value[what]));
					else
						text.setVisibility(View.GONE);
					CompoundButton button = (CompoundButton) root.findViewById(R.id.check_string_box);
					button.setVisibility(View.GONE);
			}
			return root;
		}
		
	}
	
	private class CheckAdapter extends BaseAdapter {

		private Activity context;
		public CheckAdapter(Activity context) {
			Utils.fixProperties(checked[1], getPrimary(), checked[4], property_type, property_value);
			this.context = context;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}
		
		@Override
		public boolean isEnabled(int position) {
			if (Utils.c_order != null)
				return false;
			if (position == 1 || position == 3 || position == 5 || position == 7 || position == 9)
				return true;
			if (position == 11) 
				return isDPS() != 0;
			if (isPropertyPosition(position))
				return true;
			if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 2)
				return true;
			if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 4)
				return true;
			if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 5)
				return true;
			return false;
		}
		
		@Override
		public int getCount() {
			return 18 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1);
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup viewGroup) {
			LayoutInflater inflater = context.getLayoutInflater();
			View root = null;
			TextView text;
			if (position < 10) {
				if (isEnabled(position)) {
					root = inflater.inflate(R.layout.group, null, true);
					text = (TextView) root.findViewById(R.id.group_text);
					int pos = 0;
					String array[] = null;
					switch (position) {
						case 1 :
							pos = 0;
							array = getResources().getStringArray(R.array.game_mode);
							break;
						case 3 :
							pos = 1;
							array = getResources().getStringArray(R.array.classes);
							break;
						case 5 :
							pos = 2;
							array = getResources().getStringArray(R.array.lvl_group);
							break;
						case 7 :
							pos = 3;
							array = getResources().getStringArray(R.array.primary_type);
							break;
						case 9 :
							pos = 4;
							int primary = getPrimary();
							array = getSecondaryArray(primary);
							break;
					}
					text.setText(Utils.getContent(array, checked[pos]));
				} else {
					root = inflater.inflate(R.layout.delimiter, null, true);
					text = (TextView) root.findViewById(R.id.delimiter_text);
					text.setText(getResources().getStringArray(R.array.delimiters)[position / 2]);
				}
			} else {
				if (position == 10) {
					root = inflater.inflate(R.layout.delimiter, null, true);
					text = (TextView) root.findViewById(R.id.delimiter_text);
					int temp = isDPS();
					int delim_index = 0;
					if (temp > 0)
						delim_index = 5;
					if (temp < 0)
						delim_index = 6;
					if (temp == 0)
						delim_index = 7;
					text.setText(getResources().getStringArray(R.array.delimiters)[delim_index]);
				}
				if (position == 11) {
					root = inflater.inflate(R.layout.group, null, true);
					text = (TextView) root.findViewById(R.id.group_text);
					main_attr = Math.max(Utils.getMainAttr(getPrimary(), checked[4]), main_attr);
					int temp = isDPS();
					String t = "";
					if (temp != 0)
						t = Integer.toString(main_attr);
					else
						t = "N/A";
					text.setText(t);
				}
				if (position == 12) {
					root = inflater.inflate(R.layout.delimiter, null, true);
					text = (TextView) root.findViewById(R.id.delimiter_text);
					text.setText(getResources().getStringArray(R.array.delimiters)[8]);
				}
				if (isPropertyPosition(position)){
					root = inflater.inflate(R.layout.group, null, true);
					text = (TextView) root.findViewById(R.id.group_text);
					if (isNewPropertyPosition(position)) {
						text.setText(getString(R.string.add_property));
					} else {
						text.setText(Utils.getPropertyString(property_type[position - 12 - 1], property_value[position - 12 - 1]));
					}
				}
				if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 1) {
					root = inflater.inflate(R.layout.delimiter, null, true);
					text = (TextView) root.findViewById(R.id.delimiter_text);
					text.setText(getResources().getStringArray(R.array.delimiters)[9]);
				}
				if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 2) {
					root = inflater.inflate(R.layout.group, null, true);
					text = (TextView) root.findViewById(R.id.group_text);
					text.setText(Integer.toString(buyout));
				}
				if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 3) {
					root = inflater.inflate(R.layout.delimiter, null, true);
					text = (TextView) root.findViewById(R.id.delimiter_text);
					text.setText(getResources().getStringArray(R.array.delimiters)[10]);
				}
				if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 4) {
					root = inflater.inflate(R.layout.group, null, true);
					text = (TextView) root.findViewById(R.id.group_text);
					text.setText(name);
				}
				if (position == 12 + getPropNumber() + ((getPropNumber() == property_type.length) ? 0 : 1) + 5) {
					root = inflater.inflate(R.layout.group, null, true);
					text = (TextView) root.findViewById(R.id.group_text);
					text.setText("Create order");
				}
			}
			return root;
		}
		
	}
}
