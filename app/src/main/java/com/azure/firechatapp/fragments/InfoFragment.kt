package com.azure.firechatapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.azure.firechatapp.R
import com.azure.firechatapp.utils.CircleTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info.view.*


class InfoFragment : Fragment() {

    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null

    // TODO: Create a file with constants
    private val COLLECTION_NAME = "chat"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_info, container, false)

        setupChatDB()
        setUpCurrentUser()
        setUpCurrentUserInfoUI()

        return _view
    }

    private fun setupChatDB(){
        chatDBRef = store.collection(COLLECTION_NAME)
    }

    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI(){
        _view.textViewInfoEmail.text = currentUser.email
        _view.textViewInfoName.text = currentUser.displayName?.let { currentUser.displayName } ?: run { getString(R.string.info_no_name) }
        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl).resize(100,100)
                .centerCrop().transform(CircleTransform()).into(_view.imageViewInfoAvatar)
        } ?: run {
            Picasso.get().load(R.drawable.ic_person_black_24dp).resize(300,300)
                .centerCrop().transform(CircleTransform()).into(_view.imageViewInfoAvatar)
        }
    }
}
