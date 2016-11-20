package net;

import java.util.HashMap;
import java.util.Map;
 
public class PrimitiveLoader extends ClassLoader {
		public static PrimitiveLoader loader = new PrimitiveLoader();
		
        public PrimitiveLoader() {
                super();
        }
        
        private static Map nameToPrimitiveClass = new HashMap();
        static {
                nameToPrimitiveClass.put("boolean",Boolean.TYPE);
                nameToPrimitiveClass.put("byte",Byte.TYPE);
                nameToPrimitiveClass.put("char",Character.TYPE);
                nameToPrimitiveClass.put("short",Short.TYPE);
                nameToPrimitiveClass.put("int",Integer.TYPE);
                nameToPrimitiveClass.put("long",Long.TYPE);
                nameToPrimitiveClass.put("float",Float.TYPE);
                nameToPrimitiveClass.put("double",Double.TYPE);
        }
        
        protected Class findClass(String name) 
                throws ClassNotFoundException
        {
        	try
        	{
        		return Class.forName(name);
        	}
        	catch(ClassNotFoundException e)
        	{
        		Class c = (Class)nameToPrimitiveClass.get(name);
        		if( c == null ) throw new ClassNotFoundException(name);
        		return c;
        	}
        }
        
        public String toString() {
                return "PrimitiveLoader";
        }
}