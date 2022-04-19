package com.synectiks.procurement.cucumber.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src\\test\\java\\com\\synectiks\\procurement\\cucumber\\feature\\requisitionFeature.feature",  
		glue = {"com.synectiks.procurement.cucumber.stepDefinition"}
		)

public class Runner {}
