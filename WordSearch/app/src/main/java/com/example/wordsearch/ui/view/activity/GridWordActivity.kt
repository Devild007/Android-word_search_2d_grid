package com.example.wordsearch.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.wordsearch.R
import com.example.wordsearch.databinding.ActivityGridWordBinding
import com.example.wordsearch.databinding.GridItemLayoutBinding
import com.example.wordsearch.device.utility.SessionManager
import com.example.wordsearch.device.utility.dialogOk
import com.example.wordsearch.device.utility.getString
import com.example.wordsearch.device.utility.showKeyboardForEditText
import com.example.wordsearch.ui.view.dialog.DialogOK
import timber.log.Timber
import java.util.Locale
import kotlin.math.min

class GridWordActivity : AppCompatActivity(), DialogOK.IDialogOKCallback {

    private var _binding: ActivityGridWordBinding? = null
    private val binding get() = _binding

    private var gridAdapter: GridAdapter? = null

    private lateinit var gridData: ArrayList<Char>

    private val session = SessionManager()

    private val rows = if (session.keyRow?.isNotEmpty() == true) session.keyRow.toString().toInt() else 0
    private val columns = if (session.keyColumn?.isNotEmpty() == true) session.keyColumn.toString().toInt() else 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGridWordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        init()
    }

    private fun init() = binding?.apply {
        Timber.e("keyAlphabets ===> ${session.keyAlphabets}")
        Timber.e("keyRow ===> ${session.keyRow}")
        Timber.e("keyColumn ===> ${session.keyColumn}")
        createGrid()

        btnSearch.setOnClickListener { searchBtnClicked() }

        etWord.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) searchBtnClicked()
            true
        }

        flBack.setOnClickListener {
            startActivity(Intent(this@GridWordActivity, MainActivity::class.java))
            finish()
        }

    }

    override fun closeDialog() {
        binding?.apply {
            showKeyboardForEditText(etWord)
            etWord.requestFocus()
        }
    }

    private fun searchBtnClicked() = binding?.apply {
        if (etWord.getString().isNotEmpty() && etWord.getString().length >= 2)
            highlightWord(etWord.getString().lowercase(Locale.getDefault()))
        else dialogOk(getString(R.string.attention), "Min length should be 2 or more", this@GridWordActivity)
    }

    private fun createGrid() = binding?.apply {
        gridData = ArrayList()
        for (i in 0 until rows * columns) {
            gridData.add(' ')
        }
        gridAdapter = GridAdapter(rows, columns)
        gridView.adapter = gridAdapter
        gridView.numColumns = columns
        // Set alphabets in the grid
        session.keyAlphabets?.let { inputAlphabets ->
            for (i in 0 until minOf(gridData.size, inputAlphabets.length)) {
                gridData[i] = inputAlphabets[i]
            }
            gridAdapter?.notifyDataSetChanged()
        }
    }

    private fun highlightWord(searchWord: String) {
        val searchWordLowerCase = searchWord.toLowerCase(Locale.getDefault())
        // Create a copy of the original grid data
        val copyOfGridData = ArrayList(gridData)
        // Highlight all occurrences of the word
        for ((index, char) in copyOfGridData.withIndex()) {
            if (char.toLowerCase() == searchWordLowerCase[0]) {
                val directions = arrayOf(
                    Pair(1, 0), // Right
                    Pair(0, 1), // Down
                    Pair(1, 1)  // Diagonal
                )

                for ((dx, dy) in directions) {
                    if (isWordInDirection(index, searchWordLowerCase, dx, dy)) {
                        // Highlight the word in the copy
                        for (i in searchWord.indices) {
                            val newIndex = index + i * (dx + (gridAdapter?.numColumns ?:0) * dy)
                            copyOfGridData[newIndex] = copyOfGridData[newIndex].uppercaseChar()
                        }
                    }
                }
            }
        }
        // Update the grid data with the modified copy
        gridData = copyOfGridData
        gridAdapter?.notifyDataSetChanged()
    }

    private fun isWordInDirection(startIndex: Int, searchWord: String, dx: Int, dy: Int): Boolean {
        val numRows = gridAdapter?.numRows ?:0
        val numColumns = gridAdapter?.numColumns ?:0
        val wordLength = searchWord.length

        if (startIndex % numColumns + dx * (wordLength - 1) >= numColumns ||
            startIndex / numColumns + dy * (wordLength - 1) >= numRows
        ) return false

        for (i in 0 until wordLength) {
            val index = startIndex + i * (dx + numColumns * dy)
            if (gridData[index].toLowerCase() != searchWord[i]) return false
        }
        return true
    }

    private inner class GridAdapter(val numRows: Int, val numColumns: Int) : BaseAdapter() {

        override fun getCount(): Int = numRows * numColumns

        override fun getItem(position: Int): Any = gridData[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val binding = GridItemLayoutBinding.inflate(layoutInflater)
            val char = gridData[position]
            binding.gridTextView.apply {
                text = char.toString()
                // Change text color if the character is uppercase (selected)
                setTextColor(resources.getColor(if (char.isUpperCase()) R.color.gold else R.color.text_color))
            }
            return binding.root
        }
    }

    override fun onDestroy() {
        gridAdapter = null
        _binding = null
        super.onDestroy()
    }
}
