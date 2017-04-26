import com.naleid.kafkademo.HelloEndpoint
import com.naleid.kafkademo.KafkaDemoModule

import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        module(KafkaDemoModule)
    }
    handlers {
        get('hello', HelloEndpoint)
    }
}
