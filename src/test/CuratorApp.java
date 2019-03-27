import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Auther: BeatificWang
 * @Date: 2018/12/2 10:31
 */
public class CuratorApp {
	public static void main(String[] args) {
		String s = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
		CuratorFramework framework = CuratorFrameworkFactory
				                             .builder()
				                             .connectString(s)
				                             .sessionTimeoutMs(40000)
				                             .retryPolicy(new ExponentialBackoffRetry(1000, 3))
				                             .namespace("curator").build();
		framework.start();
		System.out.println(framework.getState());

		String path = "/wang/test1";
		try {
			addTreeCache(framework, path);
			//			// 创建节点
			//			String string = framework.create()
			//					                .creatingParentsIfNeeded()
			//					                .withMode(CreateMode.PERSISTENT)
			//					                .forPath(path, "1".getBytes());
			//			System.out.println(string);
			//			Stat stat = new Stat();
			//			// 获取节点
			//			framework.getData().storingStatIn(stat);
			//			System.out.println(stat);
			//			// 更新节点
			//			stat = framework.setData().withVersion(stat.getVersion()).forPath(path, "2".getBytes());
			//			System.out.println(stat);
			//			// 获取节点
			//			byte[] bytes = framework.getData().forPath(path);
			//			System.out.println(new String(bytes));
			//			// 删除节点
			//			framework.delete().deletingChildrenIfNeeded().forPath(path);
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void addTreeCache(CuratorFramework framework, String path) throws Exception {
		TreeCache treeCache = new TreeCache(framework, path);
		TreeCacheListener treeCacheListener = new TreeCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				System.out.println("event.getType(): " + event.getType() + ",event.getData().getPath(): " + event.getData().getPath());
			}
		};
		treeCache.getListenable().addListener(treeCacheListener);
		treeCache.start();
	}
}
