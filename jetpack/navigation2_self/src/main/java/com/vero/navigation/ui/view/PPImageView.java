package com.vero.navigation.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

public class PPImageView extends AppCompatImageView {
    public PPImageView(Context context) {
        super(context);
    }

    public PPImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PPImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * requireAll :true时：
     *
     * image_url isCircle 这两个参数都写在xml时，才会调用这个方法
     *
     * false:只需要1个参数，就会掉这个方法
     *
     *
     *
     */
    @BindingAdapter(value = {"image_url", "isCircle"}, requireAll = true)
    public static void setImageUrl(PPImageView view, String imageUrl, boolean isCircle) {
        RequestBuilder<Drawable> builder = Glide.with(view).load(imageUrl);
        if (isCircle) {
            builder.transform(new CircleCrop());
        }

        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
            builder.override(layoutParams.width, layoutParams.height);
        }
        builder.into(view);

    }
}
