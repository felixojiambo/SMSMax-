package com.design.SMSMax.config;

import io.github.bucket4j.*;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitingAspect {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String apiKey) {
        return buckets.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1))))
                .build();
    }

    @Around("@annotation(RateLimited)")
    public Object rateLimit(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null) {
            apiKey = "default";
        }
        Bucket bucket = resolveBucket(apiKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            // Proceed with the method
            return pjp.proceed();
        } else {
            // Too many requests
            throw new RateLimitExceededException("Too many requests");
        }
    }
}