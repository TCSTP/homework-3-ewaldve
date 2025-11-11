package tcs.app.dev.homework1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tcs.app.dev.R
import tcs.app.dev.homework1.data.Cart
import tcs.app.dev.homework1.data.Discount
import tcs.app.dev.homework1.data.MockData

@Composable
fun DiscountRow(
    discount: Discount,
    alreadyInCart: Boolean,
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
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))

            val label = when (discount) {
                is Discount.Percentage -> stringResource(
                    id = R.string.percentage_off,
                    discount.value.toString()
                )

                is Discount.Fixed -> stringResource(
                    id = R.string.amount_off,
                    discount.amount.toString()
                )

                is Discount.Bundle -> stringResource(
                    id = R.string.pay_n_items_and_get,
                    discount.amountItemsPay.toString(),
                    stringResource(MockData.getName(discount.item)),
                    discount.amountItemsGet.toString()
                )
            }

            Column {
                Text(text = label)
                if (alreadyInCart) {
                    Text(
                        text = stringResource(id = R.string.in_cart),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Button(
            onClick = onAddToCart,
            enabled = !alreadyInCart
        ) {
            Text(if (alreadyInCart) stringResource(id = R.string.discount_added_to_cart) else stringResource(id = R.string.add_discount_to_cart))
        }
    }
}

@Composable
fun ShopDiscountScreen(
    availableDiscounts: List<Discount>,
    cart: Cart,
    modifier: Modifier = Modifier,
    onAddDiscount: (Discount) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.title_discounts),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(availableDiscounts) { discount ->
                val alreadyInCart = cart.discounts.contains(discount)

                DiscountRow(
                    discount = discount,
                    alreadyInCart = alreadyInCart,
                    onAddToCart = {
                        if (!alreadyInCart) {
                            onAddDiscount(discount)
                        }
                    }
                )
            }
        }
    }
}
