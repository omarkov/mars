package net.xstream;

import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * Mapper that removes the annoying CGLib signature which generates an unsuable
 * XML (the classes mentioned in there do not exist).
 *  
 * <br>
 * <strong>NOTE</strong> This mapper takes care only of the writing to
 * the XML (deflating) not the other way around (inflating) because there is no
 * need.
 * 
 * @author Costin Leau
 * 
 */
public class CGLibMapper extends MapperWrapper {
    public static final String marker = new String("EnhancerByCGLIB");

    public CGLibMapper(ClassMapper wrapped) {
        super(wrapped);
    }

    /**
     * @see com.thoughtworks.xstream.alias.ClassMapper#mapNameToXML(java.lang.String)
     */
    public String mapNameToXML(String javaName) {
        return removeSignature(super.mapNameToXML(javaName));
    }

    /**
     * @see com.thoughtworks.xstream.mapper.Mapper#serializedClass(java.lang.Class)
     */
    public String serializedClass(Class type) {
        return removeSignature(super.serializedClass(type));
    }

    /**
     * @see com.thoughtworks.xstream.mapper.Mapper#serializedMember(java.lang.Class,
     *      java.lang.String)
     */
    public String serializedMember(Class type, String memberName) {
        return removeSignature(super.serializedMember(type, memberName));
    }

    private String removeSignature(String name) {
        int count = name.indexOf(marker);
        if (count >= 0) {
            count -= 2;
            return name.substring(0, count);
        }
        return name;
    }
}
