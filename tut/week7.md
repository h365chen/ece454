layout: true

class: middle

---

class: center, middle, inverse

# Hadoop Distributed Cache and Spark Broadcast Variables

---

## Hadoop Distributed Cache

Ahmed has created excellent slides and sample code under

_Tutorials_ -> _Tutorial 7_

Read through (slide show) the slides to understand how distributed cache are
configured. To execute the sample code, simply run

```bash
ssh ecehadoop
tar -xf HadoopWC_distributed_cache.tar # assuming you have the file uploaded on ecehadoop
./run_WordCount_Hadoop_ClusterMode.sh
# ^ this will re-create input/, output/, and skip.txt,
#   you may want to adjust the paths
```

---

To show output

```bash
hdfs dfs -cat output/*
```

---

Which will show

```bash
Bear,2
Deer,2
River,2
```

---

### Named File (File Labels)

This method is much more simpler then specifying the full file path and may also
avoid issues if file paths are too long (source:
https://buhrmann.github.io/hadoop-distributed-cache.html)

Assuming the file label is `filelabel`, then inside `main()`

```diff
-        job.addCacheFile(new Path(otherArgs[2]).toUri());
+        job.addCacheFile(
+                new URI(
+                        new Path(otherArgs[2]) + "#filelabel"
+                )
+        );
```

---

Then change the `setup` function

```Java
@Override
public void setup(Context context) throws IOException, InterruptedException {
    URI[] cacheFiles = context.getCacheFiles();
    if (cacheFiles != null && cacheFiles.length > 0)
    {
        BufferedReader fis = new BufferedReader(
                new FileReader("filelabel")
                // ^ we refer to filelabel directly
        );
        String skipWord = null;
        while ((skipWord = fis.readLine()) != null) {
            wordsToSkip.add(skipWord);
        }
    }
}
```


---

## Spark Broadcast Variables

See https://spark.apache.org/docs/3.4.0/rdd-programming-guide.html#broadcast-variables

---

> Spark actions are executed through a set of stages, separated by distributed
> “shuffle” operations. Spark automatically broadcasts the common data needed by
> tasks within each stage. The data broadcasted this way is cached in serialized
> form and deserialized before running each task. This means that explicitly
> creating broadcast variables is only useful when tasks across multiple stages
> need the same data or when caching the data in deserialized form is important.

---

> After the broadcast variable is created, it should be used instead of the
> value `v` in any functions run on the cluster so that `v` is not shipped to
> the nodes more than once. In addition, the object `v` should not be modified
> after it is broadcast in order to ensure that all nodes get the same value of
> the broadcast variable (e.g. if the variable is shipped to a new node later).

---

To try it, we can use the spark shell

```bash
ssh ecehadoop
spark-shell
```

---

Inside the spark shell, run

```bash
scala> val broadcastVar = sc.broadcast(Array(1, 2, 3))
broadcastVar: org.apache.spark.broadcast.Broadcast[Array[Int]] = Broadcast(0)

scala> broadcastVar.value
res0: Array[Int] = Array(1, 2, 3)
```
