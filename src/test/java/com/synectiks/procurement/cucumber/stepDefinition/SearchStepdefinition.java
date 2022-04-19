package com.synectiks.procurement.cucumber.stepDefinition;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class SearchStepdefinition {

	WebDriver driver1;

	@Given("^user is going on procurment login page1$")
	public void user_is_going_on_procurment_login_page1() {
		System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\cucumberDriver\\chromedriver1.exe");
		driver1 = new ChromeDriver();
		driver1.manage().window().maximize();
		driver1.get("http://localhost:3000/prelogin/login");
	}

	@Then("^click on submit1$")
	public void click_on_submit1() throws Throwable {
		driver1.findElement(By.className("MuiButton-fullWidth")).click();
		
	}

	@Then("^click on manage requisition$")
	public void click_on_manage_requisition() throws Throwable {
		driver1.findElement(By.xpath("//a[@href='/postlogin/managerequisition']")).click();
	}

	@And("^user select for search$")
	public void select_for_search() {

		Select status = new Select(driver1.findElement(By.name("status")));
		status.selectByValue("DRAFT");
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	}

	@Then("^click on search$")
	public void click_on_search() {

		driver1.findElement(By.className("search_button_manage")).click();
	}

}
