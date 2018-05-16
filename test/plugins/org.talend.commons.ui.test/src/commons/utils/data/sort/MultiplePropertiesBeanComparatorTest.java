// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package commons.utils.data.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.collections.CollectionUtils;
import org.talend.commons.utils.data.sort.MultiplePropertiesBeanComparator;

import commons.utils.Bean;

/**
 * 
 * @author amaumont <br/>
 * $Id: MultiplePropertiesBeanComparatorTest.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 */
public class MultiplePropertiesBeanComparatorTest extends TestCase {

    private final int fiveHundred = 500;

    private final int ten = 10;

    /*
     */
    @SuppressWarnings("unchecked")
    public void test() {

        List<Bean> list = new ArrayList<Bean>();
        List<String> listValidTest = new ArrayList<String>();

        for (int i = 0; i < fiveHundred; i++) {
            for (int j = 0; j < ten; j++) {
                for (int k = 0; k < ten; k++) {
                    Bean bean = new Bean();
                    bean.setString1(String.valueOf(i));
                    bean.setString2(String.valueOf(j));
                    bean.setString3(String.valueOf(k));
                    list.add(bean);
                    listValidTest.add("" + i + j + k); //$NON-NLS-1$

                    bean = new Bean();
                    bean.setString1(String.valueOf(i));
                    bean.setString2(String.valueOf(k));
                    bean.setString3(String.valueOf(j));
                    list.add(bean);
                    listValidTest.add("" + i + k + j); //$NON-NLS-1$

                    bean = new Bean();
                    bean.setString1(String.valueOf(k));
                    bean.setString2(String.valueOf(i));
                    bean.setString3(String.valueOf(j));
                    list.add(bean);
                    listValidTest.add("" + k + i + j); //$NON-NLS-1$

                }
            }
        }
        MultiplePropertiesBeanComparator comparator = new MultiplePropertiesBeanComparator(true, new String[] {
                "string1", "string2", "string3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        long time1 = System.currentTimeMillis();
        Collections.sort(list, comparator);
        long time2 = System.currentTimeMillis();
        System.out.println("Time of execution:" + (time2 - time1)); //$NON-NLS-1$

        Collections.sort(listValidTest);

        List<String> listValidTest2 = new ArrayList<String>();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Bean bean = (Bean) iter.next();
            listValidTest2.add(bean.getString1() + bean.getString2() + bean.getString3());
        }

        // for( Iterator iter = list.iterator(); iter.hasNext(); ) {
        // Bean bean = (Bean)iter.next();
        //      
        // System.out.println(bean.getString1()+ " " + bean.getString2() + " " + bean.getString3());
        //      
        // }
        //    
        // int lstSize = listValidTest.size();
        // for( int i = 0; i < lstSize; i++ ) {
        // String string = (String)listValidTest.get( i );
        // String string2 = (String)listValidTest2.get( i );
        //      
        // System.out.println(string +"|||||" + string2);
        // }
        System.out.println(listValidTest.size() + " objects sorted by three properties"); //$NON-NLS-1$
        assertTrue(CollectionUtils.isEqualCollection(listValidTest, listValidTest2));

    }

    @SuppressWarnings("unchecked")
    public void test2() {
        List<Bean> list = new ArrayList<Bean>();
        List<String> listValidTest = new ArrayList<String>();

        for (int i = 0; i < fiveHundred; i++) {
            for (int j = 0; j < ten; j++) {
                for (int k = 0; k < ten; k++) {
                    Bean bean = new Bean();
                    bean.setString1(String.valueOf(i));
                    bean.setString2(String.valueOf(j));
                    bean.setString3(String.valueOf(k));
                    list.add(bean);
                    listValidTest.add("" + i + j + k); //$NON-NLS-1$

                    bean = new Bean();
                    bean.setString1(String.valueOf(i));
                    bean.setString2(String.valueOf(k));
                    bean.setString3(String.valueOf(j));
                    list.add(bean);
                    listValidTest.add("" + i + k + j); //$NON-NLS-1$

                    bean = new Bean();
                    bean.setString1(String.valueOf(k));
                    bean.setString2(String.valueOf(i));
                    bean.setString3(String.valueOf(j));
                    list.add(bean);
                    listValidTest.add("" + k + i + j); //$NON-NLS-1$

                }
            }
        }
        MultiplePropertiesBeanComparator comparator = new MultiplePropertiesBeanComparator(true, new String[] { "string1" }); //$NON-NLS-1$

        long time1 = System.currentTimeMillis();
        Collections.sort(list, comparator);
        long time2 = System.currentTimeMillis();
        System.out.println("Time of execution:" + (time2 - time1)); //$NON-NLS-1$

        Collections.sort(listValidTest);

        List<String> listValidTest2 = new ArrayList<String>();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Bean bean = (Bean) iter.next();
            listValidTest2.add(bean.getString1() + bean.getString2() + bean.getString3());
        }

        // for( Iterator iter = list.iterator(); iter.hasNext(); ) {
        // Bean bean = (Bean)iter.next();
        //      
        // System.out.println(bean.getString1()+ " " + bean.getString2() + " " + bean.getString3());
        //      
        // }
        //    
        // int lstSize = listValidTest.size();
        // for( int i = 0; i < lstSize; i++ ) {
        // String string = (String)listValidTest.get( i );
        // String string2 = (String)listValidTest2.get( i );
        //      
        // System.out.println(string +"|||||" + string2);
        // }
        System.out.println(listValidTest.size() + " objects sorted with by one property"); //$NON-NLS-1$
        assertTrue(CollectionUtils.isEqualCollection(listValidTest, listValidTest2));

    }

}
