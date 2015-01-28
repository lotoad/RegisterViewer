package com.pod.registerviewer

import com.pod.registerviewer.com.pod.registerviewer.Count

class CountController {

    def index() {
        def c = new Count()
        c.save(flush:true)
        [count:Count.count()]
    }
}
