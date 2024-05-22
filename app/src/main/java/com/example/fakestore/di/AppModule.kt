package com.example.fakestore.di

import com.example.fakestore.data.repository.StoreRepository
import com.example.fakestore.data.repository.StoreRepositoryImplementation
import com.example.fakestore.data.source.remote.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppModule {//you can inject anything you want like database, location, api, repository
    val getApi : ApiInterface
    val getRepository : StoreRepository
    //if there are 2 or more repositories or api, you can add more
}

class AppModuleImpl: AppModule{

    override val getApi: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    override val getRepository: StoreRepository by lazy {
        StoreRepositoryImplementation(getApi)
    }

}