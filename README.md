# Ratpack Kafka Demo

Simple ratpack app showing one way to connect a service to a kafka topic on startup and have it emit promises 
within a ratpack `Execution`.

This could be used to save the events to a database/cache in an asynchronous manner.

To use it:

If you don't have a kafka running already (0.10.1.0 or greater), run the dockerfile to start one up on localhost:9092:

    docker-compose up -d
    
Then you can run the app by running the `Ratpack.groovy` file (either in your IDE) or via `./gradlew run`

It will start listening a topic called `the-topic` in a polling loop and will print out any records that it finds.

You can send things to that topic with a tool like `kafkacat` (`brew install kafkacat` if you don't have it) using
something like:

    for I in $(seq 100); do
    VALUE=$RANDOM
    echo "sending $VALUE"
    echo $VALUE | kafkacat -P -b localhost:9092 -t the-topic
    done;
    
If you want to send a bunch of things to test out batching, you can do something like:

    seq 100000 | kafkacat -P -b localhost:9092 -t the-topic
    
Do that and you should see output from the ratpack like:

    Polling on ratpack-blocking-3-1
    offset: 412, key: null value: 20910 on ratpack-compute-1-2
    Polling on ratpack-blocking-3-1
    Polling on ratpack-blocking-3-1
    offset: 413, key: null value: 13779 on ratpack-compute-1-2
    Polling on ratpack-blocking-3-1
    Polling on ratpack-blocking-3-1
    offset: 414, key: null value: 22697 on ratpack-compute-1-2
    Polling on ratpack-blocking-3-1
    
    
if you curl the endpoint, you'll see that the same event loop thread is free for it to use (this uses [`ganda`](https://github.com/tednaleid/ganda)):

    $ yes | awk '{print "http://localhost:5050/hello"}' | ganda

    Response: 200 http://localhost:5050/hello
    Hello from ratpack-compute-1-2
    Response: 200 http://localhost:5050/hello
    Hello from ratpack-compute-1-6
    Response: 200 http://localhost:5050/hello
    Hello from ratpack-compute-1-6
    Response: 200 http://localhost:5050/hello
    Hello from ratpack-compute-1-1
    Response: 200 http://localhost:5050/hello
    Hello from ratpack-compute-1-1