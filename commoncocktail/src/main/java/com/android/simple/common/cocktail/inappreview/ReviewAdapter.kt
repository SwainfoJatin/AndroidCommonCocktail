package com.android.simple.common.cocktail.inappreview

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.simple.common.cocktail.R
import com.android.simple.common.cocktail.databinding.ReviewRawItemBinding

class ReviewAdapter(var activity: Activity,var dataList: List<String>,var selectTag: OnSelectTag) : RecyclerView.Adapter<ReviewAdapter.ReviewAHolder>() {

    class ReviewAHolder(var binding:ReviewRawItemBinding ) : RecyclerView.ViewHolder(binding.root) {}
      var selectedpos = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAHolder {
        val binding = ReviewRawItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewAHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewAHolder, position: Int) {
        holder.binding.tagName.text= dataList.get(position)

        if(selectedpos == position)
        {
            holder.binding.tagContainer.setBackgroundResource(R.drawable.selected_bg)
            holder.binding.tagName.setTextColor(ContextCompat.getColor(activity, R.color.white))
            selectTag.setSelectedTag(dataList.get(position))
        }
        else
        {
            holder.binding.tagContainer.setBackgroundResource(R.drawable.unselected_bg)
            holder.binding.tagName.setTextColor(ContextCompat.getColor(activity, R.color.black))
        }


        holder.binding.tagContainer.setOnClickListener(View.OnClickListener {

            selectTag.setSelectedTag(dataList.get(position))
            selectedpos = position
            notifyDataSetChanged()
        })
    }

    override fun getItemCount(): Int {
       return dataList.size
    }
}

