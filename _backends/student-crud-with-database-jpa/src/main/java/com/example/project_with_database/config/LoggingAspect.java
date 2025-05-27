package com.example.project_with_database.config;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	// Pointcut that matches all methods in our service package
	@Pointcut("execution(* com.example.project_with_database.service.*.*(..))")
	public void servicePointcut() {

	}

	// Pointcut that matches all methods in our controller package
	@Pointcut("execution(* com.example.project_with_database.controller.*.*(..))")
	public void controllerPointcut() {

	}

	// Log before executing service methods
	@Before("servicePointcut()")
	public void logBeforeService(JoinPoint joinPoint) {
		log.info("Starting service method: {} with arguments: {}", joinPoint.getSignature().getName(),
				Arrays.toString(joinPoint.getArgs()));
	}

	// Log after executing service methods
	@AfterReturning(pointcut = "servicePointcut()", returning = "result")
	public void logAfterService(JoinPoint joinPoint, Object result) {
		log.info("Service method: {} completed with result: {}", joinPoint.getSignature().getName(), result);
	}

	// Log exceptions thrown by service methods
	@AfterThrowing(pointcut = "servicePointcut()", throwing = "exception")
	public void logAfterService(JoinPoint joinPoint, Exception exception) {
		log.error("Exception in service method: {} with exception: {}", joinPoint.getSignature().getName(),
				exception.getMessage());
	}

	// Log exception time for controller methods using Around advice
	@Around("controllerPointcut()")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();

		log.info("Starting controller method: {}", joinPoint.getSignature().getName());

		try {
			// Execute the method
			Object result = joinPoint.proceed();

			// Log method exit and execution time
			long endTime = System.currentTimeMillis();
			log.info("Controller method: {} completed in {}ms", joinPoint.getSignature().getName(),
					(endTime - startTime));

			return result;

		} catch (Exception e) {
			// Log exceptions
			log.error("Exception in controller method: {} with message: {}", joinPoint.getSignature().getName(),
					e.getMessage());
			throw e;
		}

	}

}
