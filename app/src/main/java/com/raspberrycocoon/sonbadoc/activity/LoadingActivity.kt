package com.raspberrycocoon.sonbadoc.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.raspberrycocoon.sonbadoc.R
import kotlinx.android.synthetic.main.activity_loading.*

/**
 * 로딩화면
 * 작성일 : 2020.12.02
 * 작성자 : 노경은
 */
class LoadingActivity : AppCompatActivity() {

    private val TAG = LoadingActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        Log.e(TAG," onCreate");

        startLoading() //로딩메서드
    }

    //로딩메서드
    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            Log.e(TAG," startLoading 로딩메서드 ");

            finish()
            intent_next(this)
        }, 2000) //동작타임 1000 : 1초
    }

    companion object {
        private fun intent_next(activity: AppCompatActivity) {
            Log.e(activity.toString()," intent_next");
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout); //화면전환 애니메이션
        }
    }
}