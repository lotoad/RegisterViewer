package com.pod.registerviewer.com.pod.registerviewer
import com.pod.registerviewer.Category
class CategoryController {

    def index() {
        def categories = Category.findAll()
        println categories.size()
        [categories:categories]
    }
}
