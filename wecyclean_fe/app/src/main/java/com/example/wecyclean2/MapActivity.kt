package com.example.wecyclean2

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.wecyclean2.ui.theme.Wecyclean2Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}





@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun getPermission() {
    Log.e("","permissions")
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )


    if (locationPermissionsState.allPermissionsGranted) {
        Log.e("","got permissions")
    } else {
        Log.e("","fail permission")
        permissionModal(locationPermissionsState)
//        Column {
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Button(onClick = {  }) {
//                Text("buttonText")
//            }
//        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun permissionModal(locationPermissionsState: MultiplePermissionsState) {
    Log.e(""," permission modal")
    locationPermissionsState.launchMultiplePermissionRequest()
//    locationPermissionsState.launchMultiplePermissionRequest()
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun Maps(navController: NavController,markerList:MutableList<MarkerModel>) {

    var currentLat = 0.0
    var currentLng = 0.0
    var syu = LatLng(37.6435824, 127.1063459)


    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(currentLat,currentLng), 15f)
    }


    //현재 위치를 받아옵니다
    var fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)



    fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
        override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token

        override fun isCancellationRequested() = false
    })
        .addOnSuccessListener {
            if (it == null)
                Log.e("","위치연동 실패")
                //Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
            else {
                currentLat = it.latitude
                currentLng = it.longitude
                Log.e("",currentLat.toString()+currentLng.toString())

                cameraPositionState.move(CameraUpdateFactory.newLatLng(LatLng(currentLat,currentLng)))
            }

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
                        text = "내 주변 쓰레기통",
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



//                Text(
//                    text = "현재위치",
//                    style = TextStyle(
//                        platformStyle = PlatformTextStyle(
//                            includeFontPadding = false
//                        )
//                    ),
//                    color= Color.White,
//                    fontFamily = notosanskr ,
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 18.sp
//                )
//                Text(
//                    text = "현재위치가 표시됩니다",
//                    style = TextStyle(
//                        platformStyle = PlatformTextStyle(
//                            includeFontPadding = false
//                        )
//                    ),
//                    color= Color.White,
//                    fontFamily = notosanskr ,
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 18.sp
//                )
            }
            Column(Modifier.weight(0.8f)) {


                Box(Modifier.fillMaxSize()) {
                    googleMap(markerList,cameraPositionState)
                }

            }
        }
    }
}

@Composable
fun googleMap(markerList:MutableList<MarkerModel>,cameraPositionState:CameraPositionState) {
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }
    GoogleMap(
        properties = mapProperties,
        uiSettings = uiSettings,
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
        markerList.forEach { position ->
            Marker(
                state = MarkerState(
                    position = LatLng(
                        position.y,
                        position.x
                    )
                ),title = position.address
            )
        }
    }
}