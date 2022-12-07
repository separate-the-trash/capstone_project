package com.example.wecyclean2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.launch

class CommunityActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Community(navController: NavController,postList:MutableList<PostModel>) {

        Column(
            Modifier
                .fillMaxSize()
                .background(c_main_green),
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(20.dp)){

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
                            text = "커뮤니티",
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
                Column(Modifier.weight(0.3f)) {
                    ShowNews()
                }
                Column(Modifier.weight(0.3f)) {



                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .shadow(8.dp, RoundedCornerShape(8.dp))
                            .background(Color.White)
                    )   {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Text(
                                    text = "게시판",
                                    style = TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    ),
                                    color= Color.Black,
                                    fontFamily = notosanskr ,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp
                                )
                                Text(
                                    modifier = Modifier.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null) {
                                        navController.navigate("PostList")
                                    },
                                    text = "더보기",
                                    style = TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    ),
                                    color= c_light_red,
                                    fontFamily = notosanskr ,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            }

                            Divider(Modifier.padding(0.dp, 20.dp,0.dp,10.dp),
                                color = c_main_gray, thickness = 1.dp)


                            postList.reversed().forEach { post ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp,0.dp,10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Row(
                                        modifier = Modifier.fillMaxWidth(0.6f)
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
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 15.sp
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(0.4f),
                                        horizontalArrangement = Arrangement.End
                                    ){
                                        Text(
                                            text = "댓글 "+post.replys.size,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),
                                            color= Color.Black,
                                            fontFamily = notosanskr ,
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            }

                        }
                    }

                    Spacer(modifier = Modifier.weight(0.20f))

                }

            }
        }
    }

// 슬라이드를 표시합니다.
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowNews() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        val pagerState1 = rememberPagerState(initialPage = 0)
        val coroutineScope = rememberCoroutineScope()





        // 앱 설명 슬라이드
        val context = LocalContext.current

        HorizontalPager(
            count = 5,
            state = pagerState1,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(10.dp)
                    .shadow(8.dp, RoundedCornerShape(8.dp))
                    .background(Color.White),

                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id= NewsList(it,context)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        PagerIndicator(
            pagerState = pagerState1,
            indicatorCount = 5,
            onClick = {
                coroutineScope.launch {
                    pagerState1.scrollToPage(it)
                }
            })


    }
}

// 슬라이드 번호에 맞는 이미지 drawable을 반환합니다
fun NewsList(it:Int,context: Context):Int{
    val img_list = mutableListOf<String>("card1","card2","card3","card4","card5")
    val drawableId =
        context.resources.getIdentifier(
            img_list[it],
            "drawable",
            context.packageName
        )
    return drawableId
}