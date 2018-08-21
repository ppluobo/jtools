package com.ppluobo.jtools.id;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;


/**
 * 基于snowFlake，machineId自动生成
 */
public final class SequenceGenerator {

    /**
     * 起始的时间戳, 2018-01-01T00:00:00.000Z
     */
    private final static long TA_EPOCH = 1514764800000L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 10;   //机器标识占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    private final static long MACHINE_ID;     //机器标识
    private static long lastTimestamp = -1L;//上一次时间戳

    private static long sequence = 0L;


    public static synchronized long nextId() {

        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (timestamp == lastTimestamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                timestamp = tilNextMillis();
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        long id = (timestamp - TA_EPOCH) << TIMESTAMP_LEFT;
        id |= (MACHINE_ID << MACHINE_LEFT);
        id |= sequence;
        return id;
    }

    static {
        MACHINE_ID = createMachineId();
    }

    private static long tilNextMillis() {
        long timeStamp = timeGen();
        while (timeStamp <= lastTimestamp) {
            timeStamp = timeGen();
        }
        return timeStamp;
    }

    private static long timeGen() {
        return Instant.now().toEpochMilli();
    }

    private static synchronized long createMachineId() {
        long machineId;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X", mac[i]));
                    }
                }
            }
            machineId = sb.toString().hashCode();
        } catch (Exception ex) {
            machineId = (new SecureRandom().nextInt());
        }
        machineId = machineId & MAX_MACHINE_NUM;
        return machineId;
    }

}

