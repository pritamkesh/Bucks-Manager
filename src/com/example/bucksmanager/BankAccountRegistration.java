/* This is source code of Bank Account Registration.
* Source Code written by Pritam Kesh.

* E-mail : pritamkesh.summercode@gmail.com
*
* To report any bugs please send me an e-mail.
* Tips are welcome.
*
*/
package com.example.bucksmanager;

import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BankAccountRegistration extends Activity {
	
	SQLiteDatabase db1 = null;
	private static String DBNAME="DBBANKING.db";
	private Button regnButnSubmit,regnButnReset;
    private	EditText AccountNumber,CardNumber,BankName,BranchName,BranchAddress,IFSC,CurrentBalance,RegisteredNumber,AccountType=null;
    Editable d1,d2,d3,d4,d5,d6,d8,d9=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_bank_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		AccountNumber=(EditText)findViewById(R.id.regnAccountNumber);
		CardNumber=(EditText)findViewById(R.id.regnCardNumber);
		BankName=(EditText)findViewById(R.id.regnBankName);
		BranchName=(EditText)findViewById(R.id.regnBranchName);
		BranchAddress=(EditText)findViewById(R.id.regnBranchAddress);
		IFSC=(EditText)findViewById(R.id.regnIFSC);
		
		RegisteredNumber=(EditText)findViewById(R.id.regnRegisteredNumber);
		AccountType=(EditText)findViewById(R.id.regnAccountType);
	
		
		 
		 regnButnSubmit= (Button) findViewById(R.id.regnButnSubmit);
		 regnButnReset=(Button)findViewById(R.id.regnButnReset);
		 
		 regnButnReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				AccountNumber.setText("");
	        	CardNumber.setText("");
	        	BankName.setText("");
	        	BranchName.setText("");
	        	BranchAddress.setText("");
	        	IFSC.setText("");
	        	RegisteredNumber.setText("");
	        	AccountType.setText("");
				
				
			}
		});
		 
		 regnButnSubmit.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				
				if (addcheckForBlank()==false)
				{
					addcheckForBlank();
					Toast.makeText(getApplicationContext(), "Please Fill The Form", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(checkAccountNumberAlreadyExist()==true)
					{
						AccountNumber.setText("");
						AccountNumber.setHint("Account Number");
						
					}
					else
					{
						insertIntoDatabase();
						finish();
					}
				}
				
			}
		});
}

		boolean checkAccountNumberAlreadyExist()
		{
			boolean flag=false;
			String accnum=AccountNumber.getText().toString();
			
			 db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
			
			try
			{
				Cursor c=db1.rawQuery("SELECT * FROM accountdata WHERE ACCOUNTNUMBER ='"+accnum+"'",null);
				
				if(c!=null)
				{
					if (c.moveToFirst()) 
					{
						do {
							String oldnum=c.getString(c.getColumnIndex("ACCOUNTNUMBER"));
								if(oldnum.equals(accnum)==true)
								{
									flag=true;
									Toast.makeText(getApplicationContext(), "Bank Account cannot be created.\nAccount Number already exists.", Toast.LENGTH_LONG).show();
									break;
								}
							}
						while(c.moveToNext());}
					
				}
			
				
			}catch(Exception e)
			{
				
				db1.execSQL("CREATE TABLE IF NOT EXISTS accountdata(ID INTEGER PRIMARY KEY ,ACCOUNTNUMBER VARCHAR, CARDNUMBER VARCHAR , BANKNAME VARCHAR, BRANCHNAME VARCHAR, BRANCHADDRESS VARCHAR, IFSCCODE VARCHAR, CURRENTBALANCE VARCHAR, RESISTERNUMBER VARCHAR, ACCOUNTTYPE VARCHAR ); ");
			}
			
			
			return flag;
		}
	
		public boolean addcheckForBlank()
		
		 {
				
		 	boolean flag=true;
		 	
		 	if(AccountNumber.getText().toString().trim().equals("")==true)
		 	{
		 		
		 		AccountNumber.setHint("Insert Account Number");
		 		flag=false; 
		 	}
		 	
		 	else if(CardNumber.getText().toString().trim().equals("")==true)
		 	{
		 		
		 		CardNumber.setHint("Invalid Card Number");
		 		flag=false;
		 		
		 	}
		 	else if(BankName.getText().toString().trim().equals("")==true)
		 	{
		 		BankName.setHint("Invalid Bank Name");
		 		flag=false;
		 	}
		 	
		 	else if(BranchName.getText().toString().trim().equals("")==true)
		 	{
		 		BranchName.setHint("Invalid Branch Name");
		 		flag=false;
		 	}
		 	else if(BranchAddress.getText().toString().trim().equals("")==true)
		 	{
		 		BranchAddress.setHint("Invalid Branch Address");
		 		flag=false;
		 	}
		 	else if(IFSC.getText().toString().trim().equals("")==true)
		 	{
		 		IFSC.setHint("Invalid IFSC Code");
		 		flag=false;
		 	}
		 	
		 	
		 	else if(RegisteredNumber.getText().toString().trim().equals("")==true)
		 	{
		 			RegisteredNumber.setHint("Invalid Registered Number");
			 		flag=false;
		 	}
		 	else if(AccountType.getText().toString().trim().equals("")==true)
		 	{
		 		AccountType.setHint("Invalid Account Type");
		 		flag=false;
		 		
		 	}
		 	return flag;
		 }
		
		public void insertIntoDatabase()
		{
			
			d1 =AccountNumber.getText();
        	d2 =CardNumber.getText();
        	d3 =BankName.getText();
        	d4 =BranchName.getText();
        	d5 =BranchAddress.getText();
        	d6 =IFSC.getText();
        	d8 =RegisteredNumber.getText();
        	d9 =AccountType.getText();
        	
        	 db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        	
        	Calendar c = Calendar.getInstance(); 
        	final int day=c.get(Calendar.DAY_OF_MONTH);
        	final int month=c.get(Calendar.MONTH);
        	final int year=c.get(Calendar.YEAR);
        	String Cdate=day+"/"+String.valueOf(month+1)+"/"+year;
        	
        	Toast.makeText(getApplicationContext(), Cdate, Toast.LENGTH_LONG).show();
        	final int hour=c.get(Calendar.HOUR_OF_DAY);
        	final int minute=c.get(Calendar.MINUTE);
        	final int millisec=c.get(Calendar.MILLISECOND);
        	String Ctime=hour+":"+minute+":"+millisec;
        	
        	 
        	
      	try{
        		db1.execSQL("CREATE TABLE IF NOT EXISTS accountdata(ID INTEGER PRIMARY KEY ,ACCOUNTNUMBER VARCHAR, CARDNUMBER VARCHAR , BANKNAME VARCHAR, BRANCHNAME VARCHAR, BRANCHADDRESS VARCHAR, IFSCCODE VARCHAR, CURRENTBALANCE VARCHAR, RESISTERNUMBER VARCHAR, ACCOUNTTYPE VARCHAR ); ");
				db1.execSQL("INSERT INTO accountdata(ACCOUNTNUMBER,CARDNUMBER,BANKNAME,BRANCHNAME,BRANCHADDRESS,IFSCCODE,CURRENTBALANCE,RESISTERNUMBER,ACCOUNTTYPE)  VALUES ('"+d1+"','"+d2+"','"+d3+"','"+d4+"','"+d5+"','"+d6+"','"+0+"','"+d8+"','"+d9+"');");
        		
				db1.execSQL("CREATE TABLE IF NOT EXISTS '"+d1+"'(ID INTEGER PRIMARY KEY,ACCOUNTNUMBER VARCHAR,WITHDRAWL VARCHAR,DEPOSITED VARCHAR,CURRENTBALANCE VARCHAR,DATE VARCHAR,TIME VARCHAR);");
				db1.execSQL("INSERT INTO '"+d1+"' (ACCOUNTNUMBER,WITHDRAWL,DEPOSITED,CURRENTBALANCE,DATE,TIME) VALUES('"+d1+"','"+0+"','"+0+"','"+0+"','"+Cdate+"','"+Ctime+"');");
			
				//other values
				//db1.execSQL("INSERT INTO '"+d1+"' (ACCOUNTNUMBER,WITHDRAWL,DEPOSITED,CURRENTBALANCE,DATE,TIME) VALUES('"+d1+"','"+0+"','"+0+"','"+d7+"','"+Cdate+"','"+Ctime+"');");
				//db1.execSQL("INSERT INTO '"+d1+"' (ACCOUNTNUMBER,WITHDRAWL,DEPOSITED,CURRENTBALANCE,DATE,TIME) VALUES('"+d1+"','"+0+"','"+0+"','"+d7+"','"+Cdate+"','"+Ctime+"');");
				
				Toast.makeText(getApplicationContext(), "Successfully Submited", Toast.LENGTH_LONG).show();
				  
				AccountNumber.setText("");
				CardNumber.setText(""); 
				BankName.setText("");
				BranchName.setText("");
				BranchAddress.setText("");
				IFSC.setText("");
				//CurrentBalance.setText("");
				RegisteredNumber.setText("");
				AccountType.setText("");
				  					  
				  					  
        	} catch(Exception e){
        		Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        		 
	        	}
        	

		}
		
}





	
