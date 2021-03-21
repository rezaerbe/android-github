## [Material Components Android Example](https://github.com/material-components/material-components-android-examples)

The apps in this repository are Android implementations of fictional [Material Studies](https://material.io/design/material-studies/). Each one is built using the [MDC-Android library](http://github.com/material-components/material-components-android/) and showcases customizations of color, typography, and shape made with [Material Theming](https://material.io/design/material-theming/).

### [Build a Material Theme](https://github.com/material-components/material-components-android-examples/tree/develop/MaterialThemeBuilder)

Build a Material Theme lets you create your own Material theme by customizing values for color, typography, and shape. See how these values appear when applied to Material Components and discover how to implement your custom theme in your own projects. Build a Material Theme is also available for the web as a remixable project on [Glitch](https://glitch.com/~material-theme-builder).

### Overview

Material Components for Android supports Material Theming by exposing top level theme attributes for color, typography and shape. Customizing these attributes will apply your custom theme throughout your entire app.

This project shows how you can organize and use your theme and style resources to take advantage of the robust support for theming in Material Components for Android.

### Change values for typography, shape, and color

By default, apps built with Material Components inherit our baseline theme values. To begin customizing, override properties in `color.xml`, `type.xml` and `shape.xml`. Each file includes detailed comments that illustrate how each subsystem can be customized.

#### type.xml

To change your theme’s typography, we recommend using [Google Fonts](https://fonts.google.com/) and choosing a font family that best reflects your style. Set TextApperances to use your custom font and additional type properties to apply a custom type scale globally. [Learn how to add fonts in Android Studio](https://developer.android.com/guide/topics/ui/look-and-feel/downloadable-fonts)

#### shape.xml

To systematically apply shape throughout your app, it helps to understand that components are grouped by size into categories of small, medium and large. The shape of each component size group can be themed by customizing its ShapeApperance style. We recommend using our [shape customization tool](https://material.io/design/shape/about-shape.html#shape-customization-tool) to help you pick your corner family and size values.

#### color.xml

To change your theme's color scheme, replace the existing HEX color values with your custom HEX values. This project has both light and dark themes, toggle between them within the app to see your changes. Use our [color palette generator](https://material.io/design/color/the-color-system.html#tools-for-picking-colors) to help come up with pairings and check your color contrast.