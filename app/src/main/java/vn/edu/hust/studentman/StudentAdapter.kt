package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(private val students: MutableList<StudentModel>) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)

    fun bind(student: StudentModel) {
      textStudentName.text = student.studentName
      textStudentId.text = student.studentId

      // Xử lý sự kiện khi bấm nút chỉnh sửa
      imageEdit.setOnClickListener {
        val dialogView = LayoutInflater.from(itemView.context)
          .inflate(R.layout.layout_dialog, null)

        val editHoten = dialogView.findViewById<EditText>(R.id.edit_hoten)
        val editMssv = dialogView.findViewById<EditText>(R.id.edit_mssv)

        // Gán dữ liệu hiện tại vào EditText
        editHoten.setText(student.studentName)
        editMssv.setText(student.studentId)

        AlertDialog.Builder(itemView.context)
          .setTitle("Chỉnh sửa thông tin sinh viên")
          .setView(dialogView)
          .setPositiveButton("Cập nhật") { _, _ ->
            val hoten = editHoten.text.toString()
            val mssv = editMssv.text.toString()

            if (hoten.isNotEmpty() && mssv.isNotEmpty()) {
              val updatedStudent = StudentModel(hoten, mssv)
              students[adapterPosition] = updatedStudent
              notifyItemChanged(adapterPosition)
              Toast.makeText(itemView.context, "Chỉnh sửa thông tin thành công", Toast.LENGTH_SHORT).show()
            } else {
              Toast.makeText(itemView.context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
          }
          .setNegativeButton("Hủy", null)
          .show()
      }

      // Xử lý sự kiện khi bấm nút xóa
      imageRemove.setOnClickListener {
        // Lưu lại vị trí và thông tin của sinh viên bị xóa
        val removedStudent = students[adapterPosition]
        val removedPosition = adapterPosition

        AlertDialog.Builder(itemView.context)
          .setIcon(R.drawable.baseline_question_mark_24)
          .setTitle("Xóa sinh viên")
          .setMessage("Bạn có chắc chắn muốn xóa sinh viên này không?")
          .setPositiveButton("Xóa") { _, _ ->
            // Xóa mục ra khỏi danh sách và thông báo RecyclerView
            students.removeAt(removedPosition)
            notifyItemRemoved(removedPosition)

            // Hiển thị Snackbar để hoàn tác
            Snackbar.make(itemView, "Sinh viên đã bị xóa", Snackbar.LENGTH_LONG)
              .setAction("Hoàn tác") {
                // Thêm lại mục bị xóa vào vị trí cũ
                students.add(removedPosition, removedStudent)
                notifyItemInserted(removedPosition)
              }
              .show()
          }
          .setNegativeButton("Hủy", null)
          .show()
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]
    holder.bind(student)
  }
}
