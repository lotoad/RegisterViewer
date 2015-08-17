package com.pod.registerviewer

import grails.test.spock.IntegrationSpec

import java.nio.file.*
import spock.lang.*

/**
 *
 */
class PendingDocumentServiceSpec extends IntegrationSpec {

    def pendingDocumentService
    static def PENDING_SETUP_BASE_PATH = "test/resources/pending_test_setup_files"
    static def PENDING_QUEUE_BASE_PATH = "test/resources/pending/"


    def setup() {
        //Copy a set of files form root resources to pending dir.
        def pd = new File(PENDING_SETUP_BASE_PATH)
        pd.listFiles().each{ f ->
            Path src = Paths.get(f.getAbsolutePath())
            Path tgt = Paths.get("${PENDING_QUEUE_BASE_PATH}/${f.name}")
            println "Copying file ${src}"
            java.nio.file.Files.copy(src, tgt, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    def cleanup() {
        def pd = new File(PENDING_QUEUE_BASE_PATH)
        pd.listFiles().each { f ->
            f.delete()
        }
    }

    void "test list documents in queue"(){
        when:
            def docs = pendingDocumentService.listPendingFiles()
        then:
            docs.size() == new File(PENDING_SETUP_BASE_PATH).listFiles().size()
    }

    void "test add documents to queue"(){
        when:
            //Get a sample doc from the pending dir and add it!
            def f = new File("${PENDING_SETUP_BASE_PATH}/SampleScannedDoc.pdf")
            pendingDocumentService.addFileToPending(f)
        then:
            //Make sure there are five docs!
            pendingDocumentService.listPendingFiles().size() == 5
    }

    void "test add duplicate documents to queue"(){
        when:
            def f = new File("${PENDING_SETUP_BASE_PATH}/SampleScannedDoc.pdf")
            pendingDocumentService.addFileToPending(f)
            pendingDocumentService.addFileToPending(f)
            pendingDocumentService.addFileToPending(f)
        then:
            pendingDocumentService.pendingDocumentCount == 7
        when:
            def files = pendingDocumentService.listPendingFiles()
        then:
            true == files*.name.contains("SampleScannedDoc_3.pdf")
    }

    void "test clear queue"(){
        when:
            pendingDocumentService.getPendingDocumentCount() == 4
            pendingDocumentService.emptyPending()
        then:
            pendingDocumentService.pendingDocumentCount == 0

    }

    /*
    void "test remove documents from queue"(){
        when:
        then:

    }

    void "test get count of documents in queue"(){}
*/

}
