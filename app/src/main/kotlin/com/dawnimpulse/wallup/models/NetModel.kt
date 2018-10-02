/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package com.dawnimpulse.wallup.models

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.dawnimpulse.wallup.pojo.SlackPojo
import com.dawnimpulse.wallup.repositories.NetRepository

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-26 by Saksham
 *
 * @note Updates :
 */
class NetModel(val lifecycle: Lifecycle) {

    // post bug / feature to slack
    fun postSlack(type: String, data: SlackPojo, callback: (Any?, Any?) -> Unit) {
        NetRepository.postSlack(type, data) { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                var once = true
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onResume() {
                    if (once) {
                        callback(e, r)
                        once = false
                    }
                }
            })

        }
    }
}