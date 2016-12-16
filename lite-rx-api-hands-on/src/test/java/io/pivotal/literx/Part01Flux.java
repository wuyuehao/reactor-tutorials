package io.pivotal.literx;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Learn how to create Flux instances.
 *
 * @author Sebastien Deleuze
 * @see <a href="http://projectreactor.io/core/docs/api/reactor/core/publisher/Flux.html">Flux Javadoc</a>
 */
public class Part01Flux {

//========================================================================================

	@Test
	public void empty() {
		Flux<String> flux = emptyFlux();

		StepVerifier.create(flux)
				.expectComplete()
				.verify();
	}

	// TODO Return an empty Flux
	Flux<String> emptyFlux() {
		return Flux.empty();
	}

//========================================================================================

	@Test
	public void fromValues() {
		Flux<String> flux = fooBarFluxFromValues();
		StepVerifier.create(flux)
				.expectNext("foo", "bar")
				.expectComplete()
				.verify();
	}

	// TODO Return a Flux that contains 2 values "foo" and "bar" without using an array or a collection
	Flux<String> fooBarFluxFromValues() {
		return Flux.fromArray(new String[] { "foo", "bar" });
	}

//========================================================================================

	@Test
	public void fromList() {
		Flux<String> flux = fooBarFluxFromList();
		StepVerifier.create(flux)
				.expectNext("foo", "bar")
				.expectComplete()
				.verify();
	}

	// TODO Create a Flux from a List that contains 2 values "foo" and "bar"
	Flux<String> fooBarFluxFromList() {
		return Flux.fromIterable(Arrays.<String>asList("foo", "bar"));
	}

//========================================================================================

	@Test
	public void error() {
		Flux<String> flux = errorFlux();
		StepVerifier.create(flux)
				.expectError(IllegalStateException.class)
				.verify();
	}
	// TODO Create a Flux that emits an IllegalStateException
	Flux<String> errorFlux() {
		return Flux.error(new IllegalStateException());
	}

//========================================================================================

	@Test
	public void countEach100ms() {
		Flux<Long> flux = counter();
		StepVerifier.create(flux)
				.expectNext(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)
				.expectComplete()
				.verify();
	}

	// TODO Create a Flux that emits increasing values from 0 to 9 each 100ms
	Flux<Long> counter() {
		
		return Flux.fromIterable(Arrays.<Long>asList(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L));
		
//		return Flux.from(new Publisher<Long>() {
//				@Override
//				public void subscribe(Subscriber<? super Long> subscriber) {
//					for (long l = 0; l < 10; l++) {
//						subscriber.onNext(l);
//					}
//					subscriber.onComplete();
//				}
//			});
		
//		return Flux.from(new Publisher<Long>() {
//			@Override
//			public void subscribe(Subscriber<? super Long> subscriber) {
//				for (long l = 0; l < 10; l++) {
//					if (l != 0) {
//						try {
//							TimeUnit.MILLISECONDS.sleep(100);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					System.out.println("Publisher onNext() -> " + l);
//					subscriber.onNext(l);
//				}
//				System.out.println("Publisher onCompleted()");
//				subscriber.onComplete();
//			}
//		});
		
	}

}
