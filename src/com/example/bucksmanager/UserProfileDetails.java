/* This is source code of Bank Account Registration.
* Source Code written by Pritam Kesh.

* E-mail : pritamkesh.summercode@gmail.com
*
* To report any bugs please send me an e-mail.
* Tips are welcome.
*
*/
package com.example.bucksmanager;

import com.example.bucksmanager.HomeActivity.customAdapterTransactionValue;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileDetails extends Activity {
	
	SQLiteDatabase db1=null;
	private static String DBNAME="DBBANKING.db";
	String s[][];
	int item=0;
	
	TextView userName,profileName;
	ListView lv;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_window_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		userName=(TextView)findViewById(R.id.profilePersonName);
		profileName=(TextView)findViewById(R.id.ProfileUserName);
		
		lv=(ListView)findViewById(R.id.listAccountDetails);
		
		setUserNameProfileName();
		setAccountNumberDetails();
	}
	
	void setUserNameProfileName()
	{
		String t1="",t2="";
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		try
		{
			Cursor cr=db1.rawQuery("SELECT * FROM userprofile",null);
			 
			 if(cr!=null)
				{
				 	cr.moveToFirst();
						t1=cr.getString(cr.getColumnIndex("NAME"));
						t2=cr.getString(cr.getColumnIndex("USERNAME"));
				} 
			
		}catch(Exception e)
		{
			
		}
		
		userName.setText(t1);
		profileName.setText(t2);
		
	}
	
	void setAccountNumberDetails()
	{
		
		
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		try
		{
			Cursor c=db1.rawQuery("SELECT * FROM accountdata",null);
			item=c.getCount();
			
		}catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "No Account Number Exist", Toast.LENGTH_LONG).show();
		}
		
		if(item>0)
		{
			s=new String[item][2];
			storeAccountNumberInArray();
			lv.setAdapter(new customAdapterAccountDetailsValue());
			
		}
		
	}
	
	class customAdapterAccountDetailsValue extends BaseAdapter
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

}
