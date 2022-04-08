package com.synectiks.procurement.cucumber.stepDefinition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class Stepdefinition {

	WebDriver driver1;

	@Given("^user is going on procurment login page$")
	public void user_is_going_on_gmail() {
		System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\cucumberDriver\\chromedriver1.exe");
		driver1 = new ChromeDriver();
		driver1.manage().window().maximize();
		driver1.get("http://localhost:3000/prelogin/login");
	}

	@Then("^click on submit$")
	public void click_on_create_account() throws Throwable {
		driver1.findElement(By.className("MuiButton-fullWidth")).click();
	}

	@Then("^click on new requisition$")
	public void click_on_for_myself() throws Throwable {
		driver1.findElement(By.xpath("//a[@href='/postlogin/newrequisition']")).click();
	}

	@And("^user fill all input fields$")
	public void user_input_firstname_and_lastname_and_username() {
		driver1.findElement(By.name("roleName")).sendKeys("Jitin");

		Select department = new Select(driver1.findElement(By.name("departmentId")));
		department.selectByValue("1");

		Select currency = new Select(driver1.findElement(By.name("currencyId")));
		currency.selectByValue("1");

		driver1.findElement(By.name("notes")).sendKeys("this is demo notes");
		driver1.findElement(By.name("status")).click();
	}

	@Then("^click on add new item$")
	public void user_input_password_and_confirm_password() {

		driver1.findElement(By.className("add_new_item")).click();

	}

	@And("^fill all item input values and click on save$")
	public void click_on_next() {
		driver1.findElement(By.name("itemDescription")).sendKeys("demo description");
		driver1.findElement(By.name("ratePerItem")).sendKeys("124");
		driver1.findElement(By.name("itemQuantity")).sendKeys("2");
		driver1.findElement(By.className("inside_new_item")).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Then("^click on send$")
	public void wait_for_three_seconds() {
		driver1.findElement(By.className("send_requi")).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

}
