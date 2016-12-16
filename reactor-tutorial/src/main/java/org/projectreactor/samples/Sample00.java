package org.projectreactor.samples;

import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class Sample00 {

	public static void main(String[] args) {
		
		Mono.delayMillis(3000)
	    .map(d -> "Spring 4")
	    .or(Mono.delayMillis(2000).map(d -> "Spring 5"))
	    .or(Mono.delayMillis(1000).map(d -> "Spring 3"))
	    .then(t -> Mono.just(t + " world"))
	    .elapsed()
	    .subscribe(System.out::println);
		
		try {
			TimeUnit.SECONDS.sleep(8);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		Mono.delayMillis(3000)
//	    .map(d -> "Spring 4")
//	    .or(Mono.delayMillis(2000).map(d -> "Spring 5"))
//	    .then(t -> Mono.just(t + " world"))
//	    .elapsed()
//	    .subscribe(System.out::println);
		
//		Tuple2<Long, Long> nowAndLater = 
//		        Mono.when(
//		                Mono.just(System.currentTimeMillis()),
//		                Flux.just(1).delayMillis(1).map(i -> System.currentTimeMillis()))
//		            .block();
		
		
//	    .then(time -> Mono.first(serviceA.findRecent(time), serviceB.findRecent(time)))
//	    .timeout(Duration.ofSeconds(3), errorHandler::fallback)
//	    .doOnSuccess(r -> serviceM.incrementSuccess())
//	    .subscribe(System.out::println);
		

	}

}
