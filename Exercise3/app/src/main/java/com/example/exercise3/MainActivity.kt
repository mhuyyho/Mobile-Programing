package com.example.exercise3

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // khai báo (giữ nguyên tên biến của bạn)
    private lateinit var listView: ListView
    private lateinit var edValue: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnRemove: Button

    private lateinit var arrayList: ArrayList<MonHoc>
    private lateinit var adapter: MonHocAdapter

    // vị trí item đang chọn trong arrayList (-1 = chưa chọn)
    private var selectedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_customadapterlistview)

        // ánh xạ view
        listView = findViewById(R.id.lv)
        edValue = findViewById(R.id.et_value)
        btnAdd = findViewById(R.id.bt_add)
        btnUpdate = findViewById(R.id.bt_update)
        btnRemove = findViewById(R.id.bt_remove)

        // dữ liệu
        arrayList = ArrayList()
        arrayList.add(MonHoc("Java", "Java", R.drawable.java))
        arrayList.add(MonHoc("C#", "C# 1", R.drawable.cplusplus))
        arrayList.add(MonHoc("PHP", "PHP 1", R.drawable.php))
        arrayList.add(MonHoc("Kotlin", "Kotlin 1", R.drawable.kotlin))
        arrayList.add(MonHoc("Dart", "Dart 1", R.drawable.dart))

        // tạo adapter (dùng lớp Java của bạn)
        adapter = MonHocAdapter(this@MainActivity, R.layout.row_monhoc, arrayList as List<MonHoc>)

        // thêm header (nếu bạn muốn header cuộn cùng list)
        val headerView = layoutInflater.inflate(R.layout.row_monhoc, listView, false)
        // tuỳ chỉnh header (không bắt buộc)
        val tvNameHeader = headerView.findViewById<TextView>(R.id.tv_name)
        val ivPicHeader = headerView.findViewById<ImageView>(R.id.iv_pic)
        tvNameHeader.text = "Header - Danh sách môn"
        ivPicHeader.setImageResource(R.drawable.activity_transparent)
        // addHeaderView phải thực hiện trước setAdapter
        listView.addHeaderView(headerView)

        // set adapter cho ListView
        listView.adapter = adapter

        // xử lý click (lưu ý: vị trí cần trừ header count)
        listView.setOnItemClickListener { parent, view, position, id ->
            val realPos = position - listView.headerViewsCount
            if (realPos >= 0 && realPos < arrayList.size) {
                val item = arrayList[realPos]
                selectedPosition = realPos
                edValue.setText(item.getName())
                Toast.makeText(this@MainActivity,
                    "Vị trí: $realPos - ${item.getName()}",
                    Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val realPos = position - listView.headerViewsCount
            if (realPos >= 0 && realPos < arrayList.size) {
                val item = arrayList[realPos]
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Xóa")
                    .setMessage("Bạn có muốn xóa ${item.getName()} ?")
                    .setPositiveButton("Xóa") { _, _ ->
                        arrayList.removeAt(realPos)
                        // thông báo adapter cập nhật
                        adapter.notifyDataSetChanged()
                        if (selectedPosition == realPos) selectedPosition = -1
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
                true
            } else {
                false
            }
        }

        // nút Add
        btnAdd.setOnClickListener {
            val text = edValue.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "Nhập tên trước khi thêm", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            arrayList.add(MonHoc(text, text, R.drawable.activity_transparent))
            adapter.notifyDataSetChanged()
            edValue.text.clear()
            selectedPosition = arrayList.size - 1
            listView.smoothScrollToPosition(selectedPosition + listView.headerViewsCount)
        }

        // nút Update
        btnUpdate.setOnClickListener {
            val text = edValue.text.toString().trim()
            if (selectedPosition < 0 || selectedPosition >= arrayList.size) {
                Toast.makeText(this, "Chọn 1 item để cập nhật", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (text.isEmpty()) {
                Toast.makeText(this, "Nhập tên mới", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val item = arrayList[selectedPosition]
            item.setName(text)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show()
        }

        // nút Remove (xóa item đang chọn)
        btnRemove.setOnClickListener {
            if (selectedPosition < 0 || selectedPosition >= arrayList.size) {
                Toast.makeText(this, "Chọn 1 item để xóa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val item = arrayList[selectedPosition]
            AlertDialog.Builder(this)
                .setTitle("Xóa")
                .setMessage("Bạn có muốn xóa ${item.getName()}?")
                .setPositiveButton("Xóa") { _, _ ->
                    arrayList.removeAt(selectedPosition)
                    adapter.notifyDataSetChanged()
                    selectedPosition = -1
                    edValue.text.clear()
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
    }
}