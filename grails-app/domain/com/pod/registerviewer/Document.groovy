package com.pod.registerviewer

import org.bson.types.ObjectId
import java.util.UUID;


class Document {

    static mapWith = "mongo"

    static hasMany = [documentVersions:DocumentVersion]

    static constraints = {
        expiry nullable: true
        currentPublishedVersion nullable: true
    }

    //static embedded = ['documentVersions']

    ObjectId id
    DocumentVersion currentPublishedVersion
    String title
    User owner
    User custodian
    Boolean published = true
    Boolean expire = false
    Date expiry
    Boolean deleted = false
    Classification classification = Classification.NONE
    /**
     * This is a file name we will generate for this document and the name
     * we will consistently pass to the gridFSservice - the actual name of the file we are
     * storing in reality against this document may change
     */
    String uniqueFileId

    /**
     *
     */
    //List documentVersions

    /**
     * What about meta-data should it be associated with the document or the documentVersion?
     */

    public Document(){
        generateUniqueFileId()
    }

    public Document(params){
        super()
        owner = params.owner
        custodian = params.custodian
        title = params.title
        generateUniqueFileId()
    }

    /**
     * Create a unique reference for this file (Probably a combination of the ObJId String and
     * something else...).
     * We can just use a uniqueId like we are doing in the project bootstrap?!
     */
    def generateUniqueFileId(){
        if(uniqueFileId == null){
            uniqueFileId = UUID.randomUUID().toString().toUpperCase()
            return uniqueFileId
        }else{
            return uniqueFileId
        }
        /*
        if(id != null){
            return id.toString()
        }else{
            throw new Exception("Unable to assign a GUID to this Document")
        }
        */
    }

}
