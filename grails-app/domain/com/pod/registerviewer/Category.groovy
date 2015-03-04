package com.pod.registerviewer

import org.bson.types.ObjectId

class Category {

    static mapWith = "mongo"

    //static mapping = {}

    static constraints = {
        parent nullable: true
        children nullable: true
        documents nullable: true
        parent nullable: true
    }

    ObjectId id
    String name
    User createdBy
    Date createdAt
    User modifiedBy
    Date modifiedAt
    Category parent
    List children
    List documents

    static embedded = ['children', 'documents']


}
