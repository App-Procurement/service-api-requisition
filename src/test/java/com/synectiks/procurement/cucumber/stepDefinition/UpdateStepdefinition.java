package com.synectiks.procurement.cucumber.stepDefinition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class UpdateStepdefinition {

	WebDriver driver1;
	
	@Given("^user is going on procurment login page2$")
	public void user_is_going_on_procurment_login_page2() {
		System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\cucumberDriver\\chromedriver1.exe");
		driver1 = new ChromeDriver();
		driver1.manage().window().maximize();
		driver1.get("http://localhost:3000/prelogin/login");
	}

	@Then("^click on submit2$")
	public void click_on_submit2()  {
		driver1.findElement(By.className("MuiButton-fullWidth")).click();
		
	}

	@Then("^click on manage requisition2$")
	public void click_on_manage_requisition2() {
		driver1.findElement(By.xpath("//a[@href='/postlogin/managerequisition']")).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@And("^user select for search2$")
	public void select_for_search2() {

		Select status = new Select(driver1.findElement(By.name("status")));
		status.selectByValue("DRAFT");
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	}

	@Then("^click on search2$")
	public void click_on_search2() {

		driver1.findElement(By.className("search_button_manage")).click();
	}
	

	@Then("^click on edit$")
	public void Then_click_on_edit() {
		
		driver1.findElement(By.xpath("//a[@href='/postlogin/managerequisition/3002']")).click();
		
	}

	@And("^user fill all input fields2$")
	public void user_fill_all_input_fields2() {
		driver1.findElement(By.name("roleName")).sendKeys("updatedName");

		Select department = new Select(driver1.findElement(By.name("departmentId")));
		department.selectByValue("2");

		Select currency = new Select(driver1.findElement(By.name("currencyId")));
		currency.selectByValue("2");

		driver1.findElement(By.name("notes")).sendKeys("this is updated demo notes");
		driver1.findElement(By.name("status")).click();
	}

	@Then("^click on add new item2$")
	public void click_on_add_new_item2() {

		driver1.findElement(By.className("add_new_item")).click();

	}

	@And("^fill all item input values and click on save2$")
	public void fill_all_item_input_values_and_click_on_save2() {
		driver1.findElement(By.name("itemDescription")).sendKeys("updated description");
		driver1.findElement(By.name("ratePerItem")).sendKeys("888");
		driver1.findElement(By.name("itemQuantity")).sendKeys("5");
		driver1.findElement(By.className("inside_new_item")).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Then("^click on send2$")
	public void click_on_send2() {
		driver1.findElement(By.className("send_requi")).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

}
