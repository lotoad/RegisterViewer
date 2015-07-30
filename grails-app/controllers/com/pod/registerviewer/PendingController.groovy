package com.pod.registerviewer

import grails.converters.JSON

class PendingController {

    def pendingDocumentService

    /**
     * List all of the pending files.
     */
    def index() {
        //TODO need to decide whether this should return JSON or should be the html in which casd
        // /pending shoudl return the JSON if we are being RESTful.
    }

    /**
     * Return a JSON list of all the pending files.
     * @return
     */
    def list(){
        def files = pendingDocumentService.listPendingFiles()
        def pendingFiles = []
        files.each {
            def file = [name:it.getName(), created:new Date(it.lastModified)]
            pendingFiles.add(file)
        }
        return pendingFiles as JSON
    }

}
