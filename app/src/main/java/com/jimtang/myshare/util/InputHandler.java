package com.jimtang.myshare.util;

/**
 * Created by tangz on 9/12/2015.
 */
public interface InputHandler<T> {

    T handleInput(CharSequence input);

    T handleNull(CharSequence input);

    T handleInvalidFormat(CharSequence input);
}
