package com.codereligion.reflect.internal;

import static com.codereligion.matcher.IsNotInstantiatable.isNotInstantiatable;
import static org.junit.Assert.assertThat;

import org.junit.Test;



/**
 * Tests {@link Generics}
 * 
 * @author sgroebler
 * @since 27.07.2013
 */
public class GenericsTest {

    @Test
    public void mustNotBeInstantiatable() {
        assertThat(Generics.class, isNotInstantiatable());
    }
}
