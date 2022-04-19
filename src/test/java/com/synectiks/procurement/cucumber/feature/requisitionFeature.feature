Feature: Requisition test Feature

Scenario: add requisition Test

	Given user is going on procurment login page
	Then click on submit
	Then click on new requisition
	And user fill all input fields
	Then click on add new item
	And fill all item input values and click on save
	Then click on send


Scenario: search requisition Test

	Given user is going on procurment login page1
	Then click on submit1
	Then click on manage requisition
	And user select for search
	Then click on search


Scenario: update requisition Test

	Given user is going on procurment login page2
	Then click on submit2
	Then click on manage requisition2
	And user select for search2
	Then click on search2
	Then click on edit
	And user fill all input fields2
	Then click on add new item2
	And fill all item input values and click on save2
	Then click on send2

Scenario: approve requisition Test

	Given user is going on procurment login page3
	Then click on submit3
	Then click on requisition tracker
	And click on approve
	Then click on yes

Scenario: delete requisition Test

	Given user is going on procurment login page4
	Then click on submit4
	Then click on manage requisition4
	And user select for search4
	Then click on search4
	And click on delete
	Then click on yes4
