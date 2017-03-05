package com.core.ddf.NoBroker.testcases;

import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

public class temp {
public static void main(String args[])
{
try{	
		
		int a=0;
		int no = Integer.parseInt("9999999999");
		while(no>0)
		{
		no=no/10;
		a++;
		}
		if (a==10)
		{
		//	test.log(LogStatus.INFO, no+" is a 10 digit number");
			System.out.println("number is a 10 digit number");
		}
		else
		{
		//	reportFailure("Mobile number"+no+"is not of 10 digits");
			//Assert.fail("Mobile number"+no+"is not of 10 digits");
			System.out.println("Mobile number is not of 10 digits");
		}
		}
		catch(Exception e)
		{
		System.out.println("helloooo catch");
		}
}}

