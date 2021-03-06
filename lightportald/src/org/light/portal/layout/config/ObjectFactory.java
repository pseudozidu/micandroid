//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.23 at 02:16:13 PM PDT 
//


package org.light.portal.layout.config;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Moveable_QNAME = new QName("", "moveable");
    private final static QName _BarBgColor_QNAME = new QName("", "barBgColor");
    private final static QName _NoConfigMode_QNAME = new QName("", "noConfigMode");
    private final static QName _Widths_QNAME = new QName("", "widths");
    private final static QName _BarFontColor_QNAME = new QName("", "barFontColor");
    private final static QName _AllowAddContent_QNAME = new QName("", "allowAddContent");
    private final static QName _Editable_QNAME = new QName("", "editable");
    private final static QName _Row_QNAME = new QName("", "row");
    private final static QName _Url_QNAME = new QName("", "url");
    private final static QName _NoEditMode_QNAME = new QName("", "noEditMode");
    private final static QName _ContentBgColor_QNAME = new QName("", "contentBgColor");
    private final static QName _Closeable_QNAME = new QName("", "closeable");
    private final static QName _Title_QNAME = new QName("", "title");
    private final static QName _Color_QNAME = new QName("", "color");
    private final static QName _Name_QNAME = new QName("", "name");
    private final static QName _Column_QNAME = new QName("", "column");
    private final static QName _NotCloseable_QNAME = new QName("", "notCloseable");
    private final static QName _PortletWindow_QNAME = new QName("", "portletWindow");
    private final static QName _Between_QNAME = new QName("", "between");
    private final static QName _Defaulted_QNAME = new QName("", "defaulted");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Portlet }
     * 
     */
    public Portlet createPortlet() {
        return new Portlet();
    }

    /**
     * Create an instance of {@link Portlets }
     * 
     */
    public Portlets createPortlets() {
        return new Portlets();
    }

    /**
     * Create an instance of {@link PortalTab }
     * 
     */
    public PortalTab createPortalTab() {
        return new PortalTab();
    }

    /**
     * Create an instance of {@link PortalUser }
     * 
     */
    public PortalUser createPortalUser() {
        return new PortalUser();
    }

    /**
     * Create an instance of {@link PortalLayout }
     * 
     */
    public PortalLayout createPortalLayout() {
        return new PortalLayout();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "moveable")
    public JAXBElement<Boolean> createMoveable(Boolean value) {
        return new JAXBElement<Boolean>(_Moveable_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "barBgColor")
    public JAXBElement<String> createBarBgColor(String value) {
        return new JAXBElement<String>(_BarBgColor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "noConfigMode")
    public JAXBElement<Boolean> createNoConfigMode(Boolean value) {
        return new JAXBElement<Boolean>(_NoConfigMode_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "widths")
    public JAXBElement<String> createWidths(String value) {
        return new JAXBElement<String>(_Widths_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "barFontColor")
    public JAXBElement<String> createBarFontColor(String value) {
        return new JAXBElement<String>(_BarFontColor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowAddContent")
    public JAXBElement<Boolean> createAllowAddContent(Boolean value) {
        return new JAXBElement<Boolean>(_AllowAddContent_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "editable")
    public JAXBElement<Boolean> createEditable(Boolean value) {
        return new JAXBElement<Boolean>(_Editable_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "row")
    public JAXBElement<BigInteger> createRow(BigInteger value) {
        return new JAXBElement<BigInteger>(_Row_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "url")
    public JAXBElement<String> createUrl(String value) {
        return new JAXBElement<String>(_Url_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "noEditMode")
    public JAXBElement<Boolean> createNoEditMode(Boolean value) {
        return new JAXBElement<Boolean>(_NoEditMode_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "contentBgColor")
    public JAXBElement<String> createContentBgColor(String value) {
        return new JAXBElement<String>(_ContentBgColor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "closeable")
    public JAXBElement<Boolean> createCloseable(Boolean value) {
        return new JAXBElement<Boolean>(_Closeable_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "color")
    public JAXBElement<String> createColor(String value) {
        return new JAXBElement<String>(_Color_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "column")
    public JAXBElement<BigInteger> createColumn(BigInteger value) {
        return new JAXBElement<BigInteger>(_Column_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "notCloseable")
    public JAXBElement<Boolean> createNotCloseable(Boolean value) {
        return new JAXBElement<Boolean>(_NotCloseable_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "portletWindow")
    public JAXBElement<String> createPortletWindow(String value) {
        return new JAXBElement<String>(_PortletWindow_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "between")
    public JAXBElement<BigInteger> createBetween(BigInteger value) {
        return new JAXBElement<BigInteger>(_Between_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "defaulted")
    public JAXBElement<Boolean> createDefaulted(Boolean value) {
        return new JAXBElement<Boolean>(_Defaulted_QNAME, Boolean.class, null, value);
    }

}
