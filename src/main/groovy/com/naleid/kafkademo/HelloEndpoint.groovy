package com.naleid.kafkademo

import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.handling.GroovyHandler

class HelloEndpoint extends GroovyHandler {
    @Override
    protected void handle(GroovyContext context) {
        context.byContent {
            json {
                handleJson(context)
            } noMatch {
                throw new Exception("must be json")
            }
        }
    }

    protected void handleJson(GroovyContext context) {
        context.render("Hello from KafkaDemo")
    }
}
