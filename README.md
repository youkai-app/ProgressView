# ProgressView

A simple android view for numeric progress selection

![](https://cloud.githubusercontent.com/assets/2550945/24289817/16a6fdd2-1083-11e7-9c5e-0912339b6808.png)

<p align="right">
<a href='https://github.com/youkai-app/ProgressView/latest'><img height="48" alt='Get apk' src='https://cloud.githubusercontent.com/assets/2550945/21590907/dd74e0f0-d0ff-11e6-971f-d429148fd03d.png'/></a>
</p>

## Download
```gradle
    compile 'app.youkai.progressview:library:1.0.0'
```
**Note:** You might have to add `jcenter()` to your repositories.

## Usage
Usage is extremely simple:
```xml
    <app.youkai.progressview.ProgressView
        android:id="@+id/progressView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:pv_showTotal="true" />
```
```java
    progressView.setTotal(24);
    progressView.setListener(new ProgressView.OnProgressChangedListener() {
        @Override
        public void onProgressChanged(int progress) {
            Toast.makeText(MainActivity.this, "Progress: " + progress, Toast.LENGTH_SHORT).show();
        }
    });

    .
    .
    .

    progressView.getProgress();
    progressView.getTotal();
    progressView.showTotal(false);
```

## Theming
By default, ProgressView uses your theme's `colorAccent` value as tint. While you can't directly set the a custom color, you can simply define a new theme with a different accent color and apply that on your ProgressView with `android:theme="@style/MySpecialProgressViewTheme`.

## License
```
Copyright (C) 2017  Abdullah Khwaja, İhsan Işık, Matthew Dias

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
Apache License Version 2.0 ([LICENSE](/LICENSE))