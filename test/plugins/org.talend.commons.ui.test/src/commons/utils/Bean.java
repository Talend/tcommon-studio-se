// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package commons.utils; /*
                        * 
                        * @author amaumont
                        */

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Bean useful for various uses. <br/>
 * 
 * $Id: Bean.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class Bean {

    int primitiveInt;

    Integer objInteger;

    String string;

    List list;

    Collection collection;

    Map map;

    Set set;

    String string1;

    String string2;

    String string3;

    String name;

    public String getName() {
        return name;
    }

    public Collection getCollection() {
        return collection;
    }

    public List getList() {
        return list;
    }

    public Map getMap() {
        return map;
    }

    public Integer getObjInteger() {
        return objInteger;
    }

    public int getPrimitiveInt() {
        return primitiveInt;
    }

    public String getString() {
        return string;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setObjInteger(Integer objInteger) {
        this.objInteger = objInteger;
    }

    public void setPrimitiveInt(int primitiveInt) {
        this.primitiveInt = primitiveInt;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }

}
