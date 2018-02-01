package lyd.github.advert;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by shawn on 18/1/30.
 */

public class RecylerMoveUtils {

    private int mIndex = 0;
    private boolean mMove = false;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    public RecylerMoveUtils(RecyclerView recyclerView,LinearLayoutManager mLinearLayoutManager) {
        this.mRecyclerView = recyclerView;
        this.mLinearLayoutManager = mLinearLayoutManager;
        mRecyclerView.addOnScrollListener(new OnScrollListener());
    }

    public void moveToPosition(int n) {
        mIndex = n;
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            mRecyclerView.scrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        }else{
            mRecyclerView.smoothScrollToPosition(n);
            mMove = true;
        }
    }

    class OnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mMove){
                mMove = false;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < mRecyclerView.getChildCount()){
                    int top = mRecyclerView.getChildAt(n).getTop();
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }
    }

}
