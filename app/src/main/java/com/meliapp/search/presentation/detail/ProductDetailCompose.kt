package com.meliapp.search.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.meliapp.search.R
import com.meliapp.search.domain.entities.Product

@Composable
fun ProductDetail(
    product: Product,
    onProductSelected: (Product) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onProductSelected(product) }
            .background(Color.White)
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
                .fillMaxWidth()
                .height(200.dp)
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
                text = product.thumbnail,
                style = MaterialTheme.typography.bodyMedium
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
fun ProductDetailPreview() {
    ProductDetail(
        product = Product(id = "1", title = "Product 1", thumbnail = "https://http2.mlstatic.com/D_705584-MLA45585289613_042021-I.jpg", price = 20.0),
        onProductSelected = {}
    )
}
