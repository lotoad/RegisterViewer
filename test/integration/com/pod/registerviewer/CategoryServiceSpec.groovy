package com.pod.registerviewer

import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec
import spock.lang.Shared
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
//@TestFor(CategoryService)
class CategoryServiceSpec extends IntegrationSpec {

    @Shared
    def categoryService
    def documentService
    @Shared
    def USER_NAME = "userCatTest"
    @Shared
    def user1

    def setup() {

        def loggedInUser = user1
        categoryService.springSecurityService = [
                encodePassword: 'password',
                reauthenticate: { String u -> true},
                loggedIn: true,
                getCurrentUser: { loggedInUser },
                currentUser: loggedInUser]

        def category1 = new Category(name:'category1', createdBy: user1, createdAt: new Date(), modifiedBy: user1, modifiedAt: new Date())
        category1.save(flush:true)

        def category2 = new Category(name:'category2', createdBy: user1, createdAt: new Date(), modifiedBy: user1, modifiedAt: new Date())
        category2.save(flush:true)

        def category3 = new Category(name:'category3', createdBy: user1, createdAt: new Date(), modifiedBy: user1, modifiedAt: new Date())
        category3.save(flush:true)

        def category4 = new Category(name:'category4', createdBy: user1, createdAt: new Date(), modifiedBy: user1, modifiedAt: new Date())
        category4.save(flush:true)

        def category5 = new Category(name:'category5', createdBy: user1, createdAt: new Date(), modifiedBy: user1, modifiedAt: new Date())
        category5.save(flush:true)

        def category6 = new Category(name:'category6', createdBy: user1, createdAt: new Date(), modifiedBy: user1, modifiedAt: new Date())
        category6.save(flush:true)

        category3.addToChildren(category5)
        //category3.children.add(category5)
        category5.addToChildren(category6)
        category2.addToChildren(category3)
        category3.addToChildren(category1)

        category3.save(flush:true)
        category5.save(flush:true)
        category2.save(flush:true)
    }

    def setupSpec(){
        user1 = User.findByUsername(USER_NAME)
        if(user1 == null) {
            user1 = new User(username: USER_NAME, password: 'password1').save()
        }
    }

    def cleanup() {
        def categories = Category.findAll()
        categories.each {
            it.delete(flush:true)
        }
        def user = User.findByUsername(USER_NAME)
        user.delete(flush:true)
    }

    def cleanupSpec(){
        //Delete all categories!
    }

    void "test add a category"(){
        when:
        def c = categoryService.createNewCategory("Test Category 1")
        then:
        c != null
    }


    void "test delete category with documents"(){
        /*
        when:
        def c = categoryService.createNewCategory("Test Category 1")
        then:
        c != null
        when:

        c = categoryService.addDocumentToCategory(,c)
        then:
        */
    }

    void "test delete category with sub categories"(){

    }

    void "test get a category by name"() {

    }

    void "test get a category by id"(){

    }

    void "test get a categories children"(){

    }

    void "test get a categories parent"() {

    }
}
