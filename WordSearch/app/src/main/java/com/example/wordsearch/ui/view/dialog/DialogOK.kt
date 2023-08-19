package com.example.wordsearch.ui.view.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.wordsearch.databinding.DialogOkBinding

class DialogOK : DialogFragment() {

    private var _binding: DialogOkBinding? = null
    private val binding get() = _binding

    var callBack: IDialogOKCallback? = null

    private var title: String? = ""
    private var message: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Dialog)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val params = window!!.attributes
        params.dimAmount = 0.6f
        window.attributes = params
        window.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogOkBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() = binding?.apply {
        dialog?.setCancelable(false)
        setDialogPosition()
        txtTitle.text = title
        txtMessage.text = message
        btnOK.setOnClickListener { btnOKClicked() }
    }

    private fun btnOKClicked() {
        callBack?.closeDialog()
        dismissAllowingStateLoss()
    }

    private fun setDialogPosition() {
        val window = dialog!!.window
        window?.setGravity(Gravity.CENTER)
    }

    interface IDialogOKCallback {
        fun closeDialog()
    }

    companion object {
        fun newInstance(title: String, message: String, callBack: IDialogOKCallback) = DialogOK().apply {
            this.title = title
            this.message = message
            this.callBack = callBack
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}