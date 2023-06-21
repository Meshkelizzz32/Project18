package project18;

import java.util.concurrent.TimeUnit;

class A implements Runnable {
	boolean flag = false;
	public synchronized void run() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch(InterruptedException e) {
			System.out.println("sleep interrupted in A");
		}
		System.out.println("A setting flag = true"); 
		flag = true;		
	}	
}

class BusyWait implements Runnable {
	A a = new A();
	long start, end;
	public synchronized A getA() { return a; }
	private BusyWait(A a) {
		this.a = a;
	}
	public static BusyWait buildBusyWait(A a) {
		return new BusyWait(a);
	}
	public synchronized void run() {
		System.out.println("Busy a.flag = " + a.flag);		
		while(!Thread.interrupted()) {	
			start = System.nanoTime();		
			if(a.flag) {
				a.flag = false;
				System.out.println("BusyWait reset a.flag = false");
				end = System.nanoTime();
				System.out.println("Busy waiting " + (end - start) + " nanoseconds");
			}
		}		
	}
}

class BetterWait implements Runnable {
	private A a = new A();
	public synchronized A getA() { return a; }
	private BetterWait(A a) {
		this.a = a;
	}
	public static BetterWait buildBetterWait(A a) {
		return new BetterWait(a);
	}
	public synchronized void run() {
		System.out.println("Better a.flag = " + a.flag);
		try {			
			while(!a.flag) {
				wait();	
				a.flag = false;
				System.out.println("BetterWait reset a.flag = false");
			}				
		} catch(InterruptedException e) {
			System.out.println("BetterWait.run() interrupted");
		}
	}
}