package com.naleid.kafkademo

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import groovy.transform.CompileStatic

@CompileStatic
class KafkaDemoModule  extends AbstractModule {
    @Override
    protected void configure() {
        bind(HelloEndpoint).in(Scopes.SINGLETON)
        bind(KafkaService).in(Scopes.SINGLETON)
    }
}
