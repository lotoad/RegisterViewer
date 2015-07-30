package com.pod.registerviewer

import grails.transaction.Transactional
import org.bson.types.ObjectId
import sun.reflect.generics.reflectiveObjects.NotImplementedException

@Transactional
class DocumentService {
    /**
     * What methods are we likely to need on a DocumentService?
     */

    //Add metaData?
    /**
     * Add MetaData to a DocumentVersion - this is hashmap of KV pairs that will
     * be stored in the database along with the Document/DocumentVersion. The MetaData will
     * be indexed and searchable.
     * @param metaData
     * @param documentVersion
     * @return
     */
    Document addMetaData(metaData, DocumentVersion documentVersion){

    }

    /**
     * Add metadata to the latest version of a document
     * @param metaData
     * @param document
     * @return
     */
    Document addMetaData(metaData, Document document){

    }

    //Get the documents for a category? - page-able? (Not to start with).

    //Get a individual document by id.
    /**
     * Get the Document by ObjectId
     * @param objectId
     * @return
     */
    Document getDocumentById(ObjectId objectId){

    }

    /**
     * Get the document by the string version of the ObjectId
     * @param stringObjectId
     * @return
     */
    Document getDocumentById(String stringObjectId){

    }

    /**
     * Create a new Document from passed params.
     * @return
     */
    Document createDocument(documentData){
        throw new NotImplementedException()
    }

    /**
     * Delete a docuemt based on the ObjectId.
     * @param objectId
     * @return
     */
    Boolean deleteDocument(ObjectId objectId){

    }

    /**
     * Re-index a document - TBA
     * @param document
     * @return
     */
    Boolean reIndexDocument(Document document){

    }

    /**
     * Set the current published version to the DocumentVersion that is passed here.
     * @param documentVersion
     * @param document
     * @return
     */
    Document setCurrentPublishedVersion(DocumentVersion documentVersion, Document document){

    }

}
