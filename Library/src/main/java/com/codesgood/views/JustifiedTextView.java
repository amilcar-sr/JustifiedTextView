package com.codesgood.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* ***********************************************************************

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

*********************************************************************** */

//Created by CodesGood on 7/12/14.
public class JustifiedTextView extends TextView {

    //Thin space (Hair Space actually) character that will fill the spaces
    private final static String THIN_SPACE = "\u200A";

    //String that will storage the text with the inserted spaces
    private String mJustifiedText = "";

    //Object that helps us to measure the words and characters like spaces.
    private Paint mPaint;

    //Default Constructors!
    public JustifiedTextView(Context context) {
        super(context);
    }

    public JustifiedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JustifiedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        ViewGroup.LayoutParams params = this.getLayoutParams();

        String[] words = this.getText().toString().split(" ");

        mPaint = this.getPaint();

        int viewWidth = this.getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());

        //This class won't justify the text if the TextView has wrap_content as width
        //And won't repeat the process of justify text if it's already done.
        //AND! won't justify the text if the view width is 0
        if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT && viewWidth > 0 && words.length > 0 && mJustifiedText.isEmpty()) {
            mJustifiedText = getJustifiedText(words, viewWidth);

            if (!mJustifiedText.isEmpty()) {
                this.setText(mJustifiedText);
            }
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * Retrieves a String with appropriate spaces to justify the text in the TextView.
     *
     * @param words Words in the TextView
     * @param width TextView's width
     * @return Justified Text
     */
    private String getJustifiedText(String[] words, int width) {
        List<String> sentences = new ArrayList<>();
        List<String> currentSentence = new ArrayList<>();

        for (String word : words) {
            boolean containsNewLine = (word.contains("\n") || word.contains("\r"));

            if (containsNewLine) {
                String[] splitted = word.split("(?<=\\n)");
                if (fitsInSentence(splitted[0], currentSentence, width, true)) {
                    currentSentence.add(word);
                } else {
                    sentences.add(fillSentenceWithSpaces(currentSentence, width));
                    currentSentence.clear();
                    currentSentence.add(word);
                }
            } else if (fitsInSentence(word, currentSentence, width, true)) {
                currentSentence.add(word);
            } else {
                sentences.add(fillSentenceWithSpaces(currentSentence, width));
                currentSentence.clear();
                currentSentence.add(word);
            }
        }

        if (currentSentence.size() > 0) {
            sentences.add(getSentenceFromList(currentSentence, true));
        }

        return getSentenceFromList(sentences, false);
    }

    /**
     * Creates a string using the words in the list and adds spaces between words if required.
     *
     * @param strings   Strings to be merged into one
     * @param addSpaces Specifies if the method should add spaces between words.
     * @return Returns a sentence using the words in the list.
     */
    private String getSentenceFromList(List<String> strings, boolean addSpaces) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String string : strings) {
            stringBuilder.append(string);

            if (addSpaces) {
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Fills sentence with appropriate amount of spaces.
     *
     * @param sentence Sentence we'll use to build the sentence with additional spaces
     * @param width    View's width.
     * @return String with spaces.
     */
    private String fillSentenceWithSpaces(List<String> sentence, int width) {
        List<String> sentenceWithSpaces = new ArrayList<>();

        for (String word : sentence) {
            sentenceWithSpaces.add(word);
            sentenceWithSpaces.add(" ");
        }

        while (fitsInSentence(THIN_SPACE, sentenceWithSpaces, width, false)) {
            sentenceWithSpaces.add(1 + getRandomEvenNumber(sentenceWithSpaces.size() - 2), THIN_SPACE);
        }

        return getSentenceFromList(sentenceWithSpaces, false);
    }

    /**
     * Verifies if word to be added will fit into the sentence
     *
     * @param word      Word to be added
     * @param sentence  Sentence that will receive the new word
     * @param width     View's width
     * @param addSpaces Specifies weather we should add spaces to validation or not
     * @return True if word fits, false otherwise.
     */
    private boolean fitsInSentence(String word, List<String> sentence, int width, boolean addSpaces) {
        String stringSentence = getSentenceFromList(sentence, addSpaces);
        stringSentence += word;

        float sentenceWidth = mPaint.measureText(stringSentence);

        return sentenceWidth < width;
    }

    /**
     * Returns a random number, it's part of the algorithm... don't blame me.
     *
     * @param max Max number in range
     * @return Random number.
     */
    private int getRandomEvenNumber(int max) {
        Random rand = new Random();

        while (max < 1) {
            max++;
        }

        // nextInt is normally exclusive of the top value,
        return rand.nextInt((max)) & ~1;
    }
}