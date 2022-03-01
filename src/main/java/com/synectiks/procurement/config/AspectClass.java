package com.synectiks.procurement.config;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.synectiks.procurement.controllers.ValidateAction;

import io.swagger.annotations.Contact;
@Aspect
@Component
public class AspectClass {
	
	@Around(" @annotation(com.synectiks.procurement.controllers.ValidateAction)")
	  public void validateAspect(ProceedingJoinPoint pjp) throws Throwable {
	    MethodSignature signature = (MethodSignature) pjp.getSignature();
	    Method method = signature.getMethod();

	    ValidateAction validateAction = method.getAnnotation(ValidateAction.class);
	    String obj = validateAction.obj();
	    Object h=new Object(); 
	    Contact manno=((Method) h).getAnnotation(Contact.class);  
	    System.out.println("value is: "+((Around) manno).value());  
System.out.println("dadadada"+obj);
	    // Call your Authorization server and check if all is good
	   
}
}
