package com.core.ddf.NoBroker;



import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.core.ddf.NoBroker.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import com.relevantcodes.extentreports.LogStatus;


  
public class BaseTest 

{
	

	public WebDriver driver;
	public Properties prop;
	public Properties envProp;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	

	public void init(){
		//init the prop file
		if(prop==null){
			prop=new Properties();
			
			try {
				FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//projectconfig.properties");
				prop.load(fs);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}
	
	public void openBrowser(String bType){
		test.log(LogStatus.INFO, "Opening browser "+bType );
		
			if(bType.equals("Mozilla"))
				driver=new FirefoxDriver();
			else if(bType.equals("Chrome")){
				System.out.println(prop.getProperty("chromedriver_exe"));
				System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver_exe"));
				driver=new ChromeDriver();
			}
			else if (bType.equals("IE")){
				System.setProperty("webdriver.ie.driver", prop.getProperty("iedriver_exe"));
				driver= new InternetExplorerDriver();
			}
		
		
		
		
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		test.log(LogStatus.INFO, "Browser opened successfully "+ bType);

		
	}

	public void navigate(String urlKey){
		test.log(LogStatus.INFO, "Navigating to "+prop.getProperty(urlKey) );
		System.out.println(prop.getProperty(urlKey));
		driver.get(prop.getProperty(urlKey));
	}
	
	public void click(String locatorKey){
		test.log(LogStatus.INFO, "Clicking on "+locatorKey);
		getElement(locatorKey).click();
		test.log(LogStatus.INFO, "Clicked successfully on "+locatorKey);

	}
	
	public void type(String locatorKey,String data){
		test.log(LogStatus.INFO, "Tying in "+locatorKey+". Data - "+data);
		getElement(locatorKey).sendKeys(data);
		test.log(LogStatus.INFO, "Typed successfully in "+locatorKey);

	}
	// finding element and returning it
	public WebElement getElement(String locatorKey){
		WebElement e=null;
		try{
		if(locatorKey.endsWith("_id"))
			e = driver.findElement(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			e = driver.findElement(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_class"))
			e = driver.findElement(By.className(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}
		
		}catch(Exception ex){
			// fail the test and report the error
			reportFailure(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed the test - "+ex.getMessage());
		}
		return e;
	}
	
	
	public void hoverandSelectDashboard(String main, String sub)
	
	{
		Actions action = new Actions(driver);
		WebElement mainMenu = driver.findElement(By.xpath(prop.getProperty(main)));
		action.moveToElement(mainMenu).moveToElement(driver.findElement(By.xpath(prop.getProperty(sub)))).click().build().perform();
	}
	/***********************Validations***************************/
	
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> elementList=null;
		if(locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}
		
		if(elementList.size()==0)
			return false;	
		else
			return true;
	}

	/*****************************Reporting********************************/
	
	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
	}
	
	public void reportFailure(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);
	}
	
	public void takeScreenShot(){
		// fileName of the screenshot
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catcsh block
			e.printStackTrace();
		}
		//put screenshot file in reports
		test.log(LogStatus.INFO,"Screenshot-> "+ test.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		
	}

	public void waitForPageToLoad() {
		wait(1);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		
		while(!state.equals("complete")){
			wait(2);
			state = (String)js.executeScript("return document.readyState");
		}
	}
	
	public void wait(int timeToWaitInSec){
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void ExplicitwaitTillLocator(String LocatorKey){
		
             WebDriverWait wait=new WebDriverWait(driver,30);
             wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(LocatorKey))));

	}
	
	/************************App functions*****************************/

	public void selectCity(String city)
	{
		List<WebElement> cityNames=driver.findElements(By.xpath(prop.getProperty("citylist_xpath")));
	
		click("citybutton_xpath");
		int i;
	
		for(i=0;i<cityNames.size();i++){
	
			if(cityNames.get(i).getAttribute("data-key").trim().equalsIgnoreCase(city))
			{
				test.log(LogStatus.INFO, "city found in row num "+(i+1));
				int x=i+1;
				String xpath=prop.getProperty("firstpartcity")+x+prop.getProperty("secondpartcity");
			
				driver.findElement(By.xpath(xpath)).click();
				
				
				test.log(LogStatus.INFO, "selected the city "+city);
				break;
			}else 
				{
				if (i==cityNames.size()-1)
				reportFailure("City not found "+city );
				}
			}
		
				
				
	}
	
	public void searchResult(String city, String bhk, String Keywords )
	{
	    
		test.log(LogStatus.INFO, "Opened the browser");
		
		
		selectCity(city);
		selectBHK(bhk);
		if(!isElementPresent("searchbox_xpath"))
			reportFailure("Searchbox of NoBroker is not present");//critical
		
		type("searchbox_xpath",Keywords);
		wait(2);
		click("firstsearchresult_xpath");
		wait(2);
		click("searchbutton_xpath");
		waitForPageToLoad();
		if(isElementPresent("savesearch_xpath")){
			test.log(LogStatus.INFO, "Search Success");
		
		}
		else{
			test.log(LogStatus.INFO, "Search Failed");
			
		}
		wait(3);
		String actualResult=getElement("savesearch_xpath").getText();
		String expectedResult="Save Search";
		
		
		if (actualResult.equalsIgnoreCase(expectedResult))
		{
			reportPass("Search Test Passed");
		}else reportFailure("Search Test Failed.");
		
	}
	
	public void selectBHK(String BHK)
	{
		ArrayList<String> aList= new ArrayList(Arrays.asList(BHK.split(",")));
		ArrayList<String> bList= new ArrayList();
		bList.add("rk1");
		bList.add("bhk1");
		bList.add("bhk2");
		bList.add("bhk3");
		bList.add("bhk4");
		bList.add("bhk4plus");
		for (int i=1;i<=aList.size();i++)
		{
			if (!bList.contains(aList.get(i-1)))
			{
				reportFailure("BHK type not found");
				Assert.fail("BHK type not found");
			}
		}
		for(int i=1;i<=aList.size();i++)
		{
			click("bhkselector_xpath");
			wait(1);
			String xpath=prop.getProperty("firstpartbhklist")+i+prop.getProperty("secondpartbhklist");
		    driver.findElement(By.xpath(xpath)).click();
			
		}
		
	}
	
	public void ContactOwner()
	{
		List<WebElement> ownerDetailsList=driver.findElements(By.xpath(prop.getProperty("ownerdetails_xpath")));
		//getElement("ownerdetails_xpath");
		System.out.println(ownerDetailsList.size());
		for (int i=0;i<ownerDetailsList.size();i++)
		{
			System.out.println(getElement(ownerDetailsList.get(i).toString()));
		}
		
	}
	
	public void VerifyNumber(String number)
	{
		
		try{	
		
		int a=0;
		//System.out.println("number is ---->>  "+number);
		//String Number=(String)number;
		Long  no = Long.valueOf(number);
		while(no>0)
		{
		no=no/10;
		a++;
		}
		if (a==10)
		{
			test.log(LogStatus.INFO, "number is a 10 digit number");
		}
		else
		{
			reportFailure("Mobile numberis not having 10 digits");
			Assert.fail("Mobile number is not having 10 digits");
		}
		}
		catch(Exception e)
		{
			
			System.out.println(e);
			reportFailure("Mobile numberis not of an integer");
			Assert.fail("Mobile numberis not of an integer");
		}
	}
	
	public void VerifyPassword(String password)
	{
		
		if (driver.findElement(By.xpath("//*[@id='checkMobileNumber']/div[2]/fieldset[3]/div/div[1]")).getText().equalsIgnoreCase(prop.getProperty("wrongpasswordstring")))
		{
			reportFailure("Password "+password+" is wrong");
			Assert.fail("Password "+password+" is wrong");
		}
	}
    
	 public void doLogin(String number, String password)
	 {
		    wait(2);
		    click("signinbutton_xpath");
			ExplicitwaitTillLocator("loginpassword_xpath");
			type("loginusername_xpath",number);
			type("loginpassword_xpath",password);
			click("loginbutton_xpath");
	 }
	 
	 
	
	
}
