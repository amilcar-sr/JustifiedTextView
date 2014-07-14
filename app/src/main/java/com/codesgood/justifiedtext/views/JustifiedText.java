package com.codesgood.justifiedtext.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by CodesGood on 7/12/14.
 */
public class JustifiedText extends TextView {

    Paint mPaint;

    String mThinSpace = "\u200A";

    public JustifiedText(Context context) {
        super(context);
    }

    public JustifiedText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JustifiedText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams params = this.getLayoutParams();

        String[] words = this.getText().toString().split("\\s+");

        String justifiedText = "";

        ArrayList<String> temporalLine = new ArrayList<String>();

        mPaint = this.getPaint();

        //This class won't justify the text if the TextView has wrap_content as width
        if(params.width != ViewGroup.LayoutParams.WRAP_CONTENT && words.length > 0){

            int viewWidth = this.getMeasuredWidth();
            float sentenceWidth = 0;

            float whiteSpaceWidth = mPaint.measureText(mThinSpace);
            int whiteSpacesNeeded = 0;
            int wordsInThisSentence = 0;

            for(String word : words){

                if((sentenceWidth + mPaint.measureText(word)) < viewWidth){
                    temporalLine.add(word);
                    wordsInThisSentence++;
                    temporalLine.add(mThinSpace);
                    sentenceWidth += mPaint.measureText(word) + whiteSpaceWidth;
                } else {
                    if(temporalLine.size() > 0)
                        temporalLine.remove(temporalLine.size() - 1);
                    sentenceWidth -= whiteSpaceWidth;
                    while(sentenceWidth < viewWidth){
                        sentenceWidth += whiteSpaceWidth;
                        if(sentenceWidth < viewWidth)
                            whiteSpacesNeeded++;
                    }
                    insertWhiteSpaces(whiteSpacesNeeded, wordsInThisSentence, temporalLine);
                    justifiedText += joinWords(temporalLine);
                    temporalLine.clear();
                    sentenceWidth = 0;
                    whiteSpacesNeeded = 0;
                    temporalLine.add(mThinSpace + word);
                    wordsInThisSentence = 1;
                    temporalLine.add(mThinSpace);
                    sentenceWidth += mPaint.measureText(word) + whiteSpaceWidth;
                }
            }
            justifiedText += joinWords(temporalLine);

            this.setText(justifiedText);
        }
    }

    private String joinWords(ArrayList<String> words) {
         String sentence = "";
         for(String word : words){
             sentence += word;
         }
         return sentence;
    }

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
            while(whiteSpacesNeeded > wordsInThisSentence){
                for(int i = 1; i < sentence.size(); i += 2){
                    sentence.set(i, sentence.get(i) + mThinSpace);
                }
                whiteSpacesNeeded -= wordsInThisSentence;
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

    private int getRandomEvenNumber(int max){
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        int randomNum = rand.nextInt((max)) & ~1;

        return randomNum;
    }
}
