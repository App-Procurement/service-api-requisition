{
	"info": {
		"_postman_id": "af8ca21a-36c8-462a-8161-330fcc9f0531",
		"name": "Procurement",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "CommitteeController",
			"item": [
				{
					"name": "addCommittee",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"name\":\"CPS\",\r\n    \"type\":\"CPS\",\r\n    \"notes\":\"CPS committee\",\r\n    \"status\":\"ACTIVE\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addCommittee",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addCommittee"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateCommittee",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"id\"  :\"1001\",\r\n    \"name\":\"RBM\",\r\n    \"type\":\"RBM apps\",\r\n    \"notes\":\"Type updated\"\r\n    \r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateCommittee",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateCommittee"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchCommittee",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "formdata",
							"formdata": [
								{
									"key": "id",
									"value": "1001",
									"type": "text"
								}
							]
						},
						"url": {
							"raw": "http://localhost:7050/api/searchCommittee",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchCommittee"
							],
							"query": [
								{
									"key": "id",
									"value": "552",
									"disabled": true
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteCommittee",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/committee/1002",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"committee",
								"1002"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "CommitteeMember",
			"item": [
				{
					"name": "addCommitteeMembers",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "formdata",
							"formdata": [
								{
									"key": "obj",
									"value": "{\n     \n    \"committeeId\":\"1001\",\n    \"name\":\"BIG B\",\n    \"company\":\"Bib B compay\",\n    \"department\":\"ADMIN\",\n    \"designation\":\"CEO\",\n    \"status\":\"ACTIVE\",\n    \"email\":\"bigb@gmail.com\",\n    \"phoneNumber\":\"324324432\"\n    \n}",
									"type": "text"
								},
								{
									"key": "file",
									"type": "file",
									"src": "/C:/Users/admin/Pictures/agent-icon.png"
								}
							],
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addCommitteeMembers",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addCommitteeMembers"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchCommitteeMembers",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/searchCommitteeMembers?committeeId=1001",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchCommitteeMembers"
							],
							"query": [
								{
									"key": "committeeId",
									"value": "1001"
								}
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "ContactController",
			"item": [
				{
					"name": "addContact",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n\"firstName\":\"mohit\",\r\n\"middleName\":\"k\",\r\n\"lastName\":\"soni\",\r\n\"phoneNumber\":\"phoneNumber\",\r\n\"email\":\"mohit\",\r\n\"isActive\":\"isActive\",\r\n\"inviteStatus\":\"inviteStatus\",\r\n\"invitationLink\":\"invitationLink\",\r\n\"jobType\":\"jobType\",\r\n\"notes\":\"notes\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addContact",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addContact"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateContact",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n\"firstName\":\"satnam\",\r\n\"middleName\":\"kumar\",\r\n\"lastName\":\"Sharma\",\r\n\"phoneNumber\":\"phoneNumber\",\r\n\"email\":\"mohit\",\r\n\"isActive\":\"isActive\",\r\n\"inviteStatus\":\"inviteStatus\",\r\n\"invitationLink\":\"invitationLink\",\r\n\"jobType\":\"jobType\",\r\n\"notes\":\"notes\",\r\n\"id\"  :\"1101\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addContact",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addContact"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchContact",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n\"firstName\":\"aadtiya\",\r\n\"middleName\":\"pareek\",\r\n\"lastName\":\"Sharma\",\r\n\"phoneNumber\":\"phoneNumber\",\r\n\"email\":\"mohit\",\r\n\"isActive\":\"isActive\",\r\n\"inviteStatus\":\"inviteStatus\",\r\n\"invitationLink\":\"invitationLink\",\r\n\"jobType\":\"jobType\",\r\n\"notes\":\"notes\",\r\n\"id\"  :\"1101\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/searchContact",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchContact"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteContact",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/contact/1101",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"contact",
								"1101"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "CurrencyController",
			"item": [
				{
					"name": "addCurrency",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"code\":\"10111123\",\r\n    \"countryName\":\"india\",\r\n    \"countryCode\":\"1020201\",\r\n    \"symbolFilePath\":\"symbolFilePath\"\r\n\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addCurrency",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addCurrency"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateCurrency",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"code\":\"10111123\",\r\n    \"countryName\":\"indiaaaaaaa\",\r\n    \"countryCode\":\"1020201\",\r\n    \"symbolFilePath\":\"symbolFilePath\",\r\n    \"id\":\"1151\"\r\n\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateCurrency",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateCurrency"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchCurrency",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"code\":\"10111123\",\r\n    \"countryName\":\"indiaaaaaaa\",\r\n    \"countryCode\":\"1020201\",\r\n    \"symbolFilePath\":\"symbolFilePath\",\r\n   \r\n\r\n}"
						},
						"url": {
							"raw": "http://localhost:7050/api/searchCurrency",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchCurrency"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteCurrency",
					"request": {
						"method": "DELETE",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": ""
						},
						"url": {
							"raw": "http://localhost:7050/api/currency/1151",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"currency",
								"1151"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "DepartmentController",
			"item": [
				{
					"name": "addDepartment",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n \"name\":\"CAR Dehko\"\r\n\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addDepartment",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addDepartment"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateDepartment",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n \"name\":\"CAR Dehko.com\",\r\n  \"id\":\"2603\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateDepartment",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateDepartment"
							]
						}
					},
					"response": []
				},
				{
					"name": "department",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/department/1251",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"department",
								"1251"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchDepartment",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/searchDepartment",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchDepartment"
							]
						}
					},
					"response": []
				},
				{
					"name": "getDepartment/id",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/getDepartment/1",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"getDepartment",
								"1"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "DocumentController",
			"item": [
				{
					"name": "addDocument",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"fileName\":\"mohit sharma\",\r\n    \"fileType\":\"i dont no\",\r\n    \"fileSize\":\"34\",\r\n    \"localFilePath\":\"localFilePath\",\r\n    \"s3Bucket\":\"s3Bucket\",\r\n    \"sourceOfOrigin\":\"sourceOfOrigin\",\r\n    \"requisitionLineItemId\":\"3\",\r\n    \"contactId\":\"4\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addDocument",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addDocument"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateDocument",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"fileName\":\"please\",\r\n    \"fileType\":\"i dont no\",\r\n    \"fileSize\":\"34\",\r\n    \"localFilePath\":\"localFilePath\",\r\n    \"s3Bucket\":\"s3Bucket\",\r\n    \"sourceOfOrigin\":\"sourceOfOrigin\",\r\n    \"requisitionLineItemId\":\"3\",\r\n    \"contactId\":\"4\",\r\n    \"id\":\"1251\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateDocument",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateDocument"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchDocument",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"fileName\":\"please\",\r\n    \"fileType\":\"i dont no\",\r\n    \"fileSize\":\"34\",\r\n    \"localFilePath\":\"localFilePath\",\r\n    \"s3Bucket\":\"s3Bucket\",\r\n    \"sourceOfOrigin\":\"sourceOfOrigin\",\r\n    \"requisitionLineItemId\":\"3\",\r\n    \"contactId\":\"4\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/searchDocument",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchDocument"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteDocument",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/deleteDocument/1301",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"deleteDocument",
								"1301"
							]
						}
					},
					"response": []
				},
				{
					"name": "getDocument",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/getDocument/1",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"getDocument",
								"1"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "InvoiceController",
			"item": [
				{
					"name": "New Request",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \r\n}"
						},
						"url": {
							"raw": "http://localhost:7050/api/addInvoice",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addInvoice"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateInvoice",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n     \"id\":\"1901\",\r\n    \"invoiceNo\":\"120000\",\r\n    \"amount\":\"yahoo.com\",\r\n    \"modeOfPayment\":\"modeOfPayment\",\r\n    \"txnRefNo\":\"12345\",\r\n    \"issuerBank\":\"issuerBank\",\r\n    \"chequeOrDdNo\":\"chequeOrDdNo\",\r\n    \"notes\":\"notes\",\r\n    \"documentId\":\"1\",\r\n    \"quotationId\":\"2\"\r\n   \r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateInvoice",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateInvoice"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchinvoice",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \r\n     \"id\":\"1901\",\r\n    \"invoiceNo\":\"120000\",\r\n    \"amount\":\"yahoo.com\",\r\n    \"modeOfPayment\":\"modeOfPayment\",\r\n    \"txnRefNo\":\"12345\",\r\n    \"issuerBank\":\"issuerBank\",\r\n    \"chequeOrDdNo\":\"chequeOrDdNo\",\r\n    \"notes\":\"notes\"\r\n\r\n   \r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/searchInvoice",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchInvoice"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteInvoice",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/deleteInvoice/1452",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"deleteInvoice",
								"1452"
							]
						}
					},
					"response": []
				},
				{
					"name": "getInvoice/id",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": ""
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "PurchaseOrderController",
			"item": [
				{
					"name": "addPurchaseOrder",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"poNo\":\"93515233780\",\r\n    \"termsAndConditions\":\"aaaa\",\r\n    \"notes\":\"Aws cloued\"\r\n\r\n\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addPurchaseOrder",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addPurchaseOrder"
							]
						}
					},
					"response": []
				},
				{
					"name": "updatePurchaseOrder",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"poNo\":\"93515233780\",\r\n    \"termsAndConditions\":\"come on\",\r\n    \"notes\":\"Aws cloued\",\r\n    \"id\":\"1601\"\r\n\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updatePurchaseOrder",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updatePurchaseOrder"
							]
						}
					},
					"response": []
				},
				{
					"name": "New Request",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": ""
						}
					},
					"response": []
				},
				{
					"name": "approvepurchaseOrder",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": ""
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "QuotationController",
			"item": [
				{
					"name": "addQuotation",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n\"documentId\": \"3\",\r\n\"vendorId\":\"4\",\r\n\"quotationNo\":\"12\",\r\n\"notes\":\"yah\",\r\n\"status\":\"i am here\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addQuotation",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addQuotation"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateQuotation",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n\r\n\"notes\":\"yahoo\",\r\n\"status\":\"yahoo\",\r\n\"id\":\"1452\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateQuotation",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateQuotation"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteQuotation",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/deleteQuotation/1",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"deleteQuotation",
								"1"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "VendorController",
			"item": [
				{
					"name": "addVendor",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"firstName\":\"vishnu ji\",\r\n    \"middleName\":\"rajput\",\r\n    \"lastName\": \"lastName\",\r\n    \"phoneNumber\":\"9067658909\",\r\n    \"email\":\"email@mail.com\",\r\n    \"address\":\"address\",\r\n    \"zipCode\":\"123421\",\r\n      \"status\":\"yaho\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addVendor",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addVendor"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateVendor",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"firstName\":\"ronak sharma\",\r\n    \"middleName\":\"rajput\",\r\n    \"lastName\": \"lastName\",\r\n    \"phoneNumber\":\"9067658909\",\r\n    \"email\":\"email@mail.com\",\r\n    \"address\":\"asssddf\",\r\n    \"zipCode\":\"123421\",\r\n    \"status\":\"yahoo\",\r\n    \"id\":\"1801\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateVendor",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateVendor"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteVendor",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/deleteVendor/1301",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"deleteVendor",
								"1301"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchVendor",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"firstName\":\"vishnu kumar \",\r\n    \"middleName\":\"rajput\",\r\n    \"lastName\": \"lastName\",\r\n    \"phoneNumber\":\"9067658909\",\r\n    \"email\":\"email@mail.com\",\r\n    \"address\":\"asssddf\",\r\n    \"zipCode\":\"123421\",\r\n    \"status\":\"yahoo\",\r\n}"
						},
						"url": {
							"raw": "http://localhost:7050/api/searchVendor",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchVendor"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "RequisitionController",
			"item": [
				{
					"name": "addRequisition",
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "",
								"value": "",
								"type": "text"
							}
						],
						"body": {
							"mode": "formdata",
							"formdata": [
								{
									"key": "file",
									"type": "file",
									"src": "/C:/Users/admin/Pictures/88443975-the-concept-of-climate-has-changed-half-alive-and-half-dead-tree-standing-at-the-crossroads-save-the.jpg"
								},
								{
									"key": "obj",
									"value": "{\n    \"departmentId\":1201,\n    \"currencyId\":1151,\n    \"requisitionNo\" : 1,\n    \"progressStage\" : \"test\",\n    \"financialYear\": 2021,\n    \"status\":\"Active\",\n     \"roleName\":\"General_Director\",\n    \"totalPrice\": 2000,\n    \"notes\":\"ddatatata\",\n    \"requisitionLineItemLists\":[{\n\"itemDescription\":\"testt\",\n    \"price\":11\n        },{\n            \"itemDescription\":\"yy\",\n            \"price\":15\n        }]\n}",
									"type": "text"
								},
								{
									"key": "",
									"value": "",
									"type": "text",
									"disabled": true
								}
							]
						},
						"url": {
							"raw": "http://localhost:7050/api/addRequisition",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addRequisition"
							]
						}
					},
					"response": []
				},
				{
					"name": "approveRequisition",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"requisitionId\":\"1251\",\r\n    \"roleName\":\"General_Director\"\r\n    \r\n \r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/approveRequisition",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"approveRequisition"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateRequistion",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n     \"RequisitionId\":\"1251\",\r\n    \"departmentId\":1201,\r\n    \"currencyId\":1151,\r\n    \"requisitionNo\" : 332321,\r\n    \"progressStage\" : \"test1\",\r\n    \"financialYear\": 2021,\r\n    \"status\":\"Active\",\r\n     \"roleName\":\"General_Director\",\r\n    \"totalPrice\": 3000,\r\n    \"notes\":\"ddatatataaaaaaaaaaa\",\r\n    \"requisitionLineItemLists\":[{\r\n\"itemDescription\":\"testt\",\r\n    \"price\":11\r\n        },{\r\n            \"itemDescription\":\"yy\",\r\n            \"price\":15\r\n        }]\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateRequisition",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateRequisition"
							]
						}
					},
					"response": []
				},
				{
					"name": "serachRequisition",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/searchRequisition",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchRequisition"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteRequistion",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/deleteRequisition/1251",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"deleteRequisition",
								"1251"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "VendorRequisitionBucket",
			"item": [
				{
					"name": "sendRequisitionToVendor",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "[\r\n{\r\n    \"stages\":\"mohit\",\r\n    \"notes\":\"ggggggggggggggggggggggggggggggg\",\r\n    \"status\":\"gggggggggggggggggg\",\r\n    \"id\":\"1102\",\r\n    \"vendorId\"  :\"1151\",\r\n    \"requisitionId\":\"1351\"\r\n\r\n}\r\n]",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/sendRequisitionToVendor",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"sendRequisitionToVendor"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateRequisitionToVendor",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "[\r\n{\r\n    \"stages\":\"mohit\",\r\n    \"notes\":\"ggggggggggggggggggggggggggggggg\",\r\n    \"status\":\"gggggggggggggggggg\",\r\n    \"id\":\"1102\",\r\n    \"vendorId\"  :\"1151\",\r\n    \"requisitionId\":\"1351\"\r\n\r\n\r\n}\r\n]",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateRequisitionToVendor",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateRequisitionToVendor"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Roles",
			"item": [
				{
					"name": "addRoles",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"name\":\"General_Director\",\r\n    \"status\":\"ACTIVE\",\r\n    \"description\":\"Test 2 role\",\r\n    \"isGroup\":\"true\"\r\n   \r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addRoles",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addRoles"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateRoles",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "\r\n    {\r\n    \"name\":\"General_Director\",\r\n    \"status\":\"fdsssss\",\r\n    \"description\":\"ACTIVEdfgdf\",\r\n    \"isGroup\":\"false\",\r\n    \"id\":\"1001\"\r\n\r\n}\r\n",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateRoles",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateRoles"
							]
						}
					},
					"response": []
				},
				{
					"name": "serchRoles",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": ""
						},
						"url": {
							"raw": "http://localhost:7050/api/searchRoles",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchRoles"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteRoles",
					"request": {
						"method": "DELETE",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n   \r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/deleteRoles/3",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"deleteRoles",
								"3"
							],
							"query": [
								{
									"key": "",
									"value": "3",
									"disabled": true
								}
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Rules",
			"item": [
				{
					"name": "addRules",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n   \"roleId\":\"1001\",\r\n    \"name\":\"REQUISITION_TYPE\",\r\n    \"status\":\"ACTIVE\",\r\n    \"description\":\"requisition type rule\",\r\n    \"rule\":{\"nonstandard\":{\"min\":\"200\",\"max\":\"4000\"},\"standard\":{\"min\":\"6000\"}}\r\n\r\n}\r\n",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addRules",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addRules"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateRules",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"name\":\"mohit\",\r\n    \"status\":\"ACTIVE\",\r\n    \"description\":\"qqqqqqqqqqqq\",\r\n    \"rule\":\"qqqqqqqqqqq\",\r\n    \"id\":\"1101\"\r\n   \r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateRules",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateRules"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchRules",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/searchRules",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchRules"
							]
						}
					},
					"response": []
				},
				{
					"name": "getRulesid",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/getRules/1101",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"getRules",
								"1101"
							]
						}
					},
					"response": []
				},
				{
					"name": "deleteRules",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/deleteRules/1104",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"deleteRules",
								"1104"
							]
						}
					},
					"response": []
				},
				{
					"name": "GetByName",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/getRulesByName/REQUISITION_TYPE",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"getRulesByName",
								"REQUISITION_TYPE"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "approveRequisition",
			"item": []
		},
		{
			"name": "BuyerController",
			"item": [
				{
					"name": "addBuyer",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"firstName\":\"Ronak\",\r\n    \"middleName\":\"rajput\",\r\n    \"lastName\": \"lastName\",\r\n    \"phoneNumber\":\"9067658909\",\r\n    \"email\":\"email@mail.com\",\r\n    \"address\":\"address\",\r\n    \"zipCode\":\"123421\",\r\n      \"status\":\"ACTIVE\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/addBuyer",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"addBuyer"
							]
						}
					},
					"response": []
				},
				{
					"name": "updateBuyer",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"firstName\":\"ronak sharma\",\r\n    \"middleName\":\"rajput\",\r\n    \"lastName\": \"lastName\",\r\n    \"phoneNumber\":\"9067658909\",\r\n    \"email\":\"email@mail.com\",\r\n    \"address\":\"asssddf\",\r\n    \"zipCode\":\"123421\",\r\n    \"status\":\"yahoo\",\r\n    \"id\":\"1001\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/updateBuyer",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"updateBuyer"
							]
						}
					},
					"response": []
				},
				{
					"name": "searchBuyer",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7050/api/searchBuyer?id=1001",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"searchBuyer"
							],
							"query": [
								{
									"key": "id",
									"value": "1001"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "New Request",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"method": "GET",
						"header": [
							{
								"key": "",
								"value": "",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "// {\r\n//     \"username\":\"admin\",\r\n//     \"password\":\"password\",\r\n//     \"role\":\"NEW\",\r\n//     \"id\":1\r\n\r\n// }",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7050/api/ListofRequisition",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7050",
							"path": [
								"api",
								"ListofRequisition"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "LoginController",
			"item": [
				{
					"name": "New Request",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": ""
						}
					},
					"response": []
				}
			]
		}
	]
}