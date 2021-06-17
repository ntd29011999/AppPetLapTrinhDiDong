package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class LoginActivity : AppCompatActivity() {
    val urlGetAccount:String = "http://ntd29011999.000webhostapp.com/getaccount.php"
    var mangAccount: ArrayList<String> = ArrayList()
    var mangPhone: ArrayList<String> = ArrayList()
    var mangPassword: ArrayList<String> = ArrayList()
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val actionBar = supportActionBar
        actionBar?.hide()
        val edtPhone = findViewById<EditText>(R.id.editTextPhoneLogin)
        val edtPassword = findViewById<EditText>(R.id.editTextPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)
        val btnToSignUp = findViewById<Button>(R.id.ToSignUp)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val sharedPhoneValue = sharedPreferences.getString("phone","defaultPhone")
        val sharedPasswordValue = sharedPreferences.getString("password","defaultPassword")
        edtPhone.setText(sharedPhoneValue)
        edtPassword.setText(sharedPasswordValue)
        GetData().execute(urlGetAccount)
        val handler = Handler()
        handler.postDelayed(Runnable { // Do something after 1s = 2000ms
            var checkAccount = CheckAccount(
                edtPhone.text.toString().trim(),
                edtPassword.text.toString().trim()
            )
            if (checkAccount) {
                Toast.makeText(applicationContext, "Login successfully", Toast.LENGTH_SHORT)
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                editor.clear()
                editor.putString("phone",edtPhone.text.toString().trim())
                editor.putString("password",edtPassword.text.toString().trim())
                editor.apply()
                editor.commit()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                //finish()
            }
        }, 2000)


        btnToSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            if (edtPhone.text.trim().toString().isNullOrEmpty()) {
                edtPhone.setError("enter email please!");
            }
            if (edtPassword.text.trim().toString().isNullOrEmpty()) {
               edtPassword.setError("enter password please!");
            }

            var checkAccount = CheckAccount(
                edtPhone.text.toString().trim(),
                edtPassword.text.toString().trim()
            )
            if (checkAccount) {
                Toast.makeText(applicationContext, "Login successfully", Toast.LENGTH_SHORT)
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                editor.clear()
                editor.putString("phone",edtPhone.text.toString().trim())
                editor.putString("password",edtPassword.text.toString().trim())
                editor.apply()
                editor.commit()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                //finish()
            } else {
                Toast.makeText(applicationContext, "please try again!", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun CheckAccount(phone: String, password: String): Boolean{
        var i = 0;
        for (phoneCheck in mangPhone) {
            if (phone.equals(phoneCheck))
                return password.equals(mangPassword[i])
            i++;
        }
        return false
    }

    inner class GetData : AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg params: String?): String {
            return getContentURL(params[0])
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
//            Toast.makeText(applicationContext, result, Toast.LENGTH_LONG).show()
            var jsonArray: JSONArray = JSONArray(result)
            var id:String =""
            var phone:String = ""
            var password:String =""
            var deviceid:String =""

            for (account in 0..jsonArray.length()-1){
                var objectAccount: JSONObject = jsonArray.getJSONObject(account)
                id = objectAccount.getString("id")
                phone = objectAccount.getString("phone")
                password = objectAccount.getString("password")
                deviceid = objectAccount.getString("deviceid")
                mangAccount.add(
                    id + " - " + phone + " - " + password + " - "
                            + deviceid
                )
                mangPhone.add(phone)
                mangPassword.add(password)
            }



        }

    }
    private fun getContentURL(url: String?) : String{
        var content: StringBuilder = StringBuilder();
        val url: URL = URL(url)
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val inputStreamReader: InputStreamReader = InputStreamReader(urlConnection.inputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

        var line: String = ""
        try {
            do {
                line = bufferedReader.readLine()
                if(line != null){
                    content.append(line)
                }
            }while (line != null)
            bufferedReader.close()
        }catch (e: Exception){
            Log.d("AAA", e.toString())
        }
        return content.toString()
    }
}