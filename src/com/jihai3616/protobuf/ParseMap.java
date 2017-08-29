package com.jihai3616.protobuf;

import java.io.IOException;
import java.util.HashMap;

import com.google.protobuf.Message;

/**
 * Created by Qzy on 2016/1/29.
 */
public class ParseMap {

    @FunctionalInterface
    public interface Parsing{
        Message  process(byte[] bytes) throws IOException;
    }

    public static HashMap<Integer, ParseMap.Parsing> parseMap = new HashMap<>();
    public static HashMap<Class<?>, Integer> msg2ptoNum = new HashMap<>();

    public static void register(int ptoNum, ParseMap.Parsing parse, Class<?> cla) {
        if (parseMap.get(ptoNum) == null)
            parseMap.put(ptoNum, parse);
        else {
            System.err.printf("pto has been registered in parseMap, ptoNum: {}", ptoNum);
            return;
        }

        if(msg2ptoNum.get(cla) == null)
            msg2ptoNum.put(cla, ptoNum);
        else {
        	System.err.printf("pto has been registered in msg2ptoNum, ptoNum: {}", ptoNum);
            return;
        }
    }

    public static Message getMessage(int ptoNum, byte[] bytes) throws IOException {
        Parsing parser = parseMap.get(ptoNum);
        if(parser == null) {
        	System.err.printf("UnKnown Protocol Num: {}", ptoNum);
        }
        Message msg = parser.process(bytes);

        return msg;
    }

    public static Integer getPtoNum(Message msg) {
        return getPtoNum(msg.getClass());
    }

    public static Integer getPtoNum(Class<?> clz) {
        return msg2ptoNum.get(clz);
    }

}
