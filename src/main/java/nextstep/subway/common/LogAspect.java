package nextstep.subway.common;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import sun.rmi.runtime.Log;

@Aspect
@Component
public class LogAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final Logger fileLogger = LoggerFactory.getLogger("file");

	@Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
	private void controllerTarget(){}

	@Around("controllerTarget()")
	public Object loggerAdvice(ProceedingJoinPoint pjp) throws Throwable{
		String className = pjp.getSignature().getDeclaringType().getSimpleName();
		String methodName = pjp.getSignature().getName();

		log.info("[REQUEST] {}:{}, {}", className, methodName, pjp.getArgs());
		fileLogger.info("[REQUEST] {}:{}, {}", className, methodName, pjp.getArgs());

		Object result = pjp.proceed();

		log.info("[RESPONSE] {}:{}, {}", className, methodName, result);
		fileLogger.info("[RESPONSE] {}:{}, {}", className, methodName, result);

		return result;
	}
}
