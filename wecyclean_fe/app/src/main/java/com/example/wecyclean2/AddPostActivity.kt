package com.example.wecyclean2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wecyclean2.ui.theme.Wecyclean2Theme

class AddPostActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}


@Composable
fun AddPosts(navController: NavController) {
    var titleState by remember { mutableStateOf("") } // 입력된 제목
    var contentState by remember { mutableStateOf("") } // 입력된 내용
    var localfocus = LocalFocusManager.current
    Column(
        Modifier
            .fillMaxSize()
            .background(c_main_green),
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(20.dp, 20.dp, 20.dp, 0.dp)) {

            Column(Modifier.weight(0.05f)) {
                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
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
                        text = "게시글 작성",
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        color = Color.White,
                        fontFamily = notosanskr,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.post),
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                //게시글을 추가합니다
                                if(titleState!="" && contentState!=""){
                                    addPost(uid,titleState,contentState)
                                    navController.popBackStack()
                                }

                            },

                        )
                }
            }
            Spacer(modifier = Modifier.weight(0.05f))

            Column(Modifier.weight(0.85f)) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(8.dp, RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                        .background(Color.White),
                ) {
                    Column(modifier=Modifier.fillMaxSize(),){
                        Column(
                            modifier = Modifier.weight(0.85f)
                                .padding(20.dp, 20.dp, 20.dp, 0.dp),
                        ) {

                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                        .fillMaxHeight(0.1f),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    SimpleTextField(
                                        placeholderText = "제목을 입력하세요",
                                        textAlign = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        value = titleState,
                                        textStyle = TextStyle.Default.copy(fontFamily = notosanskr, fontWeight = FontWeight.Normal ,fontSize = 18.sp),
                                        onValueChange = {
                                            titleState = it
                                        },
                                        singleLine = true,
                                        trailingIcon = {
                                        }
                                    )
                                }

                                Divider(Modifier.padding(0.dp, 10.dp, 0.dp, 10.dp),
                                    color = c_main_gray, thickness = 2.dp)

                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                        .fillMaxHeight(0.4f),
                                    horizontalAlignment = Alignment.CenterHorizontally ,
                                    verticalArrangement = Arrangement.Center,

                                ) {
                                    SimpleTextField(
                                        placeholderText = "내용을 입력하세요",
                                        textAlign = Alignment.Top,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.8f),
                                        value = contentState,
                                        textStyle = TextStyle.Default.copy(fontFamily = notosanskr, fontWeight = FontWeight.Normal ,fontSize = 15.sp),
                                        onValueChange = {
                                            contentState = it
                                        },
                                        singleLine = false,
                                        trailingIcon = {
                                        }
                                    )
                                }


                            }


//                        Column(
//                            modifier = Modifier
//                                .weight(0.15f)
//                                .padding(horizontal = 20.dp, vertical = 10.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center,
//                        ) {
//
//                            Text(
//                                text = "게시글 작성",
//                                style = TextStyle(
//                                    platformStyle = PlatformTextStyle(
//                                        includeFontPadding = false
//                                    )
//                                ),
//                                color = Color.Black,
//                                fontFamily = notosanskr,
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 18.sp
//                            )
//
//                        }
                    }


                }
            }

        }

    }
}