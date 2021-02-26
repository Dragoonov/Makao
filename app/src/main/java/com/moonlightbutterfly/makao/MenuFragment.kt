package com.moonlightbutterfly.makao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class MenuFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_menu, container, false)
        root.findViewById<Button>(R.id.start).setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace<GameFragment>(R.id.fragment_host)
            }
        }
        return root
    }
}