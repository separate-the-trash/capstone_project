package com.example.wecyclean2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

val imageSize = 224

private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
) {

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.KOREA).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e("kilo", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}



@Composable
fun CameraView(
    navController:NavController,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
)
{
    // 1
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val preview = androidx.camera.core.Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    // 2
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture);
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(c_main_green),
    ) {
        Column(
            Modifier
                .fillMaxHeight()
            ,verticalArrangement = Arrangement.SpaceBetween){

            Column(Modifier
                .weight(0.2f)
                .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(id = R.drawable.left_arrow),
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { navController.popBackStack() },

                        )
                    Text(
                        text = "카메라",
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        color= Color.White,
                        fontFamily = notosanskr ,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.size(30.dp))
                }

            }
            Column(Modifier.weight(0.8f)) {


                    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
                        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

                        IconButton(
                            modifier = Modifier.padding(bottom = 20.dp),
                            onClick = {
                                Log.i("kilo", "ON CLICK")
                                takePhoto(
                                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                                    imageCapture = imageCapture,
                                    outputDirectory = outputDirectory,
                                    executor = executor,
                                    onImageCaptured = onImageCaptured,
                                    onError = onError
                                )
                            },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.camera_button),
                                    contentDescription = "Take picture",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(1.dp)
                                        .border(1.dp, Color.White, CircleShape)
                                )
                            }
                        )
                    }
                }

            }
        }
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraResult(
    navController:NavController,
    photoUri:Uri,
    shouldShowPhoto:MutableState<Boolean>
)
{

    val result = ImageClassify(photoUri)


    Column(
        Modifier
            .fillMaxSize()
            .background(c_main_green),
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(20.dp, 20.dp, 20.dp, 0.dp)){

            Column(Modifier.weight(0.05f)){
                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(id = R.drawable.left_arrow),
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { navController.popBackStack() },

                        )
                    Text(
                        text = "결과",
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        color= Color.White,
                        fontFamily = notosanskr ,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.size(30.dp))
                }
            }
            Spacer(modifier = Modifier.weight(0.05f))
            Column(Modifier.weight(0.3f)) {
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(8.dp, RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp))
                        .background(Color.White)
                )
                {
                    Image(
                    painter = rememberImagePainter(photoUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )}

            }
            Spacer(modifier = Modifier.weight(0.05f))
            Column(Modifier.weight(0.55f)) {



                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(8.dp, RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                        .background(Color.White)
                )   {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                    ) {
                        item{
                            // 결과내용
                            RecycledResult(result)
                        }


                    }
                }


            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImageClassify(photoUri: Uri):String {

    val context = LocalContext.current
    val source = ImageDecoder.createSource(context.contentResolver, photoUri)
    var photoBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var result=""
    photoBitmap = ImageDecoder.decodeBitmap(
        source,
        ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true
        })

    val scaledBitmap = Bitmap.createScaledBitmap(photoBitmap!!, imageSize, imageSize, false);

    TensorFLowHelper.classifyImage(scaledBitmap){
        result = it
        Log.e("",it)
    }

    return result
}

// 텐서플로우에 의해 판별된 결과를 보여줍니다.
@Composable
fun RecycledResult(result:String) {
    var sorted: Painter = painterResource(id = R.drawable.plastic_logo)
//    val sorted_list = mutableListOf<String>("plastic_logo","metal_logo","glass_logo","general_logo")
//    var sorted_title = ""
//    var sorted_desc = ""

    var list_general = listOf<String>(
        "나무젓가락",
        "칫솔",
        "기저귀",
        "테이프",
        "고무장갑",
        "컵라면용기",
        "라텍스",
        "식칼",
    )
    var list_glass = listOf<String>(
        "스티로폼",
        "맥주병",
    )
    var list_plastic = listOf<String>("스티로폼",)
    var list_metal = listOf<String>("후라이팬",)

    // 검색목록
    if(list_general.contains(result)) {
        sorted = painterResource(id = R.drawable.general_desc)
    }
    if(list_glass.contains(result)) {
        sorted = painterResource(id = R.drawable.glass_desc)
    }
    if(list_plastic.contains(result)) {
        sorted = painterResource(id = R.drawable.plastic_desc)
    }
    if(list_metal.contains(result)) {
        sorted = painterResource(id = R.drawable.metal_desc)
    }


    //---- 카메라 분석 ----
    if(result == "plastic") {
        sorted = painterResource(id = R.drawable.plastic_desc)
    }
    if(result == "metal") {
        sorted = painterResource(id = R.drawable.metal_desc)

    }
    if(result == "brown-glass" || result == "white-glass" ) {
        sorted = painterResource(id = R.drawable.glass_desc)
    }
    if(result == "trash") {
        sorted = painterResource(id = R.drawable.general_desc)
    }

    Column(Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = sorted,
            contentDescription ="",
            modifier = Modifier.fillMaxSize()
        )
//        Spacer(Modifier.height(10.dp))
//        Text(
//            text = sorted_title,
//            style = TextStyle(
//                platformStyle = PlatformTextStyle(
//                    includeFontPadding = false
//                )
//            ),
//            color= Color.Black,
//            fontFamily = notosanskr ,
//            fontWeight = FontWeight.Normal,
//            fontSize = 18.sp
//        )
//        Text(
//            text = sorted_desc,
//            style = TextStyle(
//                platformStyle = PlatformTextStyle(
//                    includeFontPadding = false
//                )
//            ),
//            color= c_main_gray,
//            fontFamily = notosanskr ,
//            fontWeight = FontWeight.Thin,
//            fontSize = 14.sp,
//            textAlign = TextAlign.Center
//        )

    }
}