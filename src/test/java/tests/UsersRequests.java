package tests;

import org.apache.tools.ant.types.resources.comparators.Size;
import org.json.simple.JSONObject;
import org.junit.internal.runners.statements.Fail;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class UsersRequests {
	
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest test ;

	@BeforeSuite
	public void setup() {
		// start reporters
		htmlReporter = new ExtentHtmlReporter("extent.html");
		// create ExtentReports and attach reporter(s)
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
	}

	
	//Retrieve all users

	@Test(priority=1)
	public void getAllUsers(){
		
		 test = extent.createTest("Get All Users Request", "Http request to get all users");

		RestAssured.baseURI ="https://jsonplaceholder.typicode.com";
		RequestSpecification request = RestAssured.given();

		Response response = request.get("/users"); 
		int statusCode = response.getStatusCode();

		// Assert that correct status code is returned.
		Assert.assertEquals(statusCode, 200);
        test.pass("Status Code is:"+" "+statusCode);

		// Header named Content-Type
		String contentType = response.header("Content-Type");
		Assert.assertEquals(contentType, "application/json; charset=utf-8");
		
		//JsonPath js =new JsonPath(bodyAsString);
		int size = response.jsonPath().getList("id").size();
		Assert.assertEquals(size, 10);
        test.pass("Total number of users is "+" "+size);


	}

	//Retrieve specific post

	@Test(priority=2)
	public void getSpecificuser(){
		
		 test = extent.createTest("Get Specific User Request", "Http request to get Specific User");

		int userId=35;
		
		RestAssured.baseURI ="https://jsonplaceholder.typicode.com";
		RequestSpecification request = RestAssured.given();


		Response response = request.get("/users/"+userId); 
		int statusCode = response.getStatusCode();

		// Assert that correct status code is returned.
		Assert.assertEquals(statusCode, 404);
        test.pass("Status Code is:"+" "+404+" "+ " as user not found");

		// Header named Content-Type
		String contentType = response.header("Content-Type");
		Assert.assertEquals(contentType, "application/json; charset=utf-8");

	}

	@AfterSuite
	public void tearDown(){
		
        extent.flush();
	}


}
