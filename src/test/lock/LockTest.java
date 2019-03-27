package lock;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: BeatificWang
 * @Date: 2018/12/4 22:53
 */
public class LockTest {
	public static void main(String[] args) throws IOException {
		CountDownLatch countDownLatch= new CountDownLatch(10);
		for (int i = 0; i < 10 ; i++) {
			new Thread(()->{
				try {
					countDownLatch.await();
					DistributedLock lock= new DistributedLock();
					// 获得锁
					lock.lock();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			},"Thread-"+i).start();
			countDownLatch.countDown();
		}
		System.in.read();

	}
}
