import com.pod.mongofile.GridFsService
import com.pod.registerviewer.Category
import com.pod.registerviewer.Document
import com.pod.registerviewer.DocumentVersion
import com.pod.registerviewer.Role
import com.pod.registerviewer.User
import com.pod.registerviewer.UserRole
import static java.util.UUID.randomUUID

class BootStrap {

    def init = { servletContext ->


        /**
         * We'll create some sample instances of the classes and relationships so that we can play around.
         **/
        Role userRole = new Role()
        userRole.authority = "USER_ROLE"
        userRole.save(flush:true)
        Role adminRole = new Role()
        adminRole.authority = "ADMIN_ROLE"
        adminRole.save(flush:true)

        /**
         * Create some sample users and roles.
         */
        def user1 = new User(username: 'user1', password: 'password1')
        user1.save(flush: true)
        def user2 = new User(username: 'user2', password: 'password2')
        user2.save(flush: true)
        def user3 = new User(username: 'user3', password: 'password3')
        user3.save(flush: true)
        def user4 = new User(username: 'user4', password: 'password4')
        user4.save(flush: true)

        UserRole ur1 = new UserRole(user: user1, role: userRole)
        ur1.save(flush: true)

        UserRole ur2 = new UserRole(user: user2, role: userRole)
        ur2.save(flush: true)

        UserRole ur3 = new UserRole(user: user3, role: userRole)
        ur3.save(flush: true)

        UserRole ur4 = new UserRole(user: user4, role: userRole)
        ur4.save(flush: true)

        UserRole ur5 = new UserRole(user: user4, role: adminRole)
        ur5.save(flush: true)

        /**
         * Create some sample categories
         */

        def category1 = new Category(name:'category1', createdBy: user1, createdAt: new Date(), modifiedBy: user1, modifiedAt: new Date())
        category1.save(flush:true)

        def category2 = new Category(name:'category2', createdBy: user2, createdAt: new Date(), modifiedBy: user2, modifiedAt: new Date())
        category2.save(flush:true)

        def category3 = new Category(name:'category3', createdBy: user3, createdAt: new Date(), modifiedBy: user3, modifiedAt: new Date())
        category3.save(flush:true)

        def category4 = new Category(name:'category4', createdBy: user4, createdAt: new Date(), modifiedBy: user4, modifiedAt: new Date())
        category4.save(flush:true)

        def category5 = new Category(name:'category5', createdBy: user1, createdAt: new Date(), modifiedBy: user1, modifiedAt: new Date())
        category5.save(flush:true)

        def category6 = new Category(name:'category6', createdBy: user2, createdAt: new Date(), modifiedBy: user2, modifiedAt: new Date())
        category6.save(flush:true)

        category3.addToChildren(category5)
        category5.addToChildren(category6)
        category2.addToChildren(category3)
        category3.addToChildren(category1)

        category3.save(flush:true)
        category5.save(flush:true)
        category2.save(flush:true)


        println gridFsService
        println pendingDocumentService

        /**
         * Create some sample documents and versions to go with them using the files in
         * test/resources/pending_test_setup_files
         */
        def PENDING_SETUP_BASE_PATH = "test/resources/pending_test_setup_files"
        def files = new File(PENDING_SETUP_BASE_PATH).listFiles()
        def d
        files.eachWithIndex {  file, i ->
            d = new Document(owner: user1, custodian: user2, title: "Document ${i+1}", generatedFilename: randomUUID().toString().toUpperCase())
            d.save(flush:true)
            def fis = new FileInputStream(file.getAbsolutePath())
            def objId = gridFsService.saveFile(fis, "application/pdf", d.generatedFilename)
            DocumentVersion dv = new DocumentVersion(parentDocument: d, fileName: file.getName(), gridFsFileId: objId)
            dv.save(flush:true)
        }



        /*
        def c = Category.findByName('category3')
        println c.id
        c.children.each{
            println it.name
        }
        assert c != null
        */


    }
    def pendingDocumentService

    GridFsService gridFsService
    def destroy = {


    }
}
