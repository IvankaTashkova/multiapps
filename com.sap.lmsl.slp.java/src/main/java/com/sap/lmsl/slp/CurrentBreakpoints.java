
package com.sap.lmsl.slp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{http://www.sap.com/lmsl/slp}Breakpoint"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "breakpoint"
})
@XmlRootElement(name = "currentBreakpoints")
public class CurrentBreakpoints {

    @XmlElement(name = "Breakpoint")
    protected List<Breakpoint> breakpoint;

    /**
     * Gets the value of the breakpoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the breakpoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBreakpoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Breakpoint }
     * 
     * 
     */
    public List<Breakpoint> getBreakpoint() {
        if (breakpoint == null) {
            breakpoint = new ArrayList<Breakpoint>();
        }
        return this.breakpoint;
    }

}
