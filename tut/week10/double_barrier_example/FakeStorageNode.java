public class FakeStorageNode {
    public static void main(String [] args) throws InterruptedException {
        // the node should be killed every 2s
        // but if something happens, it will stop in at most 10s
        for (int i = 0; i < 10; i++) {
        System.out.println("The fake storage node is running");
        Thread.sleep(1000);
        }
        System.out.println("The fake storage node stopped");
    }
}
