package com.pod.registerviewer

import org.bson.types.ObjectId

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
    Boolean published = false
    Boolean expire = false
    Date expiry
    Classification classification = Classification.NONE
    /**
     * This is a file name we will generate for this document and the name
     * we will consistently pass to the gridFSservice - the actual name of the file we are
     * storing in reality against this document may change
     */
    String generatedFilename

    /**
     *
     */
    //List documentVersions

    /**
     * What about meta-data should it be associated with the document or the documentVersion?
     */

    /*
    public Document(){
        this.generatedFilename = generateUniqueFileId()
        super()
    }
    */

    /**
     * Create a unique reference for this file (Probably a combination of the ObJId String and
     * something else...).
     * We can just use a uniqueId like we are doing in the project bootstrap?!
     */
    def generateUniqueFileId(){
        if(id != null){
            return id.toString()
        }else{
            throw new Exception("Unable to assign a GUID to this Document")
        }
    }

}
