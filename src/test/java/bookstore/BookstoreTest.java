package bookstore;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;

import org.testng.annotations.BeforeTest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*; 

import java.net.URL;

public class BookstoreTest {
  
	private Book mBook;
	private Response mRespone;

	
	@Test ( priority = 1 )
  public void getTest() {
		String bookId = "57b2c72280bc48bedbb59319";
		ValidatableResponse response = get("/books/" + bookId).
		then().
			assertThat().statusCode(200).
		and().
			assertThat().body("title", not(equalTo(null))).
		and().
			assertThat().body("description", not(equalTo(null)));
  }
	
	@Test ( priority = 2)
	public void putTest() {
		getBookData("57b2c72280bc48bedbb59319");
		String title = mRespone.getBody().jsonPath().getString("title");
		String description = mRespone.getBody().jsonPath().getString("description");
		mBook = new Book(title, description);
//		Assert.assertEquals(c, "1");
		putBookData("57b2c72280bc48bedbb59319");
		
		getBookData("57b2c72280bc48bedbb59319");
		String title1 = mRespone.getBody().jsonPath().getString("title");
		System.out.println(title1);
	}
	
	
  
	@BeforeTest
  public void beforeTest() {
	System.out.println("start");
	RestAssured.baseURI = "https://bookstore-abtd.c9users.io/api";
  }

  
	@AfterTest
  public void afterTest() {
		
  }
	
	
	
	public void getBookData(String bookId) {
	 	mRespone = given().
			contentType("application/json").
			when().
			get("/books/" + bookId).
			then().
			statusCode(200).
			extract().response();	
	}
	
	public void putBookData(String bookId) {
		given().
			param("title", "test").
			param("description", "test2").
		when().
			put("/books/" + bookId).
		then().
			body(containsString("OK"));
	}

}
