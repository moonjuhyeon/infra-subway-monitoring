package nextstep.subway.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger fileLogger = LoggerFactory.getLogger("file");
	private static final Logger jsonLogger = LoggerFactory.getLogger("json");
	private static final String LOGGING_CONTROLLER = "execution(public * nextstep.subway..ui.*Controller.*(..))";

	@Before(value = LOGGING_CONTROLLER)
	public void requestLog(JoinPoint joinPoint){
		fileLogger.info("{}, {} => Request: {}",
			joinPoint.getTarget().getClass(),
			joinPoint.getSignature().getName(),
			joinPoint.getArgs());

		jsonLogger.info("{}, {} => Request: {}",
			joinPoint.getTarget().getClass(),
			joinPoint.getSignature().getName(),
			joinPoint.getArgs());
	}

	@AfterReturning(value =  LOGGING_CONTROLLER, returning = "responseEntity")
	public void responseLog(JoinPoint joinPoint, ResponseEntity<?> responseEntity){
		fileLogger.info("{}.{} => Response [{}]: {} "
			, joinPoint.getTarget().getClass()
			, joinPoint.getSignature().getName()
			, responseEntity.getStatusCodeValue()
			, responseEntity.getBody());

		jsonLogger.info("{}.{} => Response [{}]: {} "
			, joinPoint.getTarget().getClass()
			, joinPoint.getSignature().getName()
			, responseEntity.getStatusCodeValue()
			, responseEntity.getBody());
	}

	@AfterThrowing(value = LOGGING_CONTROLLER, throwing = "exception")
	public void exceptionLog(JoinPoint joinPoint, Exception exception){
		fileLogger.error("{}.{} => Exception: {} "
			, joinPoint.getSignature().getName()
			, joinPoint.getArgs()
			, exception.getStackTrace());

		jsonLogger.error("{}.{} => Exception: {} "
			, joinPoint.getSignature().getName()
			, joinPoint.getArgs()
			, exception.getStackTrace());
	}
}
