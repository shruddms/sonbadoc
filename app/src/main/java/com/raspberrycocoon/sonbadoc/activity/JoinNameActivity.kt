package com.raspberrycocoon.sonbadoc.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.raspberrycocoon.sonbadoc.R
import kotlinx.android.synthetic.main.activity_join_name.*
/**
 * 회원가입 후 이름입력화면
 * 작성일 : 2020.12.02
 * 작성자 : 노경은
 */
class JoinNameActivity : AppCompatActivity() {

    private val TAG = JoinNameActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_name)
        Log.e(TAG," onCreate");
    }
}