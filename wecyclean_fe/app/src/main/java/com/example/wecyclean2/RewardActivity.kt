package com.example.wecyclean2

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
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

class RewardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

@Composable
fun Reward(navController: NavController) {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .background(c_main_green),
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(20.dp, 20.dp, 20.dp, 0.dp)) {

            Column(Modifier.weight(0.1f),horizontalAlignment = Alignment.CenterHorizontally) {
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
                        text = "리워드",
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
                    Spacer(modifier = Modifier.size(30.dp))
                }
                Text(
                    text = "포인트:"+upoint,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    color = Color.White,
                    fontFamily = notosanskr,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.weight(0.05f))
            Column(Modifier.weight(0.8f)) {


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(8.dp, RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                        .background(Color.White)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                    ) {
                        item {
                            for(i:Int in 1..5){
                            Column() {
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween)
                                {
                                    Row(modifier = Modifier.weight(0.45f))
                                    {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Image(
                                                painter = painterResource(id = R.drawable.gift1),
                                                contentDescription = "",
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Text(
                                                text = "삼육두유",
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
                                            Text(
                                                text = "100 포인트",
                                                style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false
                                                    )
                                                ),
                                                color = Color.Black,
                                                fontFamily = notosanskr,
                                                fontWeight = FontWeight.Thin,
                                                fontSize = 15.sp
                                            )
                                            Button(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentPadding = PaddingValues(0.dp),
                                                onClick =
                                                {
                                                    usepoint(uid_id,100,navController, context )
                                                },
                                                colors = ButtonDefaults.buttonColors(backgroundColor = c_sub_green)
                                            ) {
                                                Text(
                                                    text = "교환하기",
                                                    textAlign = TextAlign.Center,
                                                    color = Color.White,
                                                    fontFamily = notosanskr,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.weight(0.1f))

                                    Row(modifier = Modifier.weight(0.45f))
                                    {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Image(
                                                painter = painterResource(id = R.drawable.gift1),
                                                contentDescription = "",
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Text(
                                                text = "삼육두유",
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
                                            Text(
                                                text = "100 포인트",
                                                style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false
                                                    )
                                                ),
                                                color = Color.Black,
                                                fontFamily = notosanskr,
                                                fontWeight = FontWeight.Thin,
                                                fontSize = 15.sp
                                            )
                                            Button(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentPadding = PaddingValues(0.dp),
                                                onClick =
                                                {
                                                    usepoint(uid_id,100,navController, context )
                                                },
                                                colors = ButtonDefaults.buttonColors(backgroundColor = c_sub_green)
                                            ) {
                                                Text(
                                                    text = "교환하기",
                                                    textAlign = TextAlign.Center,
                                                    color = Color.White,
                                                    fontFamily = notosanskr,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        }
                    }
                }
            }
        }
    }
}