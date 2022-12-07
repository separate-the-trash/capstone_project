package com.example.wecyclean2

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.wecyclean2.ui.theme.Wecyclean2Theme
import com.google.accompanist.permissions.*

class PermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Wecyclean2Theme {
            }
        }
    }
}


@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    rationaleMessage: String = "To use this app's functionalities, you need to give us the permission.",
) {
    if(isPermission == 1) {
        return
    }
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
        )
    )
    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale
            ) { permissionState.launchMultiplePermissionRequest() }
        },
        content = {
            /*   Content(
                   text = "PERMISSION GRANTED!",
                   showButton = false
               ) {}*/
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun HandleRequest(
    permissionState: MultiplePermissionsState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {

    if (permissionState.allPermissionsGranted) {
        isPermission=1
        content()
    } else {
        deniedContent(permissionState.shouldShowRationale)
    }
}



@Composable
fun Content(showButton: Boolean = true, onClick: () -> Unit) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            CustomDialogLocation(
                title = "권한 설정",
                desc = "어플리케이션에 필요한 권한을 허용해주세요.",
                enableLocation,
                onClick
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {

    if (shouldShowRationale) {

        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission Request",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Give Permission")
                }
            }
        )

    }
    else {
        Content(onClick = onRequestPermission)
    }

}

@Composable
fun CustomDialogLocation(
    title: String? = "Message",
    desc: String? = "Your Message",
    enableLocation: MutableState<Boolean>,
    onClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { enableLocation.value = false}
    ) {
        Box(
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                // .width(300.dp)
                // .height(164.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(25.dp,25.dp,25.dp,25.dp)
                )
                .verticalScroll(rememberScrollState())

        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(
                    text = title!!,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        //  .padding(top = 5.dp)
                        .fillMaxWidth(),
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                //.........................Text : description
                Text(
                    text = desc!!,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    letterSpacing = 1.sp,
                )
                //.........................Spacer
                Spacer(modifier = Modifier.height(24.dp))

                //.........................Button : OK button
                val cornerRadius = 16.dp
                val roundedCornerShape = RoundedCornerShape(topStart = 30.dp,bottomEnd = 30.dp)

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = c_main_green)
                ) {
                    Text(
                        text = "허용",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontFamily = notosanskr,
                        fontWeight = FontWeight.Normal,
                    )
                }


                //.........................Spacer
                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {enableLocation.value = false},
                ) {
                    Text(
                        text = "허용하지 않음",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontFamily = notosanskr,
                        fontWeight = FontWeight.Normal,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }
}