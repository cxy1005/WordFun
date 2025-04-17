package com.example.applicationtest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtest.R;
import com.example.applicationtest.activity.WordManageActivity;
import com.example.applicationtest.model.Table;
import com.tencent.mmkv.MMKV;

import java.util.List;

public class MyWordDBRecycleViewAdapter extends RecyclerView.Adapter<MyWordDBRecycleViewAdapter.MyViewHolder> {

    private static final String TAG = "MyWordDBRecycleViewAdap";
    private MMKV mmkv = MMKV.defaultMMKV();

    private List<Table> data;
    private Context context;
    private WordManageActivity wordManageActivity; // 添加这个引用

    public MyWordDBRecycleViewAdapter(Context context, List<Table> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyWordDBRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.list_item1,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWordDBRecycleViewAdapter.MyViewHolder holder, int position) {
        holder.tv1.setText(data.get(position).getName());
        Log.e(TAG, "onBindViewHolder: "+ position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv1;
        Button deleteBtn;
        Button use;
        View wordDBCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            deleteBtn = itemView.findViewById(R.id.deleteWordDB);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: 删除词库名："+tv1.getText());
                }
            });
            use = itemView.findViewById(R.id.use);
            use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: 使用词库名："+tv1.getText());
                    mmkv.encode("curWordDB", (String) tv1.getText());
                    wordManageActivity.updateCurrentWordDB((String) tv1.getText());
                }
            });
            wordDBCard = itemView.findViewById(R.id.wordDBCard);
            wordDBCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: 打开单词本："+tv1.getText());


                }
            });
        }
    }

    private OnRecyclerItemClickListener mOnItemClickListener;

    public void  setRecyclerItemClickListener(OnRecyclerItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public interface  OnRecyclerItemClickListener{
        void onRecyclerItemClick(int position);
    }
}
