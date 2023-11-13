package com.meliapp.search.presentation.list

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.meliapp.search.R
import com.meliapp.search.domain.entities.Product

@Composable
fun ProductListFragmentContent(
    query: String,
    products: List<Product>,
    onProductSelected: (Product) -> Unit,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    isLoading: Boolean = false,
    isApiError: Boolean = false,
    isNotFoundError: Boolean = false,
    isFatalError: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SearchBar(
            query = query,
            onQueryChanged = onQueryChanged,
            onClearQuery = onClearQuery,
        )

        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                )
            }

            products.isNotEmpty() -> {
                ProductList(
                    products = products,
                    onProductSelected = onProductSelected,
                )
            }

            isNotFoundError -> ErrorView(
                message = stringResource(id = R.string.product_list_text_no_products)
            )

            isApiError -> ErrorView(
                message = stringResource(id = R.string.product_list_text_no_connectivity)
            )
        }
    }

    if (isFatalError) {
        val context = LocalContext.current
        showToast(context, stringResource(id = R.string.product_list_text_general_error))
    }
}

@Composable
fun ErrorView(
    message: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var queryState by remember { mutableStateOf(query) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
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
                    onQueryChanged(queryState)
                    expanded = false
                    keyboardController?.hide()
                    view.clearFocus()
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.thumbnail)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.FillBounds,
                contentDescription = product.title,
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Price: $${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
    ProductListFragmentContent(
        query = "Mock Query",
        products = listOf(
            Product(id = "1", title = "Product 1", thumbnail = "", price = 20.0),
            Product(id = "2", title = "Product 2", thumbnail = "", price = 20.0),
        ),
        onProductSelected = { /* Handle product selection in preview */ },
        onQueryChanged = { /* Handle query change in preview */ },
        onClearQuery = { /* Handle clearing query in preview */ },
        isLoading = false,
    )
}

@Preview(showBackground = true)
@Composable
fun ProductListNotFoundErrorPreview() {
    ProductListFragmentContent(
        query = "Mock Query",
        products = emptyList(),
        onProductSelected = { /* Handle product selection in preview */ },
        onQueryChanged = { /* Handle query change in preview */ },
        onClearQuery = { /* Handle clearing query in preview */ },
        isNotFoundError = true,
        isLoading = false,
    )
}

@Preview(showBackground = true)
@Composable
fun ProductListApiErrorPreview() {
    ProductListFragmentContent(
        query = "Mock Query",
        products = emptyList(),
        onProductSelected = { /* Handle product selection in preview */ },
        onQueryChanged = { /* Handle query change in preview */ },
        onClearQuery = { /* Handle clearing query in preview */ },
        isApiError = true,
        isLoading = false,
    )
}

@Preview(showBackground = true)
@Composable
fun ProductListLoadingPreview() {
    ProductListFragmentContent(
        query = "",
        products = emptyList(),
        onProductSelected = { /* Handle product selection in preview */ },
        onQueryChanged = { /* Handle query change in preview */ },
        onClearQuery = { /* Handle clearing query in preview */ },
        isLoading = true,
    )
}
