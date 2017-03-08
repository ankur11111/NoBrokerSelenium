package com.core.ddf.NoBroker.testcases;


	import java.io.IOException;
	import java.util.Hashtable;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
    import org.testng.annotations.AfterMethod;
	import org.testng.annotations.BeforeMethod;
	import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
	import org.testng.asserts.SoftAssert;

import com.core.ddf.NoBroker.BaseTest;
import com.core.ddf.NoBroker.DataUtil;
import com.core.ddf.NoBroker.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

	public class ShortlistTest  extends BaseTest {
		String testCaseName="ShortlistTest";
		SoftAssert softAssert;
		Xls_Reader xls;
		
		@Test(dataProvider="getData")
		public void addremoveShortlist(Hashtable<String,String> data) throws IOException
		{
			test = rep.startTest("addremoveShortlist");
			test.log(LogStatus.INFO, data.toString());
			if(!DataUtil.isRunnable(testCaseName, xls) ||  data.get("Runmode").equalsIgnoreCase("N")){
				test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
				throw new SkipException("Skipping the test as runmode is N");
			}
			
			
			openBrowser(data.get("Browser"));
			navigate("appurl");
			searchResult(data.get("city"),data.get("bhk"),data.get("location"));
		    takeScreenShot();
		    wait(2);
		    click("shortlistbutton_xpath");
		    wait(2);
		    type("contactnumber_xpath",data.get("number"));
		    wait(2);
			click("nextbutton1_xpath");
			wait(4);
			click("radiobutton_xpath");
			wait(2);
			type("password_xpath",data.get("password"));
			wait(2);
			click("nextbutton2_xpath");
			wait(2);
			doLogin(data.get("number"),data.get("password"));
			hoverandSelectDashboard("mainelement","subelement");
			click("shortlistTab_xpath");
			wait(2);
			String Shortlistcountbefore = getElement("shortlistedcount_xpath").getText();
			click("shortlisttabnextpage_xpath");
			wait(2);
			click("removeshortlist_xpath");
			wait(2);
			String Shortlistcountafter = getElement("shortlistedcount_xpath").getText();
			if (Shortlistcountbefore.equalsIgnoreCase(Shortlistcountafter))
			{
				reportFailure("Shortlist Test Failed");
				Assert.fail("Shortlist Test Failed");
			}
			else
			{
				reportPass("Shortlist Test Passed");
				takeScreenShot();
			}
		
		}
		
		
		
		
		@BeforeMethod
		public void init(){
			softAssert = new SoftAssert();
	//		super.init();
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


