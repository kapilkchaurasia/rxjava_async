package rxjava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Entry {

	public static void main(String[] args) throws InterruptedException {
		
		// creating observable from simplest operator = just
		Observable.just("hi","hello","okay").subscribe(z -> System.out.print(z));
		
		// creating observable using create in lambda style[Sync]
		Observable.create( f  ->{
							f.onNext("hello");
							if( f.isUnsubscribed() ){
								return; 
							}}).subscribe( s -> System.out.println(s));
		
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		System.out.println("----"+Thread.currentThread().getName());
		
		// creating observable using create in lambda style[Async]
		Observable.create(f -> {executor.submit(() ->{
			        f.onNext(Thread.currentThread().getName());
	     	});}).subscribe(s -> {System.out.println(Thread.currentThread().getName()); 
	     							System.out.println(s);} );
		
		// creating observable using create in lambda style[Async + observerOn]
		Observable.create(f -> {executor.submit(() ->{
			        f.onNext(Thread.currentThread().getName());
	     	});}).observeOn(Schedulers.newThread()).subscribe(s -> {System.out.println(Thread.currentThread().getName()); 
	     							System.out.println(s);} );

		
		// creating observable using create in lambda style[Async + subscribeOn]
		Observable.create(f -> {executor.submit(() ->{
			        f.onNext(Thread.currentThread().getName());
	     	});}).subscribeOn(Schedulers.newThread()).subscribe(s -> {System.out.println(Thread.currentThread().getName()); 
	     							System.out.println(s);} );

		
		List<Integer> result = new ArrayList<Integer>();
		//bulk API simulation
		Observable<Integer> obs = Observable.just(1,2,3,4,5,6).map(userId ->{
			//make mysql call
		  try { System.out.println("---"+Thread.currentThread().getName());
			return makeSqlCall(userId);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		});
				
	   obs.subscribeOn(Schedulers.io()).subscribe(id ->{ result.add(id);System.out.println(Thread.currentThread().getName());});
	   for(Integer x: result){System.out.print(x+" - ");}
		
	}
	
	static int makeSqlCall(int userId) throws InterruptedException{
		Thread.sleep(1000);
		return userId;
	}
	

}
