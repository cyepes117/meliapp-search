package com.meliapp.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meliapp.search.domain.entities.Product

@Composable
fun ProductListFragmentContent(
    query: String,
    products: List<Product>,
    onProductSelected: (Product) -> Unit,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search bar
        SearchBar(
            query = query,
            onQueryChanged = onQueryChanged,
            onClearQuery = onClearQuery
        )


        // Product list
        ProductList(
            products = products,
            onProductSelected = onProductSelected
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var queryState by remember { mutableStateOf(query) }

    // Obtain software keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current

    // Obtain view
    val view = LocalView.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Search icon
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    expanded = true
                }
        )

        // Search TextField
        TextField(
            value = queryState,
            onValueChange = {
                queryState = it
                onQueryChanged(it)
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .background(Color.Transparent),
            placeholder = { Text(text = "Search") },
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            trailingIcon = {
                if (queryState.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.clickable {
                            queryState = ""
                            onQueryChanged("")
                            onClearQuery()
                        }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Handle search action
                    onQueryChanged(queryState)
                    expanded = false

                    // Hide the keyboard
                    keyboardController?.hide()

                    // Reset focus to the view
                    view.clearFocus()
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                //textColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductSelected: (Product) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(products) { product ->
            ProductListItem(
                product = product,
                onProductSelected = onProductSelected
            )
        }
    }
}

@Composable
fun ProductListItem(
    product: Product,
    onProductSelected: (Product) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onProductSelected(product) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "Price: $${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
    ProductListFragmentContent (
        query = "Mock Query",
        products = listOf(
            Product(id = 1, name = "Product 1", description = "", price = 20.0),
            Product(id = 2, name = "Product 2", description = "", price = 20.0),
            // Add more mock products as needed
        ),
        onProductSelected = { /* Handle product selection in preview */ },
        onQueryChanged = { /* Handle query change in preview */ },
        onClearQuery = { /* Handle clearing query in preview */ }
    )
}
