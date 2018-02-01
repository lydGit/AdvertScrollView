package lyd.github.advertscrollview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shawn on 18/1/25.
 */

public class AdvertScrollAdapter extends RecyclerView.Adapter<AdvertScrollAdapter.AdvertScrollHolder> {

    private Context mContext;

    public AdvertScrollAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public AdvertScrollHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdvertScrollHolder(LayoutInflater.from(mContext).inflate(R.layout.item_advert_scroll, parent, false));
    }

    @Override
    public void onBindViewHolder(AdvertScrollHolder holder, final int position) {
        holder.text.setText("Item" + position);
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class AdvertScrollHolder extends RecyclerView.ViewHolder {

        TextView text;

        public AdvertScrollHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
