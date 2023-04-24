package com.drmontoya.openweather.presentation.fragments.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drmontoya.openweather.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setupViewModelObservers()
        setupOnClickListeners()
        return binding.root
    }

    private fun setupOnClickListeners() {
        binding.apiLanguageButton.setOnClickListener {
            displayDialog(SELECT_A_LANGUAGE, SettingsViewModel.LANGUAGES, ({ selectedItem ->
                viewModel.selectApiLanguge(selectedItem)
            }))
        }
    }

    private fun displayDialog(
        title: String,
        list: List<String>,
        function: (selectedItem: String) -> Unit
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setItems(list.toTypedArray()) { _, which ->
            function(list[which])
        }
        builder.show()
    }

    private fun setupViewModelObservers() {
        viewModel.apiLanguage.observe(viewLifecycleOwner) {
            viewModel.modifyApiLanguage(requireContext())
        }
    }

    companion object {
        const val SELECT_A_LANGUAGE = "Select a language"
    }

}