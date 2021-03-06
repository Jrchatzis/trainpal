package com.example.travelinfo.ldbws;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 5.5.0.6
//
// Created by Quasar Development 
//
//---------------------------------------------------


import java.util.Hashtable;
import org.ksoap2.serialization.*;

public class DAADepartureItem extends AttributeContainer implements KvmSerializable
{

    
    public DAAServiceItem service;
    
    public String crs;
    private transient Object __source;
    

    
    
    
    public void loadFromSoap(Object paramObj,DAAExtendedSoapSerializationEnvelope __envelope)
    {
        if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;
        __source=inObj; 
        if(inObj instanceof SoapObject)
        {
            SoapObject soapObject=(SoapObject)inObj;
            int size = soapObject.getPropertyCount();
            for (int i0=0;i0< size;i0++)
            {
                PropertyInfo info=soapObject.getPropertyInfo(i0);
                if(!loadProperty(info,soapObject,__envelope))
                {
                }
            } 
        }
        if (inObj.hasAttribute("crs"))
        {	
            Object j = inObj.getAttribute("crs");
            if (j != null)
            {
                crs = j.toString();
            }
        }

    }

    
    protected boolean loadProperty(PropertyInfo info,SoapObject soapObject,DAAExtendedSoapSerializationEnvelope __envelope)
    {
        Object obj = info.getValue();
        if (info.name.equals("service"))
        {
            if(obj!=null)
            {
                Object j = obj;
                this.service = (DAAServiceItem)__envelope.get(j,DAAServiceItem.class,false);
            }
            return true;
        }
        return false;
    }
    
    public Object getOriginalXmlSource()
    {
        return __source;
    }    
    

    @Override
    public Object getProperty(int propertyIndex) {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==0)
        {
            return this.service!=null?this.service:SoapPrimitive.NullNilElement;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 1;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = DAAServiceItem.class;
            info.name = "service";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
    }
    
    @Override
    public void setProperty(int arg0, Object arg1)
    {
    }



    @Override
    public int getAttributeCount() {
        return 1;
    }
    
    @Override
    public void getAttributeInfo(int index, AttributeInfo info) {
        if(index==0)
        {
            info.name = "crs";
            info.namespace= "";
            if(this.crs!=null)
            {
                info.setValue(this.crs);
            }
            
        }
    }

    @Override
    public void getAttribute(int i, AttributeInfo attributeInfo) {

    }

    @Override
    public void setAttribute(AttributeInfo attributeInfo) {

    }    
}

