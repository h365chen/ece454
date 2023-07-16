layout: true

class: middle

---

class: center, middle, inverse

# ZooKeeper Barriers

---

## Roadmap

- We will show how to use ZooKeeper barriers

---

## A very basic example

```java
// CuratorFramework curClient = ...
DistributedBarrier barrier =
        new DistributedBarrier(curClient, "_barrier");
barrier.setBarrier();
// The additional thread here simulates another machine inside the cluster.
new Thread(new Runnable() {
    public void run() {
        try {
            Thread.sleep(3000);
            barrier.removeBarrier();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Barrier node has been removed");
    }
}).start();
System.out.println("=== Program is blocked by the barrier ===");
barrier.waitOnBarrier();
System.out.println("=== Program resumes execution ===");
```
