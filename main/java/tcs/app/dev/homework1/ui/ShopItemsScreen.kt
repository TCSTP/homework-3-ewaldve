package tcs.app.dev.homework1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tcs.app.dev.homework1.data.Euro
import tcs.app.dev.homework1.data.Item
import tcs.app.dev.homework1.data.MockData
import tcs.app.dev.homework1.data.Shop


@Composable
fun ShopItemsScreen(
    shop: Shop,
    modifier: Modifier = Modifier,
    onAddToCart: (Item) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            shop.items.toList(),
            key = { it.id }) {
            item -> val price: Euro = shop.prices[item] ?: return@items

            ShopItemRow(
                item = item,
                price = price,
                onAddToCart = { onAddToCart(item) }
            )
        }
    }
}

@Composable
fun ShopItemRow(
    item: Item,
    price: Euro,
    modifier: Modifier = Modifier,
    onAddToCart: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = MockData.getImage(item)),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(8.dp))
            Text(text = stringResource(id = MockData.getName(item)))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = price.toString())
            Spacer(Modifier.width(8.dp))
            Button(onClick = onAddToCart) {
                Text("Add")
            }
        }
    }
}
