package com.example.todolist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_news_list.*
import kotlinx.android.synthetic.main.fragment_news_list.empty_text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsListFragment() : Fragment() {

    private val ENDPOINT_URL by lazy { "https://newsapi.org/v2/" }
    private lateinit var topHeadlinesEndpoint: TopHeadlinesEndpoint
    private lateinit var newsApiConfig: String
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: ArrayList<Article>
    private lateinit var userKeyWordInput: String
    private lateinit var topHeadlinesObservable: Observable<TopHeadlines>
    private lateinit var compositeDisposable: CompositeDisposable
    private var artObj = PublishSubject.create<Article>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val actionBar: ActionBar? = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setCustomView(R.menu.nav_menu)
        //context.initializeCustomActionBar()
        return inflater.inflate(R.layout.fragment_news_list, container, false)

    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //val Bundle
        val AddNewsFragment = AddNewsFragment(artObj)



        //Network request
        newsApiConfig = resources.getString(R.string.api_key)


        val retrofit: Retrofit = generateRetrofitBuilder()
        topHeadlinesEndpoint = retrofit.create(TopHeadlinesEndpoint::class.java)
        articleList = ArrayList()
        userKeyWordInput = ""
        articleAdapter = ArticleAdapter(articleList)

        //val ramiVarSource = Source("ramiNews01","ramiDaily")
        //val ramiVarContent = Article(ramiVarSource, "Rami LAJMI", "TTTTTTTT","FFSQDSQDSQDQSDQDQSD","aeaeaeaea","eaeaeaea","eaeaeaea","azezaezaeazezaezaezaezaezaeazezaezaezaeaze")

        //CompositeDisposable is needed to avoid memory leaks
        compositeDisposable = CompositeDisposable()
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = articleAdapter
        Toast.makeText(getActivity(),"Bienvenue",Toast.LENGTH_SHORT).show();

        swipe_refresh.setOnRefreshListener {
            Toast.makeText(getActivity(),"Actualisation",Toast.LENGTH_SHORT).show();
            checkUserKeywordInput()
        }


        SwipeRefreshLayout.OnRefreshListener{
            checkUserKeywordInput()
            //compositeDisposable = CompositeDisposable()
            recycler_view.setHasFixedSize(true)
            recycler_view.layoutManager = LinearLayoutManager(context)
            recycler_view.itemAnimator = DefaultItemAnimator()
            recycler_view.adapter = articleAdapter
            swipe_refresh.isRefreshing = false
        }



            //(ecoute avec subscribe)
            compositeDisposable.add(artObj.subscribe{
                articleList.add(it)
            })

            val fab: View = view.findViewById(R.id.fab)
            fab.setOnClickListener { view ->
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, AddNewsFragment).commit()
                }
            }
    }


        override fun onStart() {
            super.onStart()
            checkUserKeywordInput()
        }

        override fun onDestroy() {
            super.onDestroy()
            compositeDisposable.clear()
        }

        fun onRefresh() {
            checkUserKeywordInput()
        }

        /*fun SwipeRefreshLayout.setOnRefreshListener() {
            checkUserKeywordInput()
        }*/

        private fun checkUserKeywordInput() {
            if (userKeyWordInput.isEmpty()) {
                queryTopHeadlines()
            } else {
                getKeyWordQuery(userKeyWordInput)
            }
        }

        private fun getKeyWordQuery(userKeywordInput: String) {
            swipe_refresh.isRefreshing = true
            if (userKeywordInput != null && userKeywordInput.isNotEmpty()) {
                topHeadlinesObservable = topHeadlinesEndpoint.getUserSearchInput(newsApiConfig, userKeywordInput)
                subscribeObservableOfArticle()
            } else {
                queryTopHeadlines()
            }
        }

        private fun queryTopHeadlines() {
            swipe_refresh.isRefreshing = true
            topHeadlinesObservable = topHeadlinesEndpoint.getTopHeadlines("us", newsApiConfig)
            subscribeObservableOfArticle()
        }

        private fun subscribeObservableOfArticle() {
            articleList.clear()
            compositeDisposable.add(
                    topHeadlinesObservable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .flatMap {
                                Observable.fromIterable(it.articles)
                            }
                            .subscribeWith(createArticleObserver())
            )
        }

        private fun createArticleObserver(): DisposableObserver<Article> {
            return object : DisposableObserver<Article>() {
                override fun onNext(article: Article) {
                    if (!articleList.contains(article)) {
                        articleList.add(article)
                    }
                }

                override fun onComplete() {
                    showArticlesOnRecyclerView()
                }

                override fun onError(e: Throwable) {
                    Log.e("createArticleObserver", "Article error: ${e.message}")
                }
            }
        }

        private fun showArticlesOnRecyclerView() {
            if (articleList.size > 0) {
                //val ramiVarSource = Source("ramiNews01","ramiDaily")
                //val ramiVarContent = Article(ramiVarSource, "Rami LAJMI", "TTTTTTTTTTTTT","FFSQDSQDSQDQSDQDQSD","aeaeaeaea","https://i.picsum.photos/id/237/200/300.jpg","eaeaeaea","azezaezaeazezaezaezaezaezaeazezaezaezaeaze")
                if(arguments?.getSerializable("testData") != null){
                    val testData = requireArguments().getSerializable("testData")
                    articleList.add(testData as Article)
                }

                empty_text.visibility = View.GONE
                retry_fetch_button.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE
                articleAdapter.setArticles(articleList)
            } else {
                recycler_view.visibility = View.GONE
                empty_text.visibility = View.VISIBLE
                retry_fetch_button.visibility = View.VISIBLE
                retry_fetch_button.setOnClickListener { checkUserKeywordInput() }
            }
            swipe_refresh.isRefreshing = false
        }

        private fun generateRetrofitBuilder(): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(ENDPOINT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
}


