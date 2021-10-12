

# zk更新缓存的watcher

```java

public class ZookeeperWatcher implements Watcher
{
	@Autowired
	private Zookeeper zooKeeper;

	public void process(WatchedEvent event){
		if(event.getType()==EventType.NodeDataChanged){ //zk目录节点数据变化通知事件
			String path = event.getPath();
			String soldOutFlag = new String(zooKeeper.getData(path,true));
			if("false".equals(soldOutFlag)){
				String productId = path.substring(path.lastIndexOf("/")+1,path.length());
				MiaoshaAction.getProductSoldOutMap().remove(productId);
			}
		}

	}
}

```



