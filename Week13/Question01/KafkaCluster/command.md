```sh
./kafka-producer-perf-test.sh --topic testNeon --num-records 100000 --record-size 1000 --throughput 200000 --producer-props bootstrap.servers=localhost:9002
./kafka-consumer-perf-test.sh --bootstrap-server localhost:9002 --topic testNeon --fetch-size 1048576 --messages 100000 --threads 1
```

```sh
./zookeeper-server-start.sh ../config/zookeeper.properties
./kafka-server-start.sh ../config/server9001.properties
```

```sh
bin/kafka-console-consumer.sh --bootstrap-server localhost:9002 --from-beginning --topic testNeon
bin/kafka-console-producer.sh --bootstrap-server localhost:9003 --topic testNeon
```

