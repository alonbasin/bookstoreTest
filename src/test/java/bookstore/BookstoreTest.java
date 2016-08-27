package bookstore;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;

public class BookstoreTest {

	ArrayList<Book> mBooksList = new ArrayList<Book>();

	
	@Test ( priority = 1 )
	public void getTest() {
		getBookData(mBooksList.get(0).id);
	}
	
	@Test ( priority = 2)
	public void putTest() {
		putBookData(mBooksList.get(0).id);
		
		ValidatableResponse respone = getBookData(mBooksList.get(0).id);
		respone.
		assertThat().
			body("title", equalTo("foo")).
		and().
			body("desc", equalTo("bar"));
	}
	
	@BeforeTest
	public void beforeTest() {
		System.out.println("Start...");
		//RestAssured.baseURI = "https://bookstore2-abtd.c9users.io/api";
		RestAssured.baseURI = "http://ec2-52-43-230-225.us-west-2.compute.amazonaws.com:3000/api";
		
		try {
			getBooksData(RestAssured.baseURI);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterTest
	public void afterTest() {
		resetBookData(mBooksList.get(0).id);
	}
	
	////////////////////////////////////
	///////////help functions///////////
	////////////////////////////////////
	
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
	
	public void resetBookData(String bookId) {
		given().
		request().
			header("Content-Type", "application/json").
		and().
			body("{\"title\": \"reset\",\"desc\": \"reset\"}").
		when().
			put("/books/" + bookId);
	}
	
	public void getBooksData(String baseUrl) throws IOException {
		JSONParser parser = new JSONParser();
		String booksUrlString = baseUrl + "/books/";
		
		try {
			URL booksUrl = new URL(booksUrlString);
			URLConnection uc = booksUrl.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			
			String inputLine;
            while ((inputLine = br.readLine()) != null) {               
                JSONArray booksJson = (JSONArray) parser.parse(inputLine);

                for (Object o : booksJson) {
                    JSONObject bookJson = (JSONObject) o;
                    if (bookJson.get("_id") != null && bookJson.get("title") != null && bookJson.get("desc") != null) {
    					Book book = new Book(bookJson.get("_id").toString(), bookJson.get("title").toString(), bookJson.get("desc").toString());
    					mBooksList.add(book);
					}
                }
            }
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
