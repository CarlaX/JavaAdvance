package com.fzw.threaddemo;

/**
 * @author fzw
 * @description
 * @date 2021-05-23
 **/
public class Demo01 {
    public static String result = null;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程开始:" + TimeUtil.currentDateTimeFormat());
        MyThread thread = new MyThread();
        thread.start();
        thread.join();
        System.out.println("子线程结果:" + result);
        System.out.println("主线程结束:" + TimeUtil.currentDateTimeFormat());
    }


    private static class MyThread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("子线程开始:" + TimeUtil.currentDateTimeFormat());
                Thread.sleep(3000);
                result = "success";
                System.out.println("子线程结束:" + TimeUtil.currentDateTimeFormat());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
