# did-motif-android
A android library for generating DID identicons.

# Install

```groovy
implementation 'io.arcblock.did:did-motif:${version}'
```

# Usage

render DID Motif:

1. add view in xml
```xml
    <com.arcblock.wallet.appcommonres.view.didmotif.DIDMotif
        android:id="@+id/motif_circle"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_marginTop="10dp"
        />
```

2. set motif address
```kotlin
binding.motifCircle.setAddress(address, DIDMotif.Shape.Circle, true)
```
there are 4 shape of DIDMotif: Circle, Square, Hexagon and Rectangle. and the last parameter indicates whether to render the animation.
<img width="341" alt="image" src="https://user-images.githubusercontent.com/4629442/158416084-175610af-0fdc-4303-8e1a-e3dbb36330d2.png">


render Blockies IdentIcon

1. add view in xml
```xml
    <io.arcblock.did_motif.blockies.BlockiesIdenticon
        android:id="@+id/identicon"
        android:layout_width="100dp"
        android:layout_height="100dp"
        />
```
3. set data in kotlin
```kotlin
binding.identicon.setAddress(ethAddress,BlockiesIdenticon.SHAPE_SQUARE, BlockiesData.DEFAULT_SIZE, 20f)
```
the Blockies Identicon only has 2 shape: Circle and Square.

<img width="218" alt="image" src="https://user-images.githubusercontent.com/4629442/158416657-d25c8d4d-9f43-4369-ad49-8ff7e6d444e1.png">



# LICENSE
```
                Copyright [2022] [ArcBlock.io]
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```




