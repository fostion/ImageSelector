package cc.fs.imageselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fs.cc.library.ui.activity.ImageSelectorActivity;
import fs.cc.library.utils.L;


public class MainActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelectorActivity.start(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){//返回成功
            if(requestCode == ImageSelectorActivity.SELECT_IMAGE){
                String[] imagesArray = data.getStringArrayExtra("images");
                for(int i=0;i<imagesArray.length;i++){
                    L.w(" 选择图片 "+imagesArray[i]);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
