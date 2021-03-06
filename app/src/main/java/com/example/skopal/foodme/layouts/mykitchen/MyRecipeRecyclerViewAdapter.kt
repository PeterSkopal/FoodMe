package com.example.skopal.foodme.layouts.mykitchen

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.skopal.foodme.R
import com.example.skopal.foodme.classes.RecipeItem
import com.example.skopal.foodme.layouts.mykitchen.RecipeFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import android.widget.ImageView
import com.example.skopal.foodme.utils.DownloadImageTask

/**
 * [RecyclerView.Adapter] that can display a [RecipeItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyRecipeRecyclerViewAdapter(
        private val mValues: List<RecipeItem>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyRecipeRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as RecipeItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.title
        DownloadImageTask(holder.mImageView).execute(item.image)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content
        val mImageView: ImageView = mView.card_view_image

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text
        }
    }
}
