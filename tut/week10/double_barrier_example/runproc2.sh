#!/bin/bash

#
# Huanyi Chen, 2023
#

source settings.sh

# TODO Ctrl+C fix
trap '[ -v SNPID ] && kill -9 $SNPID; exit 1' SIGINT SIGTERM

# repeat 3 times
for i in $(seq 1 3);
do
  # wait on barrier until there are two .enter() are called
  echo "hit barrier"
  $JAVA_HOME/bin/java -cp .:"lib/*" DoubleBarrier $ZKSTRING /$USER 2

  # wait on barrier
  echo "hit barrier"
  $JAVA_HOME/bin/java -cp .:"lib/*" DoubleBarrier $ZKSTRING /$USER 2

  # if there is a storage node running
  # kill -9 storage node running on host B
  [ -v SNPID ] && echo 'kill the running node' && kill -9 $SNPID

  sleep 2

  # start a fake storage node on host B
  $JAVA_HOME/bin/java FakeStorageNode &
  SNPID=$! # note down its storage node pid

  sleep 2
  # repeat from the top
done
