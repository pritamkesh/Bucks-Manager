/* This is source code of Bank Account Registration.
* Source Code written by Pritam Kesh.

* E-mail : pritamkesh.summercode@gmail.com
*
* To report any bugs please send me an e-mail.
* Tips are welcome.
*
*/
package com.example.bucksmanager;

import android.app.Activity; 
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUpWindow extends Activity {
	
	
	SQLiteDatabase db1=null;
	private static String DBNAME="DBBANKING.db";
	public Editable n1,u1,p1;
	public String gen;
	public Button signup;
	public EditText pname,username, password,cnfpassword;
	public RadioButton r1,r2;
	String s;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.design_signup_window);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		pname=(EditText)findViewById(R.id.personName);
		username=(EditText)findViewById(R.id.userid);
		password=(EditText)findViewById(R.id.passwordNEW);
		cnfpassword=(EditText)findViewById(R.id.confirmpassword);
		r1=(RadioButton)findViewById(R.id.radio1);
		r2=(RadioButton)findViewById(R.id.radio2);
		
		
		
		
		signup=(Button)findViewById(R.id.signupButttonForm);
		
		
	}
	
	public void completeSignupProcess(View v)
	{
		
		if(checkForBlank()==true)
		{
			if(confirmPassword()==true)
			{
				addProfileInDatabase();
				
				this.finish();
				
			}
			else
			{
				password.setText("");
				password.setHint("Password didnot matched");
				cnfpassword.setText("");
				cnfpassword.setHint("Password didnot matched");
			}
			
		}
		else
		{
			Toast.makeText(getApplicationContext(), "It seem that you have some Empty Field", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	public void addProfileInDatabase()
	{
		
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);

		n1=pname.getText();
		u1=username.getText();
		p1=password.getText();
		
		try{
			db1.execSQL("CREATE TABLE IF NOT EXISTS userprofile(ID INTEGER PRIMARY KEY ,NAME VARCHAR,GENDER VARCHAR, USERNAME VARCHAR ,PASSWORD VARCHAR ); ");
			db1.execSQL("INSERT INTO userprofile(NAME,GENDER,USERNAME,PASSWORD)  VALUES ('"+n1+"','"+gen+"','"+u1+"','"+p1+"');");
			//INSERT INTO tabq34 (FIRSTNAME,GENDER,MIDDLENAME,LASTNAME)  VALUES ('Rudra','Prasad','Mukherjee');
			//INSERT INTO tabq34 (FIRSTNAME,GENDER,MIDDLENAME,LASTNAME)  VALUES ("Amio","Bhusan","Sharma");
			
			db1.execSQL("CREATE TABLE IF NOT EXISTS passworddata(ID INTEGER PRIMARY KEY,PASSWORD VARCHAR);");
			db1.execSQL("INSERT INTO passworddata(PASSWORD) VALUES('"+p1+"');");
			
			//SMS number table created
			//db1.execSQL("CREATE TABLE IF NOT EXISTS SMSDATABASE(ID INTEGER PRIMARY KEY,smsnumber VARCHAR);");
			Toast.makeText(getApplicationContext(), "Sign Up Successful...\nYou are allowed to Sign Up only once.", Toast.LENGTH_LONG).show();
			
			SharedPreferences signUpremove= PreferenceManager.getDefaultSharedPreferences(this); 
			signUpremove.edit().putBoolean("SIGNUPED",true).commit();
			
			
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i.putExtra("EXIT", true);
			startActivity(i);
		    finish();
			
		} catch(Exception e){
			
		}
		
		
		
	}
	
	public boolean confirmPassword()
	{
		boolean f=true;
		String s1,s2;
		
		s1=password.getText().toString().trim();
		s2=cnfpassword.getText().toString().trim();
		
		if(s1.equals(s2)==true)
		{
			
			f=true;
		}
		else
		{
			f=false;
		}
		
		return f;
	}
	
	public boolean checkForBlank()
	{
		boolean flag=true, t=false;
		
		if(pname.getText().toString().trim().equals("")==true)
		{	
			pname.setText("");
			pname.setHint("Invalid Name");
			flag=false; 
		}
		if(username.getText().toString().trim().equals("")==true)
		{
			username.setText("");
			username.setHint("Invalid User-Name");
			flag=false;
		}
		if(password.getText().toString().trim().equals("")==true)
		{
			password.setText("");
			password.setHint("Invalid Password");
			flag=false;
		}
		if(cnfpassword.getText().toString().trim().equals("")==true)
		{
			cnfpassword.setText("");
			cnfpassword.setHint("Invalid Confirm Password");
			flag=false;
		}
		
		if(r1.isChecked()==false)
		{
			if(r2.isChecked()==false)
			{	flag=false;
			}
			else
			{
				gen=r2.getText().toString();
			}
			
		}
		else
		{
			gen=r1.getText().toString();
		}
				
		return flag;
	}
	
	
}
