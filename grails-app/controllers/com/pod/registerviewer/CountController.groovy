package com.pod.registerviewer

class CountController {

    def index() {
        def c = new Count()
        c.save(flush:true)
        [count:Count.count()]
    }
}
