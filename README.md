# SlidingRootNav

The library is a DrawerLayout-like ViewGroup, where a "drawer" is hidden under the content view, which can be shifted to make the drawer visible. It doesn't provide you with a drawer builder.

![GifSample](art/sample.gif)

## Gradle 
Add this into your dependencies block.
```
compile 'com.yarolegovich:sliding-root-nav:1.1.1'
```
## Sample
Please see the [sample app](sample/src/main/java/com/yarolegovich/slidingrootnav/sample) for a library usage example.

## Wiki
#### Usage:
 1. Create your content_view.xml ([example](sample/src/main/res/layout/activity_main.xml)) or construct a `View` programatically.
 2. Set the content view (for example, using `setContentView` in your activity).
 3. Create your menu.xml ([example](sample/src/main/res/layout/menu_left_drawer.xml)) or construct a `View` programatically.
 4. Now you need to inject the menu in your `onCreate`. You can specify transformations of a content view or use the default ones. 
```java
new SlidingRootNavBuilder(this)
  .withMenuLayout(R.layout.menu_left_drawer)
  .inject();
```

### API
#### Transformations
You can specify root transformations using `SlidingRootNavBuilder`.
```java
new SlidingRootNavBuilder(this)
  .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
  .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
  .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
  .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
  .addRootTransformation(customTransformation)
  .inject();
```
`customTransformation` in the above example is a user-created class that implements `RootTransformation` interface. For an example, refer to the [default transformations](library/src/main/java/com/yarolegovich/slidingrootnav/transform). 

#### Menu behavior
```java
new SlidingRootNavBuilder(this)
  .withMenuOpened(true) //Initial menu opened/closed state. Default == false
  .withMenuLocked(false) //If true, a user can't open or close the menu. Default == false.
  .withGravity(SlideGravity.LEFT) //If LEFT you can swipe a menu from left to right, if RIGHT - the direction is opposite. 
  .withSavedState(savedInstanceState) //If you call the method, layout will restore its opened/closed state
  .withContentClickableWhenMenuOpened(isClickable) //Pretty self-descriptive. Builder Default == true
```
#### Controling the layout
A call to `inject()` returns you an interface for controlling the layout.
```java
public interface SlidingRootNav {
    boolean isMenuClosed();
    boolean isMenuOpened();
    boolean isMenuLocked();
    void closeMenu();
    void closeMenu(boolean animated);
    void openMenu();
    void openMenu(boolean animated);
    void setMenuLocked(boolean locked);
    SlidingRootNavLayout getLayout(); //If for some reason you need to work directly with layout - you can
}
```

#### Callbacks
* Drag progress:
```java
builder.addDragListener(listener);

public interface DragListener {
  void onDrag(float progress); //Float between 0 and 1, where 1 is a fully visible menu
}

```
* Drag state changes:
```java
builder.addDragStateListener(listener);

public interface DragStateListener {
  void onDragStart();
  void onDragEnd(boolean isMenuOpened);
}
```

* Compatibility with `DrawerLayout.DrawerListener`:
```java
DrawerListenerAdapter adapter = new DrawerListenerAdapter(yourDrawerListener, viewToPassAsDrawer);
builder.addDragListener(listenerAdapter).addDragStateListener(listenerAdapter);
```

## Special thanks
Thanks to [Tayisiya Yurkiv](https://www.behance.net/yurkivt) for a beautiful GIF.

## License
```
Copyright 2017 Yaroslav Shevchuk

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
