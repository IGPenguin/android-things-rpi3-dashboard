package com.intergalacticpenguin.raspboard

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_article.view.*

/**
 * Created by eidamsvoboda on 06/10/2017.
 */
class ArticleAdapter(var context: Context, var articleList: ArrayList<Article>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val v = LayoutInflater.from(context).inflate(R.layout.card_article, parent, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bind(articleList[position])
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: Article) {
            Picasso
                    .with(itemView.context)
                    .load(_list.urlToImage)
                    .resize(256,256)
                    .centerCrop()
                    .into(itemView.card_image)
            itemView.card_title.text = _list.title
            itemView.card_content.text = _list.description
        }
    }
}
