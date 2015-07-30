package com.pod.registerviewer

import grails.transaction.Transactional
import org.springframework.beans.factory.InitializingBean

import java.nio.file.Path
import java.nio.file.Paths


@Transactional
class PendingDocumentService implements InitializingBean{

    def pendingDirectoryPath
    def grailsApplication
    Integer MAX_DUPLICATE_PENDING_FILES = 10000

    //From InitializingBean
    void afterPropertiesSet() {
        this.pendingDirectoryPath = grailsApplication.config.registerviewer.queue.path
    }

    /**
     * Get the next pending document.
     */
    File getNextPendingDocument(){
        def dir = new File(pendingDirectoryPath)
        def files = dir.listFiles([accept:{file-> file ==~ /.*?\.pdf/ }] as FileFilter)
        List<File> sortedFiles = files.sort {it.lastModified()}
        return sortedFiles.first()
    }


    /**
     * List all files in the pending directory
     * @return
     */
    List<File> listPendingFiles(){
        def dir = new File(pendingDirectoryPath)
        def files = dir.listFiles([accept:{file-> file ==~ /.*?\.pdf/ }] as FileFilter)
        return files.sort {it.lastModified()}
    }

    /**
     * Return a count of pending docs.
     * @return
     */
    Integer getPendingDocumentCount(){
        def dir = new File(pendingDirectoryPath)
        return dir.listFiles([accept:{file-> file ==~ /.*?\.pdf/ }] as FileFilter).size()
    }

    /**
     * Add a file to the pending directory
     * @param file
     * @returns the file in pending that has been created.
     */
    def addFileToPending(File file){
        //Check if it already exists
        def fileName = file.name.substring(0,file.name.lastIndexOf('.'))//Just the name
        def fileExtension = file.name.substring(file.name.lastIndexOf('.')+1, file.name.length())//just the extension (if it has one?)
        def uniqueKey = 1
        def checkFilePath = "${pendingDirectoryPath}/${fileName}.${fileExtension}"
        def checkFile = new File(checkFilePath)
        while(checkFile.exists()){
            if(uniqueKey > MAX_DUPLICATE_PENDING_FILES){
                throw new Exception("Too many duplicate files in pending directory. Exceeded ${MAX_DUPLICATE_PENDING_FILES}.")
            }
            def newFileName = fileName + "_" + uniqueKey
            checkFilePath = "${pendingDirectoryPath}/${newFileName}.${fileExtension}"
            checkFile = new File(checkFilePath)
            uniqueKey++
        }
        Path src = Paths.get(file.getAbsolutePath())
        Path tgt = Paths.get(checkFile.getAbsolutePath())
        java.nio.file.Files.copy(src, tgt)
        return checkFile
    }

    /**
     * DeleteAll files fom the pending directory
     */
    def emptyPending(){
        def dir = new File(pendingDirectoryPath)
        dir.listFiles().each{ f ->
            f.delete()
        }
    }

    /**
     *
     * @param fileName
     */
    def deleteFileFromPending(String fileName){
        def f = new File(${pendingDirectoryPath}/fileName)
        f.delete()
    }

}
