layout: true

class: middle

---

class: center, middle, inverse

# Apache ZooKeeper and Curator

---

## Roadmap

- We will use ZooKeeper Command Line Interface to quickly explore ZooKeeper
  operations
- We will talk about how to write Java code to achieve such operations

---

## ZooKeeper Command Line Interface

Using the zookeeper CLI would help you check if your code work as expected quickly

Download zookeeper-3.4.13.tar.gz from https://archive.apache.org/dist/zookeeper/zookeeper-3.4.13/

Note that the server (`$ZKSTRING`) can be found in `settings.sh` in the A3
starter code.

```bash
tar xf zookeeper-3.4.13.tar.gz     # remember to check if the version matches the assignment spec
cd zookeeper-3.4.13/bin/
./zkCli.sh -server $ZKSTRING:2181  # ZKSTRING can be found in settings.sh in the A3 starter code
```

---

After connection, we can try some simple commands (replace `$USER` to your user
id)

```bash
ls /$USER
```

```bash
create /$USER/node1 "hello world"
# ^ please do every operation under path /$USER
ls /$USER
```

```bash
get /$USER/node1
```

```bash
delete /$USER/node1
```

---

## Curator

The Curator is a high-level API built on top of ZooKeeper's official API. It
handles the complexity of managing connections and provides a cleaner interface
to the programmer.

Let's take a look some code in `A3Client.java`

---

### Creating a Curator client

```java
// code are inside the start() function
curClient = CuratorFrameworkFactory.builder()
    .connectString(zkConnectString)         // Set the url to the zookeeper server (in our case, $ZKSTRING)
    .retryPolicy(new RetryNTimes(10, 1000)) // Retry 10 times, wait 1s between each retry.
    .connectionTimeoutMs(1000)              // Set connection timeout to 1s.
    .sessionTimeoutMs(10000)                // Terminates the session if zookeeper does not hear from the client for more than 10s
    .build();

curClient.start();
```

---

### Listing nodes

```java
// code are inside the getPrimary() function
curClient.sync(); // sync the zookeeper cluster to make sure the
                  // client will get the newest data
List<String> children =
    curClient
        .getChildren()      // get the children list
        .usingWatcher(this) // set a watcher to listen to changes
        .forPath(zkNode);   // of a given directory (ZNode)
```

---

### Using Watcher

```java
public class A3Client implements CuratorWatcher {
    // ...
            // ... .usingWatcher(this) ...
    // ...

    // process() will be called when change occurs under zkNode
    synchronized public void process(WatchedEvent event) {
        log.info("ZooKeeper event " + event);
        try {
            primaryAddress = getPrimary();
        } catch (Exception e) {
            log.error("Unable to determine primary");
        }
    }

    // ...
}
```

---

### Creating ZNode

```java
// Creating a znode with mode set to EPHEMERAL_SEQUENTIAL:
curClient.create()
    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
    .forPath("/path/to/znode");

// Creating a znode with data:
curClient.create()
    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
    .forPath("/path/to/znode", data); //data needs to be a byte[]

// You can also set the data after the znode is created:
curClient.setData().forPath("/path/to/znode", data);
```

---

Let's add the following line to the end of `StorageNode.java`

```java
curClient.create()
    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
    .forPath(args[3] + "/znode");
```

build then run the server

```bash
./build.sh
./runserver.sh
```

Then use `zkCli` to `ls` the node while the server is running

---

### `kill` VS. `kill -9`

Let's take a closer look when the znode will be removed after we shutdown the
server.

```bash
./runserver.sh > log &
ps | grep java  # this will give you the pid of your server, note down the pid
```

- Shutdown the server using `kill` and `ls` the znode.
- Shutdown the server using `kill -9` and `ls` the znode. (You need to do the
  `ls` quickly)

Think: take a look at the `log` file in both cases, what do you see?

---

| Mode                  | Description                                                                                                                                 |
|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| EPHEMERAL             | The znode will be deleted upon the client's disconnect.                                                                                     |
| EPHEMERAL_SEQUENTIAL  | The znode will be deleted upon the client's disconnect, and its name will be appended with a monotonically increasing number.               |
| PERSISTENT            | The znode will not be automatically deleted upon client's disconnect.                                                                       |
| PERSISTENT_SEQUENTIAL | The znode will not be automatically deleted upon client's disconnect, and its name will be appended with a monotonically increasing number. |

---

### Deleting ZNode

```java
curClient.delete().forPath("/path/to/znode");
```

---

## Question

If the minimum session timeout for Zookeeper is 2 "ticks" and the tick time on
the zookeeper server is set to 500ms, how long does it take to remove the
ephemeral znode when the curator client disconnects?

Is it a high latency or is it reasonable?
