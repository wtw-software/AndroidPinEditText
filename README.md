AndroidPinEditView
==================

```
<dependency>
  <groupId>no.wtw.android</groupId>
  <artifactId>android-pin-edit-text</artifactId>
  <type>aar</type>
  <version>3</version>
</dependency>
```

## Usage

```
<no.wtw.android.androidpinedittext.view.PinEditText
        android:id="@+id/pin_code"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">
    <requestFocus/>
</no.wtw.android.androidpinedittext.view.PinEditText>
```

```
compile 'no.wtw.android:android-pin-edit-text:3'
```

## Styling
Add new namespace to root element of layout XML file: 

```
xmlns:app="http://schemas.android.com/apk/res-auto
```


Then adjust attributes:

```
<no.wtw.android.androidpinedittext.view.PinEditText
        ...
        android:textColor="#333"
        app:pinLength="4"
        app:digitSize="56dp"
        app:digitMargin="8dp"
        app:digitDotRadius="6dp"
        app:digitBackgroundDefault="@drawable/background_default"
        app:digitBackgroundFocused="@drawable/background_focused"
        />
```
