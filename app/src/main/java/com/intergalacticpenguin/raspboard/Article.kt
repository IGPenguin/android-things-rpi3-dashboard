package com.intergalacticpenguin.raspboard

/**
 * Created by eidamsvoboda on 05/10/2017.
 */

data class Article(
        val author: String,
        val title: String,
        val description: String,
        val url: String,
        val urlToImage: String,
        val publishedAt: String
)

data class ResultArticle (
        val status:String,
        val source:String,
        val sortBy:String,
        val articles:ArrayList<Article>
)
