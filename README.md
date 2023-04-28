# Text
Android自定义TextView，适配行高，字体大小&amp;组件高度严格受限于行高

### 引用
~~~ groovy
allprojects {
 repositories {
    mavenCentral()
    maven { url "https://jitpack.io" }
 }
}
~~~
~~~ groovy
dependencies {
    implementation 'com.github.xiaobaicz.text:text:1.0.1'
}
~~~

### 使用说明
1. lineHeightX 属性：通过指定行高，另Text组件高度严格按照(组件高度 = 行高 * 行数)进行计算，并自适应字体大小为行高的范围
2. includeFontPadding 属性：是否使用预留空白，默认不使用。(中英文一般不需要设置为true，只有一些比较高的文字才需要，如：ရြတ္ျဖစ္သြားမယ္)
~~~ xml
<cc.xiaobaicz.widgets.text.Text
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:background="#ff0"
    android:text="Hello\nHello"
    android:textColor="#000"
    android:textSize="50sp"
    android:includeFontPadding="true"
    app:lineHeightX="100dp" />
~~~
