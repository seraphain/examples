/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2014 by Ericsson, all rights reserved.
 */

package com.github.seraphain.examples.powermock;

/**
 * 
 * @author
 */
public class TestedClass {

    private DependedClass dependedClass;

    public void call_static_void() {
        DependedClass.static_void();
    }

    public void call_static_void_object(DataObject data) {
        DependedClass.static_void_object(data);
    }

    public Object call_static_object() {
        return DependedClass.static_object();
    }

    public Object call_static_object_object(DataObject data) {
        return DependedClass.static_object_object(data);
    }

    public Object call_private_static_object_object(DataObject data) {
        return private_static_object_object(data);
    }

    private static Object private_static_object_object(DataObject data) {
        return new Object();
    }

    public void call_nonstatic_void() {
        dependedClass.nonstatic_void();
    }

    public void call_nonstatic_void_object(DataObject data) {
        dependedClass.nonstatic_void_object(data);
    }

    public Object call_nonstatic_object() {
        return dependedClass.nonstatic_object();
    }

    public Object call_nonstatic_object_object(DataObject data) {
        return dependedClass.nonstatic_object_object(data);
    }

    public void setDependedClass(DependedClass dependedClass) {
        this.dependedClass = dependedClass;
    }

    public Object call_private_nonstatic_object_object(DataObject data) {
        return private_nonstatic_object_object(data);
    }

    private Object private_nonstatic_object_object(DataObject data) {
        return new Object();
    }

    public Object call_constructor_and_nonstatic_object_object(DataObject data) {
        return new DependedClass().nonstatic_object_object(data);
    }

}
