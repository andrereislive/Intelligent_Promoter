package com.a1500fh.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.a1500fh.intelligent_promoter.R;

/**
 * Created by Andre on 30/05/2017.
 */

public class ZoomImageThumb {

    private Animator mCurrentAnimatorEffect;

    private int mShortAnimationDurationEffect = 200;


    /**
     * How to USE:
     * <p>
     * Passo 1 - ADD NO XML DA ACTIVITY O SEGUINTE
     * <?xml version="1.0" encoding="utf-8"?>
     * <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
     * android:id="@+id/container"
     * android:layout_width="match_parent"
     * android:layout_height="match_parent"
     * android:layout_margin="16dp">
     * <p>
     * <RelativeLayout .....
     * ..
     * SEU CODIGO DO RELATIVE LAYOUT (PODE USAR OUTRO LAYOUT SE PREFERIR)
     * ...
     * </RelativeLayout>
     * <p>
     * <ImageView
     * android:id="@+id/expanded_image"
     * android:layout_width="match_parent"
     * android:layout_height="match_parent"
     * android:visibility="invisible" />
     * <p>
     * </FrameLayout>
     *
     * PASSO 2 - NO ACTIVITY.java instanciar o
     * ZoomImageThumb zoom = new ZoomImageThumb();
     *
     * PASSO 3 - NO CLICK DA IMAGEM ADD - nao mudar o final(ImageView) findViewById(R.id.expanded_image), R.id.container,this);
     * ONCLICK(){    zoom.zoomImageFromThumb(ivPequeno, imagemEmBitmap, (ImageView) findViewById(R.id.expanded_image), R.id.container,this);
     * }
     *
     * @param thumbView
     * @param imageBitmap
     * @param expandedImage
     * @param rIdContainer
     * @param appCompatActivity
     */


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void zoomImageFromThumb(final View thumbView, Bitmap imageBitmap, ImageView expandedImage, int rIdContainer, AppCompatActivity appCompatActivity) {
        if (mCurrentAnimatorEffect != null) {
            mCurrentAnimatorEffect.cancel();
        }

        final ImageView expandedImageView = expandedImage;

        expandedImageView.setImageBitmap(imageBitmap);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        appCompatActivity.findViewById(rIdContainer) // possivel erro <<<<<<<<<<<<<
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDurationEffect);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimatorEffect = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimatorEffect = null;
            }
        });
        set.start();
        mCurrentAnimatorEffect = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimatorEffect != null) {
                    mCurrentAnimatorEffect.cancel();
                }

                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDurationEffect);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimatorEffect = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimatorEffect = null;
                    }
                });
                set.start();
                mCurrentAnimatorEffect = set;
            }
        });
    }
}
