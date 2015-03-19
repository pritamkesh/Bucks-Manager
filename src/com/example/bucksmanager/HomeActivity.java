package com.example.bucksmanager;
//import com.example.android.adapter.ImageAdapter;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;

public class HomeActivity extends Activity {
	
	SQLiteDatabase db1=null;
	private static String DBNAME="DBBANKING.db";
	int item=0;
	String s[][];
	
	GridView gridView;
	final Context context=this;

	static final String[] HOME_MENU = new String[] {"Profile", "Balance","Transaction History", "Settings" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		gridView = (GridView) findViewById(R.id.gridView1);

		gridView.setAdapter(new ImageAdapter(this, HOME_MENU));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				
				switch(position)
				{
				case 0:
					Intent screenProfile=new Intent(getApplicationContext(),UserProfileDetails.class);
					startActivity(screenProfile);
					break;
				
				case 1:
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
						openCustomDialogBoxForBalance();
					}
					else
					{
						promptIfNoAccountExist();
					}
					break;
				case 2:
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
						openCustomDialogBox();
					}
					else
					{
						promptIfNoAccountExist();
					}
					break;
				case 3:
						Intent screenSetting=new Intent(getApplicationContext(),SettingsActivity.class);
						startActivity(screenSetting);
						break;
				}
			}
		});

	}
	
	void openCustomDialogBox()
	{
		final Dialog dialog=new Dialog(context);
		dialog.setContentView(R.layout.custom_dialog_transaction);
		dialog.setTitle("Select Account");
		dialog.setCancelable(true);
		
		

		ListView lv=(ListView)dialog.findViewById(R.id.listAccountNumber);
		lv.setAdapter(new customAdapterTransactionValue());
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int listPosition,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent screenTransaction=new Intent(getApplicationContext(),TransactionHistory.class);
				screenTransaction.putExtra("AccountNumber",s[listPosition][1]);
				startActivity(screenTransaction);
				dialog.dismiss();
				
			}
			
		});
			
		dialog.show();
	}
	
	void openCustomDialogBoxForBalance()
	{
		final Dialog dialog=new Dialog(context);
		dialog.setContentView(R.layout.custom_dialog_transaction);
		dialog.setTitle("Select Account");
		dialog.setCancelable(true);
		
		

		ListView lv=(ListView)dialog.findViewById(R.id.listAccountNumber);
		lv.setAdapter(new customAdapterTransactionValue());
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int listPosition,
					long arg3) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				showCustomDialogForCurrentBalance(s[listPosition][1]);
				
			}
			
		});
			
		dialog.show();
		
	}
	
	
	
	class customAdapterTransactionValue extends BaseAdapter
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
	
	void showCustomDialogForCurrentBalance(String accnum)
	{
		String balance="";
		
		final Dialog dialog=new Dialog(context);
		dialog.setContentView(R.layout.custom_dialog_currentbalance_layout);
		dialog.setTitle("Current Balance");
		dialog.setCancelable(true);
		
		TextView t1=(TextView)dialog.findViewById(R.id.currentAccountNumber);
		TextView t2=(TextView)dialog.findViewById(R.id.currentBalance);
		TextView t3=(TextView)dialog.findViewById(R.id.currentDate);
		TextView t4=(TextView)dialog.findViewById(R.id.currentTime);
		
     	Calendar c = Calendar.getInstance(); 
     	final int day=c.get(Calendar.DAY_OF_MONTH);
     	final int month=c.get(Calendar.MONTH);
     	final int year=c.get(Calendar.YEAR);
     	String Cdate=day+"/"+month+"/"+year;
     	
     	final int hour=c.get(Calendar.HOUR_OF_DAY);
     	final int minute=c.get(Calendar.MINUTE);
     	final int millisec=c.get(Calendar.MILLISECOND);
     	String Ctime=hour+":"+minute+":"+millisec;
		

		 db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
    	
		 try
		{
			 Cursor ch=db1.rawQuery("SELECT * FROM accountdata WHERE ACCOUNTNUMBER ='"+accnum+"'",null);
				
				if(ch!=null)
				{
					ch.moveToFirst();
					
					balance=ch.getString(ch.getColumnIndex("CURRENTBALANCE"));
					
				}
			
		}catch(Exception e)
		{
			
		}
		
		 t1.setText(accnum);
		 t2.setText("Rs  "+balance);
		 t3.setText(Cdate);
		 t4.setText(Ctime);
		
		dialog.show();
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
