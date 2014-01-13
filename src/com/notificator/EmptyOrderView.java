package com.notificator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EmptyOrderView extends Activity{
	private Button create_button;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empty_order_view);
		create_button = (Button) findViewById(R.id.empty_order_create_button);
		create_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.c_order = null;
				startActivity(new Intent(EmptyOrderView.this, CreateOrderView.class));
				EmptyOrderView.this.finish();
				
			}
		});
	}
}
