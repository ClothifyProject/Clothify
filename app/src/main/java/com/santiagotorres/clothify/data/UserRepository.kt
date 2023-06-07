package com.santiagotorres.clothify.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.santiagotorres.clothify.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private var auth: FirebaseAuth = Firebase.auth
    private var db = Firebase.firestore

    suspend fun signUpUser(email: String, password: String): ResourceRemote<String?> {

        return try {
            val result = auth.createUserWithEmailAndPassword(email,password).await()
            ResourceRemote.Success(data = result.user?.uid)
        } catch (e: FirebaseAuthException){
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            ResourceRemote.Error(message = e.localizedMessage)
        }

    }

    suspend fun signInUser(email: String, password: String): ResourceRemote<String?> {

        return try {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            ResourceRemote.Success(data = result.user?.uid)
        } catch (e: FirebaseAuthException){
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            ResourceRemote.Error(message = e.localizedMessage)
        }

    }

    suspend fun recoveryUser(email: String): ResourceRemote<String?> {

        return try {
            val result = auth.sendPasswordResetEmail(email).await()
            ResourceRemote.Success(data = email)
        } catch (e: FirebaseAuthException){
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            ResourceRemote.Error(message = e.localizedMessage)
        }

    }

    fun isSessionActive(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
    }

    fun getUIDCurrentUser () : String? {
        return auth.currentUser?.uid
    }

    suspend fun createUser(user: User): ResourceRemote<String?> {

        return try {

            user.uid?.let { db.collection("users").document(it).set(user).await() }

           // val result =auth.signInWithEmailAndPassword(email,password).await()
            ResourceRemote.Success(data = user.uid)
        } catch (e: FirebaseAuthException){
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            ResourceRemote.Error(message = e.localizedMessage)
        }

    }

    suspend fun loadUserInfo(): ResourceRemote<QuerySnapshot> {
        return try {
            val result = db.collection("users").get().await()
            ResourceRemote.Success(data = result)
        } catch (e: FirebaseAuthException){
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }

}

//prueba 4






