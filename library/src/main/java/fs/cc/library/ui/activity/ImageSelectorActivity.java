package fs.cc.library.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import fs.cc.library.R;
import fs.cc.library.data.Image;
import fs.cc.library.presenser.ImagePresener;
import fs.cc.library.ui.adapter.ImageSelectorAdapter;
import fs.cc.library.utils.L;
import fs.cc.library.utils.MyCallback;

/**
 * Created by fostion on 2015/11/26.
 */
public class ImageSelectorActivity extends AppCompatActivity{

    public static final int SELECT_IMAGE = 1111;

    private ArrayList<Image> images;
    private ArrayList<Image> selectImages;
    private RecyclerView recyclerView;
//    private Toolbar toolbar;
    private LayoutManager layoutManager;
    private ImageSelectorAdapter adapter;
    private ImagePresener imagePresener;
    private ImageView backBtn;
    private Button doneBtn;

    public static void start(Activity context){
        Intent intent = new Intent(context,ImageSelectorActivity.class);
        context.startActivityForResult(intent,SELECT_IMAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selctor);
        //init image lib
        Fresco.initialize(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        doneBtn = (Button) findViewById(R.id.doneBtn);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);

//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        toolbar.setTitle("选择图片");
//        setSupportActionBar(toolbar);

        images = new ArrayList<>();
        selectImages = new ArrayList<>();

        layoutManager = new GridLayoutManager(this,3);
        adapter = new ImageSelectorAdapter(images,selectImages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new ImageSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int type,Image image) {
                if(type == ImageSelectorAdapter.TYPE_CAMERA){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                } else {

                }
//                L.e(" 选择图片 "+image.getName());
            }
        });

        imagePresener = new ImagePresener();
        imagePresener.phoneImages(getApplicationContext(), new MyCallback.Callback1<ArrayList<Image>>() {
            @Override
            public Object run(final ArrayList<Image> param) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(param != null && param.size() > 0){
                            images.clear();
                            images.addAll(param);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

                return null;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.d(" 选择的图片 " + selectImages.size());
                Intent intent = new Intent();
                String stringArray[] = new String[selectImages.size()];
                for(int i=0;i < stringArray.length;i++){
                    stringArray[i] = images.get(i).getPath();
                }
                intent.putExtra("images",stringArray);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_image_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;

//            case R.id.action_done:
//                L.d(" 选择的图片 " + selectImages.size());
//                Intent intent = new Intent();
//                String stringArray[] = new String[selectImages.size()];
//                for(int i=0;i < stringArray.length;i++){
//                    stringArray[i] = images.get(i).getPath();
//                }
//                intent.putExtra("images",stringArray);
//                setResult(RESULT_OK,intent);
//                finish();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
