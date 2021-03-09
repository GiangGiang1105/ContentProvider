package com.example.contentprovider

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var resolver: ContentResolver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resolver = contentResolver
        checkPermissionReadContact()
        showListData()
    }

    private fun queryData(): Cursor? = resolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID),
        null, null, null
    )

    private fun listData(): MutableList<String> {
        val list: MutableList<String> = mutableListOf()
        val cursor = queryData()
        while (cursor?.moveToNext()!!) {
            list.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
        }
        return list
    }

    private fun showListData() {
        val list: MutableList<String> = listData()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        list_view.adapter = adapter
    }

    private fun checkPermissionReadContact() {
        when (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            true -> requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 100)
        }
    }

}