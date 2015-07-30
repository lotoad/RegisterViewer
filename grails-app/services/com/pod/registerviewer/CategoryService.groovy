package com.pod.registerviewer

import grails.transaction.Transactional

@Transactional
class CategoryService {

    def springSecurityService

    /**
    Category findCategoryByName(){

    }

    Category findCategoryById(){

    }
    **/

    Category createNewCategory(String name){
        def category = new Category(name:name, createdBy: springSecurityService.currentUser, createdAt: new Date(), modifiedBy: springSecurityService.currentUser, modifiedAt: new Date())
        category.save(flush:true)
    }

    Category deleteCategory(Category category){
        //TODO Check for any documents that are still in the category?
        category.delete(flush: true)
    }

    Category addChild(Category parent, Category child){
        parent.addToChildren(child)
        parent.save(flush:true)
    }

    Category removeChild(Category parent, Category child){
        parent.removeFromChildren(child)
        parent.save(flush:true)
    }

    Category addDocumentToCategory(Document document, Category category){
        category.addToDocuments(document)
        category.save(flush:true)
    }

    Category removeDocumentFromCategory(Category category, Document document){
        category.removeFromDocuments(document)
        category.save(flush:true)
    }
}
