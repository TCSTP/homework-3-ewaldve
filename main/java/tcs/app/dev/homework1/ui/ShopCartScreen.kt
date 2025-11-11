package tcs.app.dev.homework1.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tcs.app.dev.R
import tcs.app.dev.homework1.data.Cart
import tcs.app.dev.homework1.data.Discount
import tcs.app.dev.homework1.data.Euro
import tcs.app.dev.homework1.data.Item
import tcs.app.dev.homework1.data.MockData
import tcs.app.dev.homework1.data.decrementItem
import tcs.app.dev.homework1.data.plus
import tcs.app.dev.homework1.data.minus
import tcs.app.dev.homework1.data.times


@Composable
fun CartItemRow(
    item: Item,
    amount: UInt,
    pricePerItem: Euro,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rowTotal = pricePerItem * amount

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
            Column {
                Text(text = stringResource(id = MockData.getName(item)))
                Text(text = "x $amount")
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = rowTotal.toString(), modifier = Modifier.padding(end = 8.dp))
            IconButton(onClick = onDecrease) {
                Text(stringResource(id = R.string.item_minus_one))
            }
            Text(amount.toString(), modifier = Modifier.padding(horizontal = 4.dp))
            IconButton(onClick = onIncrease) {
                Text(stringResource(id = R.string.item_plus_one))
            }
        }
    }
}

@Composable
fun ShopCartScreen(
    cart: Cart,
    modifier: Modifier = Modifier,
    onUpdateCart: (Cart) -> Unit,
    onNavigateBack: () -> Unit
) {
    BackHandler { onNavigateBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (cart.items.isEmpty() && cart.discounts.isEmpty()) {
            Text(stringResource(id = R.string.empty_cart))
            return
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            cart.items.forEach { (item, amount) ->
                val pricePerItem = cart.shop.prices[item] ?: return@forEach

                item(key = item.id) {
                    CartItemRow(
                        item = item,
                        amount = amount,
                        pricePerItem = pricePerItem,
                        onIncrease = {
                            if (amount < 99u) {
                                onUpdateCart(cart + item)
                            }
                        },
                        onDecrease = {
                            onUpdateCart(cart.decrementItem(item))
                        }
                    )
                }
            }

            if (cart.discounts.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.label_discounts),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                cart.discounts.forEach { discount: Discount ->
                    item(key = discount.hashCode()) {

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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = label)
                            Button(onClick = { onUpdateCart(cart - discount) }) {
                                Text(stringResource(id = R.string.description_remove_from_cart))
                            }
                        }
                    }
                }

            }
        }
    }
}
