public class ListQueue implements Queue
{
	protected Link head;
	protected Link tail;

	ListQueue()
	{
		head = null;
		tail = null;
	}

	public void enqueue(Object elem)
	{
		if (head == null)
		{
			head = new Link(elem);
			tail = head;
		} else
		{
			tail.setNext(new Link(elem));
			tail = tail.next();
		}
	}

	public Object dequeue()
	{
		Object dequeued_elem;

		if (head == null)
			return null;
		dequeued_elem = head.element();
		head = head.next;
		if (head == null)
			tail = null;
		return dequeued_elem;
	}

	public boolean empty()
	{
		return (head == null);
	}

	public String toString()
	{
		String result = "[";
		Link tmp = head;
		if (tmp != null)
		{
			result = result + tmp.element();
			tmp = tmp.next();
			while (tmp != null)
			{
				result = result + "," + tmp.element();
				tmp = tmp.next();
			}
		}
		result = result + "]";
		return result;
	}

	public class Link
	{
		private Object element;
		private Link next;

		public Link(Object newelement)
		{
			element = newelement;
			next = null;
		}

		public Link(Object newelement, Link newnext)
		{
			element = newelement;
			next = newnext;
		}

		public Link next()
		{
			return next;
		}

		public Object element()
		{
			return element;
		}

		public void setNext(Link newnext)
		{
			next = newnext;
		}

		public void setElement(Object newelement)
		{
			element = newelement;
		}
	}
}
