package com.ruskonert.GameServer.asm;

import com.ruskonert.GameServer.program.ProgramManager;
import com.ruskonert.GameServer.util.SystemUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * TargetReference가 첨부되어 있는 필드의 값을 모두 가져오는 클래스입니다. 해당 클래스는 단독으로 사용될 수 없으며,
 * 클래스에 상속하여 사용할 수 있습니다.<br> 자식 클래스에서는 {@link #onInit(Object)}을 오버라이딩하여 이 클래스에게
 * 값을 넘겨주면 자동으로 프로세싱합니다. 코드의 예시는 다음과 같습니다.
 * <pre>
 * <code>
 * public void subClass extends TargetBuilder<subClass>
 * {
 *     ...
 *     @Override
 *     public Object onInit(Object handleInstance)
 *     {
 *         super.onInit(this);
 *         ...
 *         // return this;
 *         // return null;
 *     }
 * }
 * </code>
 * </pre>
 * @param <E> 서브 클래스의 타입
 * @author Ruskonert (Junwon Kim)
 */
public abstract class TargetBuilder<E> implements HandleInstance
{
    private Object genericInstance;
    public Object getGenericInstance() { return this.genericInstance; }

    private Class<E> genericType;
    public Class<E> getGenericType() { return this.genericType; }

    private List<Field> referenceFields = new ArrayList<>();
    public List<Field> getReferenceFields() { return this.referenceFields; }

    @SuppressWarnings({"rawtype", "unchecked", "ReflectionForUnavailableAnnotation"})
    protected TargetBuilder()
    {
        this.genericType = (Class<E>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.onInit(null);
    }

    public static Object getAnnotationDefaultValue(Class<? extends Annotation> anno, String fieldName)
    {
        Method method = null;
        try
        {
            method = anno.getDeclaredMethod(fieldName);
        }
        catch (NoSuchMethodException e)
        {
            SystemUtil.Companion.error(e);
        }
        assert method != null;
        return method.getDefaultValue();
    }

    @Override
    public Object onInit(Object handleInstance)
    {
        this.genericInstance = handleInstance;
        for(Field field : this.genericInstance.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            if(field.getAnnotation(TargetReference.class) == null) continue;
            TargetReference reference = field.getAnnotation(TargetReference.class);

            String defaultPackageName = (String)getAnnotationDefaultValue(TargetReference.class, "target");
            String packageName = reference.target();
            if(!packageName.equalsIgnoreCase(defaultPackageName))
            {
                packageName = defaultPackageName + "." + reference.target();
            }
            String fieldName = reference.value();
            try
            {
                if (fieldName.split("#").length > 1)
                {
                    Class<?> referenceClazz = Class.forName(packageName);
                    Field methodToField = referenceClazz.getDeclaredField(fieldName.split("#")[0]);
                    Class<?> targetClazz = this.getFromComponents(methodToField).getClass();
                    Method referenceMethod = targetClazz.getMethod(fieldName.split("#")[1]);

                    field.set(this.genericInstance, referenceMethod.invoke(this.getFromComponents(methodToField)));
                }
                else
                {
                    Class<?> referenceClazz = Class.forName(packageName);
                    Field targetField = referenceClazz.getDeclaredField(fieldName);
                    targetField.setAccessible(true);

                    Object value = this.getFromComponents(targetField);
                    field.set(this.genericInstance, value);
                }
                this.referenceFields.add(field);
            }
            catch (InvocationTargetException | NoSuchFieldException |
                    ClassNotFoundException | IllegalAccessException |
                    NoSuchMethodException e)
            {
                SystemUtil.Companion.error(e);
            }
        }
        return this;
    }

    protected Object getFromComponents(Field field)
    {
        field.setAccessible(true);
        try
        {
            return field.get(ProgramManager.getProgramComponent());
        }
        catch(IllegalArgumentException e)
        {
            try
            {
                return field.get(ProgramManager.getPreloadComponent());
            }
            catch(IllegalArgumentException e2)
            {
                SystemUtil.Companion.error(e2);
                return null;
            }
            catch(IllegalAccessException e3)
            {
                SystemUtil.Companion.error(e3);
            }
        }
        catch(IllegalAccessException e)
        {
            SystemUtil.Companion.error(e);
        }
        return null;
    }

}
