package com.example.exercise2_2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable as BD
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val videocall_Include = findViewById<View>(R.id.include_videocall)
        val notification_Include = findViewById<View>(R.id.include_notification)
        val voicecall_Include = findViewById<View>(R.id.include_voicecall)

        configViewElement(videocall_Include, R.drawable.video_call, "Video Call", have_frame = false)
        configViewElement(notification_Include, R.drawable.notification, "Notification", have_frame = false)
        configViewElement(voicecall_Include, R.drawable.voice_call, "Voice Call", have_frame = false)


        val inboxItem = findViewById<View>(R.id.item_inbox)
        val mapItem = findViewById<View>(R.id.item_map)
        val chatItem = findViewById<View>(R.id.item_chat)
        val reportItem = findViewById<View>(R.id.item_report)
        val calendertem = findViewById<View>(R.id.item_calendar)
        val tipsItem = findViewById<View>(R.id.item_tips)
        val settingsItem = findViewById<View>(R.id.item_settings)
        val otherItem = findViewById<View>(R.id.item_other)

        configViewElement(inboxItem, R.drawable.ic_1, "Inbox", have_frame = true)
        configViewElement(mapItem, R.drawable.ic_2, "Map", have_frame = true)
        configViewElement(chatItem, R.drawable.ic_3, "Chat", have_frame = true)
        configViewElement(reportItem, R.drawable.ic_4, "Report", have_frame = true)
        configViewElement(calendertem, R.drawable.ic_5, "Calendar", have_frame = true)
        configViewElement(tipsItem, R.drawable.ic_6, "Tips", have_frame = true)
        configViewElement(settingsItem, R.drawable.ic_7, "Settings", have_frame = true)
        configViewElement(otherItem, R.drawable.ic_8, "Other", have_frame = true)


        val bmp = BitmapFactory.decodeResource(resources, R.drawable.pro_trung)
        val radiusPx = dpToPx(100)
        val rounded = getRoundedCornerBitmap(bmp, radiusPx)

        val ivAvt = findViewById<ImageView>(R.id.iv_avt)
        ivAvt.scaleType = ImageView.ScaleType.CENTER_CROP
        ivAvt.setImageBitmap(rounded)
    }

    fun configViewElement(includeRoot: View?, iconRes: Int, text: String?, have_frame: Boolean) {
        if (includeRoot == null) return

        val fl = includeRoot.findViewById<FrameLayout?>(R.id.fl_element)
        val tv = includeRoot.findViewById<TextView?>(R.id.tv_element)
        val iv = includeRoot.findViewById<ImageView?>(R.id.iv_element)

        if (have_frame) {
            iv?.setImageResource(iconRes)
            if (text != null) tv?.text = text
        } else {
            fl?.setBackgroundResource(iconRes)
            if (text != null) tv?.text = text
        }
    }


    fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = 0xff424242.toInt()
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = pixels.toFloat()

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }
}