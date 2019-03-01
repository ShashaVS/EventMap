
# EventMap    
 A simple and easy implementation of scalable and draggable layout with smooth animation in Android. Basically, EventMap used to implement a simple map with custom markers, but other scenarios can be applied. Try the sample app to see `EventMap` in action. <p>    
<img src="screenshots/device_01.png" width="420" vspace="20" hspace="5"> <img src="screenshots/device_02.png" width="420" vspace="20" hspace="5"></p> <p>    
 <img src="screenshots/device_03.png" width="420" vspace="20" hspace="5"> <img src="screenshots/device_04.png" width="420" vspace="20" hspace="5"> </p>  
  ## How to    
    
Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
``` 
Add the dependency:  
```
dependencies {
	implementation 'com.github.shashavs:eventmap:1.0.1'
}
```
 
## DragScaleLayout    
    
A container for views, which can be dragged and zoomed. DragScaleLayout supports three types of children: map, marker, info. Map is ImageView, marker and info are default or custom views. DragScaleLayout handles the following events: move, fling, double tap, scale. To the maptype view, the scale and translation actions is applied. For types of marker and info - only translation.    

## EventMap usage example
  
  - Layout xml:
  ```
  <com.shashavs.eventmap.drag_scale.DragScaleLayout  
	  android:id="@+id/map_container"  
	  android:layout_width="match_parent"  
	  android:layout_height="match_parent"  
	  android:animateLayoutChanges="true"  
	  app:mapScr="@drawable/seating_plan1" 
	  app:maxZoom="1.5" />
  ```
  The map ImageView can be installed in xml - `mapScr` or programmatically via `getMapImageView()` method. 
  
 - Create Marker:
 ```
    val marker = Marker(id, x * metrics.density, y * metrics.density)
  ```
  The marker coordinates depends on the size of the source map image in px. 
  
  - If necessary, create a default or custom InfoLayout:
   ```
    val infoView = DefaultInfoLayout(context, marker, title, description)
  ```
  - Build MarkerView:
  ```
	val markerView = MarkerView.Builder(context, marker)
		.background(R.drawable.default_title_marker)
		.text(title)
		.textColor(R.color.light)
		.textSize(14f)
		.build()
  ``` 
  ```
    val markerView = MarkerView.Builder(context, marker)
		.background(R.drawable.default_marker)
		.info(infoView)
		.build()
  ``` 
  - Add markerView to DragScaleLayout:
  ```
    dragScaleLayout.addMarker(markerView)
  ```
  - If necessary, add a markerListener to get a click event on a marker:
  ```
    addMarkerListenet(markerListener: MarkerListener)
  ```
