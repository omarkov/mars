package web.java;

import server.domain.Department;


public class DepartmentTableDecorator extends org.displaytag.decorator.TableDecorator
{
	public String getLink()
	{
		Department department = (Department)getCurrentRowObject();
		Long id = department.getId();

		return    "<a href=\"manageDepartment-submit.do?currentDepartmentID=" + id
			+ "&submit=edit\">Edit</a> | "
			+ "<a href=\"manageDepartment-submit.do?currentDepartmentID=" + id
			+ "&submit=delete\">Delete</a>";
	}
}
