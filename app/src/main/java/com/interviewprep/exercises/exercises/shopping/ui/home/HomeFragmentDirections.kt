package com.interviewprep.exercises.exercises.shopping.ui.home

import androidx.navigation.NavDirections
import com.interviewprep.exercises.R

/**
 * HomeFragmentDirections — manually written Safe Args equivalent.
 *
 * ─── What is Safe Args? ───────────────────────────────────────────────────────
 *
 * Safe Args is a Gradle plugin that auto-generates type-safe classes for
 * navigating with arguments. You add the plugin, define <argument> tags in
 * your nav graph, and the plugin generates:
 *   - [Fragment]Directions classes with action methods
 *   - [Fragment]Args classes for reading arguments in the destination
 *
 * ─── Why we wrote it manually here ───────────────────────────────────────────
 *
 * Enabling the Safe Args Gradle plugin requires kapt and changes to the
 * build.gradle files that add significant complexity to a reference project.
 * To keep the build simple and buildable by anyone, we write the Directions
 * class manually — the pattern is IDENTICAL to what the plugin generates.
 *
 * In a real project you would:
 *   1. Add to root build.gradle:
 *      id 'androidx.navigation.safeargs.kotlin' version '2.6.0' apply false
 *   2. Add to app/build.gradle:
 *      id 'androidx.navigation.safeargs.kotlin'
 *   3. Define in nav graph:
 *      <argument android:name="productId" app:argType="integer"/>
 *   4. Use the auto-generated class (identical API to this stub):
 *      HomeFragmentDirections.actionHomeToProductDest(product.id)
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
object HomeFragmentDirections {

    /**
     * Navigate from Home to Product Detail, passing productId as an argument.
     *
     * Safe Args encoding: bundles the argument so the destination fragment can
     * read it via arguments?.getInt("productId") — type-safe at compile time.
     */
    fun actionHomeToProductDest(productId: Int): NavDirections {
        return object : NavDirections {
            override val actionId: Int = R.id.action_home_to_product_dest
            override val arguments: android.os.Bundle = android.os.Bundle().apply {
                putInt("productId", productId)
            }
        }
    }
}
