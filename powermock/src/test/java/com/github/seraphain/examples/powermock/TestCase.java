package com.github.seraphain.examples.powermock;

import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * 
 * @author
 */
// Must have one of @RunWith(PowerMockRunner.class) or @Rule public
// PowerMockRule rule = new PowerMockRule();
// PowerMockRunner breaks test_mock_object_with_multiple_interfaces.
// @RunWith(PowerMockRunner.class)
// @PrepareForTest is needed for mocking static method or final class
@PrepareForTest({ TestedClass.class, DependedClass.class })
public class TestCase {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private DependedClass dependedClass;

    private TestedClass testedClass;

    private DataObject dataObject;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * It's better to create mock object in <code>{@link Before}</code> method
     * instead of <code>{@link BeforeClass}</code> method. Since the
     * <code>{@link Mockito#reset(Object...)}</code> method has some issue when
     * mocking static method.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(DependedClass.class);
        dependedClass = PowerMockito.mock(DependedClass.class);
        testedClass = new TestedClass();
        testedClass.setDependedClass(dependedClass);
        // Set dataObject to null to avoid test cases impact each other
        dataObject = null;
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Verify how many times static method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_void() throws Exception {
        PowerMockito.doNothing().when(DependedClass.class, "static_void");

        testedClass.call_static_void();

        // Verify method is called once
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_void();
    }

    /**
     * Mock exception and verify how many times static method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_void_exception() throws Exception {
        // Mock exception
        PowerMockito.doThrow(new NullPointerException()).when(DependedClass.class, "static_void");

        try {
            testedClass.call_static_void();
            Assert.fail("Exception expected.");
        } catch (NullPointerException e) {
        }

        // Verify method is called once
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_void();
    }

    /**
     * Verify how many times static method is called and argument value.<br>
     * This way can ONLY be used for the argument which rewrites
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_void_object_1() throws Exception {
        PowerMockito.doNothing().when(DependedClass.class, "static_void_object", Mockito.anyObject());

        testedClass.call_static_void_object(new DataObject("test", 5));

        // Verify method is called once and argument
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_void_object(new DataObject("test", 5));
    }

    /**
     * Verify how many times static method is called and argument value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_void_object_2() throws Exception {
        PowerMockito.doNothing().when(DependedClass.class, "static_void_object", Mockito.anyObject());

        testedClass.call_static_void_object(new DataObject("test", 5));

        // Verify method is called once and argument
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_void_object(Mockito.argThat(new ArgumentMatcher<DataObject>() {
            @Override
            public boolean matches(Object argument) {
                if (!(argument instanceof DataObject)) {
                    return false;
                }
                DataObject object = (DataObject) argument;
                if (!object.getName().equals("test")) {
                    return false;
                }
                if (object.getNumber() != 5) {
                    return false;
                }
                return true;
            }
        }));
    }

    /**
     * Another way to verify how many times static method is called and argument
     * value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_void_object_3() throws Exception {
        // Use Answer to set argument to dataObject field
        PowerMockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                dataObject = (DataObject) invocation.getArguments()[0];
                return null;
            }
        }).when(DependedClass.class, "static_void_object", Mockito.anyObject());

        testedClass.call_static_void_object(new DataObject("test", 5));

        // Verify method is called once.
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_void_object((DataObject) Mockito.anyObject());

        // Verify argument
        Assert.assertEquals("test", dataObject.getName());
        Assert.assertEquals(5, dataObject.getNumber());
    }

    /**
     * Mock returned value and verify how many times static method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_1() throws Exception {
        // Mock returned value
        PowerMockito.doReturn("test").when(DependedClass.class, "static_object");

        Assert.assertEquals("test", testedClass.call_static_object());

        // Verify method is called once
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object();
    }

    /**
     * Mock returned value and verify how many times static method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_2() throws Exception {
        // Another way to mock returned value
        PowerMockito.when(DependedClass.static_object()).thenReturn("test");

        Assert.assertEquals("test", testedClass.call_static_object());

        // Verify method is called once
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object();
    }

    /**
     * Mock exception and verify how many times static method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_exception_1() throws Exception {
        // Mock exception
        PowerMockito.doThrow(new NullPointerException()).when(DependedClass.class, "static_object");

        try {
            testedClass.call_static_object();
            Assert.fail("Exception expected.");
        } catch (NullPointerException e) {
        }

        // Verify method is called once
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object();
    }

    /**
     * Mock exception and verify how many times static method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_exception_2() throws Exception {
        // Another way to mock exception
        PowerMockito.when(DependedClass.static_object()).thenThrow(new NullPointerException());

        try {
            testedClass.call_static_object();
            Assert.fail("Exception expected.");
        } catch (NullPointerException e) {
        }

        // Verify method is called once
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object();
    }

    /**
     * Verify how many times static method is called and argument value.<br>
     * This way can ONLY be used for the argument which rewrites
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_object_1() throws Exception {
        // Mock returned value
        PowerMockito.when(DependedClass.static_object_object((DataObject) Mockito.anyObject())).thenReturn("test");

        Assert.assertEquals("test", testedClass.call_static_object_object(new DataObject("test", 5)));

        // Verify method is called once and argument
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object_object(new DataObject("test", 5));
    }

    /**
     * Another way to verify how many times static method is called and argument
     * value.<br>
     * This way can ONLY be used for the argument which rewrites
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_object_2() throws Exception {
        // Specify argument to mock returned value
        PowerMockito.when(DependedClass.static_object_object(new DataObject("test", 5))).thenReturn("test");

        Assert.assertEquals("test", testedClass.call_static_object_object(new DataObject("test", 5)));

        // Verify method is called once.
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object_object((DataObject) Mockito.anyObject());
    }

    /**
     * Verify how many times static method is called and argument value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_object_3() throws Exception {
        // Another way to mock returned value
        PowerMockito.doReturn("test").when(DependedClass.class, "static_object_object", Mockito.anyObject());

        Assert.assertEquals("test", testedClass.call_static_object_object(new DataObject("test", 5)));

        // Verify method is called once and argument
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object_object(Mockito.argThat(new ArgumentMatcher<DataObject>() {
            @Override
            public boolean matches(Object argument) {
                if (!(argument instanceof DataObject)) {
                    return false;
                }
                DataObject object = (DataObject) argument;
                if (!object.getName().equals("test")) {
                    return false;
                }
                if (object.getNumber() != 5) {
                    return false;
                }
                return true;
            }
        }));
    }

    /**
     * Another way to verify how many times static method is called and argument
     * value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_object_4() throws Exception {
        // Use Answer to set argument to dataObject field and mock returned
        // value
        PowerMockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                dataObject = (DataObject) invocation.getArguments()[0];
                return "test";
            }
        }).when(DependedClass.class, "static_object_object", Mockito.anyObject());

        Assert.assertEquals("test", testedClass.call_static_object_object(new DataObject("test", 5)));

        // Verify method is called once.
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object_object((DataObject) Mockito.anyObject());

        // Verify argument
        Assert.assertEquals("test", dataObject.getName());
        Assert.assertEquals(5, dataObject.getNumber());
    }

    /**
     * Another way to verify how many times static method is called and argument
     * value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_static_object_object_5() throws Exception {
        // Another way to use Answer to set argument to dataObject field and
        // mock returned value
        PowerMockito.when(DependedClass.static_object_object((DataObject) Mockito.anyObject())).thenAnswer(
                new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        dataObject = (DataObject) invocation.getArguments()[0];
                        return "test";
                    }
                });

        Assert.assertEquals("test", testedClass.call_static_object_object(new DataObject("test", 5)));

        // Verify method is called once.
        PowerMockito.verifyStatic(Mockito.times(1));
        DependedClass.static_object_object((DataObject) Mockito.anyObject());

        // Verify argument
        Assert.assertEquals("test", dataObject.getName());
        Assert.assertEquals(5, dataObject.getNumber());
    }

    /**
     * Mock private static method and verify how many times private static
     * method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_private_static_object_object() throws Exception {
        PowerMockito.spy(TestedClass.class);
        PowerMockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                dataObject = (DataObject) invocation.getArguments()[0];
                return "test";
            }
        }).when(TestedClass.class, "private_static_object_object", Mockito.anyObject());

        Assert.assertEquals("test", testedClass.call_private_static_object_object(new DataObject("test", 5)));

        PowerMockito.verifyPrivate(TestedClass.class, Mockito.times(1)).invoke("private_static_object_object",
                new DataObject("test", 5));

        // Verify argument
        Assert.assertEquals("test", dataObject.getName());
        Assert.assertEquals(5, dataObject.getNumber());
    }

    /**
     * Verify how many times nonstatic method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_void_1() throws Exception {
        Mockito.doNothing().when(dependedClass).nonstatic_void();

        testedClass.call_nonstatic_void();

        // Verify method is called once
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_void();
    }

    /**
     * Verify how many times nonstatic method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_void_2() throws Exception {
        // Another way to mock
        PowerMockito.doNothing().when(dependedClass, "nonstatic_void");

        testedClass.call_nonstatic_void();

        // Verify method is called once
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_void();
    }

    /**
     * Mock exception and verify how many times nonstatic method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_void_exception_1() throws Exception {
        // Mock exception
        Mockito.doThrow(new NullPointerException()).when(dependedClass).nonstatic_void();

        try {
            testedClass.call_nonstatic_void();
            Assert.fail("Exception expected.");
        } catch (NullPointerException e) {
        }

        // Verify method is called once
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_void();
    }

    /**
     * Mock exception and verify how many times nonstatic method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_void_exception_2() throws Exception {
        // Another way to mock exception
        PowerMockito.doThrow(new NullPointerException()).when(dependedClass, "nonstatic_void");

        try {
            testedClass.call_nonstatic_void();
            Assert.fail("Exception expected.");
        } catch (NullPointerException e) {
        }

        // Verify method is called once
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_void();
    }

    /**
     * Verify how many times nonstatic method is called and argument value.<br>
     * This way can ONLY be used for the argument which rewrites
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_void_object_1() throws Exception {
        PowerMockito.doNothing().when(dependedClass).nonstatic_void_object((DataObject) Mockito.anyObject());

        testedClass.call_nonstatic_void_object(new DataObject("test", 5));

        // Verify method is called once and argument
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_void_object(new DataObject("test", 5));
    }

    /**
     * Verify how many times nonstatic method is called and argument value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_void_object_2() throws Exception {
        PowerMockito.doNothing().when(dependedClass, "nonstatic_void_object", Mockito.anyObject());

        testedClass.call_nonstatic_void_object(new DataObject("test", 5));

        // Verify method is called once and argument
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_void_object(
                Mockito.argThat(new ArgumentMatcher<DataObject>() {
                    @Override
                    public boolean matches(Object argument) {
                        if (!(argument instanceof DataObject)) {
                            return false;
                        }
                        DataObject object = (DataObject) argument;
                        if (!object.getName().equals("test")) {
                            return false;
                        }
                        if (object.getNumber() != 5) {
                            return false;
                        }
                        return true;
                    }
                }));
    }

    /**
     * Another way to verify how many times nonstatic method is called and
     * argument value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_void_object_3() throws Exception {
        // Use Answer to set argument to dataObject field
        PowerMockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                dataObject = (DataObject) invocation.getArguments()[0];
                return null;
            }
        }).when(dependedClass, "nonstatic_void_object", Mockito.anyObject());

        testedClass.call_nonstatic_void_object(new DataObject("test", 5));

        // Verify method is called once.
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_void_object((DataObject) Mockito.anyObject());

        // Verify argument
        Assert.assertEquals("test", dataObject.getName());
        Assert.assertEquals(5, dataObject.getNumber());
    }

    /**
     * Mock returned value and verify how many times nonstatic method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_1() throws Exception {
        // Mock returned value
        PowerMockito.doReturn("test").when(dependedClass, "nonstatic_object");

        Assert.assertEquals("test", testedClass.call_nonstatic_object());

        // Verify method is called once
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object();
    }

    /**
     * Mock returned value and verify how many times nonstatic method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_2() throws Exception {
        // Another way to mock returned value
        PowerMockito.when(dependedClass.nonstatic_object()).thenReturn("test");

        Assert.assertEquals("test", testedClass.call_nonstatic_object());

        // Verify method is called once
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object();
    }

    /**
     * Mock exception and verify how many times nonstatic method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_exception_1() throws Exception {
        // Mock exception
        PowerMockito.doThrow(new NullPointerException()).when(dependedClass, "nonstatic_object");

        try {
            testedClass.call_nonstatic_object();
            Assert.fail("Exception expected.");
        } catch (NullPointerException e) {
        }

        // Verify method is called once
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object();
    }

    /**
     * Mock exception and verify how many times nonstatic method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_exception_2() throws Exception {
        // Another way to mock exception
        PowerMockito.when(dependedClass.nonstatic_object()).thenThrow(new NullPointerException());

        try {
            testedClass.call_nonstatic_object();
            Assert.fail("Exception expected.");
        } catch (NullPointerException e) {
        }

        // Verify method is called once
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object();
    }

    /**
     * Verify how many times nonstatic method is called and argument value.<br>
     * This way can ONLY be used for the argument which rewrites
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_object_1() throws Exception {
        // Mock returned value
        PowerMockito.when(dependedClass.nonstatic_object_object((DataObject) Mockito.anyObject())).thenReturn("test");

        Assert.assertEquals("test", testedClass.call_nonstatic_object_object(new DataObject("test", 5)));

        // Verify method is called once and argument
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object_object(new DataObject("test", 5));
    }

    /**
     * Another way to verify how many times nonstatic method is called and
     * argument value.<br>
     * This way can ONLY be used for the argument which rewrites
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_object_2() throws Exception {
        // Specify argument to mock returned value
        PowerMockito.when(dependedClass.nonstatic_object_object(new DataObject("test", 5))).thenReturn("test");

        Assert.assertEquals("test", testedClass.call_nonstatic_object_object(new DataObject("test", 5)));

        // Verify method is called once.
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object_object((DataObject) Mockito.anyObject());
    }

    /**
     * Verify how many times nonstatic method is called and argument value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_object_3() throws Exception {
        // Another way to mock returned value
        PowerMockito.doReturn("test").when(dependedClass, "nonstatic_object_object", Mockito.anyObject());

        Assert.assertEquals("test", testedClass.call_nonstatic_object_object(new DataObject("test", 5)));

        // Verify method is called once and argument
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object_object(
                Mockito.argThat(new ArgumentMatcher<DataObject>() {
                    @Override
                    public boolean matches(Object argument) {
                        if (!(argument instanceof DataObject)) {
                            return false;
                        }
                        DataObject object = (DataObject) argument;
                        if (!object.getName().equals("test")) {
                            return false;
                        }
                        if (object.getNumber() != 5) {
                            return false;
                        }
                        return true;
                    }
                }));
    }

    /**
     * Another way to verify how many times nonstatic method is called and
     * argument value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_object_4() throws Exception {
        // Use Answer to set argument to dataObject field and mock returned
        // value
        PowerMockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                dataObject = (DataObject) invocation.getArguments()[0];
                return "test";
            }
        }).when(dependedClass, "nonstatic_object_object", Mockito.anyObject());

        Assert.assertEquals("test", testedClass.call_nonstatic_object_object(new DataObject("test", 5)));

        // Verify method is called once.
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object_object((DataObject) Mockito.anyObject());

        // Verify argument
        Assert.assertEquals("test", dataObject.getName());
        Assert.assertEquals(5, dataObject.getNumber());
    }

    /**
     * Another way to verify how many times nonstatic method is called and
     * argument value.<br>
     * This way can be used for the argument which doesn't rewrite
     * <code>{@link Object#equals(Object)}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_nonstatic_object_object_5() throws Exception {
        // Another way to use Answer to set argument to dataObject field and
        // mock returned value
        PowerMockito.when(dependedClass.nonstatic_object_object((DataObject) Mockito.anyObject())).thenAnswer(
                new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        dataObject = (DataObject) invocation.getArguments()[0];
                        return "test";
                    }
                });

        Assert.assertEquals("test", testedClass.call_nonstatic_object_object(new DataObject("test", 5)));

        // Verify method is called once.
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object_object((DataObject) Mockito.anyObject());

        // Verify argument
        Assert.assertEquals("test", dataObject.getName());
        Assert.assertEquals(5, dataObject.getNumber());
    }

    /**
     * Mock private nonstatic method and verify how many times private nonstatic
     * method is called.
     * 
     * @throws Exception
     */
    @Test
    public void test_private_nonstatic_object_object() throws Exception {
        testedClass = PowerMockito.spy(testedClass);
        PowerMockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                dataObject = (DataObject) invocation.getArguments()[0];
                return "test";
            }
        }).when(testedClass, "private_nonstatic_object_object", Mockito.anyObject());

        Assert.assertEquals("test", testedClass.call_private_nonstatic_object_object(new DataObject("test", 5)));

        PowerMockito.verifyPrivate(testedClass, Mockito.times(1)).invoke("private_nonstatic_object_object",
                new DataObject("test", 5));

        // Verify argument
        Assert.assertEquals("test", dataObject.getName());
        Assert.assertEquals(5, dataObject.getNumber());
    }

    /**
     * Mock constructor and nonstatic method, verify how many times nonstatic
     * method is called. When mocking constructor, the class which uses the
     * constructor must be included in <code>{@link PrepareForTest}</code>.
     * 
     * @throws Exception
     */
    @Test
    public void test_constructor_and_nonstatic_object_object() throws Exception {
        PowerMockito.when(dependedClass.nonstatic_object_object((DataObject) Mockito.anyObject())).thenReturn("test");

        // Mock constructor
        PowerMockito.whenNew(DependedClass.class).withNoArguments().thenReturn(dependedClass);

        Assert.assertEquals("test", testedClass.call_constructor_and_nonstatic_object_object(new DataObject("test", 5)));

        // Verify method is called once and argument
        PowerMockito.verifyNew(DependedClass.class).withNoArguments();
        Mockito.verify(dependedClass, Mockito.times(1)).nonstatic_object_object(new DataObject("test", 5));
    }

    /**
     * Mock an object with multiple interfaces. DON'T use
     * <code>{@link PowerMockito}</code> here!
     * 
     * @throws Exception
     */
    @Test
    public void test_mock_object_with_multiple_interfaces() throws Exception {
        Runnable runnable = Mockito.mock(Runnable.class, Mockito.withSettings().extraInterfaces(Iterator.class));
        Mockito.doThrow(new IllegalStateException()).when(runnable).run();
        @SuppressWarnings("unchecked")
        Iterator<Object> iterator = (Iterator<Object>) runnable;
        Mockito.when(iterator.next()).thenReturn("test1", "test2");

        Assert.assertEquals("test1", iterator.next());
        Assert.assertEquals("test2", iterator.next());
        try {
            runnable.run();
            Assert.fail("Exception expected.");
        } catch (IllegalStateException e) {
        }
    }

}
