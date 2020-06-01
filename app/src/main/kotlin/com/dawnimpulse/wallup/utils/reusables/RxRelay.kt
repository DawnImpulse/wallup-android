package com.dawnimpulse.wallup.utils.reusables

import com.jakewharton.rxrelay2.PublishRelay

val RxBus by lazy { PublishRelay.create<Void>() }
val RxBusType by lazy { PublishRelay.create<RxType>() }

data class RxType(
        val type:String,
        val data:Any
)