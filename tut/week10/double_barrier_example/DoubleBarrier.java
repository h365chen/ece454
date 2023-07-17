import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DoubleBarrier {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: DoubleBarrier zkstring zkbarriernode num_clients");
            System.exit(-1);
        }

        try {
            String zkstring = args[0];
            String zknode = args[1] + "/_double_barrier";
            int numClients = Integer.parseInt(args[2]);
            CuratorFramework curClient = CuratorFrameworkFactory.newClient(
                            zkstring,
                            new ExponentialBackoffRetry(1000, 3)
            );
            curClient.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    curClient.close();
                }
            });

            DistributedDoubleBarrier barrier
                    = new DistributedDoubleBarrier(curClient, zknode, numClients);
            barrier.enter(); // sync
            barrier.leave(); // sync
            curClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
