package com.example.wecyclean2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val notosanskr = FontFamily(
    Font(R.font.notosanskrlight, FontWeight.Thin),
    Font(R.font.notosanskrregular, FontWeight.Normal),
    Font(R.font.notosanskrbold, FontWeight.Bold),
)

val yangjin = FontFamily(Font(R.font.yangjin,FontWeight.Normal))

val c_main_green = Color(0xFF00A49C)
val c_sub_green = Color(0xFF00BB9D)
val c_main_gray = Color(0xFF95A5A6)
val c_light_gray = Color(0xFFD9D9D9)
val c_light_red = Color(0xFFDD5353)
val c_bright_gray = Color(0xFFE4E8E8)

var uid_id=0
var uid=""
var upoint=0
var temp_uid = "aa"
var temp_upw = "bb"
var isPermission = 0

class MainActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var photoUri: Uri
    private lateinit var photoBitmap:Bitmap
    private var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)

//    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(true)



    private fun handleImageCapture(uri: Uri) {
        Log.i("kilo", "Image captured: $uri")
//        shouldShowCamera.value = false
        photoUri = uri
        shouldShowPhoto.value = true



    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //알람 설정
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this,MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY,21)
            set(Calendar.MINUTE,6)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent)

        setContent {
            //마커 리스트
            val markerList = remember { mutableStateListOf<MarkerModel>() }
            //게시글 리스트
            val postList = remember { mutableStateListOf<PostModel>() }

            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "Intro",
            ){
                composable("Intro") {
                    Intro(navController)
                }
                composable("Login") {
                    Main(navController)
                }
                composable("Home") { backStackEntry ->
                    shouldShowPhoto.value = false
                    Home(navController=navController,markerList)
                }
                composable("Map") {
                    Maps(navController,markerList)
                }
                composable("Community") {
                    getPostList(postList)
                    Community(navController,postList)
                }
                composable("PostList") {
                    getPostList(postList)
                    PostList(navController,postList)
                }
                composable("Post/{postid}") { backStackEntry ->
                    getPostList(postList)
                    Posts(navController=navController,postList,postid=backStackEntry.arguments?.getString("postid") ?:"")
                }
                composable("AddPost") {
                    AddPosts(navController=navController)
                }
                composable("Reward") {
                    Reward(navController=navController)
                }
                composable("Camera") {
                    if (!shouldShowPhoto.value) {
                        CameraView(
                            navController,
                            outputDirectory = outputDirectory,
                            executor = cameraExecutor,
                            onImageCaptured = ::handleImageCapture,
                            onError = { Log.e("kilo", "View error:", it) }
                        )
                    }
                    else
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            CameraResult(
                                navController,photoUri,shouldShowPhoto
                            )
                        }
                    }




                }
                composable("Search/{category}") { backStackEntry ->
                    SearchResult(navController=navController,searchState=backStackEntry.arguments?.getString("category") ?:"")
                }
            }

            outputDirectory = getOutputDirectory()
            cameraExecutor = Executors.newSingleThreadExecutor()
        }
    }
}

@Composable
fun Intro(navController: NavController) {

    val handler = Handler()
    handler.postDelayed( {
        navController.navigate("Login") {
            //Intro를 백스택에서 제거합니다 (뒤로가기를 눌러도 인트로화면 나오지않게 하기 위함)
            popUpTo("Intro") {inclusive=true}
        }
    }, 1500)
    Column(
        Modifier
            .fillMaxSize()
            .background(c_main_green),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "우리사이클린",
                color = Color.White,
                fontFamily = yangjin,
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Main(navController: NavController) {

    val isLogin = remember { mutableStateOf(false)}

    //권한 설정
    if(isPermission == 0) {
        RequestPermission()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(c_main_green),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,

            ) {
            Text(text = "우리사이클린",color= Color.White, fontFamily = yangjin , fontWeight = FontWeight.Normal, fontSize = 36.sp)
            ShowSlides()
            loginform(navController)
        }
    }
}


// 슬라이드를 표시합니다.
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowSlides() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        val pagerState1 = rememberPagerState(initialPage = 0)
        val coroutineScope = rememberCoroutineScope()





        // 앱 설명 슬라이드
        val context = LocalContext.current
        val desclist = mutableListOf<String>("정확한 쓰레기 분류방법을 알 수 있습니다.","내 위치 주변의 쓰레기통을 찾을 수 있습니다.","커뮤니티 기능을 제공합니다.")

        HorizontalPager(
            count = 3,
            state = pagerState1,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(desclist[it], fontSize = 15.sp, color = Color.White, fontFamily = notosanskr, fontWeight = FontWeight.Normal)

                Image(
                    painter = painterResource(id= ImageList(it,context)),
                    contentDescription = null,
                    modifier = Modifier.width(150.dp)
                )
            }

//            Box(
//                modifier = Modifier
//                    .padding(10.dp)
//                    .shadow(1.dp, RoundedCornerShape(8.dp))
//                    .background(Color.White)
//                    .fillMaxWidth()
//                    .height(200.dp),
//                contentAlignment = Alignment.Center
//            ) {
//              Text(
//                    "Text $it",
//                    fontSize = 40.sp,
//                    color = Color.Gray
//                )
//            }
        }
        Spacer(Modifier.height(10.dp))
        PagerIndicator(
            pagerState = pagerState1,
            indicatorCount = 3,
            onClick = {
                coroutineScope.launch {
                    pagerState1.scrollToPage(it)
                }
            },
        )


    }
}

// 슬라이드 번호에 맞는 이미지 drawable을 반환합니다
fun ImageList(it:Int,context:Context):Int{
    val img_list = mutableListOf<String>("recycle_bin","maps","meeting")
    val drawableId =
        context.resources.getIdentifier(
            img_list[it],
            "drawable",
            context.packageName
        )
    return drawableId
}


// 슬라이드 밑의 인디케이터를 표시합니다.
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    indicatorCount: Int = 3,
    indicatorSize: Dp = 16.dp,
    indicatorShape: Shape = CircleShape,
    space: Dp = 8.dp,
    activeColor: Color = Color.White,
    inActiveColor: Color = Color.LightGray,
    onClick: ((Int) -> Unit)? = null,
) {

    val listState = rememberLazyListState()

    val totalWidth: Dp = indicatorSize * indicatorCount + space * (indicatorCount - 1)
    val widthInPx = LocalDensity.current.run { indicatorSize.toPx() }

    val currentItem by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }

    val itemCount = pagerState.pageCount

    LaunchedEffect(key1 = currentItem) {
        val viewportSize = listState.layoutInfo.viewportSize
        listState.animateScrollToItem(
            currentItem,
            (widthInPx / 2 - viewportSize.width / 2).toInt()
        )
    }


    LazyRow(
        modifier = modifier.width(totalWidth),
        state = listState,
        contentPadding = PaddingValues(vertical = space),
        horizontalArrangement = Arrangement.spacedBy(space),
        userScrollEnabled = false
    ) {

        items(itemCount) { index ->

            val isSelected = (index == currentItem)

            // Index of item in center when odd number of indicators are set
            // for 5 indicators this is 2nd indicator place
            val centerItemIndex = indicatorCount / 2

            val right1 =
                (currentItem < centerItemIndex &&
                        index >= indicatorCount - 1)

            val right2 =
                (currentItem >= centerItemIndex &&
                        index >= currentItem + centerItemIndex &&
                        index <= itemCount - centerItemIndex + 1)
            val isRightEdgeItem = right1 || right2

            // Check if this item's distance to center item is smaller than half size of
            // the indicator count when current indicator at the center or
            // when we reach the end of list. End of the list only one item is on edge
            // with 10 items and 7 indicators
            // 7-3= 4th item can be the first valid left edge item and
            val isLeftEdgeItem =
                index <= currentItem - centerItemIndex &&
                        currentItem > centerItemIndex &&
                        index < itemCount - indicatorCount + 1

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val scale = if (isSelected) {
                            1f
                        } else if (isLeftEdgeItem || isRightEdgeItem) {
                            .5f
                        } else {
                            .5f // 항목 5개 이상이면 .8f로 수정
                        }
                        scaleX = scale
                        scaleY = scale

                    }

                    .clip(indicatorShape)
                    .size(indicatorSize)
                    .background(
                        if (isSelected) activeColor else inActiveColor,
                        indicatorShape
                    )
                    .then(
                        if (onClick != null) {
                            Modifier
                                .clickable {
                                    onClick.invoke(index)
                                }
                        } else Modifier
                    )
            )
        }
    }
}

//로그인 폼을 표시합니다.
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun loginform(navController:NavController) {
    var formState by remember { mutableStateOf("login") } // 현재 폼 상태를 나타냅니다 (login:로그인폼,signup:회원가입폼)
    var idState by remember { mutableStateOf("") } // 입력된 아이디값
    var passwordState by remember { mutableStateOf("") } //입력된 패스워드값
    var context = LocalContext.current
    var loginError by remember { mutableStateOf(false)} // 로그인 에러 변수

    fun setLoginError() {
        loginError=true
    }


    // 로그인 폼 표시
    if (formState == "login") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "아이디",
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                fontFamily = notosanskr,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = idState,
                //textStyle = TextStyle.Default.copy(fontFamily = notosanskr, fontWeight = FontWeight.Thin ,fontSize = 12.sp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    idState = it
                    loginError = false
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if (loginError) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            tint = Color.Red,
                            contentDescription = null
                        )
                    }
                    else if (idState.isNotEmpty()) {
                        IconButton(onClick = { idState = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "비밀번호",
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                fontFamily = notosanskr,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,

                )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = passwordState,
                //textStyle = TextStyle.Default.copy(fontFamily = notosanskr, fontWeight = FontWeight.Thin ,fontSize = 12.sp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    passwordState = it
                    loginError = false
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if (loginError) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            tint = Color.Red,
                            contentDescription = null
                        )
                    }
                    else if (passwordState.isNotEmpty()) {
                        IconButton(onClick = { passwordState = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
            )


            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick =
                {
                    //로그인 시도
                    if(idState!="" && passwordState!=""){
                        loginAccount(idState,passwordState,navController,context)
                    }
                    else {
                        Toast.makeText(context, "잘못된 입력입니다.", Toast.LENGTH_SHORT).show()
                        loginError = true
                    }

//                 navController.navigate("Home")
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text(
                    text = "로그인",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontFamily = notosanskr,
                    fontWeight = FontWeight.Normal,
                )
            }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    idState=""
                    passwordState=""
                    formState="signup"
                          },
            ) {
                Text(
                    text = "회원가입",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontFamily = notosanskr,
                    fontWeight = FontWeight.Normal,
                )
            }

        }
    }
    //회원가입 폼 표시
    else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "아이디",
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                fontFamily = notosanskr,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = idState,
//                textStyle = TextStyle.Default.copy(fontFamily = notosanskr, fontWeight = FontWeight.Thin ,fontSize = 12.sp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    idState = it
                    loginError = false
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if (loginError) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            tint = Color.Red,
                            contentDescription = null
                        )
                    }
                    else if (idState.isNotEmpty()) {
                        IconButton(onClick = { idState = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "비밀번호",
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                fontFamily = notosanskr,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,

                )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = passwordState,
                //textStyle = TextStyle.Default.copy(fontFamily = notosanskr, fontWeight = FontWeight.Thin ,fontSize = 12.sp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    passwordState = it
                    loginError = false
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if (loginError) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            tint = Color.Red,
                            contentDescription = null
                        )
                    }
                    else if (passwordState.isNotEmpty()) {
                        IconButton(onClick = { passwordState = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
            )


            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {



                            if(idState!="" && passwordState!=""){
                                registerAccount(idState,passwordState,navController,context)
                            }
                            else {
                                Toast.makeText(context, "잘못된 입력입니다.", Toast.LENGTH_SHORT).show()
                                loginError = true
                            }
                          },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text(
                    text = "회원가입",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontFamily = notosanskr,
                    fontWeight = FontWeight.Normal,
                )
            }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                        idState=""
                        passwordState=""
                        formState = "login"
                        loginError=false
                          },
            ) {
                Text(
                    text = "뒤로가기",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontFamily = notosanskr,
                    fontWeight = FontWeight.Normal,
                )
            }

        }

    }



}

// 인풋 텍스트 박스
@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholderText: String = "",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    cursorBrush: Brush = SolidColor(Color.Black),
    textAlign : Alignment.Vertical,
) {
    BasicTextField(modifier = modifier
        .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        enabled = enabled,
        readOnly = readOnly,
        interactionSource = interactionSource,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onTextLayout = onTextLayout,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = textAlign
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(
                        placeholderText,
                        style = textStyle
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}