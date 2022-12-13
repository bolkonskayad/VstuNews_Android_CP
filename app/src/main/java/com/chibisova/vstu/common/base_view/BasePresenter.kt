package com.chibisova.vstu.common.base_view

import io.reactivex.rxjava3.disposables.CompositeDisposable

import moxy.MvpPresenter
import moxy.MvpView

//Этот фрагмент
// чтобы очищать потоки и выделять основные операции
abstract class BasePresenter<View : MvpView> : MvpPresenter<View>() {

    protected val compositeDisposable = CompositeDisposable()

}