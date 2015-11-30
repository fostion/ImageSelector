package fs.cc.library.presenser;

import android.content.Context;

import java.util.ArrayList;

import fs.cc.library.data.Image;
import fs.cc.library.data.local.ImageUtils;
import fs.cc.library.utils.MyCallback;

/**
 * Created by fostion on 2015/11/26.
 */
public class ImagePresener {

    public ImagePresener(){

    }

    public void phoneImages(Context context, final MyCallback.Callback1<ArrayList<Image>> callback){

        ImageUtils.getPhoneImages(context, new MyCallback.Callback1<ArrayList<Image>>() {
            @Override
            public Object run(ArrayList<Image> param) {
                callback.run(param);
                return null;
            }
        });
    }
}
