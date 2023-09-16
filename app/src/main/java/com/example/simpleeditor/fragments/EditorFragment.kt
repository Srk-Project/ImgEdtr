package com.example.simpleeditor.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.simpleeditor.R
import com.example.simpleeditor.adapter.FeaturesAdapter
import com.example.simpleeditor.databinding.FragmentEditorBinding
import com.example.simpleeditor.models.FeaturesModel
import com.example.simpleeditor.reusable.Constants
import com.example.simpleeditor.viewModel.ImageViewModel
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.Executors


class EditorFragment : Fragment(){
    private var editorBinding: FragmentEditorBinding? = null
    private lateinit var navController: NavController
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var folder: File
    private lateinit var adapters: FeaturesAdapter
    private lateinit var name: String
    private lateinit var extension: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var bitmap: Bitmap
    private lateinit var recView: RecyclerView
    @Volatile
    private var imgStr: ByteArray = byteArrayOf()
    private lateinit var py: Python
    private lateinit var pyObject: PyObject
    private lateinit var pyFileObject:PyObject
    private lateinit var imgUri: Uri
    private lateinit var progressBar:ProgressBar
    private lateinit var handler: Handler
    private val executor = Executors.newSingleThreadExecutor()
    private  var recent_bmp:Bitmap?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        if (!Python.isStarted())
            Python.start(AndroidPlatform(requireActivity()))
        py = Python.getInstance()

        editorBinding = FragmentEditorBinding.inflate(inflater, container, false)
        return editorBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        observer()
        handleRecView();
        backPressHandle()

        clickOperation(view)



    }


    private fun handleRecView() {
        val iClick = object : IClickHandle {
            override fun clickHandle(
                isReq: Boolean,
                min: Float,
                max: Float,
                start: Float,
                cardView: CardView,
                tv: TextView,
                rangeSlider: RangeSlider,
                step:Float
            ) {


                editorBinding?.imgDiscard?.visibility=View.VISIBLE


                recView.forEachIndexed { _, view ->
                    view.findViewById<CardView>(R.id.cv_img_features).background?.setTint(
                        ContextCompat.getColor(requireActivity(), R.color.white)
                    )
                    view.findViewById<TextView>(R.id.txt_features).textSize = 12.0F
                    view.findViewById<TextView>(R.id.txt_features)
                        .setTypeface(null, Typeface.NORMAL)
                    view.findViewById<RangeSlider>(R.id.seekbar).visibility=View.GONE

                }


                if (isReq) {
                    rangeSlider.visibility=View.VISIBLE

                    if(tv.text.equals("Blur")){

                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            if (rangeSlider.values[0].toInt()==0){

                                callPyObject("applyFilter",1,true)
                            }else{
                                callPyObject("applyFilter",rangeSlider.values[0].toInt(),true)
                            }
                        }
                    }

                    if (tv.text.equals("GrayScale")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("grayScale",rangeSlider.values[0].toInt(),true)


                        }
                    }

                    if (tv.text.equals("Brightness")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("brightness",rangeSlider.values[0].toInt(),true)


                        }
                    }
                    if (tv.text.equals("Hue")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("hue",rangeSlider.values[0].toInt(),true)
                        }
                    }
                    if (tv.text.equals("Saturation")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("saturation", rangeSlider.values[0].toInt(), true)
                        }
                    }

                    if (tv.text.equals("Eye")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("eyeColor",rangeSlider.values[0].toInt(),true)

                        }
                    }




                } else {
                    rangeSlider.visibility = View.GONE
                }



                cardView.background?.setTint(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.primary
                    )
                )
                tv.textSize = 15.0F
                tv.setTypeface(null, Typeface.BOLD)
                tv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                rangeSlider.addOnChangeListener() { slider, value, fromUser ->
                    if(tv.text.equals("Blur")){
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            var slider_value=value.toInt()
                            if (value==0F) (slider_value++)
                            callPyObject("applyFilter",slider_value,false)
                        }
                    }

                    if (tv.text.equals("GrayScale")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("grayScale",value.toInt(),false)
                        }
                    }

                    if (tv.text.equals("Brightness")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("brightness",value.toInt(),false)


                        }
                    }
                    if (tv.text.equals("Hue")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("hue",value.toInt(),false)


                        }
                    }

                    if (tv.text.equals("Saturation")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("saturation",value.toInt(),false)


                        }
                    }

                    if (tv.text.equals("Eye")) {
                        showProgressbar()
                        clickDisable()
                        executor.execute {
                            callPyObject("eyeColor",value.toInt(),false)


                        }
                    }




                }




            }
        }
        adapters = FeaturesAdapter(getList(), requireActivity(), iClick)
        recView.adapter = adapters
        val linearLayout = LinearLayoutManager(requireActivity())
        linearLayout.orientation = LinearLayoutManager.HORIZONTAL
        recView.layoutManager = linearLayout


    }

    private fun getList(): List<FeaturesModel> {
        val lists: MutableList<FeaturesModel> = ArrayList<FeaturesModel>()

        lists.add(FeaturesModel(R.drawable.baseline_camera_alt_24, "Hue", true, 0F, 360F, 0F,5F))
        lists.add(FeaturesModel(R.drawable.baseline_gallery, "Brightness", true, 0F,20F , 10F,1F))
        lists.add(FeaturesModel(R.drawable.baseline_camera_alt_24, "Saturation", true, 0F, 20F, 10F,1F))
        lists.add(FeaturesModel(R.drawable.baseline_camera_alt_24, "Blur", true, 0F, 6F, 0F,3F))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24, "GrayScale", true, 0F, 6F, 0F,1F))
        lists.add(FeaturesModel(R.drawable.baseline_camera_alt_24, "Eye", true, 0F, 360F, 0F,5F))




        return lists


    }


    private fun backPressHandle() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showAlertDialog()
                }
            }
            )
    }

    private fun showAlertDialog() {
        val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.editorFragment, true)
            .build()
        AlertDialog.Builder(requireActivity())
            .setMessage("Save Your changes or discard them ?")
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener { _, _ ->
                if (recent_bmp==null){
                    Toast.makeText(requireActivity(), "Make any changes to store image", Toast.LENGTH_SHORT).show()
                }else{
                    val name=editorBinding!!.etName.text
                    val strs=name.trim().split(".")
                    if (strs.size!=2) name.append(".").append(extension)

                    saveBitmapToExternalStorage(requireActivity(), recent_bmp!!, editorBinding!!.etName.text.toString())
                    Toast.makeText(requireActivity(), "Image Saved Successfully", Toast.LENGTH_SHORT).show()

                }

            })
            .setNeutralButton("Cancel", DialogInterface.OnClickListener { _, _ ->

            })
            .setNegativeButton("Discard", DialogInterface.OnClickListener { _, _ ->
                imageViewModel.setUriOfImg(null)
                navController.navigate(R.id.action_editorFragment_to_homeFragment, null, navOptions)

            })
            .show()
    }

    private fun clickOperation(view: View) {
        editorBinding?.imgHome?.setOnClickListener {

            showAlertDialog()
        }

        editorBinding?.imgDiscard?.setOnClickListener{
            showProgressbar()
            clickDisable()
            executor.execute {

                pyObject = pyFileObject.callAttr("showOriginalImage")

                val byteArray = pyObject.toJava(ByteArray::class.java)

                if (byteArray.isNotEmpty()) {
                    try {
                        val bmp =
                            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                        if (bmp != null) {
                            editorBinding?.mainImg?.setImageBitmap(bmp)
                        } else {
                            Log.d(
                                "information",
                                Thread.currentThread().name + "Bitmap is null"
                            )

                        }
                    } catch (e: Exception) {
                        Log.d(
                            "information",
                            Thread.currentThread().name + "Error: ${e.message}"
                        )

                    }
                } else {
                    Log.d(
                        "information",
                        Thread.currentThread().name + " Byte array is empty"
                    )
                }

                hideProgressbar()
                clickEnable()

            }
            editorBinding?.imgDiscard?.visibility=View.INVISIBLE
            recView.forEachIndexed{_,view->
                view.findViewById<CardView>(R.id.cv_img_features).background?.setTint(
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
                view.findViewById<TextView>(R.id.txt_features).textSize = 12.0F
                view.findViewById<TextView>(R.id.txt_features)
                    .setTypeface(null, Typeface.NORMAL)
                view.findViewById<RangeSlider>(R.id.seekbar).visibility=View.GONE

            }


        }

        editorBinding?.img?.setOnClickListener{
            if (recent_bmp==null){
                Toast.makeText(requireActivity(), "Make any changes to store image", Toast.LENGTH_SHORT).show()
            }else{
                val name=editorBinding!!.etName.text
                val strs=name.trim().split(".")
                if (strs.size!=2) name.append(".").append(extension)

                saveBitmapToExternalStorage(requireActivity(), recent_bmp!!, editorBinding!!.etName.text.toString())
                Toast.makeText(requireActivity(), "Image Saved Successfully", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun loadFileFromPrefs() {
        val file = File(folder, sharedPreferences.getString(Constants.GLOBAL_PHOTO_NAME, null)!!)
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.path)
            editorBinding?.mainImg?.setImageBitmap(bitmap)
            editorBinding?.etName?.setText(
                sharedPreferences.getString(
                    Constants.GLOBAL_PHOTO_NAME,
                    null
                )!!
            )
        }

    }

    private fun observer() {
        val args = arguments
        if (args != null && args.containsKey("uri")) {
            val it = Uri.parse(args.getString("uri"))

        if (it != null) {
            editorBinding?.mainImg?.setImageURI(it)
            imgUri = it
            executor.execute {
                showProgressbar()
                clickDisable()

                Log.d("information", Thread.currentThread().name + "uriToBytesOfArray" + "Start")

                bitmap = if (Build.VERSION.SDK_INT < 28) {

                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imgUri)
                } else {
                    val source =
                        ImageDecoder.createSource(requireActivity().contentResolver, imgUri)
                    ImageDecoder.decodeBitmap(source)
                }
                imgStr = getStringImg(bitmap)

                pyFileObject = py.getModule("file")
                pyFileObject.callAttr("getImg", imgStr)

                Log.d("information", Thread.currentThread().name + "uriToBytesOfArray" + "Start")


            }
            getExtension(imgUri)
            hideProgressbar()
            clickEnable()

        }
    }

    }

    private fun getStringImg(bitmap: Bitmap): ByteArray {
        var byout = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byout)
        return byout.toByteArray()
        //Base64.encodeToString(imgByte, Base64.DEFAULT)


    }



    private fun createFile(bitmap: Bitmap) {

        val file = File(folder, name)

        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            sharedPreferences.edit()
                .putString(Constants.GLOBAL_PHOTO_NAME, name)
                .apply()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Toast.makeText(requireActivity(), "Something went error", Toast.LENGTH_LONG).show()
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun setName() {

        name = "new_photo.$extension"
        editorBinding?.etName?.setText(name)


    }

    private fun getExtension(it: Uri) {
        val cursor: Cursor? = requireActivity().contentResolver.query(it, null, null, null, null)

        val nameIndex: Int? = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        if (nameIndex != null) {
            val str = cursor.getString(nameIndex).toString().trim().split(".")
            extension = str[1]

        }
        cursor?.close()
        setName()

    }


    private fun initView(view: View) {
        recView = editorBinding?.recViewEditor!!
        progressBar=editorBinding?.progressBar!!
        navController = Navigation.findNavController(view)
        imageViewModel = ViewModelProvider(requireActivity())[ImageViewModel::class.java]
        folder = requireActivity().getDir("temp", MODE_PRIVATE)
        sharedPreferences = requireActivity().getSharedPreferences(
            Constants.GLOBAL_SHARED_PREFERENCES,
            MODE_PRIVATE
        )
        handler=Handler(Looper.getMainLooper())


    }

    private fun showProgressbar(){
        handler.post(kotlinx.coroutines.Runnable {
            progressBar.visibility=View.VISIBLE
        })
    }

    private fun hideProgressbar(){
        handler.post(kotlinx.coroutines.Runnable {
            progressBar.visibility=View.INVISIBLE
        })
    }

    private fun showToast(str:String){
        handler.post(kotlinx.coroutines.Runnable {
            Toast.makeText(requireActivity(), ""+str, Toast.LENGTH_LONG).show()
        })
    }

    private fun clickEnable(){
        handler.post(kotlinx.coroutines.Runnable {
            recView.forEachIndexed { index, view ->
                view.findViewById<CardView>(R.id.cv_img_features).isClickable=true
            }
            editorBinding?.imgHome?.isClickable=true;
            editorBinding?.img?.isClickable=true
        })


    }

    private fun clickDisable(){
        handler.post(kotlinx.coroutines.Runnable {
            recView.forEachIndexed { index, view ->

                view.findViewById<CardView>(R.id.cv_img_features).isClickable=false

            }
            editorBinding?.imgHome?.isClickable=false;
            editorBinding?.img?.isClickable=false

        })

    }


    private fun callPyObject(functionName:String,value:Int,isFirst:Boolean){
        try {
            pyObject = if (isFirst) {
                pyFileObject.callAttr(
                    functionName,
                    value, true
                )
            }else{
                pyFileObject.callAttr(
                    functionName,
                    value, false
                )
            }
        }catch (e:Exception){
            if (functionName=="eyeColor") showToast("Sorry, We are not able to detect any faces/eyes")

            hideProgressbar()
            clickEnable()
            return
        }

        val byteArray = pyObject.toJava(ByteArray::class.java)

        if (byteArray.isNotEmpty()) {
            try {
                recent_bmp =
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                if (recent_bmp != null) {
                    editorBinding?.mainImg?.setImageBitmap(recent_bmp)
                } else {
                    Log.d(
                        "information",
                        Thread.currentThread().name + "Bitmap is null"
                    )
                    hideProgressbar()
                    clickEnable()

                }
            } catch (e: Exception) {
                Log.d(
                    "information",
                    Thread.currentThread().name + "Error: ${e.message}"
                )
                hideProgressbar()
                clickEnable()
            }
        } else {
            Log.d(
                "information",
                Thread.currentThread().name + " Byte array is empty"
            )
            hideProgressbar()
            clickEnable()
        }

        hideProgressbar()
        clickEnable()


    }


    fun saveBitmapToExternalStorage(context: Context, bitmap: Bitmap, fileName: String){
        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "srk_img")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)

        var outputStream: OutputStream? = null

        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            context.sendBroadcast(android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
        editorBinding = null
    }

}