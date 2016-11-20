package net.xstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentSet;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * XStream converter that strips HB collections specific information and retrieves the underlying 
 * collection which is then parsed by the delegated converter. This converter only takes care of the 
 * values inside the collections while the mapper takes care of the collections naming. 
 *  
 * @author Costin Leau
 *
 */
public class HibernateCollectionConverter implements Converter
{
    private Converter listSetConverter;
    private Converter mapConverter;
    private Converter treeMapConverter;
    private Converter treeSetConverter;
    private Converter defaultConverter;
    private static final String _CGLIB_ = "CGLIB";
    private static final String _ENHANCER_ = "Enhancer";
    
    public HibernateCollectionConverter(ConverterLookup converterLookup)
    {
        listSetConverter = converterLookup
            .lookupConverterForType(ArrayList.class);
        mapConverter = converterLookup.lookupConverterForType(HashMap.class);
        treeMapConverter = converterLookup
            .lookupConverterForType(TreeMap.class);
        treeSetConverter = converterLookup
            .lookupConverterForType(TreeSet.class);
        defaultConverter = converterLookup.lookupConverterForType(Object.class);
    }

    /**
     * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
     */
    public boolean canConvert(Class type)
    {
        return PersistentCollection.class.isAssignableFrom(type) || type.toString().indexOf(_CGLIB_) >= 0;
    }

    /**
     * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
     */
    public void marshal(Object source, HierarchicalStreamWriter writer,
        MarshallingContext context)
    {
        Object collection = source;
        String className = source.getClass().toString();
        
/*        if(className.indexOf(_CGLIB_) >= 0 && className.indexOf(_ENHANCER_) < 0)
        {
        	System.out.println("removing Class : " + source.getClass().getName());
        	return;
        }*/
        
        if(source instanceof PersistentBag)
        {
            if(((PersistentBag)source).wasInitialized())
            {
		//System.out.println("Translating PersitentBag");
        	    PersistentBag bag = (PersistentBag)source;
        	    bag.forceInitialization();
        	    collection = new ArrayList();
        	    ((ArrayList)collection).addAll(bag);
            }else
            {
		//  System.out.println("Removing PersitentBag");
        	    collection = new ArrayList();
            }
        }else
        if (source instanceof PersistentSet)
        {
            if(((PersistentSet)source).wasInitialized())
            {
            	//System.out.println("Translating PersitentSet");
                // the set is returned as a map by Hibernate (unclear why exactly)
//                PersistentCollection col = (PersistentCollection) source;
                Object objs[] = ((PersistentSet)collection).toArray();
                collection = new HashSet();
                for(int i=0; i < objs.length; i++)
                {
                    ((HashSet)collection).add(objs[i]);
                }
                // collection = new HashSet(); // FIXME ((HashMap)collection).entrySet());
            }else
            {
            	//System.out.println("Removing PersitentSet");
                collection = new HashSet();
            }
        }else
        if (source instanceof PersistentCollection)
        {
            /*if(((PersistentCollection)source).wasInitialized())
            {
            	System.out.println("Translating PersitentCollection");
                PersistentCollection col = (PersistentCollection) source;
                col.forceInitialization();
                collection = col.getCollectionSnapshot().getSnapshot();
            }else
            {*/
	    //System.out.println("Removing PersitentCollection");
                collection = new HashSet();
            //}
        }
 
        
        // delegate the collection to the approapriate converter
        if (listSetConverter.canConvert(collection.getClass()))
        {
            listSetConverter.marshal(collection, writer, context);
            return;
        }
        if (mapConverter.canConvert(collection.getClass()))
        {
            mapConverter.marshal(collection, writer, context);
            return;
        }
        if (treeMapConverter.canConvert(collection.getClass()))
        {
            treeMapConverter.marshal(collection, writer, context);
            return;
        }
        if (treeSetConverter.canConvert(collection.getClass()))
        {
            treeSetConverter.marshal(collection, writer, context);
            return;
        }

        defaultConverter.marshal(collection, writer, context);
    }
    
    /**
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    public Object unmarshal(HierarchicalStreamReader reader,
        UnmarshallingContext context)
    {
        return null;
    }
}
