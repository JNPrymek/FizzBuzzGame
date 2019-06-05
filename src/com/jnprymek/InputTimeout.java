package com.jnprymek;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.KeyEvent;


public class InputTimeout extends Thread 
{
	long timeOut;
	UserInputUnit UInput;
	private boolean canceled;
	
	public InputTimeout(UserInputUnit UInput)
	{
		timeOut = 10 * 1000; //10 seconds, for readability
		this.UInput = UInput;
		canceled = true;
	}
	public InputTimeout(UserInputUnit UInput, long timeOut)
	{
		this.timeOut = timeOut;
		this.UInput = UInput;
	}
	
	public void setTimeOut(long timeOut)
	{
		this.timeOut = timeOut;
	}
	public long GetTimeOut()
	{
		return timeOut;
	}
	
	public void SetUserInputUnit(UserInputUnit UInput)
	{
		this.UInput = UInput;
	}
	public UserInputUnit GetUserInputUnit()
	{
		return UInput;
	}
	
	public void CancelTimeout()
	{
		canceled = true;
	}
	
	@Override
	public void run()
	{
		try 
		{
			canceled = false;
			Thread.sleep(timeOut);
			if (!canceled)
			{
				Robot robit = new Robot();
				robit.keyPress(KeyEvent.VK_ENTER);
				robit.keyRelease(KeyEvent.VK_ENTER);
				UInput.SetInputExpired();
				//System.out.println("TimeOut Occurred.");
			}
		}
		catch (InterruptedException IntE)
		{
			System.out.println("Thread Intterupted Exception from Timer");
			IntE.printStackTrace();
		}
		catch (AWTException AwtE)
		{
			System.out.println("AWT Exception from Robot");
			AwtE.printStackTrace();
		}
	}
}
