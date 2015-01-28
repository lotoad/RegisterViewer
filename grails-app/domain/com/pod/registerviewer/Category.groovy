package com.pod.registerviewer

class Category {

    static constraints = {
        parent nullable: true
    }

    Category parent
    List<Category> children = new ArrayList<Category>()
    List<Document> documents = new ArrayList<Document>()

}
