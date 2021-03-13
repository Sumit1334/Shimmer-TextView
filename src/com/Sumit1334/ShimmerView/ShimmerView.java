package com.Sumit1334.ShimmerView;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.HashMap;


@DesignerComponent(version = 1,  description = "This extension for creating shimmer view by Sumit Kumar",category = ComponentCategory.EXTENSION,
        nonVisible = true,   iconName = "https://community.kodular.io/user_avatar/community.kodular.io/sumit1334/120/82654_2.png")
@SimpleObject(external = true)
@UsesLibraries(libraries="Textview.jar")
public class ShimmerView extends AndroidNonvisibleComponent {
    private Context context;
    private HashMap<Integer,ShimmerTextView> compoents=new HashMap<>();
    private HashMap<ShimmerTextView,Integer> ids=new HashMap<>();
    public ShimmerView(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.context=componentContainer.$context();
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
        compoents.put(id,textView);
        ids.put(textView,id);
    }
    @SimpleFunction(description = "Start the shimmer")
    public void Start(int id,int direction,long duration){
        Shimmer shimmer=new Shimmer();
        if (direction==1)
            shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_LTR);

        else if (direction==2)
            shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_RTL);
        else
            throw new YailRuntimeError("Invalid Direction","The given direction is invalid");
        shimmer.setDuration(duration);
        shimmer.start(compoents.get(id));
    }
    @SimpleFunction(description = "return the component by its id")
    public Object GetComponent(int id){
        return compoents.get(id);
    }
    @SimpleFunction(description = "return the ids by its component")
    public int GetId(Object component){
        return ids.get(component);
    }
    @SimpleFunction(description = "set the text of label by its id")
    public void SetText(int id,String text){
        compoents.get(id).setText(text);
    }
    @SimpleFunction(description = "set the visibility of label by its id")
    public void SetVisible(int id,boolean visibility){
        int visibile;
        if (visibility==true)
            visibile=View.VISIBLE;
        else
            visibile=View.INVISIBLE;
        compoents.get(id).setVisibility(visibile);
    }
    @SimpleFunction(description = "sets the shimmer color of label by its id")
    public void SetShimmerColor(int id,int color){
        compoents.get(id).setReflectionColor(color);
    }
    @SimpleFunction(description = "Set the text color of label by its id")
    public void SetTextColor(int id,int color){
        compoents.get(id).setTextColor(color);
    }
    @SimpleFunction(description = "set the text size of label by its id")
    public void SetTextSize(int id,float size){
        compoents.get(id).setTextSize(size);
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
}