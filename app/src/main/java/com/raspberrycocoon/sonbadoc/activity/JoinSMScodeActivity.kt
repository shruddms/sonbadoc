package com.raspberrycocoon.sonbadoc.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.raspberrycocoon.sonbadoc.R
import kotlinx.android.synthetic.main.activity_join_s_m_scode.*

/**
* 회원가입 화면 - sms문자인증
* 작성일 : 2020.12.02
* 작성자 : 노경은
*/
class JoinSMScodeActivity : AppCompatActivity() {

    private val TAG = JoinSMScodeActivity::class.java.name

    var phonenumber : String = "" //핸드폰번호
    var macaddress : String = "" //mac주소

    //인증시간 타이머
    var countDownTimer: CountDownTimer? = null
    val MILLISINFUTURE = 180 * 1000 //총 시간 (180초 = 3분)
    val COUNT_DOWN_INTERVAL = 1000 //onTick 메소드를 호출할 간격 (1초)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_s_m_scode)
        Log.e(TAG," onCreate");

        try {
            val intent = this.intent //데이터 수신
            phonenumber = intent.extras!!.getString("phonenumber").toString() //핸드폰번호
            macaddress = intent.extras!!.getString("macaddress").toString() //핸드폰 고유값
            Log.e(TAG, " 인텐트받기 $phonenumber $macaddress")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        countDownTimer_phone() //문자인증시간 메소드 실행

        /**
         * 텍스트변화감지
         */
        //인증번호 입력창
        edit_code1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.length == 1) {
                    edit_code2.requestFocus() //포커스이동
                    val code: String = edit_code1.getText().toString() + edit_code2.getText().toString()+
                            edit_code3.getText().toString() + edit_code4.getText().toString()
                    if (code.length >= 4) {
                        retrofit_smscode_check(phonenumber, macaddress, code) //sms인증번호 확인 레트로핏 메소드
                    }
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        edit_code2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.length == 1) {
                    edit_code3.requestFocus() //포커스이동
                    val code: String = edit_code1.getText().toString() + edit_code2.getText().toString()+
                            edit_code3.getText().toString() + edit_code4.getText().toString()
                    if (code.length >= 4) {
                        retrofit_smscode_check(phonenumber, macaddress, code) //sms인증번호 확인 레트로핏 메소드
                    }
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        edit_code3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.length == 1) {
                    edit_code4.requestFocus() //포커스이동
                    val code: String = edit_code1.getText().toString() + edit_code2.getText().toString()+
                            edit_code3.getText().toString() + edit_code4.getText().toString()
                    if (code.length >= 4) {
                        retrofit_smscode_check(phonenumber, macaddress, code) //sms인증번호 확인 레트로핏 메소드
                    }
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        edit_code4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.length == 1) {
                    val code: String = edit_code1.getText().toString() + edit_code2.getText().toString()+
                            edit_code3.getText().toString() + edit_code4.getText().toString()
                    if (code.length >= 4) {
                        retrofit_smscode_check(phonenumber, macaddress, code) //sms인증번호 확인 레트로핏 메소드
                    }
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        /**
         * 입력창 키보드 삭제 이벤트 처리
         */
        val keyListener =
            View.OnKeyListener { v, keyCode, event ->
                Log.d(TAG, " keyListener $keyCode")
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edit_code2.setText("")
                    edit_code1.requestFocus()
                }
                false
            }
        val keyListener2 =
            View.OnKeyListener { v, keyCode, event ->
                Log.d(TAG, " keyListener2 $keyCode")
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edit_code3.setText("")
                    edit_code2.requestFocus()
                }
                false
            }
        val keyListener3 =
            View.OnKeyListener { v, keyCode, event ->
                Log.d(TAG, " keyListener3 $keyCode")
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edit_code4.setText("")
                    edit_code3.requestFocus()
                }
                false
            }
        edit_code2.setOnKeyListener(keyListener)
        edit_code3.setOnKeyListener(keyListener2)
        edit_code4.setOnKeyListener(keyListener3)

        /**
         * 클릭
         */
        image_back.setOnClickListener(View.OnClickListener {
            Log.e(TAG, " 뒤로가기클릭")
            finish()
        })
        text_retransmission_code.setOnClickListener(View.OnClickListener {
            Log.e(TAG, " 코드재전송 클릭")
            edit_reset() //입력창 초기화
            JoinActivity.
            retrofit_sendsms(this,phonenumber,macaddress) //인증번호 전송 레트로핏

        })
    } //onCreate

    //인증번호가 4자리가 입력되면 자동으로 레트로핏이 실행됨
    private fun retrofit_smscode_check(phonenumber: String, macaddress: String, code: String) {
        Log.e(TAG," retrofit_smscode_check 인증번호 검사 및 회원가입.로그인 레트로핏 $phonenumber $macaddress $code");

        //TODO 레트로핏 제작해야됨

        intent_next(this)
    }

    //문자인증시간 메소드
    fun countDownTimer_phone() { //카운트 다운 메소드
        Log.e(TAG, "countDownTimer_phone  문자인증시간 메소드실행")

        edit_reset() //입력창 초기화

        countDownTimer = object : CountDownTimer(MILLISINFUTURE.toLong(), COUNT_DOWN_INTERVAL.toLong()) {
            override fun onTick(millisUntilFinished: Long) { //(300초에서 1초 마다 계속 줄어듬)
                val emailAuthCount = millisUntilFinished / 1000
                if (emailAuthCount - emailAuthCount / 60 * 60 >= 10) { //초가 10보다 크면 그냥 출력
                    text_overtime.setText((emailAuthCount / 60).toString() + " : " + (emailAuthCount - emailAuthCount / 60 * 60))
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    text_overtime.setText((emailAuthCount / 60).toString() + " : 0" + (emailAuthCount - emailAuthCount / 60 * 60))
                }
            }

            override fun onFinish() { //시간이 다 되면 다이얼로그 종료
                Log.e(TAG, " 인증시간종료")

                Snackbar.make(layout_main, getString(R.string.termination_time), Snackbar.LENGTH_SHORT).show()
                edit_reset() //입력창 초기화
            }
        }.start()
    }

    private fun edit_reset() {
        Log.e(TAG," edit_reset 입력창초기화 ");
        try {
            countDownTimer?.cancel() //실행중인게 있으면 취소
        } catch(e: Exception){
            Log.e(TAG," ${e.localizedMessage}");
            e.printStackTrace()
        }
        edit_code1.setText("")
        edit_code2.setText("")
        edit_code3.setText("")
        edit_code4.setText("")
        edit_code1.requestFocus()
        text_overtime.setText("00:00")
        text_errormsg.setVisibility(View.GONE)
    }

    companion object {
        private fun intent_next(activity: AppCompatActivity) {
            Log.e(activity.toString()," intent_next");
            val intent = Intent(activity, JoinNameActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.rightin,R.anim.not_move); //화면전환 애니메이션
        }
    }
}