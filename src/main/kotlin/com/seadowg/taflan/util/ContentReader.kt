package com.seadowg.taflan.util

import android.net.Uri
import java.io.InputStream

interface ContentReader {
    fun read(uri: Uri): InputStream
}
