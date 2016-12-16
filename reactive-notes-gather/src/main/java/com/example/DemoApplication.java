/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@RestController
public class DemoApplication {

    @Value("${app.url:http://example.com}")
    private String url = "http://example.com";

    private static Logger log = LoggerFactory.getLogger(DemoApplication.class);
    private RestTemplate restTemplate = new RestTemplate();
    private Scheduler scheduler = Schedulers.elastic();

    @RequestMapping("/parallel")
    public CompletableFuture<Result> parallel() {
        log.info("Handling /parallel");
        return Flux.range(1, 10) // <1>
                .log() //
                .flatMap( // <2>
                        this::fetch, 4 ) // <3>
                .collect(Result::new, Result::add) // <4>
                .doOnSuccess(Result::stop) // <5>
                .toFuture();

        // <1> make 10 calls
        // <2> drop down to a new publisher to process in parallel
        // <3> concurrency hint in flatMap
        // <4> collect results and aggregate into a single object
        // <5> at the end stop the clock

    }
    
    private Mono<HttpStatus> fetch(int value) {

    	return Mono.fromCallable(() -> block(value)) // <1>
        .subscribeOn(this.scheduler);    	         // <2>
    	
    	// <1> blocking code here inside a Callable to defer execution
        // <2> subscribe to the slow publisher on a background thread

    }

    @RequestMapping("/serial")
    public CompletableFuture<Result> serial() {
        log.info("Handling /serial");
        return Flux.range(1, 10) // <1>
                .log() //
                .map( // <2>
                        this::block) // <3>
                .collect(Result::new, Result::add) // <4>
                .subscribeOn(this.scheduler) // <6>
                .doOnSuccess(Result::stop) // <5>
                .toFuture();
        // <1> make 10 calls
        // <2> stay in the same publisher chain
        // <3> blocking call not deferred (no point in this case)
        // <4> collect results and aggregate into a single object
        // <5> at the end stop the clock
        // <6> subscribe on a background thread
    }

    private HttpStatus block(int value) {
        return this.restTemplate.getForEntity(url, String.class, value).getStatusCode();
    }

    public static void main(String[] args) {
        System.setProperty("reactor.io.epoll", "false");
        SpringApplication.run(DemoApplication.class, args);
    }

}

class Result {

    private ConcurrentMap<HttpStatus, AtomicLong> counts = new ConcurrentHashMap<>();

    private long timestamp = System.currentTimeMillis();

    private long duration;

    public long add(HttpStatus status) {
        AtomicLong value = this.counts.getOrDefault(status, new AtomicLong());
        this.counts.putIfAbsent(status, value);
        return value.incrementAndGet();
    }

    public void stop() {
        this.duration = System.currentTimeMillis() - this.timestamp;
    }

    public long getDuration() {
        return this.duration;
    }

    public Map<HttpStatus, AtomicLong> getCounts() {
        return this.counts;
    }

}
