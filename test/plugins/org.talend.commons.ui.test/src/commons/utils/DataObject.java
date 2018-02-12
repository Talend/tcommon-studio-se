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
package commons.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * Bean useful for various uses. <br/>
 * 
 * $Id: DataObject.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class DataObject {

    /**
     * Constants used in class.
     */
    private static final int A22 = 22;

    private static final int A23 = 23;

    private static final int A21 = 21;

    private static final int A12 = 12;

    private static final int A13 = 13;

    private static final int A11 = 11;

    private static final int A100 = 100;

    private static final int A99998 = 99998;

    /**
     * random number generator.
     */
    private static Random random = new Random();

    private boolean bool;

    /**
     * libelle.
     */
    private String libelle;

    /**
     * total.
     */
    private double total;

    /**
     * unit.
     */
    private String unit;

    /**
     * monnaie.
     */
    private String monnaie;

    /**
     * totalValorise.
     */
    private double totalValorise;

    /**
     * field1.
     */
    private Object field1;

    /**
     * field2.
     */
    private Object field2;

    /**
     * field2.
     */
    // private Object field3;
    /**
     * id.
     */
    private int id = -1;

    /**
     * integerValue.
     */
    private int primaryIntegerValue;

    /**
     * integerValue.
     */
    private Integer integerValue1;

    private Integer integerValue2;

    /**
     * emptyString.
     */
    private String emptyString = ""; //$NON-NLS-1$

    /**
     * name.
     */
    /**
     * email.
     */
    private String email;

    /**
     * date.
     */
    private Date date;

    /**
     * money.
     */
    private double money;

    /**
     * description.
     */
    private String description;

    /**
     * long description.
     */
    private String longDescription;

    /**
     * url.
     */
    private String url;

    /**
     * sub list used to test nested tables.
     */
    private List<SubListItem> subList;

    /**
     * sub list used to test nested tables.
     */
    private ArrayList2 vect;

    /**
     * 
     */
    private String[] libelles = new String[] { "abc", "abcd", "abcde", "ABC", "ABCD", "ABCDE", }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

    private String[] units = new String[] { "kvarh", "kWh", "m3", }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private String[] monnaies = new String[] { "Yen", "Euros", }; //$NON-NLS-1$ //$NON-NLS-2$

    private Object arrayField;

    /**
     * Constructor for ListObject.
     */
    @SuppressWarnings("unchecked")
    public DataObject() {
        this.id = random.nextInt(A99998) + 1;
        this.money = (random.nextInt(A99998) + 1) / A100;

        // int n = new Random().nextInt(A3);

        // added sublist for testing of nested tables
        this.subList = new ArrayList<SubListItem>();
        this.subList.add(new SubListItem());
        this.subList.add(new SubListItem());
        this.subList.add(new SubListItem());

        this.vect = new ArrayList2();
        ArrayList2 vect1 = new ArrayList2();
        vect1.add(new Integer(A11));
        vect1.add(new Integer(A12));
        vect1.add(new Integer(A12));
        this.vect.add(vect1);
        ArrayList2 vect2 = new ArrayList2();
        vect2.add(new Integer(A21));
        vect2.add(new Integer(A22));
        vect2.add(new Integer(A22));
        this.vect.add(vect2);

        bool = random.nextBoolean();

        libelle = libelles[random.nextInt(libelles.length)];
        field1 = new Double(money);
        unit = units[random.nextInt(units.length)];
        monnaie = monnaies[random.nextInt(monnaies.length)];
        field2 = new Double((random.nextInt(A99998) + 1) / A100);
        // field3 = new Double((random.nextInt(A99998) + 1) / A100);

    }

    /**
     * getter for id.
     * 
     * @return int id
     */
    public int getId() {
        return this.id;
    }

    /**
     * setter for id.
     * 
     * @param value int id
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * getter for email.
     * 
     * @return String email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * setter for email.
     * 
     * @param value String email
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * getter for date.
     * 
     * @return Date
     */
    public Date getDate() {
        if (this.date != null) {
            return (Date) this.date.clone();
        }
        return null;
    }

    /**
     * getter for money.
     * 
     * @return double money
     */
    public double getMoney() {
        return this.money;
    }

    /**
     * getter for description.
     * 
     * @return String description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * getter for long description.
     * 
     * @return String long description
     */
    public String getLongDescription() {
        return this.longDescription;
    }

    /**
     * getter for url.
     * 
     * @return String url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * test for null values.
     * 
     * @return null
     */
    public String getStringNullValue() {
        return null;
    }

    public String getLibelle() {
        return libelle;
    }

    public double getTotal() {
        return total;
    }

    public double getTotalValorise() {
        return totalValorise;
    }

    public String getUnit() {
        return unit;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public Object getArrayField() {
        return this.arrayField;
    }

    public Object getArrayOfList() {
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(new Integer(A11));
        list1.add(new Integer(A12));
        list1.add(new Integer(A13));
        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(new Integer(A21));
        list2.add(new Integer(A22));
        list2.add(new Integer(A23));
        List<Integer> list3 = new ArrayList<Integer>();
        list3.add(new Integer(A21));
        list3.add(new Integer(A22));
        list3.add(new Integer(A23));
        return new List[] { list1, list2, list3 };
    }

    public Object getField1() {
        return field1;
    }

    public Object getField2() {
        return field2;
    }

    public Object getField3() {
        return field2;
    }

    public void setField1(Object field1) {
        this.field1 = field1;
    }

    public void setField2(Object field2) {
        this.field2 = field2;
    }

    public void setField3(Object field3) {
        // this.field3 = field3;
    }

    /**
     * Returns a simple string representation of the object.
     * 
     * @return String simple representation of the object
     */
    public String toString() {
        return "ListObject(" + this.id + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Returns a detailed string representation of the object.
     * 
     * @return String detailed representation of the object
     */
    public String toDetailedString() {
        return "ID:          " //$NON-NLS-1$
                + this.id + "\n" //$NON-NLS-1$
                + "Email:       " //$NON-NLS-1$
                + this.email + "\n" //$NON-NLS-1$
                + "Date:        " //$NON-NLS-1$
                + this.date + "\n" //$NON-NLS-1$
                + "Money:       " //$NON-NLS-1$
                + this.money + "\n" //$NON-NLS-1$
                + "Description: " //$NON-NLS-1$
                + this.description + "\n" //$NON-NLS-1$
                + "URL:         " //$NON-NLS-1$
                + this.url + "\n"; //$NON-NLS-1$
    }

    /**
     * Returns the subList.
     * 
     * @return List
     */
    public List getSubList() {
        return this.subList;
    }

    /**
     * Returns the subList.
     * 
     * @return List
     */
    public ArrayList2 getVect() {
        return this.vect;
    }

    /**
     * Inner class used in testing nested tables.
     * 
     * @author Fabrizio Giustina
     */
    public static class SubListItem {

        /**
         * name.
         */
        private String itemName;

        /**
         * email.
         */
        private String itemEmail;

        /**
         * Constructor for SubListItem.
         */
        public SubListItem() {
        }

        /**
         * getter for name.
         * 
         * @return String name
         */
        public String getName() {
            return this.itemName;
        }

        /**
         * getter for email.
         * 
         * @return String
         */
        public String getEmail() {
            return this.itemEmail;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE) // 
                    .append("name", this.itemName) //$NON-NLS-1$
                    .append("email", this.itemEmail) //$NON-NLS-1$
                    .toString();
        }
    }

    /**
     * <br/>
     * .
     * 
     */
    class ArrayList2 extends ArrayList {

        /**
         * 
         */
        private static final long serialVersionUID = 4155640352372921319L;

        public Object getValue(int index) {
            return super.get(index);
        }

    }

    public static void setRandom(Random random) {
        DataObject.random = random;
    }

    public void setArrayField(Object arrayField) {
        this.arrayField = arrayField;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setLibelles(String[] libelles) {
        this.libelles = libelles;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public void setMonnaies(String[] monnaies) {
        this.monnaies = monnaies;
    }

    public void setSubList(List<SubListItem> subList) {
        this.subList = subList;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotalValorise(double totalValorise) {
        this.totalValorise = totalValorise;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnits(String[] units) {
        this.units = units;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVect(ArrayList2 vect) {
        this.vect = vect;
    }

    public int getPrimaryIntegerValue() {
        return primaryIntegerValue;
    }

    public void setPrimaryIntegerValue(int integerValue) {
        this.primaryIntegerValue = integerValue;
    }

    public String getEmptyString() {
        return emptyString;
    }

    public void setEmptyString(String emptyString) {
        this.emptyString = emptyString;
    }

    public Integer getIntegerValue2() {
        return this.integerValue2;
    }

    public void setIntegerValue2(Integer integerValue2) {
        this.integerValue2 = integerValue2;
    }

    public Integer getIntegerValue1() {
        return this.integerValue1;
    }

    public void setIntegerValue1(Integer integerValue) {
        this.integerValue1 = integerValue;
    }

    public boolean isBool() {
        return this.bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

}
