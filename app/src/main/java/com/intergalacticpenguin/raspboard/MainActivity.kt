package com.intergalacticpenguin.raspboard

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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
    var disposable: Disposable? = null
    val dateFormat = SimpleDateFormat("dd. MM. yyyy hh:mm:ss")
    val debugTextList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // display resolution
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        debugTextList.add(metrics.widthPixels.toString()+"x"+metrics.heightPixels.toString())

        setupDebugText()
        getNews()
    }

    private fun getNews(): Disposable {
        return newsService.getNews("bbc-news", "top", API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> showCard(result.articles.get(0).title, result.articles.get(0).description) },
                        { error -> Log.e("Error", error.message) }
                )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun setupDebugText() {
        Observable
                .interval(1L, TimeUnit.SECONDS)
                .timeInterval()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updateDebugText(debugTextList) })
    }

    fun updateDebugText(debugTextList: List<String>) {
        var debugText = StringBuilder()
        debugText.append(dateFormat.format(Date()))
        for (string: String in debugTextList) {
            debugText.append(" "+string)
        }
        debug_text.setText(debugText.toString())
    }

    fun showCard(title: String, content: String) {
        card_title.setText(title)
        card_content.setText(content)
    }
}
