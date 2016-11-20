package net.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class ObjectStream extends XStream {
	public ObjectStream()
	{
		this.registerConverter(new HibernateCollectionConverter(this.getConverterLookup()));
	}

    protected MapperWrapper wrapMapper(MapperWrapper next)
    {
        return new HibernateCollectionsMapper(new CGLibMapper(next));
    }
}
