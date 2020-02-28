/**
 * ISC License
 *
 * Copyright 2020, Saksham (DawnImpulse)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.dawnimpulse.wallup.network.controller

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @info - realtime database handling
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-02-27 by Saksham
 * @note Updates :
 */
object RealtimeCtrl {

    /**
     * set value in database
     *
     * @param ref  - firebase db reference
     * @param value - value to set
     */
    suspend fun setValue(ref: DatabaseReference, value: Any): Void{
        return ref.setValue(value).await()
    }

    /**
     * get value from database
     *
     * @param ref - firebase db ref
     */
    suspend fun <T>getValue(ref: DatabaseReference, clazz: Class<T>) = suspendCoroutine<T?> { continuation ->
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                continuation.resumeWithException(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                continuation.resume(p0.getValue(clazz))
            }

        })
    }
}