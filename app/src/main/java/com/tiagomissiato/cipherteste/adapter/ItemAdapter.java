package com.tiagomissiato.cipherteste.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tiagomissiato.cipherteste.R;
import com.tiagomissiato.cipherteste.model.Item;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by trigoleto on 11/27/14.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String TAG = ItemAdapter.class.getSimpleName();

    Context mContext;
    private List<Item> items;
    NumberFormat format;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView value;
        ImageView image;

        public ViewHolder(View iteView) {
            super(iteView);
            this.title = (TextView) iteView.findViewById(R.id.item_title);
            this.description = (TextView) iteView.findViewById(R.id.item_description);
            this.value = (TextView) iteView.findViewById(R.id.item_value);
            this.image = (ImageView) iteView.findViewById(R.id.item_image);
        }
    }

    public ItemAdapter(Context mContext, List<Item> items) {
        this.items = items;
        this.mContext = mContext;

        Locale meuLocal = new Locale( "pt", "BR" );
        format = NumberFormat.getCurrencyInstance(meuLocal);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, null);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Item item = this.items.get(position);

        viewHolder.title.setText(item.getTitle());
        viewHolder.description.setText(item.getDescription());
        viewHolder.value.setText(format.format(item.getValor()));
        Glide.with(mContext)
                .load(item.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.image);
//        viewHolder.campaignType.setText(typeStr);
    }

}
