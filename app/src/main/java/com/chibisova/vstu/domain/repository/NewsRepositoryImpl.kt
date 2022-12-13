package com.chibisova.vstu.domain.repository

import com.chibisova.vstu.data.api.NewsApi
import com.chibisova.vstu.data.dto.mappers.New.NewDataMapper
import com.chibisova.vstu.data.dto.network.NetworkNews
import com.chibisova.vstu.data.storage.Storage
import com.chibisova.vstu.domain.model.News
import com.chibisova.vstu.domain.repository.NewsRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val networkMapper: NewDataMapper<NetworkNews>,
    private val storage: Storage
) : NewsRepository {

    //Получаем список новостей с сервера, добавляем в бд данные и берем поток уже с бд
    override fun getNews(): Single<List<News>> = newsApi
        .getNews()
        .map { networkMapper.transformList(it) }
        .doOnSuccess {
            storage.insertNews(it)
        }
        .flatMap { storage.getAllNews() }
        .subscribeOn(Schedulers.io())

    //Получаем список новостей, созданных локально
    override fun getUserNews(): Single<List<News>> = storage.getUserNews()

    override fun addNews(news: News) = storage.insertUserNew(news)

    override fun deleteNews(news: News): Completable {
        return storage.deleteNews(news)
    }
}

