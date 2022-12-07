package com.example.wecyclean2

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

//테스트용 게시글 모델
data class DataModel(
    // on below line we are creating variables for name and job
    var userId: Int,
    var id: Int,
    var title:String,
    var body:String,
)

//게시글 모델
data class PostModel(
    var id: Int,
    var postid: Int,
    var userid: String,
    var title: String,
    var content: String,
    var regdate: String,
    var updatedate: String,
    var replys: Array<ReplyModel>,
)

//게시글 작성 모델
data class PostContentModel(
    var userid: String,
    var title: String,
    var content: String,
)

//댓글 모델 (get 모델)
data class  ReplyModel(
    var replyno: Int,
    var userid: String,
    var content: String,
    var regdate: String,
    var board: Int,
)

//댓글 작성모델 (post를 보낼때 사용되는 모델)
data class  ReplyContentModel(
    var userid: String,
    var content: String,
)


//유저 모델
data class AccountModel(
    var username: String,
    var password: String,
)

//유저 반환모델
data class AccountReturnModel(
    var id: Int,
    var username: String,
    var password: String,
    var role: String,
    var point: Int,

)

//마커 모델
data class MarkerModel(
    var id: Int,
    var address: String,
    var x: Double,
    var y: Double,
)

//포인트 모델(전송시)
data class PointModel(
    var reward:Int,
)
//포인트 반환모델
data class PointReturnModel(
    var id:Int,
    var point:Int,
    var message:String
)
// 서버 주소 안드로이드의 localhost가 10.0.2.2
//val base_url = "http://10.0.2.2:8080/"
val base_url = "http://172.30.1.44:8080/"


interface RetrofitAPI {

    @GET("map")
    fun  // on below line we are creating a method to post our data.
            getMap(): Call<List<MarkerModel>?>?

    // 모든 글 목록을 불러옵니다.
    @GET("board/list")
    fun
            getPost(): Call<List<PostModel>?>?
    @POST("account/register")
    fun
            register(@Body AccountModel: AccountModel?): Call<AccountModel?>?

    @POST("account/login")
    fun
            login(@Body AccountModel: AccountModel?): Call<AccountReturnModel?>?


    @POST("board/register")
    fun
            addpost(@Body PostContentModel: PostContentModel?): Call<Int?>?
    @POST("reply/register/{postid}")
    fun
            addreply(@Body ReplyContentModel: ReplyContentModel?,@Path("postid") postid: String): Call<Void?>?
    @POST("board/delete/{postid}/{userid}")
    fun
            deletepost(@Path("postid") postid: String,@Path("userid") userid: String): Call<Int?>?

    @POST("reward/usepoint/{userid}")
    fun
            usepoint(@Path("userid") userid: Int,@Body PointModel: PointModel?): Call<PointReturnModel?>?

}

// 회원가입
fun registerAccount(username:String,password:String,navController: NavController,context: Context) {



    Log.e("",username+password)


    val retrofit = Retrofit.Builder()
        .baseUrl(base_url) // 기본 API URI
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

    val call: Call<AccountModel?>? = retrofitAPI.register(AccountModel(username,password))

    call!!.enqueue(object : Callback<AccountModel?> {

        override fun onResponse(
            call: Call<AccountModel?>?,
            response: Response<AccountModel?>
        ) {
            if (response.isSuccessful) {
                Log.e("","회원가입 성공!")
                Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                navController.navigate("Login")

            }
            else
            {
                Toast.makeText(context, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show()
                Log.e("","중복 아이디!")
            }
        }

        override fun onFailure(call: Call<AccountModel?>?, t: Throwable) {
            Log.d("log",t.message.toString())
            Log.e("","회원가입 실패!")

        }
    })


}

//로그인

fun loginAccount(username:String,password:String,navController:NavController,context:Context) {
    Log.e("",username+password)
    val retrofit = Retrofit.Builder()
        .baseUrl(base_url) // 기본 API URI
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

    val call: Call<AccountReturnModel?>? = retrofitAPI.login(AccountModel(username,password))
    call!!.enqueue(object : Callback<AccountReturnModel?> {
        override fun onResponse(
            call: Call<AccountReturnModel?>?,
            response: Response<AccountReturnModel?>,
        ) {
            if (response.isSuccessful) {

                Log.e("","로그인 성공!")
                //Log.e("",response.body()!!.point.toString())
                //Log.e("",response.headers().toString()) 헤더정보
                uid_id=response.body()!!.id // id 정보 클라이언트에 저장
                uid=username
                upoint=response.body()!!.point // 포인트 정보 클라이언트에 저장
                Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                navController.navigate("Home")

            }
            else{
                Toast.makeText(context, "잘못된 아이디 혹은 비밀번호입니다.", Toast.LENGTH_SHORT).show()

            }
        }

        override fun onFailure(call: Call<AccountReturnModel?>?, t: Throwable) {
            Log.d("log",t.message.toString())
            Log.e("","로그인 실패!")

        }
    })

}


fun getPostList(postList:MutableList<PostModel>) {
    val retrofit = Retrofit.Builder()
        .baseUrl(base_url) // 기본 API URI
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

    val call: Call<List<PostModel>?>? = retrofitAPI.getPost()

    call!!.enqueue(object : Callback<List<PostModel>?> {
        override fun onResponse(
            call: Call<List<PostModel>?>?,
            response: Response<List<PostModel>?>?
        ) {
            if (response != null) {
                if (response.isSuccessful) {
                    postList.clear()
                    // 게시글의 목록을 불러오고 게시글 리스트에 추가합니다.
                    for (i in 0 until response.body()!!.size) {
                        postList.add(response.body()!!.get(i))

                    }
                    Log.e("","리스트에 추가!")

                }
            }
        }

        override fun onFailure(call: Call<List<PostModel>?>?, t: Throwable) {
            Log.e("","리스트에 추가실패!")
        }
    })
}

// 게시글작성
fun addPost(userid:String,title:String,content:String) {



    Log.e("",userid+title+content)
    val retrofit = Retrofit.Builder()
        .baseUrl(base_url) // 기본 API URI
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

    val call: Call<Int?>? = retrofitAPI.addpost(PostContentModel(userid,title,content))

    call!!.enqueue(object : Callback<Int?> {

        override fun onResponse(
            call: Call<Int?>?,
            response: Response<Int?>
        ) {
            if (response.isSuccessful) {

                Log.e("","글추가 성공!")

            }
        }

        override fun onFailure(call: Call<Int?>?, t: Throwable) {
            Log.d("log",t.message.toString())
            Log.e("","글추가 실패!")
        }
    })
}

// 게시글삭제
fun deletePost(postid:String,userid:String,postList: MutableList<PostModel>) {



    val retrofit = Retrofit.Builder()
        .baseUrl(base_url) // 기본 API URI
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

    val call: Call<Int?>? = retrofitAPI.deletepost(postid,userid)

    call!!.enqueue(object : Callback<Int?> {

        override fun onResponse(
            call: Call<Int?>?,
            response: Response<Int?>
        ) {
            if (response.isSuccessful) {
                Log.e("","글삭제 성공!")
                getPostList(postList)

            }
        }

        override fun onFailure(call: Call<Int?>?, t: Throwable) {
            Log.d("log",t.message.toString())
            Log.e("","글삭제 실패!")
        }
    })
}

// 댓글작성
fun addReply(postid:String,userid:String,content:String,postList: MutableList<PostModel>) {



    Log.e("",postid+userid+content)
    val retrofit = Retrofit.Builder()
        .baseUrl(base_url) // 기본 API URI
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

    val call: Call<Void?>? = retrofitAPI.addreply(ReplyContentModel(userid,content),postid)

    call!!.enqueue(object : Callback<Void?> {

        override fun onResponse(
            call: Call<Void?>?,
            response: Response<Void?>
        ) {
            if (response.isSuccessful) {

                Log.e("","댓글추가 성공!")
                getPostList(postList)
            }
        }

        override fun onFailure(call: Call<Void?>?, t: Throwable) {
            Log.d("log",t.message.toString())
            Log.e("","댓글추가 실패!")
        }
    })
}

fun getMap(markerList:MutableList<MarkerModel>,navController: NavController) {
    val retrofit = Retrofit.Builder()
        .baseUrl(base_url) // 기본 API URI
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

    val call: Call<List<MarkerModel>?>? = retrofitAPI.getMap()

    call!!.enqueue(object : Callback<List<MarkerModel>?> {
        override fun onResponse(
            call: Call<List<MarkerModel>?>?,
            response: Response<List<MarkerModel>?>?
        ) {
            if (response != null) {
                if (response.isSuccessful) {
                    markerList.clear()
                    // 게시글의 목록을 불러오고 게시글 리스트에 추가합니다.
                    for (i in 0 until response.body()!!.size) {
                        markerList.add(response.body()!!.get(i))

                    }
                    Log.e("","지도마커 추가!")
                    navController.navigate("Map")


                }
            }
        }

        override fun onFailure(call: Call<List<MarkerModel>?>?, t: Throwable) {
            Log.e("","지도마커 추가실패!")
        }
    })
}


fun usepoint(userid_id:Int,reward:Int,navController:NavController,context:Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl(base_url) // 기본 API URI
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

    val call: Call<PointReturnModel?>? = retrofitAPI.usepoint(userid_id,PointModel(reward))
    call!!.enqueue(object : Callback<PointReturnModel?> {
        override fun onResponse(
            call: Call<PointReturnModel?>?,
            response: Response<PointReturnModel?>,
        ) {
            if (response.isSuccessful) {

                Log.e("","교환 성공!")
                //Log.e("",response.body()!!.point.toString())
                //Log.e("",response.headers().toString()) 헤더정보
                upoint=response.body()!!.point // 포인트 정보 클라이언트에 저장
                Toast.makeText(context, "성공적으로 교환되었습니다", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                navController.navigate("Reward")

            }
            else{
                Toast.makeText(context, "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()

            }
        }

        override fun onFailure(call: Call<PointReturnModel?>?, t: Throwable) {
            Log.d("log",t.message.toString())
            Log.e("","로그인 실패!")

        }
    })

}