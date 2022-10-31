// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.lang.reflect.*;
import java.util.*;

public class ArrayUtil {

  public static <T> T[] append(T[] arr, T t, Class<T> c) {
      T[] ret = (T[])Array.newInstance(c, arr.length+1);
      System.arraycopy(arr, 0, ret, 0, arr.length);
      ret[ret.length-1] = t;
      return ret;
  }

    public static <T> T[] clone(T[] source, Class<T> c) {
        T[] dest = (T[])Array.newInstance(c, source==null ? 0 : source.length);
        if (source != null) System.arraycopy(source, 0, dest, 0, source.length);
        return dest;
    }

}
