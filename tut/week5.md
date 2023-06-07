layout: true

class: middle

---

class: center, middle, inverse

# Intro to ECE Hadoop

---

## Roadmap

- We will talk about how to access ecehadoop and perform basic operations to get
  familiar with it

---

## Demo

- ecehadoop

```bash
ssh eceterm
# on eceterm
ssh ecehadoop.private.uwaterloo.ca
```

_Think: how to config SSH so you can log into ecehadoop without typing password?_

---

- Home folder

```bash
hdfs dfs -ls /
hdfs dfs -ls /user/<yourid>
# hdfs dfs -ls /user/<otherid>
# ^^^Can you do this?
```

---

- Copy a file from local to HDFS

```bash
echo "Hello ecehadoop!" > hello.txt
```

```bash
hdfs dfs -copyFromLocal hello.txt
hdfs dfs -ls
# ^^^Where is the folder containing the file?
hdfs dfs -cat hello.txt
```

_Try: Can you overwrite the file?_

---

- Copy a file from HDFS to local

```bash
rm hello.txt
ls
hdfs dfs -copyToLocal hello.txt
cat hello.txt
```

---

- Stat

```bash
hdfs dfs -stat hello.txt
# with format
hdfs dfs -stat "type:%F perm:%a %u:%g size:%b mtime:%y atime:%x name:%n" hello.txt
```

See
https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/FileSystemShell.html#stat

---

- Remove

```bash
hdfs dfs -rm hello.txt
```

---

## Cluster Management

- Cluster statistics

```bash
hdfs dfsadmin -report
```

Useful references:

https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HDFSErasureCoding.html

---

- To check replication factor

```bash
hdfs fsck hello.txt
hdfs fsck /user/h365chen
```

Useful references:

https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html#fsck

