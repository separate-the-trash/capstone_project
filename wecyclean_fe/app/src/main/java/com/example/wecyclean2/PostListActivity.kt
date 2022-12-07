package com.example.wecyclean2

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}



@Composable
fun PostList(navController: NavController,postList:MutableList<PostModel>) {
    Column(
        Modifier
            .fillMaxSize()
            .background(c_main_green),
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(20.dp,20.dp,20.dp,0.dp)){

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
                        text = "게시글 목록",
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
                    Icon(
                        painter = painterResource(id = R.drawable.post),
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { navController.navigate("AddPost") },

                        )
                }
            }
            Spacer(modifier = Modifier.weight(0.05f))
            Column(Modifier.weight(0.9f)) {



                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(8.dp, RoundedCornerShape(8.dp,8.dp,0.dp,0.dp))
                        .background(Color.White)
                )   {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                    ) {
                        item{
                            postList.reversed().forEach { post ->
                                Column(modifier = Modifier.fillMaxWidth().clickable { navController.navigate("Post/${post.id}") }){
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(
                                            text = post.title,
                                            style = TextStyle(

                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),

                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            color= Color.Black,
                                            fontFamily = notosanskr ,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 15.sp
                                        )

                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(0.dp, 5.dp,0.dp,5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(
                                            text = post.content,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),

                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            color= Color.Black,
                                            fontFamily = notosanskr ,
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(0.dp, 5.dp,0.dp,5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(
                                            text = "댓글 "+post.replys.size,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),

                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            color= Color.Black,
                                            fontFamily = notosanskr ,
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        var timeData = post.regdate.split("-","T",":")
                                        val dateAndtime: LocalDateTime = LocalDateTime.of(timeData[0].toInt(), timeData[1].toInt(), timeData[2].toInt(), timeData[3].toInt(), timeData[4].toInt(), timeData[5].toDouble().toInt())
                                        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 | ")
                                        val timeformatted = dateAndtime.format(formatter)
                                        Text(
                                            text = timeformatted,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),
                                            color= c_main_gray,
                                            fontFamily = notosanskr ,
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = "작성자 "+post.userid,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),
                                            color= c_main_gray,
                                            fontFamily = notosanskr ,
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 12.sp
                                        )

                                    }

                                    Divider(Modifier.padding(0.dp, 20.dp,0.dp,20.dp),
                                        color = c_main_gray, thickness = 1.dp)
                                }

                            }
                        }




                    }
                }


            }

        }
    }
}