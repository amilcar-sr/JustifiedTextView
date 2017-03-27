package com.codesgood.views;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.CharacterStyle;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codesgood.views.model.SpanHolder;
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

  //Object that helps us to measure the words and characters like spaces.
  private Paint mPaint;

  //Thin space (Hair Space actually) character that will fill the spaces
  private String mThinSpace = "\u200A";

  //String that will storage the text with the inserted spaces
  private String mJustifiedText = "";

  //Float that represents the actual width of a sentence
  private float mSentenceWidth = 0;

  //Integer that counts the spaces needed to fill the line being processed
  private int mWhiteSpacesNeeded = 0;

  //Integer that counts the actual amount of words in the sentence
  private int mWordsInThisSentence = 0;

  //ArrayList of Strings that will contain the words of the sentence being processed
  private ArrayList<String> mTemporalLine = new ArrayList<String>();

  //StringBuilder that will hold the temporal chunk of the string to calculate word index.
  private StringBuilder mStringBuilderCSequence = new StringBuilder();

  //List of SpanHolder class that will hold the spans within the giving string.
  private List<SpanHolder> mSpanHolderList = new ArrayList<>();

  //StringBuilder that will store temp data for joining sentence.
  private StringBuilder sentence = new StringBuilder();

  private int mViewWidth;

  private float mThinSpaceWidth;

  private float mWhiteSpaceWidth;

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

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    if (mJustifiedText.replace(" ", "")
        .replace("", mThinSpace)
        .equals(this.getText().toString().replace(" ", "").replace("", mThinSpace))) {
      return;
    }

    ViewGroup.LayoutParams params = this.getLayoutParams();

    CharSequence charSequence = this.getText();

    mSpanHolderList.clear();

    String[] words = this.getText().toString().split(" ");

    //Get spans within the string and adds the instance references into the
    //SpanHolderList to be applied once the justify process has been performed.

    SpannableString s = SpannableString.valueOf(charSequence);
    if ((charSequence instanceof SpannedString)) {
      for (int i = 0; i < this.getText().length() - 1; i++) {
        CharacterStyle[] spans =
            ((SpannedString) charSequence).getSpans(i, i + 1, CharacterStyle.class);
        if (spans != null && spans.length > 0) {
          for (CharacterStyle span : spans) {
            int spaces =
                charSequence.toString().substring(0, i).split(" ").length + charSequence.toString()
                    .substring(0, i)
                    .split(mThinSpace).length;

            SpanHolder spanHolder =
                SpanHolder.getNewInstance(spans, s.getSpanStart(span), s.getSpanEnd(span), spaces);
            mStringBuilderCSequence.setLength(0);
            for (int j = 0; j <= words.length - 1; j++) {
              mStringBuilderCSequence.append(words[j]);
              mStringBuilderCSequence.append(" ");
              if (mStringBuilderCSequence.length() > i) {
                if (words[j].trim().replace(mThinSpace, "").length() == 1) {
                  spanHolder.setWordHolderIndex(j);
                } else {
                  spanHolder.setWordHolderIndex(j);
                  spanHolder.setTextChunkPadded(true);
                }
                break;
              }
            }
            mSpanHolderList.add(spanHolder);
          }
        }
      }
    }
    mPaint = this.getPaint();
    mViewWidth = this.getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());

    //This class won't justify the text if the TextView has wrap_content as width
    //And won't repeat the process of justify text if it's already done.
    //AND! won't justify the text if the view width is 0
    if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT
        && mViewWidth > 0
        && words.length > 0
        && mJustifiedText.isEmpty()) {
      mThinSpaceWidth = mPaint.measureText(mThinSpace);
      mWhiteSpaceWidth = mPaint.measureText(" ");
      for (int i = 0; i <= words.length - 1; i++) {
        boolean containsNewLine = (words[i].contains("\n") || words[i].contains("\r"));
        if (containsNewLine) {
          String[] splitted = words[i].split("(?<=\\n)");
          for (String splitWord : splitted) {
            processWord(splitWord, splitWord.contains("\n"));
          }
        } else {
          processWord(words[i], false);
        }
      }
      mJustifiedText += joinWords(mTemporalLine);
    }

    //Apply the extra spaces to the items of the SpanList that were added due
    //the justifying process.
    SpannableString spannableString = SpannableString.valueOf(mJustifiedText);

    for (SpanHolder sH : mSpanHolderList) {
      int spaceCount = 0, wordCount = 0;
      boolean isCountingWord = false;
      int j = 0;
      while (wordCount < (sH.getWordHolderIndex() + 1)) {
        if (mJustifiedText.charAt(j) == ' ' || mJustifiedText.charAt(j) == ' ') {
          spaceCount++;
          if (isCountingWord) {
            wordCount++;
          }
          isCountingWord = false;
        } else {
          isCountingWord = true;
        }
        j++;
      }
      sH.setStart(
          sH.getStart() + spaceCount - sH.getCurrentSpaces() + (sH.isTextChunkPadded() ? 1 : 0));
      sH.setEnd(
          sH.getEnd() + spaceCount - sH.getCurrentSpaces() + (sH.isTextChunkPadded() ? 1 : 0));
    }
    //Applies spans on Justified String.
    for (SpanHolder sH : mSpanHolderList) {
      for (CharacterStyle cS : sH.getSpans())
        spannableString.setSpan(cS, sH.getStart(), sH.getEnd(), 0);
    }

    if (!mJustifiedText.isEmpty()) this.setText(spannableString);
  }

  private void processWord(String word, boolean containsNewLine) {
    if ((mSentenceWidth + mPaint.measureText(word)) < mViewWidth) {
      mTemporalLine.add(word);
      mWordsInThisSentence++;
      mTemporalLine.add(containsNewLine ? "" : " ");
      mSentenceWidth += mPaint.measureText(word) + mWhiteSpaceWidth;
      if (containsNewLine) {
        mJustifiedText += joinWords(mTemporalLine);
        resetLineValues();
      }
    } else {
      while (mSentenceWidth < mViewWidth) {
        mSentenceWidth += mThinSpaceWidth;
        if (mSentenceWidth < mViewWidth) mWhiteSpacesNeeded++;
      }

      if (mWordsInThisSentence > 1) {
        insertWhiteSpaces(mWhiteSpacesNeeded, mWordsInThisSentence, mTemporalLine);
      }
      mJustifiedText += joinWords(mTemporalLine);
      resetLineValues();

      if (containsNewLine) {
        mJustifiedText += word;
        mWordsInThisSentence = 0;
        return;
      }
      mTemporalLine.add(word);
      mWordsInThisSentence = 1;
      mTemporalLine.add(" ");
      mSentenceWidth += mPaint.measureText(word) + mWhiteSpaceWidth;
    }
  }

  //Method that resets the values of the actual line being processed
  private void resetLineValues() {
    mTemporalLine.clear();
    mSentenceWidth = 0;
    mWhiteSpacesNeeded = 0;
    mWordsInThisSentence = 0;
  }

  //Function that joins the words of the ArrayList
  private String joinWords(ArrayList<String> words) {
    sentence.setLength(0);
    for (String word : words) {
      sentence.append(word);
    }
    return sentence.toString();
  }

  //Method that inserts spaces into the words to make them fix perfectly in the width of the view. I know I'm a genius naming stuff :)
  private void insertWhiteSpaces(int whiteSpacesNeeded, int wordsInThisSentence,
      ArrayList<String> sentence) {

    if (whiteSpacesNeeded == 0) return;

    if (whiteSpacesNeeded == wordsInThisSentence) {
      for (int i = 1; i < sentence.size(); i += 2) {
        sentence.set(i, sentence.get(i) + mThinSpace);
      }
    } else if (whiteSpacesNeeded < wordsInThisSentence) {
      for (int i = 0; i < whiteSpacesNeeded; i++) {
        int randomPosition = getRandomEvenNumber(sentence.size() - 1);
        sentence.set(randomPosition, sentence.get(randomPosition) + mThinSpace);
      }
    } else if (whiteSpacesNeeded > wordsInThisSentence) {
      //I was using recursion to achieve this... but when you tried to watch the preview,
      //Android Studio couldn't show any preview because a StackOverflow happened.
      //So... it ended like this, with a wild while xD.
      while (whiteSpacesNeeded > wordsInThisSentence) {
        for (int i = 1; i < sentence.size() - 1; i += 2) {
          sentence.set(i, sentence.get(i) + mThinSpace);
        }
        whiteSpacesNeeded -= (wordsInThisSentence - 1);
      }
      if (whiteSpacesNeeded == 0) return;

      if (whiteSpacesNeeded == wordsInThisSentence) {
        for (int i = 1; i < sentence.size(); i += 2) {
          sentence.set(i, sentence.get(i) + mThinSpace);
        }
      } else if (whiteSpacesNeeded < wordsInThisSentence) {
        for (int i = 0; i < whiteSpacesNeeded; i++) {
          int randomPosition = getRandomEvenNumber(sentence.size() - 1);
          sentence.set(randomPosition, sentence.get(randomPosition) + mThinSpace);
        }
      }
    }
  }

  //Gets a random number, it's part of the algorithm... don't blame me.
  private int getRandomEvenNumber(int max) {
    Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    return rand.nextInt((max)) & ~1;
  }
}