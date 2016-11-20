//javaScript.js

    function getSelectedLine() {  
    
    var obj_list_box = document.my_form.my_select;
       alert(obj_list_box.options[obj_list_box.selectedIndex].value)
    }
	
	function processEditGroup() {
		javascript:window.location='manageGroupEdit.jsp';
		// get the selected Group and submit
	}
	
	//used to propagate the pressed delete or edit button in 
	//manageUser.jsp
	function setCurrentUserIndex(target) {
		document.forms[0].currentUserIndex.value=target;
	}
	
	//used to propagate the pressed delete or edit button in 
	//manageGroup.jsp
	function setCurrentGroupIndex(target) {
		document.forms[0].currentGroupIndex.value=target;
	}
	
	function setDispatch(target){
		document.forms[0].dispatch.value=target;
	} 
	//used to propagate the pressed delete or edit button in 
	//manageProfilesShow.jsp
	function setCurrentProfileIndex(target) {
		document.forms[0].currentProfileIndex.value=target;
	}
	
	// set action to invoke and submit to the corresponding Actionclass
	// only for manageGroup*.jsp-page
	function setAction(tmp) {
		document.forms[0].faction.value=tmp;					
		document.forms[0].submit();
	}
	
	// set selected group in hidden field, submit formular and reload the page 
	// only for manageGroup.jsp-Site
	function getfgroup(){
		var groupstr = "";
		var index;
		
		for (i = 0; i < document.forms[0].getElementsByTagName("option").length; ++i)
	  	{
			if (document.forms[0].getElementsByTagName("option")[i].selected == true)
		 	{
				groupstr = document.forms[0].getElementsByTagName("option")[i].value;
				// Because of : groups.add( tmpGroup.getName() + " ( " + .....
				index = groupstr.indexOf(" (");
				index = index - 1;
				document.forms[0].fgroupname.value = groupstr.substring(0, index);
		 	}
	  	}	 	
	  	// set action to showdetails and submit the form to relod the page
	  	document.forms[0].faction.value = "showDetails";
	  	document.forms[0].submit();
	}
		
		
	// Function to add objects to a listboxes - multiselection is possible
	// sSource = String-ID of listbox, which is the list with available content
	// sTarget = String-ID of listbox, which is the playlist

    function setVideoLists(videoLists)
    {
        document.videoLists = videoLists.split(";");
    }

    function setNoVideoLists(noVideoLists)
    {
        document.noVideoLists = noVideoLists.split(";");
    }
	
	
	function AddToList(isVideo, sSource, sTarget, warning) {
	 //Finding elements on HTML-page
	 var source = document.getElementById(sSource);
	 var target = document.getElementById(sTarget);
     var add = true;

     if(isVideo)
     {
        for(i=0; i < document.noVideoLists.length; i++)
        {
            var list = document.getElementById(document.noVideoLists[i]);
            if(list.length > 0)
            {
               add = false; 
            }
        }
     }else
     {
        for(i=0; i < document.videoLists.length; i++)
        {
            var list = document.getElementById(document.videoLists[i]);
            if(list.length > 0)
            {
               add = false; 
            }
        }
     }

     if(add)
     {
	    //copying selected elements to Playlist-listbox
	    for ( i=0; i < source.length; i++) {
	        if (source.options[i].selected == true) {
	           newEntry = new Option(source.options[i].text,source.options[i].value);
	           target.options[target.length] = newEntry;
	        }
        }
     }
     else
     {
     		alert(warning);
     }
	}
	
	// Function to remove objects in a listbox - multiselection is possible
	// sTarget = String-ID of listbox, which is the Playlist
	function RemoveFromList (sTarget)
	{
	 var target = document.getElementById(sTarget);
	 
	 for ( i=0; i < target.length; i++) {
	   if (target.options[i].selected == true) {
	     target.options[i] = null;
	     i--;
	   }
	 }
	}
	
	// only for manageProfileNew/Edit.jsp-pages
	function selectAllOptionElements() 
	{
	   var i;
	   var j;
           var selects = document.getElementsByTagName("select");
	   for (i = 0; i < selects.length; i++)
	   {
		if(selects[i].multiple)
	   	{
           	     var options = selects[i].options;
	             for(j = 0; j < options.length; j++)
	             {
		          options[j].selected = true;
                     }
                }
	   }	    		
	}

	//used in manageProfilesEdit.jsp to incement/decrement a numeric value.
	function numericChange(tagId, min, max, step, operation) {
		
		var intValue = parseInt(document.getElementById(tagId).value);
		var intMin = parseInt(min);
		var intMax = parseInt(max);
		var intStep = parseInt(step);
		
		if (operation == "+") {
			var newValuePlus = intValue + intStep; 
			if (newValuePlus >= intMin) {
			   if (newValuePlus <= intMax) {
				document.getElementById(tagId).value = newValuePlus;
			   }
			}
			
		} 
		if (operation == "-") {
			var newValueMinus = intValue - intStep; 
			if (newValueMinus >= intMin) {
			   if (newValueMinus <= intMax) {
				document.getElementById(tagId).value = newValueMinus;
			   }
			}
			
		}
	}
