package com.Sumit1334.ShimmerView;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.TextViewUtil;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;
import com.romainpiel.shimmer.ShimmerTextView;
import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


@DesignerComponent(version = 3,  description = "This extension for creating shimmer view by Sumit Kumar",category = ComponentCategory.EXTENSION,
        nonVisible = true,   iconName = "https://community.kodular.io/user_avatar/community.kodular.io/sumit1334/120/82654_2.png")
@SimpleObject(external = true)
@UsesLibraries(libraries="Textview.jar")
public class ShimmerView extends AndroidNonvisibleComponent {
    private Context context;
    private ComponentContainer container;
    private HashMap<Integer,ShimmerTextView> compoents=new HashMap<>();
    private HashMap<ShimmerTextView,Integer> ids=new HashMap<>();
    private HashMap<ShimmerButton,Integer> buttonComponent=new HashMap<>();
    private HashMap<Integer,ShimmerButton> buttonIDs=new HashMap<>();
    private String customfont="";
    private boolean isRepl=false;
    private ViewGroup.LayoutParams layoutParams;
    public ShimmerView(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.context=componentContainer.$context();
        this.container=componentContainer;
        this.isRepl=container.$form() instanceof ReplForm;
    }
    @SimpleEvent
    public void LongClick(int id,Object component){
        EventDispatcher.dispatchEvent(this,"LongClick",id,component);
    }
    @SimpleProperty(description = "Returns the custom font")
    public String CustomFont(){
        return this.customfont;
    }
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET)
    @SimpleProperty(description = "Set the custom font of created textview")
    public void CustomFont(String str){
        this.customfont=str;
    }
    @SimpleFunction(description = "Creates the text view with shimmer effect")
    public void CreateLabel(AndroidViewComponent in,int id,String text){
        ShimmerTextView textView=new ShimmerTextView(this.context);
        textView.setText(text);
        textView.setClickable(true);
        textView.setId(id);
        textView.setTextAlignment(2);
        ((LinearLayout) ((ViewGroup) in.getView()).getChildAt(0)).addView(textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clicked(ids.get((ShimmerTextView) view),view);
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LongClick(ids.get((ShimmerTextView) view),view);
                return false;
            }
        });
        compoents.put(id,textView);
        ids.put(textView,id);
    }
    @SimpleFunction
    public void SetBackgroundColor(int id,int color){
        if (compoents.containsKey(id)){
            this.compoents.get(id).setBackgroundColor(color);
        }else if (buttonIDs.containsKey(id)){
            this.buttonIDs.get(id).setBackgroundColor(color);
        }
    }
    @SimpleFunction
    public void SetSize(int id,int height,int width){
        if (compoents.containsKey(id)){
            View view=compoents.get(id);
            this.layoutParams=view.getLayoutParams();
            this.layoutParams.height=pixelstodp(height);
            this.layoutParams.width=pixelstodp(width);
            view.setLayoutParams(this.layoutParams);
        }else if (buttonIDs.containsKey(id)){
            View view=buttonIDs.get(id);
            this.layoutParams=view.getLayoutParams();
            this.layoutParams.height=pixelstodp(height);
            this.layoutParams.width=pixelstodp(width);
            view.setLayoutParams(this.layoutParams);
        }
    }
    @SimpleFunction
    public void Delete(int id){
        if (compoents.containsKey(id)){
            ViewGroup viewGroup=(ViewGroup) compoents.get(id).getParent();
            viewGroup.removeView(compoents.get(id));
            ids.remove(compoents.get(id));
            compoents.remove(id);
        }else if (buttonIDs.containsKey(id)){
            ViewGroup viewGroup=(ViewGroup) buttonIDs.get(id).getParent();
            viewGroup.removeView(buttonIDs.get(id));
            buttonComponent.remove(buttonIDs.get(id));
            buttonIDs.remove(id);
        }
    }
    @SimpleFunction(description = "")
    public void CreateButton(final AndroidViewComponent in,final int id,final String text){
        final ShimmerButton button=new ShimmerButton(this.context);
        button.setText(text);
        button.setTextAlignment(2);
        ((LinearLayout) ((ViewGroup) in.getView()).getChildAt(0)).addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clicked(buttonComponent.get((ShimmerButton) view),view);
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LongClick(ids.get((ShimmerButton) view),view);
                return false;
            }
        });
        buttonIDs.put(id,button);
        buttonComponent.put(button,id);
    }
    @SimpleFunction
    public boolean IsButton(Object component){
        return component instanceof ShimmerButton;
    }
    @SimpleFunction
    public boolean IsLabel(Object component){
        return component instanceof ShimmerTextView;
    }
    @SimpleFunction(description = "Start the shimmer")
    public void Start(int id,int direction,long duration){
        if (compoents.containsKey(id)) {
            Shimmer shimmer = new Shimmer();
            if (direction == 1)
                shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_LTR);

            else if (direction == 2)
                shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_RTL);
            else
                throw new YailRuntimeError("Invalid Direction", "The given direction is invalid");
            shimmer.setDuration(duration);
            shimmer.start(compoents.get(id));
        }else if (buttonIDs.containsKey(id)){
            Shimmer shimmer = new Shimmer();
            if (direction == 1)
                shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_LTR);

            else if (direction == 2)
                shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_RTL);
            else
                throw new YailRuntimeError("Invalid Direction", "The given direction is invalid");
            shimmer.setDuration(duration);
            shimmer.start(buttonIDs.get(id));
        }
    }
    @SimpleFunction(description = "return the component by its id")
    public Object GetComponent(int id){
        if (compoents.containsKey(id)){
            return this.compoents.get(id);
        } else if (buttonIDs.containsKey(id)){
            return this.buttonIDs.get(id);
        }
        return null;
    }
    @SimpleFunction(description = "return the ids by its component")
    public int GetId(Object component){
        if (component instanceof ShimmerTextView){
            return this.ids.get((ShimmerTextView) component);
        }else if (component instanceof ShimmerButton)
            return buttonComponent.get(component);
        return 0;
    }
    @SimpleFunction(description = "set the text of label by its id")
    public void SetText(int id,String text){
        if (compoents.containsKey(id)){
            compoents.get(id).setText(text);
        }else if (buttonIDs.containsKey(id)){
            buttonIDs.get(id).setText(text);
        }
    }
    @SimpleFunction(description = "set the font typeface of label by its id")
    public void Font(int id,boolean bold,int typeface,boolean italic){
        if (compoents.containsKey(id)) {
            TextViewUtil.setFontTypeface(compoents.get(id), typeface, bold, italic);
        }else if (buttonIDs.containsKey(id)){
            TextViewUtil.setFontTypeface(buttonIDs.get(id), typeface, bold, italic);
        }
    }
    @SimpleFunction(description = "Set the custom font typeface of label by reference ot its id")
    public void CustomFont(int id){
        TextView textView=null;
        if (compoents.containsKey(id)) {
            textView = (TextView) compoents.get(id);
        }else if (buttonIDs.containsKey(id)){
            textView=(TextView) buttonIDs.get(id);
        }
        if (!(textView==null)) {

            String path = this.customfont;
            Typeface titleTypeface;
            if (isRepl) {
                path = getAssetsPath() + path;
                titleTypeface = Typeface.createFromFile(path);
            } else {
                titleTypeface = Typeface.createFromAsset(this.context.getAssets(), path);
            }
            textView.setTypeface(titleTypeface);
        }
    }
    @SimpleFunction(description = "set the visibility of label by its id")
    public void SetVisible(int id,boolean visibility){
        int visibile;
        if (visibility==true)
            visibile=View.VISIBLE;
        else
            visibile=View.INVISIBLE;
        if (compoents.containsKey(id)){
            compoents.get(id).setVisibility(visibile);
        }else if (buttonIDs.containsKey(id)){
            buttonIDs.get(id).setVisibility(visibile);
        }

    }
    @SimpleFunction(description = "sets the shimmer color of label by its id")
    public void SetShimmerColor(int id,int color){
        if (compoents.containsKey(id)){
            compoents.get(id).setReflectionColor(color);
        }else if (buttonIDs.containsKey(id)){
            buttonIDs.get(id).setReflectionColor(color);
        }
    }
    @SimpleFunction(description = "Set the text color of label by its id")
    public void SetTextColor(int id,int color) {
        if (compoents.containsKey(id)) {
            compoents.get(id).setTextColor(color);
        }else if (buttonIDs.containsKey(id)){
            buttonIDs.get(id).setTextColor(color);
        }
    }
    @SimpleFunction(description = "set the text size of label by its id")
    public void SetFontSize(int id,float size){
        if (compoents.containsKey(id)){
            compoents.get(id).setTextSize(size);
        }else if (buttonIDs.containsKey(id)){
            buttonIDs.get(id).setTextSize(size);
        }
    }
    @SimpleEvent(description = "This event raises when label clicked")
    public void Clicked(int id,Object component){
        EventDispatcher.dispatchEvent(this,"Clicked",id,component);
    }
    @SimpleProperty(description = "This property for direction left to Right")
    public int LeftToRight(){
        return 1;
    }
    @SimpleProperty(description = "This property for direction right to left")
    public int RightToLeft(){
        return 2;
    }


    private File getExternalStoragePath() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            return Environment.getExternalStorageDirectory();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return context.getExternalFilesDir(null);
        } else {
            return Environment.getExternalStorageDirectory();
        }
    }


    private String getAssetsPath() {
        String externalStoragePath = getExternalStoragePath().toString();
        String pathToAssets = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            pathToAssets = externalStoragePath + "/assets/";
        } else {
            if (context.getPackageName().contains("makeroid")) {
                File pathToMakeroidAssets = new File(externalStoragePath + "/Makeroid/assets");
                File pathToKodularAssets = new File(externalStoragePath + "/Kodular/assets");
                if (pathToMakeroidAssets.exists()) {
                    pathToAssets = pathToMakeroidAssets.toString() + "/";
                } else if (pathToKodularAssets.exists()) {
                    pathToAssets = pathToKodularAssets.toString() + "/";
                }
            } else {
                File pathToAppInventorAssets = new File(externalStoragePath + "/AppInventor/assets");
                if  (pathToAppInventorAssets.exists()) {
                    pathToAssets = pathToAppInventorAssets.toString() + "/";
                }
            }
        }
        return pathToAssets;
    }

    private int pixelstodp(int size){
        return (int) (((float) size) * context.getResources().getDisplayMetrics().density);
    }

}