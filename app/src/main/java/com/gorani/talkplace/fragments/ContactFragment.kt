package com.gorani.talkplace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gorani.talkplace.R
import com.gorani.talkplace.common.URL_MY_GITHUB
import com.gorani.talkplace.databinding.FragmentContactBinding

class ContactFragment: Fragment() {

    private lateinit var binding: FragmentContactBinding

    private val webView by lazy {
        binding.webView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWebView()

    }

    private fun initWebView() {
        webView.apply {
            settings.javaScriptEnabled = true
            loadUrl(URL_MY_GITHUB)
        }

    }
}