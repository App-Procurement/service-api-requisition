# xformation-procurement-api
- [xformation-procurement-api](#xformation-procurement-api)
  - [About](#about)
  - [Database and Data Model](#database-and-data-model)
  - [Quick start](#quick-start)
  - [Code Structure](#code-structure)
  - [Build the Source](#build-the-source)
  - [Contributing to Procurement](#contributing-to-procurement)
  - [MockDev](#mockdev)
  - [Tests](#tests)
  - [Run Development Environment](#run-development-environment)
  - [Create Sample Data](#create-sample-data)
  - [Run Production Environment](#run-production-environment)
  - [Postman Collection](#postman-collection)
  - [Run Postman Collection](#run-postman-collection)
  - [Remote debugging in Eclipse](#remote-debugging-in-eclipse)
- [BDD testing of add requisition by cucumber](#bdd-testing-of-add-requisition-by-cucumber)
        - [Feature file](#feature-file)
        - [Step definition file](#step-definition-file)
        - [Runner file](#runner-file)
    - [Feature file](#feature-file-1)
    - [Step definition file](#step-definition-file-1)
    - [Runner file](#runner-file-1)
  
## About 
  This codebase is SpringBoot backend API's for Procurement solution. Procurement application has complete life cycle flow to create a requisition request, requisition approval, getting quotations from registered vendors against an approved requisition, purchase orders and invoicing of assets etc. It has user authentication and authorization and maintains the role based request flow with different roles like PSDS Admin, Budget holder, director general, requisition etc.
  Procurement APIs provide access to user's email inbox, that gives the user an advantage not to switch to the different email applications for their mails.  

## Database and Data Model
  **postgres** is used as backend database  
  The data model and ORM code is generated using jhipster. **jhipster-jdl.jdl** file contains all the entity definitions  

  > [**ER Diagram**](ERD.md)  


## Quick start
  jhipster command is used for codebase setup  

  ```
  jhipster import-jdl src\model\jhipster-jdl.jdl
  ```
## Code Structure  
  > [Snapshot](CODE_STRUCTURE.md)  

  > Java package hierarchy  

  | Type       | Package                                    |
  | ---------- | :----------------------------------------- |
  | Main       | com.synectiks.procurement                  |
  | Config     | com.synectiks.procurement.config           |
  | Entities   | com.synectiks.procurement.domain           |
  | Controller | com.synectiks.procurement.controllers      |
  | Services   | com.synectiks.procurement.business.service |
  | Repository | com.synectiks.procurement.repository       |
  | Security   | com.synectiks.procurement.security         |
  
  > Resources  

  | Files/Directories           | Details                                                                                                                                                                                                                                                                                                        |
  | --------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
  | config/liquibase/changelog  | All the liquibase specific database entities (e.g. 20210722052455_added_entity_Document.xml) and their constraint definitions (e.g. 20210722052455_added_entity_constraints_Document.xml) found in changelog directory. <br> Liquibase executes the DDL and DMLs based on the definitions given in these files |
  | config/liquibase/fake-data  | It contains CSV files which contains dummy data for all the entities. if faker is turned on in Spring cofig file, faker.js inserts that data into the tables after server starts up. <br> **To make it work, make an entry of faker in liquibase context**                                                     |
  | config/liquibase/master.xml | master.xml is the main file for liquibase. All the changelog files must be registered in master.xml                                                                                                                                                                                                            |
  

## Build the Source  
> NOTE: Procurement service is built and tested with JDK 8  

  Run mvn command in the root directory to clean build the application  
  
  ```
  mvn -e clean install
  ```
  The above mvn command will execute all the test cases before making the build  
  
  To skip the test execution during the build, use -DskipTests=true with the command  

  ```
  mvn -e clean install -DskipTests=true
  ```

  The resulting Procurement distribution can be found in the folder `/target`, i.e.
  
  ```
  /target/procurement-0.0.1-SNAPSHOT.jar
  ```

  Schema for dev profile is **procurementdev** and it is defined in **application-dev.yml** file  
  Schema for prod profile is **procurement** and it is defined in **application-prod.yml** file  

## Contributing to Procurement  

## MockDev  

## Tests
  
  Jhipster by-default provides JUnit test cases for all the APIs  
  Jhipster provides the **Cucumber** (automation testing) and **Gatling** (load testing) test setup at the time of codebase setup  

## Run Development Environment  
  Start Procurement service with dev profile  

  ```
  java -jar -Dspring.profiles.active=dev target/procurement-0.0.1-SNAPSHOT.jar
  ```  

## Create Sample Data  

  When service started with **dev** profile, faker.js inserts fake data in all the tables of **procurementdev** schema. Developer can focus on testing the APIs rather than putting efforts to generate test data before testing the core APIs.  

## Run Production Environment  
  Start Procurement service with prod profile 

```
  java -jar -Dspring.profiles.active=prod target/procurement-0.0.1-SNAPSHOT.jar  
```  

## Postman Collection
 [collection](POSTMAN_COLLECTION.md)  

## Run Postman Collection  
  
  > Import postman collection given in [Postman Collection](POSTMAN_COLLECTION.md) file  

  > Open the context menu of Procurement collection and click on Run collection  

## Remote debugging in Eclipse  
  
  Make following entry in pom.xml

```
<plugins>
    <plugin>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-maven-plugin</artifactId>
          <version>${spring-boot.version}</version>
          <executions>
              <execution>
                  <goals>
                      <goal>repackage</goal>
                  </goals>
              </execution>
          </executions>
          <configuration>
              <mainClass>${start-class}</mainClass>
              <fork>true</fork>
              <jvmArguments>-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7052</jvmArguments>
          </configuration>
    </plugin>
</plugins>
```

  Complete the eclipse configuration. Follow the steps given below:  
  > Go to Run -> Debug Configurations  
  > Create a new Remote Java Application configuration  
  ![Remote Debug](./remote_debug_snapshot.png)  
  > Click on apply  

  

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



