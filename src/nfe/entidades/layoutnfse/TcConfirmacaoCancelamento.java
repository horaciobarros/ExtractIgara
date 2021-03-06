//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.18 at 10:18:08 AM BRT 
//


package nfe.entidades.layoutnfse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for tcConfirmacaoCancelamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcConfirmacaoCancelamento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Pedido" type="{http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd}tcPedidoCancelamento"/>
 *         &lt;element name="DataHoraCancelamento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd}tsIdTag" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcConfirmacaoCancelamento", propOrder = {
    "pedido",
    "dataHoraCancelamento"
})
public class TcConfirmacaoCancelamento {

    @XmlElement(name = "Pedido", required = true)
    protected TcPedidoCancelamento pedido;
    @XmlElement(name = "DataHora", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataHoraCancelamento;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Gets the value of the pedido property.
     * 
     * @return
     *     possible object is
     *     {@link TcPedidoCancelamento }
     *     
     */
    public TcPedidoCancelamento getPedido() {
        return pedido;
    }

    /**
     * Sets the value of the pedido property.
     * 
     * @param value
     *     allowed object is
     *     {@link TcPedidoCancelamento }
     *     
     */
    public void setPedido(TcPedidoCancelamento value) {
        this.pedido = value;
    }

    /**
     * Gets the value of the dataHoraCancelamento property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataHoraCancelamento() {
        return dataHoraCancelamento;
    }

    /**
     * Sets the value of the dataHoraCancelamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataHoraCancelamento(XMLGregorianCalendar value) {
        this.dataHoraCancelamento = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
