package com.intergalacticpenguin.raspboard

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_article.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {

    val newsService by lazy {
        NewsService.create()
    }
    val metrics = DisplayMetrics()
    val dateFormat = SimpleDateFormat("dd. MM. yyyy hh:mm:ss", Locale.getDefault())
    val debugTextList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        world_news_recycler.layoutManager = AutofitGridLayoutManager(this, 340)
        tech_news_recycler.layoutManager = AutofitGridLayoutManager(this, 340)

        setupDebugText()
        getNews()
    }

    private fun getNews() { //TODO check periodically
        newsService.getNews("bbc-news", "top", API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> world_news_recycler.adapter = ArticleAdapter(this, result.articles) },
                        { error -> Log.e("Error", error.message) }
                )
        newsService.getNews("engadget", "latest", API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> tech_news_recycler.adapter = ArticleAdapter(this, result.articles) },
                        { error -> Log.e("Error", error.message) }
                )
    }

    override fun onPause() {
        super.onPause()
        //disposable?.dispose() TODO should do with all disposables
    }

    fun setupDebugText() {
        // display resolution
        windowManager.defaultDisplay.getMetrics(metrics)
        debugTextList.add(metrics.widthPixels.toString() + "x" + metrics.heightPixels.toString())

        // display ip
        debugTextList.add(getDeviceIp(this))

        // run observable for updating debug text
        Observable
                .interval(1L, TimeUnit.SECONDS)
                .timeInterval()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updateDebugText(debugTextList) })
    }

    fun updateDebugText(debugTextList: ArrayList<String>) {
        var debugText = StringBuilder()
        debugText.append(dateFormat.format(Date()))
        for (string: String in debugTextList) {
            debugText.append(" " + string)
        }
        debug_text.setText(debugText.toString())
    }

    fun showCard(title: String, content: String) {
        card_title.setText(title)
        card_content.setText(content)
    }
}
