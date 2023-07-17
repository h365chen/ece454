import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.RetryNTimes;

public class BarrierExample {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java BarrierExample zkconnectstring barrier_node");
            System.exit(-1);
        }

        CuratorFramework curClient =
                CuratorFrameworkFactory.builder()
                        .connectString(args[0])
                        .retryPolicy(new RetryNTimes(10, 1000))
                        .connectionTimeoutMs(1000)
                        .sessionTimeoutMs(10000)
                        .build();


        curClient.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                curClient.close();
            }
        });

        DistributedBarrier barrier =
                new DistributedBarrier(curClient, args[1] + "/_basic_barrier");
        barrier.setBarrier();

        // The additional thread here simulates another machine inside the cluster.
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                    barrier.removeBarrier();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Barrier node has been removed");
            }
        });
        thread.start();

        System.out.println("=== Program is blocked by the barrier ===");
        barrier.waitOnBarrier();
        System.out.println("=== Program resumes execution ===");


    }
}
