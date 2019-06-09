/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package org.sourcei.wallup.deprecated.utils


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-09-20 by Saksham
 *
 * @note Updates :
 */
object ML {
    private val NAME = "ML"

    // predicting image labels
    /*fun labels(bitmap: Bitmap, callback: (List<FirebaseVisionLabel>) -> Unit) {
        val options = FirebaseVisionLabelDetectorOptions.Builder()
                .setConfidenceThreshold(0.6f)
                .build()

        val detector = FirebaseVision.getInstance()
                .visionLabelDetector

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        detector
                .detectInImage(image)
                .addOnSuccessListener {
                    callback(it)
                }
                .addOnFailureListener {
                    L.e(NAME, "Firebase image recognition error")
                    L.e(NAME, it.message!!)
                }

    }*/
}