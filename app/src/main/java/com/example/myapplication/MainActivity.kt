package com.example.myapplication

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val myRegex = MyRegex()

    companion object {
        var regexString = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText.addTextChangedListener(tw())

        val setFunction =  { result : String ->
            MainActivity.regexString = result
            inputText.hint = "文字を入力してください"
            inputText.isEnabled = true
        }

        myRegex.getRegexData(setFunction)

    }
    private inner class tw : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val regex = MainActivity.regexString.toRegex()
            val matched = regex.containsMatchIn(inputText.text.toString())

            if (matched) {
                result.text = "判定:〇"
                result.setTextColor(Color.BLUE)
            }else {
                result.text = "判定:×"
                result.setTextColor(Color.RED)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            print(inputText.text.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            print(inputText.text.toString())
        }
    }

}

class MyRegex {

   fun getRegexData(setFunction: (String) -> Unit){

        //InfoReceiverインスタンスを生成
        val receiver = MyInfoReceiver(setFunction)
        //InfoReceiverを実行
        receiver.execute()

    }


    private inner class MyInfoReceiver(setFunction: (String) -> Any) : InfoReceiver() {

        var mySetFunction = setFunction

        override fun setdata(result:String){

            result?.let{
                this.mySetFunction(it!!)
            }
        }
    }

    // 非同期処理
    private inner open class InfoReceiver() : AsyncTask<String, String, String>() {
        // バックグランド処理
        override fun doInBackground(vararg params: String): String {
            //入力チェック用正規表現
            val setRegexString = "[\\w\\-._]+@[\\w\\-._]+\\.[A-Za-z]+"
            //非同期処理確認用。
            Thread.sleep(5000)

            return setRegexString
        }

        // データ処理
        override fun onPostExecute(result: String) {
            // データ確認
            result ?: return
            setdata(result)
        }

        open fun setdata(result:String) {}
    }
}