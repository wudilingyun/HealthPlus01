package com.vee.healthplus.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;

public class CustomDialog extends Dialog {
    private Context context;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    public void show() {
        super.show();

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
    }

    public void setDisMissShort(final CustomDialog dialog) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }.start();
    }

    public static class Builder {
        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private boolean isAutoDisappear = true;
        private int titlebg = -1;
        private Drawable mainviewbg = null;
        private int titlecolor = -1;

        private final static String TAG = "Builder";

        private DialogInterface.OnClickListener positiveButtonClickListener,
                negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setDisappear(boolean bool) {
            this.isAutoDisappear = bool;
            return this;
        }

        public void setViewBG(Drawable drawable) {
            this.mainviewbg = drawable;
        }

        public void setTitleColor(int color) {
            this.titlecolor = color;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set a custom content view for the Dialog. If a message is set, the
         * contentView is not added to the Dialog...
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 设置Title背景
         */
        public void setTitleBG(int titlebg) {
            this.titlebg = titlebg;
        }
        
        public float px2sp(float px){
    		float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
    		return px/fontScale + 0.5f;
    	}

        public Builder setMessage(int resId) {
            TextView contentView = new TextView(context);
            contentView.setTextColor(context.getResources().getColor(R.drawable.gray));
            contentView.setPadding(5, 5, 5, 5);
            contentView.setTextSize(px2sp(context.getResources().getDimension( R.dimen.health_plus_font_size_1)));
            //contentView.setGravity(Gravity.CENTER);
            contentView.setText(resId);
            this.contentView = contentView;
            return this;
        }

        public Builder setMessage(String txt) {
            TextView contentView = new TextView(context);
            contentView.setTextColor(context.getResources().getColor(R.drawable.gray));
            contentView.setPadding(30, 30,30, 40);
            //contentView.setGravity(Gravity.CENTER);
            contentView.setTextSize(px2sp(context.getResources().getDimension( R.dimen.health_plus_font_size_1)));
            contentView.setText(txt);
            this.contentView = contentView;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        
       /**
         * Create the custom dialog
         */
        public CustomDialog createWrapContent() {
//            Log.e(TAG, "create");
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.custom_dialog, null);
//            Log.e(TAG,  "width " + LayoutParams.MATCH_PARENT );
            
            dialog.addContentView(layout, new LayoutParams(
                    (int) (LayoutParams.MATCH_PARENT), LayoutParams.WRAP_CONTENT));
            // set the dialog title
            TextView title_tv = ((TextView) layout.findViewById(R.id.title));
            title_tv.setText(title);
            if (titlecolor != -1) {
                title_tv.setTextColor(titlecolor);
            }
            LinearLayout titlebg_ll = (LinearLayout) layout.findViewById(R.id.titlebg_ll);
           /* if (titlebg != -1) {
                titlebg_ll.setBackgroundColor(titlebg);
            }*/
            LinearLayout mainview_ll = (LinearLayout) layout.findViewById(R.id.mainview_ll);
            if (mainviewbg != null) {
                mainview_ll.setBackgroundDrawable(mainviewbg);
                LayoutParams params=mainview_ll.getLayoutParams();
            	params.width=android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
            	mainview_ll.setLayoutParams(params);
            	mainview_ll.setMinimumWidth(180);
            }

            final Button positiveButton = (Button) layout.findViewById(R.id.positiveButton);
            final Button negativeButton = (Button) layout.findViewById(R.id.negativeButton);
            // set the confirm button
            if (positiveButtonText != null) {
                Log.e(TAG, "positiveButtonText = " + positiveButtonText);
                positiveButton.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
//                    Log.e(TAG, " set positiveButtonClickListener  ");
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
//                                    Log.e(TAG, "positiveButton onClick!");

                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    if (isAutoDisappear) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
//                Log.e(TAG, "positiveButton Gone! ");
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
//                Log.e(TAG, "negativeButtonText = " + negativeButtonText);

                if (negativeButtonClickListener != null) {
//                    Log.e(TAG, "set negativeButtonClickListener");

                    negativeButton.setText(negativeButtonText);
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
//                                    Log.e(TAG, "positiveButton onClick!");
                            //positiveButton.setBackgroundResource(R.drawable.button_blue);
                            //negativeButton.setBackgroundResource(R.drawable.button_silver);
                            //negativeButtonClickListener
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
//                Log.e(TAG, "negativeButton Gone! ");
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content view
            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
//                Log.e(TAG, "contentView != null");
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView/*, new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT)*/);
            }
            dialog.setContentView(layout);
            return dialog;
        }
        /**
         * Create the custom dialog
         */
        public CustomDialog create() {
//            Log.e(TAG, "create");
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.custom_dialog, null);
//            Log.e(TAG,  "width " + LayoutParams.MATCH_PARENT );
            dialog.addContentView(layout, new LayoutParams(
                    (int) (LayoutParams.MATCH_PARENT), LayoutParams.WRAP_CONTENT));
            // set the dialog title
            TextView title_tv = ((TextView) layout.findViewById(R.id.title));
            title_tv.setText(title);
            if (titlecolor != -1) {
                title_tv.setTextColor(titlecolor);
            }
            LinearLayout titlebg_ll = (LinearLayout) layout.findViewById(R.id.titlebg_ll);
            if (titlebg != -1) {
                titlebg_ll.setBackgroundColor(titlebg);
            }
            LinearLayout mainview_ll = (LinearLayout) layout.findViewById(R.id.mainview_ll);
            if (mainviewbg != null) {
                mainview_ll.setBackgroundDrawable(mainviewbg);
            }

            final Button positiveButton = (Button) layout.findViewById(R.id.positiveButton);
            final Button negativeButton = (Button) layout.findViewById(R.id.negativeButton);
            // set the confirm button
            if (positiveButtonText != null) {
                Log.e(TAG, "positiveButtonText = " + positiveButtonText);
                positiveButton.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
//                    Log.e(TAG, " set positiveButtonClickListener  ");
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
//                                    Log.e(TAG, "positiveButton onClick!");

                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    if (isAutoDisappear) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
//                Log.e(TAG, "positiveButton Gone! ");
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
//                Log.e(TAG, "negativeButtonText = " + negativeButtonText);

                if (negativeButtonClickListener != null) {
//                    Log.e(TAG, "set negativeButtonClickListener");

                    negativeButton.setText(negativeButtonText);
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
//                                    Log.e(TAG, "positiveButton onClick!");
                            //positiveButton.setBackgroundResource(R.drawable.button_blue);
                            //negativeButton.setBackgroundResource(R.drawable.button_silver);
                            //negativeButtonClickListener
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
//                Log.e(TAG, "negativeButton Gone! ");
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content view
            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
//                Log.e(TAG, "contentView != null");
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView/*, new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT)*/);
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }
    


}
