package com.pod.registerviewer

import org.bson.types.ObjectId

class Category implements Serializable{

    static mapWith = "mongo"

    //static mapping = {}
    static hasMany = [children:Category, documents:Document]

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

    //Embedding docs doesn't appear to work.
    //List children
    //List documents
    //static embedded = ['children', 'documents']


}
