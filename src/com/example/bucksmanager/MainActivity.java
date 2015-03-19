package com.example.bucksmanager;
import android.os.Bundle;  
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static int SPLASH_TIME_OUT = 1600;
	Button signin;
	Button signup;
	SQLiteDatabase db1=null;
	private static String DBNAME="DBBANKING.db";
	public EditText nuser,npassword;
	
	public int trim1=0,trim2=0;
	
	LinearLayout l1;
	LinearLayout l2;
	LinearLayout l3;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//cookies for loged in
		//SharedPreferences signData= PreferenceManager.getDefaultSharedPreferences(this);
		
		/*if (signData.getBoolean("locked",false )==true) 
		{ 
			startActivity(new Intent(this, HomeActivity.class));
			this.finish();
		}*/
		
		
		
		setContentView(R.layout.activity_main);
		
		l1=(LinearLayout)findViewById(R.id.linearLayout1);
		l2=(LinearLayout)findViewById(R.id.linearLayout2);
		l3=(LinearLayout)findViewById(R.id.linearlayout3);
		
		
		
		nuser=(EditText)findViewById(R.id.userName);
		npassword=(EditText)findViewById(R.id.passwordID);
		
		signin=(Button)findViewById(R.id.signIN);
		signup=(Button)findViewById(R.id.signUP);
		
		Intent in=getIntent();
		if (in.getBooleanExtra("EXIT", false))
		{	
			l3.setVisibility(LinearLayout.GONE);
			
		}
		else
		{
			SharedPreferences signUpremove= PreferenceManager.getDefaultSharedPreferences(this);
			//cookies for removing signup button
			if (signUpremove.getBoolean("SIGNUPED",false )==true) 
			{ 
				loginDesignFunctionWithoutSignUP();
			}
			else
			{
				loginDesignFunctionWithoutSignIN();
			}
		}
		
	 	
		
		
		signin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				performLogin();
			}
		});
		
		
		signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent nextScreen1=new Intent(getApplicationContext(),SignUpWindow.class);
				
				startActivity(nextScreen1);
			}
		});
		
	}
	
	public void performLogin()
	{
		if(checkForBlank()==true)
		{
			fetchFromDataBase();
		}
		else
		{
			if((trim1==1) && (trim2==1) )
			{
				Toast.makeText(getApplicationContext(), "Please Enter UserName & Password", Toast.LENGTH_LONG).show();
				trim1=0;
				trim2=0;
			}
			else if(trim1==1)
			{
				Toast.makeText(getApplicationContext(), "Please Enter UserName", Toast.LENGTH_LONG).show();
				trim1=0;
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
				trim2=0;
			}
			
		}
		
	}
	
	
	public void fetchFromDataBase()
	{
		String s1=nuser.getText().toString().trim();
		String s2=npassword.getText().toString().trim();
		
		db1=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		
		try
		{
			Cursor c=db1.rawQuery("SELECT * FROM userprofile WHERE USERNAME ='"+s1+"'",null);
			
			if(c!=null)
			{
				c.moveToFirst();
				
				String pass=c.getString(c.getColumnIndex("PASSWORD"));
				
				if(s2.equals(pass)==true)
				{
					Toast.makeText(getApplicationContext(), "Welcome "+c.getString(c.getColumnIndex("USERNAME")), Toast.LENGTH_LONG).show();
					
					//Share Preference
					//SharedPreferences signData= PreferenceManager.getDefaultSharedPreferences(this); 
					//signData.edit().putBoolean("locked",true).commit();
					
					Intent nextScreen3=new Intent(getApplicationContext(),HomeActivity.class);
					startActivity(nextScreen3);
					this.finish();
				}
				else
				{
					npassword.setText("");
					npassword.setHint("Invalid Password");
				}
				
				
			}
		
			
		}catch(Exception e)
		{
			nuser.setText("");
			nuser.setHint("Invalid User Name");
			//Toast.makeText(getApplicationContext(), "Error "+e, Toast.LENGTH_LONG).show();
		}
		
	}
	
	public boolean checkForBlank()
	{
		boolean flag=true;
		
		if(nuser.getText().toString().trim().equals("")==true)
		{
			nuser.setText("");
			flag=false;
			trim1=1;
		}
		if(npassword.getText().toString().trim().equals("")==true)
		{
			npassword.setText("");
			flag=false;
			trim2=1;
		}
		
		
		
		return flag;
	}

	
	public void loginDesignFunctionWithoutSignIN()
	{
		
		l2.setVisibility(LinearLayout.GONE);
		l3.setVisibility(LinearLayout.GONE);
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				
				l3.setVisibility(LinearLayout.VISIBLE);
				// close this activity
			
			}
		}, SPLASH_TIME_OUT);
		
	}
	
	public void loginDesignFunctionWithoutSignUP()
	{
		l2.setVisibility(LinearLayout.GONE);
		l3.setVisibility(LinearLayout.GONE);
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				
				l2.setVisibility(LinearLayout.VISIBLE);
				
				// close this activity
			
			}
		}, SPLASH_TIME_OUT);
		
	}
	
	
}
