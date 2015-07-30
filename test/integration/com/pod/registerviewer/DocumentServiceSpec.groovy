package com.pod.registerviewer

import grails.test.mixin.TestFor
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DocumentService)
class DocumentServiceSpec extends Specification {

    def documentService
    def pendingDocumentService
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
    }

    void "test add a document"() {
        when:
        def documentTitle = "SAMPLE_DOCUMENT_TITLE_ADD_TEST"
        def metaData = [stringVal:"someValue", dateVal:new Date()]
        def document = documentService.createDocument(owner: owner, custodian: custodian, title: documentTitle, file: pendingDocumentService.getNextPendingDocument(), metadata:metaData)
        then:
        Document.findByTitle(documentTitle) != null

        /*
        d = new Document(owner: user1, custodian: user2, title: "Document ${i+1}", generatedFilename: randomFileName)
        d.save(flush:true)

        DocumentVersion dv = new DocumentVersion(parentDocument: d, fileName: file.getName(), gridFsFileId: objId)
        dv.save(flush:true)

        d.currentPublishedVersion = dv
        d.addToDocumentVersions(dv)

        d.save(flush:true)
        */
    }

    void "test get a document"(){

    }

    void "test delete a document"(){

    }
}
