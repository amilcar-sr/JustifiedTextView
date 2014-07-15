package com.codesgood.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
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
public class JText extends TextView {

    //Object that helps us to measure the words and characters like spaces.
    Paint mPaint;

    //Thin space character that will fill the spaces
    String mThinSpace = "\u200A";

    //String that will storage the text with the inserted spaces
    String justifiedText = "";

    //Float that represents the actual width of a sentence
    float sentenceWidth = 0;

    //Integer that counts the spaces needed to fill the line being processed
    int whiteSpacesNeeded = 0;

    //Integer that counts the actual amount of words in the sentence
    int wordsInThisSentence = 0;

    //ArrayList of Strings that will contain the words of the sentence being processed
    ArrayList<String> temporalLine = new ArrayList<String>();

    //Default Constructors!
    public JText(Context context) {
        super(context);
    }

    public JText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams params = this.getLayoutParams();

        String[] words = this.getText().toString().split(" ");

        mPaint = this.getPaint();

        //This class won't justify the text if the TextView has wrap_content as width
        //And won't repeat the process of justify text if it's already done.
        //AND! won't justify the text if the view width is smaller than 150dp
        if(params.width != ViewGroup.LayoutParams.WRAP_CONTENT && getMeasuredWidth() >= 150 && words.length > 0 && justifiedText.isEmpty()){

            int viewWidth = this.getMeasuredWidth();

            float thinSpaceWidth = mPaint.measureText(mThinSpace);
            float whiteSpaceWidth = mPaint.measureText(" ");

            for(String word : words){

                boolean containsNewLine = (word.contains("\n") || word.contains("\r"));

                if((sentenceWidth + mPaint.measureText(word)) < viewWidth){
                    temporalLine.add(word);
                    wordsInThisSentence++;
                    temporalLine.add(" ");
                    sentenceWidth += mPaint.measureText(word) + whiteSpaceWidth;
                    if(containsNewLine){
                        justifiedText += joinWords(temporalLine);
                        resetLineValues();
                    }
                } else {
                    while(sentenceWidth < viewWidth){
                        sentenceWidth += thinSpaceWidth;
                        if(sentenceWidth < viewWidth)
                            whiteSpacesNeeded++;
                    }
                    insertWhiteSpaces(whiteSpacesNeeded, wordsInThisSentence, temporalLine);
                    justifiedText += joinWords(temporalLine);
                    resetLineValues();

                    if(containsNewLine){
                        justifiedText += word;
                        wordsInThisSentence = 0;
                        continue;
                    }
                    temporalLine.add(word);
                    wordsInThisSentence = 1;
                    temporalLine.add(" ");
                    sentenceWidth += mPaint.measureText(word) + whiteSpaceWidth;
                }
            }
            justifiedText += joinWords(temporalLine);
        }

        if(!justifiedText.isEmpty())
            this.setText(justifiedText);
    }

    //Method that resets the values of the actual line being processed
    private void resetLineValues(){
        temporalLine.clear();
        sentenceWidth = 0;
        whiteSpacesNeeded = 0;
        wordsInThisSentence = 0;
    }

    //Function that joins the words of the ArrayList
    private String joinWords(ArrayList<String> words) {
        String sentence = "";
        for(String word : words){
            sentence += word;
        }
        return sentence;
    }

    //Method that inserts spaces into the words to make them fix perfectly in the width of the view. I know I'm a genius naming stuff :)
    private void insertWhiteSpaces(int whiteSpacesNeeded, int wordsInThisSentence, ArrayList<String> sentence){

        if(whiteSpacesNeeded == 0)
            return;

        if(whiteSpacesNeeded == wordsInThisSentence){
            for(int i = 1; i < sentence.size(); i += 2){
                sentence.set(i, sentence.get(i) + mThinSpace);
            }
        } else if(whiteSpacesNeeded < wordsInThisSentence){
            for(int i = 0; i < whiteSpacesNeeded; i++){
                int randomPosition = getRandomEvenNumber(sentence.size() - 1);
                sentence.set(randomPosition, sentence.get(randomPosition) + mThinSpace);
            }
        } else if(whiteSpacesNeeded > wordsInThisSentence){
            //I was using recursion to achieve this... but when you tried to watch the preview,
            //Android Studio couldn't show any preview because a StackOverflow happened.
            //So... it ended like this, with a wild while xD.
            while(whiteSpacesNeeded > wordsInThisSentence){
                for(int i = 1; i < sentence.size() - 1; i += 2){
                    sentence.set(i, sentence.get(i) + mThinSpace);
                }
                whiteSpacesNeeded -= (wordsInThisSentence - 1);
            }
            if(whiteSpacesNeeded == 0)
                return;

            if(whiteSpacesNeeded == wordsInThisSentence){
                for(int i = 1; i < sentence.size(); i += 2){
                    sentence.set(i, sentence.get(i) + mThinSpace);
                }
            } else if(whiteSpacesNeeded < wordsInThisSentence){
                for(int i = 0; i < whiteSpacesNeeded; i++){
                    int randomPosition = getRandomEvenNumber(sentence.size() - 1);
                    sentence.set(randomPosition, sentence.get(randomPosition) + mThinSpace);
                }
            }
        }
    }

    //Gets a random number, it's part of the algorithm... don't blame me.
    private int getRandomEvenNumber(int max){
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        return rand.nextInt((max)) & ~1;
    }
}