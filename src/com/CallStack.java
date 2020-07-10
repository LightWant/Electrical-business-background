package com;


public class CallStack {
    public static void printCallStatck() {
//        Throwable ex = new Throwable();
//        StackTraceElement[] stackElements = ex.getStackTrace();
//        if (stackElements != null) {
//            for (int i = 0; i < stackElements.length; i++) {
//                System.out.print(stackElements[i].getClassName()+"/t");
//                System.out.print(stackElements[i].getFileName()+"/t");
//                System.out.print(stackElements[i].getLineNumber()+"/t");
//                System.out.println(stackElements[i].getMethodName());
//            }
//        }
    	try {
			Float.parseFloat("sadf");
		}catch(Exception e) {
			e.printStackTrace();
		}
    }
    
}