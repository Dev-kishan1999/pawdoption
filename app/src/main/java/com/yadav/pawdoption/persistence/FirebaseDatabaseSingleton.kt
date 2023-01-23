package com.yadav.pawdoption.persistence

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


// Code Reference 7: https://blog.mindorks.com/how-to-create-a-singleton-class-in-kotlin
//https://firebase.google.com/docs/database/android/read-and-write
object FirebaseDatabaseSingleton {

    private val database = Firebase.database
    private val sheltersReference = database.getReference("Shelters")
    private val usersReference = database.getReference("Users")
    private val userTypeReference = database.getReference("UserType")
    private val auth: FirebaseAuth = Firebase.auth
    private var currentUser : FirebaseUser? = null
    private var mCurrentUserType = "petAdopter"
    private var mCurrentUid = ""

    fun getSheltersReference(): DatabaseReference {
        return sheltersReference
    }

    fun getUsersReference(): DatabaseReference {
        return usersReference
    }

    fun getUserTypeReference(): DatabaseReference {
        return userTypeReference
    }

    fun getCurrentUserType(): String {
        return mCurrentUserType
    }

    fun setCurrentUserType(currentUserType : String) {
        mCurrentUserType = currentUserType
    }

    fun getCurrentUid(): String {
        return mCurrentUid
    }

    fun setCurrentUid(currentUid : String) {
        mCurrentUid = currentUid
    }

    fun getCurrentUser(): FirebaseUser? {
        return currentUser
    }

    fun setCurrentUser() {
        currentUser = auth.currentUser!!
        mCurrentUid = currentUser!!.uid
    }
}