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
barrier.setBarrier();  // set barrier node at the "_barrier"
// The additional thread here simulates another machine inside the cluster.
new Thread(new Runnable() {
    public void run() {
        try {
            Thread.sleep(3000);
            barrier.removeBarrier();  // remove the barrier node
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Barrier node has been removed");
    }
}).start();
System.out.println("=== Program is blocked by the barrier ===");
barrier.waitOnBarrier();  // block until the barrier is removed
System.out.println("=== Program resumes execution ===");
```

---

An example is provided under week10/

```bash
cp a3_starter/settings.sh basic_barrier_example/
cd basic_barrier_example
./build_run.sh
```

Also see https://curator.apache.org/curator-recipes/barrier.html

---

## Double Barrier

```java
// CuratorFramework curClient = ...
DistributedDoubleBarrier barrier
        = new DistributedDoubleBarrier(curClient, zknode, numClients);
barrier.enter(); // sync, wait until enough number of clients (numClients) to enter
barrier.leave(); // sync, wait until enough number of clients (numClients) to leave
```

---

Also see https://curator.apache.org/curator-recipes/double-barrier.html

---

### Use ZK Double Barrier to write a testing harness for A3

Process 1 running on host A:
- wait on barrier
- kill -9 any A3 server node running on host A
- sleep 2s
- start an A3 server node on host A
- sleep 2s
- wait on barrier
- repeat from the top

---

Process 2 on host B:
- wait on barrier
- wait on barrier
- kill -9 any A3 server node running on host B
- sleep 2s
- start an A3 server node on host B
- sleep 2s
- repeat from the top

---

See the example provided under week10/

```bash
cp a3_starter/settings.sh double_barrier_example/
```

```bash
cd double_barrier_example
./build.sh
```

Then on two different ece machines, do

```bash
./runproc1.sh # host A
```

```bash
./runproc2.sh # host B
```

Then check the outputs

(*If the script stucks, kindly wait until it finishes*)
