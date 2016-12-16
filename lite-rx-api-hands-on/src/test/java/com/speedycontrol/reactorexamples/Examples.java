package com.speedycontrol.reactorexamples;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.pivotal.literx.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Examples {

	@Test
	public void test00() {
		
		List<String> words = Arrays.asList(
		        "the",
		        "quick",
		        "brown",
		        "fox",
		        "jumped",
		        "over",
		        "the",
		        "lazy",
		        "dog"
		        );
		
		 Flux<String> fewWords = Flux.just("Hello", "World");
	     Flux<String> manyWords = Flux.fromIterable(words);

	     fewWords.subscribe(System.out::println);
	     System.out.println();
	     manyWords.subscribe(System.out::println);
		
	}
	
	@Test
	public void test01() {
		
		java.util.function.Consumer<Integer> consumer = (r -> System.out.println(r));
		
		Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 8, 9))
			.subscribe(consumer);
		
	}
	
	@Test
	public void test02() {
		
		Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 8, 9))
			.all(r-> {
					System.out.println("all predicate -> " + r);
					return r > 0 && r < 10;
				})
			.subscribe(new Consumer<Boolean>() {
				@Override
				public void accept(Boolean t) {
					if (!t) {
						throw new AssertionError();
					}
				}
			});
	}
	
	@Test
	public void test03() {
		
		Flux.intervalMillis(1000)
		.subscribe(new Consumer<Long>() {
				@Override
				public void accept(Long t) {
					System.out.println(t);
				}
			});
		
		System.out.println("Terminando.");
		
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test04() {
		
		Flux.from(new Publisher<Long>() {
				@Override
				public void subscribe(Subscriber<? super Long> subscriber) {
					for (long l = 0; l < 10; l++) {
						subscriber.onNext(l);
					}
					subscriber.onComplete();
				}
			})
		.mergeWith(Flux.interval(Duration.ofMillis(1000)))
		.subscribe(new Consumer<Long>() {
				@Override
				public void accept(Long t) {
					System.out.println(t);
				}
			});
		
		try {
			TimeUnit.SECONDS.sleep(15);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Terminando.");
	}
	
	@Test
	public void test05() {
		
		System.out.println("Test5-Before");
		
//		Publisher<User> publisher = Mono.just(User.SAUL);
		Flux<User> flux = Flux.fromIterable(Arrays.asList(User.JESSE, User.SAUL));
		
//		Flux<User> flux = Flux.from(publisher);
		flux.doOnNext(t-> {
				System.out.println("doOnNext() -> " + t.getUsername());
			});
		
		Mono<Void> voidResult = flux.then(); 
		
		System.out.println("Test5-After: " + voidResult.toString());
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Finish");
		
	}

}
