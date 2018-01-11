package com.wesoft.codetest;

import java.util.IdentityHashMap;

public class Map {

    public static void main(String[] args) {
        IdentityHashMap<String, Integer> ihm = new IdentityHashMap<String, Integer>();

        //下面两行代码向IdentityHashMap对象添加两个key-value对
        ihm.put(new String("语文"),89);
        ihm.put(new String("语文"),78);
        //下面两行代码只会向IdentityHashMap对象添加一个key-value对
        ihm.put("java",93);
        ihm.put("java",98);
        System.out.println(ihm);

    }

}

