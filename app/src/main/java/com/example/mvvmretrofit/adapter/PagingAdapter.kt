package com.example.mvvmretrofit.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import com.example.mvvmretrofit.R
import com.example.mvvmretrofit.activity.MainActivity
import com.example.mvvmretrofit.activity.WebActivity
import com.example.mvvmretrofit.bean.GithubSearchBean
import com.example.mvvmretrofit.databinding.ItemGithubBinding

class PagingAdapter(private val activity: MainActivity) : PagingDataAdapter<GithubSearchBean, ItemViewHolder>(diff) {

    private lateinit var itemGithubBinding: ItemGithubBinding

    companion object {
        private val diff = object : DiffUtil.ItemCallback<GithubSearchBean>() {
            override fun areItemsTheSame(oldItem: GithubSearchBean, newItem: GithubSearchBean): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubSearchBean, newItem: GithubSearchBean): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        itemGithubBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_github, parent, false)
        return ItemViewHolder(itemGithubBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            itemGithubBinding.nameText.text = item.name
            itemGithubBinding.descriptionText.text = item.description
            itemGithubBinding.starCountText.text = item.starCount.toString()
            itemGithubBinding.ivLogo.load(item.owner.avatar_url){
                transformations(RoundedCornersTransformation(topRight = 10f, topLeft = 10f, bottomLeft = 10f, bottomRight = 10f)) //图片变换  圆形
//                transformations(BlurTransformation(context = activity, radius = 5f, sampling = 5f))
//                transformations(GrayscaleTransformation())
                listener(
                    onStart ={ request ->
                        Log.d("lpf", "PlaceHolder onError 開始加載...")
                    },
                    onError = { request, throwable ->
                        Log.d("lpf", "PlaceHolder onError 加載失敗...")
                    },
                    onCancel = { request ->
                        Log.d("lpf", "PlaceHolder onCancel 加載重載中...")
                    },
                    onSuccess = { request, metadata ->
                        Log.d("lpf", "PlaceHolder onSuccess 加載成功...")
                    }
                )
            }
            itemGithubBinding.llRoot.setOnClickListener {
                val intent = Intent(activity, WebActivity::class.java)
                intent.putExtra("url", item.htmlUrl)
                activity.startActivity(intent)
            }
        }
    }

}