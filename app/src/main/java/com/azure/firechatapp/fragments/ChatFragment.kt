package com.azure.firechatapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.azure.firechatapp.R
import com.azure.firechatapp.adapters.ChatAdapter
import com.azure.firechatapp.models.Message
import com.azure.firechatapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import java.util.EventListener

class ChatFragment : Fragment() {

    private lateinit var _view: View
    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val COLLECTION_NAME = "chat"

    private var chatSubscription: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpChatSendBtn()

        subscribeToChatMessages()

        return _view
    }

    private fun setUpChatDB() {
        chatDBRef = store.collection(COLLECTION_NAME)
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }


    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList, currentUser.uid)

        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.layoutManager = layoutManager
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.adapter = adapter
    }

    private fun setUpChatSendBtn() {
        _view.buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString()

            if(messageText.isNotEmpty()){
                val message = Message(currentUser.uid, messageText, currentUser.photoUrl.toString(), Date())


                _view.editTextMessage.setText("")
            }
        }
    }

    private fun saveMessage(message: Message){
        val newMessage = HashMap<String,Any>()
        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageURL"] = message.profileImageURL
        newMessage["sendAt"] = message.sendAt

        chatDBRef.add(newMessage)
            .addOnCompleteListener {
                activity!!.toast("Message added!")
            }.addOnFailureListener {
                activity!!.toast("Message error, try again")
            }
    }

    private fun subscribeToChatMessages(){
        chatSubscription = chatDBRef
            .orderBy("sentAt", Query.Direction.DESCENDING)
            .limit(5)
            .addSnapshotListener(object: EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                exception?.let {
                    activity!!.toast("Exception")
                    return
                }

                snapshot?.let{
                    messageList.clear()
                    val messages = it.toObjects(Message::class.java)
                    messageList.addAll(messages.asReversed())
                    adapter.notifyDataSetChanged()
                    _view.recyclerView.smoothScrollToPosition(messageList.size)
                }
            }
        })
    }

    override fun onDestroy() {
        chatSubscription?.remove()
        super.onDestroy()
    }


}
