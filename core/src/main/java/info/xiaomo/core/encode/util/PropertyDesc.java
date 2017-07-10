package info.xiaomo.core.encode.util;

import java.lang.reflect.Method;

/**
 * 属性信息描述
 * @author Administrator
 *
 */
public class PropertyDesc
{

    public static final PropertyDesc NULL = new PropertyDesc();

    private String name;

    private Class<?> propertyType;

    private Method writeMethod;

    private Method readMethod;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Class<?> getPropertyType()
    {
        return propertyType;
    }

    public void setPropertyType(Class<?> propertyType)
    {
        this.propertyType = propertyType;
    }

    public Method getWriteMethod()
    {
        return writeMethod;
    }

    public void setWriteMethod(Method writeMethod)
    {
        this.writeMethod = writeMethod;
    }

    public Method getReadMethod()
    {
        return readMethod;
    }

    public void setReadMethod(Method readMethod)
    {
        this.readMethod = readMethod;
    }

}
