package com.bitc.android_team3.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.databinding.RecyclerItemBinding

class RecyclerAdapter(val person: MutableList<String>, val onClick: (String)->(Unit)): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(name: String){
            binding.nameTextView.text = name

            binding.root.setOnClickListener {
                // 리사이클러뷰 아이템에 클릭이벤트 발생
                onClick(name) // 생성자 파라미터로 받은 람다함수 onClick 실행
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bind = (holder as ViewHolder).binding

        if (position == 0) {
            bind.emojiTextView.text = "\uD83C\uDF71"
        }else if (position == 1){
            bind.emojiTextView.text = "\uD83E\uDDFB"
        }else if (position == 2){
            bind.emojiTextView.text = "\uD83D\uDCFA\uFE0E"
        }else if (position == 3){
            bind.emojiTextView.text = "\uD83D\uDC84"
        }else if (position == 4){
            bind.emojiTextView.text = "\uD83D\uDC5F"
        }else if (position == 5){
            bind.emojiTextView.text = "\uD83E\uDDF3"
        }else if (position == 6){
            bind.emojiTextView.text = "\uD83C\uDF7C"
        }else if (position == 7){
            bind.emojiTextView.text = "\uD83D\uDCDA"
        }else if (position == 8){
            bind.emojiTextView.text = "\uD83D\uDECB"
        }

        holder.bind(person[position])
    }

    override fun getItemCount(): Int = person.size

}