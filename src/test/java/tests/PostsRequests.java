package tests;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class PostsRequests {

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


	// Create post

	@Test(priority=1)
	public void createPost() throws IOException{
		
		 test = extent.createTest("Create Post Request", "Http request to create post");

		// Specify the base URL to the RESTful web service
		RestAssured.baseURI ="https://jsonplaceholder.typicode.com";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams=new JSONObject();
		requestParams.put("title", "foo");
		requestParams.put("body", "bar");
		requestParams.put("userId", 1);

		// Add a header stating the Request body is a JSON
		request.header("Content-Type", "application/json");
		request.body(requestParams.toJSONString());

		// Post the request and check the response
		Response response = request.post("/posts");

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 201);
        test.pass("Status Code is:"+" "+statusCode);

		ResponseBody body = response.getBody();
		String bodyAsString = body.asString();
		JsonPath js =new JsonPath(bodyAsString);

		String Actualtitle =js.getString("title");
		
		Assert.assertEquals(Actualtitle, "foo");
        test.pass("Title is:"+" "+Actualtitle);

	}



	//Retrieve all posts

	@Test(priority=2)
	public void getAllPosts(){

		 test = extent.createTest("Get All Posts Request", "Http request to get all posts");

		RestAssured.baseURI ="https://jsonplaceholder.typicode.com";
		RequestSpecification request = RestAssured.given();

		Response response = request.get("/posts"); 
		int statusCode = response.getStatusCode();

		// Assert that correct status code is returned.
		Assert.assertEquals(statusCode, 200);
        test.pass("Status Code is:"+" "+statusCode);

		// Header named Content-Type
		String contentType = response.header("Content-Type");
		Assert.assertEquals(contentType, "application/json; charset=utf-8");

		int size = response.jsonPath().getList("id").size();
        test.pass("Total Number of posts is"+" "+size);

	}

	//Retrieve specific post

	@Test(priority=3)
	public void getSpecificPost(){
		
		 test = extent.createTest("Get Specific Post Request", "Http request to get specific post");

		int postId=35;

		RestAssured.baseURI ="https://jsonplaceholder.typicode.com";
		RequestSpecification request = RestAssured.given();


		Response response = request.get("/posts/"+postId); 
		int statusCode = response.getStatusCode();

		// Assert that correct status code is returned.
		Assert.assertEquals(statusCode, 200);
        test.pass("Status Code is:"+" "+statusCode);

		// Header named Content-Type
		String contentType = response.header("Content-Type");
		Assert.assertEquals(contentType, "application/json; charset=utf-8");

		ResponseBody body = response.getBody();
		String bodyAsString = body.asString();
		JsonPath js =new JsonPath(bodyAsString);

		int actualID=js.getInt("id");
		Assert.assertEquals(actualID, postId);
        test.pass("ID is"+" "+actualID);

	}


	@AfterSuite
	public void tearDown(){
		
        extent.flush();
	}


}
