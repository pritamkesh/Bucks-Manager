/* This is source code of Bank Account Registration.
* Source Code written by Pritam Kesh.

* E-mail : pritamkesh.summercode@gmail.com
*
* To report any bugs please send me an e-mail.
* Tips are welcome.
*
*/
package com.example.bucksmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

 
public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] homeMenu;
 
	public ImageAdapter(Context context, String[] homeMenu) {
		this.context = context;
		this.homeMenu = homeMenu;
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
 
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View gridView;
 
		if (convertView == null) {
 
			gridView = new View(context);
 
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.items, null);
 
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(homeMenu[position]);
 
			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);
 
			String mobile = homeMenu[position];
 
			if (mobile.equals("Transaction History")) {
				imageView.setImageResource(R.drawable.transaction_icon);
			} else if (mobile.equals("Balance")) {
				imageView.setImageResource(R.drawable.balance_icon);
			} else if (mobile.equals("Settings")) {
				imageView.setImageResource(R.drawable.settings_icon);
			} else {
				imageView.setImageResource(R.drawable.profile_icon);
			}
 
		} else {
			gridView = (View) convertView;
		}
 
		return gridView;
	}
 
	public int getCount() {
		return homeMenu.length;
	}
 
	public Object getItem(int position) {
		return null;
	}
 
	public long getItemId(int position) {
		return 0;
	}
 
}
