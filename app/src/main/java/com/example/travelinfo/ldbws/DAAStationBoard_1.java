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

public class DAAStationBoard_1 extends DAABaseStationBoard implements KvmSerializable
{

    
    public DAAArrayOfServiceItems_1 trainServices;
    
    public DAAArrayOfServiceItems_1 busServices;
    
    public DAAArrayOfServiceItems_1 ferryServices;
    

    
    
    @Override
    public void loadFromSoap(Object paramObj,DAAExtendedSoapSerializationEnvelope __envelope)
    {
        if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;
        super.loadFromSoap(paramObj, __envelope);

    }

    @Override
    protected boolean loadProperty(PropertyInfo info,SoapObject soapObject,DAAExtendedSoapSerializationEnvelope __envelope)
    {
        Object obj = info.getValue();
        if (info.name.equals("trainServices"))
        {
            if(obj!=null)
            {
                Object j = obj;
                this.trainServices = (DAAArrayOfServiceItems_1)__envelope.get(j,DAAArrayOfServiceItems_1.class,false);
            }
            return true;
        }
        if (info.name.equals("busServices"))
        {
            if(obj!=null)
            {
                Object j = obj;
                this.busServices = (DAAArrayOfServiceItems_1)__envelope.get(j,DAAArrayOfServiceItems_1.class,false);
            }
            return true;
        }
        if (info.name.equals("ferryServices"))
        {
            if(obj!=null)
            {
                Object j = obj;
                this.ferryServices = (DAAArrayOfServiceItems_1)__envelope.get(j,DAAArrayOfServiceItems_1.class,false);
            }
            return true;
        }
        return super.loadProperty(info,soapObject,__envelope);
    }
    
    

    @Override
    public Object getProperty(int propertyIndex) {
        int count = super.getPropertyCount();
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==count+0)
        {
            return this.trainServices!=null?this.trainServices:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==count+1)
        {
            return this.busServices!=null?this.busServices:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==count+2)
        {
            return this.ferryServices!=null?this.ferryServices:SoapPrimitive.NullSkip;
        }
        return super.getProperty(propertyIndex);
    }


    @Override
    public int getPropertyCount() {
        return super.getPropertyCount()+3;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        int count = super.getPropertyCount();
        if(propertyIndex==count+0)
        {
            info.type = PropertyInfo.VECTOR_CLASS;
            info.name = "trainServices";
            info.namespace= "http://thalesgroup.com/RTTI/2016-02-16/ldb/types";
        }
        if(propertyIndex==count+1)
        {
            info.type = PropertyInfo.VECTOR_CLASS;
            info.name = "busServices";
            info.namespace= "http://thalesgroup.com/RTTI/2016-02-16/ldb/types";
        }
        if(propertyIndex==count+2)
        {
            info.type = PropertyInfo.VECTOR_CLASS;
            info.name = "ferryServices";
            info.namespace= "http://thalesgroup.com/RTTI/2016-02-16/ldb/types";
        }
        super.getPropertyInfo(propertyIndex,arg1,info);
    }
    
    @Override
    public void setProperty(int arg0, Object arg1)
    {
    }

    
}

