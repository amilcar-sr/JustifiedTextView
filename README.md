JText
=========

JText is an Android View that justifies the Text!

![alt landscape](https://raw.githubusercontent.com/amilcar-sr/amilcar-sr.github.io/master/jtext/jtext_land.png)

![alt portrait](https://raw.githubusercontent.com/amilcar-sr/amilcar-sr.github.io/master/jtext/jtext_port.png)


##Usage

You just have to add the view in your layout:

```xml
<com.codesgood.views.JText
        android:text="@string/lorem_ipsum"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="15sp"/>
```

And that's it! the text contained in the JText view will be justified. (In this example I wrote 15sp as textSize, but you can use a different textSize and it won't be a problem).


##Add JText to your project

JText is available on Maven Central. Please ensure that you are using the latest version by [checking here](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.codesgood%22%20AND%20a%3A%22jtextlib%22)

Gradle:
```
compile 'compile 'com.codesgood:jtextlib:1.0.0''
```

Maven:
```
<dependency>
    <groupId>com.codesgood</groupId>
    <artifactId>jtextlib</artifactId>
    <version>1.0.0</version>
</dependency>
```

Or download the jar [here](https://github.com/amilcar-sr/JText/blob/master/jtext.jar)


##Thanks

- [Rose Hulman Institute of Technology (Algorithm source)](http://www.rose-hulman.edu/Users/faculty/young/CS-Classes/csse220/200820/web/Programs/Markov/justification.html)


##License

    Copyright 2014 CodesGood

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
