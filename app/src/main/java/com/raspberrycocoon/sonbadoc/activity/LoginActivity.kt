package com.raspberrycocoon.sonbadoc.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.raspberrycocoon.sonbadoc.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 로그인화면
 * 작성일 : 2020.12.02
 * 작성자 : 노경은
 */
class LoginActivity : AppCompatActivity() {

    private val TAG = LoginActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.e(TAG," onCreate");

        /**
         * 클릭
         */
        layout_login_google.setOnClickListener(View.OnClickListener {
            Log.e(TAG, " 구글시작하기 클릭")
        })
        layout_login_facebook.setOnClickListener(View.OnClickListener {
            Log.e(TAG, " 페이스북시작하기 클릭")
        })
        layout_login_naver.setOnClickListener(View.OnClickListener {
            Log.e(TAG, " 네이버시작하기 클릭")
        })
        layout_login_kakao.setOnClickListener(View.OnClickListener {
            Log.e(TAG, " 카카오시작하기 클릭")
        })
        layout_login_phonenum.setOnClickListener(View.OnClickListener {
            Log.e(TAG, " 핸드폰시작하기 클릭")
            intent_next(this)

        })
    }

    companion object {
        private fun intent_next(activity: AppCompatActivity) {
            Log.e(activity.toString()," intent_next");
            val intent = Intent(activity, JoinActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout); //화면전환 애니메이션
        }
    }
}