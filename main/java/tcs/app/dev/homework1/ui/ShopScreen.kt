package tcs.app.dev.homework1.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import tcs.app.dev.homework1.data.Cart
import tcs.app.dev.homework1.data.Discount
import tcs.app.dev.homework1.data.Shop
import androidx.compose.ui.res.stringResource
import tcs.app.dev.R
import tcs.app.dev.homework1.data.plus
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


/**
 * # Homework 3 — Shop App
 *
 * Build a small shopping UI with ComposeUI using the **example data** from the
 * `tcs.app.dev.homework.data` package (items, prices, discounts, and ui resources).
 * The goal is to implement three tabs: **Shop**, **Discounts**, and **Cart**.
 *
 * ## Entry point
 *
 * The composable function [ShopScreen] is your entry point that holds the UI state
 * (selected tab and the current `Cart`).
 *
 * ## Data
 *
 * - Use the provided **example data** and data types from the `data` package:
 *   - `Shop`, `Item`, `Discount`, `Cart`, and `Euro`.
 *   - There are useful resources in `res/drawable` and `res/values/strings.xml`.
 *     You can add additional ones.
 *     Do **not** hard-code strings in the UI!
 *
 * ## Requirements
 *
 * 1) **Shop item tab** //done
 *    - Show all items offered by the shop, each row displaying:
 *      - item image + name,
 *      - item price,
 *      - an *Add to cart* button.
 *    - Tapping *Add to cart* increases the count of that item in the cart by 1.
 *
 * 2) **Discount tab** //done
 *    - Show all available discounts with:
 *      - an icon + text describing the discount,
 *      - an *Add to cart* button.
 *    - **Constraint:** each discount can be added **at most once**.
 *      Disable the button (or ignore clicks) for discounts already in the cart.
 *
 * 3) **Cart tab** //done
 *    - Only show the **Cart** tab contents if the cart is **not empty**. Within the cart:
 *      - List each cart item with:
 *        - image + name,
 *        - per-row total (`price * amount`),
 *        - an amount selector to **increase/decrease** the quantity (min 0, sensible max like 99).
 *      - Show all selected discounts with a way to **remove** them from the cart.
 *      - At the bottom, show: done
 *        - the **total price** of the cart (items minus discounts),
 *        - a **Pay** button that is enabled only when there is at least one item in the cart.
 *      - When **Pay** is pressed, **simulate payment** by clearing the cart and returning to the
 *        **Shop** tab.
 *
 * ## Navigation
 * - **Top bar**: done
 *      - Title shows either the shop name or "Cart".
 *      - When not in Cart, show a cart icon.
 *        If you feel fancy you can add a badge to the icon showing the total count (capped e.g. at "99+").
 *      - The cart button is enabled only if the cart contains items. In the Cart screen, show a back
 *        button to return to the shop.
 *
 * - **Bottom bar**: done
*       - In Shop/Discounts, show a 2-tab bottom bar to switch between **Shop** and **Discounts**.
*       - In Cart, hide the tab bar and instead show the cart bottom bar with the total and **Pay**
*         action as described above.
 *
 * ## Hints
 * - Keep your cart as a single source of truth and derive counts/price from it.
 *   Rendering each list can be done with a `LazyColumn` and stable keys (`item.id`, discount identity).
 * - Provide small reusable row components for items, cart rows, and discount rows.
 *   This keeps the screen implementation compact.
 *
 * ## Bonus (optional)
 * Make the app feel polished with simple animations, such as:
 * - `AnimatedVisibility` for showing/hiding the cart,
 * - `animateContentSize()` on rows when amounts change,
 * - transitions when switching tabs or updating the cart badge.
 *
 * These can help if want you make the app feel polished:
 * - [NavigationBar](https://developer.android.com/develop/ui/compose/components/navigation-bar)
 * - [Card](https://developer.android.com/develop/ui/compose/components/card)
 * - [Swipe to dismiss](https://developer.android.com/develop/ui/compose/touch-input/user-interactions/swipe-to-dismiss)
 * - [App bars](https://developer.android.com/develop/ui/compose/components/app-bars#top-bar)
 * - [Pager](https://developer.android.com/develop/ui/compose/layouts/pager)
 *
 */

enum class ShopInterface {
    Shop,
    Discounts,
    Cart
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    shop: Shop,
    availableDiscounts: List<Discount>,
    modifier: Modifier = Modifier
) {
    var cart by rememberSaveable { mutableStateOf(Cart(shop = shop)) }
    var currentInterface by rememberSaveable { mutableStateOf(ShopInterface.Shop)}
    var cartClickCount by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        modifier = modifier,

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    when (currentInterface) {
                        ShopInterface.Cart -> {Text(stringResource(id = R.string.title_cart))}
                        ShopInterface.Discounts -> {Text(stringResource(id = R.string.title_discounts))}
                        else -> Text(stringResource(id = R.string.name_shop))
                    }
                },
                navigationIcon = {
                    if (currentInterface == ShopInterface.Cart) {
                        IconButton(onClick = { currentInterface = ShopInterface.Shop }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    if (cart.itemCount > 0u && currentInterface != ShopInterface.Cart) {
                        IconButton(onClick = {
                            cartClickCount++ // später evtl. entfernen
                            currentInterface = ShopInterface.Cart
                        }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = stringResource(id = R.string.description_go_to_cart)
                            )
                        }
                    }
                }

            )
        },
        bottomBar = {
            if (currentInterface != ShopInterface.Cart) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentInterface == ShopInterface.Shop,
                        onClick = { currentInterface = ShopInterface.Shop; currentInterface = ShopInterface.Shop
                        },
                        icon = { Icon(Icons.Default.Store, contentDescription = null) },
                        label = { Text(stringResource(id = R.string.description_go_to_shop)) }
                    )
                    NavigationBarItem(
                        selected = currentInterface == ShopInterface.Discounts,
                        onClick = { currentInterface = ShopInterface.Discounts },
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text(stringResource(id = R.string.label_discounts)) }
                    )
                }
            } else {
                Surface(
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.total_price,
                                cart.price.toString()
                            ),
                            style = MaterialTheme.typography.titleMedium
                        )


                        Button(
                            onClick = {
                                if (cart.itemCount > 0u) {
                                    cart = Cart(shop = shop)
                                    currentInterface = ShopInterface.Shop
                                }
                            },
                            enabled = cart.itemCount > 0u
                        ) {
                            Text(stringResource(id = R.string.label_pay))
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize()
        ) {
            when (currentInterface) {
                ShopInterface.Shop -> ShopItemsScreen(
                    shop = shop,
                    modifier = Modifier.padding(innerPadding),
                    onAddToCart = { item ->
                        cart += item
                    }
                )
                ShopInterface.Discounts -> ShopDiscountScreen(
                    availableDiscounts = availableDiscounts,
                    cart = cart,
                    modifier = Modifier.padding(innerPadding),
                    onAddDiscount = { discount -> cart += discount }
                )
                ShopInterface.Cart -> ShopCartScreen(
                    cart = cart,
                    modifier = Modifier.padding(innerPadding),
                    onUpdateCart = {
                            newCart -> cart = newCart
                    },
                    onNavigateBack = { currentInterface = ShopInterface.Shop }
                )
            }
        }
    }

}
