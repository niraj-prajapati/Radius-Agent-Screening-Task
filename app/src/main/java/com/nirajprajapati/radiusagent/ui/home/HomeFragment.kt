package com.nirajprajapati.radiusagent.ui.home

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.nirajprajapati.radiusagent.data.FacilityRealm
import com.nirajprajapati.radiusagent.data.OptionRealm
import com.nirajprajapati.radiusagent.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var optionsAdapter: OptionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindLiveData()
    }

    private fun initViews() {
        optionsAdapter = OptionsAdapter()

        binding.apply {
            rvSelectedFacilities.layoutManager = GridLayoutManager(requireContext(), 3)
            rvSelectedFacilities.adapter = optionsAdapter

            btnReset.setOnClickListener {
                homeViewModel.facilitiesLiveData.value?.let { facilities ->
                    buildFacilitySelectionUI(facilities)
                    optionsAdapter.setData(listOf())
                }
            }
        }
    }

    private fun bindLiveData() {
        homeViewModel.apply {
            facilitiesLiveData.observe(viewLifecycleOwner) { facilities ->
                buildFacilitySelectionUI(facilities)
            }

            loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
                binding.progress.isVisible = isLoading
                binding.containerUI.isVisible = !isLoading
            }

            errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buildFacilitySelectionUI(facilities: List<FacilityRealm>) {
        binding.apply {
            facilitiesContainer.removeAllViews()
            val selectedOptions = mutableListOf<OptionRealm>()
            val chipGroups = mutableListOf<ChipGroup>()
            val chipIdOptionIdMap = mutableMapOf<Int, String>()
            for (facility in facilities) {
                val facilityNameTextView = TextView(requireContext())
                facilityNameTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Medium)
                facilityNameTextView.text = facility.name
                facilitiesContainer.addView(facilityNameTextView)

                val chipGroup = ChipGroup(requireContext())
                chipGroup.apply {
                    isSelectionRequired = true
                    isSingleSelection = true
                }
                chipGroups.add(chipGroup)
                facilitiesContainer.addView(chipGroup)

                for (option in facility.options) {
                    val chip = Chip(
                        ContextThemeWrapper(
                            requireContext(),
                            com.google.android.material.R.style.Widget_Material3_Chip_Filter
                        )
                    )
                    chip.text = option.name
                    chip.isCheckable = true
                    chip.isClickable = true
                    chip.isFocusable = true

                    val chipId = View.generateViewId()
                    chip.id = chipId
                    chipIdOptionIdMap[chipId] = option.id

                    chip.setOnCheckedChangeListener { _, isChecked ->
                        val excludedOptionId = option.exclusionOptionId
                        val excludedChipId =
                            chipIdOptionIdMap.entries.firstOrNull { it.value == excludedOptionId }?.key
                        val excludedChip =
                            excludedChipId?.let { binding.root.findViewById<Chip>(it) }
                        excludedChip?.isEnabled =
                            !(isChecked && option.exclusionOptionId != "0")

                        when {
                            isChecked -> {
                                selectedOptions.add(option)
                            }

                            else -> {
                                selectedOptions.remove(option)
                            }
                        }

                        optionsAdapter.setData(selectedOptions)
                    }

                    chipGroup.addView(chip)
                }
            }

            for (i in 0 until chipGroups.size) {
                val chipGroup = chipGroups[i]
                chipGroup.setOnCheckedChangeListener { group, _ ->
                    if (group.checkedChipId != View.NO_ID) {
                        for (j in i + 1 until chipGroups.size) {
                            chipGroups[j].clearCheck()
                        }
                    }
                }
            }
        }
    }
}