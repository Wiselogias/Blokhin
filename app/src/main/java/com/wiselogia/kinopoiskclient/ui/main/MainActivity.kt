package com.wiselogia.kinopoiskclient.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.wiselogia.kinopoiskclient.R
import com.wiselogia.kinopoiskclient.databinding.ActivityMainBinding
import com.wiselogia.kinopoiskclient.ui.favorite.FavoriteFragment

class MainActivity : AppCompatActivity() {
    private val mainFragment = MainFragment()
    private val favoriteFragment = FavoriteFragment()
    private var currentFragment : Fragment = mainFragment
    private lateinit var binding: ActivityMainBinding

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit()
        currentFragment = fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragment(mainFragment)
        binding.mainMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.action_fav -> setFragment(favoriteFragment)
                else -> setFragment(mainFragment)
            }
            true
        }
    }

}

