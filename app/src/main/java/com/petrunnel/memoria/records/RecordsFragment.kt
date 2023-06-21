package com.petrunnel.memoria.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.petrunnel.memoria.R
import com.petrunnel.memoria.databinding.FragmentRecordsBinding
import java.lang.reflect.Field

class RecordsFragment(private val backgroundColor: String): Fragment() {
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!
    private val titles = arrayOf("по времени", "по очкам")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)

        try {
            val field: Field = binding.tabLayout::class.java.getDeclaredField("tabBackgroundResId")
            field.isAccessible = true
            field.set(binding.tabLayout,  parseColor(backgroundColor))
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerFragmentAdapter(this)
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.position?.let { binding.viewPager.setCurrentItem(it, false) }
            }
        })
    }

    internal class ViewPagerFragmentAdapter(fragment: RecordsFragment) : FragmentStateAdapter(fragment) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> RecordsTimeFragment()
                else -> RecordsPointFragment()
            }
        }

        override fun getItemCount(): Int = 2
    }

    private fun parseColor(string: String): Int {
        return when (string) {
            "#5C6BC0" -> R.color.blue
            "#8D6E63" -> R.color.brown
            "#FFA726" -> R.color.orange
            "#9CCC65" -> R.color.green
            "#29B6F6" -> R.color.light_blue
            "#7E57C2" -> R.color.purple
            "#FFEE58" -> R.color.yellow
            "#BDBDBD" -> R.color.gray
            "#78909C" -> R.color.blue_gray
            "#26A69A" -> R.color.teal
            "#F48FB1" -> R.color.pink
            else -> R.color.purple
        }
    }
}