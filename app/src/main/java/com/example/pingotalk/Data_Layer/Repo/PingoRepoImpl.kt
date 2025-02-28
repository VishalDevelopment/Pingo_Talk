package com.example.pingotalk.Data_Layer.Repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.pingotalk.Data_Layer.Model.ChatData
import com.example.pingotalk.Data_Layer.Model.ChatUser
import com.example.pingotalk.Data_Layer.Model.Message
import com.example.pingotalk.Data_Layer.Model.Notification
import com.example.pingotalk.Data_Layer.Model.NotificationResponse
import com.example.pingotalk.Data_Layer.Model.User
import com.example.pingotalk.R
import com.example.pingotalk.Data_Layer.Retrofit.ApiServices
import com.example.pingotalk.Common.Routes.Routes
import com.example.pingotalk.Common.State
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Call
import retrofit2.Callback

@Singleton
class PingoRepoImpl @Inject constructor(
    private val pingoService : ApiServices,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStore: FirebaseFirestore,
    private val firebaseFcm: FirebaseMessaging,
) : PingoRepo {
    val startDestination = MutableStateFlow<Routes>(Routes.SiginInScreen)

    // Handle FCM Token
    override suspend fun updateToken(Newtoken:String?) {
        val currentUser = firebaseAuth.currentUser
        val token = Newtoken ?: firebaseFcm.token.await()
        if (currentUser != null) {
            if (currentUser!!.uid != null && token != null) {
                val user = User(
                    id = firebaseAuth.currentUser?.uid ?: "",
                    name = currentUser.displayName ?: "Unknown",
                    photoUrl = currentUser.photoUrl?.toString() ?: "",
                    email = currentUser.email ?: "No Email",
                    phoneNo = "",
                    subscription = "free",
                    fcm= token
                )
                firebaseStore.collection("USERS").document(currentUser.uid)
                    .set(user, SetOptions.merge()).await()
            }
        }
    }


    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()
    override suspend fun handleGoogleSignIn(context: Context, ChangeState: (Boolean) -> Unit) {
        ChangeState(true) // Set loading state before making API call

        googleSignIn(context).collect { result ->
            when (result) {
                is State.Success -> {
                    val currentUser = result.data!!.user
                    if (currentUser != null) {
                        withContext(Dispatchers.IO) {
                            val user = User(
                                id = firebaseAuth.currentUser?.uid ?: "",
                                name = currentUser.displayName ?: "Unknown",
                                photoUrl = currentUser.photoUrl?.toString() ?: "",
                                email = currentUser.email ?: "No Email",
                                phoneNo = "",
                                subscription = "free",
                                fcm = ""
                            )

                            val userCollection =
                                firebaseStore.collection("USERS").document(currentUser.uid)
                            val userExists = userCollection.get().await().exists()

                            if (userExists) {
                                userCollection.set(user, SetOptions.merge()).await()
                            } else {
                                userCollection.set(user).await()
                            }
                            updateToken(null)
                            withContext(Dispatchers.Main) {
                                com.example.pingotalk.user.value = user
                                    startDestination.emit(Routes.HomeScreen)
                                Toast.makeText(
                                    context,
                                    "Account Created Successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                ChangeState(false) // Set loading state to false after success
                            }
                        }
                    } else {
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                        ChangeState(false) // Ensure loading stops on error
                    }
                }

                is State.Error -> {
                    Toast.makeText(
                        context,
                        "Something went wrong: ${result.message}!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    ChangeState(false) // Stop loading on error
                }

                is State.Loading -> {
                    Log.d("GOOGLE", "TRUE")
                    ChangeState(true) // Ensure loading is set to true here
                }
            }
        }
    }

    override suspend fun googleSignIn(context: Context): Flow<State<AuthResult>> {

        return callbackFlow {
            trySend(State.Loading())
            try {
                val credentialManager = CredentialManager.create(context)
                //Generate Nonce for security
                val ranNonce = UUID.randomUUID().toString()
                val byte = ranNonce.toByteArray()
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(byte)
                val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.webclient))
                    .setNonce(hashedNonce)
                    .setAutoSelectEnabled(true)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                    trySend(State.Success(authResult))
                } else {
                    throw RuntimeException("Received an invalid credential type. ")
                }

            } catch (e: GetCredentialCancellationException) {
                trySend(State.Error(e.message.toString()))
            } catch (e: Exception) {
                trySend(State.Error(e.message.toString()))
            }
            awaitClose()
        }
    }

    private val _chat = MutableStateFlow<List<ChatData>>(emptyList())
    val chat = _chat.asStateFlow()
    private var chatListener: ListenerRegistration? = null
    override fun getAllChatPartners() {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId != null) {
            chatListener?.remove()

            chatListener = firebaseStore.collection("CHATS")
                .document(currentUserId)
                .collection("chats")
                .addSnapshotListener { result, error ->

                    if (error != null) {
                        Log.w("Chats", "Error getting chats for user $currentUserId", error)
                        return@addSnapshotListener
                    }

                    result?.let {
                        val chatList = it.toObjects(ChatData::class.java)

                        // Emit inside a coroutine scope
                        CoroutineScope(Dispatchers.IO).launch {
                            _chat.emit(chatList)
                        }

                        Log.d("Chats", "Found ${chatList.size} chats for user $currentUserId")
                    } ?: run {
                        Log.d("Chats", "No chats found for user $currentUserId")
                    }
                }
        }
    }

    private val _userData = MutableStateFlow<User>(User())
    val userData = _userData.asStateFlow()
    private var userDataListener: ListenerRegistration? = null
    override fun fetchUserData() {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId != null) {
            userDataListener = firebaseStore.collection("USERS")
                .document(currentUserId)
                .addSnapshotListener(MetadataChanges.INCLUDE) { document, error ->

                    document?.let {
                        val user = document.toObject(User::class.java)
                        if (document.exists() && user != null) {
                            _userData.value = user
                        } else {
                            Log.d("VM", "Document does not exist")
                        }
                    }
                    error?.let {
                        Log.d("VM", "Error fetching user data: ${error.message}")
                    }
                }
        }
    }

    override fun addChatPartner(email: String) {
        // Use Filter.or() with proper structure. Make sure the logic matches your needs.
        firebaseStore.collection("CHATS").where(
            Filter.or(
                Filter.and(
                    Filter.equalTo("user1.email", email),
                    Filter.equalTo("user2.email", userData.value.email)
                ),
                Filter.equalTo("user2.email", email),
                Filter.equalTo("user1.email", userData.value.email)
            )
        ).get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                firebaseStore.collection("USERS").whereEqualTo("email", email).get()
                    .addOnSuccessListener { userSnapshot ->

                        Log.d("FCM","Add : ${ userSnapshot.toObjects(User::class.java)}")

                        if (userSnapshot.isEmpty) {
                            Log.d("AddChat", "User does not exist in USERS Firestore")
                        } else {
                            val chatPartner = userSnapshot.toObjects(User::class.java).firstOrNull()
                            val id = firebaseStore.collection("CHATS").document()
                            Log.d("NOTIFICATION", "Chat partner: $chatPartner")
                            if (chatPartner == null) {
                                Log.d("AddChat", "Chat partner is null")
                                return@addOnSuccessListener
                            }
                            val chat = ChatData(
                                chatId = id.id,
                                last = Message(
                                    senderId = "",
                                    content = "You added ${chatPartner.name}",
                                    time = 0
                                ),
                                user1 = ChatUser(
                                    userId = userData.value.id!!,
                                    typing = false,
                                    bio = "",
                                    username = userData.value.name,
                                    ppurl = userData.value.photoUrl!!,
                                    email = userData.value.email!!,
                                ),
                                user2 = ChatUser(
                                    userId = chatPartner.id!!,
                                    typing = false,
                                    bio = "",
                                    username = chatPartner.name,
                                    ppurl = chatPartner.photoUrl!!,
                                    email = chatPartner.email!!,
                                    status = false,
                                    unread = 0,
                                    fcm =chatPartner.fcm!! // Ensure this field is non-null in Firestore
                                )
                            )
                            Log.d("FCM","user 1 : ${chat.user1.fcm} user 2 : ${chatPartner.fcm}")

                            // Write chat for current user
                            firebaseStore.collection("CHATS")
                                .document(userData.value.id!!)
                                .collection("chats")
                                .document(id.id)
                                .set(chat)

                            // Create a separate chat document for the chat partner
                            val chatForPartner = ChatData(
                                chatId = id.id,
                                last = Message(
                                    senderId = "",
                                    content = "${firebaseAuth.currentUser?.displayName} added You",
                                    time = 0
                                ),
                                user2 = ChatUser(
                                    userId = userData.value.id!!,
                                    typing = false,
                                    bio = "",
                                    username = userData.value.name,
                                    ppurl = userData.value.photoUrl!!,
                                    email = userData.value.email!!,
                                ),
                                user1 = ChatUser(
                                    userId = chatPartner.id,
                                    typing = false,
                                    bio = "",
                                    username = chatPartner.name,
                                    ppurl = chatPartner.photoUrl,
                                    email = chatPartner.email,
                                    status = false,
                                    unread = 0
                                )
                            )
                            firebaseStore.collection("CHATS")
                                .document(chatPartner.id)
                                .collection("chats")
                                .document(id.id)
                                .set(chatForPartner)
                        }
                    }
            }
        }
    }

    override fun sendMessage(chatId: ChatData, message: String) {
        val userId = firebaseAuth.currentUser!!.uid
        val messageId = EncryptedCode()
        val time = System.currentTimeMillis()

        val messageData = Message(
            msgId = messageId,
            senderId = userId,
            time = time,
            content = message
        )

        firebaseStore.collection("MESSAGE").document(chatId.chatId.toString()).collection("message")
            .document(messageId).set(messageData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        firebaseStore.collection("CHATS").document(userId).collection("chats")
                            .document(chatId.chatId.toString())
                            .update("last", messageData)
                        firebaseStore.collection("CHATS").document(chatId.user2.userId)
                            .collection("chats")
                            .document(chatId.chatId.toString())
                            .update("last", messageData)


                        val notification = Notification(
                            deviceToken = chatId.user2.fcm,
                            title = chatId.user2.username.toString(),
                            message = message
                        )
                        val call = pingoService.SendNotification(notification)
                        call.enqueue(object : Callback<NotificationResponse> {
                            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                                Log.d("RESPONSE", "Failure: ${t.message}")
                            }

                            override fun onResponse(
                                call: Call<NotificationResponse>,
                                response: retrofit2.Response<NotificationResponse>
                            ) {
                                if (response.isSuccessful && response.body() != null) {
                                    Log.d("RESPONSE", "Success: ${response.body()?.message_id}")
                                } else {
                                    Log.d("RESPONSE", "Unsuccessful response: ${response.errorBody()?.string()}")
                                }
                            }
                        })




                        Log.d("MESSAGE", "message sent : ")
                    } catch (e: Exception) {
                        Log.d("MESSAGE", "exception : ${e.message}")
                    }
                } else {
                    Log.d("MESSAGE", "message sent : ")
                }

            }
    }

    private val _individualChat = MutableStateFlow<List<Message>>(emptyList())
    val individualChat = _individualChat.asStateFlow()
    var individualChatListener: ListenerRegistration? = null
    override fun receiveMessages(chatId: String) {
        individualChatListener?.remove()
        individualChatListener = firebaseStore.collection("MESSAGE")
            .document(chatId)
            .collection("message")
            .addSnapshotListener { result, error ->
                if (error != null) {
                    Log.w("MESSAGE", "Error getting chats: ${error.message}")
                    return@addSnapshotListener
                }

                result?.let {
                    val messageList = it.toObjects(Message::class.java)
                        .sortedBy { it.time }
                    CoroutineScope(Dispatchers.IO).launch {
                        _individualChat.emit(messageList)
                    }
                    messageList.forEach { message ->
                        Log.d(
                            "MESSAGE",
                            "Message: ${message.content}, Time: ${message.timeFormatted}"
                        )
                    }

                    Log.d("MESSAGE", "Found ${messageList.size} messages for chatId: $chatId")
                } ?: run {
                    Log.d("MESSAGE", "Firestore result is null")
                }
            }
    }

}

fun EncryptedCode(): String {
    val messageId = UUID.randomUUID().toString()
    return messageId
}