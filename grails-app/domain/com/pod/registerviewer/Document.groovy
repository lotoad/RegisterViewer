package com.pod.registerviewer

class Document {

    static constraints = {
    }

    String currentPublishedVersion
    String title
    Boolean published
    Map<String,Version> versions
    static embedded = ['versions']

}
