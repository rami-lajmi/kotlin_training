package com.example.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_add_news.*

class AddNewsFragment(artObj: PublishSubject<Article>) : Fragment(){
    var ramiVar = artObj
    var ramiVarContent = null
    var checkFormF = false
    lateinit var ramiVarSource: Source

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val NewsListFragment = NewsListFragment()

        val btnToValidateFNews_: Button = view.findViewById(R.id.btnToValidateFNews)

        val btnToValidateFNews: View = view.findViewById(R.id.btnToValidateFNews)
        btnToValidateFNews.setOnClickListener { view ->
            //if form is ok
            if(checkFormF){
                var ramiVarSource = Source("ramiNews01","ramiDaily")
                var ramiVarContent = Article(ramiVarSource, "Rami LAJMI", fnewsTitleInput.text.toString(),"FFSQDSQDSQDQSDQDQSD","aeaeaeaea","eaeaeaea","eaeaeaea",fnewsDescInupt.text.toString())
                ramiVar.onNext(ramiVarContent)
                val bundle = Bundle()
                bundle.putSerializable("testData",ramiVarContent)
                NewsListFragment.arguments = bundle
            }

            //ramiVar.publish(ramiVarContent)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, NewsListFragment)
                addToBackStack(null)
                commit()
            }
        }


        val closeAddNewsFBtn: View = view.findViewById(R.id.closeAddNewsFBtn)
        closeAddNewsFBtn.setOnClickListener { view ->
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, NewsListFragment)
                addToBackStack(null)
                commit()
            }
        }

        fnewsDescInupt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(fnewsDescInupt.length()>0 && fnewsTitleInput.length()>0){
                    btnToValidateFNews_.text = "Enregistrer et valider"
                    checkFormF = true
                }else{
                    btnToValidateFNews_.text = "Retour au menu principal"
                    checkFormF = false;
                }
            }
        })

        }

    /*fun changeMyBtnState(button: Button, vFormF: Boolean) {
        //disable btn
        //button?.isEnabled = false
        if(vFormF){
            button?.setText("Enregistrer et valider")
        }else{
            button?.setText("Retour au menu principal")
        }
    }*/
}