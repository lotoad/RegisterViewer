package com.pod.registerviewer

import org.bson.types.ObjectId

class DocumentVersion {

    static mapWith = "mongo"

    static constraints = {
    }


    public DocumentVersion(){
        super()
    }

    public DocumentVersion(params){
        super()
        parentDocument = params.parentDocument
        gridFsFileId = params.gridFsFileId
        fileName = params.fileName
    }

    //TODO need to add the usual createdBy, at, updatedBy, updatedAt.
    ObjectId id
    Document parentDocument
    ObjectId gridFsFileId
    String fileName
    HashMap<String, String> metaData = [:]
    Boolean deleted = false
    Boolean published = true

}
