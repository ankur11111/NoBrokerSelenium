package com.core.ddf.NoBroker.testcases;

import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.core.ddf.NoBroker.BaseTest;
import com.core.ddf.NoBroker.DataUtil;
import com.core.ddf.NoBroker.Xls_Reader;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class selectplanTest extends BaseTest {
	String testCaseName="selectplanTest";
	
	SoftAssert softAssert;
	Xls_Reader xls;
	
	@Test(dataProvider="getData")
	public void search(Hashtable<String,String> data) throws IOException
	{
		
		test = rep.startTest("Search");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable(testCaseName, xls) ||  data.get("Runmode").equalsIgnoreCase("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		openBrowser(data.get("Browser"));
		searchResult(data.get("city"),data.get("bhk"),data.get("location"));
	   	takeScreenShot();
		wait(2);
		getElement("ownerdetails_xpath").click();
		type("contactnumber_xpath",data.get("number"));
		VerifyNumber(data.get("number"));
		click("nextbutton1_xpath");
		wait(2);
		click("radiobutton_xpath");
		//System.out.println("-->>"+driver.findElement(By.xpath("//div[@class='errorMsgText']")).getAttribute("innerText"));
		type("password_xpath",data.get("password"));
		//VerifyPassword(data.get("password"));
		click("nextbutton2_xpath");
		wait(2);
		VerifyPassword(data.get("password"));
		wait(2);
		//System.out.println("-->>"+driver.findElement(By.xpath("//*[@id='checkMobileNumber']/div[2]/fieldset[3]/div/div[1]")).getText());
		click("paynowbutton_xpath");
		waitForPageToLoad();
		if(isElementPresent("orderdetails_xpath")){
			test.log(LogStatus.INFO, "Payment Success");
		
		}
		else{
			test.log(LogStatus.INFO, "Payment Failed");
			
		}
		
		String actualResult=getElement("orderdetails_xpath").getText();
		String expectedResult="ORDER DETAILS";
		
		
		if (actualResult.equalsIgnoreCase(expectedResult))
		{
			reportPass("Payment Test Passed");
		}else reportFailure("Payment Test Failed.");
		
		takeScreenShot();
	
		
}
	
	
	@BeforeMethod
	public void init(){
		softAssert = new SoftAssert();
	//	super.init();
	}
	
	
	
	
	@AfterMethod
	public void repoquit(){
		if(rep!=null){
			rep.endTest(test);
			rep.flush();
		}
		if(driver!=null)
			driver.quit();
			}
	@DataProvider
	public Object[][] getData(){
		super.init();
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		Object[][] data= DataUtil.getTestData(xls, testCaseName);
		return data;
		
	}

}
