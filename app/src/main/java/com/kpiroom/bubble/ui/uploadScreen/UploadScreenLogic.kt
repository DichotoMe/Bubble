package com.kpiroom.bubble.ui.uploadScreen

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.R
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.bitmap.getCompressed
import com.kpiroom.bubble.util.constants.drw
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.progressState.livedata.alert
import com.kpiroom.bubble.util.progressState.livedata.alertAsync
import com.kpiroom.bubble.util.progressState.livedata.finishAsync
import com.kpiroom.bubble.util.progressState.livedata.loadAsync
import kotlinx.coroutines.Dispatchers

class UploadScreenLogic : CoreLogic() {

    companion object {
        private const val RENDER_MODE = "r"
    }

    val progress = MutableLiveData<ProgressState>()
    val bitmapPreview = MutableLiveData<Bitmap>()

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private lateinit var uploadUri: Uri

    val uploadClicked = MutableLiveData<Boolean>()
    val uploadComplete = MutableLiveData<Boolean>()

    fun renderPreview(uri: Uri) {
        uploadUri = uri
        progress.apply {
            AsyncProcessor(Dispatchers.IO) {
                loadAsync()
                val bitmap = drw(R.drawable.default_cover).toBitmap().run {
                    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                }
                PdfRenderer(app.contentResolver.openFileDescriptor(uri, RENDER_MODE))
                    .openPage(0).apply {
                        render(bitmap, null, null, RENDER_MODE_FOR_DISPLAY)
                        close()
                    }
                bitmapPreview.postValue(bitmap.getCompressed())
                finishAsync()
            } handleError {
                alertAsync(it.message)
            } runWith (bag)
        }
    }

    fun onUpload() {
        uploadClicked.value = true

        val title = title.value
        val description = description.value

        val thumbnail = bitmapPreview.value ?: return

        if (title.isNullOrBlank() || description.isNullOrBlank())
            progress.alert(str(R.string.upload_screen_empty_fields))
        else
            Source.apply {
                val uploadTimeMs = System.currentTimeMillis()
                val comicId = "${userPrefs.uuid}-$uploadTimeMs"

                progress.apply {
                    AsyncProcessor {
                        loadAsync()
                        api.apply {
                            uploadComic(comicId, uploadUri)
                            uploadComicData(
                                comicId,
                                Comic(
                                    comicId,
                                    title,
                                    uploadComicThumbnail(comicId, thumbnail).toString(),
                                    description,
                                    userPrefs.uuid,
                                    uploadTimeMs
                                )
                            )
                        }
                        finishAsync()
                        uploadComplete.postValue(true)
                    } handleError {
                        alertAsync(it.message)
                    } runWith (bag)
                }
            }
    }
}