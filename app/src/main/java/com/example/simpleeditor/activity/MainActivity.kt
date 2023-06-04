package com.example.simpleeditor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.simpleeditor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        if(!Python.isStarted())
            Python.start(AndroidPlatform(this))
        val py = Python.getInstance()
        val file = py.getModule("file")
        val j: PyObject? = file.callAttr("strstr","Iammonkey")
       // Toast.makeText(this, j.toString(), Toast.LENGTH_SHORT).show()
    }


}