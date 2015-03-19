package com.example.bucksmanager;

import android.app.Activity;  
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordForm extends Activity {
	
	
	SQLiteDatabase db1=null;
	private static String DBNAME="DBBANKING.db";
	public Editable p1,p2,p3;
	public EditText currentp, newp , renewp;
	public Button changep,cancelp;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.design_change_password);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		currentp=(EditText)findViewById(R.id.currentPassword);
		newp=(EditText)findViewById(R.id.newPassword);
		renewp=(EditText)findViewById(R.id.retypeNewPassword);
		
		changep=(Button)findViewById(R.id.buttonChange);
		cancelp=(Button)findViewById(R.id.buttonCancel);
		
		
		cancelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
	}
	
	
	public void changePasswordFunction(View v)
	{
		if(checkForBlank()==true)
		{
			if(currentPasswordMatch()==true)
			{
								
				checkNewPasswordMatch();
			}
			
		}
		else
		{
			Toast.makeText(getApplicationContext(),"Please Fill the Form", Toast.LENGTH_LONG).show();
		}
		
		
		
	}
	
	public void checkNewPasswordMatch()
	{
		String s2,s3;
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		
		s2=newp.getText().toString();
		s3=renewp.getText().toString();
		
		if(s2.equals(s3)==true)
		{
			if(checkForExistingPassword()==true)
			{
				db1.execSQL("UPDATE userprofile SET PASSWORD='"+s2+"'");
				db1.execSQL("INSERT INTO passworddata (PASSWORD)  VALUES ('"+s2+"');");
				
				newp.setText("");
				renewp.setText("");
				currentp.setText("");
				
				newp.setHint("New Password");
				renewp.setHint("Re-type New Password");
				currentp.setHint("Current Password");
				Toast.makeText(getApplicationContext(),"Password Changed Successfully ", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			newp.setText("");
			newp.setHint("New Password Didnot Matched");
			
			renewp.setText("");
			renewp.setHint("New Password Didnot Matched");
		}
		
	}
	
	public boolean checkForExistingPassword()
	{
		boolean flag=true;
		String str=newp.getText().toString();
		
		
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		
		try
		{
			Cursor c=db1.rawQuery("SELECT * FROM passworddata WHERE PASSWORD ='"+str+"'",null);
			
			if(c!=null)
			{
				if (c.moveToFirst()) 
				{
					do {
						String oldpass=c.getString(c.getColumnIndex("PASSWORD"));
							if(oldpass.equals(str)==true)
							{
								flag=false;
								Toast.makeText(getApplicationContext(),"Its an old Password, Please Provide a New Password ", Toast.LENGTH_LONG).show();
								break;
							}
						}
					while(c.moveToNext());}
				
				
			}
		
			
		}catch(Exception e)
		{
			flag=true;
			
		}	
		
		
		return flag;
	}
	
	public boolean checkForBlank()
	{
		boolean flag=true;
			
		if(currentp.getText().toString().equals("")==true)
		{
			currentp.setText("");
			currentp.setHint("Invalide Current Password");
			flag=false;
		}
		if(newp.getText().toString().equals("")==true)
		{
			newp.setText("");
			newp.setHint("Invalide New Password");
			flag=false;
		}
		if(renewp.getText().toString().equals("")==true)
		{
			renewp.setText("");
			renewp.setHint("Invalide Re-Type New Password");
			flag=false;
		}
		
		
		return flag;
	}
	
	
	
	public boolean currentPasswordMatch()
	{
		boolean flag=false;
		
		String s1=currentp.getText().toString();
		
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		
		try
		{
			Cursor c=db1.rawQuery("SELECT * FROM userprofile WHERE PASSWORD ='"+s1+"'",null);
			
			if(c!=null)
			{
				c.moveToFirst();
				String pass=c.getString(c.getColumnIndex("PASSWORD"));
				
				if(s1.equals(pass)==true)
				{
					flag=true;
				}
				
			}
		
			
		}catch(Exception e)
		{
			currentp.setText("");
			currentp.setHint("Invalid Current Password");
		}	
		
		
		return flag;
	} 

}
