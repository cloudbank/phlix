package com.anubis.flickr.util;






/**
 * via codepath libs
*/

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenericsUtil {
    public GenericsUtil() {
    }

    public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
        HashMap resolvedTypes = new HashMap();
        Object type = childClass;

        while(true) {
            int i;
            while(!getClass((Type)type).equals(baseClass)) {
                if(type instanceof Class) {
                    type = ((Class)type).getGenericSuperclass();
                } else {
                    ParameterizedType actualTypeArguments = (ParameterizedType)type;

                    assert actualTypeArguments != null;

                    Class typeArgumentsAsClasses = (Class)actualTypeArguments.getRawType();
                    Type[] baseType = actualTypeArguments.getActualTypeArguments();
                    TypeVariable[] typeParameters = typeArgumentsAsClasses.getTypeParameters();

                    for(i = 0; i < baseType.length; ++i) {
                        resolvedTypes.put(typeParameters[i], baseType[i]);
                    }

                    if(!typeArgumentsAsClasses.equals(baseClass)) {
                        type = typeArgumentsAsClasses.getGenericSuperclass();
                    }
                }
            }

            Object var10;
            if(type instanceof Class) {
                var10 = ((Class)type).getTypeParameters();
            } else {
                assert type != null;

                var10 = ((ParameterizedType)type).getActualTypeArguments();
            }

            ArrayList var11 = new ArrayList();
            Object var9 = var10;
            i = ((Object[])var10).length;

            for(int var13 = 0; var13 < i; ++var13) {
                Object var12;
                for(var12 = ((Object[])var9)[var13]; resolvedTypes.containsKey(var12); var12 = (Type)resolvedTypes.get(var12)) {
                    ;
                }

                var11.add(getClass((Type)var12));
            }

            return var11;
        }
    }

    private static Class<?> getClass(Type type) {
        if(type instanceof Class) {
            return (Class)type;
        } else if(type instanceof ParameterizedType) {
            return getClass(((ParameterizedType)type).getRawType());
        } else if(type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType)type).getGenericComponentType();
            Class componentClass = getClass(componentType);
            return componentClass != null? Array.newInstance(componentClass, 0).getClass():null;
        } else {
            return null;
        }
    }
}