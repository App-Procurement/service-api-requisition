# API First Development
  - [What Is API-First App Development?](#what-is-api-first-app-development)
  - [API-First development with JHipster](#api-first-development-with-jhipster)  

## What Is API-First App Development  
  The API-first approach recognizes that the programming should begin with planning the API which means how API is going to behave before you start coding the application. With this approach, you can set clear goals on what will be provided by your application.  
  API-first development achieves consistency and reusability by using an API specification and the most widely-used API specification is the **OpenAPI Specification** (originally called the **Swagger Specification**).  
  In this tutorial, we'll learn **how to implement a Spring-based server stub using OpenAPI Generator via its maven plugin**.  
  
## API-First development with JHipster  
  When generating a JHipster application, you can choose the **``` API first development using OpenAPI-generator ```** option when prompted for additional technologies. This option will configure your build tool to use OpenAPI-generator to generate API code from an OpenAPI (Swagger) definition file. Both Swagger v2 and OpenAPI v3 formats are supported.  
  [Reference:](https://www.jhipster.tech/doing-api-first-development/)  
    
  JHipster does all the configuration setup in the project like **``` generate the API definition file api.yml, add maven plugin and required dependencies in pom.xml ```**.  

  > You can enhance the API definition file **api.yml** with the help of postman (API framework).  

  ```Once the project setup is done, you can verify the maven dependencies and plugin for OpenAPI Generator in pom.xml ```  
  > [Maven Plugin for OpenAPI Generator](MVN_OPEN_API_PLUGIN.md)  
  > [Maven Dependencies for OpenAPI Generator](MVN_OPEN_API_DEPENDENCIES.md)
