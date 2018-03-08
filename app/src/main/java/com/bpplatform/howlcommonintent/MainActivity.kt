package com.bpplatform.howlcommonintent

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS,Manifest.permission.CAMERA),0)
        }
        button_alarm.setOnClickListener { setAlarm() }
        button_webpage.setOnClickListener { openWebPage() }
        button_sms.setOnClickListener { sendSMS() }
        button_email.setOnClickListener { openEmail() }
        button_contact.setOnClickListener { openContact() }
        button_album.setOnClickListener { openAlbum() }
        button_camera.setOnClickListener { openCamera() }
    }
    fun setAlarm(){
        var intent = Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE,"하울 알람 설정")
                .putExtra(AlarmClock.EXTRA_HOUR,7)
                .putExtra(AlarmClock.EXTRA_MINUTES,0)
        intent.resolveActivity(packageManager)
        startActivity(intent)
    }
    fun openWebPage(){
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naver.com"))
        intent.`package` = "com.android.chrome"
        startActivity(intent)
    }
    fun sendSMS(){
        var intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:010-1234-1234")
        intent.putExtra("sms_body","안녕하세요!")
        startActivity(intent)
    }
    fun openEmail(){
        var intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:you6878@icloud.com")
        intent.putExtra(Intent.EXTRA_SUBJECT,"문의 사항 및 불편사항")
        intent.putExtra(Intent.EXTRA_TEXT,"문의 내용 : ")
        startActivity(intent)
    }
    fun openContact(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent,0)
    }
    fun openAlbum(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,1)
    }
    fun openCamera(){
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            var contactUri = data!!.data
            var projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            var cursor = contentResolver.query(contactUri,projection,null,null,null)
            cursor.moveToFirst()
            var numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            var number = cursor.getString(numberIndex)
            println(number)

        }
        //앨범
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            var imageUri = data!!.data
            imageView_main.setImageURI(imageUri)
        }
        //카메라
        if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            var bitmap = data!!.getParcelableExtra<Bitmap>("data")
            imageView_main.setImageBitmap(bitmap)
        }
    }
}
