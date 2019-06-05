package com.jnprymek;

public class UserInputUnit 
{
	private String inputText;
	private boolean timeExpired;
	
	//TODO Add internal time-keeping (system time, checking methods)
	
	public UserInputUnit()
	{
		inputText = "";
		timeExpired = false;
	}
	
	public void SetInputString(String text)
	{
		inputText = text;
	}
	
	public String GetInputString()
	{
		return inputText;
	}
	
	public void SetInputExpired()
	{
		SetInputExpired(true);
	}
	public void SetInputExpired(boolean expired)
	{
		timeExpired = expired;
	}
	
	public boolean isExpired()
	{
		return timeExpired;
	}
	
	public void ResetInput()
	{
		timeExpired = false;
		inputText = "";
	}
}
