package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityChatBinding
import com.example.myapplication.databinding.FragmentFlirtingBinding
import com.example.myapplication.databinding.FragmentSpecialCharBinding
import com.example.myapplication.databinding.ItemSimpleChatDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class FragmentSpecialChar : Fragment() {

    lateinit var binding: FragmentSpecialCharBinding
    private lateinit var specialList: ArrayList<SimpleChatDataModel>
    lateinit var adapter: MyAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpecialCharBinding.inflate(inflater)
        mAuth = Firebase.auth
        mDbRef = Firebase.database.reference
        specialList = ArrayList()
        adapter = MyAdapter(context, specialList)

        binding.specialCharRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.specialCharRecyclerView.adapter = adapter

        //DB에 있는 간편채팅 데이터 가져와서 리스트에 넣기
        mDbRef.child("simpleChat").child("specialChar")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    specialList.clear()
                    for (postSnapshot in snapshot.children) {
                        val data = postSnapshot.getValue(SimpleChatDataModel::class.java)
                        if (data != null) {
                            specialList.add(data!!)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("간챗", "----------플러팅 탭 데이터 로딩 실패")
                }
            })

        return binding.root
    }//onCreateView 끝


    class MyAdapter(
        private val context: Context?,
        private val specialList: ArrayList<SimpleChatDataModel>
    ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.item_data)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_simple_chat_data, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return specialList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val currentData = specialList[position].chatData
            holder.textView.text = currentData
            Log.e("간챗", "데이터 $currentData")
            //리사이클러뷰를 선택했을 때 editText에 텍스트 넣기
            holder.itemView.setOnClickListener{
                Log.e("간챗", "클릭했을때 데이터 $currentData")
                (context as ChatActivity).binding.messageEdit.setText(currentData)
            }
            //길게 클릭할 경우 텍스트 누적시키기
            holder.itemView.setOnLongClickListener {
                (context as ChatActivity).binding.messageEdit.append(" " + currentData)
                true
            }
        }

    }//어댑터 끝


}