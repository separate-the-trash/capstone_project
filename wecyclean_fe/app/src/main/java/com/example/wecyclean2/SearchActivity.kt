package com.example.wecyclean2

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.wecyclean2.ui.theme.Wecyclean2Theme

class SearchResult : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SearchResult(
    navController: NavController,
    searchState:String,
)
{



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
                        text = searchState,
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
            Column(Modifier.weight(0.9f)) {



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
                            RecycledResult(searchState)
                        }


                    }
                }


            }

        }
    }
}
