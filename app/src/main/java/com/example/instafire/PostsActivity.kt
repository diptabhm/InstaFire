package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instafire.databinding.ActivityLogInBinding
import com.example.instafire.model.User
import com.example.instafire.model.posts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

public const val EXTRA_USERNAME = "EXTRA_USERNAME"
private const val TAG = "PostsActivity"
open class PostsActivity : AppCompatActivity() {

    private var signedInUser: User?= null
    private lateinit var firestoreDb : FirebaseFirestore
    private lateinit var postsList: MutableList<posts>
    private lateinit var adapter: postsAdapter
    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        //Create the layout file which represents one post
        //Create data source
        postsList = mutableListOf()
        //Create the adapter
        adapter = postsAdapter(this,postsList)
        //Bind the adapter and the layout manager to the recycler view
        rv = findViewById(R.id.rvPosts)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        firestoreDb = FirebaseFirestore.getInstance()

        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener {userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                Log.i(TAG,"Signed is user: $signedInUser")
            }
            .addOnFailureListener{
                exeption ->
                Log.i(TAG,"Failure fetching signed in user", exeption)
            }

        var postsReference = firestoreDb
            .collection("posts")
            .limit(20)
            .orderBy("creation_time", Query.Direction.DESCENDING)

        val user = intent.getStringExtra(EXTRA_USERNAME)
        if(user != null){
            supportActionBar?.title = user
            postsReference = postsReference.whereEqualTo("user.username",user)
        }

        postsReference.addSnapshotListener{
            snapshot,exception ->

            if(exception != null || snapshot == null){
                Log.e(TAG,"Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val postListfb = snapshot.toObjects(posts::class.java)
            postsList.clear()
            postsList.addAll(postListfb)
            adapter.notifyDataSetChanged()
            for(posts in postListfb){
                Log.i(TAG,"Posts ${posts}")
            }
        }

        fab = findViewById(R.id.fabCreate)
        fab.setOnClickListener{
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_profile){
            val intent = Intent(this,ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME,signedInUser?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
