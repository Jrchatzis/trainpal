package com.example.travelinfo.ldbws;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 5.5.0.6
//
// Created by Quasar Development 
//
//---------------------------------------------------


import org.ksoap2.serialization.*;

public class DAAToiletAvailabilityType  
{

    
    public String value;
    
    public DAAEnums.ToiletStatus status;
    private transient Object __source;
    

    
    
    
    public void loadFromSoap(Object paramObj,DAAExtendedSoapSerializationEnvelope __envelope)
    {
        if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;
        __source=inObj; 
        if(!(inObj instanceof SoapObject))
        {
            Object j =(Object)inObj;
            value = j.toString();
        }
        if (inObj.hasAttribute("status"))
        {	
            Object j = inObj.getAttribute("status");
            if (j != null)
            {
                status = DAAEnums.ToiletStatus.fromString(j.toString());
            }
        }

    }

    
    protected boolean loadProperty(PropertyInfo info,SoapObject soapObject,DAAExtendedSoapSerializationEnvelope __envelope)
    {
        Object obj = info.getValue();
        if (info.name.equals("value"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.value = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.value = (String)obj;
                }
            }
            return true;
        }
        return false;
    }
    
    public Object getOriginalXmlSource()
    {
        return __source;
    }    
    
    public Object getSimpleValue()
    {
        Object value=this.value != null ? this.value.toString() : "";
        SoapPrimitive primitive = new SoapPrimitive("http://thalesgroup.com/RTTI/2017-10-01/ldb/commontypes", "value",value);
        
        if (this.status != null)
        {
            AttributeInfo attrInfo = new AttributeInfo();
            attrInfo.setName("status");
            attrInfo.setValue(this.status);
            attrInfo.setNamespace("");
            primitive.addAttribute(attrInfo);
        }
        return primitive;
    }    
}

