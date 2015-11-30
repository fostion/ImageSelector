package fs.cc.library.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fostion on 2015/11/26.
 */
public class Image implements Parcelable,Comparable<Image> {
    private int id;
    private String name;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.path = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    /**
     *由于无法获取所以使用id排序获取，id越大时间越新
     */
    @Override
    public int compareTo(Image image) {
        return image.getId() - getId();
    }
}
