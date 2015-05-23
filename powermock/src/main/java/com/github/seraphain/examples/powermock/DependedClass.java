package com.github.seraphain.examples.powermock;

/**
 * 
 * @author
 */
public final class DependedClass {

    public static DependedClass getInstance() {
        return new DependedClass();
    }

    public static void static_void() {
    }

    public static void static_void_object(DataObject data) {
    }

    public static Object static_object() {
        return null;
    }

    public static Object static_object_object(DataObject data) {
        return null;
    }

    public void nonstatic_void() {
    }

    public void nonstatic_void_object(DataObject data) {
    }

    public Object nonstatic_object() {
        return null;
    }

    public Object nonstatic_object_object(DataObject data) {
        return null;
    }

}
