package com.pod.registerviewer

import grails.transaction.Transactional
import org.bson.types.ObjectId
import sun.reflect.generics.reflectiveObjects.NotImplementedException

@Transactional
class DocumentService {

    def gridFsService

    /**
     * What methods are we likely to need on a DocumentService?
     *
     * The document service has to:
     *
     * Persist a Document and the associated DocumentVersions associated with it.
     *
     * The DocumentVersion represents the actual 'file' and maintains the link to the GridFS entry.
     *
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
        throw new org.apache.commons.lang.NotImplementedException()
    }

    /**
     * Add metadata to the latest version of a document
     * @param metaData
     * @param document
     * @return
     */
    Document addMetaData(metaData, Document document){
        throw new org.apache.commons.lang.NotImplementedException()
    }

    //Get the documents for a category? - page-able? (Not to start with).

    /**
     * Get the Document by ObjectId
     * @param objectId
     * @return
     */
    Document getDocumentById(ObjectId objectId){
        return Document.findById(objectId)
    }

    /**
     * Get the document by the string version of the ObjectId
     * @param stringObjectId
     * @return
     */
    Document getDocumentById(String stringObjectId){
        ObjectId id = new ObjectId(stringObjectId)
        return getDocumentById(id)
    }

    /**
     * Create a new Document from passed params.
     * @return
     */
    Document createDocument(documentData){
        //TODO Check that we have the correct params!
        def d = new Document([owner: documentData.owner, custodian: documentData.custodian,
                title: documentData.title])
        d.save(flush:true) //Do we need to do this?
        def objId = addToGridFs(d.uniqueFileId, documentData.file)
        DocumentVersion dv = new DocumentVersion([parentDocument: d, fileName: documentData.file.name, gridFsFileId: objId])
        dv.save(flush:true)
        d.currentPublishedVersion = dv
        d.addToDocumentVersions(dv)
        d.save(flush:true)
        return d
    }

    /**
     * Delete a document based on the ObjectId.
     * @param objectId
     * @return
     */
    //Note this is the 'super delete' i.e completely erase, not just mark as deleted.
    Boolean deleteDocument(ObjectId objectId){
        log.warn("Deleting document with id:${objectId.toString()}.")
        //Delete a document it's documentVersions and associated GridFS entries.
        def d = Document.findById(objectId)
        //Delete all the GridFsItems.
        d.getDocumentVersions().each{ dv ->
            gridFsService.deleteFile(dv.gridFsFileId)
            dv.delete()
        }
        d.delete()
        log.warn("Deleted.")
        return true
    }

    /**
     * Re-index a document - TBA
     * @param document
     * @return
     */
    Boolean reIndexDocument(Document document){
        throw new org.apache.commons.lang.NotImplementedException()
    }

    /**
     * Set the current published version to the DocumentVersion that is passed here.
     * @param documentVersion
     * @param document
     * @return
     */
    Document setCurrentPublishedVersion(DocumentVersion documentVersion, Document document){
        log.info("Updating ${document.title} with id:${document.id.toString()} current version to documentVersion:${documentVersion.id.toString()} file: ${documentVersion.fileName}")
        document.setCurrentPublishedVersion(documentVersion)
        document.save()
    }

    Document addNewVersion(Document document, File file, Boolean currentVersion){
        def objId = addToGridFs(document.uniqueFileId, file)
        DocumentVersion dv = new DocumentVersion([parentDocument: document, fileName: file.name, gridFsFileId: objId])
        dv.save(flush:true)
        document.addToDocumentVersions(dv)
        if(currentVersion){
            document.setCurrentPublishedVersion(dv)
        }
        log.info("Adding new version to ${document.title} with id:${document.id.toString()}, documentVersion:${dv.id.toString()} file: ${dv.fileName}")
        document.save(flush:true)
        return document
    }

    private ObjectId addToGridFs(uniqueFileId, file){
        def fis = new FileInputStream(file.getAbsolutePath())
        def objId = gridFsService.saveFile(fis, "application/pdf", uniqueFileId)
        return objId
    }

}
