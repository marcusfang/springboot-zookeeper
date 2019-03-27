package lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: BeatificWang
 * @Date: 2018/12/11 23:16
 */
public class CuratorLock {
	public static void main(String[] args) throws IOException {
		String s = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
		CuratorFramework framework = CuratorFrameworkFactory
				                             .builder()
				                             .connectString(s)
				                             .sessionTimeoutMs(40000)
				                             .retryPolicy(new ExponentialBackoffRetry(1000, 3))
				                             .namespace("curator").build();
		framework.start();
		InterProcessMutex interProcessMutex = new InterProcessMutex(framework, "/locks");
		CountDownLatch countDownLatch = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				try {
					countDownLatch.await();
					interProcessMutex.acquire();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, "Thread-" + i).start();
			countDownLatch.countDown();
		}
		System.in.read();
	}
}
