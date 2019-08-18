package com.seadowg.taflan.util

import android.content.Context
import android.net.Uri
import java.io.InputStream

class AndroidContentReader(private val context: Context) : ContentReader {
    override fun read(uri: Uri): InputStream {
        return context.contentResolver.openInputStream(uri)!!
    }
}
