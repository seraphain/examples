package com.github.seraphain.examples.initialization;

public class Parent {

    private static String parentStaticAttribute = initParentStaticAttribute();

    private String parentUnstaticAttribute = initParentUnstaticAttribute();

    static {
        System.out.println("Initializing parent static block.");
    }

    {
        System.out.println("Initializing parent unstatic block.");
    }

    public Parent() {
        System.out.println("Initializing parent constructor.");
    }

    private static String initParentStaticAttribute() {
        System.out.println("Initializing parent static attribute.");
        return "parentStaticAttribute";
    }

    private String initParentUnstaticAttribute() {
        System.out.println("Initializing parent unstatic attribute.");
        return "parentUnstaticAttribute";
    }

    public static String getParentStaticAttribute() {
        return parentStaticAttribute;
    }

    public String getParentUnstaticAttribute() {
        return parentUnstaticAttribute;
    }

}
