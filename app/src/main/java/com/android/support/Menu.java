package com.android.support;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.internal.view.SupportMenu;
import com.json.nb;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

/* loaded from: /tmp/dex_extract/classes6.dex */
public class Menu {
    public static final String TAG = "Mod_Menu";
    Context getContext;
    LinearLayout mCollapse;
    RelativeLayout mCollapsed;
    LinearLayout mExpanded;
    RelativeLayout mRootContainer;
    LinearLayout mSettings;
    WindowManager mWindowManager;
    LinearLayout mods;
    boolean overlayRequired;
    FrameLayout rootFrame;
    LinearLayout.LayoutParams scrlLL;
    LinearLayout.LayoutParams scrlLLExpanded;
    ScrollView scrollView;
    ImageView startimage;
    boolean stopChecking;
    WindowManager.LayoutParams vmParams;
    int TEXT_COLOR = Color.parseColor("#82CAFD");
    int TEXT_COLOR_2 = Color.parseColor("#FFFFFF");
    int BTN_COLOR = Color.parseColor("#1C262D");
    int MENU_BG_COLOR = Color.parseColor("#EE1C2A35");
    int MENU_FEATURE_BG_COLOR = Color.parseColor("#DD141C22");
    int MENU_WIDTH = 290;
    int MENU_HEIGHT = 210;
    int POS_X = 0;
    int POS_Y = 100;
    float MENU_CORNER = 4.0f;
    int ICON_SIZE = 45;
    float ICON_ALPHA = 0.7f;
    int ToggleON = -16711936;
    int ToggleOFF = SupportMenu.CATEGORY_MASK;
    int BtnON = Color.parseColor("#1b5e20");
    int BtnOFF = Color.parseColor("#7f0000");
    int CategoryBG = Color.parseColor("#2F3D4C");
    int SeekBarColor = Color.parseColor("#80CBC4");
    int SeekBarProgressColor = Color.parseColor("#80CBC4");
    int CheckBoxColor = Color.parseColor("#80CBC4");
    int RadioColor = Color.parseColor("#FFFFFF");
    int CollapseColor = Color.parseColor("#232F2C");
    String NumberTxtColor = "#41c300";

    native String[] GetFeatureList();

    native String Icon();

    native String IconWebViewData();

    native void Init(Context context, TextView textView, TextView textView2);

    native boolean IsGameLibLoaded();

    native String[] SettingsList();

    public Menu(Context context) {
        this.getContext = context;
        Preferences.context = context;
        this.rootFrame = new FrameLayout(context);
        this.rootFrame.setOnTouchListener(onTouchListener());
        this.mRootContainer = new RelativeLayout(context);
        this.mCollapsed = new RelativeLayout(context);
        this.mCollapsed.setVisibility(0);
        this.mCollapsed.setAlpha(this.ICON_ALPHA);
        this.mExpanded = new LinearLayout(context);
        this.mExpanded.setVisibility(8);
        this.mExpanded.setBackgroundColor(this.MENU_BG_COLOR);
        this.mExpanded.setOrientation(1);
        this.mExpanded.setLayoutParams(new LinearLayout.LayoutParams(dp(this.MENU_WIDTH), -2));
        GradientDrawable gdMenuBody = new GradientDrawable();
        gdMenuBody.setCornerRadius(this.MENU_CORNER);
        gdMenuBody.setColor(this.MENU_BG_COLOR);
        gdMenuBody.setStroke(1, Color.parseColor("#32cb00"));
        this.startimage = new ImageView(context);
        this.startimage.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
        int applyDimension = (int) TypedValue.applyDimension(1, this.ICON_SIZE, context.getResources().getDisplayMetrics());
        this.startimage.getLayoutParams().height = applyDimension;
        this.startimage.getLayoutParams().width = applyDimension;
        this.startimage.setScaleType(ImageView.ScaleType.FIT_XY);
        byte[] decode = Base64.decode(Icon(), 0);
        this.startimage.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
        ((ViewGroup.MarginLayoutParams) this.startimage.getLayoutParams()).topMargin = convertDipToPixels(10);
        this.startimage.setOnTouchListener(onTouchListener());
        this.startimage.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Menu.this.mCollapsed.setVisibility(8);
                Menu.this.mExpanded.setVisibility(0);
            }
        });
        WebView wView = new WebView(context);
        wView.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
        int applyDimension2 = (int) TypedValue.applyDimension(1, this.ICON_SIZE, context.getResources().getDisplayMetrics());
        wView.getLayoutParams().height = applyDimension2;
        wView.getLayoutParams().width = applyDimension2;
        wView.loadData("<html><head></head><body style=\"margin: 0; padding: 0\"><img src=\"" + IconWebViewData() + "\" width=\"" + this.ICON_SIZE + "\" height=\"" + this.ICON_SIZE + "\" ></body></html>", "text/html", nb.N);
        wView.setBackgroundColor(0);
        wView.setAlpha(this.ICON_ALPHA);
        wView.getSettings().setCacheMode(2);
        wView.setOnTouchListener(onTouchListener());
        TextView settings = new TextView(context);
        settings.setText(Build.VERSION.SDK_INT >= 23 ? "⚙" : "🔧");
        settings.setTextColor(this.TEXT_COLOR);
        settings.setTypeface(Typeface.DEFAULT_BOLD);
        settings.setTextSize(20.0f);
        RelativeLayout.LayoutParams rlsettings = new RelativeLayout.LayoutParams(-2, -2);
        rlsettings.addRule(11);
        settings.setLayoutParams(rlsettings);
        settings.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.2
            boolean settingsOpen;

            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                try {
                    this.settingsOpen = !this.settingsOpen;
                    if (this.settingsOpen) {
                        Menu.this.scrollView.removeView(Menu.this.mods);
                        Menu.this.scrollView.addView(Menu.this.mSettings);
                        Menu.this.scrollView.scrollTo(0, 0);
                    } else {
                        Menu.this.scrollView.removeView(Menu.this.mSettings);
                        Menu.this.scrollView.addView(Menu.this.mods);
                    }
                } catch (IllegalStateException e) {
                }
            }
        });
        this.mSettings = new LinearLayout(context);
        this.mSettings.setOrientation(1);
        featureList(SettingsList(), this.mSettings);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setPadding(10, 5, 10, 5);
        relativeLayout.setVerticalGravity(16);
        TextView title = new TextView(context);
        title.setTextColor(this.TEXT_COLOR);
        title.setTextSize(18.0f);
        title.setGravity(17);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-2, -2);
        rl.addRule(14);
        title.setLayoutParams(rl);
        TextView subTitle = new TextView(context);
        subTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        subTitle.setMarqueeRepeatLimit(-1);
        subTitle.setSingleLine(true);
        subTitle.setSelected(true);
        subTitle.setTextColor(this.TEXT_COLOR);
        subTitle.setTextSize(10.0f);
        subTitle.setGravity(17);
        subTitle.setPadding(0, 0, 0, 5);
        this.scrollView = new ScrollView(context);
        this.scrlLL = new LinearLayout.LayoutParams(-1, dp(this.MENU_HEIGHT));
        this.scrlLLExpanded = new LinearLayout.LayoutParams(this.mExpanded.getLayoutParams());
        this.scrlLLExpanded.weight = 1.0f;
        this.scrollView.setLayoutParams(Preferences.isExpanded ? this.scrlLLExpanded : this.scrlLL);
        this.scrollView.setBackgroundColor(this.MENU_FEATURE_BG_COLOR);
        this.mods = new LinearLayout(context);
        this.mods.setOrientation(1);
        RelativeLayout relativeLayout2 = new RelativeLayout(context);
        relativeLayout2.setPadding(10, 3, 10, 3);
        relativeLayout2.setVerticalGravity(17);
        RelativeLayout.LayoutParams lParamsHideBtn = new RelativeLayout.LayoutParams(-2, -2);
        lParamsHideBtn.addRule(9);
        Button hideBtn = new Button(context);
        hideBtn.setLayoutParams(lParamsHideBtn);
        hideBtn.setBackgroundColor(0);
        hideBtn.setText("HIDE/KILL (Hold)");
        hideBtn.setTextColor(this.TEXT_COLOR);
        hideBtn.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Menu.this.mCollapsed.setVisibility(0);
                Menu.this.mCollapsed.setAlpha(0.0f);
                Menu.this.mExpanded.setVisibility(8);
                Toast.makeText(view.getContext(), "Icon hidden. Remember the hidden icon position", 1).show();
            }
        });
        hideBtn.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.support.Menu.4
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "Menu killed", 1).show();
                Menu.this.rootFrame.removeView(Menu.this.mRootContainer);
                Menu.this.mWindowManager.removeView(Menu.this.rootFrame);
                return false;
            }
        });
        RelativeLayout.LayoutParams lParamsCloseBtn = new RelativeLayout.LayoutParams(-2, -2);
        lParamsCloseBtn.addRule(11);
        Button closeBtn = new Button(context);
        closeBtn.setLayoutParams(lParamsCloseBtn);
        closeBtn.setBackgroundColor(0);
        closeBtn.setText("MINIMIZE");
        closeBtn.setTextColor(this.TEXT_COLOR);
        closeBtn.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Menu.this.mCollapsed.setVisibility(0);
                Menu.this.mCollapsed.setAlpha(Menu.this.ICON_ALPHA);
                Menu.this.mExpanded.setVisibility(8);
            }
        });
        this.mRootContainer.addView(this.mCollapsed);
        this.mRootContainer.addView(this.mExpanded);
        if (IconWebViewData() == null) {
            this.mCollapsed.addView(this.startimage);
        } else {
            this.mCollapsed.addView(wView);
        }
        relativeLayout.addView(title);
        relativeLayout.addView(settings);
        this.mExpanded.addView(relativeLayout);
        this.mExpanded.addView(subTitle);
        this.scrollView.addView(this.mods);
        this.mExpanded.addView(this.scrollView);
        relativeLayout2.addView(hideBtn);
        relativeLayout2.addView(closeBtn);
        this.mExpanded.addView(relativeLayout2);
        Init(context, title, subTitle);
    }

    public void ShowMenu() {
        this.rootFrame.addView(this.mRootContainer);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() { // from class: com.android.support.Menu.6
            boolean viewLoaded = false;

            @Override // java.lang.Runnable
            public void run() {
                if (Preferences.loadPref && !Menu.this.IsGameLibLoaded() && !Menu.this.stopChecking) {
                    if (!this.viewLoaded) {
                        Menu.this.Category(Menu.this.mods, "Save preferences was been enabled. Waiting for game lib to be loaded...\n\nForce load menu may not apply mods instantly. You would need to reactivate them again");
                        Menu.this.Button(Menu.this.mods, -100, "Force load menu");
                        this.viewLoaded = true;
                    }
                    handler.postDelayed(this, 600L);
                    return;
                }
                Menu.this.mods.removeAllViews();
                Menu.this.featureList(Menu.this.GetFeatureList(), Menu.this.mods);
            }
        }, 500L);
    }

    public void SetWindowManagerWindowService() {
        int iparams = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        this.vmParams = new WindowManager.LayoutParams(-2, -2, iparams, 67108872, -3);
        this.vmParams.gravity = 51;
        this.vmParams.x = this.POS_X;
        this.vmParams.y = this.POS_Y;
        this.mWindowManager = (WindowManager) this.getContext.getSystemService("window");
        this.mWindowManager.addView(this.rootFrame, this.vmParams);
        this.overlayRequired = true;
    }

    public void SetWindowManagerActivity() {
        this.vmParams = new WindowManager.LayoutParams(-2, -2, this.POS_X, this.POS_Y, 2, 41943304, -2);
        this.vmParams.gravity = 51;
        this.vmParams.x = this.POS_X;
        this.vmParams.y = this.POS_Y;
        this.mWindowManager = ((Activity) this.getContext).getWindowManager();
        this.mWindowManager.addView(this.rootFrame, this.vmParams);
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() { // from class: com.android.support.Menu.7
            final View collapsedView;
            final View expandedView;
            private float initialTouchX;
            private float initialTouchY;
            private int initialX;
            private int initialY;

            {
                this.collapsedView = Menu.this.mCollapsed;
                this.expandedView = Menu.this.mExpanded;
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        this.initialX = Menu.this.vmParams.x;
                        this.initialY = Menu.this.vmParams.y;
                        this.initialTouchX = motionEvent.getRawX();
                        this.initialTouchY = motionEvent.getRawY();
                        return true;
                    case 1:
                        int rawX = (int) (motionEvent.getRawX() - this.initialTouchX);
                        int rawY = (int) (motionEvent.getRawY() - this.initialTouchY);
                        Menu.this.mExpanded.setAlpha(1.0f);
                        Menu.this.mCollapsed.setAlpha(1.0f);
                        if (rawX < 10 && rawY < 10 && Menu.this.isViewCollapsed()) {
                            try {
                                this.collapsedView.setVisibility(8);
                                this.expandedView.setVisibility(0);
                            } catch (NullPointerException e) {
                            }
                        }
                        return true;
                    case 2:
                        Menu.this.mExpanded.setAlpha(0.5f);
                        Menu.this.mCollapsed.setAlpha(0.5f);
                        Menu.this.vmParams.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                        Menu.this.vmParams.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                        Menu.this.mWindowManager.updateViewLayout(Menu.this.rootFrame, Menu.this.vmParams);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void featureList(String[] listFT, LinearLayout linearLayout) {
        boolean switchedOn;
        LinearLayout linearLayout2;
        int subFeat;
        String feature;
        int featNum;
        char c;
        LinearLayout llBak;
        String[] strSplit;
        int i;
        String[] strArr = listFT;
        LinearLayout llBak2 = linearLayout;
        int i2 = 0;
        int subFeat2 = 0;
        while (i2 < strArr.length) {
            String feature2 = strArr[i2];
            if (!feature2.contains("_True")) {
                switchedOn = false;
            } else {
                feature2 = feature2.replaceFirst("_True", "");
                switchedOn = true;
            }
            LinearLayout linearLayout3 = llBak2;
            if (!feature2.contains("CollapseAdd_")) {
                linearLayout2 = linearLayout3;
            } else {
                LinearLayout linearLayout4 = this.mCollapse;
                feature2 = feature2.replaceFirst("CollapseAdd_", "");
                linearLayout2 = linearLayout4;
            }
            String[] str = feature2.split("_");
            if (TextUtils.isDigitsOnly(str[0]) || str[0].matches("-[0-9]*")) {
                int featNum2 = Integer.parseInt(str[0]);
                subFeat = subFeat2 + 1;
                feature = feature2.replaceFirst(str[0] + "_", "");
                featNum = featNum2;
            } else {
                subFeat = subFeat2;
                feature = feature2;
                featNum = i2 - subFeat2;
            }
            String[] strSplit2 = feature.split("_");
            String str2 = strSplit2[0];
            switch (str2.hashCode()) {
                case -1943191956:
                    if (str2.equals("ButtonLink")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case -1784436876:
                    if (str2.equals("Toggle")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -923700249:
                    if (str2.equals("InputValue")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -658531749:
                    if (str2.equals("SeekBar")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -584041481:
                    if (str2.equals("InputText")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -567441459:
                    if (str2.equals("Collapse")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -339785223:
                    if (str2.equals("Spinner")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -106518818:
                    if (str2.equals("ButtonOnOff")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 82909838:
                    if (str2.equals("RichTextView")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case 115155230:
                    if (str2.equals("Category")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case 427235197:
                    if (str2.equals("RichWebView")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case 776382189:
                    if (str2.equals("RadioButton")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case 1133277359:
                    if (str2.equals("InputLValue")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 1601505219:
                    if (str2.equals("CheckBox")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 2001146706:
                    if (str2.equals("Button")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    llBak = llBak2;
                    Switch(linearLayout2, featNum, strSplit2[1], switchedOn);
                    break;
                case 1:
                    llBak = llBak2;
                    SeekBar(linearLayout2, featNum, strSplit2[1], Integer.parseInt(strSplit2[2]), Integer.parseInt(strSplit2[3]));
                    break;
                case 2:
                    llBak = llBak2;
                    Button(linearLayout2, featNum, strSplit2[1]);
                    break;
                case 3:
                    llBak = llBak2;
                    ButtonOnOff(linearLayout2, featNum, strSplit2[1], switchedOn);
                    break;
                case 4:
                    llBak = llBak2;
                    TextView(linearLayout2, strSplit2[1]);
                    Spinner(linearLayout2, featNum, strSplit2[1], strSplit2[2]);
                    break;
                case 5:
                    llBak = llBak2;
                    InputText(linearLayout2, featNum, strSplit2[1]);
                    break;
                case 6:
                    llBak = llBak2;
                    if (strSplit2.length == 3) {
                        InputNum(linearLayout2, featNum, strSplit2[2], Integer.parseInt(strSplit2[1]));
                    }
                    if (strSplit2.length != 2) {
                        break;
                    } else {
                        InputNum(linearLayout2, featNum, strSplit2[1], 0);
                        break;
                    }
                case 7:
                    if (strSplit2.length != 3) {
                        strSplit = strSplit2;
                        llBak = llBak2;
                        i = 2;
                    } else {
                        strSplit = strSplit2;
                        llBak = llBak2;
                        i = 2;
                        InputLNum(linearLayout2, featNum, strSplit2[2], Long.parseLong(strSplit2[1]));
                    }
                    if (strSplit.length != i) {
                        break;
                    } else {
                        InputLNum(linearLayout2, featNum, strSplit[1], 0L);
                        break;
                    }
                case '\b':
                    CheckBox(linearLayout2, featNum, strSplit2[1], switchedOn);
                    llBak = llBak2;
                    break;
                case '\t':
                    RadioButton(linearLayout2, featNum, strSplit2[1], strSplit2[2]);
                    llBak = llBak2;
                    break;
                case '\n':
                    Collapse(linearLayout2, strSplit2[1], switchedOn);
                    subFeat++;
                    llBak = llBak2;
                    break;
                case 11:
                    subFeat++;
                    ButtonLink(linearLayout2, strSplit2[1], strSplit2[2]);
                    llBak = llBak2;
                    break;
                case '\f':
                    subFeat++;
                    Category(linearLayout2, strSplit2[1]);
                    llBak = llBak2;
                    break;
                case '\r':
                    subFeat++;
                    TextView(linearLayout2, strSplit2[1]);
                    llBak = llBak2;
                    break;
                case 14:
                    subFeat++;
                    WebTextView(linearLayout2, strSplit2[1]);
                    llBak = llBak2;
                    break;
                default:
                    llBak = llBak2;
                    break;
            }
            subFeat2 = subFeat;
            i2++;
            strArr = listFT;
            llBak2 = llBak;
        }
    }

    private void Switch(LinearLayout linLayout, final int featNum, final String featName, boolean swiOn) {
        final Switch switchR = new Switch(this.getContext);
        ColorStateList buttonStates = new ColorStateList(new int[][]{new int[]{-16842910}, new int[]{android.R.attr.state_checked}, new int[0]}, new int[]{-16776961, this.ToggleON, this.ToggleOFF});
        try {
            switchR.getThumbDrawable().setTintList(buttonStates);
            switchR.getTrackDrawable().setTintList(buttonStates);
        } catch (NullPointerException ex) {
            Log.d(TAG, String.valueOf(ex));
        }
        switchR.setText(featName);
        switchR.setTextColor(this.TEXT_COLOR_2);
        switchR.setPadding(10, 5, 0, 5);
        switchR.setChecked(Preferences.loadPrefBool(featName, featNum, swiOn));
        switchR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.support.Menu.8
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                Preferences.changeFeatureBool(featName, featNum, bool);
                switch (featNum) {
                    case -3:
                        Preferences.isExpanded = bool;
                        ScrollView scrollView = Menu.this.scrollView;
                        Menu menu = Menu.this;
                        scrollView.setLayoutParams(bool ? menu.scrlLLExpanded : menu.scrlLL);
                        break;
                    case -1:
                        Preferences.with(switchR.getContext()).writeBoolean(-1, bool);
                        if (!bool) {
                            Preferences.with(switchR.getContext()).clear();
                            break;
                        }
                        break;
                }
            }
        });
        linLayout.addView(switchR);
    }

    private void SeekBar(LinearLayout linearLayout, final int featNum, final String featName, final int min, int max) {
        int i;
        int loadedProg = Preferences.loadPrefInt(featName, featNum);
        LinearLayout linearLayout2 = new LinearLayout(this.getContext);
        linearLayout2.setPadding(10, 5, 0, 5);
        linearLayout2.setOrientation(1);
        linearLayout2.setGravity(17);
        final TextView textView = new TextView(this.getContext);
        textView.setText(Html.fromHtml(featName + ": <font color='" + this.NumberTxtColor + "'>" + (loadedProg == 0 ? min : loadedProg)));
        textView.setTextColor(this.TEXT_COLOR_2);
        SeekBar seekBar = new SeekBar(this.getContext);
        seekBar.setPadding(25, 10, 35, 10);
        seekBar.setMax(max);
        if (Build.VERSION.SDK_INT < 26) {
            i = min;
        } else {
            i = min;
            seekBar.setMin(i);
        }
        seekBar.setProgress(loadedProg == 0 ? i : loadedProg);
        seekBar.getThumb().setColorFilter(this.SeekBarColor, PorterDuff.Mode.SRC_ATOP);
        seekBar.getProgressDrawable().setColorFilter(this.SeekBarProgressColor, PorterDuff.Mode.SRC_ATOP);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.android.support.Menu.9
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i2, boolean z) {
                seekBar2.setProgress(i2 < min ? min : i2);
                Preferences.changeFeatureInt(featName, featNum, i2 < min ? min : i2);
                textView.setText(Html.fromHtml(featName + ": <font color='" + Menu.this.NumberTxtColor + "'>" + (i2 < min ? min : i2)));
            }
        });
        linearLayout2.addView(textView);
        linearLayout2.addView(seekBar);
        linearLayout.addView(linearLayout2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Button(LinearLayout linearLayout, final int featNum, final String featName) {
        Button button = new Button(this.getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(7, 5, 7, 5);
        button.setLayoutParams(layoutParams);
        button.setTextColor(this.TEXT_COLOR_2);
        button.setAllCaps(false);
        button.setText(Html.fromHtml(featName));
        button.setBackgroundColor(this.BTN_COLOR);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                switch (featNum) {
                    case -100:
                        Menu.this.stopChecking = true;
                        break;
                    case -6:
                        Menu.this.scrollView.removeView(Menu.this.mSettings);
                        Menu.this.scrollView.addView(Menu.this.mods);
                        break;
                }
                Preferences.changeFeatureInt(featName, featNum, 0);
            }
        });
        linearLayout.addView(button);
    }

    private void ButtonLink(LinearLayout linearLayout, String featName, final String url) {
        Button button = new Button(this.getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(7, 5, 7, 5);
        button.setLayoutParams(layoutParams);
        button.setAllCaps(false);
        button.setTextColor(this.TEXT_COLOR_2);
        button.setText(Html.fromHtml(featName));
        button.setBackgroundColor(this.BTN_COLOR);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setFlags(268435456);
                intent.setData(Uri.parse(url));
                Menu.this.getContext.startActivity(intent);
            }
        });
        linearLayout.addView(button);
    }

    private void ButtonOnOff(LinearLayout linearLayout, final int featNum, String featName, boolean switchedOn) {
        boolean isOn;
        final Button button = new Button(this.getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(7, 5, 7, 5);
        button.setLayoutParams(layoutParams);
        button.setTextColor(this.TEXT_COLOR_2);
        button.setAllCaps(false);
        final String finalfeatName = featName.replace("OnOff_", "");
        boolean isOn2 = Preferences.loadPrefBool(featName, featNum, switchedOn);
        if (isOn2) {
            button.setText(Html.fromHtml(finalfeatName + ": ON"));
            button.setBackgroundColor(this.BtnON);
            isOn = false;
        } else {
            button.setText(Html.fromHtml(finalfeatName + ": OFF"));
            button.setBackgroundColor(this.BtnOFF);
            isOn = true;
        }
        final boolean finalIsOn = isOn;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.12
            boolean isOn;

            {
                this.isOn = finalIsOn;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Preferences.changeFeatureBool(finalfeatName, featNum, this.isOn);
                if (this.isOn) {
                    button.setText(Html.fromHtml(finalfeatName + ": ON"));
                    button.setBackgroundColor(Menu.this.BtnON);
                    this.isOn = false;
                } else {
                    button.setText(Html.fromHtml(finalfeatName + ": OFF"));
                    button.setBackgroundColor(Menu.this.BtnOFF);
                    this.isOn = true;
                }
            }
        });
        linearLayout.addView(button);
    }

    private void Spinner(LinearLayout linearLayout, final int featNum, String featName, String list) {
        Log.d(TAG, "spinner " + featNum + " " + featName + " " + list);
        List<String> lists = new LinkedList<>(Arrays.asList(list.split(",")));
        LinearLayout linearLayout2 = new LinearLayout(this.getContext);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.setMargins(7, 2, 7, 2);
        linearLayout2.setOrientation(1);
        linearLayout2.setBackgroundColor(this.BTN_COLOR);
        linearLayout2.setLayoutParams(layoutParams2);
        final Spinner spinner = new Spinner(this.getContext, 1);
        spinner.setLayoutParams(layoutParams2);
        spinner.getBackground().setColorFilter(1, PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter aa = new ArrayAdapter(this.getContext, android.R.layout.simple_spinner_dropdown_item, lists);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((SpinnerAdapter) aa);
        spinner.setSelection(Preferences.loadPrefInt(featName, featNum));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.android.support.Menu.13
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Preferences.changeFeatureInt(spinner.getSelectedItem().toString(), featNum, position);
                ((TextView) parentView.getChildAt(0)).setTextColor(Menu.this.TEXT_COLOR_2);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        linearLayout2.addView(spinner);
        linearLayout.addView(linearLayout2);
    }

    private void InputNum(LinearLayout linearLayout, final int featNum, final String featName, final int maxValue) {
        LinearLayout linearLayout2 = new LinearLayout(this.getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(7, 5, 7, 5);
        final Button button = new Button(this.getContext);
        int num = Preferences.loadPrefInt(featName, featNum);
        button.setText(Html.fromHtml(featName + ": <font color='" + this.NumberTxtColor + "'>" + num + "</font>"));
        button.setAllCaps(false);
        button.setLayoutParams(layoutParams);
        button.setBackgroundColor(this.BTN_COLOR);
        button.setTextColor(this.TEXT_COLOR_2);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AlertDialog.Builder alertName = new AlertDialog.Builder(Menu.this.getContext);
                final EditText editText = new EditText(Menu.this.getContext);
                if (maxValue != 0) {
                    editText.setHint("Max value: " + maxValue);
                }
                editText.setInputType(2);
                editText.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
                InputFilter[] FilterArray = {new InputFilter.LengthFilter(10)};
                editText.setFilters(FilterArray);
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.android.support.Menu.14.1
                    @Override // android.view.View.OnFocusChangeListener
                    public void onFocusChange(View v, boolean hasFocus) {
                        Context context = Menu.this.getContext;
                        Context context2 = Menu.this.getContext;
                        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
                        if (hasFocus) {
                            imm.toggleSoftInput(2, 1);
                        } else {
                            imm.toggleSoftInput(1, 0);
                        }
                    }
                });
                editText.requestFocus();
                alertName.setTitle("Input number");
                alertName.setView(editText);
                LinearLayout layoutName = new LinearLayout(Menu.this.getContext);
                layoutName.setOrientation(1);
                layoutName.addView(editText);
                alertName.setView(layoutName);
                alertName.setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.android.support.Menu.14.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int num2;
                        try {
                            String inp = editText.getText().toString();
                            num2 = Integer.parseInt(inp.isEmpty() ? "0" : inp);
                            if (maxValue != 0 && num2 >= maxValue) {
                                num2 = maxValue;
                            }
                        } catch (NumberFormatException e) {
                            num2 = maxValue != 0 ? maxValue : Integer.MAX_VALUE;
                        }
                        button.setText(Html.fromHtml(featName + ": <font color='" + Menu.this.NumberTxtColor + "'>" + num2 + "</font>"));
                        Preferences.changeFeatureInt(featName, featNum, num2);
                        editText.setFocusable(false);
                    }
                });
                alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.android.support.Menu.14.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Context context = Menu.this.getContext;
                        Context context2 = Menu.this.getContext;
                        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
                        imm.toggleSoftInput(1, 0);
                    }
                });
                if (Menu.this.overlayRequired) {
                    AlertDialog dialog = alertName.create();
                    dialog.getWindow().setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
                    dialog.show();
                    return;
                }
                alertName.show();
            }
        });
        linearLayout2.addView(button);
        linearLayout.addView(linearLayout2);
    }

    private void InputLNum(LinearLayout linearLayout, final int featNum, final String featName, final long maxValue) {
        LinearLayout linearLayout2 = new LinearLayout(this.getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(7, 5, 7, 5);
        final Button button = new Button(this.getContext);
        long num = Preferences.loadPrefLong(featName, featNum);
        button.setText(Html.fromHtml(featName + ": <font color='" + this.NumberTxtColor + "'>" + num + "</font>"));
        button.setAllCaps(false);
        button.setLayoutParams(layoutParams);
        button.setBackgroundColor(this.BTN_COLOR);
        button.setTextColor(this.TEXT_COLOR_2);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.15
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AlertDialog.Builder alertName = new AlertDialog.Builder(Menu.this.getContext);
                final EditText editText = new EditText(Menu.this.getContext);
                if (maxValue != 0) {
                    editText.setHint("Max value: " + maxValue);
                }
                editText.setInputType(2);
                editText.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
                InputFilter[] FilterArray = {new InputFilter.LengthFilter(20)};
                editText.setFilters(FilterArray);
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.android.support.Menu.15.1
                    @Override // android.view.View.OnFocusChangeListener
                    public void onFocusChange(View v, boolean hasFocus) {
                        Context context = Menu.this.getContext;
                        Context context2 = Menu.this.getContext;
                        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
                        if (hasFocus) {
                            imm.toggleSoftInput(2, 1);
                        } else {
                            imm.toggleSoftInput(1, 0);
                        }
                    }
                });
                editText.requestFocus();
                alertName.setTitle("Input number");
                alertName.setView(editText);
                LinearLayout layoutName = new LinearLayout(Menu.this.getContext);
                layoutName.setOrientation(1);
                layoutName.addView(editText);
                alertName.setView(layoutName);
                alertName.setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.android.support.Menu.15.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        long num2;
                        try {
                            String inp = editText.getText().toString();
                            num2 = Long.parseLong(inp.isEmpty() ? "0" : inp);
                            if (maxValue != 0 && num2 >= maxValue) {
                                num2 = maxValue;
                            }
                        } catch (NumberFormatException e) {
                            num2 = maxValue != 0 ? maxValue : LongCompanionObject.MAX_VALUE;
                        }
                        button.setText(Html.fromHtml(featName + ": <font color='" + Menu.this.NumberTxtColor + "'>" + num2 + "</font>"));
                        Preferences.changeFeatureLong(featName, featNum, num2);
                        editText.setFocusable(false);
                    }
                });
                alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.android.support.Menu.15.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Context context = Menu.this.getContext;
                        Context context2 = Menu.this.getContext;
                        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
                        imm.toggleSoftInput(1, 0);
                    }
                });
                if (Menu.this.overlayRequired) {
                    AlertDialog dialog = alertName.create();
                    dialog.getWindow().setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
                    dialog.show();
                    return;
                }
                alertName.show();
            }
        });
        linearLayout2.addView(button);
        linearLayout.addView(linearLayout2);
    }

    private void InputText(LinearLayout linearLayout, final int featNum, final String featName) {
        LinearLayout linearLayout2 = new LinearLayout(this.getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(7, 5, 7, 5);
        final Button button = new Button(this.getContext);
        String string = Preferences.loadPrefString(featName, featNum);
        button.setText(Html.fromHtml(featName + ": <font color='" + this.NumberTxtColor + "'>" + string + "</font>"));
        button.setAllCaps(false);
        button.setLayoutParams(layoutParams);
        button.setBackgroundColor(this.BTN_COLOR);
        button.setTextColor(this.TEXT_COLOR_2);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.16
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AlertDialog.Builder alertName = new AlertDialog.Builder(Menu.this.getContext);
                final EditText editText = new EditText(Menu.this.getContext);
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.android.support.Menu.16.1
                    @Override // android.view.View.OnFocusChangeListener
                    public void onFocusChange(View v, boolean hasFocus) {
                        Context context = Menu.this.getContext;
                        Context context2 = Menu.this.getContext;
                        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
                        if (hasFocus) {
                            imm.toggleSoftInput(2, 1);
                        } else {
                            imm.toggleSoftInput(1, 0);
                        }
                    }
                });
                editText.requestFocus();
                alertName.setTitle("Input text");
                alertName.setView(editText);
                LinearLayout layoutName = new LinearLayout(Menu.this.getContext);
                layoutName.setOrientation(1);
                layoutName.addView(editText);
                alertName.setView(layoutName);
                alertName.setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.android.support.Menu.16.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String str = editText.getText().toString();
                        button.setText(Html.fromHtml(featName + ": <font color='" + Menu.this.NumberTxtColor + "'>" + str + "</font>"));
                        Preferences.changeFeatureString(featName, featNum, str);
                        editText.setFocusable(false);
                    }
                });
                alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.android.support.Menu.16.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Context context = Menu.this.getContext;
                        Context context2 = Menu.this.getContext;
                        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
                        imm.toggleSoftInput(1, 0);
                    }
                });
                if (Menu.this.overlayRequired) {
                    AlertDialog dialog = alertName.create();
                    dialog.getWindow().setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
                    dialog.show();
                    return;
                }
                alertName.show();
            }
        });
        linearLayout2.addView(button);
        linearLayout.addView(linearLayout2);
    }

    private void CheckBox(LinearLayout linLayout, final int featNum, final String featName, boolean switchedOn) {
        final CheckBox checkBox = new CheckBox(this.getContext);
        checkBox.setText(featName);
        checkBox.setTextColor(this.TEXT_COLOR_2);
        checkBox.setButtonTintList(ColorStateList.valueOf(this.CheckBoxColor));
        checkBox.setChecked(Preferences.loadPrefBool(featName, featNum, switchedOn));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.support.Menu.17
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    Preferences.changeFeatureBool(featName, featNum, isChecked);
                } else {
                    Preferences.changeFeatureBool(featName, featNum, isChecked);
                }
            }
        });
        linLayout.addView(checkBox);
    }

    private void RadioButton(LinearLayout linearLayout, final int featNum, final String featName, String list) {
        List<String> lists = new LinkedList<>(Arrays.asList(list.split(",")));
        final TextView textView = new TextView(this.getContext);
        textView.setText(featName + ":");
        textView.setTextColor(this.TEXT_COLOR_2);
        final RadioGroup radioGroup = new RadioGroup(this.getContext);
        radioGroup.setPadding(10, 5, 10, 5);
        radioGroup.setOrientation(1);
        radioGroup.addView(textView);
        for (int i = 0; i < lists.size(); i++) {
            final RadioButton Radioo = new RadioButton(this.getContext);
            final String radioName = lists.get(i);
            View.OnClickListener first_radio_listener = new View.OnClickListener() { // from class: com.android.support.Menu.18
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    textView.setText(Html.fromHtml(featName + ": <font color='" + Menu.this.NumberTxtColor + "'>" + radioName));
                    Preferences.changeFeatureInt(featName, featNum, radioGroup.indexOfChild(Radioo));
                }
            };
            System.out.println(lists.get(i));
            Radioo.setText(lists.get(i));
            Radioo.setTextColor(-3355444);
            Radioo.setButtonTintList(ColorStateList.valueOf(this.RadioColor));
            Radioo.setOnClickListener(first_radio_listener);
            radioGroup.addView(Radioo);
        }
        int index = Preferences.loadPrefInt(featName, featNum);
        if (index > 0) {
            textView.setText(Html.fromHtml(featName + ": <font color='" + this.NumberTxtColor + "'>" + lists.get(index - 1)));
            ((RadioButton) radioGroup.getChildAt(index)).setChecked(true);
        }
        linearLayout.addView(radioGroup);
    }

    private void Collapse(LinearLayout linearLayout, final String text, final boolean expanded) {
        LinearLayout.LayoutParams layoutParamsLL = new LinearLayout.LayoutParams(-1, -1);
        layoutParamsLL.setMargins(0, 5, 0, 0);
        LinearLayout collapse = new LinearLayout(this.getContext);
        collapse.setLayoutParams(layoutParamsLL);
        collapse.setVerticalGravity(16);
        collapse.setOrientation(1);
        final LinearLayout collapseSub = new LinearLayout(this.getContext);
        collapseSub.setVerticalGravity(16);
        collapseSub.setPadding(0, 5, 0, 5);
        collapseSub.setOrientation(1);
        collapseSub.setBackgroundColor(Color.parseColor("#222D38"));
        collapseSub.setVisibility(8);
        this.mCollapse = collapseSub;
        final TextView textView = new TextView(this.getContext);
        textView.setBackgroundColor(this.CollapseColor);
        textView.setText("▽ " + text + " ▽");
        textView.setGravity(17);
        textView.setTextColor(this.TEXT_COLOR_2);
        textView.setTypeface(null, 1);
        textView.setPadding(0, 20, 0, 20);
        if (expanded) {
            collapseSub.setVisibility(0);
            textView.setText("△ " + text + " △");
        }
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.android.support.Menu.19
            boolean isChecked;

            {
                this.isChecked = expanded;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                boolean z = !this.isChecked;
                this.isChecked = z;
                if (z) {
                    collapseSub.setVisibility(0);
                    textView.setText("△ " + text + " △");
                } else {
                    collapseSub.setVisibility(8);
                    textView.setText("▽ " + text + " ▽");
                }
            }
        });
        collapse.addView(textView);
        collapse.addView(collapseSub);
        linearLayout.addView(collapse);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Category(LinearLayout linLayout, String text) {
        TextView textView = new TextView(this.getContext);
        textView.setBackgroundColor(this.CategoryBG);
        textView.setText(Html.fromHtml(text));
        textView.setGravity(17);
        textView.setTextColor(this.TEXT_COLOR_2);
        textView.setTypeface(null, 1);
        textView.setPadding(0, 5, 0, 5);
        linLayout.addView(textView);
    }

    private void TextView(LinearLayout linLayout, String text) {
        TextView textView = new TextView(this.getContext);
        textView.setText(Html.fromHtml(text));
        textView.setTextColor(this.TEXT_COLOR_2);
        textView.setPadding(10, 5, 10, 5);
        linLayout.addView(textView);
    }

    private void WebTextView(LinearLayout linLayout, String text) {
        WebView wView = new WebView(this.getContext);
        wView.loadData(text, "text/html", nb.N);
        wView.setBackgroundColor(0);
        wView.setPadding(0, 5, 0, 5);
        wView.getSettings().setCacheMode(2);
        linLayout.addView(wView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isViewCollapsed() {
        return this.rootFrame == null || this.mCollapsed.getVisibility() == 0;
    }

    private int convertDipToPixels(int i) {
        return (int) ((i * this.getContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private int dp(int i) {
        return (int) TypedValue.applyDimension(1, i, this.getContext.getResources().getDisplayMetrics());
    }

    public void setVisibility(int view) {
        if (this.rootFrame != null) {
            this.rootFrame.setVisibility(view);
        }
    }

    public void onDestroy() {
        if (this.rootFrame != null) {
            this.mWindowManager.removeView(this.rootFrame);
        }
    }
}
