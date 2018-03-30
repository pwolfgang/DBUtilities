/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.policydb.dbutilities;

import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test of DBUtilities:convertToLegalName
 * @author Paul
 */
@RunWith(Parameterized.class)
public class ConvertToLegalNameTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"File", "_File_"},
            {"END", "_END_"},
            {"Exec", "_Exec_"},
            {"Index", "_Index_"},
            {"Table", "_Table_"},
            {"Foreign", "_Foreign_"},
            {"134", "_134"},
            {"The Quick/Fast & Furious x-x #5 (:)", "The_Quick_Fast_and_Furious_xx_No5____"}        
        });
    }
    
    private final String input;
    private final String expected;
    
    public ConvertToLegalNameTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }
    
    @Test
    public void test() {
        assertEquals(expected, DBUtil.convertToLegalName(input).toString());
    }
    
}
