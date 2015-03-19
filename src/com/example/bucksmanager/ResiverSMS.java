/* This is source code of Bank Account Registration.
* Source Code written by Pritam Kesh.

* E-mail : pritamkesh.summercode@gmail.com
*
* To report any bugs please send me an e-mail.
* Tips are welcome.
*
*/
package com.example.bucksmanager;

import java.text.DecimalFormat; 
import java.util.ArrayList;      
import java.util.Calendar;
import java.util.StringTokenizer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/*Create class with extending BroadcastReceiver so in onReceive method we can write code which will read each new incoming sms when it comes to mobile.*/

public class ResiverSMS extends BroadcastReceiver {
	
	private ArrayList<String> token1=new ArrayList<String>();
	 private ArrayList<String> token=new ArrayList<String>();

	SQLiteDatabase db1=null;
	private static String DBNAME="DBBANKING.db";
	
	static private int[] failure;
	String msgbody,msgNumber,body,num;
	boolean flag=false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		  
		db1=context.openOrCreateDatabase(DBNAME, context.MODE_PRIVATE,null);
		
		//—get the SMS message passed in—
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String messages = "";
		//cont=context;
		
		if (bundle != null)
		{
			//—retrieve the SMS message received—
			Object[] smsExtra=(Object[]) bundle.get("pdus");
			msgs=new SmsMessage[smsExtra.length];
			

			for (int i=0; i<msgs.length; i++)
			{
				SmsMessage sms=SmsMessage.createFromPdu((byte[])smsExtra[i]);
				
				//take out content from SMS
				body=sms.getMessageBody().toString();
				num=sms.getOriginatingAddress();
			}
			
			msgbody=body;
			msgNumber=num;
		}
		
		//—display the new SMS message—
		//Toast.makeText(context,"Number : "+msgNumber+"\nBody : "+msgbody , Toast.LENGTH_SHORT).show();

		
		try
		{
			//create message table
			db1.execSQL("CREATE TABLE IF NOT EXISTS storemessage(ID INTEGER PRIMARY KEY,NUMBER VARCHAR,BODY VARCHAR);");
			
			Cursor c=db1.rawQuery("SELECT * FROM SMSDATABASE WHERE smsnumber ='"+msgNumber+"'",null);
			if(c!=null)
			{
				if (c.moveToFirst()) 
				{
					do {
							String storedNumber=c.getString(c.getColumnIndex("smsnumber"));
							if(storedNumber.equals(msgNumber)==true)
							{
								flag=true;
								
								db1.execSQL("INSERT INTO storemessage (NUMBER,BODY) VALUES ('"+msgNumber+"','"+msgbody+"');");
								Toast.makeText(context,"Bucks Manager..\nNumber : "+msgNumber+"\nBody : "+msgbody , Toast.LENGTH_SHORT).show();
								manupulateSMSbody(context,msgbody);
								break;
							}
						}while(c.moveToNext());
				}
			}
			}catch(Exception e)
			{
				
				db1.execSQL("CREATE TABLE IF NOT EXISTS SMSDATABASE(id INTEGER PRIMARY KEY,smsnumber VARCHAR);");
			}	

		
	}
	
	void manupulateSMSbody(Context context,String body)
	{
		getTokensFromSMS(context,body);
		
		identifyEachTokensAndDecode(context);
		
		
	}
	
	void getTokensFromSMS(Context context,String s)
	{
		String str="";

		int i;
		String key,val,temp="'",lower="";
		
		s.trim();
		s=s+" ";
		
		System.out.println("\n"+s+"\n\n");
		StringTokenizer st = new StringTokenizer(s," ()");
		
		while(st.hasMoreTokens()) { 
		key=st.nextToken();
		
		token1.add(key);
		
		} 
		
		//get all token
		for(i=0;i<token1.size();i++)
		{
			temp=checkEachToken(token1.get(i));
			token1.set(i,"");
			lower=changeStringToLowerCase(temp);
			token1.set(i,lower);
			
			
		}
		
		//get token without period between letters and numbers
		for(i=0;i<token1.size();i++)
		{
			checkPeriodBetweenNumberAndLetter(token1.get(i));
			
		}
		
		
		
		
		/*for(i=0;i<token.size();i++)
		{
			str=str+"\n"+token.get(i);
		}
		
		Toast.makeText(context,"SMS Tokens :\n"+str, Toast.LENGTH_LONG).show();
		*/

	}
	
	String checkEachToken(String n)
	{
		String ss="",tt="";
		
		
		ss=checkNumbers(n);
		tt=removePeriodFrontBack(ss);
		
		return tt;
	}
	
	String removePeriodFrontBack(String n)
	{
		String t="",m="";
		char ch=' ';
		int i=0,pos=0;
		int x;
		boolean flag=false;
		
		
		//remove period from front
		for(i=0;i<n.length();i++)
		{
			ch=n.charAt(i);
			
			if(Character.isLetterOrDigit(ch)==true)
			{
				flag=true;
				m=n.substring(i);
				break;
			}
			
		}
		
		
		if(flag==true)
		{
			t=m;
			m="";
			flag=false;
		}
		else
		{
			t=n;
			flag=false;
		}
		
		//remove Period from Back
		
		for(i=t.length()-1;i>=0;i--)
		{
			ch=t.charAt(i);
			
			if(Character.isLetterOrDigit(ch)==true)
			{
				t=t.substring(0,i+1);
				break;
			}
			
		}
		
		
		
		return t;
	}
	
	String checkNumbers(String n)
	{
		int i;
		String t="";
		char p=' ';
		boolean flag=false;
		
			n.trim();
			
			for(i=0;i<n.length();i++)
			{
				p=n.charAt(i);
				
				if(Character.isDigit(p)==true)
				{
					t=t+p;
				}
				else if(p==',')
				{
					flag=true;
					continue;
				}
				else
				{
					t=t+p;
				}
			}
		
		if(flag==true)
			return t;
		else
			return n;
	}
	
	String changeStringToLowerCase(String m)
	{
		int i,x=0;
		String f="";
		char pp;
		
		for(i=0;i<m.length();i++)
		{
			pp=m.charAt(i);
			x=(int)pp;
			
			if(Character.isUpperCase(pp)==true)
			{
				f=f+Character.toLowerCase(pp);
			}
			else if(Character.isLowerCase(pp)==true)
			{
				f=f+pp;
			}
			else
			{
				f=f+pp;
			}
			
					
		}
		
		return f;
	}
	
	void checkPeriodBetweenNumberAndLetter(String string)
	{
		String t1="",t2="",t="";
		boolean flag=false;
		int i;
		char ch=' ',p=' ';
		
		for(i=0;i<string.length()-1;i++)
		{
			ch=string.charAt(i);
			p=string.charAt(i+1);
			
			if((Character.isLetter(ch)==true) && (p=='.')==true )
			{
				
				if(Character.isDigit(string.charAt(i+2))==true)
				{
					flag=true;
					t1=string.substring(0,i+1);
					t2=string.substring(i+2);
					
				}
			}
			 
		}
		
		if(flag==true)
		{
			token.add(t1);
			token.add(t2);
		}
		else
		{
			token.add(string);
		}
		
	}
	
	void identifyEachTokensAndDecode(Context context)
	{
		boolean patternMatch=false;
		String realNumber="";
		String encriptedAccount="";
		String getAccountPattern="";
		
		encriptedAccount=searchEncriptedAccountNumber();
		getAccountPattern=searchAccountNumberPattern(encriptedAccount.trim());
		
		
		try
		{
			Cursor c=db1.rawQuery("SELECT ACCOUNTNUMBER FROM accountdata",  null);
			
			if(c!=null)
			{
				if (c.moveToFirst()) 
				{
					do {
							String accountNumber=c.getString(c.getColumnIndex("ACCOUNTNUMBER"));
							
							patternMatch=KnuthMorisAlgorithm(accountNumber,getAccountPattern);
							
							if(patternMatch==true)
							{
								realNumber=accountNumber;
								//Toast.makeText(context,"Account Number : "+realNumber, Toast.LENGTH_LONG).show();
								break;
							}
							
						}while(c.moveToNext());
				}
			}

			
		}catch(Exception e)
		{
			//Toast.makeText(context,"Error in Knuth Moris", Toast.LENGTH_LONG).show();
		}
		
		
		if(patternMatch==true)
		{
			updateMyBankAccount(context,realNumber);
		}
		else
		{
			//Toast.makeText(context,"Account Number Pattern didnot matched", Toast.LENGTH_LONG).show();
		}
		
	}
	
	String searchEncriptedAccountNumber()
	{
		String t="";
		boolean m=false;
		int i=0,pos=0;
		
		
		while(i<token.size())
		{
			if(token.get(i).equalsIgnoreCase("account")==true || token.get(i).equalsIgnoreCase("a/c")==true)
			{
				pos=i;
				break;
			}
			
			i++;
		}
		
		
		for(int j=pos+1;j<token.size();j++)
		{
			m=getAccountNumber(token.get(j));
			
			if(m==true)
			{
				t=token.get(j);
				break;
			}
		}
		
		
		if(m==true)
			return t;
		else
			return token.get(pos+1);
	}
	
	boolean getAccountNumber(String sm)
	{
		char ch=' ',p1=' ';
		boolean f=false;
		
			
			if(Character.isDigit(sm.charAt(sm.length()-1))==true)
			{
				for(int k=sm.length()-1;k>0;k--)
				{
					ch=sm.charAt(k);
					p1=sm.charAt(k-1);
					
					if((Character.isDigit(ch)==true) && (p1=='x'))
					{
						f=true;
						break;
					}
					
				}
			}
			else
			{
				for(int k=0;k<sm.length()-1;k++)
				{
					ch=sm.charAt(k);
					p1=sm.charAt(k+1);
					
					if((Character.isDigit(ch)==true) && (p1=='x'))
					{
						f=true;
						break;
					}
				}
				
			}
		return f;	
	}
	
	String searchAccountNumberPattern(String n)
	{
		String pattern="";
		char p=' ',ch=' ';
		int i;
		boolean flag=false;
		
			if((n.charAt(n.length()-1)=='x') || (n.charAt(n.length()-1)=='X'))
			{
				for(i=0;i<n.length()-1;i++)
				{
					p=n.charAt(i);
					ch=n.charAt(i+1);
					
					if((Character.isDigit(p)==true) && (ch=='x'))
					{
						flag=true;
						pattern=pattern+p;
						break;
					}
					else
					{
						
						pattern=pattern+p;
					}
					
					
				}
				
			}
			else
			{
				
				for(i=n.length()-1;i>0;i--)
				{
					p=n.charAt(i);
					ch=n.charAt(i-1);
					
					if((Character.isDigit(p)==true) && (ch=='x'))
					{
						flag=true;
						pattern=p+pattern;
						break;
					}
					else
					{
						pattern=p+pattern;
					}
				}
			}
		
			if(flag==true)
				return pattern;
			else
				return n;
	}
	
	boolean KnuthMorisAlgorithm(String text, String pat)
	{
		
		// pre construct failure array for a pattern
        failure = new int[pat.length()];
        
        fail(pat);
        
        // find match
        int pos=posMatch(text, pat);
        
        if (pos == -1)
            return false;
        else
            return true;
    }
    
    /** Failure function for a pattern **/
    void fail(String pat)
    {
        int n = pat.length();
        int j,i;
        failure[0]=-1;
        
        for (j=1;j<n;j++)
        {
            i=failure[j-1];
            
            while ((pat.charAt(j) != pat.charAt(i + 1)) && i >= 0)
                i = failure[i];
            
            if (pat.charAt(j) == pat.charAt(i + 1))
                failure[j] = i + 1;
            else
                failure[j] = -1;
        }
    }
    
    /** Function to find match for a pattern **/
    int posMatch(String text, String pat)
    {
        int i= 0,j=0;
        int lens=text.length();
        int lenp=pat.length();
        while ((i<lens) && (j<lenp))
        {
            if (text.charAt(i)==pat.charAt(j))
            {
                i++;
                j++;
            }
            else if (j==0)
                i++;
            else
                j=failure[j-1]+1;
        }
        return ((j==lenp)?(i-lenp):-1);
    }

	void updateMyBankAccount(Context context,String realNumber)
	{
		String t="",smsAmount="";
		boolean asap=false;
		int i;
		
		for(i=token.size()-2;i>=0;i--)
		{
			t=token.get(i);
			
			if((t.equalsIgnoreCase("rs")==true) || (t.equalsIgnoreCase("inr")==true))
			{
				if(checkForAmountPatternMatch(token.get(i+1))==true)
				{
					smsAmount=token.get(i+1);
					asap=true;
					
					//Toast.makeText(context, "Available Balance : Rs "+smsAmount, Toast.LENGTH_LONG).show();
					break;
				}
			}
		}
		if(asap==true)
		{
			updateAmountInDatabase(context,realNumber,smsAmount);
		}
		/*else
		{
			Toast.makeText(context, "No Amount Found", Toast.LENGTH_LONG).show();
		}*/
		
	}
	 boolean checkForAmountPatternMatch(String qwerty)
	    {
	    	char p=' ',ch=' ';
	    	int i=0;
	    	boolean flag=false;
	    	
	    	
	    		for(i=0;i<qwerty.length()-1;i++)
	    		{
	    			p=qwerty.charAt(i);
	    			ch=qwerty.charAt(i+1);
	    			
	    			if((Character.isDigit(p)==true) && (ch=='.'))
	    			{
	    				if(Character.isDigit(qwerty.charAt(i+2))==true)
	    				{
	    					flag=true;
	    					break;
	    				}
	    			}
	    			
	    		}
	    	
	    	return flag;
	    }

	 void updateAmountInDatabase(Context context,String realNumber,String smsAmount)
	 {
		 int times=0;
		 try
		 {
			 Cursor c=db1.rawQuery("SELECT * FROM '"+realNumber+"'",null);
			 times=c.getCount();
			 
		 }catch(Exception e)
		 {
			 db1.execSQL("CREATE TABLE IF NOT EXISTS '"+realNumber+"'(ID INTEGER PRIMARY KEY,ACCOUNTNUMBER VARCHAR,WITHDRAWL VARCHAR,DEPOSITED VARCHAR,CURRENTBALANCE VARCHAR,DATE VARCHAR,TIME VARCHAR);");
		 }
		 
		 if(times==1)
		 {
			 justPutAmountInDatabase(context,realNumber,smsAmount);
		 }
		 else if(times>1)
		 {
			 justPutAmountInDatabaseWithCreditAndDebit(context,realNumber,smsAmount);
		 }
		 
	 }
	 
	 void justPutAmountInDatabase(Context context,String realNumber,String smsAmount)
	 {
		 
     	Calendar c=Calendar.getInstance(); 
     	final int day=c.get(Calendar.DAY_OF_MONTH);
     	final int month=c.get(Calendar.MONTH);
     	final int year=c.get(Calendar.YEAR);
     	String Cdate=day+"/"+String.valueOf(month+1)+"/"+year;
     	
     	final int hour=c.get(Calendar.HOUR_OF_DAY);
     	final int minute=c.get(Calendar.MINUTE);
     	final int millisec=c.get(Calendar.MILLISECOND);
     	String Ctime=hour+":"+minute+":"+millisec;
     	
     	setNotification(context);
		 try
		 {
			 db1.execSQL("INSERT INTO '"+realNumber+"' (ACCOUNTNUMBER,WITHDRAWL,DEPOSITED,CURRENTBALANCE,DATE,TIME) VALUES('"+realNumber+"','"+0+"','"+0+"','"+smsAmount+"','"+Cdate+"','"+Ctime+"');");
			 
			 db1.execSQL("UPDATE accountdata SET CURRENTBALANCE ='"+smsAmount+"' WHERE ACCOUNTNUMBER ='"+realNumber+"'");
			 
		 }catch(Exception e)
		 {
			 
		 }
		 
	 }
	 
	 void justPutAmountInDatabaseWithCreditAndDebit(Context context,String realNumber,String smsAmount)
	 {
		 String t1,t2;
		 String balFormat="";
		 float previousBalance=0;
		 float changedBalance=0;
		 float smsBalance=Float.parseFloat(smsAmount);
		 
		 
		 Calendar c = Calendar.getInstance(); 
     	final int day=c.get(Calendar.DAY_OF_MONTH);
     	final int month=c.get(Calendar.MONTH);
     	final int year=c.get(Calendar.YEAR);
     	String Cdate=day+"/"+String.valueOf(month+1)+"/"+year;
     	
     	final int hour=c.get(Calendar.HOUR_OF_DAY);
     	final int minute=c.get(Calendar.MINUTE);
     	final int millisec=c.get(Calendar.MILLISECOND);
     	String Ctime=hour+":"+minute+":"+millisec;
     	
		 
		 try
		 {
			 Cursor cr=db1.rawQuery("SELECT * FROM accountdata WHERE ACCOUNTNUMBER ='"+realNumber+"'",null);
			 
			 if(cr!=null)
				{
				 	cr.moveToFirst();
						t1=cr.getString(cr.getColumnIndex("CURRENTBALANCE"));
						previousBalance=Float.parseFloat(t1);
				} 
			 
		 }catch(Exception e)
		 	{}
		 
		 if(smsBalance>previousBalance)
		 {
			
			 //Creadited
			 
			 setNotification(context);
			 changedBalance=smsBalance-previousBalance;
			 t2=String.valueOf(changedBalance);
			 //balFormat=getBalanceFormat(t2);
			 
			 try
			 {
				 db1.execSQL("INSERT INTO '"+realNumber+"' (ACCOUNTNUMBER,WITHDRAWL,DEPOSITED,CURRENTBALANCE,DATE,TIME) VALUES('"+realNumber+"','"+0+"','"+t2+"','"+smsAmount+"','"+Cdate+"','"+Ctime+"');");
				 
				 db1.execSQL("UPDATE accountdata SET CURRENTBALANCE ='"+smsAmount+"' WHERE ACCOUNTNUMBER ='"+realNumber+"'");
				 
			 }catch(Exception e)
			 {
				 
			 }
			 
		 }
		 else
		 {
			 //Debited
			
			 setNotification(context);	
			 changedBalance=previousBalance-smsBalance;
			 t2=String.valueOf(changedBalance);
			// balFormat=getBalanceFormat(t2);
			 try
			 {
				 db1.execSQL("INSERT INTO '"+realNumber+"' (ACCOUNTNUMBER,WITHDRAWL,DEPOSITED,CURRENTBALANCE,DATE,TIME) VALUES('"+realNumber+"','"+t2+"','"+0+"','"+smsAmount+"','"+Cdate+"','"+Ctime+"');");
				 
				 db1.execSQL("UPDATE accountdata SET CURRENTBALANCE ='"+smsAmount+"' WHERE ACCOUNTNUMBER ='"+realNumber+"'");
				 
			 }catch(Exception e)
			 {
				 
			 }
		 }
		 
		 
		 
	 }
	 
	 void setNotification(Context context)
	 {
		  NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	        final Notification notification = new NotificationCompat.Builder(context)
	                .setSmallIcon(R.drawable.ic_launcher)
	                .setContentTitle("Account Balance Updated"/*your notification title*/)
	                .setContentText("Click to See"/*notifcation message*/)
	                .build();
	        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
	        notificationManager.notify(10000/*some int*/, notification);
	 }
	 
	/* String getBalanceFormat(String ss)
	 {
		 String t="";
		 int i=0;
		 char p=' ';
		 
		 	for(i=0;i<ss.length();i++)
		 	{
		 		p=ss.charAt(i);
		 		if(p=='.')
		 		{
		 			t=t+ss.charAt(i+1)+ss.charAt(i+2);
		 			break;
		 		}
		 		else
		 		{
		 			t=t+p;
		 		}
		 	}
		 
		 
		 return t;
	 }*/
	
}


