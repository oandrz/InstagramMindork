package com.mindorks.bootcamp.instagram.data.remote

import com.mindorks.bootcamp.instagram.data.remote.request.DummyRequest
import com.mindorks.bootcamp.instagram.data.remote.request.LikeUnlikeBodyRequest
import com.mindorks.bootcamp.instagram.data.remote.request.LoginRequest
import com.mindorks.bootcamp.instagram.data.remote.request.SignupRequest
import com.mindorks.bootcamp.instagram.data.remote.response.DummyResponse
import com.mindorks.bootcamp.instagram.data.remote.response.GeneralResponse
import com.mindorks.bootcamp.instagram.data.remote.response.PostListResponse
import com.mindorks.bootcamp.instagram.data.remote.response.UserResponse
import io.reactivex.Single
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @POST(Endpoints.DUMMY)
    fun doDummyCall(
        @Body request: DummyRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): Single<DummyResponse>

    /*
     * Example to add other headers
     *
     *  @POST(Endpoints.DUMMY_PROTECTED)
        fun sampleDummyProtectedCall(
            @Body request: DummyRequest,
            @Header(Networking.HEADER_USER_ID) userId: String, // pass using UserRepository
            @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String, // pass using UserRepository
            @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
        ): Single<DummyResponse>
     */

    @POST(Endpoints.LOGIN)
    fun doLoginCall(
        @Body request: LoginRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<UserResponse>

    @POST(Endpoints.SIGN_UP)
    fun doSignUpCall(
        @Body request: SignupRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<UserResponse>


    @GET(Endpoints.ALL_FEED)
    fun fetchFeed(
            @Header(Networking.HEADER_USER_ID) userId: String,
            @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
            @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<PostListResponse>

    @PUT(Endpoints.LIKE_FEED)
    fun likeFeed(
        @Body request: LikeUnlikeBodyRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<GeneralResponse>

    @PUT(Endpoints.UNLIKE_FEED)
    fun unLikeFeed(
        @Body request: LikeUnlikeBodyRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<GeneralResponse>
}