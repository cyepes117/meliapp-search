package com.meliapp.search.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.meliapp.search.R
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val viewModel: ProductEventRouter by lazy { get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()
        reactToViewModelEvents()
    }

    private fun reactToViewModelEvents() {
        lifecycleScope.launch {
            viewModel.viewModelEvents
                .collect { viewModelEvent ->
                    when (viewModelEvent) {
                        is ProductEventRouter.ViewModelEvent.ProductDetail.ShowProductDetail -> {
                            navController.navigate(NavRoutes.PRODUCT_DETAIL)
                        }

                        is ProductEventRouter.ViewModelEvent.ProductList.ShowList -> {
                            navController.navigate(NavRoutes.PRODUCT_LIST)
                        }
                    }
                }

        }
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        navController.graph = navController.createGraph(
            startDestination = NavRoutes.PRODUCT_LIST
        ) {
            fragment<ProductListFragment>(NavRoutes.PRODUCT_LIST)

            fragment<ProductDetailFragment>(NavRoutes.PRODUCT_DETAIL)
        }
    }

}

object NavRoutes {
    const val PRODUCT_LIST = "PRODUCT_LIST"
    const val PRODUCT_DETAIL = "PRODUCT_DETAIL"
}
