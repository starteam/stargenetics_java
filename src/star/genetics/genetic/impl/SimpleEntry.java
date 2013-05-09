package star.genetics.genetic.impl;

import java.io.Serializable;
import java.util.Map.Entry;

import utils.Equals;

public class SimpleEntry<K, V> implements Entry<K, V>, Serializable
{

	private static final long serialVersionUID = 1L;
	K key;
	V value;

	public SimpleEntry(K key, V value)
	{
		this.key = key;
		this.value = value;
	}

	public K getKey()
	{

		return key;
	}

	public V getValue()
	{
		return value;
	}

	public V setValue(V value)
	{
		V old = this.value;
		this.value = value;
		return old;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Entry)
		{
			@SuppressWarnings("unchecked")
			Entry<K, V> that = (Entry<K, V>) obj;
			return Equals.isEquals(this.getKey(), that.getKey()) && Equals.isEquals(this.getValue(), that.getValue());
		}
		return false;
	}
}
