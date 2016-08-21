package bookstore;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;

public class BookstoreTest {
  
//	Test	
//	private Book mBook;
//	private Response mRespone;
	private String bookId = "57b95ec7b0bbb2d4cc4ff33e";

	
	@Test ( priority = 1 )
	public void getTest() {
		getBookData(bookId);
	}
	
	@Test ( priority = 2)
	public void putTest() {
		putBookData(bookId);
		
		ValidatableResponse respone = getBookData(bookId);
		respone.
		assertThat().
			body("title", equalTo("foo")).
		and().
			body("desc", equalTo("bar"));
	}
	
	@BeforeTest
	public void beforeTest() {
		System.out.println("start");
		RestAssured.baseURI = "https://bookstore2-abtd.c9users.io/api";
	}

	@AfterTest
	public void afterTest() {
		
	}
	
	////////////////////////////////////
	///////////help functions///////////
	
	public ValidatableResponse getBookData(String bookId) {
		return get("/books/" + bookId).
		then().
			assertThat().statusCode(200).
		and().
			assertThat().body("title", not(equalTo(null))).
		and().
			assertThat().body("desc", not(equalTo(null)));
	}
	
	public void putBookData(String bookId) {
		given().
		request().
			header("Content-Type", "application/json").
		and().
			body("{\"title\": \"foo\",\"desc\": \"bar\"}").
		when().
			put("/books/" + bookId);
	}

}
