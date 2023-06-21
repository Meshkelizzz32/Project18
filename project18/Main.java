package project18;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		BusyWait busy = BusyWait.buildBusyWait(new A());
		exec.execute(busy.a);
		exec.execute(busy);
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch(InterruptedException e) {
			System.out.println("sleep interrupted in main()");
		}
		System.out.println();
		BetterWait better = BetterWait.buildBetterWait(new A());
		exec.execute(better.getA());
		exec.execute(better);
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch(InterruptedException e) {
			System.out.println("sleep interrupted in main()");
		}
		synchronized(better) {
			System.out.println("Sending better.notifyAll()");
			better.notifyAll();
		}
		exec.shutdownNow();
	}
}