/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.repositories

import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.source.RealtimeSource
import com.dawnimpulse.wallup.utils.C
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-20 by Saksham
 *
 * @note Updates :
 */
object DatabaseRepository {
    private val NAME = "DatabaseRepository"

    /**
     * get trending images
     * @param timestamp
     * @param callback
     */
    fun getTrendingImages(timestamp: Int?, callback: (Any?, Any?) -> Unit) {
        if (timestamp != null) {
            val ref = FirebaseDatabase.getInstance().reference
                    .child(C.TRENDING)
                    .orderByChild(C.TIMESTAMP)
                    .startAt(timestamp.toDouble())
                    .limitToFirst(31)
            RealtimeSource.getDataOnce(ref) { error, response ->
                if (error != null)
                    callback(error, null)
                else {
                    response as DataSnapshot
                    var data = ArrayList<ImagePojo>()
                    for (snapshot in response.children){
                        data.add(snapshot.getValue(ImagePojo::class.java)!!)
                    }
                    data.removeAt(0)
                    callback(null, data)
                }
            }
        } else {
            val ref = FirebaseDatabase.getInstance().reference
                    .child(C.TRENDING)
                    .orderByChild(C.TIMESTAMP)
                    .limitToFirst(30)
            RealtimeSource.getDataOnce(ref) { error, response ->
                if (error != null)
                    callback(error, null)
                else {
                    response as DataSnapshot
                    var data = ArrayList<ImagePojo>()
                    for (snapshot in response.children) {
                        data.add(snapshot.getValue(ImagePojo::class.java)!!)
                    }
                    callback(null, data)
                }
            }
        }

    }
}