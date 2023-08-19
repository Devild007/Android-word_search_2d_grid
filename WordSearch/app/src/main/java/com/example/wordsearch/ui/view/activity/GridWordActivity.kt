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

    private lateinit var gridAdapter: GridAdapter

    private lateinit var gridData: ArrayList<Char>

    private val session = SessionManager()

    val rows = if (session.keyRow?.isNotEmpty() == true) session.keyRow.toString().toInt() else 0
    val columns = if (session.keyColumn?.isNotEmpty() == true) session.keyColumn.toString().toInt() else 0

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
        val gridSize = Pair(gridAdapter.numRows, gridAdapter.numColumns)
        val minChars = calculateMinChars(gridSize)
        Timber.e("minChars ===> $minChars")
        if (etWord.getString().isNotEmpty() && etWord.getString().length >= minChars)
            highlightWord(etWord.getString().lowercase(Locale.getDefault()))
        else dialogOk(getString(R.string.attention), "Min length should be $minChars", this@GridWordActivity)
    }

    // Calculate the minimum characters required based on grid size
    private fun calculateMinChars(gridSize: Pair<Int, Int>): Int = min(gridSize.first, gridSize.second)


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
            gridAdapter.notifyDataSetChanged()
        }

    }


    private fun highlightWord(searchWord: String) {
        Timber.e("searchWord ===> $searchWord")
        val searchWordLowerCase = searchWord.toLowerCase()

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
                        for (i in 0 until searchWord.length) {
                            val newIndex = index + i * (dx + gridAdapter.numColumns * dy)
                            copyOfGridData[newIndex] = copyOfGridData[newIndex].uppercaseChar()
                        }
                    }
                }
            }
        }
        // Update the grid data with the modified copy
        gridData = copyOfGridData
        gridAdapter.notifyDataSetChanged()
    }

    private fun isWordInDirection(startIndex: Int, searchWord: String, dx: Int, dy: Int): Boolean {
        val numRows = gridAdapter.numRows
        val numColumns = gridAdapter.numColumns
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
        //private val selectedColor = ContextCompat.getColor(this@GridWordActivity, R.color.gold)

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

}