package com.meix.library.ui;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meix.library.R;
import com.meix.library.utils.DisplayUtil;

/**
 * author steven
 * date 2021/12/27
 * description 通用标题栏
 */
public class TitleBar extends RelativeLayout {
    private View rootView;

    public interface OnClickLeftBackListener {
        void click();
    }

    public interface OnClickRightImgsListener {
        void click(int position);
    }

    public interface OnClickRightStrsListener {
        void click(int position);
    }

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this);
    }

    public Builder getBuilder(Activity context) {
        return new Builder(context);
    }

    public class Builder {
        private Activity context;
        private TextView tv_middle_title;
        private ImageView iv_left;
        private TextView tv_left;
        private String middleTitle;
        private String leftTitle;
        private LinearLayout ll_middle;
        private LinearLayout ll_right_func;
        private int ivLeftImg;
        private int[] rightImgs = new int[]{};
        private String[] rightStrs = new String[]{};
        /**
         * 右侧功能按钮直接的间距
         */
        private int rightFuncDash;
        private OnClickLeftBackListener onClickLeftBackListener;
        private OnClickRightImgsListener onClickRightImgsListener;
        private OnClickRightStrsListener onClickRightStrsListener;

        public Builder(Activity context) {
            this.context = context;
            iv_left = rootView.findViewById(R.id.iv_back);
            tv_left = rootView.findViewById(R.id.tv_back);
            tv_middle_title = rootView.findViewById(R.id.tv_middle_title);
            ll_right_func = rootView.findViewById(R.id.ll_right_function);
            ll_middle = findViewById(R.id.ll_middle);
            rightFuncDash = DisplayUtil.dip2px(context, 10);
        }

        public Builder setMiddleTitle(String middleTitle) {
            this.middleTitle = middleTitle;
            return this;
        }

        public Builder setIvLeftImg(int ivLeftImg) {
            this.ivLeftImg = ivLeftImg;
            return this;
        }

        /**
         * 设置右侧图标支持设置多个
         *
         * @param ims
         * @return
         */
        public Builder setRightImgs(int... ims) {
            this.rightImgs = ims;
            return this;
        }

        /**
         * 设置右侧文本支持设置多个
         *
         * @param ims
         * @return
         */
        public Builder setRightStrs(String... ims) {
            this.rightStrs = ims;
            return this;
        }

        /**
         * 左侧的标题内容
         *
         * @param leftTitle
         * @return
         */
        public Builder setLeftTitle(String leftTitle) {
            this.leftTitle = leftTitle;
            return this;
        }

        /**
         * 完全自定义中间标题view
         *
         * @param view
         * @return
         */
        public Builder setSelfMiddleView(View view) {
            if (ll_middle.getChildCount() > 0) {
                ll_middle.removeAllViews();
                ll_middle.addView(view);
            }
            return this;
        }

        /**
         * 完全自定义右侧view
         * @param view
         * @return
         */
        public Builder setSelfRightView(View view) {
            if (ll_right_func.getChildCount() > 0) {
                ll_right_func.removeAllViews();
                ll_right_func.addView(view);
            }
            return this;
        }

        /**
         * 重新设置返回按钮的点击事件-设置后源码的点击事件将失效
         *
         * @param onClickLeftBackListener
         * @return
         */
        public Builder setOnClickLeftBackListener(OnClickLeftBackListener onClickLeftBackListener) {
            this.onClickLeftBackListener = onClickLeftBackListener;
            return this;
        }

        public Builder setOnClickRightImgsListener(OnClickRightImgsListener onClickRightImgsListener) {
            this.onClickRightImgsListener = onClickRightImgsListener;
            return this;
        }

        public Builder setOnClickRightStrsListener(OnClickRightStrsListener onClickRightStrsListener) {
            this.onClickRightStrsListener = onClickRightStrsListener;
            return this;
        }

        /**
         * 隐藏左侧操作栏
         *
         * @return
         */
        public Builder hideLeft() {
            tv_left.setVisibility(GONE);
            iv_left.setVisibility(GONE);
            return this;
        }

        /**
         * 不展示左侧返回文本
         * @return
         */
        public Builder showOnlyLeftImg(){
            tv_left.setVisibility(GONE);
            iv_left.setVisibility(VISIBLE);
            return this;
        }

        /**
         * 不展示左侧返回图标
         * @return
         */
        public Builder showOnlyLeftStr(){
            tv_left.setVisibility(VISIBLE);
            iv_left.setVisibility(GONE);
            return this;
        }


        /**
         * 隐藏中间标题
         *
         * @return
         */
        public Builder hideMiddleTitle() {
            tv_middle_title.setVisibility(GONE);
            return this;
        }

        public void create() {
            iv_left.setOnClickListener(v -> {
                if (onClickLeftBackListener != null) {
                    onClickLeftBackListener.click();
                } else {
                    context.finish();
                }
            });
            iv_left.setImageResource(ivLeftImg > 0 ? ivLeftImg : R.mipmap.icon_back);
            tv_left.setText(TextUtils.isEmpty(leftTitle) ? "" : leftTitle);
            tv_middle_title.setText(middleTitle);
            tv_middle_title.setVisibility(TextUtils.isEmpty(middleTitle) ? GONE : VISIBLE);
            if (rightImgs.length > 0) {
                for (int i = 0; i < rightImgs.length; i++) {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(rightImgs[i]);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = rightFuncDash;
                    int finalI = i;
                    imageView.setOnClickListener(v -> {
                        if (onClickRightImgsListener != null) {
                            onClickRightImgsListener.click(finalI);
                        }
                    });
                    imageView.setLayoutParams(layoutParams);
                    ll_right_func.addView(imageView);
                }
            }
            if (rightStrs.length > 0) {
                for (int i = 0; i < rightStrs.length; i++) {
                    TextView textView = new TextView(context);
                    textView.setText(rightStrs[i]);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = rightFuncDash;
                    int finalI = i;
                    textView.setOnClickListener(v -> {
                        if (onClickRightStrsListener != null) {
                            onClickRightStrsListener.click(finalI);
                        }
                    });
                    textView.setLayoutParams(layoutParams);
                    ll_right_func.addView(textView);
                }
            }
        }
    }
}
