package com.raspberrycocoon.sonbadoc.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.raspberrycocoon.sonbadoc.R
import kotlinx.android.synthetic.main.activity_join.*
import java.net.NetworkInterface
import java.util.*


/**
 * 회원가입 화면 - 휴대폰번호 입력
 * 작성일 : 2020.12.02
 * 작성자 : 노경은
 */
class JoinActivity : AppCompatActivity() {

    private val TAG = JoinActivity::class.java.name

    var imm : InputMethodManager? = null //키보드

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        Log.e(TAG," onCreate");

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager //키보드

        //키보드올리기
        edit_phonenum.postDelayed(Runnable {
            edit_phonenum.requestFocus()
            imm!!.showSoftInput(edit_phonenum, 0)
        }, 100)

        /**
         * 포커스컨트롤
         */
        //휴대폰번호입력창
        edit_phonenum.setOnFocusChangeListener(View.OnFocusChangeListener { v, gainFocus ->
            //포커스가 주어졌을 때
            if (gainFocus) {
                line_phoneedit.setBackgroundColor(resources.getColor(R.color.colorBlack))
            } else {
                line_phoneedit.setBackgroundColor(resources.getColor(R.color.colorGray6))
                if (edit_phonenum.getText().toString() == "") { //공백일때
                    text_errormsg.setVisibility(View.GONE) //잘못된형식번호 에러메세지 숨기기
                }
            }
        })

        /**
         * 텍스트변화감지
         */
        //핸드폰번호입력감지
        edit_phonenum.addTextChangedListener(object : TextWatcher {
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
                if (s.length > 0) {
                    text_next.setBackgroundResource(R.drawable.roundspace_btn_blue)
                } else {
                    //0글자일때
                    text_errormsg.setVisibility(View.GONE)
                    text_next.setBackgroundResource(R.drawable.roundspace_btn_gray)
                    edit_phonenum.setHint(resources.getString(R.string.enter_phone))
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        /**
         * 국가코드 선택
         */
        country_code_picker.setCountryForNameCode( Locale.getDefault().getCountry() )
        country_code_picker.setOnCountryChangeListener {
            Log.e(TAG, " 선택한 국가코드: ${country_code_picker.selectedCountryCode} 국가 ${country_code_picker.selectedCountryName}")
            country_code_picker.selectedCountryName
            text_errormsg.setVisibility(View.GONE)
        }

        /**
         * 클릭
         */
        text_next.setOnClickListener(View.OnClickListener {
            Log.e(TAG, " 인증하기클릭")
            do_certified() //인증하기 메소드 실행
        })

    } //oncreate

    private fun do_certified() {
        Log.e(TAG, "do_certified 인증하기 메소드");

        var phonenumber: String = edit_phonenum.getText().toString()

        if (!phonenumber.equals("")) { //핸드폰번호 공백아님
            if (phonenumber.length < 20 && phonenumber.length > 5) { //5글자 초과 20글자 미만인지 체크

                phonenumber = phonenumber.replace("[^0-9.]".toRegex(), "") //숫자만 추출

                country_code_picker.registerCarrierNumberEditText(edit_phonenum)
                Log.e(TAG, " 번호형식체크 ${country_code_picker.isValidFullNumber}()")
                if (country_code_picker.isValidFullNumber()) { //번호형식 맞음
                    phonenumber = country_code_picker.getFullNumber()
                    var countrycode = country_code_picker.selectedCountryCode
                    Log.e(TAG, " 국가코드적용한 핸드폰번호 $phonenumber 국가코드 $countrycode");
                    text_errormsg.setVisibility(View.GONE)
                    //핸드폰번호 정상
                    retrofit_sendsms(this,phonenumber,countrycode) //인증번호 전송 레트로핏

                } else { //번호형식 틀림
                    text_errormsg.setVisibility(View.VISIBLE)
                }
            } else { ////5글자 초과 20글자 미만 - 잘못입력
                text_errormsg.setVisibility(View.VISIBLE)
            }
        } else { //핸드폰번호 공백
            text_errormsg.setVisibility(View.GONE)
        }
    } //do_certified

    companion object {
        private fun intent_next(activity: AppCompatActivity, phonenumber: String?, macaddress: String?) {
            Log.e(activity.toString()," intent_next $phonenumber $macaddress");
            val intent = Intent(activity, JoinSMScodeActivity::class.java)
            //핸드폰번호와 맥주소를 전달하는 이유는 다음화면에서 인증번호 재전송을 위해서이다.
            intent.putExtra("phonenumber",phonenumber)
            intent.putExtra("macaddress",macaddress)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.rightin,R.anim.not_move); //화면전환 애니메이션
        }

        fun retrofit_sendsms(activity: AppCompatActivity, phonenumber: String, macaddress: String) {
            Log.e(activity.toString()," retrofit_sendsms 인증번호 전송 $phonenumber $macaddress");

            val macaddress = getMacAddress() //mac주소 구하기
            Log.e(activity.toString()," macaddress $macaddress");

            //TODO 인증번호 전송 레트로핏 코드 작성해야함

            dialog_loading(activity, phonenumber, macaddress.toString())
        }

        //mac주소 구하기
        fun getMacAddress(): String? {
            Log.e(""," getMacAddress mac주소 구하기");
            try {
                val all: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                    val macBytes = nif.hardwareAddress ?: return ""
                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02X", b))
                    }
                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            } catch (ex: java.lang.Exception) {
                //handle exception
            }
            return ""
        }

        fun dialog_loading(activity: AppCompatActivity, phonenumber: String?, macaddress: String?) {
            Log.e(activity.toString(), " sms전송 로딩 다이얼로그 메소드 dialog_loading $phonenumber $macaddress")
            val builder = AlertDialog.Builder(activity!!)
            val inflater = activity.layoutInflater
            val view: View = inflater.inflate(R.layout.dialog_loading_sms, null)
            builder.setView(view)
            val dialog = builder.create()
            val image_loading = view.findViewById<View>(R.id.image_loading) as ImageView

            Glide.with(activity).load(R.raw.ico_loading_sms).into(image_loading)
            val thread: Thread = object : Thread() {
                override fun run() {
                    Log.e(activity.toString(), "스레드 run")
                    try {
                        sleep(2000) //스레드 정지 1000 : 1초
                        dialog.dismiss() //다이얼로그 닫기
                        interrupt() //스레드 종료

                        //현재 화면이 인증번호화면이 아니면 인증번호 화면으로 이동하고 아니면 화면 그대로
                        Log.e(activity.toString()," 현재 화면이 인증번호화면이 아니면 인증번호 화면으로 이동하고 아니면 화면 그대로 ");
                        if(!activity.toString().contains("JoinSMScodeActivity")){
                            intent_next(activity,phonenumber,macaddress) //다음화면 이동
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            thread.start() //스레드시작
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            dialog.window!!.setLayout(650, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

}

