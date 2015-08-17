package com.pod.registerviewer

import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec
import org.bson.types.ObjectId
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
//@TestFor(DocumentService)
class DocumentServiceSpec extends IntegrationSpec {
    @Shared
    def documentService
    def pendingDocumentService
    def gridFsService
    @Shared
    def OWNER_NAME = "userOwner"
    @Shared
    def CUSTODIAN_NAME = "userCustodian"
    @Shared
    def owner, custodian

    def setup() {
        if(pendingDocumentService.getPendingDocumentCount() < 1){
            def pd = new File(PendingDocumentServiceSpec.PENDING_SETUP_BASE_PATH)
            pd.listFiles().each{ f ->
                Path src = Paths.get(f.getAbsolutePath())
                Path tgt = Paths.get("${PendingDocumentServiceSpec.PENDING_QUEUE_BASE_PATH}/${f.name}")
                println "Copying file ${src}"
                java.nio.file.Files.copy(src, tgt, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }

    def setupSpec(){
        owner = User.findByUsername(OWNER_NAME)
        if(owner == null) {
            owner = new User(username: OWNER_NAME, password: 'password1').save()
        }
        custodian = User.findByUsername(CUSTODIAN_NAME)
        if(custodian == null) {
            custodian = new User(username: CUSTODIAN_NAME, password: 'password1').save()
        }
    }

    def cleanup() {
        pendingDocumentService.emptyPending()
    }

    def cleanupSpec(){
        def docs = Document.findAll()
        docs.each{
            documentService.deleteDocument(it.id)
        }
    }

    void "test add a document"() {
        when:
        def documentTitle = "SAMPLE_DOCUMENT_TITLE_ADD_TEST_1"
        def metaData = [stringVal:"someValue", dateVal:new Date()]
        def d = documentService.createDocument(owner: owner, custodian: custodian, title: documentTitle, file: pendingDocumentService.getNextPendingDocument(), metadata:metaData)
        then:
        def retrievedDoc = Document.findByTitle(documentTitle)
        assert retrievedDoc != null
        assert retrievedDoc.documentVersions.size() == 1
    }


    void "test add a document with multiple versions"() {
        when:
        //Add first version.
        def documentTitle = "SAMPLE_DOCUMENT_TITLE_ADD_MULTIPLE_VERSIONS_1"
        def metaData = [stringVal:"someValue", dateVal:new Date()]
        def d = documentService.createDocument(owner: owner, custodian: custodian, title: documentTitle, file: pendingDocumentService.getNextPendingDocument(), metadata:metaData)
        then:
        def retrievedDoc = Document.findByTitle(documentTitle)
        assert retrievedDoc != null
        assert retrievedDoc.documentVersions.size() == 1
        //Add another version.
        when:
        documentService.addNewVersion(retrievedDoc, pendingDocumentService.getNextPendingDocument(), true)
        documentService.addNewVersion(retrievedDoc, pendingDocumentService.getNextPendingDocument(), false)
        then:
        assert retrievedDoc.documentVersions.size() == 3
    }

    void "test get a document"(){
        when:
        def documentTitle = "SAMPLE_DOCUMENT_TITLE_FIND_TEST_2"
        def metaData = [stringVal:"someValue", dateVal:new Date()]
        def d = documentService.createDocument(owner: owner, custodian: custodian, title: documentTitle, file: pendingDocumentService.getNextPendingDocument(), metadata:metaData)
        then:
        def retrievedDoc = documentService.getDocumentById(d.id)
        assert retrievedDoc != null
        assert retrievedDoc.documentVersions.size() == 1
        assert d.id == retrievedDoc.id
    }

    void "test get a document by String id"(){
        when:
        def documentTitle = "SAMPLE_DOCUMENT_TITLE_FIND_TEST_1"
        def metaData = [stringVal:"someValue", dateVal:new Date()]
        def d = documentService.createDocument(owner: owner, custodian: custodian, title: documentTitle, file: pendingDocumentService.getNextPendingDocument(), metadata:metaData)
        then:
        def retrievedDoc = documentService.getDocumentById(d.id)
        assert retrievedDoc != null
        assert retrievedDoc.documentVersions.size() == 1
        assert d.id == retrievedDoc.id
    }

    void "test delete a document"(){
        when:
        def documentTitle = "SAMPLE_DOCUMENT_TITLE_FIND_TEST_3"
        def metaData = [stringVal:"someValue", dateVal:new Date()]
        def d = documentService.createDocument(owner: owner, custodian: custodian, title: documentTitle, file: pendingDocumentService.getNextPendingDocument(), metadata:metaData)
        then:
        d.id == documentService.getDocumentById(d.id).id
        when:
        def dv = d.documentVersions.getAt(0)
        ObjectId objectId = dv.gridFsFileId
        documentService.deleteDocument(d.id)
        then:
        def retrievedDoc = documentService.getDocumentById(d.id)
        assert retrievedDoc == null
        //Make sure the grid fs and version have done.
        assert DocumentVersion.findById(dv.id) == null
        assert gridFsService.retriveFile(objectId) == null
    }
}
