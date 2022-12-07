package com.example.wecyclean2

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wecyclean2.ui.theme.Wecyclean2Theme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}


@Composable
fun Posts(navController: NavController,postList:MutableList<PostModel>,postid:String) {
    var replyState by remember { mutableStateOf("") } // 입력된 댓글
    var deleteState by remember {mutableStateOf(false)} // 글 삭제되었는지 확인
    if (deleteState == false) {
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
                            text = "게시글",
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
                        if (postList[postid.toInt() - 1].userid == uid) {
                            Icon(
                                painter = painterResource(id = R.drawable.remove),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        //글 삭제
                                        deleteState=true
                                        navController.popBackStack()
                                        deletePost(postList[postid.toInt() - 1].postid.toString(), uid, postList)
                                    },
                            )
                        } else {
                            Spacer(modifier = Modifier.size(30.dp))
                        }

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
                        Column(modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween) {
                            Column(
                                modifier = Modifier.weight(0.85f)
                                    .padding(20.dp, 20.dp, 20.dp, 0.dp),
                            ) {
                                Column() {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(

                                            text = postList[postid.toInt() - 1].title,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),
                                            color = Color.Black,
                                            fontFamily = notosanskr,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 15.sp
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp, 5.dp, 0.dp, 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "작성자 " + postList[postid.toInt() - 1].userid,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),
                                            color = Color.Black,
                                            fontFamily = notosanskr,
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 12.sp
                                        )
                                    }


                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp, 5.dp, 0.dp, 5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = postList[postid.toInt() - 1].content,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),
                                            color = Color.Black,
                                            fontFamily = notosanskr,
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 12.sp
                                        )
                                    }
                                    var timeData =
                                        postList[postid.toInt() - 1].regdate.split("-", "T", ":")
                                    val dateAndtime: LocalDateTime =
                                        LocalDateTime.of(timeData[0].toInt(),
                                            timeData[1].toInt(),
                                            timeData[2].toInt(),
                                            timeData[3].toInt(),
                                            timeData[4].toInt(),
                                            timeData[5].toDouble().toInt())
                                    val formatter =
                                        DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
                                    val timeformatted = dateAndtime.format(formatter)
                                    Text(
                                        text = timeformatted,
                                        style = TextStyle(
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = false
                                            )
                                        ),
                                        color = c_main_gray,
                                        fontFamily = notosanskr,
                                        fontWeight = FontWeight.Thin,
                                        fontSize = 12.sp
                                    )

                                    Text(
                                        text = "댓글 " + postList[postid.toInt() - 1].replys.size,
                                        style = TextStyle(
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = false
                                            )
                                        ),
                                        color = c_main_gray,
                                        fontFamily = notosanskr,
                                        fontWeight = FontWeight.Thin,
                                        fontSize = 12.sp
                                    )

                                    Divider(Modifier.padding(0.dp, 20.dp, 0.dp, 20.dp),
                                        color = c_main_gray, thickness = 2.dp)

                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        item {
                                            postList[postid.toInt() - 1].replys.forEach { reply ->


                                                Column() {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = reply.userid,
                                                            style = TextStyle(
                                                                platformStyle = PlatformTextStyle(
                                                                    includeFontPadding = false
                                                                )
                                                            ),
                                                            color = Color.Black,
                                                            fontFamily = notosanskr,
                                                            fontWeight = FontWeight.Normal,
                                                            fontSize = 15.sp
                                                        )
                                                        // 삭제 아이콘 넣기
                                                    }

                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(0.dp, 5.dp, 0.dp, 5.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = reply.content,
                                                            style = TextStyle(
                                                                platformStyle = PlatformTextStyle(
                                                                    includeFontPadding = false
                                                                )
                                                            ),
                                                            color = Color.Black,
                                                            fontFamily = notosanskr,
                                                            fontWeight = FontWeight.Thin,
                                                            fontSize = 12.sp
                                                        )
                                                    }

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Start,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        //시간데이터를 규격에 맞게 변환합니다
                                                        var timeData =
                                                            reply.regdate.split("-", "T", ":")
                                                        val dateAndtime: LocalDateTime =
                                                            LocalDateTime.of(timeData[0].toInt(),
                                                                timeData[1].toInt(),
                                                                timeData[2].toInt(),
                                                                timeData[3].toInt(),
                                                                timeData[4].toInt(),
                                                                timeData[5].toDouble().toInt())
                                                        val formatter =
                                                            DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
                                                        val timeformatted =
                                                            dateAndtime.format(formatter)
                                                        Text(
                                                            text = timeformatted,
                                                            style = TextStyle(
                                                                platformStyle = PlatformTextStyle(
                                                                    includeFontPadding = false
                                                                )
                                                            ),
                                                            color = c_main_gray,
                                                            fontFamily = notosanskr,
                                                            fontWeight = FontWeight.Thin,
                                                            fontSize = 12.sp
                                                        )

                                                    }

                                                    Divider(Modifier.padding(0.dp,
                                                        20.dp,
                                                        0.dp,
                                                        20.dp),
                                                        color = c_main_gray, thickness = 1.dp)
                                                }


                                            }
                                        }

                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.15f)
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {

                                SimpleTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.8f)
                                        .padding(horizontal = 10.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(c_bright_gray),
                                    value = replyState,
                                    textStyle = TextStyle.Default.copy(fontFamily = notosanskr,
                                        fontWeight = FontWeight.Thin,
                                        fontSize = 12.sp),
                                    onValueChange = {
                                        replyState = it
                                    },
                                    textAlign = Alignment.CenterVertically,
                                    singleLine = true,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.send),
                                            tint = c_main_green,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(25.dp)
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = null
                                                ) {
                                                    addReply(postList[postid.toInt() - 1].postid.toString(),
                                                        uid,
                                                        replyState,
                                                        postList)
                                                    replyState = ""
                                                },

                                            )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}