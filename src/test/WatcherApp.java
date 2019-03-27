import org.apache.curator.utils.DefaultZookeeperFactory;
import org.apache.curator.utils.ZookeeperFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: BeatificWang
 * @Date: 2018/12/1 18:36
 */
public class WatcherApp {
	public static void main(String[] args) {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		String s = "192.168.74.253:2181,192.168.74.253:2182,192.168.74.253:2183";
		int sessionTimeOut = 3000;
		ZooKeeper zooKeeper = null;
		try {
			ZookeeperFactory zookeeperFactory = new DefaultZookeeperFactory();
			// ZooKeeper zooKeeper = zookeeperFactory.newZooKeeper(s, sessionTimeOut, null, true);
			zooKeeper = new ZooKeeper(s, sessionTimeOut, new org.apache.zookeeper.Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (Event.KeeperState.SyncConnected.equals(event.getState())
					) {
						countDownLatch.countDown();
					}
				}

			});
			// 强制等待至计数器归0
			countDownLatch.await();
			//CONNECTED
			System.out.println(zooKeeper.getState());
			String path = "/wang-test-watcher";
			// 新增节点,返回实际节点路径值
			String s1 = zooKeeper.create(path, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			Thread.sleep(2000);

			//使用exists绑定，生效一次
			zooKeeper.exists(path, new org.apache.zookeeper.Watcher() {
				@Override
				public void process(WatchedEvent event) {
					System.out.println("event.getType(): " + event.getType() + ",event.getPath(): " + event.getPath());
				}
			});


			// 新建Stat对象,记录节点状态
			Stat stat = new Stat();

			// 获取节点值
			byte[] bytes = zooKeeper.getData(path, null, stat);
			System.out.println(new String(bytes));

			// 修改节点值
			Stat stat1 = zooKeeper.setData(path, "1".getBytes(), stat.getVersion());
			Stat stat2 = zooKeeper.setData(path, "2".getBytes(), stat1.getVersion());

			// 获取节点值
			byte[] uBytes = zooKeeper.getData(path, null, stat);
			System.out.println(new String(uBytes));

			// 删除节点值
			zooKeeper.delete(path, stat2.getVersion());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				zooKeeper.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
