package com.hi.picturebackend.test;

import org.junit.jupiter.api.Test;

public class LockTest {
    @Test
    public void addLock() {
        String s1 = String.valueOf("a");
        String s2 = String.valueOf("a"); // 字符串常量，本来就是从常量池里拿的
        String s3 = String.valueOf(1).intern();
        String s4 = String.valueOf(1).intern();
        String s5 = String.valueOf(1);
        String s6 = String.valueOf(1);
        if (s1 == s2)   // false
         {
            System.out.println("s1 = s2");
        }
        if (s5==s3) {
            System.out.println("s5 = s3");
        }
        if (s3 == s4) {
            System.out.println("s3 = s4");
        }

    }
}
