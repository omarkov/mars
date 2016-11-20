/*
 * Title  -> JavaScript Date Chooser
 * Author -> Paul Gration (Gratz)
 * URL    -> http://www.i-labs.org
 * Email  -> pmgration(at)i-labs.org
 */

function JavaScriptDateChooser()
{
    var today;
    //var day_today;
    //var month_today;
    var year_today;
	var expirationDay;
	var expirationMonth;
	var expirationYear;
	
    var months;
    var days;
    var years;

    var day_selected;
    var month_selected;
    var year_selected;
    
    this.JavaScriptDateChooser = function()
    {
    	today = new Date();
		var expirationDate = document.getElementById("expirationDate");
		var value;
		if (expirationDate.value != "null") {
		    value = expirationDate.value;
	    	    expirationDate = new Date(value);
		} else {
		    expirationDate = new Date();
		    expirationDate.setDate(expirationDate.getDate() + 1);
		}
    	expirationDay = expirationDate.getDate();
    	expirationMonth = expirationDate.getMonth();
    	expirationYear = expirationDate.getFullYear();
/*      day_today = today.getDate();
        month_today = today.getMonth();
*/
        year_today = today.getFullYear();

        days = new Array(31,29,31,30,31,30,31,31,30,31,30,31);
        months = new Array("January","February","March","April","May","June","July","August","September","October","November","December");
        years = new Array(15);
        for (var i = 0; i <= 15; i++) {
        	years[i] = year_today + i;
        }

		//document.write("<form id=\"date\" action=\"\" method=\"post\">");
        document.write("        <select id=\"day\" name=\"day\"></select>");
        document.write(" ");
		document.write("		<select id=\"month\" name=\"month\"></select>");
		document.write(" ");
		document.write("		<select id=\"year\" name=\"year\"></select>");
		//document.write("</form>");

        day_selected = document.getElementById("day");
        month_selected = document.getElementById("month");
        year_selected = document.getElementById("year");

        month_selected.onchange = this.update;
        year_selected.onchange = this.update;

        this.setYears();
        this.setMonths();
        this.setDays();
        this.update();
    }

    this.setYears = function()
    {
	  var selected;
        for(i in years)
        {  
		selected = false;
        	if (years[i] == expirationYear) {
            	selected = true;
 
            }
            year_selected.options[year_selected.options.length] = new Option(years[i], years[i], selected);

        }
    }

    this.setMonths = function()
    {
        for(i in months)
        {
            month_selected.options[month_selected.options.length] = new Option(months[i], i);
        }
        month_selected.options[expirationMonth].selected = true;
    }

    this.setDays = function()
    {
        for(i = 1; i <= days[expirationMonth]; i++)
        {
            day_selected.options[day_selected.options.length] = new Option(i, i);
        }
        day_selected.options[expirationDay-1].selected = true;
    }

    this.update = function()
    {
        var year = year_selected.options[year_selected.selectedIndex].value;
        var prev = day_selected.selectedIndex;

		
        if(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
        {
            days[1] = 29;
        }
        else
        {
            days[1] = 28;
        }

        for(i = day_selected.options.length; i > days[1]; i--)
        {
            day_selected.options[i-1] = null;
        }

        for(i = day_selected.options.length; i < days[month_selected.selectedIndex]; i++)
        {
            day_selected.options[day_selected.options.length] = new Option(i+1, i+1);
        }

        if(day_selected.options.length > prev)
        {
            day_selected.options[prev].selected = true;
        }
    }
}
