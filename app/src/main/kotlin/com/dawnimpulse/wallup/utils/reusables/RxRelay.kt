package com.dawnimpulse.wallup.utils.reusables

import com.jakewharton.rxrelay2.PublishRelay

val RxBus by lazy { PublishRelay.create<Void>() }
val RxBusPair by lazy { PublishRelay.create<Pair<String, Any>>() }