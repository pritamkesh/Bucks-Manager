package com.example.bucksmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class TransactionHistory extends Activity {
	
	SQLiteDatabase db1=null;
	String intentData="";
	private static String DBNAME="DBBANKING.db";
	
	protected ListView tview;
	int numOfAccount=0,item=0;
	String a[][];
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_history);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		tview=(ListView)findViewById(R.id.listTransactions);
		tview.setAdapter(new transactionAdapterValue());
		
		Intent id=getIntent();
		intentData=id.getStringExtra("AccountNumber");
		
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		try
		{
			Cursor c = db1.rawQuery("SELECT * FROM '"+intentData+"'",null);
				item=c.getCount();
		}catch(Exception e)
		{
			
		}
		
		
		try
		{
			Cursor c=db1.rawQuery("SELECT * FROM '"+intentData+"'",null);
			item=c.getCount();
			if(item>=15)
			{
				a=new String[15][6];
			}
			else
			{
				a=new String[item][6];
			}
			
			int i=0;
			
			
			if(c!=null)
			{
				if (c.moveToLast()) 
				{
					do {
							a[i][0]=c.getString(c.getColumnIndex("ACCOUNTNUMBER"));
							a[i][1]=c.getString(c.getColumnIndex("WITHDRAWL"));
							a[i][2]=c.getString(c.getColumnIndex("DEPOSITED"));
							a[i][3]=c.getString(c.getColumnIndex("CURRENTBALANCE"));
							a[i][4]=c.getString(c.getColumnIndex("DATE"));
							a[i][5]=c.getString(c.getColumnIndex("TIME"));
							
							i++;
							
						}
					while(c.moveToPrevious());}
				}
			
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "A/c No. "+intentData+" doesnot contain any Data.", Toast.LENGTH_LONG).show();
			
		}
		
	}
	
	class transactionAdapterValue extends BaseAdapter
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
				rview=layinf.inflate(R.layout.transaction_activity_listview, arg2,false);

				final TextView tnumber=(TextView)rview.findViewById(R.id.textAccountNumber);
				final TextView twithdrawl=(TextView)rview.findViewById(R.id.textWithdrawl);
				final TextView tdeposited=(TextView)rview.findViewById(R.id.textDeposited);
				final TextView tbalance=(TextView)rview.findViewById(R.id.textBalance);
				final TextView tdate=(TextView)rview.findViewById(R.id.textDate);
				final TextView ttime=(TextView)rview.findViewById(R.id.textTime);
				
				tnumber.setText(a[arg0][0]);
				twithdrawl.setText("Rs. "+a[arg0][1]);
				tdeposited.setText("Rs. "+a[arg0][2]);
				tbalance.setText("Rs. "+a[arg0][3]);
				tdate.setText(a[arg0][4]);
				ttime.setText(a[arg0][5]);
			}
			
			return rview;
		}
		
	}
	

}
