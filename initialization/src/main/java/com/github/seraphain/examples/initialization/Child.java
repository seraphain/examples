package com.github.seraphain.examples.initialization;

public class Child extends Parent {

    private static String childStaticAttribute = initChildStaticAttribute();

    private String childUnstaticAttribute = initChildUnstaticAttribute();

    static {
        System.out.println("Initializing child static block.");
    }

    {
        System.out.println("Initializing child unstatic block.");
    }

    public Child() {
        System.out.println("Initializing child constructor.");
    }

    private static String initChildStaticAttribute() {
        System.out.println("Initializing child static attribute.");
        return "childStaticAttribute";
    }

    private String initChildUnstaticAttribute() {
        System.out.println("Initializing child unstatic attribute.");
        return "childUnstaticAttribute";
    }

    public static String getChildStaticAttribute() {
        return childStaticAttribute;
    }

    public String getChildUnstaticAttribute() {
        return childUnstaticAttribute;
    }

}
