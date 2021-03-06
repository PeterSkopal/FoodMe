package com.example.skopal.foodme.layouts.mykitchen


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.skopal.foodme.MainActivity

import com.example.skopal.foodme.R
import com.example.skopal.foodme.services.SpoonacularApi
import com.example.skopal.foodme.utils.DownloadImageTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 *
 */
class RecipeInstruction : Fragment() {

    private var recipeId = 0
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            recipeId = it.getInt(RECIPE_ID)

            val url = it.getString(IMAGE_URL)
            if (url != null) {
                imageUrl = url
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false)

        DownloadImageTask(view.findViewById(R.id.recipe_instruction_image)).execute(imageUrl)

        val ingredients = view.findViewById<TextView>(R.id.recipe_instruction_ingredients)
        val instructions = view.findViewById<TextView>(R.id.recipe_instruction_instructions)

        val baseContext = (activity as MainActivity).baseContext

        (activity as MainActivity).showSpinner(getString(R.string.loading_recipe))
        SpoonacularApi(baseContext).getRecipe(recipeId) { res ->
            GlobalScope.launch(Dispatchers.Main) {
                if (res !== null) {
                    (activity as MainActivity).setActionBarTitle(res.title)
                    (activity as MainActivity).hideSpinner()

                    val i = "Ingredients:\n\n" +
                            res.extendedIngredients.joinToString("\n") {
                                "${Math.round(it.measures.metric.amount)} ${it.measures.metric.unitShort} ${it.name}"
                            }.plus("\n")
                    ingredients.text = i

                    val ins = "Instructions:\n\n" +
                            res.analyzedInstructions[0].steps.joinToString("\n") {
                                "${it.number}:   ${it.step}"
                            }
                    instructions.text = ins
                }
            }
        }

        return view
    }

    companion object {
        const val RECIPE_ID = "recipe-id"
        const val IMAGE_URL = "image-url"

        @JvmStatic
        fun newInstance(id: Int, imageUrl: String) = RecipeInstruction().apply {
            arguments = Bundle().apply {
                putInt(RECIPE_ID, id)
                putString(IMAGE_URL, imageUrl)
            }
        }
    }

}
