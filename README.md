# CallSlideView
滑动实现接听和挂断电话的View

## 效果图
1、向左滑动接听，向右滑动挂断   
2、向两边滑动时，颜色渐变   
3、颜色和字体可以在xml中完全自定义      

![image](https://github.com/gpfduoduo/CallSlideView/blob/master/result/demo.gif "效果图")

## 实现方法   
监听onTouhc，在运动的过程中不停的出发onDraw，画图同时颜色渐变


## 使用方法  
```JAVA
CallSliderView callView = (CallSliderView) findViewById(R.id.slider_view);
        callView.setSliderEndListener(new CallSliderView.SliderListener() {
            @Override
            public void onSliderEnd() {
                Toast.makeText(CallSliderEndViewActivity.this, "挂断", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onSliderListen() {
                Toast.makeText(CallSliderEndViewActivity.this, "接听", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
```


在 xml中
```JAVA
 <com.guo.duoduo.library.CallSliderView
        android:id="@+id/slider_view"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:layout_centerInParent="true"
        android:layout_margin="30dp"
        app:CallSliderView_backgroundColor="@color/gray"
        app:CallSliderView_progressBackgroundColor="@color/red"
        app:CallSliderView_text="滑动"
        app:CallSliderView_Left_Text="接听"
        app:CallSliderView_Right_Text="挂断"
        app:CallSliderView_textSize="20sp" />
```
