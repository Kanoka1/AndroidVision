package com.example.marinab.testapplication

import android.graphics.Bitmap
import org.json.JSONObject

class ImageModel {
    var imagePhoto : Bitmap? = null
    var docType : DocTypes? = null

    constructor(jsonObject: JSONObject) {
    }
}