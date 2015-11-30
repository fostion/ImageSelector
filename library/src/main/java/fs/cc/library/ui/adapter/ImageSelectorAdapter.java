package fs.cc.library.ui.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import fs.cc.library.R;
import fs.cc.library.data.Image;

/**
 * Created by fostion on 2015/11/26.
 */
public class ImageSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_CAMERA = 0;
    public static final int TYPE_ITEM = 1;

    private ArrayList<Image> images;
    private ArrayList<Image> selectImages;

    public ImageSelectorAdapter(ArrayList<Image> images, ArrayList<Image> selectImages) {
        this.images = images;
        this.selectImages = selectImages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_camera, parent, false);
            return new CameraViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_selector, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof  ItemViewHolder){
            int index = position - 1;
            ItemViewHolder vh = (ItemViewHolder) holder;
            vh.bindData(images.get(index));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_CAMERA;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return images.size()+1;
    }

    class CameraViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public CameraViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(TYPE_CAMERA,null);
                    }
                }
            });
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView draweeView;
        ImageView isSelect;
        View mask;

        public ItemViewHolder(View itemView) {
            super(itemView);

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.image);
            isSelect = (ImageView) itemView.findViewById(R.id.isSelect);
            mask = (View) itemView.findViewById(R.id.mask);
        }

        public void bindData(final Image image) {

            //本地文件生成预览图，减小内存消耗
            Uri uri = Uri.parse("file://" + image.getPath());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(draweeView.getController())
                    .build();
            draweeView.setController(controller);
            isSelect.setVisibility(View.VISIBLE);

            if (selectImages.contains(image)) {
                isSelect.setSelected(true);
                mask.setVisibility(View.VISIBLE);
            } else {
                isSelect.setSelected(false);
                mask.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSelect.isSelected()) {
                        isSelect.setSelected(false);
                        removeSelected(image);
                        mask.setVisibility(View.GONE);
                    } else {
                        isSelect.setSelected(true);
                        addSelected(image);
                        mask.setVisibility(View.VISIBLE);
                    }
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(TYPE_ITEM,image);
                    }
                }
            });
        }
    }

    public void addSelected(Image image) {
        selectImages.add(image);
    }

    private void removeSelected(Image image) {
        int index = selectImages.indexOf(image);
        if (index > -1 && index < selectImages.size()) {
            //下标合法
            selectImages.remove(index);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int type,Image image);
    }

}
