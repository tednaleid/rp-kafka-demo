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

    VALUE=$RANDOM
    echo "sending $VALUE"
    echo $VALUE | kafkacat -P -b localhost:9092 -t the-topic
    done;
    
    
Do that and you should see output from the ratpack like:

    Polling!
    Polling!
    offset: 169, key: null value: 31850
    Polling!
    Polling!
    offset: 170, key: null value: 6335
    Polling!
    Polling!
    offset: 171, key: null value: 23058
    Polling!
    Polling!
    offset: 172, key: null value: 30356
    Polling!
    Polling!
    offset: 173, key: null value: 1995
    Polling!