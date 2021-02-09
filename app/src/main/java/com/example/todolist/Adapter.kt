package com.example.todolist

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_article.view.*


class ArticleAdapter(
    private var articleList: ArrayList<Article>
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private val placeHolderImage = "https://pbs.twimg.com/profile_images/467502291415617536/SP8_ylk9.png"

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ArticleViewHolder {
        val itemView: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_article, viewGroup, false)
        return ArticleViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(articleViewHolder: ArticleViewHolder, itemIndex: Int) {
        val article: Article = articleList.get(itemIndex)
        var artObj = PublishSubject.create<Article>()
        val url = "https://stackoverflow.com/"
        lateinit var webView: WebView

        setPropertiesForArticleViewHolder(articleViewHolder, article)

        articleViewHolder.cardView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity
                val btnLike = activity.findViewById<View>(R.id.likeBtnNews)
                btnLike.setBackgroundResource(R.drawable.likered)
                btnLike.invalidate()
                val AddNewsFragment = AddNewsFragment(artObj)
                val url = article.url
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)

                activity.startActivity(intent)

                /*activity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, AddNewsFragment).commit()
                }*/
            }
        })


    }

    private fun setPropertiesForArticleViewHolder(articleViewHolder: ArticleViewHolder, article: Article) {
        checkForUrlToImage(article, articleViewHolder)
        articleViewHolder.title.text = article?.title
        articleViewHolder.description.text = article?.description
    }

    private fun checkForUrlToImage(article: Article, articleViewHolder: ArticleViewHolder) {
        if (article.urlToImage == null || article.urlToImage.isEmpty()) {
            Picasso.get()
                .load(placeHolderImage)
                .centerCrop()
                .fit()
                .into(articleViewHolder.urlToImage)
        } else {
            Picasso.get()
                .load(article.urlToImage)
                .centerCrop()
                .fit()
                .into(articleViewHolder.urlToImage)
        }
    }

    fun setArticles(articles: ArrayList<Article>) {
        articleList = articles
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView by lazy {view.card_view}
        val urlToImage: ImageView by lazy { view.article_urlToImage }
        val title: TextView by lazy { view.article_title }
        val description: TextView by lazy { view.article_description }
    }
}