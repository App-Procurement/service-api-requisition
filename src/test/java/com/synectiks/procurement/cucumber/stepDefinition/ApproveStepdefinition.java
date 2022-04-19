package com.synectiks.procurement.cucumber.stepDefinition;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.internal.MouseAction.Button;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class ApproveStepdefinition {

	WebDriver driver1;

	@Given("^user is going on procurment login page3$")
	public void user_is_going_on_procurment_login_page3() {
		System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\cucumberDriver\\chromedriver1.exe");
		driver1 = new ChromeDriver();
		driver1.manage().window().maximize();
		driver1.get("http://localhost:3000/prelogin/login");
	}

	@Then("^click on submit3$")
	public void click_on_submit3() throws Throwable {
		driver1.findElement(By.className("MuiButton-fullWidth")).click();
	}

	@Then("^click on requisition tracker$")
	public void click_on_requisition_tracker() {
		driver1.findElement(By.xpath("//a[@href='/postlogin/requisitiontracker']")).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@And("^click on approve$")
	public void click_on_approve() {
		List<WebElement> li = driver1.findElements(By.tagName("tbody"));
		List<WebElement> li2 = li.get(0).findElements(By.className("secondary-btn"));
		li2.get(0).click();
		driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Then("^click on yes$")
	public void clic_on_yes() {
		driver1.findElement(By.className("MuiDialogActions-spacing")).findElement(By.className("primary-btn")).click();

	}

}
