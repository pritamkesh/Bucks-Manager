/* This is source code of Bank Account Registration.
* Source Code written by Pritam Kesh.

* E-mail : pritamkesh.summercode@gmail.com
*
* To report any bugs please send me an e-mail.
* Tips are welcome.
*
*/
package com.example.bucksmanager;

import android.os.Bundle;  
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	
	private String[] Sdata=new String[]{"Bank Account","SMS Number","Change Password","Delete Account","Sign Out"};
	protected ListView Sview;

	SQLiteDatabase db1=null;
	private static String DBNAME="DBBANKING.db";
	
	ListView lv;
	
	int item=0;
	String s[][];
	final Context context=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		
		Sview=(ListView)findViewById(R.id.listSettings);
		Sview.setAdapter(new customAdapterValue());
		
		Sview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int listPosition,
					long arg3) {
				// TODO Auto-generated method stub
				
					
					
					
				switch(listPosition)
				{
				case 0:
					Intent nextScreen1=new Intent(getApplicationContext(),BankAccountRegistration.class);
					startActivity(nextScreen1);
					break;
				case 1:
					Intent nextScreen2=new Intent(getApplicationContext(),SMSNumberUpdateForm.class);
					startActivity(nextScreen2);
					break;
				
				case 2:
					Intent nextScreen4=new Intent(getApplicationContext(),ChangePasswordForm.class);
					startActivity(nextScreen4);
					break;
				case 3:
					db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
					try
					{
						Cursor c=db1.rawQuery("SELECT * FROM accountdata",null);
						item=c.getCount();
						
					}catch(Exception e)
					{
						//Toast.makeText(getApplicationContext(), "No Account Number Exist", Toast.LENGTH_LONG).show();
					}
					
					if(item>0)
					{
						s=new String[item][2];
						storeAccountNumberInArray();
						performDeleteAccountOperation();
					}
					else
					{
						promptIfNoAccountExist();
					}
					break;
					
				case 4:
					performLogoutOperation();
					break;
				}
				
				
				
				
			}
		
		});
		
		
	}

	class customAdapterValue extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Sdata.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		LayoutInflater layinf=getLayoutInflater();

		View rview=arg1;


		if(rview==null){

		rview=layinf.inflate(R.layout.design_setting, arg2,false);

			final TextView tv=(TextView)rview.findViewById(R.id.textSettings);
			final TextView textArrow=(TextView)rview.findViewById(R.id.textSettingArrow);

		tv.setText(Sdata[arg0]);

		}
		
		
		return rview;
	}
	
}
	
	public void performLogoutOperation()
	{
		/*SharedPreferences signData= PreferenceManager.getDefaultSharedPreferences(this); 
		signData.edit().putBoolean("locked",false).commit(); */
		
		Toast.makeText(getApplicationContext(), "Successfully Sign Out", Toast.LENGTH_LONG).show();
		Intent i = new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		i.putExtra("EXIT", true);
		startActivity(i);
	    finish();
		
	}
	public void performDeleteAccountOperation()
	{
		
		final Dialog dialog=new Dialog(context);
		dialog.setContentView(R.layout.custom_dialog_transaction);
		dialog.setTitle("Delete Account");
		dialog.setCancelable(true);
		
		lv=(ListView)dialog.findViewById(R.id.listAccountNumber);
		lv.setAdapter(new customAdapterDeleteAccount());
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int listPosition,
					long arg3) {
				// TODO Auto-generated method stub
				
				showAlertDialogBoxforDelete(s[listPosition][1]);
				dialog.dismiss();
			
			}
			
		});
			
		dialog.show();

		
		
		
	}
	
	class customAdapterDeleteAccount extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return item;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			
			LayoutInflater layinf=getLayoutInflater();
			View rview=arg1;

			if(rview==null)
			{
				rview=layinf.inflate(R.layout.custom_dialog_transaction_history_design, arg2,false);
				
				final TextView tabank=(TextView)rview.findViewById(R.id.showBankName);
				final TextView tanumber=(TextView)rview.findViewById(R.id.showAccountNumber);
				
				tabank.setText(s[arg0][0]);
				tanumber.setText(s[arg0][1]);
			}
			
			return rview;

			
		}
		
	}
	
	void storeAccountNumberInArray()
	{
		
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		try
		{
			Cursor c=db1.rawQuery("SELECT * FROM accountdata",null);
			int i=0;
			
			if(c!=null)
			{
				if (c.moveToFirst()) 
				{
					do {
							s[i][0]=c.getString(c.getColumnIndex("BANKNAME"));
							s[i][1]=c.getString(c.getColumnIndex("ACCOUNTNUMBER"));
							i++;
							
						}
					while(c.moveToNext());}
				}
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	void showAlertDialogBoxforDelete(String accnum)
	{
		final String acc=accnum;
		AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setMessage("Are you sure you want to delete Account?");
        adb.setTitle("Delete Account");
        
        
        adb.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			deleteAccountFromDataBase(acc);
		}
        });
        
        
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			finish();
					}
        });
     adb.setIcon(R.drawable.ic_launcher);
     adb.show();
		
		
	}
	
	void deleteAccountFromDataBase(String accnum)
	{
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		
		try
		{	
			db1.execSQL("DELETE FROM accountdata WHERE ACCOUNTNUMBER ='"+accnum+"';");
			
			db1.execSQL("DROP TABLE '"+accnum+"';");
			
			
		}catch(Exception e)
		{
			
		}
		
	}

	void promptIfNoAccountExist()
	{
		final Dialog dialog=new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialogbox_for_no_account_exist);
		
		dialog.setCancelable(true);
		
		dialog.show();
	}
	
}
