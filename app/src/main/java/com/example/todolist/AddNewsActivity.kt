package com.example.todolist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


class AddNewsActivity(private val artObj:PublishSubject<Article>) : AppCompatActivity() {

    private lateinit var articleList: ArrayList<Article>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_news)

        val btnToValidateNews: View = findViewById(R.id.btnToValidateNews)
        btnToValidateNews.setOnClickListener { view ->
            //Insert News in list and return to main activity
            //data inside
            //artObj.onNext()
            onBackPressed()
        }

        //title
        //content

        //articleList = intent.getStringExtra("articleList")
        //articleList = intent.getSerializableExtra("articleList") as ArrayList<Article>

        //val newsTitleInput  = findViewById<EditText>(R.id.newsTitleInput)
        //val newsDescInput  = findViewById<EditText>(R.id.newsDescInupt)

        //val newsSourceToAdd = Source("ramiNews01","ramiDaily"); val newsToAdd = Article(newsSourceToAdd, "Rami LAJMI", newsTitleInput.text.toString(),"FFSQDSQDSQDQSDQDQSD","aeaeaeaea","eaeaeaea","eaeaeaea",newsDescInput.text.toString()); articleList.add(newsToAdd);
    }

    /*private fun createArticleObserver(): DisposableObserver<Article> {
        return object : DisposableObserver<Article>() {
            override fun onNext(article: Article) {
                if (!articleList.contains(article)) {
                    articleList.add(article)
                }
            }

            override fun onComplete() {
                //not yet
            }

            override fun onError(e: Throwable) {
                Log.e("createArticleObserver", "Article error: ${e.message}")
            }
        }
    }*/
}