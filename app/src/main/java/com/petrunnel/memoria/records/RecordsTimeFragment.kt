package com.petrunnel.memoria.records

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.petrunnel.memoria.PreferenceHelper
import com.petrunnel.memoria.databinding.FragmentRecordsListBinding

class RecordsTimeFragment: Fragment() {
    private var _binding: FragmentRecordsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecordTimeAdapter(requireContext())
        binding.root.adapter = adapter
        binding.root.background = ColorDrawable(PreferenceHelper(requireContext()).loadBackground())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}