# BDD testing of add requisition by cucumber

There are mainly three files for this testing
##### Feature file
##### Step definition file
##### Runner file


### Feature file
This file contains a Scenario that contain all the steps that is required for adding a requisition  

```
Scenario: add requisition Test Scenario
	
	Given user is going on procurment login page
	Then click on submit
	Then click on new requisition
	And user fill all input fields
	Then click on add new item
	And fill all item input values and click on save
	Then click on send
```

###  Step definition file
corresponding to every line of scenario in feature file there is a function which handle and execute the event

e.g.


* 1
  
```
    Given user is going on procurment login page
```
It calls web driver and run browser and open link

```
	@Given("^user is going on procurment login page$")
	public void user_is_going_on_gmail() {
		System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\cucumberDriver\\chromedriver1.exe");
		driver1 = new ChromeDriver();
		driver1.manage().window().maximize();
		driver1.get("http://localhost:3000/prelogin/login");
	}
```

* 2
  
```
   Then click on submit
```
It login into the page

```
	@Then("^click on submit$")
	public void click_on_create_account() throws Throwable {
		driver1.findElement(By.className("MuiButton-fullWidth")).click();
	}
```
* 3
  
```
  	Then click on new requisition
```
Opens requisition tab 

```
    @Then("^click on new requisition$")
	public void click_on_for_myself() throws Throwable {
		 driver1.findElement(By.xpath("//a[@href='/postlogin/newrequisition']")).click();
	}
```
* 4
  
```
  	And user fill all input fields
```
It fills and select all fields  if required

```
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

```
* 5
  
```
  Then click on add new item
```
Opens new item popup

```
  	@Then("^click on add new item$")
	  public void user_input_password_and_confirm_password() {
		driver1.findElement(By.className("add_new_item")).click();
	}
```
* 6
  
```
  And fill all item input values and click on save
```
Fills add item's inputs 

```
  @And("^fill all item input values and click on save$")
	public void click_on_next() {
		driver1.findElement(By.name("itemDescription")).sendKeys("demo description");
		driver1.findElement(By.name("ratePerItem")).sendKeys("124");
		driver1.findElement(By.name("itemQuantity")).sendKeys("2");
		driver1.findElement(By.className("inside_new_item")).click();
    driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
```
* 7
  
```
 Then click on send
```
Clicks on final button and requisition will be added

```
  @Then("^click on send$")
	public void wait_for_three_seconds() {
		driver1.findElement(By.className("send_requi")).click();
    driver1.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
```
### Runner file
It has configuration of feature file and Step Definition file and it runs the test

```
    @RunWith(Cucumber.class)
    @CucumberOptions(
        features = "src\\test\\java\\com\\synectiks\\procurement\\cucumber\\feature\\requisitionFeature.feature",  
        glue = {"com.synectiks.procurement.cucumber.stepDefinition"}
        )

```



