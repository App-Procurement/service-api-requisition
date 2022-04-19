package com.synectiks.procurement.cucumber.stepDefinition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class DeleteStepdefinition {

	WebDriver driver1;
	
	@Given("^user is going on procurment login page4$")
	public void user_is_going_on_procurment_login_page4() {
		System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\cucumberDriver\\chromedriver1.exe");
		driver1 = new ChromeDriver();
		driver1.manage().window().maximize();
		driver1.get("http://localhost:3000/prelogin/login");
	}

	@Then("^click on submit4$")
	public void click_on_submit4()  {
		driver1.findElement(By.className("MuiButton-fullWidth")).click();
		
	}

	@Then("^click on manage requisition4$")
	public void click_on_manage_requisition4() {
		driver1.findElement(By.xpath("//a[@href='/postlogin/managerequisition']")).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@And("^user select for search4$")
	public void select_for_search4() {

		Select status = new Select(driver1.findElement(By.name("status")));
		status.selectByValue("DRAFT");
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	}

	@Then("^click on search4$")
	public void click_on_search4() {

		driver1.findElement(By.className("search_button_manage")).click();
	}
	

	@Then("^click on delete$")
	public void  click_on_delete() {
		
		
		List<WebElement> li = driver1.findElements(By.tagName("tbody"));
		List<WebElement> li2 = li.get(0).findElement(By.className("popper-toggle")).findElements(By.tagName("button"));
		li2.get(2).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
	}

	@And("^click on yes4$")
	public void click_on_yes4() {
		driver1.findElement(By.className("MuiDialogActions-spacing")).findElement(By.className("primary-btn")).click();

	}



}
