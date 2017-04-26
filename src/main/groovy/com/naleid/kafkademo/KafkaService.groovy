package com.naleid.kafkademo

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import ratpack.exec.Blocking
import ratpack.exec.ExecResult
import ratpack.exec.Execution
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.func.Function
import ratpack.service.Service
import ratpack.service.StartEvent

@CompileStatic
@Slf4j
class KafkaService implements Service, Action<Execution> {

    KafkaConsumer<String, String> consumer

    @Override
    void onStart(StartEvent event) throws Exception {
        Properties props = new Properties()
        props.putAll([
                "bootstrap.servers"      : "localhost:9092",
                "group.id"               : "test-consumer",
                "enable.auto.commit"     : "true",
                "auto.commit.interval.ms": "1000",
                "session.timeout.ms"     : "30000",
                "key.deserializer"       : "org.apache.kafka.common.serialization.StringDeserializer",
                "value.deserializer"     : "org.apache.kafka.common.serialization.StringDeserializer"
        ])

        consumer = new KafkaConsumer<>(props)
        consumer.subscribe(["the-topic"])

        Execution.fork().start(this)
    }


    @Override
    void execute(Execution execution) throws Exception {
        // this starts the recursivePolling and makes it active by asking for the result (that we never expect)
        recursivePoll().result({ ExecResult result ->
            if (result.isError()) {
                log.error("Error!", result.throwable)
            } else {
                log.info("Consumer shutting down for some other reason: ", result.value)
            }
            consumer.close()
        })
    }

    // instead of doing a recursive poll, it might also work to make this a Runnable and do a
    // ExecController.require().getExecutor().scheduleAtFixedRate(this, 0, 5, TimeUnit.SECONDS)
    // then in the `run` method call poll like this:
    //    public void run() {
    //        ExecController.require().fork().start(e -> {
    //            Blocking.get({ -> consumer.poll(100) })
    //                .flatMap(this.&process as Function)
    //        });
    //    }

    Promise recursivePoll() {
        Blocking.get(this.&pollKafka)
                .flatMap(this.&process as Function)
                .flatMap({ ignore -> this.recursivePoll() } as Function)
    }

    ConsumerRecords<String, String> pollKafka() {
        println "Polling on ${Thread.currentThread().name}"
        consumer.poll(1000)
    }

    Promise process(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            println "offset: ${record.offset()}, key: ${record.key()} value: ${record.value()} on ${Thread.currentThread().name}"
        }

        return Promise.value("instead of printing, call redis or whatever and then return a promise from that when it is done")
    }
}
