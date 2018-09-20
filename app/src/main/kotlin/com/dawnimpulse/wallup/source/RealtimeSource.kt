package com.dawnimpulse.wallup.source

import com.google.firebase.database.*

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

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-20 by Saksham
 *
 * @note Updates :
 */

object RealtimeSource {

    /**
     * Get data from realtime database once
     * @param reference
     * @param callback
     */
    fun getDataOnce(reference: DatabaseReference, callback: (Any?, Any?) -> Unit) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                callback(null, p0)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback(p0.toString(), null)
            }
        })
    }


    /**
     * Get data from realtime database once (Query)
     * @param reference
     * @param callback
     */
    fun getDataOnce(reference: Query, callback: (Any?, Any?) -> Unit) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                callback(null, p0)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback(p0.toString(), null)
            }
        })
    }
}