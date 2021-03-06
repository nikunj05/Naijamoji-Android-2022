package com.naijamojiapp.app.cusromsharedialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.naijamojiapp.R;
import com.naijamojiapp.app.cusromsharedialog.Models.ShareActionModel;
import com.naijamojiapp.app.cusromsharedialog.Utils.DisplayType;

import java.util.ArrayList;
import java.util.List;


public class ShareItemsAdapter extends RecyclerView.Adapter<ShareItemsAdapter.SharableViewHolder>
        implements OnItemClickListener {

    private Context context;
    private List<ShareActionModel> items;
    private DisplayType type;

    private OnShareActionSelect callback;

    public ShareItemsAdapter(Context context, boolean isHorizontal) {
        this.context = context;
        this.items = new ArrayList<>();

        if(isHorizontal)
            type = DisplayType.HORIZONTAL;
        else
            type = DisplayType.DEFAULT;
    }

    public ShareItemsAdapter(Context context, DisplayType type) {
        this.context = context;
        this.items = new ArrayList<>();
        this.type = type;
    }

    @Override
    public SharableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int itemLayout;

        switch (type){
            case HORIZONTAL:
                itemLayout = R.layout.item_share_action_horizontal;
                break;
            case LIST:
                itemLayout = R.layout.item_share_action_single;
                break;
            default:
                itemLayout = R.layout.item_share_action;
                break;
        }

        View layout = LayoutInflater.from(context).inflate(itemLayout, parent, false);

        return new SharableViewHolder(layout, this);
    }

    @Override
    public void onBindViewHolder(SharableViewHolder holder, int position) {
        Log.i("infoo",items.get(position).getAppInfo().loadLabel(context.getPackageManager()).toString());
        holder.label.setText(items.get(position).getAppInfo().loadLabel(context.getPackageManager()));
        Drawable icon = items.get(position).getAppInfo().loadIcon(context.getPackageManager());
        holder.icon.setBackground(icon);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemClick(View v, int position) {
        if (callback != null)
            callback.onSelect(items.get(position), position);
    }

    public void setItems(List<ShareActionModel> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setCallbackListener(OnShareActionSelect callback) {
        this.callback = callback;
    }

    public class SharableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View item;
        public ImageView icon;
        public TextView label;

        private OnItemClickListener listener;

        public SharableViewHolder(View itemView) {
            super(itemView);
            this.item = itemView.findViewById(R.id.item);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.label = (TextView) itemView.findViewById(R.id.label);

            itemView.setOnClickListener(this);
        }

        public SharableViewHolder(View itemView, OnItemClickListener listener) {
            this(itemView);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, this.getAdapterPosition());
        }
    }

    interface OnShareActionSelect {
        void onSelect(ShareActionModel model, int position);
    }
}
