package lyd.github.advertscrollview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import lyd.github.advert.AdvertScrollView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdvertScrollView recyclerView = (AdvertScrollView) findViewById(R.id.list);
        recyclerView.setAdapter(new AdvertScrollAdapter(this));
        getLifecycle().addObserver(recyclerView);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("lyd"," onClick ");
            }
        });
    }
}
