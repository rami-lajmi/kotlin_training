package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){

    //observers & observable
    //creation -> observation -> emtteur depuis une autre activity -> observtion/transformation inside main view
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val NewsListFragment = NewsListFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, NewsListFragment).
                    commit()
        }
    }

    fun initializeCustomActionBar() {
        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setCustomView(R.menu.nav_menu)
    }

    //alertDialog with editText OLD
    /*fun withEditText(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("With EditText")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_add_news, null)
        val titleNews  = dialogLayout.findViewById<EditText>(R.id.newsTitleInput)
        val descNews  = dialogLayout.findViewById<EditText>(R.id.newsDescInupt)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i -> val newsSourceToAdd = Source("ramiNews01","ramiDaily"); val newsToAdd = Article(newsSourceToAdd, "Rami LAJMI", titleNews.text.toString(),"FFSQDSQDSQDQSDQDQSD","aeaeaeaea","eaeaeaea","eaeaeaea",descNews.text.toString()); articleList.add(newsToAdd); Toast.makeText(applicationContext, "News added", Toast.LENGTH_SHORT).show() }
        builder.show()
    }*/
}