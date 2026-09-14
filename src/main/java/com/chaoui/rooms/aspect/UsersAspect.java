package com.chaoui.rooms.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Aspect
//@Order(10) // order of execution if multiple aspects are defined for the same method or class
@Slf4j
public class UsersAspect {

    /*
    * Pointcut expressions
    * */
    @Pointcut("execution(* com.chaoui.rooms.services.UserService.registerNewUser(..))")
    public void registerUser() {
    }

    @Pointcut("execution(* com.chaoui.rooms.services.UserService.deleteUserById(..))")
    public void deleteUser() {
    }


    /*
    * Advice executions
    * */
    @Before("registerUser() || deleteUser()")
    public void logUserAction_Before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        List<String> argsList = Arrays.stream(joinPoint.getArgs())
            .map(Object::toString)
            .toList();

        IO.println("""
            ######## @Before Advice #######
            # logUserAction:
                JP long : %s
                Signature: %s
                Method : %s
                Args : %s
            """.formatted(
            joinPoint.toLongString(),
            signature,
            methodName,
            argsList
        ));

        log.info("User action: {} with args: {}", methodName, argsList);
    }

    @AfterReturning(
        pointcut = "registerUser() || deleteUser()",
        returning = "result"
    )
    public void logUserAction_AfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        /// IMPORTANT:
        /// With @AfterReturning we can modify the returned result before it arrives to the final caller
        /// Examples: adding items, formatting, filtering ...

        IO.println("""
            ######## @AfterReturning Advice #######
            # logUserAction:
                JP long : %s
                Signature: %s
                Result : %s
            """.formatted(
            joinPoint.toLongString(),
            signature,
            result
        ));

        log.info("User action: '{}' done with result: '{}'", signature.toShortString(), result);
    }

    @AfterThrowing(
        pointcut = "registerUser() || deleteUser()",
        throwing = "exception"
    )
    public void logUserAction_AfterThrowing(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        /// IMPORTANT:
        /// With @AfterThrowing the exception is still propagated to the final caller

        IO.println("""
            ######## @AfterThrowing Advice #######
            # logUserAction:
                JP long : %s
                Signature: %s
                Exception: %s
                Exception message: %s
            """.formatted(
            joinPoint.toLongString(),
            signature,
            exception.getClass().getName(),
            exception.getMessage()
        ));

        log.info("User action: '{}' throws exception: '{}' with message: '{}'",
            signature.toShortString(), exception.getClass().getName(), exception.getMessage());
    }

    @After("registerUser() || deleteUser()")
    public void logUserAction_After(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        /// IMPORTANT: @After will run for success or failure, like 'finally' in 'try-catch'

        IO.println("""
            ######## @After Advice #######
            # logUserAction:
                JP long : %s
                Signature: %s
            """.formatted(
            joinPoint.toLongString(),
            signature
        ));

        log.info("User action: '{}' done", signature.toShortString());
    }

    @Around("registerUser() || deleteUser()")
    public Object logUserAction_Around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();

        /// NB: @Around is equivalent to @Before and @AfterReturning combined
        /// We can handle method exceptions here and return a customized result if we don't want it to propagate to the main program.
        /// for example, we can use this to retry failed external API calls

        long start = System.currentTimeMillis();

        // Method execution and result handling
        var res = proceedingJoinPoint.proceed();

        long end = System.currentTimeMillis();

        IO.println("# Method took : "+ (end-start) + " ms");
        log.info("User action: '{}' done with result: '{}'", signature.toShortString(), res);

        return res;
    }
}

