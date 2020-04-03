/*
 * Copyright (c) 2018-2019.
 * Beijing sky blue technology co., LTD.
 * All rights reserved
 *
 * author: sourcod
 * github: https://github.com/WilleamZhao
 * site：http://codframe.com
 */

import org.junit.Test;
//import org.junit.Test;

public class a {

  public static void set(String a) {
    System.out.println(a);
    System.out.println("asd");
    System.out.println(123);


  }

  public static void main(String[] arqgs) {
    a.set("asdasdasd");
    System.out.println("abcd");
    System.out.println("asd");
    // System.out.println("asasdasdasdsaqweqweqweqwewqd");
    if (true) {
      a.set("asd");
    }
  }

  @Test
  public void testAdd() {
    a.set("3");
    a.set("4");
  }
}
