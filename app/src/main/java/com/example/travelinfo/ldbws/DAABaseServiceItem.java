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

public class DAABaseServiceItem extends AttributeContainer implements KvmSerializable
{

    
    public String sta;
    
    public String eta;
    
    public String std;
    
    public String etd;
    
    public String platform;
    
    public String _operator;
    
    public String operatorCode;
    
    public Boolean isCircularRoute;
    
    public Boolean isCancelled;
    
    public Boolean filterLocationCancelled;
    
    public DAAEnums.ServiceType serviceType=DAAEnums.ServiceType.train;
    
    public Integer length;
    
    public Boolean detachFront;
    
    public Boolean isReverseFormation;
    
    public String cancelReason;
    
    public String delayReason;
    
    public String serviceID;
    
    public DAAArrayOfAdhocAlert adhocAlerts;
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

    }

    
    protected boolean loadProperty(PropertyInfo info,SoapObject soapObject,DAAExtendedSoapSerializationEnvelope __envelope)
    {
        Object obj = info.getValue();
        if (info.name.equals("sta"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.sta = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.sta = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("eta"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.eta = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.eta = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("std"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.std = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.std = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("etd"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.etd = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.etd = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("platform"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.platform = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.platform = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("operator"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this._operator = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this._operator = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("operatorCode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.operatorCode = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.operatorCode = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("isCircularRoute"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.isCircularRoute = new Boolean(j.toString());
                    }
                }
                else if (obj instanceof Boolean){
                    this.isCircularRoute = (Boolean)obj;
                }
            }
            return true;
        }
        if (info.name.equals("isCancelled"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.isCancelled = new Boolean(j.toString());
                    }
                }
                else if (obj instanceof Boolean){
                    this.isCancelled = (Boolean)obj;
                }
            }
            return true;
        }
        if (info.name.equals("filterLocationCancelled"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.filterLocationCancelled = new Boolean(j.toString());
                    }
                }
                else if (obj instanceof Boolean){
                    this.filterLocationCancelled = (Boolean)obj;
                }
            }
            return true;
        }
        if (info.name.equals("serviceType"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.serviceType = DAAEnums.ServiceType.fromString(j.toString());
                    }
                }
                else if (obj instanceof DAAEnums.ServiceType){
                    this.serviceType = (DAAEnums.ServiceType)obj;
                }
            }
            return true;
        }
        if (info.name.equals("length"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.length = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.length = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("detachFront"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.detachFront = new Boolean(j.toString());
                    }
                }
                else if (obj instanceof Boolean){
                    this.detachFront = (Boolean)obj;
                }
            }
            return true;
        }
        if (info.name.equals("isReverseFormation"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.isReverseFormation = new Boolean(j.toString());
                    }
                }
                else if (obj instanceof Boolean){
                    this.isReverseFormation = (Boolean)obj;
                }
            }
            return true;
        }
        if (info.name.equals("cancelReason"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.cancelReason = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.cancelReason = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("delayReason"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.delayReason = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.delayReason = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("serviceID"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.serviceID = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.serviceID = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("adhocAlerts"))
        {
            if(obj!=null)
            {
                Object j = obj;
                this.adhocAlerts = (DAAArrayOfAdhocAlert)__envelope.get(j,DAAArrayOfAdhocAlert.class,false);
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
            return this.sta!=null?this.sta:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==1)
        {
            return this.eta!=null?this.eta:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==2)
        {
            return this.std!=null?this.std:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==3)
        {
            return this.etd!=null?this.etd:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==4)
        {
            return this.platform!=null?this.platform:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==5)
        {
            return _operator;
        }
        if(propertyIndex==6)
        {
            return operatorCode;
        }
        if(propertyIndex==7)
        {
            return this.isCircularRoute!=null?this.isCircularRoute:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==8)
        {
            return this.isCancelled!=null?this.isCancelled:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==9)
        {
            return this.filterLocationCancelled!=null?this.filterLocationCancelled:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==10)
        {
            return this.serviceType!=null?this.serviceType.toString():SoapPrimitive.NullSkip;
        }
        if(propertyIndex==11)
        {
            return this.length!=null?this.length:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==12)
        {
            return this.detachFront!=null?this.detachFront:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==13)
        {
            return this.isReverseFormation!=null?this.isReverseFormation:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==14)
        {
            return this.cancelReason!=null?this.cancelReason:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==15)
        {
            return this.delayReason!=null?this.delayReason:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==16)
        {
            return serviceID;
        }
        if(propertyIndex==17)
        {
            return this.adhocAlerts!=null?this.adhocAlerts:SoapPrimitive.NullSkip;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 18;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "sta";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "eta";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==2)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "std";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==3)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "etd";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==4)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "platform";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==5)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "operator";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==6)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "operatorCode";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==7)
        {
            info.type = PropertyInfo.BOOLEAN_CLASS;
            info.name = "isCircularRoute";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==8)
        {
            info.type = PropertyInfo.BOOLEAN_CLASS;
            info.name = "isCancelled";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==9)
        {
            info.type = PropertyInfo.BOOLEAN_CLASS;
            info.name = "filterLocationCancelled";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==10)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "serviceType";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==11)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "length";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==12)
        {
            info.type = PropertyInfo.BOOLEAN_CLASS;
            info.name = "detachFront";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==13)
        {
            info.type = PropertyInfo.BOOLEAN_CLASS;
            info.name = "isReverseFormation";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==14)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "cancelReason";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==15)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "delayReason";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==16)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "serviceID";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
        if(propertyIndex==17)
        {
            info.type = PropertyInfo.VECTOR_CLASS;
            info.name = "adhocAlerts";
            info.namespace= "http://thalesgroup.com/RTTI/2015-11-27/ldb/types";
        }
    }
    
    @Override
    public void setProperty(int arg0, Object arg1)
    {
    }

    
}

