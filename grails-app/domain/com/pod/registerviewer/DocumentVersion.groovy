package com.pod.registerviewer

import org.bson.types.ObjectId

class DocumentVersion {

    static mapWith = "mongo"

    static constraints = {
    }

    //TODO need to add the usual createdBy, at, updatedBy, updatedAt.
    ObjectId id
    Document parentDocument
    ObjectId gridFsFileId
    String fileName
    //String generatedFileName

}
